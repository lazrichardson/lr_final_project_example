package com.example.lrfinalproject.databases;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

public class Lucene {

    StandardAnalyzer analyzer;
    FSDirectory index;
    IndexWriterConfig config;
    IndexWriter writer;

    public Lucene(String indexDirectory) throws IOException {
        // Specify the analyzer for tokenizing text.
        analyzer = new StandardAnalyzer();

        // Create the index
        index = FSDirectory.open(Paths.get(indexDirectory));
        config = new IndexWriterConfig(analyzer);
        writer = new IndexWriter(index, config);
    }

    public ArrayList<Article> search(String searchTerm) throws IOException, ParseException {
        ArrayList<Article> results = new ArrayList<>();
        // Query
        // "title" arg specifies the default field to us when no field is explicitly specified in the query.
        Query q = new QueryParser("title", analyzer).parse(searchTerm);
        // Search
        int hitsPerPage = 1000000000;
        IndexReader reader = DirectoryReader.open(index);
        IndexSearcher searcher = new IndexSearcher(reader);
        TopDocs docs = searcher.search(q, hitsPerPage);
        // Add results
        ScoreDoc[] hits = docs.scoreDocs;

        for (int i = 0; i < hits.length; ++i) {
            int docId = hits[i].doc;
            Document d = searcher.doc(docId);
            Article article = new Article(d.get("title"), d.get("year"));
            results.add(article);
        }
        reader.close();
        return results;
    }

    public void addDoc(String title, String year) throws IOException {
        Document doc = new Document();
        doc.add(new TextField("title", title, Field.Store.YES));
        doc.add(new TextField("year", year, Field.Store.YES));
        writer.addDocument(doc);
    }

    public void indexCleanup() {
        File[] indexCleanup = new File("lucene_index/").listFiles();
        // clean up the index for the next run
        for (File file : indexCleanup) {
            file.delete();
        }
    }
}


