package org.md2k.datadiagnostic.marker.power;

import java.util.ArrayList;
import java.util.List;

import org.md2k.datadiagnostic.configurations.DDT_PARAMETERS;
import org.md2k.datadiagnostic.configurations.METADATA;
import org.md2k.datadiagnostic.struct.DataPointQuality;
import org.md2k.datadiagnostic.struct.DataPoints;

public class BatteryDataMarker {

	public final List<DataPointQuality> markedWindows;

	public BatteryDataMarker() {
		markedWindows = new ArrayList<DataPointQuality>();
	}

	/**
	 * This method checks at what time of a day phone was manually powered off
	 * or turned off due to low battery.
	 * 
	 * @param rawBatteryLevels
	 *            {@link DataPoints}
	 * @param blankWindows
	 *            {@link DataPointQuality}
	 */
	public void phoneBatteryMarker(List<DataPoints> rawBatteryLevels, List<DataPointQuality> blankWindows) {
		long startTime, endTime;
		// iterate over battery levels to see if there is 0 battery level
		for (int i = 0; i < blankWindows.size(); i++) {

			startTime = blankWindows.get(i).getDataPoints().get(0).getTimestamp();
			endTime = blankWindows.get(i).getDataPoints().get(blankWindows.get(i).getDataPoints().size() - 1)
					.getTimestamp();

			int temp = 0, temp2 = 0;
			for (int j = 0; j < rawBatteryLevels.size(); j++) {
				if (rawBatteryLevels.get(j).getTimestamp() >= startTime) {
					if (rawBatteryLevels.get(j).getTimestamp() >= startTime
							&& rawBatteryLevels.get(j).getTimestamp() <= endTime) {
						temp2 = 1;
						break;
					} else {
						temp++;
						// at least 33% of the time battery data wasn't
						// available
						if (temp > 20) {
							// mark blankWindows as phone battery down
							if (rawBatteryLevels.get(j).getValue() <= DDT_PARAMETERS.PHONE_BATTERY_DOWN) {
								blankWindows.get(i).setQuality(METADATA.PHONE_BATTERY_DOWN);
								temp2 = 1;
								break;
							} else {
								blankWindows.get(i).setQuality(METADATA.PHONE_POWERED_OFF);
								temp2 = 1;
								break;
							}
						}
					}
				}
			}
			if (temp2 == 0) {
				blankWindows.get(i).setQuality(METADATA.PHONE_POWERED_OFF);
			}
		}
		this.markedWindows.addAll(blankWindows);
	}

	/**
	 * This method checks at what time of a day sensor was manually powered off
	 * or turned off due to low battery.
	 * 
	 * @param rawBatteryLevels
	 *            {@link DataPoints}
	 * @param blankWindows
	 *            {@link DataPointQuality}
	 */
	public void senseBatteryMarker(List<DataPoints> rawBatteryLevels, List<DataPointQuality> blankWindows) {
		long startTime, endTime;
		double voltageValue;
		// iterate over battery levels to see if there is 0 battery level
		for (int i = 0; i < blankWindows.size(); i++) {

			startTime = blankWindows.get(i).getDataPoints().get(0).getTimestamp();
			endTime = blankWindows.get(i).getDataPoints().get(blankWindows.get(i).getDataPoints().size() - 1)
					.getTimestamp();

			for (int j = 0; j < rawBatteryLevels.size(); j++) {
				if (rawBatteryLevels.get(j).getTimestamp() >= startTime) {
					// ADC value to voltage
					voltageValue = (rawBatteryLevels.get(i).getValue() / 4096) * 3 * 2;
					if (blankWindows.get(i).getQuality() == 999) {
						if (rawBatteryLevels.get(j).getTimestamp() >= startTime
								&& rawBatteryLevels.get(j).getTimestamp() <= endTime) {
							break;
						} else {
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
		this.markedWindows.addAll(blankWindows);
	}

	/**
	 * This method checks at what time of a day sensor was manually powered off
	 * or turned off due to low battery.
	 * 
	 * @param rawBatteryLevels
	 *            {@link DataPoints}
	 * @param blankWindows
	 *            {@link DataPointQuality}
	 */
	public void motionSenseBatteryMarker(List<DataPoints> rawBatteryLevels, List<DataPointQuality> blankWindows) {
		long startTime, endTime;
		// iterate over battery levels to see if there is 0 battery level
		for (int i = 0; i < blankWindows.size(); i++) {

			startTime = blankWindows.get(i).getDataPoints().get(0).getTimestamp();
			endTime = blankWindows.get(i).getDataPoints().get(blankWindows.get(i).getDataPoints().size() - 1)
					.getTimestamp();

			for (int j = 0; j < rawBatteryLevels.size(); j++) {
				if (rawBatteryLevels.get(j).getTimestamp() >= startTime) {
					if (blankWindows.get(i).getQuality() == 999) {
						if (rawBatteryLevels.get(j).getTimestamp() >= startTime
								&& rawBatteryLevels.get(j).getTimestamp() <= endTime) {
							break;
						} else {
							// mark blankWindows as sensor battery down/off
							if (rawBatteryLevels.get(i).getValue() <= DDT_PARAMETERS.MOTIONSENSE_BATTERY_DOWN) {
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
		this.markedWindows.addAll(blankWindows);
	}

}
