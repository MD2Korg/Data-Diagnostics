package org.md2k.datadiagnostic.util;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.md2k.datadiagnostic.data.CSVExporter;
import org.md2k.datadiagnostic.struct.*;

import org.junit.experimental.theories.DataPoint;

public class Util {


	
	/**
	 * This method will sum all the timestamps stored in {@link DataPointQuality}
	 * @param dps {@link DataPointQuality}
	 * @return long totalTime
	 */
	public static long dataPointsQualityTime(List<DataPointQuality> dps) {
		long startTime, endTime, totalTime = 0;
		int temp;
		for (int i = 0; i < dps.size(); i++) {
			temp = dps.get(i).getDataPoints().size();
			startTime = dps.get(i).getDataPoints().get(0).getTimestamp();
			endTime = dps.get(i).getDataPoints().get(temp - 1).getTimestamp();

			totalTime += endTime - startTime;
		}

		return totalTime;
	}

	/**
	 * This method will sum all the timestamps stored in {@link DataPoint}
	 * @param dps {@link DataPoint}
	 * @return long totalTime
	 */
	public static long dataPointsTime(List<DataPoints> dps) {
		long totalTime = 0;

		for (int i = 0; i < dps.size(); i++) {
			totalTime += dps.get(i).getEndTimestamp() - dps.get(i).getTimestamp();
		}

		return totalTime;
	}

	/**
	 * This method will compare two objects of {@link DataPoints}. If a
	 * datapoint exist in both then it will be removed.
	 * 
	 * @param haystack {@link DataPoints} 
	 * @param needle
	 *            {@link DataPoints} 
	 * @return {@link DataPoints}
	 */
	
	public static List<DataPoints> compareTwoDP(List<DataPoints> haystack, List<DataPoints> needle) {
		List<DataPoints> finalDP = new ArrayList<DataPoints>();
		int tmp=0;
		for(int i=0;i<haystack.size();i++){
			
			for(int j=0;j<needle.size();j++){
				if(haystack.get(i).equals(needle.get(j))){
					tmp=0;
					break;
				}else{
					tmp=1;
				}
			}
			if(tmp==1){
				finalDP.add(new DataPoints(haystack.get(i).getTimestamp(), haystack.get(i).getEndTimestamp()));
			}
		}
		return finalDP;
	}

	public static void createWindows(List<DataPoints> data, long size) {
		long startTime, endTime;
		List<DataPointQuality> windows = new ArrayList<DataPointQuality>();
		List<DataPoints> tempArray = new ArrayList<DataPoints>();
		List<Double> tempDP = new ArrayList<Double>();
		startTime = data.get(0).getTimestamp();
		endTime = data.get(0).getTimestamp() + size;

		List<Integer> temp1 = new ArrayList<Integer>();
		
		for (int i = 0; i < data.size(); i++) {

			if (data.get(i).getTimestamp() >= startTime && data.get(i).getTimestamp() < endTime) {
				tempArray.add(new DataPoints(data.get(i).getTimestamp(), data.get(i).getValue()));
				tempDP.add(data.get(i).getValue());
				Statistics statistics = new Statistics(tempDP);
				temp1.add((int) data.get(i).getValue());
				if (i == data.size() - 1) {
					windows.add(new DataPointQuality(tempArray, (int) statistics.getVariance()));
				}
			} else {
				tempDP.add(data.get(i).getValue());
				Statistics statistics = new Statistics(tempDP);
				windows.add(new DataPointQuality(tempArray, (int) statistics.getVariance()));
				
				
				startTime = data.get(i).getTimestamp();
				endTime = data.get(i).getTimestamp() + size;
				tempArray.clear();
				tempArray.add(new DataPoints(data.get(i).getTimestamp(), data.get(i).getValue()));
				if (i == data.size() - 1) {
					windows.add(new DataPointQuality(tempArray, (int) statistics.getVariance()));
				}
				temp1.clear();
			}
		}
		CSVExporter.writeDataPointQualityToCSV(windows,"F:/workspace/memphis/md2k_projects/DataDiagnostics_v1/data/students/output/", "acce_merg_10.csv");
	}
	/**
	 * 
	 * @param currentTimestamp
	 *            any timestamp of a day
	 * @return 23:59:59 timestamp of currentTimestamp provided.
	 */
	public static long getEndDayTime(long currentTimestamp) {
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
	public static long getStartDayTime(long currentTimestamp) {
		Timestamp timestamp = new Timestamp(currentTimestamp);
		Date date2 = new Date(timestamp.getTime());

		date2.setHours(00);
		date2.setMinutes(00);
		date2.setSeconds(00);

		return date2.getTime();
	}
}
