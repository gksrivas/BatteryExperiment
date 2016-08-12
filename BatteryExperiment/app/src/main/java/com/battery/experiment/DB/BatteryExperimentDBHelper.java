package com.battery.experiment.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.battery.experiment.Model.BatteryExperimentResult;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.database.sqlite.SQLiteDatabase.CONFLICT_REPLACE;

/**
 * Created by Gaurav on 11/08/16.
 */

public class BatteryExperimentDBHelper  extends SQLiteOpenHelper {
    private Context _context;
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "BatteryExperiment.db";

    private static final String SQL_CREATE_FIELDS_TABLE =
            "CREATE TABLE " + BatteryExperimentDBContract.FieldsTable.TABLE_NAME + " (" +
                    BatteryExperimentDBContract.FieldsTable._ID + " INTEGER PRIMARY KEY," +
                    BatteryExperimentDBContract.FieldsTable.COLUMN_NAME_START_BATTERY_LEVEL + " INTEGER DEFAULT 0," +
                    BatteryExperimentDBContract.FieldsTable.COLUMN_NAME_ELAPSED_TIME + " INTEGER DEFAULT 0," +
                    BatteryExperimentDBContract.FieldsTable.COLUMN_NAME_CONSUMED_BATTERY + " INTEGER DEFAULT 0," +
                    BatteryExperimentDBContract.FieldsTable.COLUMN_NAME_IS_EXPERIMENT_RUNNING + " INTEGER DEFAULT 0," +
                    BatteryExperimentDBContract.FieldsTable.COLUMN_NAME_START_EXPERIMENT_TIME + " TIMESTAMP" +
                    ")";

    private static final String SQL_DELETE_FIELDS_TABLE =
            "DROP TABLE IF EXISTS " + BatteryExperimentDBContract.FieldsTable.TABLE_NAME;

    public BatteryExperimentDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        _context = context;
    }

    public void onCreate(SQLiteDatabase db) {
        Log.d("AppDetailsDBHelper", "DB Created");
        db.execSQL(SQL_CREATE_FIELDS_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO: implement upgrade policy
        db.execSQL(SQL_DELETE_FIELDS_TABLE);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    /*
     * Custom helpers.
     */

    public void clearDB() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(SQL_DELETE_FIELDS_TABLE);
        onCreate(db);
        db.close();
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    // Insert a processed request field. Returns the primary key of the new row.
    // Returns -1 on error.
    public long insertProcessedField(BatteryExperimentResult batteryExperimentResult) {
        SQLiteDatabase db = this.getWritableDatabase();

        String tableName = BatteryExperimentDBContract.FieldsTable.TABLE_NAME;
        ContentValues values = new ContentValues();
        values.put(BatteryExperimentDBContract.FieldsTable.COLUMN_NAME_START_BATTERY_LEVEL,
                batteryExperimentResult.startBatteryLevel);
        values.put(BatteryExperimentDBContract.FieldsTable.COLUMN_NAME_START_EXPERIMENT_TIME,
                getDateTime());

        // Insert the new row, returning the primary key value of the new row
        return db.insertWithOnConflict(tableName, null, values, CONFLICT_REPLACE);
    }

}
