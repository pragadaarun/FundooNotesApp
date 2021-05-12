package com.example.fundoonotes.Firebase;


import android.view.View;
import android.widget.TextView;

import com.example.fundoonotes.R;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder {
    TextView noteTitle, noteDescription;
    View view;
    CardView mCardView;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        noteTitle = itemView.findViewById(R.id.note_title);
        noteDescription = itemView.findViewById(R.id.note_description);
        mCardView = itemView.findViewById(R.id.note_card);
        view = itemView;
    }
}