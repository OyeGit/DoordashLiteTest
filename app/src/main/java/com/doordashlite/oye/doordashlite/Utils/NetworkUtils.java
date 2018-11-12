package com.doordashlite.oye.doordashlite.Utils;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String SEARCH_ARTICLE_URL =
            "https://api.doordash.com/v2/restaurant/";
    private final static String LAT_PARAM = "lat";
    private final static String LNG_PARAM = "lng";
    private final static String OFFSET_PARAM = "offset";
    private final static String LIMIT_PARAM = "limit";
    private static URL url = null;

    private static String lat_value = "37.422740";
    private static String lng_value = "-122.139956";
    private static String offset_value = "0";
    private static String limit_value = "50";

    public static URL buildUrlRest(String ID) {

        String MOD_SEARCH_ARTICLE_URL = SEARCH_ARTICLE_URL+ID;
        Uri builtUri = Uri.parse(MOD_SEARCH_ARTICLE_URL).buildUpon()
               .build();

        try {
            url = new URL(builtUri.toString());
            System.out.println ( "XXXX - url = "+ url );
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);
        return url;
    }

    public static URL buildUrl() {

        Uri builtUri = Uri.parse(SEARCH_ARTICLE_URL).buildUpon()
                .appendQueryParameter(LAT_PARAM, lat_value)
                .appendQueryParameter(LNG_PARAM, lng_value)
                .appendQueryParameter(OFFSET_PARAM, offset_value)
                .appendQueryParameter(LIMIT_PARAM, limit_value)
                .build();

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

}
