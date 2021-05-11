package com.example.fundoonotes.Firebase;


import android.view.View;
import android.widget.TextView;

import com.example.fundoonotes.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder {
    TextView noteTitle, noteContent;
    View view;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        noteTitle = itemView.findViewById(R.id.note_title);
        noteContent = itemView.findViewById(R.id.note_content);
        view = itemView;
    }
}