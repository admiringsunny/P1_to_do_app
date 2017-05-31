package com.acadgild.project1.to_do_app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DB extends SQLiteOpenHelper {

    private static final String DB_NAME = "TODOTASK";
    private static final int DB_VERSION = 1;

    private static final String TABLE_NAME  = "TodoTasks";
    private static final String KEY_ID      = "id";
    private static final String KEY_TITLE   = "title";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_DATE    = "date";
    private static final String KEY_STATUS  = "status";

    private SQLiteDatabase sqLiteDatabase;

    public DB(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "create table " + TABLE_NAME + " ( " +
                KEY_ID + " integer primary key, " +
                KEY_TITLE + " text, " +
                KEY_DESCRIPTION + " text, " +
                KEY_DATE + " text, " +
                KEY_STATUS + " integer" +
                " )";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * Method to insert a single data row into the table - todotasks
     * @param title
     * @param desc
     * @param date
     * @param status
     */
    public void insertTask (String title, String desc, String date, int status) {
        /*
            1. Create the Database object - writable
            2. Create Content Value
            3. Insert the content
         */

        sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_TITLE, title);
        contentValues.put(KEY_DESCRIPTION, desc);
        contentValues.put(KEY_DATE, date);
        contentValues.put(KEY_STATUS, status);

        sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
        sqLiteDatabase.close();
    }

    public ArrayList<Task> getAllCompletedTasks () {
        ArrayList<Task> taskList = new ArrayList<>();

        sqLiteDatabase = this.getReadableDatabase();

        String selectQuery = "select * from " + TABLE_NAME + " where " + KEY_STATUS + " = 1";

        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {

                Task task = new Task();
                task.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                task.setTaskTitle(cursor.getString(cursor.getColumnIndex(KEY_TITLE)));
                task.setTaskDescription(cursor.getString(cursor.getColumnIndex(KEY_DESCRIPTION)));
                task.setTargetDate(cursor.getString(cursor.getColumnIndex(KEY_DATE)));
                task.setStatus(cursor.getInt(cursor.getColumnIndex(KEY_STATUS)));

                taskList.add(task);

            } while (cursor.moveToNext());

        }

        sqLiteDatabase.close();
        return taskList;
    }

    public ArrayList<Task> getAllTasks () {
        ArrayList<Task> taskList = new ArrayList<>();

        sqLiteDatabase = this.getReadableDatabase();

        String selectQuery = "select * from " + TABLE_NAME;

        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {

                Task task = new Task();
                task.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                task.setTaskTitle(cursor.getString(cursor.getColumnIndex(KEY_TITLE)));
                task.setTaskDescription(cursor.getString(cursor.getColumnIndex(KEY_DESCRIPTION)));
                task.setTargetDate(cursor.getString(cursor.getColumnIndex(KEY_DATE)));
                task.setStatus(cursor.getInt(cursor.getColumnIndex(KEY_STATUS)));

                taskList.add(task);

            } while (cursor.moveToNext());

        }

        sqLiteDatabase.close();
        return taskList;
    }

    /**
     * Method to update the status of the given Column ID
     * @param whereID
     * @param status
     * @return
     */
    public int updateTaskStatus (int whereID, int status) {
        sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_STATUS, status);

        int count = sqLiteDatabase.update(TABLE_NAME, values, KEY_ID+"=?", new String[]{String.valueOf(whereID)});
        sqLiteDatabase.close();

        return count;
    }

    public int updateTask(Task theTask, String title, String description, String targetDate) {
        sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, title);
        values.put(KEY_DESCRIPTION, description);
        values.put(KEY_DATE, targetDate);

        int count = sqLiteDatabase.update(TABLE_NAME, values, KEY_ID+"=?", new String[]{String.valueOf(theTask.getId())});

        sqLiteDatabase.close();
        return count;
    }

    public boolean deleteTask(int id) {
        sqLiteDatabase = this.getWritableDatabase();

        sqLiteDatabase.delete(TABLE_NAME, KEY_ID+"=?", new String[]{String.valueOf(id)});

        sqLiteDatabase.close();

        return true;
    }
}
