package org.md2k.datadiagnostic.episodes;

import java.util.ArrayList;
import java.util.List;

import org.md2k.datadiagnostic.configurations.DATA_QUALITY;
import org.md2k.datadiagnostic.struct.DataPointQuality;

public class AcceptableUnacceptableData {

	public final List<DataPointQuality> acceptableData;
	public final List<DataPointQuality> unacceptableData;

	/**
	 * Constructor
	 */
	public AcceptableUnacceptableData() {
		acceptableData = new ArrayList<DataPointQuality>();
		unacceptableData = new ArrayList<DataPointQuality>();
	}

	/**
	 * 
	 * @param windows marked with quality labels
	 * @return all windows labeled as good quality labels
	 */
	public List<DataPointQuality> getAcceptableData(List<DataPointQuality> windows) {

		return acceptableData;

	}

	/**
	 * 
	 * @param windows marked with quality labels
	 * @return all windows labeled as not good quality labels
	 */
	public List<DataPointQuality> getUnacceptable(List<DataPointQuality> windows) {

		return unacceptableData;

	}

	/**
	 * Merge all good and not good quality windows. It will produce two big sets of windows, i.e., acceptable and unacceptable quality data. 
	 * 
	 * @param windows marked with quality labels
	 * @param start time of a day milliseconds
	 * @param end time of a day in milliseconds 
	 */
	public void separateAcceptableUnacceptableData(List<DataPointQuality> windows, long startDayTime,
			long endDayTime) {
		int size=0;
		for (int i = 0; i < windows.size(); i++) {
			size = windows.get(i).getDataPoints().size();
			if (windows.get(i).getDataPoints().get(0).getTimestamp() >= startDayTime
					&& windows.get(i).getDataPoints().get(size-1).getTimestamp() <= endDayTime) {
				if (windows.get(i).getQuality() == DATA_QUALITY.GOOD) {
					this.acceptableData
							.add(new DataPointQuality(windows.get(i).getDataPoints(), DATA_QUALITY.ACCEPTABLE_DATA));
				} else if(windows.get(i).getQuality() == DATA_QUALITY.DATA_LOST){
					this.unacceptableData
							.add(new DataPointQuality(windows.get(i).getDataPoints(), DATA_QUALITY.UNACCEPTABLE_DATA));
				}
			}
		}
	}

	/**
	 * 
	 * @param qualityWindows windows marked with quality labels
	 * @return total time for qualityWindows
	 *//*
	public long getTotalAccetpableUnacceptableTime(List<DataPointQuality> qualityWindows) {
		long startTime, endTime, diffTime = 0;
		int temp;
		for (int i = 0; i < qualityWindows.size(); i++) {
			temp = qualityWindows.get(i).getDataPoints().size();
			startTime = qualityWindows.get(i).getDataPoints().get(0).getTimestamp();
			endTime = qualityWindows.get(i).getDataPoints().get(temp - 1).getTimestamp();

			diffTime += endTime - startTime;
		}

		return (diffTime);

	}*/

}
