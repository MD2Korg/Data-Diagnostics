package org.md2k.datadiagnostic.struct;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataPointQuality {

	public DataPoints[] window;
	public ArrayList<DataPoints> window2 = new ArrayList<DataPoints>();
	public int quality;

	/**
	 * DataPointQuality Constructor
	 *
	 * @param dpA
	 *            Time windows
	 * @param quality
	 *            int point value
	 */
	public DataPointQuality(DataPoints[] dpA, int quality) {

		this.window = dpA;
		this.quality = quality;
	}

	public DataPointQuality(List<DataPoints> dpA, int quality) {

		this.window2.addAll(dpA);
		this.quality = quality;
	}

	public ArrayList<DataPoints> getDataPoints() {
		return window2;
	}

	public int getQuality() {
		return quality;
	}

	@Override
	public String toString() {
		return window2.toString() + "" + quality + "\n";
	}

}
