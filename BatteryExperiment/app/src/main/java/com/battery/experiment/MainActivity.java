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
import com.battery.experiment.Services.BatteryCheckerService;
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

        mBatteryLevelText = (TextView) findViewById(R.id.batteryLevel);
        mBatteryLevelProgress = (ProgressBar) findViewById(R.id.batteryLevelBar);
        mExperimentStartTime = (TextView) findViewById(R.id.experimentStartTime);
        mTimeElapsed = (TextView) findViewById(R.id.timeElapsed);
        mPercentageBatteryDecrease = (TextView) findViewById(R.id.percentDecreaseInBattery);
        mAvgTimePerBatteryPercent = (TextView) findViewById(R.id.averageTimePerBatteryPercentage);

        startService(new Intent(this, BatteryCheckerService.class));
    }
}