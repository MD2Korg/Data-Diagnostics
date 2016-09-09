package org.md2k.datadiagnostic.episodes;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

import org.md2k.datadiagnostic.util.*;
import org.md2k.datadiagnostic.configurations.DDT_PARAMETERS;
import org.md2k.datadiagnostic.signalquality.algorithms.*;
import org.md2k.datadiagnostic.struct.*;

/**
 * This will create fixed equal size windows of time-series data
 * @author Nasir Ali
 *
 */
public class FixedSizeWindow {

	
	public List<DataPointQuality> windows;
	//public List<DataPointQuality> ecgWindows;
	//public List<DataPointQuality> aclWindows;
	//public ArrayList<DataPoints> mergedWindows;
	
	public FixedSizeWindow(){
		windows = new ArrayList<DataPointQuality>();
		//ecgWindows = new ArrayList<DataPointQuality>();
		//aclWindows = new ArrayList<DataPointQuality>();
		//mergedWindows = new ArrayList<DataPoints>();
	}
	
	/**
	 * Windowing function for DataPoint arrays
	 *
	 * @param data
	 *            Input data array
	 * @param size
	 *            Time window size in milliseconds
	 * @return ArrayList of data split by size
	 */
	public void createWindows(List<DataPoints> data, long size) {
		//RIPQualityCalculation ripQualityCalculation = new RIPQualityCalculation();
		VarianceBasedDataQuality varianceBasedDataQuality = new VarianceBasedDataQuality();
		long startTime, endTime;
		List<DataPoints> tempArray = new ArrayList<DataPoints>();
		startTime = data.get(0).getTimestamp();
		endTime = data.get(0).getTimestamp() + size;

		List<Integer> temp1 = new ArrayList<Integer>();

		for (int i = 0; i < data.size(); i++) {

			if (data.get(i).getTimestamp() >= startTime && data.get(i).getTimestamp() < endTime) {
				tempArray.add(new DataPoints(data.get(i).getTimestamp(), data.get(i).getValue()));
				temp1.add((int) data.get(i).getValue());
				if (i == data.size() - 1) {
					windows.add(new DataPointQuality(tempArray, varianceBasedDataQuality.currentQuality(tempArray)));
				}
			} else {
				windows.add(new DataPointQuality(tempArray, varianceBasedDataQuality.currentQuality(tempArray)));
				startTime = data.get(i).getTimestamp();
				endTime = data.get(i).getTimestamp() + size;
				tempArray.clear();
				tempArray.add(new DataPoints(data.get(i).getTimestamp(), data.get(i).getValue()));
				if (i == data.size() - 1) {
					windows.add(new DataPointQuality(tempArray, varianceBasedDataQuality.currentQuality(tempArray)));
				}
				temp1.clear();
			}
		}
				//Write Active windows time in a file
				if(DDT_PARAMETERS.GENERATE_CSV_FILES==1){
					Util.writeToCSV(windows, DDT_PARAMETERS.ACTIVE_WINDOWS_CSV);
				}
	}
	
	/**
	 * Windowing function for DataPoint arrays
	 *
	 * @param data
	 *            Input data array
	 * @param size
	 *            Time window size in milliseconds
	 * @return ArrayList of data split by size
	 */
	/*public void createACLWindows(List<DataPoints> data, long size) {
		VarianceBasedDataQuality aclQualityCalculation = new VarianceBasedDataQuality();
		long startTime, endTime;
		List<DataPoints> tempArray = new ArrayList<DataPoints>();
		startTime = data.get(0).getTimestamp();
		endTime = data.get(0).getTimestamp() + size;

		List<Integer> temp1 = new ArrayList<Integer>();

		for (int i = 0; i < data.size(); i++) {

			if (data.get(i).getTimestamp() >= startTime && data.get(i).getTimestamp() < endTime) {
				tempArray.add(new DataPoints(data.get(i).getTimestamp(), data.get(i).getValue()));
				temp1.add((int) data.get(i).getValue());
				if (i == data.size() - 1) {
					windows.add(new DataPointQuality(tempArray, aclQualityCalculation.currentQuality(tempArray)));
				}
			} else {
				windows.add(new DataPointQuality(tempArray, aclQualityCalculation.currentQuality(tempArray)));
				startTime = data.get(i).getTimestamp();
				endTime = data.get(i).getTimestamp() + size;
				tempArray.clear();
				tempArray.add(new DataPoints(data.get(i).getTimestamp(), data.get(i).getValue()));
				if (i == data.size() - 1) {
					windows.add(new DataPointQuality(tempArray, aclQualityCalculation.currentQuality(tempArray)));
				}
				temp1.clear();
			}
		}
	}*/
	
	
	/**
	 * Windowing function for DataPoint arrays
	 *
	 * @param data
	 *            Input data array
	 * @param size
	 *            Time window size in milliseconds
	 * @return ArrayList of data split by size
	 */
	/*public void createECGWindows(List<DataPoints> data, long size) {
		ECGQualityCalculation ecgQualityCalculation = new ECGQualityCalculation();
		long startTime, endTime;
		List<DataPoints> tempArray = new ArrayList<DataPoints>();
		startTime = data.get(0).getTimestamp();
		endTime = data.get(0).getTimestamp() + size;

		List<Integer> temp1 = new ArrayList<Integer>();

		for (int i = 0; i < data.size(); i++) {

			if (data.get(i).getTimestamp() >= startTime && data.get(i).getTimestamp() < endTime) {
				tempArray.add(new DataPoints(data.get(i).getTimestamp(), data.get(i).getValue()));
				temp1.add((int) data.get(i).getValue());
				if (i == data.size() - 1) {
					aclWindows.add(new DataPointQuality(tempArray, ecgQualityCalculation.currentQuality(temp1)));
				}
			} else {
				aclWindows.add(new DataPointQuality(tempArray, ecgQualityCalculation.currentQuality(temp1)));
				startTime = data.get(i).getTimestamp();
				endTime = data.get(i).getTimestamp() + size;
				tempArray.clear();
				tempArray.add(new DataPoints(data.get(i).getTimestamp(), data.get(i).getValue()));
				if (i == data.size() - 1) {
					aclWindows.add(new DataPointQuality(tempArray, ecgQualityCalculation.currentQuality(temp1)));
				}
				temp1.clear();
			}
		}
	}*/

	/**
	 * Creates larger timestamp windows by merging small consecutive windows
	 * 
	 * @param windows timestamp windows 
	 * @param size Time difference between two windows to merge. For example, merge two windows if they are are 1 minute (size=60000) apart.
	 * @return ArrayList<DataPoints> merged windows in larger windows
	 */
	public ArrayList<DataPoints> mergeDataPointsWindows(List<DataPoints> windows, long size) {
		List<Long> temp = new ArrayList<Long>();
		ArrayList<DataPoints> mergedWindows = new ArrayList<DataPoints>();

		if (windows.size() == 1) {
			mergedWindows.add(new DataPoints(windows.get(0).getTimestamp(), windows.get(0).getEndTimestamp()));
		}
		for (int i = 0; i < windows.size() - 1; i++) {

			if (windows.get(i + 1).getTimestamp() - windows.get(i).getEndTimestamp() > size) {

				if (temp.size() == 0) {
					mergedWindows.add(new DataPoints(windows.get(i).getTimestamp(), windows.get(i).getEndTimestamp()));
				} else {
					mergedWindows.add(new DataPoints(temp.get(0), windows.get(i).getEndTimestamp()));
				}

				temp.clear();
			} else {
				temp.add(windows.get(i).getTimestamp());
				if (i == windows.size() - 2) {
					mergedWindows.add(new DataPoints(temp.get(0), windows.get(i + 1).getEndTimestamp()));
				}
			}
		}
		return mergedWindows;

	}
	
	/**
	 * Creates larger timestamp windows by merging small consecutive windows
	 * 
	 * @param windows timestamp windows {@link DataPointQuality} 
	 * @param size Time difference between two windows to merge. For example, merge two windows if they are are 1 minute (size=60000) apart.
	 * @return ArrayList<DataPoints> merged windows in larger windows
	 */
	/*public ArrayList<DataPoints> mergeDataPointQualityWindows(List<DataPointQuality> windows, long size) {
		List<Long> temp = new ArrayList<Long>();
		ArrayList<DataPoints> mergedWindows = new ArrayList<DataPoints>();

		if (windows.size() == 1) {
			mergedWindows.add(new DataPoints(windows.get(0).getDataPoints().get(0).getTimestamp(), windows.get(0).getDataPoints().get(0).getEndTimestamp()));
		}
		for (int i = 0; i < windows.size() - 1; i++) {

			if (windows.get(i + 1).getDataPoints().get(0).getTimestamp() - windows.get(i).getDataPoints().get(0).getEndTimestamp() > size) {

				if (temp.size() == 0) {
					mergedWindows.add(new DataPoints(windows.get(i).getDataPoints().get(0).getTimestamp(), windows.get(i).getDataPoints().get(0).getEndTimestamp()));
				} else {
					mergedWindows.add(new DataPoints(temp.get(0), windows.get(i).getDataPoints().get(0).getEndTimestamp()));
				}

				temp.clear();
			} else {
				temp.add(windows.get(i).getDataPoints().get(0).getTimestamp());
				if (i == windows.size() - 2) {
					mergedWindows.add(new DataPoints(temp.get(0), windows.get(i + 1).getDataPoints().get(0).getEndTimestamp()));
				}
			}
		}
		return mergedWindows;

	}*/
}
