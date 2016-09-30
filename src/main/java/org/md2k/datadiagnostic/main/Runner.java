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
	String inputPath;
	String streamName;
	String outputPath;

	FixedSizeWindowing staticWindows;
	double samplingRate;

	public Runner(String inputPath, String outputPath) {
		sensorData = new ArrayList<DataPoints>();
		phoneBatteryData = new ArrayList<DataPoints>();
		sensorBatteryData = new ArrayList<DataPoints>();
		staticWindows = new FixedSizeWindowing();
		dataLoader = new DataLoader();
		this.streamName = DDT_PARAMETERS.STREAM_NAME;
		this.outputPath = outputPath;
		this.inputPath = inputPath;

		//in future, get this information from a database
		long startTime = Util.getStartDayTime(1475082489918l);
		long endTime = Util.getEndDayTime(1475082489918l);

		if (streamName.equals("rip")) {
			phoneBatteryData = dataLoader.loadCSV(inputPath + "PHONE_BATTERY.csv");
			sensorBatteryData = dataLoader.loadCSV(inputPath + "AUTOSENSE_BATTERY.csv");

			samplingRate = DDT_PARAMETERS.RESPIRATION_SAMPLING_RATE;
			sensorData = dataLoader.loadCSV(inputPath + "AUTOSENSE_RESPIRATION.csv");
			staticWindows.blankWindows(sensorData, startTime, endTime, DDT_PARAMETERS.WINDOW_SIZE);
			ripDiagnose();
		} else if (streamName.equals("ecg")) {
			phoneBatteryData = dataLoader.loadCSV(inputPath + "PHONE_BATTERY.csv");
			sensorBatteryData = dataLoader.loadCSV(inputPath + "AUTOSENSE_BATTERY.csv");
			
			samplingRate = DDT_PARAMETERS.ECG_SAMPLING_RATE;
			sensorData = dataLoader.loadCSV(inputPath + "AUTOSENSE_ECG.csv");
			staticWindows.blankWindows(sensorData, startTime, endTime, DDT_PARAMETERS.WINDOW_SIZE);
			ecgDiagnose();
		}else if (streamName.equals("motionsense")) {
			phoneBatteryData = dataLoader.loadCSV(inputPath + "PHONE_BATTERY.csv");
			sensorBatteryData = dataLoader.loadCSV(inputPath + "MOTION_SENSE_BATTERY.csv");
			
			samplingRate = DDT_PARAMETERS.MOTIONSENSE_SAMPLING_RATE;
			sensorData = dataLoader.loadWristCSV(inputPath + "MOTION_SENSE_ACCELEROMETER.csv");
			staticWindows.blankWindows(sensorData, startTime, endTime, DDT_PARAMETERS.WINDOW_SIZE);
			motionsenseWristBandDiagnose();
		} else if (streamName.equals("microsoft_band")) {
			phoneBatteryData = dataLoader.loadCSV(inputPath + "PHONE_BATTERY.csv");
			sensorBatteryData = dataLoader.loadCSV(inputPath + "MOTION_SENSE_BATTERY.csv");
			
			samplingRate = DDT_PARAMETERS.MOTIONSENSE_SAMPLING_RATE;
			sensorData = dataLoader.loadWristCSV(inputPath + "MICROSOFT_ACCELEROMETER.csv");
			staticWindows.blankWindows(sensorData, startTime, endTime, DDT_PARAMETERS.WINDOW_SIZE);
			motionsenseWristBandDiagnose();
		}
	}

	/**
	 * This method is responsible to execute data diagnostic markers for chest band
	 */
	private void ripDiagnose() {
		CSVExporter csvExporter = new CSVExporter();
		BatteryDataMarker batteryDataMarker = new BatteryDataMarker();
		SensorUnavailable sensorUnavailable = new SensorUnavailable();
		SensorOffBodyMarker bodyMarker = new SensorOffBodyMarker();
		DataLossMarker dataLossMarker = new DataLossMarker();
		SensorSignalQualityMarker sensorSignalQualityMarker = new SensorSignalQualityMarker();
		
		batteryDataMarker.phoneBatteryMarker(phoneBatteryData, staticWindows.blankWindows);
		
		batteryDataMarker.autoSenseBatteryMarker(sensorBatteryData, batteryDataMarker.phoneBattery);
		
		sensorUnavailable.autosenseWirelessDC(this.inputPath, batteryDataMarker.autoSenseBattery);
		
		bodyMarker.improperOrNoAttachmentRIP(this.inputPath, this.streamName, sensorUnavailable.sensorUnavailable);
		
		dataLossMarker.packetLossMarker(bodyMarker.improperOrNoAttachment, DDT_PARAMETERS.WINDOW_SIZE, samplingRate);
		
		sensorSignalQualityMarker.markWindowsQulaity(dataLossMarker.dataLoss);
		
		csvExporter.writeDataPointQualityToCSV(sensorSignalQualityMarker.markedWindows, this.outputPath, "new.txt");

	}
	
	private void ecgDiagnose() {
		CSVExporter csvExporter = new CSVExporter();
		BatteryDataMarker batteryDataMarker = new BatteryDataMarker();
		SensorUnavailable sensorUnavailable = new SensorUnavailable();
		SensorOffBodyMarker bodyMarker = new SensorOffBodyMarker();
		DataLossMarker dataLossMarker = new DataLossMarker();
		SensorSignalQualityMarker sensorSignalQualityMarker = new SensorSignalQualityMarker();
		
		batteryDataMarker.phoneBatteryMarker(phoneBatteryData, staticWindows.blankWindows);
		
		batteryDataMarker.autoSenseBatteryMarker(sensorBatteryData, batteryDataMarker.phoneBattery);
		
		sensorUnavailable.autosenseWirelessDC(this.inputPath, batteryDataMarker.autoSenseBattery);
		
		bodyMarker.improperOrNoAttachmentECG(sensorUnavailable.sensorUnavailable);
		
		dataLossMarker.packetLossMarker(bodyMarker.improperOrNoAttachment, DDT_PARAMETERS.WINDOW_SIZE, samplingRate);
		
		sensorSignalQualityMarker.markWindowsQulaity(dataLossMarker.dataLoss);
		
		csvExporter.writeDataPointQualityToCSV(sensorSignalQualityMarker.markedWindows, this.outputPath, "new.txt");

	}
	
	private void motionsenseWristBandDiagnose() {
		CSVExporter csvExporter = new CSVExporter();
		BatteryDataMarker batteryDataMarker = new BatteryDataMarker();
		SensorUnavailable sensorUnavailable = new SensorUnavailable();
		SensorOffBodyMarker bodyMarker = new SensorOffBodyMarker();
		DataLossMarker dataLossMarker = new DataLossMarker();
		SensorSignalQualityMarker sensorSignalQualityMarker = new SensorSignalQualityMarker();
		
		batteryDataMarker.phoneBatteryMarker(phoneBatteryData, staticWindows.blankWindows);
		
		batteryDataMarker.motionSenseBatteryMarker(sensorBatteryData, batteryDataMarker.phoneBattery);
		
		sensorUnavailable.autosenseWirelessDC(this.inputPath, batteryDataMarker.motionSenseBattery);
		
		dataLossMarker.packetLossMarker(batteryDataMarker.motionSenseBattery, DDT_PARAMETERS.WINDOW_SIZE, samplingRate);
		
		sensorSignalQualityMarker.markWindowsQulaity(dataLossMarker.dataLoss);
		
		csvExporter.writeDataPointQualityToCSV(sensorSignalQualityMarker.markedWindows, this.outputPath, "new.txt");

	}
	
}
