package com.example.taskmanagement;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class CustomTaskAdapter extends BaseAdapter {

    private final Context context;
    public static final String DATA = "DATA";
    SharedPreferences prefs;

    SharedPreferences.Editor editor;

    private List<task> taskList;

    public CustomTaskAdapter(Context context, List<task> taskList, ListView listView) {
        this.context = context;
        this.taskList= MainActivity.taskList;
    }

    @Override
    public int getCount() {
        return taskList.size();
    }

    @Override
    public Object getItem(int position) {
        return taskList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        task task = (task) getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.task_layout, parent, false);
        }

        LinearLayout taskLayout = convertView.findViewById(R.id.taskLayout);
        TextView taskNameTextView = convertView.findViewById(R.id.taskNameTextView);
        TextView dueDateTextView = convertView.findViewById(R.id.dueDateTextView);
        TextView statusTextView = convertView.findViewById(R.id.statusTextView);
        ImageButton deleteButton = convertView.findViewById(R.id.deleteButton);
        ImageButton editButton = convertView.findViewById(R.id.editButton);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, edit_task.class);
                intent.putExtra("task_id", position);
                context.startActivity(intent);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MainActivity.taskList.remove(position);
                saveTasks(MainActivity.taskList);
                notifyDataSetChanged();
            }
        });

        if (task != null) {

            taskNameTextView.setText(task.getTaskName());

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            dueDateTextView.setText("Due : " + dateFormat.format(task.getDueDate()));

            if ("Completed".equals(task.getStatus())) {
                taskLayout.setBackgroundColor(Color.GREEN);
            } else if ("OverDue".equals(task.getStatus())) {
                taskLayout.setBackgroundColor(Color.RED);
            } else if ("Pending".equals(task.getStatus())) {
                taskLayout.setBackgroundColor(Color.YELLOW);
            }

            statusTextView.setText(task.getStatus());
        }
        return convertView;
    }

    private void saveTasks(List<task> taskList) {
        if (context instanceof MainActivity) {
            ((MainActivity) context).saveTasks(taskList);
        }
    }
}