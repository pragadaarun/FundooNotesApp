package com.example.fundoonotes.Firebase.Model;

public class FirebaseNoteModel {
    private static final String TAG = "FirebaseNoteModel";
    private String title;
    private String description;
    private String noteID;

    public FirebaseNoteModel(String title, String description, String noteID)
    {
        this.title = title;
        this.description = description;
        this.noteID = noteID;
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

    public void setNoteID(String noteID) {
        this.noteID = noteID;
    }

    public String getNoteID() {
        return noteID;
    }
}