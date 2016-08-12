package org.md2k.cerebralcortex.algorithms;

import java.util.ArrayList;

import org.md2k.cerebralcortex.struct.DataPoints;
import org.md2k.mcerebrum.utils.TimestampSampleVals;
import org.md2k.mcerebrum.utils.UtilFunctions;

/**
 * This algorithm will calculate how many times Wireless disconnection happened.
 * Old: Look at TOS file times to calculate wireless disconnections 
 * New:In the current mobile-framework, if the data is not sent for more than
 * 30 seconds it restarts itself. This could be due to wireless disconnection.
 */
public class WirelessDiscon {

	/**
	 * @param pid Participant id
	 * @param sid Session id
	 * @param timestamps Raw time-stamp values of a stream
	 * @param wearingEpisodesDataPoints Calculated using {@link WearingEpisodes}
	 */
	
	public void getWirelessDisconnAndTOStime(int pid, int sid, ArrayList<Double> timestamp, ArrayList<DataPoints> wearingEpisodesDataPoints){
		TimestampSampleVals timestampSampleVals = new TimestampSampleVals();
		ArrayList<Double> timestampTOSFileTimes = timestampSampleVals.timestampArrayTOSFileTimes();
		
		double disconnTs = 0;
		double connTs = 0;
		double wearingDur =0;
		double disconnDur = 0.0;		
		int disconnCount = 0;
		ArrayList<DataPoints> connDisconn = new ArrayList<DataPoints>();
		ArrayList<Integer> tosFileTimesIndex = new ArrayList<Integer>();
		ArrayList<Double> timestampRangeForTOSFileTimes = new ArrayList<Double>();
		ArrayList<Integer> indexNumbers = new ArrayList<Integer>();
		
		double t1, t2;
		
		for(int i=0; i<wearingEpisodesDataPoints.size(); i++){
			wearingDur = wearingDur + (((wearingEpisodesDataPoints.get(i).getTimestampEnd()-wearingEpisodesDataPoints.get(i).getTimestampStart())/1000/60)/60);
			
			tosFileTimesIndex = UtilFunctions.findIndicesInRange(timestampTOSFileTimes, wearingEpisodesDataPoints.get(i).getTimestampStart(), wearingEpisodesDataPoints.get(i).getTimestampEnd());
			disconnCount=disconnCount+tosFileTimesIndex.size();
			
			timestampRangeForTOSFileTimes = UtilFunctions.findValuesInRange2(timestamp, wearingEpisodesDataPoints.get(i).getTimestampStart(), wearingEpisodesDataPoints.get(i).getTimestampEnd());
			
			if(tosFileTimesIndex.size()>0){
				for(int j=0; j< tosFileTimesIndex.size(); j++){
					indexNumbers = UtilFunctions.findIndices2(timestampRangeForTOSFileTimes, timestampTOSFileTimes.get(tosFileTimesIndex.get(j)));
					if(indexNumbers.size()>0){
						disconnDur = disconnDur+(timestampTOSFileTimes.get(tosFileTimesIndex.get(j))- timestampRangeForTOSFileTimes.get(indexNumbers.get(indexNumbers.size()-1)))/1000/60;
						t1 = timestampTOSFileTimes.get(indexNumbers.get(j));
						t2 = timestampRangeForTOSFileTimes.get(indexNumbers.size()-1);
						System.out.println("=> "+ disconnDur+" ->"+(long)t1+" ->"+(long)t2);
						disconnTs=timestampRangeForTOSFileTimes.get(indexNumbers.size()-1);
						 
						 connTs=timestampTOSFileTimes.get(tosFileTimesIndex.get(j));
						
					}
					//TO-DO
					//put this in data structure class and return too
					//connDisconn=[connDisconn; participant day disconnTs connTs];
					connDisconn.add(new DataPoints(pid, sid, disconnTs, connTs));
					System.out.println("Wireless Disconnections "+  (long)disconnCount +" --> "+ (long)wearingDur +" --> "+ disconnDur);
				}
			}
		}
		System.out.println("Wireless connDisconn "+connDisconn.toString());
	}
}
