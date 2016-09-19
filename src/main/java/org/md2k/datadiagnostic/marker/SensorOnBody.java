package org.md2k.datadiagnostic.marker;

import java.util.ArrayList;
import java.util.List;

import org.md2k.datadiagnostic.configurations.DDT_PARAMETERS;
import org.md2k.datadiagnostic.signalquality.algorithms.VarianceBasedDataQuality;
import org.md2k.datadiagnostic.struct.DataPointQuality;
import org.md2k.datadiagnostic.struct.DataPoints;

public class SensorOnBody {

	List<DataPointQuality> windowedData;
	public SensorOnBody() {
		windowedData = new ArrayList<DataPointQuality>();
	}
	
	public void sensorOnOffBodyMarker() {
		VarianceBasedDataQuality varianceBasedDataQuality = new VarianceBasedDataQuality();
		
		for(int i=0;i<windowedData.size();i++){
			windowedData.get(i).getDataPoints();
		}
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/*public void getSensorOnEpisodes(List<DataPoints> rawData, List<DataPoints> rawBatteryLevels) {
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
	}*/
}
