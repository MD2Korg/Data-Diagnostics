package org.md2k.cerebralcortex.struct;

import java.util.Date;

public class DataPoints {

	private int pid;
	private int sid;
	private double timestampStart;
	private double timestampEnd;
	private double activeEpisodes;

	/*
	 * public DataPoints() {
	 * 
	 * }
	 */

	public DataPoints(int pid, int sid, double timestamps, double timestamps2) {
		this.pid = pid;
		this.sid = sid;
		this.timestampStart = timestamps;
		this.timestampEnd = timestamps2;
	}
	
	public DataPoints(int pid, int sid, double timestamps, double timestamps2, double activeEpisodes) {
		this.pid = pid;
		this.sid = sid;
		this.timestampStart = timestamps;
		this.timestampEnd = timestamps2;
		this.activeEpisodes = activeEpisodes;
	}

	public String toString() {
		Date startDate = new Date((long)this.timestampStart);
		Date endDate = new Date((long)this.timestampEnd);
		
		return "PID: " + this.pid + " SID: " + this.sid + " StartTime: " + startDate + " EndTime: "
				+ endDate+"\n";
	}
	
	public double getTimestampStart(){
		return timestampStart;
	}
	
	public double getTimestampEnd(){
		return timestampEnd;
	}
}
