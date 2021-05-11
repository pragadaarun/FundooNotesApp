package com.example.fundoonotes.DashBoard.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.fundoonotes.Firebase.CallBack;
import com.example.fundoonotes.Firebase.FirebaseNoteModel;
import com.example.fundoonotes.Firebase.FirebaseNoteManager;
import com.example.fundoonotes.Firebase.NoteAdapter;
import com.example.fundoonotes.R;

import java.util.ArrayList;

public class NotesFragment extends Fragment {
    private static final String TAG = "NotesFragment";
    FirebaseNoteManager firebaseNoteManager;
    private RecyclerView recyclerView;
    private final ArrayList<FirebaseNoteModel> notes = new ArrayList<FirebaseNoteModel>();
    private NoteAdapter notesAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notes, container, false);
        final StaggeredGridLayoutManager linearLayoutManager = new
                StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);
//        linearLayoutManager.setOrientation(StaggeredGridLayoutManager.VERTICAL);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        firebaseNoteManager = new FirebaseNoteManager();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)        super.onViewCreated(view, savedInstanceState);
        firebaseNoteManager.getAllNotes(notesList -> {
        Log.e("Arun", "onNoteReceived: " + notesList);
        notesAdapter = new NoteAdapter(this.getContext(), notesList);

        recyclerView.setAdapter(notesAdapter);
        notesAdapter.notifyDataSetChanged();
    });
}
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}