package com.battery.experiment.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.battery.experiment.Model.BatteryExperimentResult;
import com.battery.experiment.Model.BatteryResultDBModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
                    BatteryExperimentDBContract.FieldsTable._ID + " TEXT PRIMARY KEY," +
                    BatteryExperimentDBContract.FieldsTable.COLUMN_NAME_START_BATTERY_LEVEL + " INTEGER DEFAULT 0," +
                    BatteryExperimentDBContract.FieldsTable.COLUMN_NAME_CURRENT_BATTERY_LEVEL + " INTEGER DEFAULT 0," +
                    BatteryExperimentDBContract.FieldsTable.COLUMN_NAME_ELAPSED_TIME + " INTEGER DEFAULT 0," +
                    BatteryExperimentDBContract.FieldsTable.COLUMN_NAME_CONSUMED_BATTERY + " INTEGER DEFAULT 0," +
                    BatteryExperimentDBContract.FieldsTable.COLUMN_NAME_IS_EXPERIMENT_RUNNING + " INTEGER DEFAULT 100," +
                    BatteryExperimentDBContract.FieldsTable.COLUMN_NAME_AVG_TIME_PER_BATTERY + " TEXT NOT NULL," +
                    BatteryExperimentDBContract.FieldsTable.COLUMN_NAME_START_EXPERIMENT_TIME + " TIMESTAMP" +
                    ")";

    private static final String SQL_DELETE_FIELDS_TABLE =
            "DROP TABLE IF EXISTS " + BatteryExperimentDBContract.FieldsTable.TABLE_NAME;

    public BatteryExperimentDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        _context = context;
    }

    public void onCreate(SQLiteDatabase db) {
        Log.d("BatteryExperiment", "DB Created");
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

    private String getDateTime(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return dateFormat.format(date);
    }

    // Insert a processed request field. Returns the primary key of the new row.
    // Returns -1 on error.
    public long insertProcessedField(BatteryExperimentResult batteryExperimentResult) {
        SQLiteDatabase db = this.getWritableDatabase();

        String tableName = BatteryExperimentDBContract.FieldsTable.TABLE_NAME;
        ContentValues values = new ContentValues();

        values.put(BatteryExperimentDBContract.FieldsTable._ID,
                batteryExperimentResult.id);
        values.put(BatteryExperimentDBContract.FieldsTable.COLUMN_NAME_START_BATTERY_LEVEL,
                batteryExperimentResult.startBatteryLevel);
        values.put(BatteryExperimentDBContract.FieldsTable.COLUMN_NAME_CURRENT_BATTERY_LEVEL,
                batteryExperimentResult.batteryLevel);
        values.put(BatteryExperimentDBContract.FieldsTable.COLUMN_NAME_START_EXPERIMENT_TIME,
                getDateTime(batteryExperimentResult.experimentStartTime));
        values.put(BatteryExperimentDBContract.FieldsTable.COLUMN_NAME_ELAPSED_TIME,
                batteryExperimentResult.getElapsedTime());
        values.put(BatteryExperimentDBContract.FieldsTable.COLUMN_NAME_CONSUMED_BATTERY,
                batteryExperimentResult.getPercentBatteryDecrease());
        values.put(BatteryExperimentDBContract.FieldsTable.COLUMN_NAME_AVG_TIME_PER_BATTERY,
                batteryExperimentResult.getAverageTimePerBatteryPercentage());
        values.put(BatteryExperimentDBContract.FieldsTable.COLUMN_NAME_IS_EXPERIMENT_RUNNING,
                batteryExperimentResult.experimentRunning);


        // Insert the new row, returning the primary key value of the new row
        return db.insertWithOnConflict(tableName, null, values, CONFLICT_REPLACE);
    }

    public ArrayList<BatteryResultDBModel> getBatteryExperimentDetails() {
        String tableName = BatteryExperimentDBContract.FieldsTable.TABLE_NAME;

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                BatteryExperimentDBContract.FieldsTable._ID,
                BatteryExperimentDBContract.FieldsTable.COLUMN_NAME_CURRENT_BATTERY_LEVEL,
                BatteryExperimentDBContract.FieldsTable.COLUMN_NAME_START_EXPERIMENT_TIME,
                BatteryExperimentDBContract.FieldsTable.COLUMN_NAME_ELAPSED_TIME,
                BatteryExperimentDBContract.FieldsTable.COLUMN_NAME_CONSUMED_BATTERY,
                BatteryExperimentDBContract.FieldsTable.COLUMN_NAME_AVG_TIME_PER_BATTERY,
                BatteryExperimentDBContract.FieldsTable.COLUMN_NAME_IS_EXPERIMENT_RUNNING
        };

        SQLiteDatabase db = this.getReadableDatabase();

        String selection = BatteryExperimentDBContract.FieldsTable.COLUMN_NAME_IS_EXPERIMENT_RUNNING + "=?";
        String[] selectionArgs = {"0"};


        Cursor c = db.query(true,
                tableName,
                projection,                              // The columns to return
                selection,                               // The columns for the WHERE clause
                selectionArgs,                           // The values for the WHERE clause
                null,                                    // don't group the rows
                null,                                    // don't filter by row groups
                null,                                    // no sort order
                null                                     // limit on results
        );

        if (c.getCount() == 0) {
            Log.d("NO", "Experiment done yet.");
            return null;
        }

        ArrayList<BatteryResultDBModel> results = new ArrayList<>();
        while (c.moveToNext()) {
            BatteryResultDBModel batteryResultDBModel = new BatteryResultDBModel();

            batteryResultDBModel.experimentId =
                    c.getString(c.getColumnIndex(BatteryExperimentDBContract.FieldsTable._ID));

            batteryResultDBModel.batteryLevel =
                    c.getInt(c.getColumnIndex(BatteryExperimentDBContract.FieldsTable.COLUMN_NAME_CURRENT_BATTERY_LEVEL));

            batteryResultDBModel.experimentStartTime =
                    c.getString(c.getColumnIndex(BatteryExperimentDBContract.FieldsTable.COLUMN_NAME_START_EXPERIMENT_TIME));

            batteryResultDBModel.elapsedTime =
                    c.getInt(c.getColumnIndex(BatteryExperimentDBContract.FieldsTable.COLUMN_NAME_ELAPSED_TIME));

            batteryResultDBModel.batteryConsumed =
                    c.getInt(c.getColumnIndex(BatteryExperimentDBContract.FieldsTable.COLUMN_NAME_CONSUMED_BATTERY));

            batteryResultDBModel.avgTimePerBatteryPercent =
                    c.getInt(c.getColumnIndex(BatteryExperimentDBContract.FieldsTable.COLUMN_NAME_AVG_TIME_PER_BATTERY));

            batteryResultDBModel.isExperimentRunning =
                    c.getInt(c.getColumnIndex(BatteryExperimentDBContract.FieldsTable.COLUMN_NAME_IS_EXPERIMENT_RUNNING));

            results.add(batteryResultDBModel);
        }
        c.close();

        return results;
    }

    public boolean isAnyExperimentRunning() {
        int results;
        String tableName = BatteryExperimentDBContract.FieldsTable.TABLE_NAME;

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                BatteryExperimentDBContract.FieldsTable._ID,
        };

        SQLiteDatabase db = this.getReadableDatabase();

        String selection = BatteryExperimentDBContract.FieldsTable.COLUMN_NAME_IS_EXPERIMENT_RUNNING + "=?";
        String[] selectionArgs = {"0"};


        Cursor c = db.query(true,
                tableName,
                projection,                              // The columns to return
                selection,                               // The columns for the WHERE clause
                selectionArgs,                           // The values for the WHERE clause
                null,                                    // don't group the rows
                null,                                    // don't filter by row groups
                null,                                    // no sort order
                null                                     // limit on results
        );

        results = c.getCount();
        c.close();

        if (results == 0) {
            Log.d("NO", "Experiment done yet.");
            return false;
        } else {
            return true;
        }
    }
}
