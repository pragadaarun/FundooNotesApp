package com.example.fundoonotes.DashBoard.Fragments.Notes;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.fundoonotes.Adapters.MyViewHolder;
import com.example.fundoonotes.Firebase.Model.FirebaseNoteModel;
import com.example.fundoonotes.Firebase.DataManager.FirebaseNoteManager;
import com.example.fundoonotes.Adapters.NoteAdapter;
import com.example.fundoonotes.Firebase.Model.MyViewModelFactory;
import com.example.fundoonotes.HelperClasses.ViewState;
import com.example.fundoonotes.R;
import java.util.ArrayList;
import com.example.fundoonotes.DashBoard.Activity.HomeActivity;
import com.example.fundoonotes.SQLiteDataManager.DatabaseHelper;
import com.example.fundoonotes.SQLiteDataManager.NoteTableManager;
import com.example.fundoonotes.SQLiteDataManager.SQLiteNoteTableManager;

public class NotesFragment extends Fragment {
    private static final String TAG = "NotesFragment";
    FirebaseNoteManager firebaseNoteManager;
    private RecyclerView recyclerView;
    private NoteAdapter notesAdapter;
    private NotesViewModel notesViewModel;
    private RecyclerView.LayoutManager layoutManager;
    public UpdateNoteFragment updateNoteFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerView);
        setLayoutManager(HomeActivity.IS_LINEAR_LAYOUT);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        firebaseNoteManager = new FirebaseNoteManager();
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(getContext());
        NoteTableManager noteTableManager = new SQLiteNoteTableManager(databaseHelper);
        notesViewModel = new ViewModelProvider(this, new MyViewModelFactory(noteTableManager))
                .get(NotesViewModel.class);

        notesViewModel.notesMutableLiveData.observe(getViewLifecycleOwner(),
                new Observer<ViewState<ArrayList<FirebaseNoteModel>>>() {
                    @Override
                    public void onChanged(ViewState<ArrayList<FirebaseNoteModel>> arrayListViewState) {
                        if(arrayListViewState instanceof ViewState.Loading) {
                            Toast.makeText(getContext(), "Loading", Toast.LENGTH_SHORT).show();
                        } else if (arrayListViewState instanceof ViewState.Success) {
                            ArrayList<FirebaseNoteModel> notes = ((ViewState.Success<ArrayList<FirebaseNoteModel>>) arrayListViewState).getData();
                            Log.e(TAG, "onNoteReceived: " + notes);
                            notesAdapter = new NoteAdapter(notes, new MyViewHolder.OnNoteListener() {
                                @Override
                                public void onNoteClick(int position, View viewHolder) {
                                    Toast.makeText(getContext(),
                                            "Note Clicked at Position " + position,
                                            Toast.LENGTH_SHORT).show();
                                    String title = notesAdapter.getItem(position).getTitle();
                                    String description = notesAdapter.getItem(position).getDescription();
                                    String noteID = notesAdapter.getItem(position).getNoteID();
                                    //Put the value
                                    updateNoteFragment = new UpdateNoteFragment();
                                    Bundle noteToUpdate = new Bundle();

                                    noteToUpdate.putString("title", title);
                                    noteToUpdate.putString("description",
                                            description);
                                    noteToUpdate.putString("noteID",
                                            noteID);
                                    updateNoteFragment.setArguments(noteToUpdate);
                                    getFragmentManager().beginTransaction().
                                            replace(R.id.home_fragment_container,
                                                    updateNoteFragment)
                                            .addToBackStack(null).commit();
                                }
                            });
                            recyclerView.setAdapter(notesAdapter);
                            notesAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getContext(), "Something went Wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getBindingAdapterPosition();
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

    public void setLayoutManager(boolean isLinear) {
        if (isLinear) {
            layoutManager = new
                    LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);

        } else {
            layoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        }
        recyclerView.setLayoutManager(layoutManager);
    }

    public void addNote(FirebaseNoteModel note) {
        notesAdapter.addNote(note);
    }

    public void searchText(String newText) {
        notesAdapter.getFilter().filter(newText);
    }
}