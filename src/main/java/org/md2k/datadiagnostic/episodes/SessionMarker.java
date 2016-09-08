package org.md2k.datadiagnostic.episodes;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.md2k.datadiagnostic.configurations.DATA_QUALITY;
import org.md2k.datadiagnostic.configurations.DDT_PARAMETERS;
import org.md2k.datadiagnostic.data.DataLoader;
import org.md2k.datadiagnostic.struct.DataPointQuality;
import org.md2k.datadiagnostic.struct.DataPoints;
import org.md2k.datadiagnostic.util.Util;

public class SessionMarker {

	public List<DataPoints> startEndTimes;

	public SessionMarker() {
		startEndTimes = new ArrayList<DataPoints>();
	}

	/**
	 * Calculate start and end time of a day period
	 * 
	 * @param dayStartData
	 * 
	 * @param Windows
	 *            ({@link DataPointQuality}) marked with quality tags
	 */
	public void getAllSessionStartEndTimes(List<DataPointQuality> windows, List<DataPoints> dayStartData) {
		for (int i = 0; i < dayStartData.size(); i++) {
			startEndTimes.add(new DataPoints(dayStartData.get(i).getTimestamp(),
					(long) getEndTimes(windows, dayStartData.get(i).getTimestamp())));
		}
		System.out.println(startEndTimes.toString());
	}

	/**
	 * This algo uses start-day time as a session start time. To determine
	 * session end time: 1 - It adds 24 hours time to start time; 2 - Checks
	 * when the last more than 1 minute quality window was captured; 3 - Uses
	 * last quality window end time as session-end time.
	 * 
	 * @param Windows
	 *            ({@link DataPointQuality}) marked with quality tags
	 * @param startDayTime
	 *            start time of a day
	 * @return end time of a day (long)
	 */
	public long getEndTimes(List<DataPointQuality> windows, long startDayTime) {
		FixedSizeWindow episodeWindow = new FixedSizeWindow();
		ArrayList<DataPoints> goodQualityWindows = new ArrayList<DataPoints>();
		ArrayList<DataPoints> badQualityWindows = new ArrayList<DataPoints>();

		long sessionEndTime = 0;
		long endDay = 0;
		int size = 0;
		// Adds 24 hours in the start-time to make a 24 hours window
		endDay = startDayTime + 86400000;

		for (int i = 0; i < windows.size(); i++) {
			if (windows.get(i).getQuality() == 0
					&& windows.get(i).getDataPoints().get(0).getTimestamp() > startDayTime) {
				size = windows.get(i).getDataPoints().size();
				goodQualityWindows.add(new DataPoints(windows.get(i).getDataPoints().get(0).getTimestamp(),
						windows.get(i).getDataPoints().get(size - 1).getTimestamp()));
			} else {
				size = windows.get(i).getDataPoints().size();
				badQualityWindows.add(new DataPoints(windows.get(i).getDataPoints().get(0).getTimestamp(),
						windows.get(i).getDataPoints().get(size - 1).getTimestamp()));
			}
		}

		// Merge all the windows if they have max 1 minute gap
		goodQualityWindows = episodeWindow.mergeDataPointsWindows(goodQualityWindows, 60000);
		
		
		//Write Active windows time in a file
		if(DDT_PARAMETERS.GENERATE_CSV_FILES==1){
			Util.writeToCSV(goodQualityWindows, DDT_PARAMETERS.ACTIVE_MERGED_WINDOWS_CSV);
		}
		
		ArrayList<DataPoints> temp = new ArrayList<DataPoints>();
		for (int j = 0; j < goodQualityWindows.size(); j++) {
			// If before 12 night there is more than 1 minute windows are
			// available then keep adding into timestamp array
			if (goodQualityWindows.get(j).getTimestamp() < endDay) {
				if (goodQualityWindows.size() == 1) {
					temp.add(new DataPoints(goodQualityWindows.get(j).getTimestamp(),
							goodQualityWindows.get(j).getEndTimestamp()));
				} else if (goodQualityWindows.get(j).getEndTimestamp()
						- goodQualityWindows.get(j).getTimestamp() > 60000) {
					temp.add(new DataPoints(goodQualityWindows.get(j).getTimestamp(),
							goodQualityWindows.get(j).getEndTimestamp()));
				}
			}
		}

		if (temp.size() != 0) {
			sessionEndTime = temp.get(temp.size() - 1).getEndTimestamp();
		} else {
			return (Long) null;
		}
		return sessionEndTime;
	}

	/**
	 * This algo. looks for first more than 1 minute quality window and uses
	 * start time of the window as session-start-time. To determine
	 * session-end-time, it scans 12:00AM to 12:00AM (24 hours) period to find
	 * last more than 1 minute quality window.
	 * 
	 * @param Windows
	 *            ({@link DataPointQuality}) marked with quality tags
	 * @return List<DataPoints> startTime and EndTime
	 */
	public List<DataPoints> getEndTimes(List<DataPointQuality> windows) {
		FixedSizeWindow episodeWindow = new FixedSizeWindow();
		ArrayList<DataPoints> goodQualityWindows = new ArrayList<DataPoints>();
		ArrayList<DataPoints> badQualityWindows = new ArrayList<DataPoints>();

		List<DataPoints> sessionStartEndTime = new ArrayList<DataPoints>();
		long endDayTime = 0, startDayTime=0;
		int size = 0;
		// Adds 24 hours in the start-time to make a 24 hours window
		startDayTime = getStartDayTime(windows.get(0).getDataPoints().get(0).getTimestamp());
		endDayTime = getStartDayTime(windows.get(0).getDataPoints().get(0).getTimestamp());

		for (int i = 0; i < windows.size(); i++) {
			if (windows.get(i).getQuality() == 0
					&& windows.get(i).getDataPoints().get(0).getTimestamp() > startDayTime) {
				size = windows.get(i).getDataPoints().size();
				goodQualityWindows.add(new DataPoints(windows.get(i).getDataPoints().get(0).getTimestamp(),
						windows.get(i).getDataPoints().get(size - 1).getTimestamp()));
			} else {
				size = windows.get(i).getDataPoints().size();
				badQualityWindows.add(new DataPoints(windows.get(i).getDataPoints().get(0).getTimestamp(),
						windows.get(i).getDataPoints().get(size - 1).getTimestamp()));
			}
		}

		// Merge all the windows if they have max 1 minute gap
		goodQualityWindows = episodeWindow.mergeDataPointsWindows(goodQualityWindows, 60000);

		ArrayList<DataPoints> temp = new ArrayList<DataPoints>();
		for (int j = 0; j < goodQualityWindows.size(); j++) {
			if (goodQualityWindows.get(j).getTimestamp() > startDayTime && goodQualityWindows.get(j).getTimestamp() < endDayTime) {
				if (goodQualityWindows.size() == 1) {
					temp.add(new DataPoints(goodQualityWindows.get(j).getTimestamp(),
							goodQualityWindows.get(j).getEndTimestamp()));
				} else if (goodQualityWindows.get(j).getEndTimestamp()
						- goodQualityWindows.get(j).getTimestamp() > 60000) {
					temp.add(new DataPoints(goodQualityWindows.get(j).getTimestamp(),
							goodQualityWindows.get(j).getEndTimestamp()));
				}
			}
		}
		sessionStartEndTime.add(new DataPoints(temp.get(0).getTimestamp(), temp.get(temp.size()-1).getEndTimestamp()));
		return sessionStartEndTime;
	}

	/**
	 * 
	 * @param currentTimestamp
	 *            any timestamp of a day
	 * @return 23:59:59 timestamp of currentTimestamp provided.
	 */
	private long getEndDayTime(long currentTimestamp) {
		Timestamp timestamp = new Timestamp(currentTimestamp);
		Date date2 = new Date(timestamp.getTime());

		date2.setHours(23);
		date2.setMinutes(59);
		date2.setSeconds(59);

		return date2.getTime();
	}

	/**
	 * 
	 * @param currentTimestamp
	 *            any timestamp of a day
	 * @return 00:00:00 timestamp of currentTimestamp provided.
	 */
	private long getStartDayTime(long currentTimestamp) {
		Timestamp timestamp = new Timestamp(currentTimestamp);
		Date date2 = new Date(timestamp.getTime());

		date2.setHours(00);
		date2.setMinutes(00);
		date2.setSeconds(00);

		return date2.getTime();
	}
}
