package com.example.android.newsapp;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ArticleAdapter extends ArrayAdapter<Article> {

    /** Tag for the log messages */
    public static final String LOG_TAG = ArticleAdapter.class.getSimpleName();

    /**
     * Constructs a new ArticleAdapter
     * @param context of the app
     * @param articles is the list of articles
     */
    public ArticleAdapter(@NonNull Activity context, @NonNull ArrayList<Article> articles) {
        super(context, 0, articles);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;

        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        // Find the article at the given position in the list of articles
        Article currentArticle = getItem(position);

        // Find the TextView with view ID article_section
        TextView sectionTextView = listItemView.findViewById(R.id.article_section);
        // Display the section of the current article
        sectionTextView.setText(currentArticle.getSection());

        // Find the TextView with view ID article_date
        TextView dateTextView = listItemView.findViewById(R.id.article_date);
        // Format the date string (i.e. "Feb 2, 2018")
        String formattedDate = formatDate(currentArticle.getDate());
        // Display the formatted date of the current article
        dateTextView.setText(formattedDate);

        // Find the TextView with view ID article_title
        TextView titleTextView = listItemView.findViewById(R.id.article_title);
        // Display the title of the current article
        titleTextView.setText(currentArticle.getTitle());

        // Find the TextView with view ID article_contributor
        TextView contributorTextView = listItemView.findViewById(R.id.article_contributor);
        // Display the contributor of the current article
        contributorTextView.setText(currentArticle.getAuthor());

        return listItemView;
    }


    /**
     * Return the formatted date string (i.e. "Mar 3, 1984") from a Date object.
     */
    private String formatDate(String unformedDate) {
        SimpleDateFormat resultDateFormater = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        try {
            Date jsonDateToParse = resultDateFormater.parse(unformedDate);
            SimpleDateFormat appDateFormater = new SimpleDateFormat("LLL dd, yyyy");
            return appDateFormater.format(jsonDateToParse);
        } catch (ParseException e) {
            Log.e(LOG_TAG, "Problem with the data formater");
            return "";
        }
    }

}
