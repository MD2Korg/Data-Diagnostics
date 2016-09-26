package org.md2k.datadiagnostic.marker.wireless;

import java.util.ArrayList;
import java.util.List;

import org.md2k.datadiagnostic.configurations.DDT_PARAMETERS;
import org.md2k.datadiagnostic.configurations.METADATA;
import org.md2k.datadiagnostic.struct.DataPointQuality;
import org.md2k.datadiagnostic.struct.DataPoints;

public class SensorUnavailableMarker {

	public final List<DataPointQuality> wirelessDisconnections;

	public SensorUnavailableMarker() {
		wirelessDisconnections = new ArrayList<DataPointQuality>();
	}

	/**
	 * 
	 * @param rawData {@link DataPoints}
	 * @param blankWindows {@link DataPointQuality}
	 */
	public void wirelessDisconnectionsMarker(List<DataPoints> rawData, List<DataPointQuality> blankWindows) {
		long timeDiff, startTime;

		for (int i = 0; i < rawData.size() - 1; i++) {
			timeDiff = rawData.get(i + 1).getTimestamp() - rawData.get(i).getTimestamp();
			if (timeDiff >= DDT_PARAMETERS.WIRELESS_DISCONNECTION) {
				for (int j = 0; j < blankWindows.size(); j++) {
					startTime = blankWindows.get(j).getDataPoints().get(0).getTimestamp();
					if (blankWindows.get(j).getQuality() == 999) {
						if (rawData.get(i).getTimestamp() <= startTime
								&& rawData.get(i + 1).getTimestamp() > startTime) {
							blankWindows.get(j).setQuality(METADATA.SENSOR_UNAVAILABLE);
						}
					}
				}
			}
		}
		wirelessDisconnections.addAll(blankWindows);
	}

}
