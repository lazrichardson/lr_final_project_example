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

        // == Lucene ==
        System.out.println("Lucene...");
        docParse.search("lucene", "cancer", "2017", "2017");
        docParse.printSearchResults(docParse.getLuceneSearchResults());

        // == BruteForce ==
        System.out.println("BruteForce...");
        docParse.search("bruteforce", "cancer", "2017", "2018");
        docParse.printSearchResults(docParse.getBruteForceSearchResults());

        // == MongoDB ==
        System.out.println("Mongo...");
        docParse.search("mongo", "cancer", "2017", "2018");
        docParse.printSearchResults(docParse.getMongoSearchResults());

        // ==  MYSQL  ==
        docParse.search("mysql", "cancer", "2017", "2018");
        docParse.printSearchResults(docParse.getSqlSearchResults());
    }


}
