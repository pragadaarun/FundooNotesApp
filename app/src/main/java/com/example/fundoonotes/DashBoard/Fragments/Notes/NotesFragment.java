package com.example.fundoonotes.DashBoard.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.fundoonotes.HelperClasses.CallBack;
import com.example.fundoonotes.Firebase.Model.FirebaseNoteModel;
import com.example.fundoonotes.Firebase.FirebaseNoteManager;
import com.example.fundoonotes.Adapters.NoteAdapter;
import com.example.fundoonotes.HelperClasses.OnNoteListener;
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
        final StaggeredGridLayoutManager layoutManager = new
                StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        firebaseNoteManager = new FirebaseNoteManager();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firebaseNoteManager.getAllNotes(new CallBack<ArrayList<FirebaseNoteModel>>() {
            @Override
            public void onSuccess(ArrayList<FirebaseNoteModel> data) {
                Log.e(TAG, "onNoteReceived: " + data);
                notesAdapter = new NoteAdapter(data, new OnNoteListener() {
                    @Override
                    public void onNoteClick(int position) {
                        Toast.makeText(getContext(), "onNoteClick", Toast.LENGTH_SHORT).show();
                    }
                });
                recyclerView.setAdapter(notesAdapter);
                notesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Exception exception) {
                Toast.makeText(getContext(),
                        "Something went Wrong", Toast.LENGTH_SHORT).show();
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}