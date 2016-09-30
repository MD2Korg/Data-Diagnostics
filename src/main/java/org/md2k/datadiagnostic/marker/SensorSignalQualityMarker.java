package org.md2k.datadiagnostic.marker;

import java.util.ArrayList;
import java.util.List;

import org.md2k.datadiagnostic.signalquality.algorithms.VarianceBasedDataQuality;
import org.md2k.datadiagnostic.struct.DataPointQuality;

public class SensorSignalQualityMarker {

	public final List<DataPointQuality> markedWindows;

	public SensorSignalQualityMarker() {
		markedWindows = new ArrayList<DataPointQuality>();
	}
	
	/**
	 * This method mark the windows with various quality labels. For example, sensor on body.
	 * 
	 * @param blankWindows {@link DataPointQuality}
	 */
	public void markWindowsQulaity(List<DataPointQuality> blankWindows) {
		VarianceBasedDataQuality varianceBasedDataQuality = new VarianceBasedDataQuality();
		int quality;
		
		for (int i = 0; i < blankWindows.size(); i++) {
			if (blankWindows.get(i).getQuality() == 999) {
				quality = varianceBasedDataQuality.dataQualityMarker(blankWindows.get(i).getDataPoints());
				blankWindows.get(i).setQuality(quality);
			}
		}
		markedWindows.addAll(blankWindows);
	}
}
