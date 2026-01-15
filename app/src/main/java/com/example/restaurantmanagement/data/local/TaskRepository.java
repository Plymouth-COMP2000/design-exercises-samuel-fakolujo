package com.example.restaurantmanagement.data.local;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class TaskRepository {

    public static class Task {
        public long id;
        public String text;
        public int done;
        public Task(long id, String text, int done) {
            this.id = id; this.text = text; this.done = done;
        }
    }

    private final DatabaseHelper dbHelper;

    public TaskRepository(Context c) {
        dbHelper = new DatabaseHelper(c);
    }

    public void seedIfEmpty() {
        if (!getAll().isEmpty()) return;
        add("Clean tables");
        add("Check stock levels");
        add("Prepare cutlery");
        add("Update specials board");
    }

    public long add(String text) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.T_TEXT, text);
        cv.put(DatabaseHelper.T_DONE, 0);
        return db.insert(DatabaseHelper.T_TASKS, null, cv);
    }

    public int setDone(long id, int done) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.T_DONE, done);
        return db.update(DatabaseHelper.T_TASKS, cv,
                DatabaseHelper.T_ID + "=?",
                new String[]{String.valueOf(id)});
    }

    public List<Task> getAll() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Task> out = new ArrayList<>();
        Cursor c = db.query(DatabaseHelper.T_TASKS, null,
                null, null, null, null, DatabaseHelper.T_ID + " ASC");

        while (c.moveToNext()) {
            out.add(new Task(
                    c.getLong(c.getColumnIndexOrThrow(DatabaseHelper.T_ID)),
                    c.getString(c.getColumnIndexOrThrow(DatabaseHelper.T_TEXT)),
                    c.getInt(c.getColumnIndexOrThrow(DatabaseHelper.T_DONE))
            ));
        }
        c.close();
        return out;
    }
}
