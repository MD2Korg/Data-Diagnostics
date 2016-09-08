package org.md2k.datadiagnostic.power;

import java.util.ArrayList;
import java.util.List;

import org.md2k.datadiagnostic.configurations.DDT_PARAMETERS;
import org.md2k.datadiagnostic.signalquality.algorithms.RIPQualityCalculation;
import org.md2k.datadiagnostic.struct.DataPointQuality;
import org.md2k.datadiagnostic.struct.DataPoints;

public class BatteryLevel {

	public final List<DataPoints> phoneBatteryDown;
	public final List<DataPoints> sensorBatteryDown;
	public final List<DataPoints> phonePoweredOff;
	public final List<DataPoints> sensorPoweredOff;

	public BatteryLevel() {
		phoneBatteryDown = new ArrayList<DataPoints>();
		sensorBatteryDown = new ArrayList<DataPoints>();
		phonePoweredOff = new ArrayList<DataPoints>();
		sensorPoweredOff = new ArrayList<DataPoints>();
	}

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
	public void phoneBatteryDown(List<DataPoints> rawBatteryLevels, long startDayTime, long endDayTime) {
		//long phoneOnTime = 0, phoneOffTime = 0;
		
		// iterate over battery levels to see if there is 0 battery level
		for (int i = 0; i < rawBatteryLevels.size() - 1; i++) {
			if (rawBatteryLevels.get(i).getTimestamp() >= startDayTime
					&& rawBatteryLevels.get(i).getTimestamp() <= endDayTime) {

				// check if two stamps have more than 10 (threshold) seconds
				// difference
				if (((rawBatteryLevels.get(i).getTimestamp() - rawBatteryLevels.get(i + 1).getTimestamp()) > DDT_PARAMETERS.PHONE_SENSOR_DIFF)
						&& rawBatteryLevels.get(i).getValue() <= DDT_PARAMETERS.PHONE_BATTERY_DOWN) {
					phoneBatteryDown.add(new DataPoints(rawBatteryLevels.get(i).getTimestamp(),rawBatteryLevels.get(i+1).getTimestamp()));
				}

			}
		}
				
	}

	/**
	 * 
	 * It will check at what time of a day the battery level was more than 0%
	 * But there was no data
	 * 
	 * @param rawBatteryLevels
	 * @param dayStartTime
	 *            start time of a day
	 * @param dayEndTime
	 *            end time of a day
	 */
	public void phonePoweredOff(List<DataPoints> rawBatteryLevels, long startDayTime, long endDayTime) {
		//long phonePoweredOffTime = 0;

		// iterate over battery levels to see if there is 0 battery level
		for (int i = 0; i < rawBatteryLevels.size() - 1; i++) {
			if (rawBatteryLevels.get(i).getTimestamp() >= startDayTime
					&& rawBatteryLevels.get(i).getTimestamp() <= endDayTime) {
				// check if two stamps has more than 60 (threshold) seconds
				// difference
				if (((rawBatteryLevels.get(i).getTimestamp() - rawBatteryLevels.get(i + 1).getTimestamp()) > DDT_PARAMETERS.PHONE_SENSOR_DIFF)
						&& rawBatteryLevels.get(i).getValue() > DDT_PARAMETERS.PHONE_POWERED_OFF) {
					/*phonePoweredOffTime += (rawBatteryLevels.get(i).getTimestamp()
							- rawBatteryLevels.get(i + 1).getTimestamp());*/
					phonePoweredOff.add(new DataPoints(rawBatteryLevels.get(i).getTimestamp(),rawBatteryLevels.get(i+1).getTimestamp()));
					System.out.println("====="+phoneBatteryDown.size());
				}
			}
		}
		
		//System.out.println("Phone Powered Off (MilliSeconds): " + phonePoweredOffTime);

	}
	
	/**
	 * 
	 * It will check at what time of a day the battery level was zero to
	 * calculate sensor-off episodes.
	 * 
	 * @param rawBatteryLevels
	 * @param dayStartTime
	 *            start time of a day
	 * @param dayEndTime
	 *            end time of a day
	 */
	public void sensorBatteryDown(List<DataPoints> rawBatteryLevels, long startDayTime, long endDayTime) {
		double voltageValue;
		//long sensorPoweredOffTime = 0;
		for (int i = 0; i < rawBatteryLevels.size()-1; i++) {
			if (rawBatteryLevels.get(i).getTimestamp() >= startDayTime
					&& rawBatteryLevels.get(i).getTimestamp() <= endDayTime) {
			// ADC value to voltage
			voltageValue = (rawBatteryLevels.get(i).getValue() / 4096) * 3 * 2;
				if (((rawBatteryLevels.get(i).getTimestamp() - rawBatteryLevels.get(i + 1).getTimestamp()) > DDT_PARAMETERS.AUTOSENSE_SENSOR_DIFF)
						&& voltageValue <= DDT_PARAMETERS.AUTOSENSE_BATTERY_DOWN) {
				/*sensorPoweredOffTime += (rawBatteryLevels.get(i).getTimestamp()
						- rawBatteryLevels.get(i + 1).getTimestamp());*/
					sensorBatteryDown.add(new DataPoints(rawBatteryLevels.get(i).getTimestamp(),rawBatteryLevels.get(i+1).getTimestamp()));
			}
			}
		}
		//System.out.println("Sensor Battery Down: " + sensorPoweredOffTime);
	}
	
	/**
	 * 
	 * It will check at what time of a day the battery level was more than 0%
	 * But there was no data
	 * 
	 * @param rawBatteryLevels
	 * @param dayStartTime
	 *            start time of a day
	 * @param dayEndTime
	 *            end time of a day
	 */
	public void sensorPoweredOff(List<DataPoints> rawBatteryLevels, long startDayTime, long endDayTime) {
		double voltageValue;
		//long sensorOnTime = 0, sensorOffTime = 0;
		for (int i = 0; i < rawBatteryLevels.size()-1; i++) {
			if (rawBatteryLevels.get(i).getTimestamp() >= startDayTime
					&& rawBatteryLevels.get(i).getTimestamp() <= endDayTime) {
			// ADC value to voltage
			voltageValue = (rawBatteryLevels.get(i).getValue() / 4096) * 3 * 2;
				if (((rawBatteryLevels.get(i).getTimestamp() - rawBatteryLevels.get(i + 1).getTimestamp()) > DDT_PARAMETERS.AUTOSENSE_SENSOR_DIFF)
						&& voltageValue >= DDT_PARAMETERS.AUTOSENSE_POWERED_OFF) {
					/*sensorOffTime += (rawBatteryLevels.get(i).getTimestamp()
						- rawBatteryLevels.get(i + 1).getTimestamp());*/
					sensorPoweredOff.add(new DataPoints(rawBatteryLevels.get(i).getTimestamp(),rawBatteryLevels.get(i+1).getTimestamp()));
			}
			}
		}
		//sensorOnTime = (endDayTime - startDayTime) - sensorOffTime;

		//System.out.println("Sensor ON : " + sensorOnTime + " - Sensor OFF : " + sensorOffTime);
	}
	
	

	
}
