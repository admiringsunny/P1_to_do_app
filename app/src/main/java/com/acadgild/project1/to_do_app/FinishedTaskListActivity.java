package com.acadgild.project1.to_do_app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Collections;

public class FinishedTaskListActivity extends AppCompatActivity implements AdapterView.OnItemLongClickListener {

    ListView listView;
    Button mAdd;

    // For Custom ListView
    ArrayList<Task> taskArrayList;
    TaskAdapter taskArrayAdapter;

    // For Dialog Creation
    LayoutInflater mLayoutInflater;

    // For DB
    DB dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_tasks);

        // For Dialog
        mLayoutInflater = LayoutInflater.from(this);

        // For DB
        dbHelper = new DB(this);

        // Creating list view to display
        listView = (ListView) findViewById(R.id.listView);

        taskArrayList = dbHelper.getAllCompletedTasks();
        Collections.sort(taskArrayList, new DateCompare());
        taskArrayAdapter = new TaskAdapter(this, taskArrayList);

        listView.setAdapter(taskArrayAdapter);

        listView.setOnItemLongClickListener(this);
    }

    /**
     * Method to refresh the contents of the List view
     */
    private void refreshListViewContents() {
        taskArrayList = dbHelper.getAllCompletedTasks();
        taskArrayAdapter.setTaskList(taskArrayList);
        taskArrayAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        // Should mark the given one as completed
        Task theTask = (Task) parent.getAdapter().getItem(position);

        boolean isDeleted = dbHelper.deleteTask(theTask.getId());

        if (isDeleted)
        {
            refreshListViewContents();
            Toast.makeText(this, "Removed the completed task : " + theTask.getTaskTitle(), Toast.LENGTH_LONG).show();
        }

        return true;
    }


}
