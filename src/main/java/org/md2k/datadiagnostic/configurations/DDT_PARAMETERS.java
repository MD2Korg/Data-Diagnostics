package org.md2k.datadiagnostic.configurations;

public class DDT_PARAMETERS {
	
	//stream name to process (values = rip, ecg, motionsense, microsoft_band)
	public static final String STREAM_NAME="motionsense";
	// 0.00003 for microsoft/motionsense band and for autosense 1000
	public static final double VARIANCE_THRESHOLD=0.00003;
	
	// Initial window size in milliseconds
	public static final int WINDOW_SIZE = 60000;

	// This is the variance value set to distinguish between wireless disconnect and battery off/down
	public static final int WIRELESS_DISCONNECTION = 5000;
	
	// Value is in percentage of battery remaining. Min=0 and Max=100
	// It is used to calculate whether phone was off due to battery down or was
	// powered off
	public static final int PHONE_BATTERY_DOWN = 0;
	public static final int PHONE_POWERED_OFF = 10;
	

	// Value in battery voltage. Min=0 and Max=6
	// It is used to calculate whether sensor was off due to battery down or was
	// powered off
	public static final int MOTIONSENSE_BATTERY_DOWN = 1;
	public static final int AUTOSENSE_BATTERY_DOWN = 1;
	public static final int AUTOSENSE_POWERED_OFF = 4;
	

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

	// Two windows will be merged if difference between them is less than MERGE_WINDOWS_THRESHOLD_TIME
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
