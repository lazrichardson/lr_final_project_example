package com.example.lrfinalproject.databases;

import java.sql.*;
import java.util.ArrayList;

public class MySql {

    String connectionUrl = "jdbc:mysql://localhost:3306/cs622?useTimezone=true&serverTimezone=UTC";
    String user = "luther";
    String password = "Ilovebaobei!";
    String driver = "com.mysql.cj.jdbc.Driver";
    String tableName = "PUB_MED_ARTICLES";

    public MySql() {
        createTable("ARTICLE_TITLE", "ARTICLE_YEAR");
    }

    private void executeUpdate(String query) {
        try {
            Class.forName(driver);
            Connection con = DriverManager.getConnection(connectionUrl, user, password);
            Statement stmt = con.createStatement();
            stmt.executeUpdate(query);
            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private ArrayList<Article> executeQuery(String query) {
        ArrayList<Article> results = new ArrayList<>();
        try {
            Class.forName(driver);
            Connection con = DriverManager.getConnection(connectionUrl, user, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            results = readTermDateResultSet(rs);
            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return results;
    }

    public ArrayList<Article> readTermDateResultSet(ResultSet rs) throws SQLException {
        ArrayList<Article> results = new ArrayList<>();
        while (rs.next()) {
            String title = rs.getString("ARTICLE_TITLE");
            int date = rs.getInt("ARTICLE_YEAR");
            String dateString = Integer.toString(date);
            results.add(new Article(title, dateString));
        }
        return results;
    }

    public void addRow(Article article) {
        String cleanTitle = article.articleTitle.replace("'", " ").toLowerCase();
        insert(tableName, cleanTitle, article.articleYear.toLowerCase());
    }

    // SQL Create Table with JDBC
    public void createTable(String col1, String col2) {
        //query structure ==> CREATE TABLE table_name(column_name column_type);

        // drop the table if it already exists
        String query = "DROP TABLE IF EXISTS " + tableName;
        executeUpdate(query);

        // create the table
        query = "CREATE TABLE IF NOT EXISTS " + tableName + "("
                + col1 + " VARCHAR(6000),"
                + col2 + " INTEGER"
                + ")";

        executeUpdate(query);
    }

    // SQL Insert with JDBC
    public void insert(String table, String title, String articleAbstract) {

        String query = "INSERT INTO " + table + "(ARTICLE_TITLE,ARTICLE_YEAR)"
                + "VALUES(" + "'" + title + "'," + "'" + articleAbstract + "')";
        executeUpdate(query);
    }


    public ArrayList<Article> searchTerm(String searchTerm, String startDate, String endDate) throws SQLException {
        // Example Query:
        // SELECT cs622.PUB_MED_ARTICLES.ARTICLE_TITLE from cs622.PUB_MED_ARTICLES
        // WHERE ARTICLE_TITLE LIKE '%FLU%'
        // AND ARTICLE_YEAR BETWEEN '2019-02-01' AND '2020-01-01' ;

        String query = "SELECT * FROM PUB_MED_ARTICLES WHERE ARTICLE_TITLE LIKE '%"
                + searchTerm + "%' AND ARTICLE_YEAR BETWEEN '" + startDate + "' AND '" + endDate + "'";

        return executeQuery(query);
    }
}