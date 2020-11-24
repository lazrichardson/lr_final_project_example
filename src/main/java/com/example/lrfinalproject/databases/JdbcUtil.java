package com.example.lrfinalproject.databases;

import java.sql.*;

public class JdbcUtil {

    String connectionUrl;
    String user;
    String password;
    String driver = "com.mysql.cj.jdbc.Driver";

    public JdbcUtil(String url, String userName, String userPassword) throws SQLException {
        this.connectionUrl = url; //"jdbc:mysql://localhost:3306/cs622?useTimezone=true&serverTimezone=UTC"
        this.user = userName;
        this.password = userPassword;
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

    private void executeQuery(String query, String type) {
        try {
            Class.forName(driver);
            Connection con = DriverManager.getConnection(connectionUrl, user, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if (type.equals("termDate")) {
                readTermDateResultSet(rs);
            } else {
                readTermRangeResultSet(rs);
            }
            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void readTermDateResultSet(ResultSet rs) throws SQLException {
        System.out.println("ARTICLE_TITLE        |        ARTICLE_YEAR");
        while (rs.next()) {
            String title = rs.getString("ARTICLE_TITLE");
            Date date = rs.getDate("ARTICLE_YEAR");
            String dateString = date.toString();
            String output = title + "        | " + dateString;
            System.out.println(output);
        }
    }

    public void readTermRangeResultSet(ResultSet rs) throws SQLException {
        System.out.println("Total occurrences of search term in time period, by year");
        System.out.println("Year        |        Count");
        while (rs.next()) {
            String count = rs.getString("count");
            String year = rs.getString("year");

            System.out.println(year+"        |        "+count);
        }
    }

    // SQL Create Table with JDBC
    public void createTable(String tableName, String col1, String col2) {
        //query structure ==> CREATE TABLE table_name(column_name column_type);

        // drop the table if it already exists
        String query = "DROP TABLE IF EXISTS " + tableName;
        executeUpdate(query);

        // create the table
        query = "CREATE TABLE IF NOT EXISTS " + tableName + "("
                + col1 + " VARCHAR(6000),"
                + col2 + " DATE"
                + ")";

        executeUpdate(query);
    }

    // SQL Insert with JDBC
    public void insert(String table, String title, String articleAbstract) {

        String query = "INSERT INTO " + table + "(ARTICLE_TITLE,ARTICLE_YEAR)"
                + "VALUES(" + "'" + title + "'," + "'" + articleAbstract + "')";
        executeUpdate(query);
    }

    // SQL Delete with JDBC
    private void delete(String name, String owner) {

        String query = "DELETE FROM pet WHERE name='" + name + "' AND owner='" + owner + "'";
        executeUpdate(query);
    }

    public void searchTerm(String searchTerm, String startDate, String endDate) throws SQLException {
        // Example Query:
        // SELECT cs622.PUB_MED_ARTICLES.ARTICLE_TITLE from cs622.PUB_MED_ARTICLES
        // WHERE ARTICLE_TITLE LIKE '%FLU%'
        // AND ARTICLE_YEAR BETWEEN '2019-02-01' AND '2020-01-01' ;

        String query = "SELECT * FROM PUB_MED_ARTICLES WHERE ARTICLE_TITLE LIKE '%"
                + searchTerm + "%' AND ARTICLE_YEAR BETWEEN '" + startDate + "' AND '" + endDate + "'";

        executeQuery(query, "termDate");

    }

    public void searchRange(String searchTerm, String startDate, String endDate) throws SQLException {
        // Write a query to count the number of given keywords per year, e.g. “flu”, “obesity" keywords, for at least three years.
/*      Example Query:

        select
        sum(
            case when length(ARTICLE_TITLE) > length(replace(ARTICLE_TITLE, 'heart',''))
                then (length(ARTICLE_TITLE) - length(replace(ARTICLE_TITLE, 'heart',''))) / length('heart')
            else
                0
            end
            )
        from cs622.PUB_MED_ARTICLES;
 */

        String query = "select year(ARTICLE_YEAR) as year, sum(" +
                "case when length(ARTICLE_TITLE) > length(replace(lower(ARTICLE_TITLE), '" + searchTerm + "','')) " +
                "then (length(ARTICLE_TITLE) - length(replace(lower(ARTICLE_TITLE), '" + searchTerm + "',''))) / length('" + searchTerm + "') " +
                "ELSE 0 END" +
                ") as count FROM PUB_MED_ARTICLES WHERE ARTICLE_YEAR BETWEEN '" + startDate + "' AND '" + endDate +
                "' group by year(ARTICLE_YEAR) order by year(ARTICLE_YEAR)";
        executeQuery(query, "");

    }
}