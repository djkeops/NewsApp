package com.example.android.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.LoaderManager.LoaderCallbacks;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<List<Article>>{

    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    /** URL for articles data from the content.guardianapis.com */
    private static final String GUARDIAN_REQUEST_URL = "https://content.guardianapis.com/search?section=technology&from-date=2018-05-24&show-tags=contributor&api-key=test";

    /** Constant value for the earthquake loader ID */
    private static final int ARTICLE_LOADER_ID = 1;

    /** Adapter for the list of earthquakes */
    private ArticleAdapter mAdapter;

    /** Empty state TextView */
    private TextView mEmptyStateTextView;

    /** Spinner progress bar */
    private ProgressBar loadingSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find the list view from the layout
        ListView articlesListView = findViewById(R.id.list);

        // Find and set the empty state view from the layout
        mEmptyStateTextView = findViewById(R.id.empty_view);
        articlesListView.setEmptyView(mEmptyStateTextView);

        // Find and set the progress bar from the layout
        loadingSpinner = findViewById(R.id.loading_spinner);

        // Create a new ArrayAdapter of articles
        mAdapter = new ArticleAdapter(this, new ArrayList<Article>());

        // Set the adapter on the ListView to populate the list of articles in the user interface
        articlesListView.setAdapter(mAdapter);

        // Set an item click listener on the ListView, to open a website with the article.
        articlesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current article that was clicked on
                Article currentArticle = mAdapter.getItem(position);

                // Convert the String URL from Article object into a URI object
                Uri articleUri = Uri.parse(currentArticle.getUrl());

                // Create a new ACTION_VIEW intent
                Intent webIntent = new Intent(Intent.ACTION_VIEW, articleUri);

                // Send the intent
                startActivity(webIntent);
            }
        });

        // Get a reference to the ConnectivityManager used to checking the state of network
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active data network
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        // If there is a network connection, fetch data
        if (isConnected) {
            // Get a reference to the LoaderManager to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader.
            loaderManager.initLoader(ARTICLE_LOADER_ID, null, this);
        } else {
            // Otherwise, display error
            // Hide loading indicator
            loadingSpinner.setVisibility(View.GONE);

            // Update empty state with no connection error message
            mEmptyStateTextView.setText(R.string.no_internet);
        }

    }

    @Override
    public Loader<List<Article>> onCreateLoader(int i, Bundle bundle) {

        // Create a new loader for the given URL
        return new ArticleLoader(this, GUARDIAN_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> articles) {

        // Hide loading indicator because the data has been loaded
        loadingSpinner.setVisibility(View.GONE);

        // Set empty state text to display "No news found."
        mEmptyStateTextView.setText(R.string.no_articles);

        // Clear the adapter of previous earthquake data
        mAdapter.clear();

        // If there is a valid list of articles, then add them to the adapter's data set.
        if (articles != null && !articles.isEmpty()) {
            mAdapter.addAll(articles);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Article>> loader) {
        // Clear the existing data.
        mAdapter.clear();
    }
}
