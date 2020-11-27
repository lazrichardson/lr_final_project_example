package com.example.lrfinalproject.databases;

import java.io.File;
import java.io.IOException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;


public class XmlDocParse {

    public ArrayList<Article> articles;
    public ArrayList<Article> bruteForceSearchResults;
    public ArrayList<Article> mongoSearchResults;
    public ArrayList<Article> luceneSearchResults;
    public ArrayList<Article> sqlSearchResults;

    public Long searchTime;

    private MongoDb mongoDb;
    private Lucene lucene;
    private MySql mySql;

    final private String indexDirectory = "src/main/java/luceneIndex";


    public XmlDocParse() {
        this.articles = new ArrayList<>();
        this.bruteForceSearchResults = new ArrayList<>();
        this.mongoSearchResults = new ArrayList<>();
        this.luceneSearchResults = new ArrayList<>();
        this.sqlSearchResults = new ArrayList<>();

        this.searchTime = null;
    }


    public void addArticlesToLucene() throws IOException {

        lucene = new Lucene(indexDirectory);

        for (Article article : articles) {
            lucene.addDoc(article);
        }
        lucene.writer.close();
    }

    public void addArticlesToMongo() throws ParseException {

        mongoDb = new MongoDb();

        for (Article article : articles) {
            mongoDb.addMongoDoc(article);
        }
        System.out.println("Rows Added: " + articles.size());
    }

    public void addArticlesToMysql() throws SQLException {
        MySql mySql = new MySql();

        for (Article article : articles) {
            mySql.addRow(article);
        }
        System.out.println("Rows Added: " + articles.size());
    }

    public void parse(File inputFile) throws ParserConfigurationException, IOException, SAXException {

        String tagName = "PubmedArticle";

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(inputFile);
        NodeList nodeList = document.getElementsByTagName(tagName);

        for (int i = 0; i < nodeList.getLength(); i++) {

            // get the node
            Node node = nodeList.item(i);
            // cast to element to get the tags
            Element elem = (Element) node;
            // find and print the title
            NodeList title = elem.getElementsByTagName("ArticleTitle");
            String articleTitle = title.item(0).getTextContent();

            String articleYear = "";
            NodeList year = elem.getElementsByTagName("ArticleDate");
            Element dateParts = (Element) year.item(0);
            if (dateParts != null) {
                year = dateParts.getChildNodes();
                if (year.getLength() > 0) {
                    StringBuilder builder = new StringBuilder();
                    for (int j = 0; j < year.getLength(); j++) {
                        String data = year.item(j).getTextContent();
                        if (j == 1 || j == 3) {
                            String dataFormatted = data + "-";
                            builder.append(dataFormatted);
                        } else {
                            builder.append(data);
                        }
                    }
                    // remove spaces from the date
                    String cleanYear = builder.toString().replace("  ", "");
                    cleanYear = cleanYear.replaceAll("[\\n\\t ]", "");
                    articleYear = cleanYear;
                }
                // add to the list of articles
                articles.add(new Article(articleTitle, articleYear));
            }
        }
    }

    public void parseDirectory(String inputFiles) throws IOException, SAXException, ParserConfigurationException {

        File[] files = new File(inputFiles).listFiles();

        assert files != null;
        for (File file : files) {
            String rootFileExtension = file.getPath().substring(file.getPath().length() - 4, file.getPath().length());
            // if it is XML, then add
            if (rootFileExtension.equals(".xml")) {
                parse(file);
                //  System.out.println("Added Doc");
            }
        }
    }

    public void search(String database, String searchTerm, String startYear, String endYear) throws ParseException, SQLException {
        mongoDb.mongoTermDateSearch(searchTerm, startYear, endYear);
        luceneSearchResults = lucene.search(searchTerm, startYear, endYear);
        mySql.searchRange(searchTerm, startYear, endYear);
        bruteForceSearch(searchTerm, startYear, endYear);
    }

    public void bruteForceSearch(String searchTerm, String fromDate, String toDate) {
        if (articles.size() > 0) {
            searchTerm = searchTerm.toLowerCase();
            long startTime = System.nanoTime();
            for (Article article : articles) {
                String articleTitle = article.articleTitle.toLowerCase();

                if (articleTitle.contains(searchTerm)) {

                    int from = Integer.parseInt(fromDate);
                    int to = Integer.parseInt(toDate);

                    int articleDate = Integer.parseInt(article.articleYear);

                    if (articleDate >= from && articleDate <= to) {
                        // todo: test date filtering
                        bruteForceSearchResults.add(article);
                    }
                }
            }
            long endTime = System.nanoTime();
            long totalTime = endTime - startTime;
            searchTime = totalTime / 1000; // convert to microseconds

        } else System.out.println("No items to search");
    }

    public void printSearchResults() {
        System.out.println("Hits found: " + bruteForceSearchResults.size());
        System.out.println("Search Time (microseconds): " + searchTime);
    }
}