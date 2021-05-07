package com.example.fundoonotes.DashBoard.Fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fundoonotes.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AddNoteFragment extends Fragment {
    private EditText fAddTitleOfNote, fAddDescriptionOfNote;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_note, container, false);
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        Button saveNoteButton = (Button) Objects.requireNonNull(getView()).findViewById(R.id.saveNoteButton);
        fAddTitleOfNote = (EditText) getView().findViewById(R.id.editTextTitle);
        fAddDescriptionOfNote = (EditText) getView().findViewById(R.id.editTextDescription);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        saveNoteButton.setOnClickListener(this::saveToFirebase);
    }

    private void saveToFirebase(View v) {
        String title = fAddTitleOfNote.getText().toString();
        String description = fAddDescriptionOfNote.getText().toString();
        if (!title.isEmpty() || !description.isEmpty()) {
            DocumentReference documentReference = firebaseFirestore
                    .collection("users")
                    .document(firebaseUser.getUid())
                    .collection("notes").document();
            Map<String, Object> note = new HashMap<>();
            note.put("title", title);
            note.put("description", description);

            documentReference.set(note)
                    .addOnSuccessListener(aVoid -> Toast.makeText(getContext(),
                            "Note Created Successfully", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(getContext(),
                            "Failed To Create Note", Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(getContext(), "Both fields are Required",
                    Toast.LENGTH_SHORT).show();
        }
    }

}
