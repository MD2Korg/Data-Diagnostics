package org.md2k.datadiagnostic.configurations;

public class DDT_PARAMETERS {

	public static final String STREAM_NAME="microsoft_band";
	// 0.00003 for microsoft band and for autosense 1000
	public static final double VARIANCE_THRESHOLD=0.00003;
	
	// Initial window size in milliseconds
	public static final int WINDOW_SIZE = 60000;

	// This is the variance value set to distinguish between wireless disconnect and battery off/down
	public static final int WIRELESS_DISCONNECTION = 5000;
	
	// Value is in percentage of battery remaining. For example, 1 means 1%
	// battery remains. Min=0 and Max=100
	// It is used to calculate whether phone was off due to battery down or was
	// powered off
	public static final int PHONE_BATTERY_DOWN = 0;
	public static final int PHONE_POWERED_OFF = 10;
	// time difference between two timestampls of battery data
	public static final int PHONE_SENSOR_DIFF = 10000;

	// Value in battery voltage. Min=0 and Max=6
	// It is used to calculate whether sensor was off due to battery down or was
	// powered off
	public static final int MOTIONSENSE_BATTERY_DOWN = 1;
	public static final int AUTOSENSE_BATTERY_DOWN = 1;
	public static final int AUTOSENSE_POWERED_OFF = 4;
	// time difference between two timestampls of battery data
	public static final int AUTOSENSE_SENSOR_DIFF = 10000;

	// Sampling rate. This is used to calculate packet loss
	public static final double RESPIRATION_SAMPLING_RATE = 21.33;
	public static final double ECG_SAMPLING_RATE = 64;
	public static final double MOTIONSENSE_SAMPLING_RATE = 6;
	public static final double MICROSOFT_BAND_SAMPLING_RATE = 6;

	// Percentage of missing samples. This is used to calculate packet loss.
	// Min=0 and Max=100
	public static final double RESPIRATION_MISSING_SAMPLE = 90;
	public static final double ECG_MISSING_SAMPLE = 6;
	public static final double MICROSOFT_BAND__MISSING_SAMPLE = 6;
	public static final double AUTOSENSE__MISSING_SAMPLE = 6;

	// Threshold to merge windows. If there is less gap between two windows than
	// the threshold then both will be merged in one larger window. Value is in milliseconds
	public static final long MERGE_WINDOWS_THRESHOLD_TIME = 60000;
	
	//min 0 and max 1
	public static final double MINIMUM_ACCEPTABLE_PACKET_LOSS = 0.33;
	
	//CSV OUTPUT
	//turning this option on will generate output in CSV files. 1 = yes and 0=no
	public static final int GENERATE_CSV_FILES = 1;
	
	//CSV File Names
	public static final String ACTIVE_WINDOWS_CSV = "active_windows.csv";
	public static final String ACTIVE_MERGED_WINDOWS_CSV = "active_merged_windows.csv";

}
