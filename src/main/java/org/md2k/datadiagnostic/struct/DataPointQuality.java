package org.md2k.datadiagnostic.struct;

import java.util.ArrayList;
import java.util.List;

import org.md2k.datadiagnostic.configurations.METADATA;

public class DataPointQuality {

	public List<DataPoints> window = new ArrayList<DataPoints>();
	public int metadata;

	/**
	 * DataPointQuality Constructor
	 *
	 * @param datapoints
	 *            Time windows
	 * @param quality
	 *            int point value
	 */
	/*public DataPointQuality(DataPoints[] datapoints, int metadata) {

		this.window = datapoints;
		this.metadata = metadata;
	}*/

	/**
	 * 
	 * @param datapoints {@link DataPoints}
	 * @param metadata {@link Integer} 
	 */
	public DataPointQuality(List<DataPoints> datapoints, int metadata) {

		this.window.addAll(datapoints);
		this.metadata = metadata;
	}
	
	
	/**
	 * 
	 * @return {@link DataPoints}
	 */
	public List<DataPoints> getDataPoints() {
		return window;
	}
	
	

	/**
	 * 
	 * @return {@link Integer} metadata of a window
	 * @see METADATA
	 */
	public int getQuality() {
		return metadata;
	}
	
	/**
	 * 
	 * @param metadata {@link Integer} metadata of a window
	 * @see METADATA
	 */
	public void setQuality(int metadata) {
		this.metadata = metadata;
	}

	@Override
	public String toString() {
		return window.toString() + " " + metadata + "\n";
	}

}
