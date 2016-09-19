package org.md2k.datadiagnostic.marker.power;

import java.util.List;

import org.md2k.datadiagnostic.configurations.DDT_PARAMETERS;
import org.md2k.datadiagnostic.configurations.METADATA;
import org.md2k.datadiagnostic.struct.DataPointQuality;
import org.md2k.datadiagnostic.struct.DataPoints;

public class BatteryDataMarker {


	/**
	 * 
	 * It will check at what time of a day the battery level was less than to
	 * calculate phone-off episodes.
	 * 
	 * @param rawBatteryLevels
	 * @param dayStartTime
	 *            start time of a day
	 * @param dayEndTime
	 *            end time of a day
	 */
	public void phoneBatteryMarker(List<DataPoints> rawBatteryLevels, List<DataPointQuality> blankWindows) {
		// long phoneOnTime = 0, phoneOffTime = 0;
		long startTime, endTime;
		// iterate over battery levels to see if there is 0 battery level
		for (int i = 0; i < blankWindows.size() - 1; i++) {

			startTime = blankWindows.get(i).getDataPoints().get(0).getTimestamp();
			endTime = blankWindows.get(i).getDataPoints().get(0).getEndTimestamp();

			long tmp = (endTime - startTime) / 1000;
			Math.round(tmp);

			int temp = 0;
			for (int j = 0; j < rawBatteryLevels.size(); j++) {
				if (rawBatteryLevels.get(j).getTimestamp() >= startTime
						&& rawBatteryLevels.get(j).getTimestamp() <= endTime) {
					continue;
				} else {
					// this temp will check that at least 33% of the time
					// battery data wasn't available
					temp++;
					if (temp > 20) {
						// mark blankWindows as phone battery down
						if (rawBatteryLevels.get(j).getValue() <= DDT_PARAMETERS.PHONE_BATTERY_DOWN) {
							blankWindows.get(i).setQuality(METADATA.PHONE_BATTERY_DOWN);
							break;
						} else {
							blankWindows.get(i).setQuality(METADATA.PHONE_POWERED_OFF);
							break;
						}
					}

				}
			}
		}

	}

	public void sensorBatteryMarker(List<DataPoints> rawBatteryLevels, List<DataPointQuality> blankWindows) {
		long startTime, endTime;
		double voltageValue;
		// iterate over battery levels to see if there is 0 battery level
		for (int i = 0; i < blankWindows.size() - 1; i++) {

			startTime = blankWindows.get(i).getDataPoints().get(0).getTimestamp();
			endTime = blankWindows.get(i).getDataPoints().get(0).getEndTimestamp();

			long tmp = (endTime - startTime) / 1000;
			Math.round(tmp);
			int temp = 0;
			for (int j = 0; j < rawBatteryLevels.size(); j++) {
				// ADC value to voltage
				voltageValue = (rawBatteryLevels.get(i).getValue() / 4096) * 3 * 2;
				// this temp will check that at least 33% of the time battery
				// data wasn't available in a window
				temp++;
				if (temp > 20) {
					if (rawBatteryLevels.get(j).getTimestamp() >= startTime
							&& rawBatteryLevels.get(j).getTimestamp() < endTime) {
						continue;
					} else {
						// only mark sensor battery down/off if phone was on
						if (blankWindows.get(i).getQuality() != METADATA.PHONE_BATTERY_DOWN
								|| blankWindows.get(i).getQuality() != METADATA.PHONE_POWERED_OFF) {
							// mark blankWindows as sensor battery down/off
							if (voltageValue <= DDT_PARAMETERS.AUTOSENSE_BATTERY_DOWN) {
								blankWindows.get(i).setQuality(METADATA.SENSOR_BATTERY_DOWN);
								break;
							} else {
								blankWindows.get(i).setQuality(METADATA.SENSOR_POWERED_OFF);
								break;
							}
						}
					}
				}
			}
		}

	}

}
