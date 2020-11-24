package com.example.lrfinalproject;

import com.example.lrfinalproject.databases.XmlDocParse;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

public class Runner {

    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException, SQLException, ParseException {

        String inputFiles = "Data/";

        XmlDocParse docParse = new XmlDocParse();
        docParse.parseDirectory(inputFiles);

        // == BruteForce ==
        // todo implement bruteforce => use searchquery class

        // == Lucene ==
        // todo implement lucene

        // == MongoDB ==
        // Add the articles to Mongo
        docParse.addArticlesToMongo();

        //Write a range queries that search for a given keyword in a given from the start month to end month, like from Jan. 2017 to Mar 2018..
        docParse.mongoDb.mongoTermDateSearch("fat", "2016-01-01", "2022-01-01");

        // Write a query to count the number of given keywords per year, e.g. “flu”, “obesity" keywords, for at least three years.
        docParse.mongoDb.mongoContainsTermRange("fat", "2016-01-01", "2022-01-01");

        // ==  MYSQL  ==
        // Add the articles to the MYSQL database
        docParse.addArticlesToMysql();

        //Write a range queries that search for a given keyword in a given from the start month to end month, like from Jan. 2017 to Mar 2018..
        docParse.sqlTermDateSearch("fat", "2016-01-01", "2022-01-01");

        // Write a query to count the number of given keywords per year, e.g. “flu”, “obesity" keywords, for at least three years.
        docParse.sqlContainsTermRange("fat", "2016-02-01", "2022-01-01");
    }
}
