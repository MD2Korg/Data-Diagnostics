package org.md2k.datadiagnostic.main;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.md2k.datadiagnostic.attachment.DelayedAttachment;
import org.md2k.datadiagnostic.attachment.ImproperAttachment;
import org.md2k.datadiagnostic.configurations.DDT_PARAMETERS;
import org.md2k.datadiagnostic.data.DataLoader;
import org.md2k.datadiagnostic.marker.AcceptableUnacceptableData;
import org.md2k.datadiagnostic.marker.FixedSizeWindowing;
import org.md2k.datadiagnostic.marker.SessionMarker;
import org.md2k.datadiagnostic.marker.power.BatteryDataMarker;
import org.md2k.datadiagnostic.marker.wireless.DataLossMarker;
import org.md2k.datadiagnostic.marker.wireless.SensorUnavailableMarker;
import org.md2k.datadiagnostic.struct.DataPoints;
import org.md2k.datadiagnostic.util.Util;

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

		long startTime = Util.getStartDayTime(1472052574671l);
		long endTime = Util.getEndDayTime(1472052574671l);

		if (streamName.equals("respiration")) {
			phoneBatteryData = dataLoader.loadCSV(inputPath + "phone_battery.csv");
			sensorBatteryData = dataLoader.loadCSV(inputPath + "chest_sensor_battery.csv");

			samplingRate = DDT_PARAMETERS.AUTOSENSE_SAMPLING_RATE;
			sensorData = dataLoader.loadCSV(inputPath + "resp.csv");
			staticWindows.blankWindows(sensorData, startTime, endTime, DDT_PARAMETERS.WINDOW_SIZE);
			runner();
		} else if (streamName.equals("ecg")) {
			samplingRate = DDT_PARAMETERS.AUTOSENSE_SAMPLING_RATE;
			sensorData = dataLoader.loadCSV(inputPath + "ecg.csv");
			staticWindows.blankWindows(sensorData, startTime, endTime, DDT_PARAMETERS.WINDOW_SIZE);
			runner();
		} else if (streamName.equals("acc_microsoft_band")) {
			samplingRate = DDT_PARAMETERS.AUTOSENSE_SAMPLING_RATE;
			sensorData = dataLoader.loadCSV(inputPath + "acc_microsoft.csv");
			staticWindows.blankWindows(sensorData, startTime, endTime, DDT_PARAMETERS.WINDOW_SIZE);
			runner();
		}
	}

	private void runner() {
		markBatteryData();
		markDataLoss();
		dataQualityMarker();

	}

	public void markBatteryData() {
		BatteryDataMarker batteryDataMarker = new BatteryDataMarker();
		batteryDataMarker.phoneBatteryMarker(phoneBatteryData, staticWindows.blankWindows);
		batteryDataMarker.sensorBatteryMarker(sensorBatteryData, staticWindows.blankWindows);
	}

	public void markDataLoss() {
		SensorUnavailableMarker sensorUnavailable = new SensorUnavailableMarker();
		DataLossMarker dataLossMarker = new DataLossMarker();
		
		sensorUnavailable.wirelessDisconnectionsMarker(sensorData, staticWindows.blankWindows);
		dataLossMarker.packetLossMarker(staticWindows.blankWindows, DDT_PARAMETERS.WINDOW_SIZE, samplingRate);
	}

	public void dataQualityMarker() {

		AcceptableUnacceptableData activeInactiveEpisodes = new AcceptableUnacceptableData();
		DelayedAttachment delayedAttachment = new DelayedAttachment();
		ImproperAttachment improperAttachment = new ImproperAttachment();

		// Acceptable good quality data. From this point, only unacceptable
		// quality data
		activeInactiveEpisodes.separateAcceptableUnacceptableData(staticWindows.windows);
		System.out.println("Acceptable data: " + Util.dataPointsQualityTime(activeInactiveEpisodes.acceptableData)
				+ " Unacceptable data: " + Util.dataPointsQualityTime(activeInactiveEpisodes.unacceptableData));

		// Delay in attachment
		// delayedAttachment.getDelayInAttachment(activeInactiveEpisodes.acceptableData);
		// System.out.println("Delay in attachment: " +
		// delayedAttachment.totalDelayInAttachment);

		// Improper attachment/loose attachment
		improperAttachment.getImproperAttachmentPeriods(staticWindows.windows);
		System.out.println("Improper attachment: " + Util.dataPointsTime(improperAttachment.improperAttachment));
	}
}
