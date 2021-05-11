package com.example.fundoonotes.Firebase;

import java.util.ArrayList;

public interface NoteListener{
    void onNoteReceived(ArrayList<FirebaseModel> noteslist);
}
