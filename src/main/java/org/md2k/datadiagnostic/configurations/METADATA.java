package org.md2k.datadiagnostic.configurations;

public class METADATA {

	   public static final int ON_BODY = 0;
	    public static final int OFF_BODY = 2;
	    public static final int SENSOR_UNAVAILABLE = 4;
	    public static final int DATA_LOST=6;
	    public static final int SENSOR_OFF_BODY = 8;
	    public static final int SENSOR_ON_BODY = 9;
	    public static final int IMPROPER_ATTACHMENT = 10;
	    public static final int DELAY_IN_ATTACHMENT = 12;
	    
	    public static final int SENSOR_BATTERY_DOWN = 14;
	    public static final int PHONE_BATTERY_DOWN = 16;
	    public static final int SENSOR_POWERED_OFF = 18;
	    public static final int PHONE_POWERED_OFF = 20;
	   
	    public static final int ACCEPTABLE_DATA = 22;
	    
	    
	    public static final String METADATA_STR =" GOOD(0), SENSOR_OFF(1), SENSOR_UNAVAILABLE(2), DATA_LOST(3), SENSOR_OFF_BODY(4), IMPROPER_ATTACHMENT(5)";
}
