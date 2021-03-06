package com.example.android.ble_scanner.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.android.ble_scanner.data.DeviceContract.DeviceEntry;

public class DeviceDbHelper extends SQLiteOpenHelper {

    private String LOG_TAG = this.getClass().getSimpleName();
    public static int DATABASE_VERSION = 1;

    public static String CREATE_DEVICE_TABLE = "CREATE TABLE IF NOT EXISTS "+ DeviceEntry.TABLE_NAME +
            "( "+
                DeviceEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DeviceEntry.COLUMN_NAME + " TEXT, "+
                DeviceEntry.COLUMN_ADDRESS + " TEXT, "+
                DeviceEntry.COLUMN_RSSI + " INTEGER" +
            ")";

    public DeviceDbHelper( Context context) {
        super(context,DeviceEntry.TABLE_NAME, null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DEVICE_TABLE);
        Log.i(LOG_TAG,"exec sql: "+ CREATE_DEVICE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
