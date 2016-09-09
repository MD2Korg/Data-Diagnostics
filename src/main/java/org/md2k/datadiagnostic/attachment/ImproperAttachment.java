package org.md2k.datadiagnostic.attachment;

import java.util.ArrayList;
import java.util.List;

import org.md2k.datadiagnostic.configurations.DATA_QUALITY;
import org.md2k.datadiagnostic.struct.DataPointQuality;
import org.md2k.datadiagnostic.struct.DataPoints;

public class ImproperAttachment {

	public final List<DataPoints> improperAttachment;

	/**
	 * Constructor
	 */
	public ImproperAttachment() {
		improperAttachment = new ArrayList<DataPoints>();
	}
	
	/**
	 * Calculates time for improper attachment of a sensor.
	 * 
	 * @param windows timestamp windows marked with quality labels
	 * @param startDayTime start time of a day
	 * @param endDayTime end time of a day
	 */
	public void getImproperAttachmentPeriods(List<DataPointQuality> windows) {
		//ArrayList<DataPoints> goodQualityWindows = new ArrayList<DataPoints>();	
		long totalTime=0;
		int size=0;
		for (int i = 0; i < windows.size(); i++) {
			size = windows.get(i).getDataPoints().size();
			
				if(windows.get(i).getQuality()== DATA_QUALITY.IMPROPER_ATTACHMENT){
					size = windows.get(i).getDataPoints().size();
					totalTime += windows.get(i).getDataPoints().get(size-1).getTimestamp()-windows.get(i).getDataPoints().get(0).getTimestamp();
					improperAttachment.add(new DataPoints(windows.get(i).getDataPoints().get(0).getTimestamp(),windows.get(i).getDataPoints().get(size-1).getTimestamp()));
				} 
			
		}
	}
	
	
	/*public void getNoisePeriods(List<DataPointQuality> windows, long startDayTime,
			long endDayTime) {
		ArrayList<DataPoints> goodQualityWindows = new ArrayList<DataPoints>();	
		long totalTime=0;
		int size=0;
		for (int i = 0; i < windows.size(); i++) {
			size = windows.get(i).getDataPoints().size();
			if (windows.get(i).getDataPoints().get(0).getTimestamp() >= startDayTime
					&& windows.get(i).getDataPoints().get(size-1).getTimestamp() <= endDayTime) {
				if(windows.get(i).getQuality()== 1 && windows.get(i).getDataPoints().get(0).getTimestamp()>startDayTime){
					size = windows.get(i).getDataPoints().size();
					totalTime += windows.get(i).getDataPoints().get(size-1).getTimestamp()-windows.get(i).getDataPoints().get(0).getTimestamp();
					goodQualityWindows.add(new DataPoints(windows.get(i).getDataPoints().get(0).getTimestamp(),windows.get(i).getDataPoints().get(size-1).getTimestamp()));
					
					
				} 
			}
		}
		System.out.println("Total noise time: "+totalTime);
	}*/
}
