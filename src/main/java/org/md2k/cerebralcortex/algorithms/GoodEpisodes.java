package org.md2k.cerebralcortex.algorithms;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

import org.md2k.cerebralcortex.struct.DataPoints;
import org.md2k.mcerebrum.utils.UtilFunctions;

/**
 * This algorithm will verify whether there is any time gap between time-stamps. 
 * On each gap, it will store it as a window (start and end time) 
 */
public class GoodEpisodes {
	
	/**
	 * @param pid Participant id
	 * @param sid Session id
	 * @param timestamps Raw time-stamp values of a stream
	 * @param durationThreshold This threshold checks weather there was any longer gap than the threshold in time series data (used threshold is 1 minute) 	
	 */
	 
	
	public ArrayList<DataPoints> getGoodEpisodes(int pid, int sid, ArrayList<Double> timestamps, float durationThreshold){
		ArrayList<DataPoints> dataPoints = new ArrayList<DataPoints>();
		ArrayList<Integer> goodDiffInd = new ArrayList<Integer>();
		
		//Exit if the time-stamp is empty
		if(timestamps.size()==0){
			System.out.println("Empty timestamp. Exiting program.");
			System.exit(0);
		}
		
		
		goodDiffInd = UtilFunctions.findIndices(UtilFunctions.FindDiffs(timestamps), durationThreshold);
		
		if(goodDiffInd.size()==0){
			//Concatenate pid, sid with start and end time from timestamp
			//goodEpisodes=[str2num(pid(2:end)),str2num(sid(2:end)),int64(timestamps(1)),int64(timestamps(end))];
			dataPoints.add(new DataPoints(pid, sid, timestamps.get(0), timestamps.get(timestamps.size()-1)));
		}else{
			for(int i=0; i<goodDiffInd.size(); i++){
				if(i==0){
					dataPoints.add(new DataPoints(pid, sid, timestamps.get(0), timestamps.get(goodDiffInd.get(i))));
					//goodEpisodes=[goodEpisodes;str2num(pid(2:end)),str2num(sid(2:end)),int64(timestamps(1)),int64(timestamps(goodDiffInd(i)))];
				}else if(i==goodDiffInd.size()-1){
					//goodEpisodes=[pid, sid,int64(timestamps(goodDiffInd(i-1)+1)),int64(timestamps(goodDiffInd(i)))];
					dataPoints.add(new DataPoints(pid, sid, timestamps.get(goodDiffInd.get(i-1)+1), timestamps.get(goodDiffInd.get(i))));
					//goodEpisodes=[pid, sid,int64(timestamps(goodDiffInd(i)+1)),int64(timestamps(end))];
					dataPoints.add(new DataPoints(pid, sid, timestamps.get(goodDiffInd.get(i)+1), timestamps.get(timestamps.size()-1)));
					
				}else{
					//goodEpisodes=[pid, sid,int64(timestamps(goodDiffInd(i-1)+1)),int64(timestamps(goodDiffInd(i)))];
					
					dataPoints.add(new DataPoints(pid, sid, timestamps.get(goodDiffInd.get(i-1)+1), timestamps.get(goodDiffInd.get(i))));
				}
			}
		}
		return dataPoints;
	}
	

}
