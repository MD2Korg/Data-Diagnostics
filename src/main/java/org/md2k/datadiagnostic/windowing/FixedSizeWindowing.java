package org.md2k.datadiagnostic.windowing;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

import org.md2k.datadiagnostic.util.*;
import org.md2k.datadiagnostic.configurations.DDT_PARAMETERS;
import org.md2k.datadiagnostic.signalquality.algorithms.*;
import org.md2k.datadiagnostic.struct.*;

/**
 * Create fixed equal size windows of time-series data
 */
public class FixedSizeWindowing {

	
	public List<DataPointQuality> windows;
	public List<DataPointQuality> blankWindows;
	
	public FixedSizeWindowing(){
		windows = new ArrayList<DataPointQuality>();
		blankWindows = new ArrayList<DataPointQuality>();
	}
	
	/**
	 * This method will create n number of blank windows for 24 hours period
	 * and assign data to a window if there is any.
	 * 
	 * @param sensorRawData {@link DataPoints}
	 * @param startTime long start time of a day
	 * @param endTime long end time of a day
	 * @param size long size of a window in milliseconds
	 */
	public void blankWindows(List<DataPoints> sensorRawData, long startTime, long endTime, long size) {
		List<DataPoints> tempArray = new ArrayList<DataPoints>();
		
		long totalMinutes = ((endTime - startTime)/1000)/60;
		Math.round(totalMinutes);
		for(int i=0;i<totalMinutes;i++){
			long windowStartTime=startTime;
			 startTime = startTime+size;
			
			tempArray.add(new DataPoints(windowStartTime, 000)); 
			for (int j = 0; j < sensorRawData.size(); j++) {
				if(sensorRawData.get(j).getTimestamp()>=windowStartTime && sensorRawData.get(j).getTimestamp()<=startTime){
					tempArray.add(new DataPoints(sensorRawData.get(j).getTimestamp(), sensorRawData.get(j).getValue()));
				}
			}
			tempArray.add(new DataPoints(startTime, 000));
			
			 if(!tempArray.isEmpty()){
				 blankWindows.add(new DataPointQuality(tempArray, 999));
			 }
			tempArray.clear();
			
		}
	}

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
	
}
