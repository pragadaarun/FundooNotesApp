package com.example.fundoonotes.Firebase;

import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;

public class FirebaseNoteManager {

    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    public void getAllNotes(CallBack<ArrayList<FirebaseNoteModel>> listener) {
        ArrayList<FirebaseNoteModel> notesList = new ArrayList<FirebaseNoteModel>();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore.collection("users").document(firebaseUser.getUid())
                .collection("notes")
                .orderBy("creationDate", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (int index = 0; index < queryDocumentSnapshots.size(); index++) {
                            Log.e("NoteManager",
                                    "onSuccess: " + queryDocumentSnapshots
                                            .getDocuments().get(index));

                            String title = queryDocumentSnapshots.getDocuments()
                                    .get(index).getString("title");
                            String description = queryDocumentSnapshots.getDocuments()
                                    .get(index).getString("description");

                            FirebaseNoteModel note = new FirebaseNoteModel(title, description);
                            notesList.add(note);
                        }
                        listener.onSuccess(notesList);
                    }

                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.onFailure(e);
                    }
                });
    }

    public void addNote(String title, String description, CallBack<Boolean> addListener) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DocumentReference documentReference = firebaseFirestore
                .collection("users")
                .document(firebaseUser.getUid())
                .collection("notes").document();
        Map<String, Object> note = new HashMap<>();
        note.put("title", title);
        note.put("description", description);
        note.put("creationDate", System.currentTimeMillis());

        documentReference.set(note)
                .addOnSuccessListener(aVoid -> {
                    addListener.onSuccess(true);
                })
                .addOnFailureListener(addListener::onFailure
                );
    }
}
