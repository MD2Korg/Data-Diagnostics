package org.md2k.datadiagnostic.main;

import java.util.ArrayList;
import java.util.List;

import org.md2k.datadiagnostic.configurations.DDT_PARAMETERS;
import org.md2k.datadiagnostic.data.CSVExporter;
import org.md2k.datadiagnostic.data.DataLoader;
import org.md2k.datadiagnostic.marker.SensorAttachmentMarker;
import org.md2k.datadiagnostic.marker.SensorDataQualityMarker;
import org.md2k.datadiagnostic.marker.SensorUnavailable;
import org.md2k.datadiagnostic.marker.power.BatteryDataMarker;
import org.md2k.datadiagnostic.marker.wireless.DataLossMarker;
import org.md2k.datadiagnostic.marker.wireless.SensorUnavailableMarker;
import org.md2k.datadiagnostic.struct.DataPoints;
import org.md2k.datadiagnostic.util.Util;
import org.md2k.datadiagnostic.windowing.FixedSizeWindowing;

import demo.SampleData;

public class Runner {

	/**
	 * This class is responsible to execute all the data-diagnostic algorithms
	 */

	DataLoader dataLoader;
	List<DataPoints> sensorData;
	List<DataPoints> phoneBatteryData;
	List<DataPoints> sensorBatteryData;
	String streamName;

	FixedSizeWindowing staticWindows;
	double samplingRate;

	public Runner() {
		sensorData = new ArrayList<DataPoints>();
		phoneBatteryData = new ArrayList<DataPoints>();
		sensorBatteryData = new ArrayList<DataPoints>();
		staticWindows = new FixedSizeWindowing();
		dataLoader = new DataLoader();
		this.streamName = DDT_PARAMETERS.STREAM_NAME;

		//in future, get this information from a database
		//TODO
		long startTime = Util.getStartDayTime(1475082489918l);
		long endTime = Util.getEndDayTime(1475082489918l);

		if (streamName.equals("rip")) {
			phoneBatteryData = dataLoader.loadCSV(SampleData.PHONE_BATTERY);
			sensorBatteryData = dataLoader.loadCSV(SampleData.AUTOSENSE_BATTERY);

			samplingRate = DDT_PARAMETERS.RESPIRATION_SAMPLING_RATE;
			sensorData = dataLoader.loadCSV(SampleData.RIP_DATA);
			staticWindows.blankWindows(sensorData, startTime, endTime, DDT_PARAMETERS.WINDOW_SIZE);
			ripDiagnose();
		} else if (streamName.equals("ecg")) {
			phoneBatteryData = dataLoader.loadCSV(SampleData.PHONE_BATTERY);
			sensorBatteryData = dataLoader.loadCSV(SampleData.AUTOSENSE_BATTERY);
			
			samplingRate = DDT_PARAMETERS.ECG_SAMPLING_RATE;
			sensorData = dataLoader.loadCSV(SampleData.ECG_DATA);
			staticWindows.blankWindows(sensorData, startTime, endTime, DDT_PARAMETERS.WINDOW_SIZE);
			ecgDiagnose();
		}else if (streamName.equals("motionsense")) {
			phoneBatteryData = dataLoader.loadCSV(SampleData.PHONE_BATTERY);
			sensorBatteryData = dataLoader.loadCSV(SampleData.MOTIONSENSE_BATTERY);
			
			samplingRate = DDT_PARAMETERS.MOTIONSENSE_SAMPLING_RATE;
			sensorData = dataLoader.loadWristCSV(SampleData.MOTIONSENSE_ACCELEROMETER);
			staticWindows.blankWindows(sensorData, startTime, endTime, DDT_PARAMETERS.WINDOW_SIZE);
			motionsenseWristBandDiagnose();
		} else if (streamName.equals("microsoft_band")) {
			phoneBatteryData = dataLoader.loadCSV(SampleData.PHONE_BATTERY);
			sensorBatteryData = dataLoader.loadCSV(SampleData.MICROSOFT_BAND_BATTERY);
			
			samplingRate = DDT_PARAMETERS.MOTIONSENSE_SAMPLING_RATE;
			sensorData = dataLoader.loadWristCSV(SampleData.MICROSOFT_BAND_ACCELEROMETER);
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
		SensorAttachmentMarker sensorAttachmentMarker = new SensorAttachmentMarker();
		DataLossMarker dataLossMarker = new DataLossMarker();
		SensorDataQualityMarker sensorSignalQualityMarker = new SensorDataQualityMarker();
		
		batteryDataMarker.phoneBatteryMarker(phoneBatteryData, staticWindows.blankWindows);
		
		batteryDataMarker.senseBatteryMarker(sensorBatteryData, batteryDataMarker.markedWindows);
		
		sensorUnavailable.autoSenseWirelessDC(batteryDataMarker.markedWindows);
		
		sensorAttachmentMarker.improperOrNoAttachmentRIP(sensorUnavailable.markedWindows);
		
		dataLossMarker.packetLoss(sensorAttachmentMarker.markedWindows, DDT_PARAMETERS.WINDOW_SIZE, samplingRate);
		
		sensorSignalQualityMarker.markWindowsQulaity(dataLossMarker.markedWindows);
		
		csvExporter.writeDataPointQualityToCSV(sensorSignalQualityMarker.markedWindows, SampleData.OUTPUT_PATH, "new.txt");

	}
	
	private void ecgDiagnose() {
		CSVExporter csvExporter = new CSVExporter();
		BatteryDataMarker batteryDataMarker = new BatteryDataMarker();
		SensorUnavailable sensorUnavailable = new SensorUnavailable();
		SensorAttachmentMarker sensorAttachmentMarker = new SensorAttachmentMarker();
		DataLossMarker dataLossMarker = new DataLossMarker();
		SensorDataQualityMarker sensorSignalQualityMarker = new SensorDataQualityMarker();
		
		batteryDataMarker.phoneBatteryMarker(phoneBatteryData, staticWindows.blankWindows);
		
		batteryDataMarker.senseBatteryMarker(sensorBatteryData, batteryDataMarker.markedWindows);
		
		sensorUnavailable.autoSenseWirelessDC(batteryDataMarker.markedWindows);
		
		sensorAttachmentMarker.improperOrNoAttachmentECG(sensorUnavailable.markedWindows);
		
		dataLossMarker.packetLoss(sensorAttachmentMarker.markedWindows, DDT_PARAMETERS.WINDOW_SIZE, samplingRate);
		
		sensorSignalQualityMarker.markWindowsQulaity(dataLossMarker.markedWindows);
		
		csvExporter.writeDataPointQualityToCSV(sensorSignalQualityMarker.markedWindows, SampleData.OUTPUT_PATH, "new.txt");

	}
	
	private void motionsenseWristBandDiagnose() {
		CSVExporter csvExporter = new CSVExporter();
		BatteryDataMarker batteryDataMarker = new BatteryDataMarker();
		SensorUnavailable sensorUnavailable = new SensorUnavailable();
		SensorAttachmentMarker bodyMarker = new SensorAttachmentMarker();
		DataLossMarker dataLossMarker = new DataLossMarker();
		SensorDataQualityMarker sensorSignalQualityMarker = new SensorDataQualityMarker();
		
		batteryDataMarker.phoneBatteryMarker(phoneBatteryData, staticWindows.blankWindows);
		
		batteryDataMarker.motionSenseBatteryMarker(sensorBatteryData, batteryDataMarker.markedWindows);
		
		sensorUnavailable.autoSenseWirelessDC(batteryDataMarker.markedWindows);
		
		dataLossMarker.packetLoss(batteryDataMarker.markedWindows, DDT_PARAMETERS.WINDOW_SIZE, samplingRate);
		
		sensorSignalQualityMarker.markWindowsQulaity(dataLossMarker.markedWindows);
		
		csvExporter.writeDataPointQualityToCSV(sensorSignalQualityMarker.markedWindows, SampleData.OUTPUT_PATH, "new.txt");

	}
	
}
