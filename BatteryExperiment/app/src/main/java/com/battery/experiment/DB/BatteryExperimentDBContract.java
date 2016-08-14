package com.battery.experiment.DB;

import android.provider.BaseColumns;

/**
 * Created by Gaurav on 11/08/16.
 */

public class BatteryExperimentDBContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public BatteryExperimentDBContract() {}

    // Each entry in the FieldsTable corresponds to a field in a request along with its public
    // and private probabilities.
    public static abstract class FieldsTable implements BaseColumns {
        public static final String TABLE_NAME = "battery_experiment";
        public static final String COLUMN_NAME_START_BATTERY_LEVEL = "start_battery_level";
        public static final String COLUMN_NAME_START_EXPERIMENT_TIME = "start_experiment_time";
        public static final String COLUMN_NAME_ELAPSED_TIME = "elapsed_time";
        public static final String COLUMN_NAME_CURRENT_BATTERY_LEVEL = "current_battery_level";
        public static final String COLUMN_NAME_CONSUMED_BATTERY = "consumed_battery";
        public static final String COLUMN_NAME_AVG_TIME_PER_BATTERY = "avg_time_battery";
        public static final String COLUMN_NAME_IS_EXPERIMENT_RUNNING = "experiment_running";
    }
}
