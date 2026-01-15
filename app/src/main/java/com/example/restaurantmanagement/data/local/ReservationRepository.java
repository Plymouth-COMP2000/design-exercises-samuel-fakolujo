package com.example.restaurantmanagement.data.local;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import com.example.restaurantmanagement.models.Reservation;

public class ReservationRepository {

    private final DatabaseHelper dbHelper;

    public ReservationRepository(Context c) {
        dbHelper = new DatabaseHelper(c);
    }

    public long createReservation(String username, String date, String timeSlot,
                                  int guests, String specialRequest) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.R_USERNAME, username);
        cv.put(DatabaseHelper.R_DATE, date);
        cv.put(DatabaseHelper.R_TIMESLOT, timeSlot);
        cv.put(DatabaseHelper.R_GUESTS, guests);
        cv.put(DatabaseHelper.R_SPECIAL, specialRequest);
        cv.put(DatabaseHelper.R_STATUS, "BOOKED");
        cv.put(DatabaseHelper.R_CREATED, System.currentTimeMillis());
        return db.insert(DatabaseHelper.T_RES, null, cv);
    }

    public int cancelReservation(long reservationId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.R_STATUS, "CANCELLED");
        return db.update(DatabaseHelper.T_RES, cv,
                DatabaseHelper.R_ID + "=?",
                new String[]{String.valueOf(reservationId)});
    }

    public List<Reservation> getUserReservations(String username) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Reservation> out = new ArrayList<>();

        Cursor c = db.query(DatabaseHelper.T_RES, null,
                DatabaseHelper.R_USERNAME + "=?",
                new String[]{username},
                null, null, DatabaseHelper.R_CREATED + " DESC");

        while (c.moveToNext()) {
            out.add(map(c));
        }
        c.close();
        return out;
    }

    public List<Reservation> getAllReservations() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Reservation> out = new ArrayList<>();

        Cursor c = db.query(DatabaseHelper.T_RES, null,
                null, null,
                null, null, DatabaseHelper.R_CREATED + " DESC");

        while (c.moveToNext()) {
            out.add(map(c));
        }
        c.close();
        return out;
    }

    private Reservation map(Cursor c) {
        return new Reservation(
                c.getLong(c.getColumnIndexOrThrow(DatabaseHelper.R_ID)),
                c.getString(c.getColumnIndexOrThrow(DatabaseHelper.R_USERNAME)),
                c.getString(c.getColumnIndexOrThrow(DatabaseHelper.R_DATE)),
                c.getString(c.getColumnIndexOrThrow(DatabaseHelper.R_TIMESLOT)),
                c.getInt(c.getColumnIndexOrThrow(DatabaseHelper.R_GUESTS)),
                c.getString(c.getColumnIndexOrThrow(DatabaseHelper.R_SPECIAL)),
                c.getString(c.getColumnIndexOrThrow(DatabaseHelper.R_STATUS)),
                c.getLong(c.getColumnIndexOrThrow(DatabaseHelper.R_CREATED))
        );
    }
}
