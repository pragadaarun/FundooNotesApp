package com.example.fundoonotes.Firebase.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class FirebaseNoteModel implements Parcelable {

    private String userId;
    private String noteID;
    private String title;
    private String description;

    public FirebaseNoteModel(String userId, String noteID, String title, String description)
    {
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.noteID = noteID;
    }

    protected FirebaseNoteModel(Parcel in) {
        userId = in.readString();
        noteID = in.readString();
        title = in.readString();
        description = in.readString();
    }

    public static final Creator<FirebaseNoteModel> CREATOR = new Creator<FirebaseNoteModel>() {
        @Override
        public FirebaseNoteModel createFromParcel(Parcel in) {
            return new FirebaseNoteModel(in);
        }

        @Override
        public FirebaseNoteModel[] newArray(int size) {
            return new FirebaseNoteModel[size];
        }
    };

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeString(this.noteID);
    }
}