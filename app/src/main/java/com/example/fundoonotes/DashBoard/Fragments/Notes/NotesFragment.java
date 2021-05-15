package com.example.fundoonotes.DashBoard.Fragments.Notes;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
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
import com.example.fundoonotes.Firebase.DataManager.FirebaseNoteManager;
import com.example.fundoonotes.Adapters.NoteAdapter;
import com.example.fundoonotes.HelperClasses.OnNoteListener;
import com.example.fundoonotes.HelperClasses.ViewState;
import com.example.fundoonotes.R;
import java.util.ArrayList;

public class NotesFragment extends Fragment {
    private static final String TAG = "NotesFragment";
    FirebaseNoteManager firebaseNoteManager;
    private RecyclerView recyclerView;
    private NoteAdapter notesAdapter;
    private NotesViewModel notesViewModel;

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
                StaggeredGridLayoutManager(1,
                StaggeredGridLayoutManager.VERTICAL);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        firebaseNoteManager = new FirebaseNoteManager();
        notesViewModel = new ViewModelProvider(this).get(NotesViewModel.class);

        notesViewModel.notesMutableLiveData.observe(getViewLifecycleOwner(), new Observer<ViewState<ArrayList<FirebaseNoteModel>>>() {
            @Override
            public void onChanged(ViewState<ArrayList<FirebaseNoteModel>> arrayListViewState) {
                if(arrayListViewState instanceof ViewState.Loading) {
                    Toast.makeText(getContext(), "Loading", Toast.LENGTH_SHORT).show();
                } else if (arrayListViewState instanceof ViewState.Success) {
                    ArrayList<FirebaseNoteModel> notes = ((ViewState.Success<ArrayList<FirebaseNoteModel>>) arrayListViewState).getData();
                    Log.e(TAG, "onNoteReceived: " + notes);
                    notesAdapter = new NoteAdapter(notes, new OnNoteListener() {
                        @Override
                        public void onNoteClick(int position) {
                            Toast.makeText(getContext(), "onNoteClick", Toast.LENGTH_SHORT).show();
                        }
                    });
                    recyclerView.setAdapter(notesAdapter);
                    notesAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "Something went Wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                try {
                    String noteId = notesAdapter.getItem(position).getNoteID();
                    notesAdapter.removeNote(position);
                    firebaseNoteManager.moveToTrash("Notes","Trash", noteId);
                }catch(IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}