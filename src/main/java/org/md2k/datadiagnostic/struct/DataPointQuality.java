package org.md2k.datadiagnostic.struct;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataPointQuality {

	public DataPoints[] window;
	public ArrayList<DataPoints> window2 = new ArrayList<DataPoints>();
	public int metadata;

	/**
	 * DataPointQuality Constructor
	 *
	 * @param dpA
	 *            Time windows
	 * @param quality
	 *            int point value
	 */
	public DataPointQuality(DataPoints[] dpA, int metadata) {

		this.window = dpA;
		this.metadata = metadata;
	}

	public DataPointQuality(List<DataPoints> dpA, int metadata) {

		this.window2.addAll(dpA);
		this.metadata = metadata;
	}

	public ArrayList<DataPoints> getDataPoints() {
		return window2;
	}
	
	

	public int getQuality() {
		return metadata;
	}
	
	public void setQuality(int metadata) {
		this.metadata = metadata;
	}

	@Override
	public String toString() {
		return window2.toString() + "" + metadata + "\n";
	}

}
