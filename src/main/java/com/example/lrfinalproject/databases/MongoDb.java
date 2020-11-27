package com.example.lrfinalproject.databases;

import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Accumulators;
import com.mysql.cj.xdevapi.DatabaseObject;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.regex.Pattern;

import static com.mongodb.client.model.Aggregates.group;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.set;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Filters;


import org.bson.Document;


public class MongoDb {

    MongoClient mongoClient;
    MongoDatabase mongoDatabase;
    String collectionName = "pubMedData";

    public MongoDb() {
        // initialize the client object + get the test db
        this.mongoClient = new MongoClient();
        this.mongoDatabase = mongoClient.getDatabase("test");

        //remove the collection of the same name if there is an existing one
        for (String name : mongoDatabase.listCollectionNames()) {
            if (name.equals(collectionName)) {
                mongoDatabase.getCollection(name).drop();
            }
        }
        // create a new collection
        mongoDatabase.createCollection(collectionName);
    }

    public void addMongoDoc(Article article) {
        // insert a new document into a collection
        Document doc = new Document();
        doc.put("title", article.articleTitle.toLowerCase());
        doc.put("year", Integer.parseInt(article.articleYear));
        mongoDatabase.getCollection(collectionName).insertOne(doc);
    }

    public ArrayList<Article> mongoTermDateSearch(String searchTerm, String fromDate, String toDate) throws ParseException {
        ArrayList<Document> results = new ArrayList<>();
        ArrayList<Article> returnResults = new ArrayList<>();

        String searchRegex = ".*" + searchTerm + ".*";

        int startDate = Integer.parseInt(fromDate);
        int endDate = Integer.parseInt(toDate);

        Bson match = and(eq("title", Pattern.compile(searchTerm)), and(lte("year", 2017L), gte("year", 0L)));
        // Bson match = and(regex("title", searchRegex), gte("year", startDate), lte("year", endDate));
        mongoDatabase.getCollection(collectionName).find(match).into(results);

        for (Document result : results) {
            Article article = new Article(result.get("title").toString(),
                    result.get("year").toString());
            System.out.println(result.toString());
            returnResults.add(article);
        }
        return returnResults;
    }

    /*
    public void mongoContainsTermRange(String searchTerm, String fromDate, String toDate) throws ParseException {

        ArrayList<Document> results = new ArrayList<>();
        String searchRegex = ".*" + searchTerm + ".*";

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = simpleDateFormat.parse(fromDate);
        Date endDate = simpleDateFormat.parse(toDate);

        Bson match = and(regex("title", searchRegex), gte("date", startDate), lte("date", endDate));

        // add a count of the occurrence of the search term
        for (Document doc : mongoDatabase.getCollection(collectionName).find(match)) {

            String title = (String) doc.get("title");
            int count = title.split(searchTerm, -1).length - 1;

            if (count > 0) {
                mongoDatabase.getCollection(collectionName).updateOne(eq("title", doc.get("title")), set("count", count));
            }
        }

        System.out.println("Aggregate: ");

        mongoDatabase.getCollection(collectionName).aggregate(
                Arrays.asList(
                        Aggregates.match(Filters.exists("count")),
                        Aggregates.group("$year", Accumulators.sum("count", "$count"))
                )
        ).into(results);

        for (Document result : results) {
            System.out.println(result.toString());
        }

    }

     */
}
