/*
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
        http://localhost:8081/query?type=lucene&start=2017&end=2017&term=cancer

        // == BruteForce ==
        System.out.println("BruteForce...");
        docParse.search("bruteforce", "cancer", "2017", "2018");
        docParse.printSearchResults(docParse.getBruteForceSearchResults());
        http://localhost:8081/query?type=bruteforce&start=2017&end=2017&term=cancer

        // == MongoDB ==
        System.out.println("Mongo...");
        docParse.search("mongo", "cancer", "2017", "2018");
        docParse.printSearchResults(docParse.getMongoSearchResults());
        http://localhost:8081/query?type=mongo&start=2017&end=2017&term=cancer

        // ==  MYSQL  ==
        docParse.search("mysql", "cancer", "2017", "2018");
        docParse.printSearchResults(docParse.getSqlSearchResults());
        http://localhost:8081/query?type=mysql&start=2017&end=2017&term=cancer

    }


}


 */