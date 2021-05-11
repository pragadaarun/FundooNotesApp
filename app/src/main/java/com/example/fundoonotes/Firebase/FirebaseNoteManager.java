package com.example.fundoonotes.Firebase;

import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class FirebaseNoteManager {

    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;

    public void getAllNotes(NoteListener listener) {
        ArrayList<FirebaseModel> notesList = new ArrayList<FirebaseModel>();
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseFirestore.collection("users").document(firebaseUser.getUid())
                .collection("notes").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (int index = 0; index<queryDocumentSnapshots.size(); index++){
                    Log.e("NoteManager",
                            "onSuccess: "+queryDocumentSnapshots
                                    .getDocuments().get(index) );

                    String title = queryDocumentSnapshots.getDocuments()
                            .get(index).getString("title");
                    String content = queryDocumentSnapshots.getDocuments()
                            .get(index).getString("description");

                    FirebaseModel note = new FirebaseModel(title,content);
                    notesList.add(note);
                }
                listener.onNoteReceived(notesList);
            }
        });
    }

}
