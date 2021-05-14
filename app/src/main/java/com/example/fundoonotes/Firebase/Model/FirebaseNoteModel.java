package com.example.fundoonotes.Firebase.Model;

import android.util.Log;

public class FirebaseNoteModel {
    private static final String TAG = "FirebaseNoteModel";
    private String title;
    private String description;


    public FirebaseNoteModel()
    {

    }

    public FirebaseNoteModel(String title, String description)
    {
        Log.e(TAG,"");
        this.title = title;
        this.description = description;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}