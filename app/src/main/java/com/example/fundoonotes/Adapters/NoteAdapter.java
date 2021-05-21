package com.example.fundoonotes.Adapters;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fundoonotes.Firebase.Model.FirebaseNoteModel;
import com.example.fundoonotes.R;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NoteAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private ArrayList<FirebaseNoteModel> notesList;
    private final MyViewHolder.OnNoteListener onNoteListener;
    private ArrayList<FirebaseNoteModel> notesSource;
    private Timer timer;

    public NoteAdapter(ArrayList<FirebaseNoteModel> list, MyViewHolder.OnNoteListener onNoteListener ){
        this.notesList = list;
        this.onNoteListener = onNoteListener;
        notesSource = notesList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_details,parent,false);
        return new MyViewHolder(view, onNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        FirebaseNoteModel note = notesList.get(position);
        holder.noteTitle.setText(note.getTitle());
        holder.noteDescription.setText(note.getDescription());

    }

    @Override
    public int getItemCount() {
        Log.e("Note Adapter", "get Item Count" + notesList.size());
        return notesList.size();
    }

    public void addNote(FirebaseNoteModel note){
        notesList.add(0, note);
        notifyItemInserted(0);
    }

    public void removeNote(int position){
        notesList.remove(position);
        notifyItemRemoved(position);
    }

    public FirebaseNoteModel getItem(int position) {
        return notesList.get(position);
    }

}
