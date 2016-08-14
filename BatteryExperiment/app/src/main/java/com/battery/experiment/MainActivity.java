package com.battery.experiment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.battery.experiment.Services.BatteryCheckerService;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;


public class MainActivity extends Activity implements View.OnClickListener{

    private boolean runningExperiment;

    private TextView mBatteryLevelText;
    private ProgressBar mBatteryLevelProgress;
    private TextView mExperimentStartTime;
    private TextView mTimeElapsed;
    private TextView mPercentageBatteryDecrease;
    private TextView mAvgTimePerBatteryPercent;

    private Button mStartExperimentButton;

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

        mStartExperimentButton = (Button) findViewById(R.id.startExperiment);
        mStartExperimentButton.setOnClickListener(this);

        makeInfoUIInvisible();
    }

    @Override
    public void onClick(View view) {
        if (runningExperiment) {
            stopExperiment();
            Log.d("Stop", "Experiment");
        } else {
            startExperiment();
            Log.d("Start", "Experiment");
        }
    }

    private void startExperiment() {
        startService(new Intent(this, BatteryCheckerService.class));
        makeInfoUIVisible();
        mStartExperimentButton.setText("Stop Experiment");
        runningExperiment = true;
    }

    private void stopExperiment() {
        stopService(new Intent(this, BatteryCheckerService.class));
        makeInfoUIInvisible();
        mStartExperimentButton.setText("Start Experiment");
        runningExperiment = false;
    }

    private void makeInfoUIVisible() {
        mBatteryLevelText.setVisibility(VISIBLE);
        mBatteryLevelProgress.setVisibility(VISIBLE);
        mExperimentStartTime.setVisibility(VISIBLE);
        mTimeElapsed.setVisibility(VISIBLE);
        mPercentageBatteryDecrease.setVisibility(VISIBLE);
        mAvgTimePerBatteryPercent.setVisibility(VISIBLE);
    }

    private void makeInfoUIInvisible() {
        mBatteryLevelText.setVisibility(GONE);
        mBatteryLevelProgress.setVisibility(GONE);
        mExperimentStartTime.setVisibility(GONE);
        mTimeElapsed.setVisibility(GONE);
        mPercentageBatteryDecrease.setVisibility(GONE);
        mAvgTimePerBatteryPercent.setVisibility(GONE);
    }
}