package org.md2k.datadiagnostic.util;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
