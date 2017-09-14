package com.example.android.developerinfo;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by USER on 8/20/2017.
 */

public final class DeveloperUtils {
    // Tag for the log messages
    public static final String LOG_TAG = DeveloperUtils.class.getSimpleName();

    /**
     * Create a private constructor because no one should ever create a {@link DeveloperUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name DeveloperUtils (and an object instance of DeveloperUtils is not needed).
     */
    private DeveloperUtils() {
    }

    /**
     * Query the github database and return a list of {@link Developer} objects that has been built up from
     * parsing a JSON response.
     */
    //calls on a private helper method (getJsonString) and finally returns a list of developer objects from the  JSON object gotten from the internet
    public static List<Developer> getDevelopersData(String urlString) {
        // query the github dataabse and return a raw json string
        String jsonString = getJsonString(urlString);
        //return early if json string is empty_text or null
        if (TextUtils.isEmpty(jsonString)) {
            return null;
        }
        // Create an empty_text ArrayList that we can start adding developers to
        List<Developer> developers = new ArrayList<>();


        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            JSONObject baseJsonResponse = new JSONObject(jsonString);
            JSONArray itemsArray = baseJsonResponse.optJSONArray("items");
            for (int i = 0; i < itemsArray.length(); i++) {
                JSONObject singleDeveloperDetails = itemsArray.optJSONObject(i);
                String name = singleDeveloperDetails.optString("login");
                String imageUrl = singleDeveloperDetails.optString("avatar_url");
                String url = singleDeveloperDetails.optString("html_url");
                developers.add(new Developer(name, imageUrl, url));
            }


        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the developers JSON results", e);
        }

        // Return the list of developers
        return developers;
    }

    //takes in as input a string which points to the Url and returns the processed JSON result
    private static String getJsonString(String USGS) {
        //create URL object
        URL url = makeUrl(USGS);
        //perform HTTP request to the URL and receive a JSON response back
        String jsonOutputstring = null;
        try {
            jsonOutputstring = getResponse(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "error closing input stream", e);
        }
        // return the json response
        return jsonOutputstring;
    }

    //converts the String to a Url object
    private static URL makeUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException ex) {
            Log.e(LOG_TAG, "ERROR CREATING URL: ", ex);
        }
        return url;
    }

    // opens up an internet connection
    //use helper 'readStream' method to decode output into a String containing the raw json
    //return the json as a string
    private static String getResponse(URL url) throws IOException {

        String jsonResponse = "";
        // return early if url is null
        if (url == null) {
            return jsonResponse;
        }
        InputStream inputStream = null;
        HttpsURLConnection httpsURLConnection = null;

        try {
            httpsURLConnection = (HttpsURLConnection) url.openConnection();
            httpsURLConnection.setReadTimeout(100000);
            httpsURLConnection.setConnectTimeout(150000);
            httpsURLConnection.setRequestMethod("GET");
            httpsURLConnection.connect();
            //read input stream and parse response if request was successful(response code 200)
            if (httpsURLConnection.getResponseCode() == 200) {
                inputStream = httpsURLConnection.getInputStream();
                jsonResponse = readStream(inputStream);
            } else {
                Log.e(LOG_TAG, "ERROR RESPONSE CODE :" + httpsURLConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "problem retrieving developer json results", e);
        } finally {
            if (httpsURLConnection != null) {
                httpsURLConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    //converts the input stream from the internet connection to a string containing the whole unmodified json response
    private static String readStream(InputStream inputStream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while (line != null) {
                stringBuilder.append(line);
                line = bufferedReader.readLine();
            }
        }
        return stringBuilder.toString();
    }


}



