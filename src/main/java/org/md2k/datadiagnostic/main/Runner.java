package org.md2k.datadiagnostic.main;

import java.util.ArrayList;
import java.util.List;

import org.md2k.datadiagnostic.attachment.DelayedAttachment;
import org.md2k.datadiagnostic.attachment.ImproperAttachment;
import org.md2k.datadiagnostic.configurations.DDT_PARAMETERS;
import org.md2k.datadiagnostic.data.DataLoader;
import org.md2k.datadiagnostic.episodes.AcceptableUnacceptableData;
import org.md2k.datadiagnostic.episodes.FixedSizeWindow;
import org.md2k.datadiagnostic.episodes.SessionMarker;
import org.md2k.datadiagnostic.power.BatteryLevel;
import org.md2k.datadiagnostic.struct.DataPoints;
import org.md2k.datadiagnostic.util.Util;
import org.md2k.datadiagnostic.wireless.PacketLoss;
import org.md2k.datadiagnostic.wireless.Physicaldisconnections;

public class Runner {

	/**
	 * This class is responsible to execute all the data-diagnostic algorithms
	 */

	
	DataLoader dataLoader;
	List<DataPoints> dayStartData;
	List<DataPoints> sensorData;
	List<DataPoints> phoneBatteryData;
	List<DataPoints> sensorBatteryData;
	String outputPath;
	long dayStartTime;
	long dayEndTime;
	
	FixedSizeWindow fixedSizeWindows;
	double samplingRate;

	public Runner(String streamName, String inputPath, String outputPath) {
		dayStartData = new ArrayList<DataPoints>();
		sensorData = new ArrayList<DataPoints>();
		phoneBatteryData = new ArrayList<DataPoints>();
		sensorBatteryData = new ArrayList<DataPoints>();
		fixedSizeWindows = new FixedSizeWindow();
		dataLoader = new DataLoader();
		this.outputPath = outputPath;
		
		SessionMarker sessionMarker = new SessionMarker();

		// Calculate start and end time of a day (aka Active period)
		sessionMarker.getAllSessionStartEndTimes(fixedSizeWindows.windows, dayStartData);

		if (streamName.equals("battery")) {
			dayStartData = dataLoader.loadCSV(inputPath+"start_day.csv");
			phoneBatteryData = dataLoader.loadCSV(inputPath+"phone_battery.csv");
			sensorBatteryData = dataLoader.loadCSV(inputPath+"chest_sensor_battery.csv");
			fixedSizeWindows.createWindows(sensorData, DDT_PARAMETERS.WINDOW_SIZE);
			runner();
		}else if (streamName.equals("respiration")) {
			samplingRate = DDT_PARAMETERS.AUTOSENSE_SAMPLING_RATE;
			dayStartData = dataLoader.loadCSV(inputPath+"start_day.csv");
			sensorData = dataLoader.loadCSV(inputPath+"resp.csv");
			fixedSizeWindows.createWindows(sensorData, DDT_PARAMETERS.WINDOW_SIZE);
			runner();
		} else if (streamName.equals("ecg")) {
			samplingRate = DDT_PARAMETERS.AUTOSENSE_SAMPLING_RATE;
			dayStartData = dataLoader.loadCSV(inputPath+"start_day.csv");
			sensorData = dataLoader.loadCSV(inputPath+"ecg.csv");
			fixedSizeWindows.createWindows(sensorData, DDT_PARAMETERS.WINDOW_SIZE);
			runner();
		} else if (streamName.equals("acc_microsoft_band")) {
			samplingRate = DDT_PARAMETERS.AUTOSENSE_SAMPLING_RATE;
			dayStartData = dataLoader.loadCSV(inputPath+"start_day.csv");
			sensorData = dataLoader.loadCSV(inputPath+"acc_microsoft.csv");
			fixedSizeWindows.createWindows(sensorData, DDT_PARAMETERS.WINDOW_SIZE);
			runner();
		}
	}

	private void runner() {
		// Class objects
		SessionMarker sessionMarker = new SessionMarker();

		// Calculate start and end time of a day (aka Active period)
		sessionMarker.getAllSessionStartEndTimes(fixedSizeWindows.windows, dayStartData);

		// loop over all the start and end sessions available
		for (int i = 0; i < sessionMarker.startEndTimes.size(); i++) {
			diagnoseBatteryData(sessionMarker.startEndTimes.get(i).getTimestamp(),
					sessionMarker.startEndTimes.get(i).getEndTimestamp());

			diagnoseWirelessData(sessionMarker.startEndTimes.get(i).getTimestamp(),
					sessionMarker.startEndTimes.get(i).getEndTimestamp());

			diagnoseSensorData(sessionMarker.startEndTimes.get(i).getTimestamp(),
					sessionMarker.startEndTimes.get(i).getEndTimestamp());
			System.out.println("-------------------------------------------------------");
		}

	}

	public void diagnoseBatteryData(long sessionStartTime, long sessionEndTime) {
		BatteryLevel batteryLevel = new BatteryLevel();
		// calculate phone off time during active period
		batteryLevel.phoneBatteryDown(phoneBatteryData, sessionStartTime, sessionEndTime);
		batteryLevel.phonePoweredOff(phoneBatteryData, sessionStartTime, sessionEndTime);

		System.out.println("Phone battery down: "+Util.dataPointsTime(batteryLevel.phoneBatteryDown)+" -- Phone battery off: "+Util.dataPointsTime(batteryLevel.phonePoweredOff));
		
		// calculate sensor off time during active period
		batteryLevel.sensorBatteryDown(sensorBatteryData, sessionStartTime, sessionEndTime);
		batteryLevel.sensorPoweredOff(sensorBatteryData, sessionStartTime, sessionEndTime);
		System.out.println("Sensor battery down: "+Util.dataPointsTime(batteryLevel.sensorBatteryDown)+" -- Sensor battery off: "+Util.dataPointsTime(batteryLevel.sensorPoweredOff));
		
	}

	public void diagnoseWirelessData(long sessionStartTime, long sessionEndTime) {
		Physicaldisconnections physicaldisconnections = new Physicaldisconnections();
		PacketLoss packetLoss = new PacketLoss();
		// calculate wireless disconnection
		physicaldisconnections.getWirelessDisconnections(sensorData, sessionStartTime, sessionEndTime);
		System.out.println(
				"Wireless Disconnection: " + Util.dataPointsQualityTime(physicaldisconnections.wirelessDisconnections));

		// calculate packet loss
		// packet loss could be computed for both good and bad periods.
		// Currently, it computers for bad quality data
		packetLoss.countPacketLoss(fixedSizeWindows.windows, physicaldisconnections.wirelessDisconnections, 60,
				sessionStartTime, sessionEndTime, samplingRate);
	}

	public void diagnoseSensorData(long sessionStartTime, long sessionEndTime) {

		AcceptableUnacceptableData activeInactiveEpisodes = new AcceptableUnacceptableData();
		DelayedAttachment delayedAttachment = new DelayedAttachment();
		ImproperAttachment improperAttachment = new ImproperAttachment();

		// Acceptable good quality data. From this point, only unacceptable
		// quality data
		activeInactiveEpisodes.separateAcceptableUnacceptableData(fixedSizeWindows.windows, sessionStartTime,
				sessionEndTime);
		System.out.println("Acceptable data: " + Util.dataPointsQualityTime(activeInactiveEpisodes.acceptableData)+" Unacceptable data: " + Util.dataPointsQualityTime(activeInactiveEpisodes.unacceptableData));
		
		// Delay in attachment
		delayedAttachment.getDelayInAttachment(activeInactiveEpisodes.acceptableData, sessionStartTime);
		System.out.println("Delay in attachment: " + delayedAttachment.totalDelayInAttachment);
		
		// Improper attachment/loose attachment
		improperAttachment.getImproperAttachmentPeriods(fixedSizeWindows.windows, sessionStartTime, sessionEndTime);
		System.out.println("Improper attachment: " + Util.dataPointsTime(improperAttachment.improperAttachment));
	}
}
