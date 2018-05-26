package com.example.android.newsapp;

public class Article {

    /** The title of the article */
    private String mTitle;

    /** The section of the article */
    private String mSection;

    /** The date of the article */
    private String mDate;

    /** The author/contributor of the article */
    private String mAuthor;

    /** Website URL of the article */
    private String mUrl;

    /**
     * Constructs a new Article object
     *
     * @param title is the title of the article
     * @param section is the section of the article
     * @param date is the date of the article
     * @param author is the author/contributor of the article
     * @param url is the website url of the article
     */
    public Article (String title, String section, String date, String author, String url) {
        mTitle = title;
        mSection = section;
        mDate = date;
        mAuthor = author;
        mUrl = url;
    }

    /**
     * Returns the title of the article.
     */
    public String getTitle() {
        return mTitle;
    }

    /**
     * Returns the section of the article.
     */
    public String getSection() {
        return mSection;
    }

    /**
     * Returns the date of the article.
     */
    public String getDate() {
        return mDate;
    }

    /**
     * Returns the author/contributor of the article.
     */
    public String getAuthor() {
        return mAuthor;
    }

    /**
     * Returns the website url of the article.
     */
    public String getUrl() {
        return mUrl;
    }
}
