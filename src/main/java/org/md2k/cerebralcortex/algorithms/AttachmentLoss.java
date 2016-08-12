package org.md2k.cerebralcortex.algorithms;

import java.util.ArrayList;

import org.md2k.cerebralcortex.struct.DataPoints;
import org.md2k.mcerebrum.utils.UtilFunctions;

//calculates loss at the start and end, intermittant loss, and entire episode lost find how much is for the wireless loss, how much is for the degradation
public class AttachmentLoss {

	public void getAttachmentLoss(ArrayList<DataPoints> wearingEpisodes, ArrayList<DataPoints> goodEpisodes){
		double startTime, endTime, startDiff;
		ArrayList<Integer> goodEpiIndx = new ArrayList<Integer>();
		ArrayList<DataPoints> fullEpisodeBad = new ArrayList<DataPoints>();
		ArrayList<DataPoints> badAtStart = new ArrayList<DataPoints>();
		ArrayList<DataPoints> intermittantBad = new ArrayList<DataPoints>();
		ArrayList<DataPoints> badAtEnd = new ArrayList<DataPoints>();
		
		for(int i=0; i<wearingEpisodes.size(); i++){
			
			//Note: not sure the usage of 30*1000. Mahbub, this could be ignored. Just  incase!
			startTime = (wearingEpisodes.get(i).getTimestampStart())-30*1000;
			endTime = (wearingEpisodes.get(i).getTimestampEnd())+30*1000;
			
			goodEpiIndx = UtilFunctions.findIndicesInRangeTwoArr(UtilFunctions.getColumnOfDataPoints(goodEpisodes, "start"),UtilFunctions.getColumnOfDataPoints(goodEpisodes, "end"), startTime, endTime);	
			
			if(goodEpiIndx.size()==0){
				fullEpisodeBad.add(new DataPoints(105, 1, wearingEpisodes.get(i).getTimestampStart(), wearingEpisodes.get(i).getTimestampEnd()));
				continue;
			}
			
			startDiff = ((UtilFunctions.getColumnOfDataPoints(goodEpisodes, "start").get(goodEpiIndx.get(0))-
					UtilFunctions.getColumnOfDataPoints(wearingEpisodes, "start").get(i))/1000)/60;
			//BUG!! I don't know why in Matlab, startDiff is zero even when it shouldn't be
			if(startDiff>=0){
				badAtStart.add(new DataPoints(105, 1, wearingEpisodes.get(i).getTimestampStart(), goodEpisodes.get(goodEpiIndx.get(0)).getTimestampStart()));
			}
			if(goodEpiIndx.size()>1){
				for(int j=0; j<goodEpiIndx.size()-1;j++){
					intermittantBad.add(new DataPoints(105, 1, goodEpisodes.get(goodEpiIndx.get(j)).getTimestampEnd(), goodEpisodes.get(goodEpiIndx.get(j+1)).getTimestampStart()));
					
				}
			}
			
			badAtEnd.add(new DataPoints(105, 1, goodEpisodes.get(goodEpiIndx.get(goodEpiIndx.size()-1)).getTimestampEnd(), wearingEpisodes.get(i).getTimestampEnd()));
		}
		System.out.println("improperAttachmentRIP-fullEpisodeBad Period" + fullEpisodeBad.toString());
		
		System.out.println("delayInAttachmentRIP-badAtStart Period" + badAtStart.toString());
		
		System.out.println("intermittantLoss-intermittantBad Period" + intermittantBad.toString());
		
		System.out.println("lossAtEndRIP-intermittantBad Period" + badAtEnd.toString());
	}
}
