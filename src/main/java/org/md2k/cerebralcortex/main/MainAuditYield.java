package org.md2k.cerebralcortex.main;

import java.util.ArrayList;

import org.md2k.cerebralcortex.algorithms.ActiveInactiveEpisodes;
import org.md2k.cerebralcortex.algorithms.AttachmentLoss;
import org.md2k.cerebralcortex.algorithms.BatteryDownReport;
import org.md2k.cerebralcortex.algorithms.ChestSensorON;
import org.md2k.cerebralcortex.algorithms.GoodEpisodes;
import org.md2k.cerebralcortex.algorithms.GoodEpisodesUnderActivePeriod;
import org.md2k.cerebralcortex.algorithms.IntermittentPacketLoss;
import org.md2k.cerebralcortex.algorithms.PhoneONepisodes;
import org.md2k.cerebralcortex.algorithms.WearingEpisodes;
import org.md2k.cerebralcortex.algorithms.WirelessDiscon;
import org.md2k.cerebralcortex.struct.DataPoints;
import org.md2k.mcerebrum.utils.TimestampSampleVals;

public class MainAuditYield {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ArrayList<Double> timestampRIP = new ArrayList<Double>();

		TimestampSampleVals timestampSampleVals = new TimestampSampleVals();
		ArrayList<DataPoints> goodEpisodesDataPoints, wearingEpisodesDataPoints, ActiveEpisodesDataPoints,
				PhoneONEpisodesDataPoints, ChestOnSensorDataPoints, espisodeUnderActivePeriodDataPoints, intermittentPacketLossDataPoints, disconnectionsDataPoints = new ArrayList<DataPoints>();
		ArrayList<DataPoints> activeEpisodes = new ArrayList<DataPoints>();

		//TO-DO
		//This is temporary, pass. Use values calculated from actual disconnection algo
		DataPoints disconnectionMatrixtmp = new DataPoints(105, 1, 1.434320848018e12, 1.434320956885e12);
		disconnectionsDataPoints.add(disconnectionMatrixtmp);
		
		timestampRIP = timestampSampleVals.timestampArray();

		GoodEpisodes goodEpisodes = new GoodEpisodes();

		WearingEpisodes wearingEpisodes = new WearingEpisodes();
		ActiveInactiveEpisodes activeInactiveEpisodes = new ActiveInactiveEpisodes();
		PhoneONepisodes phoneOnEpisodes = new PhoneONepisodes();
		ChestSensorON chestSensorOn = new ChestSensorON();
		BatteryDownReport batteryDownReport = new BatteryDownReport();
		GoodEpisodesUnderActivePeriod goodEpisodesUnderActivePeriod = new GoodEpisodesUnderActivePeriod();
		WirelessDiscon wirelessDiscon = new WirelessDiscon();
		IntermittentPacketLoss intermittentPacketLoss = new IntermittentPacketLoss();
		AttachmentLoss attachmentLoss = new AttachmentLoss();

		//Sample data. This should be passed from main class
		ArrayList<Double> batterySampleVals = timestampSampleVals.sampleArrayBattery();
		ArrayList<Double> timestampBattery = timestampSampleVals.timestampArrayBattery();
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		// Calculate good expisodes
		goodEpisodesDataPoints = goodEpisodes.getGoodEpisodes(105, 1, timestampRIP, 1);
		System.out.println("Good Episodes " + goodEpisodesDataPoints.toString());
		// Calculate wearing episodes
		wearingEpisodesDataPoints = wearingEpisodes.getWearingByMergingGoodEpisodes(105, 1, goodEpisodesDataPoints, timestampRIP);
		System.out.println("Wearing Episodes " + wearingEpisodesDataPoints.toString());

		ActiveEpisodesDataPoints = activeInactiveEpisodes.getActiveInactiveEpisodesFromWearingEpisodes(105, 1, wearingEpisodesDataPoints);
		System.out.println("Active Episodes " + ActiveEpisodesDataPoints.toString());

		PhoneONEpisodesDataPoints = phoneOnEpisodes.getPhoneONepisodesByMergingPhoneAndChestSensorData(105, 1, timestampRIP,
				ActiveEpisodesDataPoints);
		System.out.println("Phone On Episodes " + PhoneONEpisodesDataPoints.toString());

		ChestOnSensorDataPoints = chestSensorOn.getChestSensorONunderPhoneOnPeriod(105, 1, timestampRIP, PhoneONEpisodesDataPoints);
		System.out.println("Chest Sensor On Episodes " + ChestOnSensorDataPoints.toString());

		// TO-DO
		//Current this will print the results inside the class
		//store it somewhere in object or a file
		batteryDownReport.getBatteryDownReport(105, 1, ChestOnSensorDataPoints, batterySampleVals, timestampBattery);

		espisodeUnderActivePeriodDataPoints = goodEpisodesUnderActivePeriod.getGoodEpisodesUnderActivePeriod(105, 1, timestampRIP, ChestOnSensorDataPoints,
				1);
		System.out.println("Episodes Under Active Period" + espisodeUnderActivePeriodDataPoints.toString());
		
		// TO-DO
		//Current this will print the results inside the class
		//IMPORTANT!! This will not work as there is no Wireless disconnection information in new format
		//If there is more than 30 second gap then it would be considered as Wireless disconnection
		wirelessDiscon.getWirelessDisconnAndTOStime(105, 1, timestampRIP, wearingEpisodesDataPoints);
		
		
		//TO-DO
		//only last start time stamp is wrong that is causing some extra packet loss
		intermittentPacketLossDataPoints = intermittentPacketLoss.getIntermittentPacketLoss(105, 1, timestampRIP, goodEpisodesDataPoints, wearingEpisodesDataPoints, disconnectionsDataPoints);
		System.out.println("Intermittent Packet Loss Data Points Period" + intermittentPacketLossDataPoints.toString());
		
		attachmentLoss.getAttachmentLoss(wearingEpisodesDataPoints, espisodeUnderActivePeriodDataPoints);
		
	}

}
