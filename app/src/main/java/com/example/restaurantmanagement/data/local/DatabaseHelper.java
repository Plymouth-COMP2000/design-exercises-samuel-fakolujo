package com.example.restaurantmanagement.data.local;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "restaurant.db";
    public static final int DB_VERSION = 1;

    // MENU table
    public static final String T_MENU = "menu";
    public static final String M_ID = "id";
    public static final String M_NAME = "name";
    public static final String M_CATEGORY = "category";
    public static final String M_PRICE = "price";
    public static final String M_ISNEW = "is_new";

    // RESERVATIONS table
    public static final String T_RES = "reservations";
    public static final String R_ID = "id";
    public static final String R_USERNAME = "username";
    public static final String R_DATE = "date";
    public static final String R_TIMESLOT = "time_slot";
    public static final String R_GUESTS = "guests";
    public static final String R_SPECIAL = "special_request";
    public static final String R_STATUS = "status";
    public static final String R_CREATED = "created_at";

    // TASKS table (staff daily tasks)
    public static final String T_TASKS = "tasks";
    public static final String T_ID = "id";
    public static final String T_TEXT = "text";
    public static final String T_DONE = "done";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createMenu =
                "CREATE TABLE " + T_MENU + " (" +
                        M_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        M_NAME + " TEXT NOT NULL, " +
                        M_CATEGORY + " TEXT NOT NULL, " +
                        M_PRICE + " REAL NOT NULL, " +
                        M_ISNEW + " INTEGER NOT NULL DEFAULT 0" +
                        ");";

        String createRes =
                "CREATE TABLE " + T_RES + " (" +
                        R_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        R_USERNAME + " TEXT NOT NULL, " +
                        R_DATE + " TEXT NOT NULL, " +
                        R_TIMESLOT + " TEXT NOT NULL, " +
                        R_GUESTS + " INTEGER NOT NULL, " +
                        R_SPECIAL + " TEXT, " +
                        R_STATUS + " TEXT NOT NULL, " +
                        R_CREATED + " INTEGER NOT NULL" +
                        ");";

        String createTasks =
                "CREATE TABLE " + T_TASKS + " (" +
                        T_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        T_TEXT + " TEXT NOT NULL, " +
                        T_DONE + " INTEGER NOT NULL DEFAULT 0" +
                        ");";

        db.execSQL(createMenu);
        db.execSQL(createRes);
        db.execSQL(createTasks);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // beginner-safe reset
        db.execSQL("DROP TABLE IF EXISTS " + T_MENU);
        db.execSQL("DROP TABLE IF EXISTS " + T_RES);
        db.execSQL("DROP TABLE IF EXISTS " + T_TASKS);
        onCreate(db);
    }
}

