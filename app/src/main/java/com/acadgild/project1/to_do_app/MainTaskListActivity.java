package com.acadgild.project1.to_do_app;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;


public class MainTaskListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, View.OnFocusChangeListener {

    ListView listView;

    ArrayList<Task> taskArrayList;
    TaskAdapter taskArrayAdapter;

    LayoutInflater inflater;

    DB dbHelper;

    @Override
    protected void onResume() {
        super.onResume();

        refreshListViewContents();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_tasks);

        // initializing Dialog
        inflater = LayoutInflater.from(this);

        // initializing DB
        dbHelper = new DB(this);

        // initializing list view to display
        listView = (ListView) findViewById(R.id.listView);

        taskArrayList = dbHelper.getAllTasks();
        Collections.sort(taskArrayList, new DateCompare());
        taskArrayAdapter = new TaskAdapter(this, taskArrayList);

        // sett adapter
        listView.setAdapter(taskArrayAdapter);

        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.task, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.newTaskMenu:
            {
                View view = inflater.inflate(R.layout.task_entry, null);

                view.findViewById(R.id.txtTitle).setOnFocusChangeListener(this);

                AlertDialog.Builder mBuilder = createAlertDialog(view);
                mBuilder.show();
                return true;
            }
            case R.id.completedTaskList: {

                Intent intent = new Intent(MainTaskListActivity.this, FinishedTaskListActivity.class);
                startActivity(intent);

                return true;
            }

        }

        return super.onOptionsItemSelected(item);
    }

    private AlertDialog.Builder createAlertDialog(final View view) {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);

        mBuilder.setView(view);
        mBuilder.setPositiveButton(getResources().getString(R.string.dialogPosBtn), new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Get the inputs from this and insert it into the DB
                EditText titleTxt = (EditText) view.findViewById(R.id.txtTitle);
                String title = titleTxt.getText().toString().trim();
                EditText descTxt = (EditText) view.findViewById(R.id.txtDescription);
                String description = descTxt.getText().toString();
                DatePicker datePicker = (DatePicker) view.findViewById(R.id.datePicker);
                String targetDate =
                        String.format("%02d", datePicker.getDayOfMonth()) +"/" +
                                String.format("%02d", Integer.valueOf(datePicker.getMonth()+1)) + "/" + datePicker.getYear();

                if (title.isEmpty())
                {
                    Toast.makeText(MainTaskListActivity.this, "Task not included", Toast.LENGTH_SHORT).show();
                    return;
                }

                // To insert a row in DB
                dbHelper.insertTask(title, description, targetDate, 0);

                // To notify the ListView about the dataset change
                MainTaskListActivity.this.refreshListViewContents();

                Toast.makeText(MainTaskListActivity.this, "Title : " + title + "\nDescription : " + description + "\nTargetDate : " + targetDate, Toast.LENGTH_LONG).show();
            }
        });
        mBuilder.setNegativeButton(getResources().getString(R.string.dialogNegBtn), new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                Toast.makeText(TaskActivity.this, "Cancel button clicked", Toast.LENGTH_LONG).show();
            }
        });
        mBuilder.setCancelable(false);
        return mBuilder;
    }

    /**
     * Method to refresh the contents of the List view
     */
    private void refreshListViewContents() {
        taskArrayList = dbHelper.getAllTasks();
        Collections.sort(taskArrayList, new DateCompare());
        taskArrayAdapter.setTaskList(taskArrayList);
        taskArrayAdapter.notifyDataSetChanged();
    }

    private AlertDialog.Builder editTaskDialog(final View view, final Task theTask) {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);

        mBuilder.setView(view);
        mBuilder.setPositiveButton(getResources().getString(R.string.dialogPosBtn), new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Get the inputs from this and insert it into the DB
                EditText titleTxt = (EditText) view.findViewById(R.id.txtTitle);
                String title = titleTxt.getText().toString().trim();
                EditText descTxt = (EditText) view.findViewById(R.id.txtDescription);
                String description = descTxt.getText().toString();
                DatePicker datePicker = (DatePicker) view.findViewById(R.id.datePicker);
                String targetDate = String.format("%02d", datePicker.getDayOfMonth()) +"/" +
                        String.format("%02d", Integer.valueOf(datePicker.getMonth()+1)) + "/" + datePicker.getYear();

                if (title.isEmpty())
                {
                    Toast.makeText(MainTaskListActivity.this, "Task not updated!!", Toast.LENGTH_SHORT).show();
                    return;
                }
                // To insert a row in DB
                dbHelper.updateTask(theTask, title, description, targetDate);

                // To notify the ListView about the dataset change
                MainTaskListActivity.this.refreshListViewContents();

                Toast.makeText(MainTaskListActivity.this, "Title : " + title + "\nDescription : " + description + "\nTargetDate : " + targetDate, Toast.LENGTH_LONG).show();
            }
        });
        mBuilder.setNegativeButton(getResources().getString(R.string.dialogNegBtn), new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                Toast.makeText(TaskActivity.this, "Cancel button clicked", Toast.LENGTH_LONG).show();
            }
        });
        mBuilder.setCancelable(false);
        return mBuilder;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // Should be able to edit the data - by launching the dialog

        Task currentTask = (Task)parent.getAdapter().getItem(position);

        View dialogView = inflater.inflate(R.layout.task_entry, null);

        // set Value for the dialog fields
        EditText txtTitle = (EditText) dialogView.findViewById(R.id.txtTitle);
        EditText txtDescription = (EditText) dialogView.findViewById(R.id.txtDescription);
        DatePicker targetDate = (DatePicker) dialogView.findViewById(R.id.datePicker);

        txtTitle.setOnFocusChangeListener(this);

        txtTitle.setText(currentTask.getTaskTitle());
        txtDescription.setText(currentTask.getTaskDescription());
        String[] dateArray = currentTask.getTargetDate().split("/");
        targetDate.init (Integer.valueOf(dateArray[2]),
                Integer.valueOf(dateArray[1])-1,
                Integer.valueOf(dateArray[0]),
                null);

        AlertDialog.Builder mBuilder = editTaskDialog(dialogView, currentTask);
        mBuilder.show();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        // Should mark the given one as completed
        Task theTask = (Task) parent.getAdapter().getItem(position);

        if (theTask.getStatus() == 1) {
            return true;
        }

        int updateCount = dbHelper.updateTaskStatus(theTask.getId(), 1);

        refreshListViewContents();
        Toast.makeText(this, "Congrats!! on completing - " + theTask.getTaskTitle(), Toast.LENGTH_LONG).show();
        return true;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (v.getId() == R.id.txtTitle) {
            EditText title = (EditText)v;
            if (title.getText().length() < 1)
            {
                title.setError("Title cant be empty!!");
            }
        }
    }
}
