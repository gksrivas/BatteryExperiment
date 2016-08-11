package com.battery.experiment.Model;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Gaurav on 11/08/16.
 */

public class BatteryExperimentResult {
    public static int startBatteryLevel;
    public int batteryLevel;
    private static Date experimentStartDate;
    public String experimentStartTime;

    public String getAverageTimePerPatteryPercentage() {
        Date date = new Date();
        Log.d("Start Time", "" + experimentStartDate);
        Log.d("Current Time", "" + date);
        long elapsedSeconds = (date.getTime() - experimentStartDate.getTime()) / 1000;
        return "" + elapsedSeconds;
    }

    public String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        experimentStartDate = date;
        return dateFormat.format(date);
    }
}
