package org.md2k.cerebralcortex.algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;

import org.md2k.cerebralcortex.struct.DataPoints;
import org.md2k.mcerebrum.utils.TimestampSampleVals;
import org.md2k.mcerebrum.utils.UtilFunctions;

/**
 * This algorithm combines time-stamp values from chest-band and phone to calculate phone-on episodes.
 * 
 */
public class PhoneONepisodes {

	/**
	 * @param pid Participant id
	 * @param sid Session id
	 * @param timestamps Raw time-stamp values of a stream
	 * @param activePeriodEpisodes Start and End time with time gap in hours. Data structure {@link DataPoints}
	 * @return phoneONepisodes {@link DataPoints} Data points (start and end time) of phone-on episodes
	 */
	//getPhoneONepisodesByMergingPhoneAndChestSensorData
	public ArrayList<DataPoints> getPhoneONepisodesByMergingPhoneAndChestSensorData(int pid,int sid, ArrayList<Double> timestamp,ArrayList<DataPoints> activePeriodEpisodes){
		TimestampSampleVals timestampSampleVals = new TimestampSampleVals();
		ArrayList<Double> timestampPhone = timestampSampleVals.timestampArrayPhone();
		ArrayList<Double> allTimestamps = new ArrayList<Double>();
		ArrayList<Double> activeTimestamps = new ArrayList<Double>();
		ArrayList<DataPoints> phoneONepisodes;
		GoodEpisodes goodEpisodes = new GoodEpisodes();
		
		allTimestamps.addAll(timestamp);
		allTimestamps.addAll(timestampPhone);
		
		//SORT AND REMOVE DUPLICATES
		allTimestamps = new ArrayList<Double>(new LinkedHashSet<Double>(allTimestamps));
		Collections.sort(allTimestamps);
		//allTimestamps=unique(allTimestamps);
		
		//remove duplicates
		allTimestamps.stream().distinct().collect(Collectors.toList());
		
		if(activePeriodEpisodes.size()==0){
			//return
		}else{
			for(int i=0; i<activePeriodEpisodes.size();i++){
				//ind2=find(allTimestamps>=activePeriodOfTheDay(i,3) & allTimestamps<=activePeriodOfTheDay(i,4));
				activeTimestamps.addAll(UtilFunctions.findValuesInRange(allTimestamps, activePeriodEpisodes.get(i).getTimestampStart(), activePeriodEpisodes.get(i).getTimestampEnd()));
				 //activeTimestamps=[activeTimestamps allTimestamps(ind2)];
				//activeTimestamps = ArrayUtils.addAll(activeTimestamps,activeTimestamps);
			   
			}
		}
		
		//phoneONepisodes=getGoodEpisodes(pid,sid,activeTimestamps,10);  
		//each wearing epsidoes are assumed to be apart by 10 minutes
		
		phoneONepisodes = goodEpisodes.getGoodEpisodes(pid, sid, activeTimestamps, 10);
		
		return phoneONepisodes;
	}
}
