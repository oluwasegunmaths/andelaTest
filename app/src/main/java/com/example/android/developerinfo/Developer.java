package com.example.android.developerinfo;

/**
 * Created by USER on 8/20/2017.
 */

public class Developer {
    private String currentUrlImageString;
    private String currentUserName;
    private String currentUrlString;

    public Developer(String name, String image, String profileUrl) {
        currentUserName = name;
        currentUrlImageString = image;
        currentUrlString = profileUrl;
    }

    //returns the current username
    public String getCurrentUserName() {

        return currentUserName;
    }

    //returns the Url string link to the current developer
    public String getCurrentUrlString() {

        return currentUrlString;
    }

    //returns the Url string link to the current developer's profile image
    public String getCurrentUrlImageString() {

        return currentUrlImageString;
    }

}

