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

	public static List<DataPointQuality> getDiffQualityWindows(List<DataPointQuality> windows, int quality) {
		List<DataPointQuality> qualityWindows = new ArrayList<DataPointQuality>();

		for (int i = 0; i < windows.size(); i++) {
			if (windows.get(i).getQuality() == quality) {
				qualityWindows.add(new DataPointQuality(windows.get(i).getDataPoints(), quality));
			}
		}
		return qualityWindows;
	}

	public static void writeToCSV(List<DataPointQuality> windows, String fileName) {
		
		if(fileName.equals("")){
			fileName = "F:/workspace/memphis/md2k_projects/DataDiagnostics_v1/data/ali804/results/vari.txt";
		}else{
			fileName = "F:/workspace/memphis/md2k_projects/DataDiagnostics_v1/data/ali804/results/"+fileName;
		}
		List<DataPoints> dp = new ArrayList<DataPoints>();
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), "utf-8"))) {
			for (int i = 0; i < windows.size(); i++) {

				dp = windows.get(i).getDataPoints();
				// for(int j=0;j<dp.size();j++)
				{
					writer.write(dp.get(0).getTimestamp() + ", " + dp.get(dp.size()-1).getTimestamp()+", "+windows.get(i).getQuality() + "\r");

				}

			}

		} catch (UnsupportedEncodingException e) {

			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void writeToCSV(ArrayList<DataPoints> windows, String fileName) {
		if(fileName.equals("")){
			fileName = "F:/workspace/memphis/md2k_projects/DataDiagnostics_v1/data/ali804/results/vari.txt";
		}else{
			fileName = "F:/workspace/memphis/md2k_projects/DataDiagnostics_v1/data/ali804/results/"+fileName;
		}
		//String fileName = "F:/workspace/memphis/md2k_projects/DataDiagnostics_v1/data/ali804/results/quality_windows.txt";
		List<DataPoints> dp = new ArrayList<DataPoints>();
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), "utf-8"))) {
			for (int i = 0; i < windows.size(); i++) {

				// for(int j=0;j<dp.size();j++)
				{

					/*//human readable time and date
					  Date startTime = new Date(windows.get(i).getTimestamp());
					Date endTime = new Date(windows.get(i).getEndTimestamp());
					
					writer.write( startTime+ ", " + endTime + "\r");*/
					
					writer.write(windows.get(i).getTimestamp() + ", " + windows.get(i).getEndTimestamp() + "\r");

				}

			}

		} catch (UnsupportedEncodingException e) {

			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
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
	 * @return 
	 */
	
	public static List<DataPoints> compareTwoDP(List<DataPoints> haystack, List<DataPoints> needle) {
		List<DataPoints> finalDP = new ArrayList<DataPoints>();
		int tmp=0;
		for(int i=0;i<haystack.size();i++){
			
			for(int j=0;j<needle.size();j++){
				//System.out.println(haystack.get(i).getTimestamp()+" == "+needle.get(j).getTimestamp()+" ---- "+haystack.get(i).getEndTimestamp()+" == "+needle.get(j).getEndTimestamp());
				//System.out.println();
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

	public static void checkDataPointExist(List<DataPoints> dp1, List<DataPoints> dp2) {

	}
	
	/**
	 * This method is equivalent to diff() method in Matlab
	 * @param timestamp Array of timestamps
	 * @return diffTimestamp		This value will contain the diffs of all the a double array
	 * */
	public static ArrayList<Double> FindDiffs(ArrayList<Double> values){
		ArrayList<Double> diffTimestamp = new ArrayList<Double>();
		
		for(int i=0; i<values.size()-1; i++){
			//Convert to minutes
			diffTimestamp.add(Math.abs((values.get(i+1))-values.get(i)));
			/*if(diffTimestamp[i]>10){
				System.out.println("great val"+diffTimestamp[i]);
			}*/
		}
		return diffTimestamp;
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
