package com.battery.experiment.Model;

import android.util.Log;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Gaurav on 11/08/16.
 */

public class BatteryExperimentResult implements Serializable {

    public String id;
    public int startBatteryLevel;
    public Date experimentStartTime;

    public int batteryLevel;
    private long elapsedTimeInSeconds;
    public int experimentRunning;

    public String getAverageTimePerBatteryPercentage() {
        Date date = new Date();
        Log.d("Start Time", "" + experimentStartTime);
        Log.d("Current Time", "" + date);
        elapsedTimeInSeconds = (date.getTime() - experimentStartTime.getTime()) / 1000;

        if(getPercentBatteryDecrease() > 0){
            return "" + elapsedTimeInSeconds/getPercentBatteryDecrease();

        } else {
            return "N.A. (No decrease in battery yet)";

        }
    }

    public int getPercentBatteryDecrease() {
        int result = (startBatteryLevel - batteryLevel) > 0 ? (startBatteryLevel - batteryLevel) : 0;
        return result;
    }

    public long getElapsedTime() {
        return elapsedTimeInSeconds;
    }

    public String getExperimentStartTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return dateFormat.format(experimentStartTime);
    }
}
