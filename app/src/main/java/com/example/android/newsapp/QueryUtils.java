package com.example.android.newsapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public final class QueryUtils {

    /**
     * Tag for the log messages
     */
    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * A private black constructor
     */
    private QueryUtils() {
    }

    /**
     * Returns an URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    /**
     * Convert the InputStream into a String which contains the JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();

        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return empty jsonResponse
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200), then read the input stream and parse the response
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Response with error code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the articles JSON result.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static List<Article> extractResultsFromJson(String articlesJson) {

        // If the JSON string is empty or null, return null
        if (TextUtils.isEmpty(articlesJson)) {
            return null;
        }

        // Create an empty ArrayList
        List<Article> articles = new ArrayList<>();


        // Parse the JSON response string.
        // If there's a problem with the way the JSON is formatted, a JSONException exception object will be thrown and printed to the logs.
        try {
            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(articlesJson);
            // Extract the JSONObject with the key "response"
            JSONObject jsonResponse = baseJsonResponse.getJSONObject("response");
            // Extract the JSONArray with the key "results" that contains the articles
            JSONArray articlesArray = jsonResponse.getJSONArray("results");

            // For each article in the articlesArray, create an Article object
            for (int i = 0; i < articlesArray.length(); i++) {

                // Get a single article at position i within the array of articles
                JSONObject currentArticle = articlesArray.getJSONObject(i);

                // Extract the article section with the key "sectionName"
                String articleSection = currentArticle.getString("sectionName");

                // Extract the article date with the key "webPublicationDate"
                String articleDate = currentArticle.getString("webPublicationDate");

                // Extract the article url with the key "webUrl"
                String articleUrl = currentArticle.getString("webUrl");

                // Extract the article title with the key "webTitle"
                String articleTitle = currentArticle.getString("webTitle");

                // Extract the JSONArray with the key "tags" that contains the contributors
                JSONArray tagsArray = currentArticle.getJSONArray("tags");

                // A string that contains the list of contributors if are available
                String articleContributors;

                // If the contributors are not available return null
                if (tagsArray.length() == 0) {
                    articleContributors = null;
                } else {
                    StringBuilder contributorsStringBuilder = new StringBuilder();

                    // For each contributor get the title with the key "webTitle"
                    for (int j = 0; j < tagsArray.length(); j++) {
                        JSONObject currentTag = tagsArray.getJSONObject(j);
                        if (j == tagsArray.length() - 1) {
                            contributorsStringBuilder.append(currentTag.getString("webTitle"));
                        } else {
                            contributorsStringBuilder.append(currentTag.getString("webTitle")).append(" & ");
                        }
                    }
                    articleContributors = contributorsStringBuilder.toString();
                }

                // Create a new Article object with the values from the JSON response.
                Article article = new Article(articleTitle, articleSection, articleDate, articleContributors, articleUrl);

                // Add article to the list of articles
                articles.add(article);
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the JSON results", e);
        }

        // Return the list of articles
        return articles;
    }

    /**
     * Query the content.guardianapis.com and return a list of Article objects.
     */
    public static List<Article> fetchArticlesData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response
        String jsonResponse = null;

        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem with the HTTP request", e);
        }

        // Extract relevant fields from the JSON response and create a list of Article objects
        List<Article> articles = extractResultsFromJson(jsonResponse);

        // Return the list of Article objects
        return articles;
    }

}
