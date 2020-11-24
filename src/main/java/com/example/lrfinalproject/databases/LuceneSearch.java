package com.example.lrfinalproject.databases;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

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

public class LuceneSearch {
    public void search(String searchTerm, String indexDirectory, XmlDocParse xmlDocParse) {
        try {
// 0. Specify the analyzer for tokenizing text. The same analyzer should be used for indexing and searching
            StandardAnalyzer analyzer = new StandardAnalyzer();
// 1. create the index
            FSDirectory index = FSDirectory.open(Paths.get(indexDirectory));
            IndexWriterConfig config = new IndexWriterConfig(analyzer);
            IndexWriter w = new IndexWriter(index, config);

            for (Article article : xmlDocParse.articles) {
                addDoc(w, article.articleTitle, article.articleContents);
                // System.out.println("Added Doc");
            }

            w.close();
            //  System.out.println("Index Created");
// 2. query
            // the "title" arg specifies the default field to use
            // when no field is explicitly specified in the query.
            Query q = new QueryParser("title", analyzer).parse(searchTerm);
            //  System.out.println("Query Parser Created");
// 3. search
            int hitsPerPage = 1000000000;
            IndexReader reader = DirectoryReader.open(index);
            IndexSearcher searcher = new IndexSearcher(reader);

            long startTime = System.nanoTime();
            TopDocs docs = searcher.search(q, hitsPerPage);
            ScoreDoc[] hits = docs.scoreDocs;
            long endTime = System.nanoTime();
            long searchTime = endTime - startTime;
            searchTime = searchTime / 1000; // convert to microseconds
// 4. display results
            System.out.println("Found " + hits.length + " hits.");
            System.out.println("Search Time (microseconds): " + searchTime);
            for (int i = 0; i < hits.length; ++i) {
                int docId = hits[i].doc;
                Document d = searcher.doc(docId);

               /*
                System.out.println((i + 1) + ". Title: " + d.get("title")
                        //        + "\ncontent: " + d.get("contents"
                );
            */
            }
            reader.close();

            File[] indexCleanup = new File("lucene_index/").listFiles();

            // clean up the index for the next run
            for (File file : indexCleanup) {
                file.delete();
            }

        } catch (IOException | ParseException ex) {
            ex.printStackTrace();
        }
    }

    private static void addDoc(IndexWriter w, String title, String contents) throws
            IOException {

        Document doc = new Document();
        doc.add(new TextField("title", title, Field.Store.YES));
        doc.add(new TextField("contents", contents, Field.Store.YES));
        w.addDocument(doc);
    }
    // System.out.println("Finished Adding Articles.....");
}


