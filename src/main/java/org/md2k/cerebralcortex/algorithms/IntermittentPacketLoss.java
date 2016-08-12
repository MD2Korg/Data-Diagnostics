package org.md2k.cerebralcortex.algorithms;

import java.util.ArrayList;

import org.md2k.cerebralcortex.struct.DataPoints;
import org.md2k.mcerebrum.utils.UtilFunctions;

public class IntermittentPacketLoss {

	//get Intermittent Packet Loss Between Two Good Episodes
	/**
	 * @param pid Participant id
	 * @param sid Session id
	 * @param timestamps Raw time-stamp values of a stream
	 * @param goodEpisodesDataPoints Calculated using {@link GoodEpisodes} Algorithm
	 * @param wearingEpisodes Calculated using {@link WearingEpisodes}
	 * @param connDisconnMatrix Calculated using {@link WirelessDiscon} 	
	 */
	public ArrayList<DataPoints> getIntermittentPacketLoss(int pid, int sid, ArrayList<Double> timestamps, ArrayList<DataPoints> goodEpisodesDataPoints, ArrayList<DataPoints> wearingEpisodes, ArrayList<DataPoints> connDisconnMatrix){
		double startTime, endTime;
		ArrayList<Integer> timestampIndex = new ArrayList<Integer>();
		ArrayList<DataPoints> burstPacketLossDur = new ArrayList<DataPoints>();
		
		int isWirelessDC,nSamples;
		double badDur,expectedSamples;
		for(int i=0; i< wearingEpisodes.size(); i++){
			//Note: not sure about the logic behind these static values (i.e., 3000)
			startTime = wearingEpisodes.get(i).getTimestampStart()-3000;
			endTime = wearingEpisodes.get(i).getTimestampEnd()+3000;
			
			timestampIndex = UtilFunctions.findIndicesInRange(UtilFunctions.getColumnOfDataPoints(goodEpisodesDataPoints, "start"), startTime, endTime);
			
			for(int j=0; j< timestampIndex.size()-1; j++){
				
				isWirelessDC = this.isWirelessDisconn(UtilFunctions.getColumnOfDataPoints(connDisconnMatrix, "start"), UtilFunctions.getColumnOfDataPoints(goodEpisodesDataPoints, "end").get(timestampIndex.get(j)));
				if(isWirelessDC==1){
					continue;
				}
				nSamples = UtilFunctions.findIndicesInRange(timestamps, UtilFunctions.getColumnOfDataPoints(goodEpisodesDataPoints, "end").get(timestampIndex.get(j)), UtilFunctions.getColumnOfDataPoints(goodEpisodesDataPoints, "start").get(timestampIndex.get(j+1))).size();
				
				badDur = (UtilFunctions.getColumnOfDataPoints(goodEpisodesDataPoints, "start").get(timestampIndex.get(j+1))-
						UtilFunctions.getColumnOfDataPoints(goodEpisodesDataPoints, "end").get(timestampIndex.get(j)))/1000;
				
				//System.out.println("##"+(UtilFunctions.getColumnOfDataPoints(goodEpisodesDataPoints, "start").get(timestampIndex.get(j+1))));
				//System.out.println("@@"+UtilFunctions.getColumnOfDataPoints(goodEpisodesDataPoints, "end").get(timestampIndex.get(j)));
				
				//Note: not sure why 21.33. Mahbub: Sampling rate of RIP signal
				expectedSamples = badDur*21.33;
				
				//90% of the samples are missing on that bad episodes, then this bad is due to packet loss
				if(((expectedSamples-nSamples)/expectedSamples)>0.9){
					burstPacketLossDur.add(new DataPoints(pid, sid, UtilFunctions.getColumnOfDataPoints(goodEpisodesDataPoints, "end").get(timestampIndex.get(j)), badDur));
				}
			}
		}
		
		return burstPacketLossDur;
	}
	
	public int isWirelessDisconn(ArrayList<Double> disconnOfTheDay, double timestampThreshold){
		ArrayList<Integer> ind = new ArrayList<Integer>();
		double timeDiff;
		ind = UtilFunctions.findIndices(disconnOfTheDay, timestampThreshold);
		
		if(ind.size()>0){
			timeDiff = (disconnOfTheDay.get(ind.get(0))-timestampThreshold)/1000;
			if(timeDiff<=1){
				return 1;
			}
		}else{
			ind = UtilFunctions.findIndices2(disconnOfTheDay, timestampThreshold);
			if(ind.size()>0){
				timeDiff = (disconnOfTheDay.get(ind.get(0))-timestampThreshold)/1000;
				if(timeDiff<=1){
					return 1;
				}
			}
		}
		return 0;
	}
	

}
