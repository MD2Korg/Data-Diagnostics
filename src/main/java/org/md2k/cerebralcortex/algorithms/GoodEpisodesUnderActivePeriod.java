package org.md2k.cerebralcortex.algorithms;

import java.util.ArrayList;

import org.apache.commons.lang3.ArrayUtils;
import org.md2k.cerebralcortex.struct.DataPoints;
import org.md2k.mcerebrum.utils.TimestampSampleVals;
import org.md2k.mcerebrum.utils.UtilFunctions;

/**
 * This algorithm calculates good episodes under active chest-sensor on episodes with a threshold.  
 */

public class GoodEpisodesUnderActivePeriod {

	/**
	 * @param pid Participant id
	 * @param sid Session id
	 * @param timestamps Raw time-stamp values of a stream
	 * @param activeSensorONepisodes Active chest sensor on episodes
	 * @param durationThreshold This threshold checks weather there was any longer gap than the threshold in time series data (used threshold is 1 minute) 	
	 */
	public ArrayList<DataPoints> getGoodEpisodesUnderActivePeriod(int pid, int sid, ArrayList<Double> timestamp,
			ArrayList<DataPoints> activeSensorONepisodes, int durationThreshold) {
		
		GoodEpisodes goodEpisodes = new GoodEpisodes();

		ArrayList<Double> activeTimestamp = new ArrayList<Double>();
		ArrayList<DataPoints> goodEpisodesUnderActivePeriod = new ArrayList<DataPoints>();
		if (timestamp.size() == 0) {
			// return
		}

		for (int i = 0; i < activeSensorONepisodes.size(); i++) {
			activeTimestamp.addAll(
					UtilFunctions.findValuesInRange2(timestamp, activeSensorONepisodes.get(i).getTimestampStart(),
							activeSensorONepisodes.get(i).getTimestampEnd()));
			// activeTimestamp =
			// ArrayUtils.addAll(activeTimestamp,activeTimestamp);
		}

		goodEpisodesUnderActivePeriod = goodEpisodes.getGoodEpisodes(pid, sid, activeTimestamp, durationThreshold);

		return goodEpisodesUnderActivePeriod;
	}
}
