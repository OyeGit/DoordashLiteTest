package com.doordashlite.oye.doordashlite.Utils;

import android.content.Context;
import android.content.Intent;

import com.doordashlite.oye.doordashlite.Data.InfoItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

public class DoordashliteJsonUtils {

    private static final String TAG = DoordashliteJsonUtils.class.getSimpleName();

    public static List<InfoItem> getSearchResultStringsFromJson(Context context, String responseJsonStr)
            throws JSONException {
        final String mResponseJsonStr ="{ \"results\":"+responseJsonStr +"}" ;
        List<InfoItem> searchData = new ArrayList();
        final String ASM_DOC = "results";
        final String ASM_MESSAGE_CODE = "Error";
        final String INTENT_BROADCAST_STRING = "com.action.doordashLite";
        final String SEARCH_RESULT_ROW = "searchResultRow";

        JSONObject forecastJson = new JSONObject(mResponseJsonStr);

        /* Is there an error? */
        if (forecastJson.has(ASM_MESSAGE_CODE)) {
            int errorCode = forecastJson.getInt(ASM_MESSAGE_CODE);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    /* Location invalid */
                    return null;
                default:
                    /* Server probably down */
                    return null;
            }
        }

        JSONArray searchResultArray = forecastJson.getJSONArray(ASM_DOC);

        for (int i = 0; i < searchResultArray.length(); i++) {

            JSONObject jsonObject = searchResultArray.getJSONObject(i);

            String idd = jsonObject.getString("id");
            String mName = jsonObject.getString("name");
            String mDeliveryFee = jsonObject.getString("delivery_fee");
            String status = jsonObject.getString("status");
            String description = jsonObject.getString("description");
            String cover_img_url = jsonObject.getString("cover_img_url");

            ArrayList<String> row = new ArrayList<>();
            row.add( idd );
            row.add( mName );
            row.add( description );
            row.add( status );
            row.add( mDeliveryFee );
            row.add( cover_img_url );

            Intent intent = new Intent(INTENT_BROADCAST_STRING).
                    putStringArrayListExtra(SEARCH_RESULT_ROW,row);
            context.sendBroadcast(intent);
        }
        return searchData;
    }

    public static ArrayList<String> getSearchRestStringsFromJson(Context context, String responseJsonStr)
            throws JSONException {
        ArrayList<String> searchData = new ArrayList();
        final String ASM_MESSAGE_CODE = "Error";
        JSONObject jsonObject = new JSONObject(responseJsonStr);

        /* Is there an error? */
        if (jsonObject.has(ASM_MESSAGE_CODE)) {
            int errorCode = jsonObject.getInt(ASM_MESSAGE_CODE);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    /* Location invalid */
                    return null;
                default:
                    /* Server probably down */
                    return null;
            }
        }

        String idd = jsonObject.getString("id");
        String mName = jsonObject.getString("name");
        String cover_img_url = jsonObject.getString("cover_img_url");
        searchData.add(  idd );
        searchData.add(  mName );
        searchData.add(  cover_img_url );

        return searchData;
    }

}
