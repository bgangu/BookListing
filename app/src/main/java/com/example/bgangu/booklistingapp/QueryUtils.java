package com.example.bgangu.booklistingapp;

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

public class QueryUtils {
    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {
    }

    /**
     * Returns new URL object from the given string URL.
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

    public static ArrayList<Books> fetchBooks(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        // Extract relevant fields from the JSON response and create an {@link Event} object
        ArrayList<Books> books = extractBooks(jsonResponse);

        return books;
    }

    /**
     * Return a list of {@link Books} objects that has been built up from
     * parsing a JSON response.
     */
    public static ArrayList<Books> extractBooks(String jsonResponse) {

        // Create an empty ArrayList that we can start adding books to
        ArrayList<Books> books = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // TODO: Parse the response given by the SAMPLE_JSON_RESPONSE string and
            // build up a list of Books objects with the corresponding data.

            JSONObject jsonResponseObject = new JSONObject(jsonResponse);

            //Get the instance of JSONArray that contains JSONObjects
            JSONArray booksArray = jsonResponseObject.optJSONArray("items");
            if (booksArray == null) {
                return null;
            }
            //Iterate the jsonArray and print the info of JSONObjects
            for (int i = 0; i < booksArray.length(); i++) {
                JSONObject currentBook = booksArray.getJSONObject(i);
                JSONObject volumeInfoObject = currentBook.getJSONObject("volumeInfo");
                JSONArray authorsArray = volumeInfoObject.optJSONArray("authors");

                String title;
                if (volumeInfoObject.getString("title") == null) {
                    title = "Unknown";
                } else {
                    title = volumeInfoObject.getString("title");
                }

                String author;
                if (authorsArray == null || authorsArray.length() < 0) {
                    author = "Unknown";
                } else {
                    author = authorsArray.getString(0);
                }

                String publishedDate;
                if (volumeInfoObject.getString("publishedDate") == null) {
                    publishedDate = "Unknown";
                } else {
                    publishedDate = volumeInfoObject.getString("publishedDate");
                }

                Books book = new Books(title, author, publishedDate);
                books.add(book);
            }

        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem in parsing the JSON results", e);
        }

        return books;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
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

}
