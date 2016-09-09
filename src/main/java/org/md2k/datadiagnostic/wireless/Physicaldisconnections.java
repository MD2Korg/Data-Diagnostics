package org.md2k.datadiagnostic.wireless;

import java.util.ArrayList;
import java.util.List;

import org.md2k.datadiagnostic.configurations.DATA_QUALITY;
import org.md2k.datadiagnostic.configurations.DDT_PARAMETERS;
import org.md2k.datadiagnostic.signalquality.algorithms.RIPQualityCalculation;
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
	public void getWirelessDisconnections(List<DataPoints> rawData) {
		List<DataPoints> tempArray = new ArrayList<DataPoints>();
		long timeDiff;
		for (int i = 0; i < rawData.size() - 1; i++) {

			

				timeDiff = rawData.get(i + 1).getTimestamp() - rawData.get(i).getTimestamp();
				if (timeDiff >= DDT_PARAMETERS.WIRELESS_DISCONNECTION) {
					tempArray.add(new DataPoints(rawData.get(i).getTimestamp(), rawData.get(i).getValue()));
					tempArray.add(new DataPoints(rawData.get(i + 1).getTimestamp(), rawData.get(i + 1).getValue()));
					this.wirelessDisconnections.add(new DataPointQuality(tempArray, DATA_QUALITY.SENSOR_UNAVAILABLE));
					
					tempArray.clear();
				
			}
		}
		
	}

}
