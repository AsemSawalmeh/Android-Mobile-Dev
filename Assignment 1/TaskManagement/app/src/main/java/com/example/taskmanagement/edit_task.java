package com.example.taskmanagement;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class edit_task extends AppCompatActivity {

    public static final String DATA = "DATA";
    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        editor = prefs.edit();

        Intent intent = getIntent();
        int id = (int)intent.getExtras().get("task_id");
        task task = MainActivity.taskList.get(id);

        TextView taskName = findViewById(R.id.taskNameTextView);
        TextInputEditText editName = findViewById(R.id.taskName);

        TextView taskDescription = findViewById(R.id.taskDescriptionTextView);
        EditText editDescription = findViewById(R.id.editTaskDescription);


        TextView dueDate = findViewById(R.id.dueDateTextView);
        Calendar currCalendar=Calendar.getInstance();
        currCalendar.setTime(task.getDueDate());
        int currYear = currCalendar.get(Calendar.YEAR);
        int currMonth = currCalendar.get(Calendar.MONTH) + 1;
        int currDay = currCalendar.get(Calendar.DAY_OF_MONTH);
        String formattedCurrDate = String.format("%04d-%02d-%02d", currYear, currMonth, currDay)+" ";
        dueDate.setText(formattedCurrDate);

        Button editDate = findViewById(R.id.setDateButton);
        editDate.setOnClickListener(new View.OnClickListener() {
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
                        edit_task.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                dueDate.setText(String.format("%04d-%02d-%02d", year, monthOfYear + 1, dayOfMonth));
                            }
                        },
                        year, month, dayOfMonth);

                datePickerDialog.show();
            }
        });

        TextView taskStatus = findViewById(R.id.taskStatus);
        Spinner editStatus = findViewById(R.id.editTaskStatus);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.taskStatusArray,
                android.R.layout.simple_spinner_item
        );


        taskName.setText("Task Name : ");
        editName.setText(task.getTaskName());

        taskDescription.setText("Description : ");
        editDescription.setText(task.getTaskDesc());

        taskStatus.setText("Status : ");
        editStatus.setAdapter(adapter);

        if(task.getStatus().equals("Completed")) {
            editStatus.setSelection(0);
        }

        else if (task.getStatus().equals("OverDue")) {
            editStatus.setSelection(2);
        }

        else if (task.getStatus().equals("Pending")){
            editStatus.setSelection(1);
        }

        Button finishButton = findViewById(R.id.addTaskButton);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmUpdateDialog();
            }
        });

    }


    private void showConfirmUpdateDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmation")
                .setMessage("Are you sure you want to perform this action?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        updateTask();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }

    private void updateTask(){
        TextInputEditText editName = findViewById(R.id.taskName);
        EditText editDesc = findViewById(R.id.editTaskDescription);
        TextView editDate = findViewById(R.id.dueDateTextView);
        Spinner taskStatus = findViewById(R.id.editTaskStatus);

        Intent intent = getIntent();
        int id = (int)intent.getExtras().get("task_id");
        task task = MainActivity.taskList.get(id);

        task.setTaskName(String.valueOf(editName.getText()));
        task.setTaskDesc(String.valueOf(editDesc.getText()));
        task.setStatus(String.valueOf(taskStatus.getSelectedItem()));

        String newDate = String.valueOf(editDate.getText());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Date parsedDate = dateFormat.parse(newDate);
            task.setDueDate(parsedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        saveTasks(MainActivity.taskList);
        Intent newIntent = new Intent(edit_task.this,
                MainActivity.class);
        startActivity(newIntent);
    }

    private void saveTasks(List<task> taskList) {
        Gson gson = new Gson();
        String taskString = gson.toJson(taskList);

        editor.putString(DATA, taskString);
        editor.apply();
    }
}