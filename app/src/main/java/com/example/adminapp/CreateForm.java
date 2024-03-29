package com.example.adminapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;

public class CreateForm extends AppCompatActivity {

    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;

    EditText eventName, department, faculty, venue, pointsAlloted, date_, time;
    Button submit, reset;
    DatabaseReference myRef;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_form);

        eventName = findViewById(R.id.eventName_et);
        department = findViewById(R.id.dept_et);
        faculty = findViewById(R.id.faculty_et);
        venue=findViewById(R.id.venue_et);
        pointsAlloted = findViewById(R.id.pointsAllotted_et);
        submit = findViewById(R.id.submit_btn);
        reset = findViewById(R.id.reset_btn);
        time = findViewById(R.id.timePickerButton);

        //date picker
        initDatePicker();
        date_ = findViewById(R.id.datePickerButton);
        date_.setText(getTodaysDate());

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertData();
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eventName.getText().clear();
                department.getText().clear();
                time.setText("00:00");
                faculty.getText().clear();
                pointsAlloted.getText().clear();
                date_.setText(getTodaysDate());
            }
        });

        date_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePicker(view);
            }
        });

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePicker();
            }
        });
    }

    public void insertData(){

        HashMap<String, Object> insert = new HashMap<>();
        insert.put("archived",0);
        insert.put("name", eventName.getText().toString());
        insert.put("date", date_.getText().toString());
        insert.put("time", time.getText().toString());
        insert.put("department", department.getText().toString());
        insert.put("faculty", faculty.getText().toString());
        insert.put("venue",venue.getText().toString());
        insert.put("points", pointsAlloted.getText().toString());

        database = FirebaseDatabase.getInstance("https://eccloginmoduletest-default-rtdb.asia-southeast1.firebasedatabase.app/");
        database.getReference().child("events").push()
                .setValue(insert)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(CreateForm.this, "Data inserted successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CreateForm.this, "Error "+e, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //date picker
    private String getTodaysDate()
    {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    private void initDatePicker()
    {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                month = month + 1;
                String date = makeDateString(day, month, year);
                date_.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);


        datePickerDialog = new DatePickerDialog(this, dateSetListener, year, month, day);
        //datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

    }

    private String makeDateString(int day, int month, int year)
    {
        return day + "-" + month + "-" + year;
    }
    public void openDatePicker(View view)
    {
        datePickerDialog.show();
    }

    //timepicker
    private void timePicker(){
        timePickerDialog = new TimePickerDialog(this,  new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {


                //Showing the picked value in the textView
                time.setText(String.valueOf(hour)+ ":"+String.valueOf(minute));

            }
        }, 15, 30, false);

        timePickerDialog.show();
    }

}