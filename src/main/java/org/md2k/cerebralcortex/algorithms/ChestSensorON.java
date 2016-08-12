package org.md2k.cerebralcortex.algorithms;

import java.util.ArrayList;

import org.apache.commons.lang3.ArrayUtils;
import org.md2k.cerebralcortex.struct.DataPoints;
import org.md2k.mcerebrum.utils.UtilFunctions;

/**
 * This algorithm computes the total time a sensor was on.
 * 
 */
public class ChestSensorON {

	/**
	 * @param pid Participant id
	 * @param sid Session id
	 * @param timestamps Raw time-stamp values of a stream
	 * @param phoneEpisodes {@link DataPoints} Data points (start and end time) of phone-on episodes
	 * @return chestOnEpisodes  {@link DataPoints} Data points (start and end time) of chest sensor episodes
	 */
	//getChestSensorONunderPhoneOnPeriod	
	public ArrayList<DataPoints> getChestSensorONunderPhoneOnPeriod(int pid, int sid,ArrayList<Double> timestamp, ArrayList<DataPoints> phoneEpisodes){
		GoodEpisodes goodEpisodes = new GoodEpisodes();
		
		ArrayList<Double> chestTimestamps = new ArrayList<Double>();
		ArrayList<DataPoints> chestOnEpisodes = new ArrayList<DataPoints>();
		if(timestamp.size()==0){
			//return
		}
		
		for(int i = 0 ; i<phoneEpisodes.size();i++){
			//ind2=find(R.sensor{1}.timestamp>=phoneEpisodesOfTheSubject(i,3) & R.sensor{1}.timestamp<=phoneEpisodesOfTheSubject(i,4));
			//chestTimestamps=[chestTimestamps R.sensor{1}.timestamp(ind2)];
			chestTimestamps.addAll(UtilFunctions.findValuesInRange(timestamp, phoneEpisodes.get(i).getTimestampStart(), phoneEpisodes.get(i).getTimestampEnd()));
			//chestTimestamps = ArrayUtils.addAll(chestTimestamps,chestTimestamps);
		}
		//In matlab code, Mahboob called getEpisodes, (getEpisodes and goodEpisodes are same)
		//chestOnEpisodes=getEpisodes(participant,day,chestTimestamps,10);
		
		chestOnEpisodes = goodEpisodes.getGoodEpisodes(pid, sid, chestTimestamps, 10);
		
		return chestOnEpisodes;
	}
}
