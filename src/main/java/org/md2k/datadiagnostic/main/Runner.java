package org.md2k.datadiagnostic.main;

import java.util.ArrayList;
import java.util.List;

import org.md2k.datadiagnostic.configurations.DDT_PARAMETERS;
import org.md2k.datadiagnostic.data.CSVExporter;
import org.md2k.datadiagnostic.data.DataLoader;
import org.md2k.datadiagnostic.marker.SensorOffBodyMarker;
import org.md2k.datadiagnostic.marker.SensorSignalQualityMarker;
import org.md2k.datadiagnostic.marker.SensorUnavailable;
import org.md2k.datadiagnostic.marker.power.BatteryDataMarker;
import org.md2k.datadiagnostic.marker.wireless.DataLossMarker;
import org.md2k.datadiagnostic.marker.wireless.SensorUnavailableMarker;
import org.md2k.datadiagnostic.struct.DataPoints;
import org.md2k.datadiagnostic.util.Util;
import org.md2k.datadiagnostic.windowing.FixedSizeWindowing;

public class Runner {

	/**
	 * This class is responsible to execute all the data-diagnostic algorithms
	 */

	DataLoader dataLoader;
	List<DataPoints> sensorData;
	List<DataPoints> phoneBatteryData;
	List<DataPoints> sensorBatteryData;
	String outputPath;

	FixedSizeWindowing staticWindows;
	double samplingRate;

	public Runner(String streamName, String inputPath, String outputPath) {
		sensorData = new ArrayList<DataPoints>();
		phoneBatteryData = new ArrayList<DataPoints>();
		sensorBatteryData = new ArrayList<DataPoints>();
		staticWindows = new FixedSizeWindowing();
		dataLoader = new DataLoader();
		this.outputPath = outputPath;

		//in future, get this information from a database
		long startTime = Util.getStartDayTime(1474313586405l);
		long endTime = Util.getEndDayTime(1474313586405l);

		if (streamName.equals("respiration")) {
			phoneBatteryData = dataLoader.loadCSV(inputPath + "phone_battery.csv");
			sensorBatteryData = dataLoader.loadCSV(inputPath + "chest_sensor_battery.csv");

			samplingRate = DDT_PARAMETERS.RESPIRATION_SAMPLING_RATE;
			sensorData = dataLoader.loadCSV(inputPath + "resp.csv");
			staticWindows.blankWindows(sensorData, startTime, endTime, DDT_PARAMETERS.WINDOW_SIZE);
			chestbandMarkers();
		} else if (streamName.equals("ecg")) {
			phoneBatteryData = dataLoader.loadCSV(inputPath + "phone_battery.csv");
			sensorBatteryData = dataLoader.loadCSV(inputPath + "chest_sensor_battery.csv");
			
			samplingRate = DDT_PARAMETERS.AUTOSENSE_SAMPLING_RATE;
			sensorData = dataLoader.loadCSV(inputPath + "ecg.csv");
			staticWindows.blankWindows(sensorData, startTime, endTime, DDT_PARAMETERS.WINDOW_SIZE);
			chestbandMarkers();
		} else if (streamName.equals("microsoft_band")) {
			phoneBatteryData = dataLoader.loadCSV(inputPath + "phone_battery.csv");
			
			samplingRate = DDT_PARAMETERS.AUTOSENSE_SAMPLING_RATE;
			sensorData = dataLoader.loadCSV(inputPath + "acc_microsoft.csv");
			staticWindows.blankWindows(sensorData, startTime, endTime, DDT_PARAMETERS.WINDOW_SIZE);
			wristBandMarkers();
		}
	}

	/**
	 * This method is responsible to execute data diagnostic markers for chest band
	 */
	private void chestbandMarkers() {
		CSVExporter csvExporter = new CSVExporter();
		BatteryDataMarker batteryDataMarker = new BatteryDataMarker();
		SensorUnavailableMarker sensorUnavailable = new SensorUnavailableMarker();
		SensorUnavailable sensorUnavailable2 = new SensorUnavailable();
		SensorOffBodyMarker bodyMarker = new SensorOffBodyMarker();
		DataLossMarker dataLossMarker = new DataLossMarker();
		SensorSignalQualityMarker sensorSignalQualityMarker = new SensorSignalQualityMarker();
		
		batteryDataMarker.phoneBatteryMarker(phoneBatteryData, staticWindows.blankWindows);
		
		batteryDataMarker.sensorBatteryMarker(sensorBatteryData, batteryDataMarker.phoneBattery);
		
		sensorUnavailable2.wirelessDC(batteryDataMarker.sensorBattery);
		
		bodyMarker.improperOrNoAttachment(sensorUnavailable2.sensorUnavailable);
		//sensorUnavailable.wirelessDisconnectionsMarker(sensorData, batteryDataMarker.sensorBattery);
		
		dataLossMarker.packetLossMarker(bodyMarker.improperOrNoAttachment, DDT_PARAMETERS.WINDOW_SIZE, samplingRate);
		
		sensorSignalQualityMarker.markWindowsQulaity(dataLossMarker.dataLoss, DDT_PARAMETERS.WINDOW_SIZE, samplingRate);
		
		csvExporter.writeToCSV(sensorSignalQualityMarker.markedWindows, this.outputPath, "new.txt");

	}
	
	/**
	 * This method is responsible to execute data diagnostic markers for wrist band
	 */
	private void wristBandMarkers() {
		CSVExporter csvExporter = new CSVExporter();
		BatteryDataMarker batteryDataMarker = new BatteryDataMarker();
		SensorUnavailableMarker sensorUnavailable = new SensorUnavailableMarker();
		DataLossMarker dataLossMarker = new DataLossMarker();
		SensorSignalQualityMarker sensorSignalQualityMarker = new SensorSignalQualityMarker();
		
		batteryDataMarker.phoneBatteryMarker(phoneBatteryData, staticWindows.blankWindows);
		
		batteryDataMarker.sensorBatteryMarker(sensorBatteryData, batteryDataMarker.phoneBattery);
		
		//sensorUnavailable.wirelessDisconnectionsMarker(sensorData, batteryDataMarker.sensorBattery);
		
		dataLossMarker.packetLossMarker(batteryDataMarker.sensorBattery, DDT_PARAMETERS.WINDOW_SIZE, samplingRate);
		
		sensorSignalQualityMarker.markWindowsQulaity(dataLossMarker.dataLoss, DDT_PARAMETERS.WINDOW_SIZE, samplingRate);
		
		csvExporter.writeToCSV(sensorSignalQualityMarker.markedWindows, this.outputPath, "new.txt");

	}
}
