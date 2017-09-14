package com.example.android.developerinfo;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.List;

/**
 * Created by USER on 8/20/2017.
 */

public class DeveloperLoader extends AsyncTaskLoader<List<Developer>> {
   private String gitHubQueryString;

    //initializes the github url string
    public DeveloperLoader(Context context, String urlString) {
        super(context);
        gitHubQueryString = urlString;
    }

    //method called to call forceLoad method which in turn calls loadInBackground
    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    //calls on the DeveloperUtils.extractDevelopers method to get a list of Developer objects and eventually returns this list to the Developer activity loader
    @Override
    public List<Developer> loadInBackground() {
        if (gitHubQueryString == null) {
            return null;
        }
        List<Developer> developers = DeveloperUtils.getDevelopersData(gitHubQueryString);
        return developers;

    }

}


