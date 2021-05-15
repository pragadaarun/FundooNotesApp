package com.example.fundoonotes.DashBoard.Fragments.Notes;

import android.util.Log;
import com.example.fundoonotes.Firebase.DataManager.FirebaseNoteManager;
import com.example.fundoonotes.Firebase.DataManager.NoteManager;
import com.example.fundoonotes.Firebase.Model.FirebaseNoteModel;
import com.example.fundoonotes.HelperClasses.CallBack;
import com.example.fundoonotes.HelperClasses.ViewState;
import java.util.ArrayList;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NotesViewModel extends ViewModel {
    MutableLiveData<ViewState<ArrayList<FirebaseNoteModel>>> notesMutableLiveData =
            new MutableLiveData<>();
    private static final String TAG = "NotesViewModel";
    private NoteManager firebaseNoteManager;

    public NotesViewModel() {
        firebaseNoteManager = new FirebaseNoteManager();
        loadNotes();
    }

    private void loadNotes() {
        notesMutableLiveData.setValue(new ViewState.Loading<>());
        firebaseNoteManager.getAllNotes(new CallBack<ArrayList<FirebaseNoteModel>>() {
            @Override
            public void onSuccess(ArrayList<FirebaseNoteModel> data) {
                Log.e(TAG, "onNoteReceived: " + data);
                notesMutableLiveData.setValue(new ViewState.Success<>(data));
            }

            @Override
            public void onFailure(Exception exception) {
                notesMutableLiveData.setValue(new ViewState.Failure<>(exception));
            }
        });
    }
}
