package org.md2k.mcerebrum.utils;

import java.security.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;

import org.md2k.cerebralcortex.struct.DataPoints;

public class UtilFunctions {

	/**
	 * This method will return indices of values greater than the threshold
	 * 
	 * @param timestamps Raw time-stamp values of a stream
	 * @param durationThreshold 
	 * @return indexNumbers Array of indices. 
	 */
	public static ArrayList<Integer> findIndices(ArrayList<Double> timestamps, double durationThreshold){
		ArrayList<Integer> indexNumbers = new ArrayList<Integer>();
	    
	    for (int i = 0; i < timestamps.size(); i++){
	        if (timestamps.get(i) > durationThreshold){
	        	indexNumbers.add(i);
	        }
	    }
	    return indexNumbers;
	}
	/**
	 * This method will return indices of values less than the threshold
	 * 
	 * @param timestamps Raw time-stamp values of a stream
	 * @param durationThreshold 
	 * @return indexNumbers Array of indices. 
	 */
	public static ArrayList<Integer> findIndices2(ArrayList<Double> timestamps, double durationThreshold){
		ArrayList<Integer> indexNumbers = new ArrayList<Integer>();
	    
	    for (int i = 0; i < timestamps.size(); i++){
	        if (timestamps.get(i) < durationThreshold){
	        	indexNumbers.add(i);
	        }
	    }
	    return indexNumbers;
	}
	
	
	/**
	 * This method will return indicies of values greater than to thresholdEnd
	 * 
	 * @param timestamps Raw time-stamp values of a stream
	 * @param thresholdStart 
	 * @param thresholdEnd
	 * @return
	 */
	public static ArrayList<Double> findValuesInRange(ArrayList<Double> timestamps, double thresholdStart, double thresholdEnd){

		ArrayList<Double> indexValues = new ArrayList<Double>();
	    
	    for (int i = 0; i < timestamps.size(); i++){
	        if (timestamps.get(i) > thresholdStart && timestamps.get(i) < thresholdEnd){
	        	indexValues.add(timestamps.get(i));
	        }
	    }
	    return indexValues;
	    
	}
	
	public static ArrayList<Integer> findIndicesInRange(ArrayList<Double> timestamps, double thresholdStart, double thresholdEnd){

		ArrayList<Integer> indexValues = new ArrayList<Integer>();
	    
	    for (int i = 0; i < timestamps.size(); i++){
	        if (timestamps.get(i) > thresholdStart && timestamps.get(i) < thresholdEnd){
	        	indexValues.add(i);
	        }
	    }
	    return indexValues;
	    
	}
	//this method takes two arraylists to find index of greater/less than values of a threshold
	public static ArrayList<Integer> findIndicesInRangeTwoArr(ArrayList<Double> timestamps1, ArrayList<Double> timestamps2, double thresholdStart, double thresholdEnd){

		ArrayList<Integer> indexValues = new ArrayList<Integer>();
	    
	    for (int i = 0; i < timestamps1.size(); i++){
	        if (timestamps1.get(i) >= thresholdStart && timestamps2.get(i) <= thresholdEnd){
	        	indexValues.add(i);
	        }
	    }
	    return indexValues;
	    
	}
	
	public static ArrayList<Double> findValuesInRange2(ArrayList<Double> timestamps, double thresholdStart, double thresholdEnd){

		ArrayList<Double> indexValues = new ArrayList<Double>();
	    
	    for (int i = 0; i < timestamps.size(); i++){
	        if (timestamps.get(i) >= thresholdStart && timestamps.get(i) <= thresholdEnd){
	        	indexValues.add(timestamps.get(i));
	        }
	    }
	    return indexValues;
	    
	}
	
	//this method is equal the Matlab syntax of 1:5:lengthOfanArray
	public static ArrayList<Integer> getIntervalValues(int arrayLength, int gap){
		int start=0, i=0;
		//int intvValues[] = null;
		ArrayList<Integer> intvValues = new ArrayList<Integer>();
		while(start< arrayLength){
			//intvValues[i] = start;
			intvValues.add(start);
			i++;
			start = start + gap;
		}
		return intvValues;
	}
	
	public static double[] greater(double[] timestamps, double threshold) {
	    Arrays.sort(timestamps);
	    return Arrays.copyOfRange(timestamps, Math.abs(Arrays.binarySearch(timestamps, threshold) + 1), timestamps.length);
	}
	
	/**
	 * This method is equivalent to diff() method in Matlab
	 * @param timestamp Array of timestamps
	 * @return diffTimestamp		This value will contain the diffs of all the a double array
	 * */
	public static ArrayList<Double> FindDiffs(ArrayList<Double> timestamp){
		ArrayList<Double> diffTimestamp = new ArrayList<Double>();
		
		for(int i=0; i<timestamp.size()-1; i++){
			//Convert to minutes
			diffTimestamp.add(((timestamp.get(i+1)/1000)/60) - ((timestamp.get(i)/1000)/60));
			/*if(diffTimestamp[i]>10){
				System.out.println("great val"+diffTimestamp[i]);
			}*/
		}
		return diffTimestamp;
	}
	
	public static ArrayList<Double> getColumnOfDataPoints(ArrayList<DataPoints> someData, String columnName){
		ArrayList<Double> columnData = new ArrayList<Double>();
		
		for(int i=0; i< someData.size(); i++){
			if(columnName.equals("start")){
				columnData.add(someData.get(i).getTimestampStart());
			}else if(columnName.equals("end")){
				columnData.add(someData.get(i).getTimestampEnd());
			}
			
		}
		return columnData;
	}
}
