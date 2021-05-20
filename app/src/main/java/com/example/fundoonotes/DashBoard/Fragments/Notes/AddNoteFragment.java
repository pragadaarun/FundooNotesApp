package com.example.fundoonotes.DashBoard.Fragments.Notes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fundoonotes.Adapters.NoteAdapter;
import com.example.fundoonotes.Firebase.Model.FirebaseNoteModel;
import com.example.fundoonotes.HelperClasses.CallBack;
import com.example.fundoonotes.Firebase.DataManager.FirebaseNoteManager;
import com.example.fundoonotes.R;

import java.util.Objects;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static com.example.fundoonotes.DashBoard.Activity.HomeActivity.addNote;


public class AddNoteFragment extends Fragment {
    private EditText fAddTitleOfNote, fAddDescriptionOfNote;

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        addNote.hide();
        return inflater.inflate(R.layout.fragment_add_note, container, false);
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Button saveNoteButton = (Button) Objects.requireNonNull(getView()).findViewById(R.id.saveNoteButton);
        fAddTitleOfNote = (EditText) getView().findViewById(R.id.editTextTitle);
        fAddDescriptionOfNote = (EditText) getView().findViewById(R.id.editTextDescription);

        saveNoteButton.setOnClickListener(this::saveToFirebase);
    }

    private void saveToFirebase(View v) {
        String title = fAddTitleOfNote.getText().toString();
        String description = fAddDescriptionOfNote.getText().toString();
        if (!title.isEmpty() || !description.isEmpty()) {
            FirebaseNoteManager firebaseNoteManager = new FirebaseNoteManager();
            firebaseNoteManager.addNote(title, description, new CallBack<Boolean>() {

                @Override
                public void onSuccess(Boolean data) {
                    Toast.makeText(getContext(),
                            "Note Created Successfully",
                            Toast.LENGTH_SHORT).show();
                    getFragmentManager().popBackStack();
                }

                @Override
                public void onFailure(Exception exception) {
                    Toast.makeText(getContext(),
                            "Failed To Create Note", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getContext(), "Both fields are Required",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        addNote.show();
    }

}
