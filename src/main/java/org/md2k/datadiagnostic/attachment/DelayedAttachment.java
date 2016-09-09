package org.md2k.datadiagnostic.attachment;

import java.util.List;

import org.md2k.datadiagnostic.struct.DataPointQuality;

public class DelayedAttachment {

	public long totalDelayInAttachment;
	
	public DelayedAttachment(){
		totalDelayInAttachment=0;
	}
	
	/**
	 * Calculates if there is any time delay between wearing sensor and collecting first good quality signal
	 * 
	 * @param windows timestamp windows marked with quality labels
	 * @param startDayTime start time of a day in milliseconds
	 * @return return total time of delay in attachment
	 */
	//Checks if there is no good data in the start of wearing a sensor
	public void getDelayInAttachment(List<DataPointQuality> windows, long startDayTime){
		long firstGoodWindowTime=0;
		
		firstGoodWindowTime = windows.get(0).getDataPoints().get(0).getTimestamp();
		totalDelayInAttachment = firstGoodWindowTime - startDayTime;
	}
	
	//Merge respiration and ECG signal data to check if there is any delay
	//Logic is that both sensors should start collecting data at almost the same time
}
