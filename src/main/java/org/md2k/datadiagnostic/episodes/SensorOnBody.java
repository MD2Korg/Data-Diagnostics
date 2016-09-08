package org.md2k.datadiagnostic.episodes;

import java.util.ArrayList;
import java.util.List;

import org.md2k.datadiagnostic.configurations.DDT_PARAMETERS;
import org.md2k.datadiagnostic.struct.DataPoints;

public class SensorOnBody {

	public final List<DataPoints> phoneBattery;
	public final List<DataPoints> autosenseBattery;
	public final List<DataPoints> phoneOnEpisodes;
	public final List<DataPoints> sensorOnEpisodes;

	public SensorOnBody() {
		phoneBattery = new ArrayList<DataPoints>();
		autosenseBattery = new ArrayList<DataPoints>();
		phoneOnEpisodes = new ArrayList<DataPoints>();
		sensorOnEpisodes = new ArrayList<DataPoints>();
	}
	
	public void getSensorOnEpisodes2(List<DataPoints> rawData, List<DataPoints> rawBatteryLevels) {
		long sensorOnTime = 0, sensorOffTime = 0, startTime, endTime;

		for (int i = 0; i < rawBatteryLevels.size() - 1; i++) {
			if (rawData.get(i).getTimestamp() == rawBatteryLevels.get(i).getTimestamp()) {
				startTime = rawBatteryLevels.get(i).getTimestamp();
				endTime = rawBatteryLevels.get(i + 1).getTimestamp();
				sensorOnTime += endTime - startTime;

			} else {
				startTime = rawBatteryLevels.get(i).getTimestamp();
				endTime = rawBatteryLevels.get(i + 1).getTimestamp();
				sensorOffTime += endTime - startTime;
			}
		}
		System.out.println("Sensor ON: " + sensorOnTime + " - Sensor OFF " + sensorOffTime);
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public void getSensorOnEpisodes(List<DataPoints> rawData, List<DataPoints> rawBatteryLevels) {
		long sensorOnTime = 0, sensorOffTime = 0, startTime, endTime;

		for (int i = 0; i < rawBatteryLevels.size() - 1; i++) {

			if (rawData.get(i).getTimestamp() == rawBatteryLevels.get(i).getTimestamp()) {
				startTime = rawBatteryLevels.get(i).getTimestamp();
				endTime = rawBatteryLevels.get(i + 1).getTimestamp();
				sensorOnTime += endTime - startTime;

			} else {
				startTime = rawBatteryLevels.get(i).getTimestamp();
				endTime = rawBatteryLevels.get(i + 1).getTimestamp();
				sensorOffTime += endTime - startTime;
			}
		}
		System.out.println("Sensor ON: " + sensorOnTime + " - Sensor OFF " + sensorOffTime);
	}

	public void getPhoneBatteryLevel(List<DataPoints> rawBatteryLevels) {
		for (int i = 0; i < rawBatteryLevels.size(); i++) {
			if (rawBatteryLevels.get(i).getValue() >= DDT_PARAMETERS.PHONE_BATTERY_DOWN) {
				this.phoneBattery.add(
						new DataPoints(rawBatteryLevels.get(i).getTimestamp(), rawBatteryLevels.get(i).getValue()));
			}
		}
	}

	public void getAutoSenseBatteryLevel(List<DataPoints> rawData) {
		double voltageValue;
		for (int i = 0; i < rawData.size(); i++) {
			// ADC value to voltage
			voltageValue = (rawData.get(i).getValue() / 4096) * 3 * 2;
			if (voltageValue >= DDT_PARAMETERS.AUTOSENSE_BATTERY_DOWN) {
				this.autosenseBattery.add(new DataPoints(rawData.get(i).getTimestamp(), rawData.get(i).getValue()));
			}
		}
	}
}
