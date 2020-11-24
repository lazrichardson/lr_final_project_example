package com.example.lrfinalproject.databases;

public class Article {

    String articleTitle;
    String articleContents;
    String articleYear;

    public Article(String articleTitle, String articleContents, String articleYear) {
        this.articleTitle = articleTitle;
        this.articleContents = articleContents;
        this.articleYear = articleYear;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

    public String getArticleContents() {
        return articleContents;
    }

    public void setArticleContents(String articleContents) {
        this.articleContents = articleContents;
    }
}
