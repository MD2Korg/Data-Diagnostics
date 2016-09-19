package org.md2k.datadiagnostic.marker.wireless;

import java.util.ArrayList;
import java.util.List;

import org.md2k.datadiagnostic.configurations.DATA_QUALITY;
import org.md2k.datadiagnostic.configurations.DDT_PARAMETERS;
import org.md2k.datadiagnostic.configurations.METADATA;
import org.md2k.datadiagnostic.struct.DataPointQuality;
import org.md2k.datadiagnostic.struct.DataPoints;
import org.md2k.datadiagnostic.util.Util;

public class Physicaldisconnections {

	public final List<DataPointQuality> wirelessDisconnections;

	public Physicaldisconnections() {
		wirelessDisconnections = new ArrayList<DataPointQuality>();
	}

	/**
	 * Iterate the timestamp and check if there is more than 30 seconds gap.
	 * More than 30 second gap is considered as wireless disconnection(s)
	 * 
	 * @param rawData
	 *            all timestamps of a sensor
	 * @param startDayTime
	 *            start time of a day in milliseconds
	 * @param endDayTime
	 *            end time of a day in milliseconds
	 */
	public void getWirelessDisconnections(List<DataPoints> rawData, List<DataPointQuality> blankWindows) {
		long timeDiff,startTime;
		
		/*for (int i = 0; i < blankWindows.size() - 1; i++) {
			if (blankWindows.get(i).getQuality() == METADATA.SENSOR_ON_BODY) {
				startTime = blankWindows.get(i).getDataPoints().get(0).getTimestamp();
				endTime = blankWindows.get(i).getDataPoints().get(0).getEndTimestamp();
				
				//tmp = blankWindows.get(i).getDataPoints().size();
				//timeDiff = blankWindows.get(i).getDataPoints().get(0).getTimestamp();
				for (int j = 0; j < rawData.size() - 1; j++) {
					if(rawData.get(j).getTimestamp()>=startTime && rawData.get(j).getTimestamp()<=endTime){
						
					}
				}
			}
		}*/
		
		for (int i = 0; i < rawData.size() - 1; i++) {
			timeDiff = rawData.get(i + 1).getTimestamp() - rawData.get(i).getTimestamp();
			if (timeDiff >= DDT_PARAMETERS.WIRELESS_DISCONNECTION) {
				for (int j = 0; j < blankWindows.size() - 1; j++) {
					startTime = blankWindows.get(i).getDataPoints().get(0).getTimestamp();
					
					if(rawData.get(i).getTimestamp()<=startTime && rawData.get(i+1).getTimestamp()>startTime){
						blankWindows.get(j).setQuality(METADATA.SENSOR_UNAVAILABLE);
					}
				}
			}
		}
		

	}
	
	
	
	
	
	
	
	public void packetLoss(List<DataPoints> rawData, List<DataPointQuality> blankWindows) {
		long timeDiff,startTime;
		for (int i = 0; i < rawData.size() - 1; i++) {
			timeDiff = rawData.get(i + 1).getTimestamp() - rawData.get(i).getTimestamp();
			if (timeDiff >= DDT_PARAMETERS.WIRELESS_DISCONNECTION) {
				for (int j = 0; j < blankWindows.size() - 1; j++) {
					startTime = blankWindows.get(i).getDataPoints().get(0).getTimestamp();
					
					if(rawData.get(i).getTimestamp()<=startTime && rawData.get(i+1).getTimestamp()>startTime){
						blankWindows.get(j).setQuality(METADATA.SENSOR_UNAVAILABLE);
					}
				}
			}
		}
		

	}

}
