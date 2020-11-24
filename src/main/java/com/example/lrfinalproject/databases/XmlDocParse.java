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


    public XmlDocParse() {
        this.articles = new ArrayList<>();
        this.searchResults = new ArrayList<>();
        this.searchTime = null;
    }

    public ArrayList<Article> articles;
    public ArrayList<Article> searchResults;
    public Long searchTime;
    public MongoDb mongoDb;

    public void addArticlesToMongo() throws ParseException {

        mongoDb = new MongoDb();

        for (Article article : articles) {
            mongoDb.addMongoDoc(article);
        }
        System.out.println("Rows Added: " + articles.size());
    }

    public void addArticlesToMysql() throws SQLException {
        JdbcUtil jdbc = new JdbcUtil("jdbc:mysql://localhost:3306/cs622?useTimezone=true&serverTimezone=UTC", "luther", "Ilovebaobei!");
        String tableName = "PUB_MED_ARTICLES";

        jdbc.createTable(tableName, "ARTICLE_TITLE", "ARTICLE_YEAR");
        for (Article article : articles) {
            String cleanTitle = article.articleTitle.replace("'", " ").toLowerCase();
            jdbc.insert(tableName, cleanTitle, article.articleYear.toLowerCase());
        }
        System.out.println("Rows Added: " + articles.size());
    }

    public void sqlTermDateSearch(String term, String startDate, String endDate) throws SQLException {
        JdbcUtil jdbc = new JdbcUtil("jdbc:mysql://localhost:3306/cs622?useTimezone=true&serverTimezone=UTC", "luther", "Ilovebaobei!");
        jdbc.searchTerm(term, startDate, endDate);
    }

    public void sqlContainsTermRange(String term, String startDate, String endDate) throws SQLException {
        JdbcUtil jdbc = new JdbcUtil("jdbc:mysql://localhost:3306/cs622?useTimezone=true&serverTimezone=UTC", "luther", "Ilovebaobei!");
        jdbc.searchRange(term, startDate, endDate);
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

            String articleAbstract = "";
            NodeList abstractContents = elem.getElementsByTagName("Abstract");
            if (abstractContents.getLength() > 0) {
                StringBuilder builder = new StringBuilder();
                for (int j = 0; j < abstractContents.getLength(); j++) {
                    String data = abstractContents.item(j).getTextContent();
                    builder.append(data);
                }
                articleAbstract = builder.toString();
            }

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
                articles.add(new Article(articleTitle, articleAbstract, articleYear));
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

    public void bruteForceSearch(String searchTerm) {
        if (articles.size() > 0) {
            searchTerm = searchTerm.toLowerCase();
            long startTime = System.nanoTime();
            for (Article article : articles) {
                String articleTitle = article.articleTitle.toLowerCase();
                //String articleContents = article.getArticleContents().toLowerCase();

                if (articleTitle.contains(searchTerm)) {
                    searchResults.add(article);
                }
            }
            long endTime = System.nanoTime();
            long totalTime = endTime - startTime;
            searchTime = totalTime / 1000; // convert to microseconds

        } else System.out.println("No items to search");
    }

    public void printSearchResults() {
        System.out.println("Hits found: " + searchResults.size());
        System.out.println("Search Time (microseconds): " + searchTime);



        /*
         int i = 0;
        for (com.example.lrfinalproject.databases.Article article : searchResults) {
            i++;
            System.out.println(i + ". Title: " + article.articleTitle);
        }
         */


    }


    /*
    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException {
        com.example.lrfinalproject.databases.XmlDocParse search = new com.example.lrfinalproject.databases.XmlDocParse();
        search.parse("com.example.lrfinalproject.databases.Article", new File("Data/pubmed20n1016.xml"));

        for (com.example.lrfinalproject.databases.Article article : search.articles) {
            System.out.println(article.articleTitle + "\n" + article.articleContents + "\n");
        }
    }

     */
}