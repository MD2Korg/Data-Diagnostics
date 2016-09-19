package org.md2k.datadiagnostic.main;

import java.util.ArrayList;
import java.util.List;

import org.md2k.datadiagnostic.attachment.DelayedAttachment;
import org.md2k.datadiagnostic.attachment.ImproperAttachment;
import org.md2k.datadiagnostic.configurations.DDT_PARAMETERS;
import org.md2k.datadiagnostic.data.DataLoader;
import org.md2k.datadiagnostic.marker.AcceptableUnacceptableData;
import org.md2k.datadiagnostic.marker.FixedSizeWindowing;
import org.md2k.datadiagnostic.marker.SessionMarker;
import org.md2k.datadiagnostic.marker.power.BatteryLevel;
import org.md2k.datadiagnostic.marker.wireless.PacketLoss;
import org.md2k.datadiagnostic.marker.wireless.Physicaldisconnections;
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
	
	FixedSizeWindowing fixedSizeWindows;
	double samplingRate;

	public Runner(String streamName, String inputPath, String outputPath) {
		sensorData = new ArrayList<DataPoints>();
		phoneBatteryData = new ArrayList<DataPoints>();
		sensorBatteryData = new ArrayList<DataPoints>();
		fixedSizeWindows = new FixedSizeWindowing();
		dataLoader = new DataLoader();
		this.outputPath = outputPath;
		
		long startTime = Util.getStartDayTime(1472052574671l);
		long endTime = Util.getEndDayTime(1472052574671l);
		
		if (streamName.equals("battery")) {
			phoneBatteryData = dataLoader.loadCSV(inputPath+"phone_battery.csv");
			sensorBatteryData = dataLoader.loadCSV(inputPath+"chest_sensor_battery.csv");
			fixedSizeWindows.createWindows(sensorData, DDT_PARAMETERS.WINDOW_SIZE);
			runner();
		}else if (streamName.equals("respiration")) {
			phoneBatteryData = dataLoader.loadCSV(inputPath+"phone_battery.csv");
			sensorBatteryData = dataLoader.loadCSV(inputPath+"chest_sensor_battery.csv");
			
			samplingRate = DDT_PARAMETERS.AUTOSENSE_SAMPLING_RATE;
			sensorData = dataLoader.loadCSV(inputPath+"resp.csv");
			fixedSizeWindows.blankWindows(sensorData, startTime, endTime, DDT_PARAMETERS.WINDOW_SIZE);
			runner();
		} else if (streamName.equals("ecg")) {
			samplingRate = DDT_PARAMETERS.AUTOSENSE_SAMPLING_RATE;
			sensorData = dataLoader.loadCSV(inputPath+"ecg.csv");
			fixedSizeWindows.createWindows(sensorData, DDT_PARAMETERS.WINDOW_SIZE);
			runner();
		} else if (streamName.equals("acc_microsoft_band")) {
			samplingRate = DDT_PARAMETERS.AUTOSENSE_SAMPLING_RATE;
			sensorData = dataLoader.loadCSV(inputPath+"acc_microsoft.csv");
			fixedSizeWindows.createWindows(sensorData, DDT_PARAMETERS.WINDOW_SIZE);
			runner();
		}
	}

	private void runner() {
			diagnoseBatteryData();

			diagnoseWirelessData();

			diagnoseSensorData();
			
			System.out.println("-------------------------------------------------------");
	}

	public void phoneOnMarker(){
		
	}
	public void diagnoseBatteryData() {
		BatteryLevel batteryLevel = new BatteryLevel();
		// calculate phone off time during active period
		batteryLevel.phoneBatteryDown(phoneBatteryData, fixedSizeWindows.blankWindows);
		//batteryLevel.phonePoweredOff(phoneBatteryData);

		System.out.println("Phone battery down: "+Util.dataPointsTime(batteryLevel.phoneBatteryDown)+" -- Phone battery off: "+Util.dataPointsTime(batteryLevel.phonePoweredOff));
		
		// calculate sensor off time during active period
		batteryLevel.sensorBatteryDown(sensorBatteryData, fixedSizeWindows.blankWindows);
		//batteryLevel.sensorPoweredOff(sensorBatteryData);
		System.out.println("Sensor battery down: "+Util.dataPointsTime(batteryLevel.sensorBatteryDown)+" -- Sensor battery off: "+Util.dataPointsTime(batteryLevel.sensorPoweredOff));
		
	}

	public void diagnoseWirelessData() {
		Physicaldisconnections physicaldisconnections = new Physicaldisconnections();
		PacketLoss packetLoss = new PacketLoss();
		// calculate wireless disconnection
		physicaldisconnections.getWirelessDisconnections(sensorData, fixedSizeWindows.blankWindows);
		System.out.println(
				"Wireless Disconnection: " + Util.dataPointsQualityTime(physicaldisconnections.wirelessDisconnections));

		// calculate packet loss
		// packet loss could be computed for both good and bad periods.
		// Currently, it computers for bad quality data
		packetLoss.countPacketLoss(fixedSizeWindows.blankWindows, 60, samplingRate);
	}

	public void diagnoseSensorData() {

		AcceptableUnacceptableData activeInactiveEpisodes = new AcceptableUnacceptableData();
		DelayedAttachment delayedAttachment = new DelayedAttachment();
		ImproperAttachment improperAttachment = new ImproperAttachment();

		// Acceptable good quality data. From this point, only unacceptable
		// quality data
		activeInactiveEpisodes.separateAcceptableUnacceptableData(fixedSizeWindows.windows);
		System.out.println("Acceptable data: " + Util.dataPointsQualityTime(activeInactiveEpisodes.acceptableData)+" Unacceptable data: " + Util.dataPointsQualityTime(activeInactiveEpisodes.unacceptableData));
		
		// Delay in attachment
		//delayedAttachment.getDelayInAttachment(activeInactiveEpisodes.acceptableData);
		//System.out.println("Delay in attachment: " + delayedAttachment.totalDelayInAttachment);
		
		// Improper attachment/loose attachment
		improperAttachment.getImproperAttachmentPeriods(fixedSizeWindows.windows);
		System.out.println("Improper attachment: " + Util.dataPointsTime(improperAttachment.improperAttachment));
	}
}
