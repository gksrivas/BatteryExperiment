package com.battery.experiment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.battery.experiment.Model.BatteryExperimentResult;
import com.battery.experiment.Util.PrefManager;

import java.util.Date;

public class MainActivity extends Activity {
    private PrefManager prefManager;

    private static int counter;
    private static BatteryExperimentResult batteryExperimentResult;
    private TextView mBatteryLevelText;
    private ProgressBar mBatteryLevelProgress;
    private TextView mExperimentStartTime;
    private TextView mTimeElapsed;
    private TextView mPercentageBatteryDecrease;
    private TextView mAvgTimePerBatteryPercent;

    private BroadcastReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if ((savedInstanceState != null) && (savedInstanceState.getSerializable("BatteryExperimentObject") != null)) {
            Log.v("", "Inside of Bundle Check");
            counter = savedInstanceState.getInt("Counter");
            batteryExperimentResult = (BatteryExperimentResult) savedInstanceState.getSerializable("BatteryExperimentObject");
        } else {
            Log.d("First Time Launch", "Yes");
            counter = 0;
            batteryExperimentResult = new BatteryExperimentResult();
        }

        mBatteryLevelText = (TextView) findViewById(R.id.batteryLevel);
        mBatteryLevelProgress = (ProgressBar) findViewById(R.id.batteryLevelBar);
        mExperimentStartTime = (TextView) findViewById(R.id.experimentStartTime);
        mTimeElapsed = (TextView) findViewById(R.id.timeElapsed);
        mPercentageBatteryDecrease = (TextView) findViewById(R.id.percentDecreaseInBattery);
        mAvgTimePerBatteryPercent = (TextView) findViewById(R.id.averageTimePerBatteryPercentage);

        mReceiver = new BatteryBroadcastReceiver();
    }

    @Override
    protected void onStart() {
        registerReceiver(mReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(mReceiver);
        super.onStop();
    }

    private class BatteryBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            mBatteryLevelText.setText(getString(R.string.battery_level) + " " + level);
            mBatteryLevelProgress.setProgress(level);

            if (counter == 0) {
                batteryExperimentResult.experimentStartTime = new Date();
                batteryExperimentResult.startBatteryLevel = level;
                batteryExperimentResult.batteryLevel = level;
                counter++;
            } else {
                batteryExperimentResult.batteryLevel = level;
                counter++;
            }

            mExperimentStartTime.setText(
                    getString(R.string.experiment_start_time) + " "
                            + batteryExperimentResult.getExperimentStartTime()
            );

            mPercentageBatteryDecrease.setText(
                    getString(R.string.battery_decrease) + " "
                            + batteryExperimentResult.getPercentBatteryDecrease()
            );

            mAvgTimePerBatteryPercent.setText(
                    getString(R.string.average_time_per_battery_percent) + " "
                            + batteryExperimentResult.getAverageTimePerBatteryPercentage()
            );

            mTimeElapsed.setText(
                    getString(R.string.time_elapsed) + " "
                            + batteryExperimentResult.getElapsedTime()
            );
            Log.d("Counter:", "" + counter);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //new PrefManager(this).setFirstTimeLaunch(true);
    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        state.putSerializable("BatteryExperimentObject", batteryExperimentResult);
        state.putInt("Counter", counter);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.v("", "Inside of onRestoreInstanceState");
        counter = savedInstanceState.getInt("Counter");
        batteryExperimentResult = (BatteryExperimentResult) savedInstanceState.getSerializable("BatteryExperimentObject");
    }

}