package com.example.android.developerinfo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.List;

public class DeveloperActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Developer>>, DeveloperAdapter.DeveloperAdapterOnClickHandler {
    public static final String LOG_TAG = DeveloperActivity.class.getName();
    private static final String BASE_GITHUB_API = "https://api.github.com/search/users?";
    private static final String GITHUB_API = "https://api.github.com/search/users?q=language:java+location:lagos";
    private static final int DEVELOPER_LOADER_ID = 1;
    TextView mTextView;
    List<Developer> mDeveloperList;
    private DeveloperAdapter mDeveloperAdapter;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_developer);
        // Find a reference to the recycler view in the layout
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mDeveloperAdapter = new DeveloperAdapter(this, this);
        // Set the adapter on the recycler view
        // so the list can be populated in the user interface
        mRecyclerView.setAdapter(mDeveloperAdapter);

        mTextView = (TextView) findViewById(R.id.empty_text);
        //get an instance of the connectivity manager class to determine whether there's internet connection or not
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            getSupportLoaderManager().initLoader(DEVELOPER_LOADER_ID, null, this);
        } else {
            View barView = findViewById(R.id.main_progress_bar);
            barView.setVisibility(View.GONE);
            //set the text as no internet connection when user is not connected to the internet
            mTextView.setText(R.string.no_internet_connection);
            mTextView.setTextSize(40);
        }


    }

    // sets up the Uri based on the parameters in the sharedPreferences and uses this Uri to instantiate a new DeveloperLoader instance
    @Override
    public Loader<List<Developer>> onCreateLoader(int id, Bundle args) {


        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String minMagSettings = sp.getString(getString(R.string.settings_language_key), getString(R.string.settings_language_default));
        String locationSettings = sp.getString(getString(R.string.settings_location_key), getString(R.string.settings_location_default));

        String defaultBehaviorString = sp.getString(getString(R.string.settings_defaultBehavior_key), getString(R.string.settings_defaultBehavior_default_value));
        if (defaultBehaviorString.equalsIgnoreCase("yes")) {
            return new DeveloperLoader(this, GITHUB_API);
        }
        Uri uri = Uri.parse(BASE_GITHUB_API);
        Uri.Builder uriBuilder = uri.buildUpon();
        String uriPath = "q=language:" + minMagSettings + "+location:" + locationSettings;
        uriBuilder.encodedQuery(uriPath);
        return new DeveloperLoader(this, uriBuilder.toString());
    }

    //sets the content of the adapter based on the data fetched from the internet
    @Override
    public void onLoadFinished(Loader<List<Developer>> loader, List<Developer> data) {
        mDeveloperList = data;
        mDeveloperAdapter.setDeveloperList(null);
        View barView = findViewById(R.id.main_progress_bar);
        barView.setVisibility(View.GONE);


        if (data != null && !data.isEmpty()) {
            mDeveloperAdapter.setDeveloperList(mDeveloperList);
        } else {
            mTextView.setText(R.string.no_developers);
            mTextView.setTextSize(40);
        }

    }

    //resets the loader when a new activity is being called
    @Override
    public void onLoaderReset(Loader<List<Developer>> loader) {
        mDeveloperAdapter.setDeveloperList(null);
    }

    // creates the menu icon on the home screen
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    // performs an action based on menu item selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    // ensures the details page is opened when user clicks list item
    @Override
    public void onClick(int position) {
        Developer currentDeveloper = mDeveloperList.get(position);
        Intent intent = new Intent(DeveloperActivity.this, DeveloperDetailsActivity.class);
        intent.putExtra("name", currentDeveloper.getCurrentUserName());
        intent.putExtra("url", currentDeveloper.getCurrentUrlString());
        intent.putExtra("image", currentDeveloper.getCurrentUrlImageString());
        startActivity(intent);
    }
}
