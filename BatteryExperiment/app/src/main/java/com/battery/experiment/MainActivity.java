package com.battery.experiment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.battery.experiment.Model.BatteryExperimentResult;

public class MainActivity extends Activity {
    private static int counter = 0;
    private TextView mBatteryLevelText;
    private ProgressBar mBatteryLevelProgress;
    private TextView mExperimentStartTime;
    private TextView mAvgTimePerBatteryPercent;

    private BroadcastReceiver mReceiver;

    private static BatteryExperimentResult batteryExperimentResult = new BatteryExperimentResult();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBatteryLevelText = (TextView) findViewById(R.id.batteryLevel);
        mBatteryLevelProgress = (ProgressBar) findViewById(R.id.batteryLevelBar);
        mExperimentStartTime = (TextView) findViewById(R.id.experimentStartTime);
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
                batteryExperimentResult.experimentStartTime = batteryExperimentResult.getDateTime();
                mExperimentStartTime.setText(
                        getString(R.string.experiment_start_time) + " "
                                + batteryExperimentResult.experimentStartTime
                );
                BatteryExperimentResult.startBatteryLevel = level;
                counter++;
            } else {
                batteryExperimentResult.batteryLevel = level;
            }

            mAvgTimePerBatteryPercent.setText(
                    batteryExperimentResult.getAverageTimePerPatteryPercentage()
            );
        }
    }
}