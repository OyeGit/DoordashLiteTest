package com.doordashlite.oye.doordashlite;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.doordashlite.oye.doordashlite.Data.InfoItem;
import com.doordashlite.oye.doordashlite.Utils.DoordashliteJsonUtils;
import com.doordashlite.oye.doordashlite.Utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<String>  {

    private static final String TAG = MainActivity.class.getSimpleName();

    final Context context = this;
    private RecyclerView recyclerView;
    private ParamsAdapter mAdapter;
    private ProgressBar mLoadingIndicator;
    private List<InfoItem> resultsList = new ArrayList<>();
    private List<InfoItem> finalList = new ArrayList<>();
    private OnItemClickListener mListener;
    final private static int LOADER_ID = 5;
    final private static String ITEM_ID = "itemID";
    final private static int REQUEST_CODE = 1;
    final String INTENT_BROADCAST_STRING = "com.action.doordashLite";
    final String SEARCH_RESULT_ROW = "searchResultRow";
    private BroadcastReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        mAdapter = new ParamsAdapter(  finalList, mListener  );
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        if ( isNetworkAvailable() ) {
            getSupportLoaderManager().initLoader(LOADER_ID, null, this);
        } else {
            Log.v( TAG, getString(R.string.internet_not_available) );
            Toast.makeText(context, getString(R.string.internet_not_available), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public Loader<String> onCreateLoader(int LOADER_ID, final Bundle args) {

        return new AsyncTaskLoader<String>(context) {

            @Override
            protected void onStartLoading() {
                mLoadingIndicator.setVisibility(View.VISIBLE);
                forceLoad();
            }

            @Override
            public String loadInBackground() {
                URL RequestUrl = NetworkUtils.buildUrl();
                try {
                    String jsonSearchResponse = NetworkUtils
                            .getResponseFromHttpUrl(RequestUrl);
                    resultsList = DoordashliteJsonUtils
                            .getSearchResultStringsFromJson(MainActivity.this, jsonSearchResponse);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onStopLoading() {
                mLoadingIndicator.setVisibility(View.INVISIBLE);
            }
        };
    }

    @Override
    public void onLoaderReset( Loader<String> loader) {
    }

    @Override
    public void onLoadFinished( Loader<String> loader, String data) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
    }

    public interface OnItemClickListener {
        void onItemClick(InfoItem item);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    class ParamsAdapter extends RecyclerView.Adapter<ParamsAdapter.MyViewHolder> {

        private List<InfoItem> infoList;
        private final OnItemClickListener listener;

        public class MyViewHolder extends RecyclerView.ViewHolder {

            public TextView mId;
            public TextView mName;
            public TextView mDescription;
            public TextView mTime;
            public TextView mStatus;
            public TextView mDeliveryFee;
            public ImageView imageView;

            public MyViewHolder(View view) {
                super(view);
                //mId = (TextView) view.findViewById(R.id.mId);
                mName = (TextView) view.findViewById(R.id.mName);
                mDescription = (TextView) view.findViewById(R.id.mDescription);
                //mTime = (TextView) view.findViewById(R.id.mTime);
                mStatus =(TextView)view.findViewById(R.id.mStatus);
                // mDeliveryFee =(TextView)view.findViewById(R.id.mDeliveryFee);
                imageView = (ImageView) view.findViewById(R.id.image1);
            }

            public void bind(final InfoItem item, final OnItemClickListener listener) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        Intent intentMain = new Intent(context, RestDetailsActivity.class);
                        intentMain.putExtra(ITEM_ID, item.getmID() );
                        startActivityForResult(intentMain, REQUEST_CODE);
                    }
                });
            }
        }

        public ParamsAdapter(List<InfoItem> vResultsList , OnItemClickListener vListener ) {
            this.infoList = vResultsList;
            this.listener = vListener;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.tabitem, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            InfoItem infoitem = infoList.get(position);
            holder.bind( infoitem, listener );
            holder.mName.setText( infoitem.getmName() );
            holder.mDescription.setText(infoitem.getmDescription() );
            holder.mStatus.setText( infoitem.getmStatus() );
            Picasso.get().load( infoitem.getmCoverImgUrl() ).into(holder.imageView);
        }

        @Override
        public int getItemCount() {
            return infoList.size();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(INTENT_BROADCAST_STRING);
        this.mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                try {
                    ArrayList<String> newRow = intent.getStringArrayListExtra(SEARCH_RESULT_ROW);
                    loadSearchResult(newRow);
                } catch( Exception e ){
                    e.printStackTrace();
                }
            }
        };
        this.registerReceiver(this.mReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.unregisterReceiver(this.mReceiver);
    }

    public void loadSearchResult ( ArrayList<String> p_row ) {

        InfoItem sched = new InfoItem( p_row.get(0),p_row.get(1),p_row.get(2),
                p_row.get(3), p_row.get(4), p_row.get(5));
        finalList.add(sched);

        mAdapter.notifyDataSetChanged();
    }



}
