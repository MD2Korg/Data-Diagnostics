package org.md2k.cerebralcortex.algorithms;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;
import org.md2k.cerebralcortex.struct.DataPoints;
import org.md2k.mcerebrum.utils.UtilFunctions;

/**
 * This algorithm will retrieve all the time-stamp values between the windows created by {@link GoodEpisodes} 
 * For all the retrieved values, it will recompute time-gaps using {@link GoodEpisodes}. The threshold is 10 minute for {@link GoodEpisodes}.
 * All the time gaps between time-stamps must be less than 10.
 */
public class WearingEpisodes {

	/**
	 * This algorithm will verify whether there is any time gap between time-stamps. On each gap, it will store it as a window (start and end time) 
	 * 
	 * @param pid Participant id
	 * @param sid Session id
	 * @param goodEpisodesDataPoints Time-stamp windows computed by {@link GoodEpisodes}
	 * @param imestamps Raw time-stamp values of a stream
	 * @return goodEpisodesDataPoints {@link DataPoints}
	 * NOTE: change method name to getWearingEpisodes	
	 */
	
	public ArrayList<DataPoints> getWearingByMergingGoodEpisodes(int pid, int sid, ArrayList<DataPoints> goodEpisodesDataPoints, ArrayList<Double> timestamps){
		GoodEpisodes goodEpisodes = new GoodEpisodes();
		ArrayList<Double> goodDiffValues = new ArrayList<Double>();

		for(int i = 0; i < goodEpisodesDataPoints.size() ; i++){
			
			goodDiffValues.addAll(UtilFunctions.findValuesInRange(timestamps, goodEpisodesDataPoints.get(i).getTimestampStart(), goodEpisodesDataPoints.get(i).getTimestampEnd()));
			//goodDiffValues = UtilFunctions.findValuesInRange(rawEpisodes, dataPoints.get(i).getTimestampStart(), dataPoints.get(i).getTimestampEnd());
			//goodDiffValues2 = Arrays.copyOf( goodDiffValues, goodDiffValues.length );
			//System.out.println(goodDiffValues.length);
		}
		//Arrays.sort(goodDiffValues);
		goodEpisodesDataPoints = goodEpisodes.getGoodEpisodes(pid, sid, goodDiffValues, 10);
		
		return goodEpisodesDataPoints;
	}
}
