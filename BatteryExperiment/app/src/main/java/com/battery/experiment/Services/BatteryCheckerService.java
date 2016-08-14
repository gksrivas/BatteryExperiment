package com.battery.experiment.Services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.IBinder;
import android.util.Log;

import com.battery.experiment.DB.BatteryExperimentDBHelper;
import com.battery.experiment.Model.BatteryExperimentResult;

import java.util.Date;
import java.util.UUID;

/**
 * Created by gary on 11/08/16.
 */

public class BatteryCheckerService extends Service {
    private static BatteryExperimentResult mBatteryExperimentResult;
    private BatteryExperimentDBHelper batteryExperimentDBHelper;
    private BroadcastReceiver mReceiver;

    public BatteryCheckerService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Initialize Battery experiment result object
        mBatteryExperimentResult = new BatteryExperimentResult();
        mBatteryExperimentResult.experimentRunning = -1;
        mBatteryExperimentResult.id = UUID.randomUUID().toString();

        //Initialize DB Helper
        batteryExperimentDBHelper = new BatteryExperimentDBHelper(getApplicationContext());

        //Register Battery broadcast rceiver
        mReceiver = new BatteryBroadcastReceiver();
        registerReceiver(mReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Log.d("Inside", "Stop Service");
        stopSelf();
    }

    private class BatteryBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            Log.d("Level", "" + level);
            if(mBatteryExperimentResult.experimentRunning == -1) {
                mBatteryExperimentResult.experimentRunning = 0;
                mBatteryExperimentResult.startBatteryLevel = level;
                mBatteryExperimentResult.batteryLevel = level;
                mBatteryExperimentResult.experimentStartTime = new Date();
            } else {
                mBatteryExperimentResult.batteryLevel = level;
            }
            batteryExperimentDBHelper.insertProcessedField(mBatteryExperimentResult);
        }
    }

    @Override
    public void onDestroy() {
        Log.d("Stop Service", "onDestroy");
        unregisterReceiver(mReceiver);
        batteryExperimentDBHelper.close();
    }
}