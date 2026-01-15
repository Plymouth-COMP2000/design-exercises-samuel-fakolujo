package com.example.restaurantmanagement.data.local;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import com.example.restaurantmanagement.models.MenuItem;

public class MenuRepository {

    private final DatabaseHelper dbHelper;

    public MenuRepository(Context c) {
        dbHelper = new DatabaseHelper(c);
    }

    public long addItem(String name, String category, double price, int isNew) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.M_NAME, name);
        cv.put(DatabaseHelper.M_CATEGORY, category);
        cv.put(DatabaseHelper.M_PRICE, price);
        cv.put(DatabaseHelper.M_ISNEW, isNew);
        return db.insert(DatabaseHelper.T_MENU, null, cv);
    }

    public List<MenuItem> getByCategory(String category) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<MenuItem> out = new ArrayList<>();

        Cursor c = db.query(DatabaseHelper.T_MENU, null,
                DatabaseHelper.M_CATEGORY + "=?",
                new String[]{category},
                null, null, DatabaseHelper.M_NAME + " ASC");

        while (c.moveToNext()) {
            out.add(new MenuItem(
                    c.getLong(c.getColumnIndexOrThrow(DatabaseHelper.M_ID)),
                    c.getString(c.getColumnIndexOrThrow(DatabaseHelper.M_NAME)),
                    c.getString(c.getColumnIndexOrThrow(DatabaseHelper.M_CATEGORY)),
                    c.getDouble(c.getColumnIndexOrThrow(DatabaseHelper.M_PRICE)),
                    c.getInt(c.getColumnIndexOrThrow(DatabaseHelper.M_ISNEW))
            ));
        }
        c.close();
        return out;
    }

    public List<MenuItem> getNewAdditions(int limit) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<MenuItem> out = new ArrayList<>();

        Cursor c = db.query(DatabaseHelper.T_MENU, null,
                DatabaseHelper.M_ISNEW + "=?",
                new String[]{"1"},
                null, null, DatabaseHelper.M_ID + " DESC",
                String.valueOf(limit));

        while (c.moveToNext()) {
            out.add(new MenuItem(
                    c.getLong(c.getColumnIndexOrThrow(DatabaseHelper.M_ID)),
                    c.getString(c.getColumnIndexOrThrow(DatabaseHelper.M_NAME)),
                    c.getString(c.getColumnIndexOrThrow(DatabaseHelper.M_CATEGORY)),
                    c.getDouble(c.getColumnIndexOrThrow(DatabaseHelper.M_PRICE)),
                    c.getInt(c.getColumnIndexOrThrow(DatabaseHelper.M_ISNEW))
            ));
        }
        c.close();
        return out;
    }

    public int updateItem(long id, String name, String category, double price, int isNew) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.M_NAME, name);
        cv.put(DatabaseHelper.M_CATEGORY, category);
        cv.put(DatabaseHelper.M_PRICE, price);
        cv.put(DatabaseHelper.M_ISNEW, isNew);

        return db.update(DatabaseHelper.T_MENU, cv,
                DatabaseHelper.M_ID + "=?",
                new String[]{String.valueOf(id)});
    }

    public int deleteItem(long id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete(DatabaseHelper.T_MENU,
                DatabaseHelper.M_ID + "=?",
                new String[]{String.valueOf(id)});
    }


}

