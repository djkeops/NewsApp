package com.example.android.newsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Loads a list of articles by using an AsyncTask to perform the network request to the given URL.
 */
public class ArticleLoader extends AsyncTaskLoader<List<Article>> {

    /**
     * Query URL
     */
    private String mUrl;

    /**
     * Constructs a new ArticleLoader
     *
     * @param context of the activity
     * @param url     to load data from it
     */
    public ArticleLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * Runs on a background thread.
     */
    @Override
    public List<Article> loadInBackground() {

        // Don't perform the request if url is null
        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, extract and return a list of articles.
        List<Article> articles = QueryUtils.fetchArticlesData(mUrl);
        return articles;
    }
}
