package com.example.taskmanagement;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private CustomTaskAdapter customAdapter;
    public static final String DATA = "DATA";
    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    public static List<task> taskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupSharedPrefs();
        taskList=loadTasks();


        ListView listView = (ListView)findViewById(R.id.taskView);
        customAdapter = new CustomTaskAdapter(this, taskList,listView);
        listView.setAdapter(customAdapter);




        ImageButton addTaskButton = findViewById(R.id.addTaskButton);
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, addTask.class);
                startActivity(intent);
            }
        });

    }

    private void setupSharedPrefs() {
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        editor = prefs.edit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        customAdapter.notifyDataSetChanged();
    }

    private List<task> loadTasks(){
        Gson gson = new Gson();
        String str = prefs.getString(DATA, "");

        if(!str.equals("")) {
            task[] tasks = gson.fromJson(str, task[].class);
            return new ArrayList<>(List.of(tasks));
        }
        else{
            return new ArrayList<>(List.of());
        }
    }
    void saveTasks(List<task> taskList) {
        Gson gson = new Gson();
        String taskString = gson.toJson(taskList);

        editor.putString(DATA, taskString);
        editor.commit();
    }
}