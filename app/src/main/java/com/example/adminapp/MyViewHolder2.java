package com.example.adminapp;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder2 extends RecyclerView.ViewHolder{
    TextView name, department, venue, faculty, date, time, points;
    Button deleteBtn;

    public MyViewHolder2(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.eventNameTV);
        department = itemView.findViewById(R.id.deptNameTV);
        faculty = itemView.findViewById(R.id.facultyNameTV);
        venue=itemView.findViewById(R.id.venueNameTV);
        date = itemView.findViewById(R.id.dateTV);
        time = itemView.findViewById(R.id.timeTV);
        points = itemView.findViewById(R.id.pointsTV);
        deleteBtn=itemView.findViewById(R.id.deleteBtn);
    }
}