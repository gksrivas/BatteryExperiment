package com.battery.experiment.Model;

import java.util.Date;

/**
 * Created by Gaurav on 12/08/16.
 */

public class BatteryResultDBModel {
    public String experimentId;
    public int batteryLevel;
    public Date experimentStartTime;
    public int elapsedTime;
    public int batteryConsumed;
    public int avgTimePerBatteryPercent;
    public int isExperimentRunning;
}
