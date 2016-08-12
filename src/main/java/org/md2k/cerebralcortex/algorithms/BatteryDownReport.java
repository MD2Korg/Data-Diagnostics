package org.md2k.cerebralcortex.algorithms;

import java.util.ArrayList;

import org.md2k.cerebralcortex.struct.DataPoints;
import org.md2k.mcerebrum.utils.TimestampSampleVals;
import org.md2k.mcerebrum.utils.UtilFunctions;

/**
 * This algorithm calculate the time episodes when battery was down and sensor was off,  
 */


public class BatteryDownReport {

	/**
	 * @param pid Participant id
	 * @param sid Session id
	 * @param timestamps Raw time-stamp values of a stream
	 * @param chestSensorONepisodes Start and End time with time gap in hours. Data structure {@link DataPoints} 	
	 */
	//getBatteryDownReport_v2	
	public void getBatteryDownReport(int pid, int sid, ArrayList<DataPoints> chestSensorONepisodes, ArrayList<Double> batterySampleVals, ArrayList<Double> timestampBattery){
		
		ArrayList<Double> batterySampleValues = new ArrayList<Double>();
		ArrayList<Double> batteryTimestampValues = new ArrayList<Double>();
		ArrayList<DataPoints> batteryDownDur = new ArrayList<DataPoints>();
		ArrayList<DataPoints> batteryDownEpisodes = new ArrayList<DataPoints>();
		ArrayList<Integer> intervalIndx = new ArrayList<Integer>();
		
		ArrayList<Integer> ind2;
		int numIntermittentSensorOff=0;
		int numBatteryDown=0;
		
		//ind=1:5:length(R.sensor{10}.sample);
		//Note: not sure why 5. Because 5 values respresents something look at matlab formated file.
		intervalIndx = UtilFunctions.getIntervalValues(batterySampleVals.size(), 5);
		
		for(int i=0; i<intervalIndx.size(); i++){
			//Note: not sure what is 4096 and *3*2. Mahbub, converting ADC to analog voltage
			batterySampleValues.add(((batterySampleVals.get(intervalIndx.get(i)))/4096)*3*2);
			batteryTimestampValues.add(timestampBattery.get(intervalIndx.get(i)));
			
		}
		
		//nSensorOff=r-1;  %number of intermittent gap between two successive good episodes
		//If there are 4 episodes then it assumes that at least 3 times the sensor was off
		numIntermittentSensorOff = chestSensorONepisodes.size()-1;
		
		for(int j = 0; j< chestSensorONepisodes.size()-1; j++){
			ind2 = UtilFunctions.findIndices2(batteryTimestampValues, chestSensorONepisodes.get(j).getTimestampEnd());
			
			if(ind2.size()>0){
				//cut off for operation is 3 volt
				if(batterySampleValues.get(ind2.size()-1)<=3){
					numBatteryDown = numBatteryDown +1;
					
					//TO-DO
					//batteryDownDur and batteryDownEpisodes are same with reversed order ONLY
					
					 //batteryDownDur=[batteryDownDur;participant day end (sensorONofTheDay(i+1,3)-sensorONofTheDay(i,4))/1000/60];
					batteryDownDur.add(new DataPoints(pid, sid, chestSensorONepisodes.get(j+1).getTimestampStart(), chestSensorONepisodes.get(j).getTimestampEnd()));
			         //batteryDownEpisodes=[batteryDownEpisodes;participant day sensorONofTheDay(i,4) sensorONofTheDay(i+1,3)];					
					batteryDownEpisodes.add(new DataPoints(pid, sid, chestSensorONepisodes.get(j).getTimestampEnd(), chestSensorONepisodes.get(j+1).getTimestampStart()));
				}
			}
		}
		
		System.out.println("nBatteryDown Period" + batteryDownDur.toString());
		
		System.out.println("nSensorOff Period " + numIntermittentSensorOff);
		
		System.out.println("batteryDownDur Period" + batteryDownDur.toString());
		
		System.out.println("batteryDownEpisodes Period" + batteryDownEpisodes.toString());
		
		//System.out.println("intermittantLoss-intermittantBad Period" + intermittantBad.toString());
	}
	
	public void getBatteryDownPeriod(){
		
	}
	
	public void numIntermittentSensorOff(){
		
	}
	
	public void getBatteryDownDur(){
		
	}
	public void getBatteryDownEpisodes(){
		
	}
}
