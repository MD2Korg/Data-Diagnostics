package org.md2k.cerebralcortex.algorithms;

import java.util.ArrayList;
import java.util.Collections;

import org.md2k.cerebralcortex.struct.DataPoints;
import org.md2k.mcerebrum.utils.UtilFunctions;

/**
 * This algorithm takes the input of wearingEpisodes computed by {@link WearingEpisodes} and calculate time gap for each wearingEpisode in hours. 
 * This will only return active periods. Read comments below in code for inactive period.
 */
public class ActiveInactiveEpisodes {

	/**
	 * @param pid Participant id
	 * @param sid Session id
	 * @param wearingTimes Wearing episodes computed by {@link WearingEpisodes}
	 * @return activeEpisodes Start and End time with time gap in hours {@link DataPoints}
	 */
	public ArrayList<DataPoints> getActiveInactiveEpisodesFromWearingEpisodes(int pid,int sid,ArrayList<DataPoints> wearingTimes){
		ArrayList<Double> timeGap = new ArrayList<Double>();
		ArrayList<DataPoints> activeEpisodes = new ArrayList<DataPoints>();
		ArrayList<Integer> ind3 = new ArrayList<Integer>();
		
		if(wearingTimes.size()<1){
			System.out.println("No wearing time episode(s)");
			System.exit(1);
		}else if(wearingTimes.size()==1){
			//activeEpisodes=[participant day wearingOfTheDay2(1,3) wearingOfTheDay2(1,4) (wearingOfTheDay2(1,4)-wearingOfTheDay2(1,3))/1000/60/60];
			activeEpisodes.add(new DataPoints(pid, sid, wearingTimes.get(0).getTimestampStart(), wearingTimes.get(0).getTimestampEnd(), (((wearingTimes.get(0).getTimestampEnd()-wearingTimes.get(0).getTimestampStart())/1000)/60)/60));
		}if(wearingTimes.size()>1){
			for(int i=0; i< wearingTimes.size()-1; i++){
				//timeGap=[timeGap; (wearingOfTheDay2(i+1,3)-wearingOfTheDay2(i,4))/1000/60/60];
				//These timeGaps are inactive periods
				timeGap.add((((wearingTimes.get(i+1).getTimestampStart()-wearingTimes.get(i).getTimestampEnd())/1000)/60)/60);
			}
			
			ind3 = UtilFunctions.findIndices(timeGap, Collections.max(timeGap));
			//inactive hours are at least 5 hours (Threshold)
			if(Collections.max(timeGap)>=5){
				for(int j =0; j<ind3.size(); j++){
					//activeEpisodes=[participant day wearingOfTheDay2(1,3) wearingOfTheDay2(ind3,4) (wearingOfTheDay2(ind3,4)-wearingOfTheDay2(1,3))/1000/60/60];
					activeEpisodes.add(new DataPoints(pid, sid, wearingTimes.get(0).getTimestampStart(), wearingTimes.get(j).getTimestampEnd(), (((wearingTimes.get(j).getTimestampEnd()-wearingTimes.get(0).getTimestampStart())/1000)/60)/60));
					
					//activeEpisodes=[activeEpisodes;participant day wearingOfTheDay2(ind3+1,3) wearingOfTheDay2(end,4) (wearingOfTheDay2(end,4)-wearingOfTheDay2(ind3+1,3))/1000/60/60];
					activeEpisodes.add(new DataPoints(pid, sid, wearingTimes.get(j+1).getTimestampStart(), wearingTimes.get(ind3.size()-1).getTimestampEnd(), (((wearingTimes.get(ind3.size()-1).getTimestampEnd()-wearingTimes.get(j+1).getTimestampStart())/1000)/60)/60));
				}
			}else{
				//otherwise the whole day is considered as active
					//activeEpisodes=[participant day wearingOfTheDay2(1,3) wearingOfTheDay2(end,4) (wearingOfTheDay2(end,4)-wearingOfTheDay2(1,3))/1000/60/60];  
					activeEpisodes.add(new DataPoints(pid, sid, wearingTimes.get(0).getTimestampStart(), wearingTimes.get(wearingTimes.size()-1).getTimestampEnd(), (((wearingTimes.get(wearingTimes.size()-1).getTimestampEnd()-wearingTimes.get(0).getTimestampStart())/1000)/60)/60));
			}
		}
		return activeEpisodes;
		
	}
	
	public void getActivePeriods(){
		
	}
	
	public void getInactivePeriods(){
		
	}
}