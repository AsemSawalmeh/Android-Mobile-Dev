package com.example.taskmanagement;

import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class addTask extends AppCompatActivity {

    public static final String DATA = "DATA";
    SharedPreferences prefs;

    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);


        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        editor = prefs.edit();


        TextInputEditText taskTitle = findViewById(R.id.taskName);
        EditText taskDescription = findViewById(R.id.taskDescription);
        TextView dueDateTextView = findViewById(R.id.dueDateTextView);
        Button setDateButton = findViewById(R.id.setDateButton);
        Button addTaskButton = findViewById(R.id.addTaskButton);

        setDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }

            private void showDatePickerDialog() {

                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        addTask.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                dueDateTextView.setText(String.format("%04d-%02d-%02d", year, monthOfYear + 1, dayOfMonth));
                            }
                        },
                        year, month, dayOfMonth);

                datePickerDialog.show();
            }
        });

        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String taskTitleText = taskTitle.getText().toString();
                String taskDescriptionText = taskDescription.getText().toString();
                String dueDateText = dueDateTextView.getText().toString();
                Date actualDate;

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

                if (TextUtils.isEmpty(taskTitleText) || TextUtils.isEmpty(taskDescriptionText) || TextUtils.isEmpty(dueDateText)) {
                    Toast.makeText(addTask.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else {

                    try {
                        actualDate=dateFormat.parse(dueDateText);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }

                    task newTask = new task(taskTitleText, taskDescriptionText, actualDate, "Pending");
                    MainActivity.taskList.add(newTask);
                    saveTasks(MainActivity.taskList);
                    Intent newIntent = new Intent(addTask.this, MainActivity.class);
                    startActivity(newIntent);
                }
            }
        });

    }

    private void saveTasks(List<task> taskList) {
        Gson gson = new Gson();
        String taskString = gson.toJson(taskList);
        editor.putString(DATA, taskString);
        editor.apply();
    }
}