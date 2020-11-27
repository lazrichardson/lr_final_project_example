package com.example.lrfinalproject;

import com.example.lrfinalproject.databases.XmlDocParse;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

public class Runner {

    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException,
            SQLException, ParseException {

        String inputFiles = "/Users/luther/Downloads/demo 2/src/main/resources/Data";

        XmlDocParse docParse = new XmlDocParse();
        docParse.parseDirectory(inputFiles);
        docParse.addArticles();

        // == BruteForce ==
        System.out.println("BruteForce...");
        docParse.search("bruteforce", "cancer", "2017", "2017");
        docParse.printSearchResults(docParse.getBruteForceSearchResults());

        // == Lucene ==
        System.out.println("Lucene...");
        docParse.search("lucene", "cancer", "2016", "2018");
        docParse.printSearchResults(docParse.getLuceneSearchResults());

        /*
        // == MongoDB ==
        // Add the articles to Mongo
        docParse.addArticlesToMongo();

        //Write a range queries that search for a given keyword in a given from the start month to end month,
        // like from Jan. 2017 to Mar 2018..
        docParse.mongoTermDateSearch("fat", "2016-01-01", "2022-01-01");

        // ==  MYSQL  ==
        // Add the articles to the MYSQL database
        docParse.addArticlesToMysql();

        //Write a range queries that search for a given keyword in a given from the start month to end month,
        // like from Jan. 2017 to Mar 2018..
        docParse.sqlTermDateSearch("fat", "2016-01-01", "2022-01-01");

         */
    }
}
