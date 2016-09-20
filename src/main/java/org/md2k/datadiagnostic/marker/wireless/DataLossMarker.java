package org.md2k.datadiagnostic.marker.wireless;

import java.util.ArrayList;
import java.util.List;

import org.md2k.datadiagnostic.configurations.DATA_QUALITY;
import org.md2k.datadiagnostic.configurations.DDT_PARAMETERS;
import org.md2k.datadiagnostic.configurations.METADATA;
import org.md2k.datadiagnostic.struct.DataPointQuality;
import org.md2k.datadiagnostic.struct.DataPoints;

public class DataLossMarker {
	public final List<DataPointQuality> dataLoss;

	public DataLossMarker() {
		dataLoss = new ArrayList<DataPointQuality>();
	}

	/**
	 * Calculates number of packets missed for all timestamp windows. Algo.
	 * NoOfMissedPackets = ExpectedSamples - ReceivedSamples;
	 * 
	 * @param windows
	 *            timestamp windows marked with quality labels
	 * @param markedWindows
	 *            timestamp windows of {@link SensorUnavailableMarker}
	 * @param windowSize
	 *            in milliseconds
	 * @param startDayTime
	 *            start time of a day in milliseconds
	 * @param endDayTime
	 *            end time of a day in milliseconds
	 */
	public void packetLossMarker(List<DataPointQuality> blankWindows, double windowSize, double samplingRate) {
		// long startTime;
		// double endTime;
		long expectedSamples = 0;
		int size;

		expectedSamples = (long) ((windowSize/1000) * samplingRate);

		for (int i = 0; i < blankWindows.size(); i++) {
			if (blankWindows.get(i).getQuality() == 999) {
				size = blankWindows.get(i).getDataPoints().size();
				double actualSamples = (double)size/expectedSamples;
				if(actualSamples<DDT_PARAMETERS.MINIMUM_ACCEPTABLE_PACKET_LOSS){
					System.out.println(blankWindows.get(i).getDataPoints().get(0).getTimestamp());
					blankWindows.get(i).setQuality(METADATA.DATA_LOST);
				}
			}

		}
		dataLoss.addAll(blankWindows);
		
		// check, window quality should be good
		/*
		 * size = blankWindows.get(i).getDataPoints().size(); startTime =
		 * blankWindows.get(i).getDataPoints().get(0).getTimestamp(); endTime =
		 * blankWindows.get(i).getDataPoints().get(size - 1).getValue(); if
		 * (!isWirelessDisconn(wirelessDisconnections, startTime, endTime)) {
		 * expectedSamples = (long) (windowSize * samplingRate); if
		 * (expectedSamples > size) { totalLostPackets += expectedSamples -
		 * size; } }
		 * 
		 * } System.out.println("Total lost packets: " + totalLostPackets);
		 */
	}

	/**
	 * Checks packet loss is not due to wireless disconnection
	 * 
	 * @param wirelessDisconnections
	 *            timestamp windows of wireless disconnections
	 * @param startTime
	 *            start time of a day in milliseconds
	 * @param endTime
	 *            end time of a day in milliseconds
	 * @return true if packet loss is due to wireless disconnections false
	 *         otherwise
	 */
	public boolean isWirelessDisconn(List<DataPointQuality> wirelessDisconnections, long startTime, double endTime) {
		long wdStartTime;
		double wdEndTime;
		int temp;
		for (int i = 0; i < wirelessDisconnections.size(); i++) {
			temp = wirelessDisconnections.get(i).getDataPoints().size();
			wdStartTime = wirelessDisconnections.get(i).getDataPoints().get(0).getTimestamp();
			wdEndTime = wirelessDisconnections.get(i).getDataPoints().get(temp - 1).getTimestamp();

			if (wdStartTime <= startTime && wdEndTime >= endTime) {
				return true;
			} else {
				return false;
			}

		}
		return false;
	}
}
