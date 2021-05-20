package com.example.fundoonotes.Adapters;

import android.view.View;
import android.widget.TextView;

import com.example.fundoonotes.R;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{
    public TextView noteTitle, noteDescription;
    View view;
    CardView mCardView;
    private OnNoteListener onNoteListener;

    public MyViewHolder(@NonNull View itemView, OnNoteListener onNoteListener) {
        super(itemView);
        noteTitle = itemView.findViewById(R.id.note_title);
        noteDescription = itemView.findViewById(R.id.note_description);
        mCardView = itemView.findViewById(R.id.note_card);
        view = itemView;
        this.onNoteListener = onNoteListener;
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        onNoteListener.onNoteClick(getBindingAdapterPosition(),v);
    }

    public interface OnNoteListener {
        void onNoteClick(int position, View viewHolder);
    }
}