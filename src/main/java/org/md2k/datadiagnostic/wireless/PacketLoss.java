package org.md2k.datadiagnostic.wireless;

import java.util.ArrayList;
import java.util.List;

import org.md2k.datadiagnostic.configurations.DATA_QUALITY;
import org.md2k.datadiagnostic.configurations.DDT_PARAMETERS;
import org.md2k.datadiagnostic.struct.DataPointQuality;
import org.md2k.datadiagnostic.struct.DataPoints;

public class PacketLoss {
	public long totalLostPackets;
	public PacketLoss(){
		totalLostPackets=0;
	}
	/**
	 * Calculates number of packets missed for all timestamp windows. 
	 * Algo.
	 * NoOfMissedPackets = ExpectedSamples - ReceivedSamples;
	 * 
	 * @param windows
	 *            timestamp windows marked with quality labels
	 * @param wirelessDisconnections
	 *            timestamp windows of {@link Physicaldisconnections}
	 * @param windowSize
	 *            in milliseconds
	 * @param startDayTime
	 *            start time of a day in milliseconds
	 * @param endDayTime
	 *            end time of a day in milliseconds
	 */
	public void countPacketLoss(List<DataPointQuality> windows, List<DataPointQuality> wirelessDisconnections,
			double windowSize, long startDayTime, long endDayTime, double samplingRate) {
		long startTime;
		double endTime;
		long expectedSamples = 0;
		int size;

		for (int i = 0; i < windows.size(); i++) {
			size = windows.get(i).getDataPoints().size();
			if (windows.get(i).getDataPoints().get(0).getTimestamp() >= startDayTime
					&& windows.get(i).getDataPoints().get(size - 1).getTimestamp() <= endDayTime) {
				// check, window quality should be good
				size = windows.get(i).getDataPoints().size();
				startTime = windows.get(i).getDataPoints().get(0).getTimestamp();
				endTime = windows.get(i).getDataPoints().get(size - 1).getValue();
				if (!isWirelessDisconn(wirelessDisconnections, startTime, endTime)) {
					expectedSamples = (long) (windowSize * samplingRate);
					if (expectedSamples > size) {
						totalLostPackets += expectedSamples - size;
					}
				}
			}
		}
		System.out.println("Total lost packets: " + totalLostPackets);
	}

	/**
	 * Checks packet loss is not due to wireless disconnection
	 * 
	 * @param wirelessDisconnections timestamp windows of wireless disconnections
	 * @param startTime start time of a day in milliseconds
	 * @param endTime end time of a day in milliseconds
	 * @return true if packet loss is due to wireless disconnections false otherwise
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
