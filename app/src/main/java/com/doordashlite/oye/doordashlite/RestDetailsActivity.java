package com.doordashlite.oye.doordashlite;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.doordashlite.oye.doordashlite.Utils.DoordashliteJsonUtils;
import com.doordashlite.oye.doordashlite.Utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;

public class RestDetailsActivity extends AppCompatActivity {

    private static String itemID;
    ArrayList<String> resultsList = new ArrayList();
    private ProgressBar mLoadingIndicator;
    ImageView imageView;
    final private static String ITEM_ID = "itemID";
    TextView mName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        itemID = bundle.getString(ITEM_ID);

        setContentView(R.layout.activity_rest_details);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        imageView = (ImageView) findViewById(R.id.image1);
        mName = (TextView) findViewById(R.id.mName);
        new ExecuteConnAsyncTask().execute();
    }

    public class ExecuteConnAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            URL RequestUrl = NetworkUtils.buildUrlRest(itemID);
            try {
                String jsonSearchResponse = NetworkUtils
                        .getResponseFromHttpUrl(RequestUrl);
                resultsList = DoordashliteJsonUtils
                        .getSearchRestStringsFromJson(RestDetailsActivity.this, jsonSearchResponse);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            Picasso.get().load(   resultsList.get(2) ).into( imageView);
            mName.setText( resultsList.get(1) );
            mLoadingIndicator.setVisibility(View.INVISIBLE);
        }

        @Override
        protected void onPreExecute() {
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }
    }
}
