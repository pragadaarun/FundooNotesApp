package com.example.fundoonotes.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.example.fundoonotes.Firebase.Model.FirebaseNoteModel;
import com.example.fundoonotes.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NoteAdapter extends RecyclerView.Adapter<MyViewHolder> implements Filterable {

    private ArrayList<FirebaseNoteModel> notesList;
    private final MyViewHolder.OnNoteListener onNoteListener;
    private ArrayList<FirebaseNoteModel> notesSearch;

    public NoteAdapter(ArrayList<FirebaseNoteModel> list, MyViewHolder.OnNoteListener onNoteListener ){
        this.notesList = list;
        this.onNoteListener = onNoteListener;
        notesSearch = notesList;
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

    @Override
    public Filter getFilter() {
        return notesFilter;
    }

    private Filter notesFilter = new Filter() {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<FirebaseNoteModel> filteredList = new ArrayList<>();

            if(constraint == null || constraint.length() == 0){
                filteredList.addAll(notesSearch);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for(FirebaseNoteModel note : notesSearch) {
                    if(note.getTitle().toLowerCase().contains(filterPattern)
                        || note.getDescription().toLowerCase().contains(filterPattern)) {
                        filteredList.add(note);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            notesList.clear();
            notesList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
