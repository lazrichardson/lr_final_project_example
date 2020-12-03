package com.example.lrfinalproject.databases;

import java.io.File;
import java.io.IOException;

import com.example.lrfinalproject.ExecutionTimer;
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
import java.util.Timer;


public class XmlDocParse {

    public final ArrayList<Article> ARTICLES;

    public ArrayList<Article> bruteForceSearchResults;
    public ArrayList<Article> mongoSearchResults;
    public ArrayList<Article> luceneSearchResults;
    public ArrayList<Article> sqlSearchResults;

    public Long searchTime;

    private MongoDb mongoDb;
    private Lucene lucene;
    private MySql mySql;
    private ArrayList<ExecutionTimer> timers;


    public XmlDocParse() throws IOException {
        this.ARTICLES = new ArrayList<>();
        this.bruteForceSearchResults = new ArrayList<>();
        this.mongoSearchResults = new ArrayList<>();
        this.luceneSearchResults = new ArrayList<>();
        this.sqlSearchResults = new ArrayList<>();
        this.timers = new ArrayList<>();

        this.mongoDb = new MongoDb();
        this.lucene = new Lucene("/Users/luther/Downloads/demo 2/src/main/resources/luceneIndex");
        this.mySql = new MySql();
    }

    public ArrayList<ExecutionTimer> getTimers() {
        return timers;
    }

    public ArrayList<Article> getBruteForceSearchResults() {
        return bruteForceSearchResults;
    }


    public ArrayList<Article> getMongoSearchResults() {
        return mongoSearchResults;
    }

    public void addArticles() throws IOException, ParseException, SQLException {
        addArticlesToLucene();
        addArticlesToMongo();
        addArticlesToMysql();
    }


    public ArrayList<Article> getLuceneSearchResults() {
        return luceneSearchResults;
    }

    private void addArticlesToLucene() throws IOException {

        for (Article article : ARTICLES) {
            lucene.addDoc(article);
        }
        System.out.println("Added " + ARTICLES.size() + " to lucene");
        lucene.writer.close();
    }

    public void addArticlesToMongo() {

        for (Article article : ARTICLES) {
            mongoDb.addMongoDoc(article);
        }
        System.out.println("Added " + ARTICLES.size() + " to mongo");
    }

    public ArrayList<Article> getSqlSearchResults() {
        return sqlSearchResults;
    }

    public void addArticlesToMysql() {

        for (Article article : ARTICLES) {
            mySql.addRow(article);
        }
        System.out.println("Added " + ARTICLES.size() + " to mySql");
    }

    private void parse(File inputFile) throws ParserConfigurationException, IOException, SAXException {

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
                    // pull out the year from XML
                    StringBuilder builder = new StringBuilder();
                    String data = year.item(1).getTextContent();
                    builder.append(data);
                    // remove spaces from the date
                    String cleanYear = builder.toString().replace("  ", "");
                    cleanYear = cleanYear.replaceAll("[\\n\\t ]", "");
                    articleYear = cleanYear;
                }
                // add to the list of articles
                ARTICLES.add(new Article(articleTitle, articleYear));
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

    public ArrayList<Article> search(String database, String searchTerm, String startYear, String endYear) throws ParseException, SQLException, IOException {

        ArrayList<Article> result = new ArrayList<>();

        // clear out the old search results
        bruteForceSearchResults.clear();
        luceneSearchResults.clear();
        mongoSearchResults.clear();
        sqlSearchResults.clear();

        // set up the timers
        ExecutionTimer bruteForceTimer = new ExecutionTimer("bruteforce");
        ExecutionTimer luceneTimer = new ExecutionTimer("lucene");
        ExecutionTimer mongoTimer = new ExecutionTimer("mongo");
        ExecutionTimer sqlTimer = new ExecutionTimer("mysql");

        // run the searches

        // bruteforce
        bruteForceTimer.startTime();
        // search
        bruteForceSearch(searchTerm, startYear, endYear);
        bruteForceTimer.endTime();
        // set timer details
        bruteForceTimer.setSearchString(searchTerm, startYear, endYear);
        bruteForceTimer.setNumResults(bruteForceSearchResults.size());
        timers.add(bruteForceTimer);

        //lucene
        luceneTimer.startTime();
        // search
        luceneSearchResults = lucene.search(searchTerm, startYear, endYear);
        luceneTimer.endTime();
        // set timer details
        luceneTimer.setSearchString(searchTerm, startYear, endYear);
        luceneTimer.setNumResults(luceneSearchResults.size());
        timers.add(luceneTimer);


        // mongo
        mongoTimer.startTime();
        // search
        mongoSearchResults = mongoDb.mongoTermDateSearch(searchTerm, startYear, endYear);
        mongoTimer.endTime();
        // set timer details
        mongoTimer.setSearchString(searchTerm, startYear, endYear);
        mongoTimer.setNumResults(mongoSearchResults.size());
        timers.add(mongoTimer);


        // mysql
        sqlTimer.startTime();
        sqlSearchResults = mySql.searchTerm(searchTerm, startYear, endYear);
        sqlTimer.endTime();
        // set timer details
        sqlTimer.setSearchString(searchTerm, startYear, endYear);
        sqlTimer.setNumResults(sqlSearchResults.size());
        timers.add(sqlTimer);


        switch (database) {
            case "bruteforce":
                System.out.println("Bruteforce Results: " + bruteForceSearchResults.size());
                result = bruteForceSearchResults;
                break;
            case "lucene":
                System.out.println("Lucene Results: " + luceneSearchResults.size());
                result = luceneSearchResults;
                break;
            case "mongo":
                System.out.println("Mongo Results: " + mongoSearchResults.size());
                result = mongoSearchResults;
                break;
            case "mysql":
                System.out.println("MySql Results: " + sqlSearchResults.size());
                result = sqlSearchResults;
                break;
        }
        return result;
    }

    private void bruteForceSearch(String searchTerm, String fromDate, String toDate) {
        if (ARTICLES.size() > 0) {
            searchTerm = searchTerm.toLowerCase();
            for (Article article : ARTICLES) {
                String articleTitle = article.articleTitle.toLowerCase();

                if (articleTitle.contains(searchTerm)) {

                    int from = Integer.parseInt(fromDate);
                    int to = Integer.parseInt(toDate);

                    int articleDate = Integer.parseInt(article.articleYear);
                    if (articleDate >= from && articleDate <= to) {
                        bruteForceSearchResults.add(article);
                    }


                }
            }
        } else System.out.println("No items to search");
    }

    public void printSearchResults(ArrayList<Article> results) {
        for (Article article : results) {
            System.out.println("Title: " + article.getArticleTitle() + "\nYear: " + article.getArticleYear());
        }
    }
}