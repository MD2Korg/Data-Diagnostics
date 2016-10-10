package org.md2k.datadiagnostic.marker;

import java.util.ArrayList;
import java.util.List;

import org.md2k.datadiagnostic.configurations.METADATA;
import org.md2k.datadiagnostic.data.DataLoader;
import org.md2k.datadiagnostic.struct.DataPointQuality;
import org.md2k.datadiagnostic.struct.DataPoints;
import org.md2k.datadiagnostic.util.Statistics;

import demo.SampleData;

public class SensorAttachmentMarker {

	public final List<DataPointQuality> markedWindows;

	public SensorAttachmentMarker() {
		markedWindows = new ArrayList<DataPointQuality>();
	}

	public void improperOrNoAttachmentRIP(List<DataPointQuality> windows) {
		DataLoader dataLoader = new DataLoader();

		long startTime = 0, endTime = 0;
		List<Double> tmp = new ArrayList<Double>();
		List<DataPoints> galvanicSkingResponse = new ArrayList<DataPoints>();
		galvanicSkingResponse = dataLoader.loadCSV(SampleData.AUTOSENSE_GSR);
		
		for (int i = 0; i < windows.size(); i++) {
			if (windows.get(i).getQuality() == 999) {

				
				startTime = windows.get(i).getDataPoints().get(0).getTimestamp();
				endTime = windows.get(i).getDataPoints().get(windows.get(i).getDataPoints().size() - 1).getTimestamp();
				for (int j = 0; j < galvanicSkingResponse.size(); j++) {
					if (galvanicSkingResponse.get(j).getTimestamp() > startTime
							&& galvanicSkingResponse.get(j).getTimestamp() < endTime) {
						tmp.add(galvanicSkingResponse.get(j).getValue());
					}
				}
				Statistics statistics = new Statistics(tmp);
				if (statistics.median() < 750) {
					windows.get(i).setQuality(METADATA.IMPROPER_ATTACHMENT);
				}
				if (statistics.median() > 1800) {
					windows.get(i).setQuality(METADATA.SENSOR_OFF_BODY);
				}
			}

			tmp.clear();
		}
		markedWindows.addAll(windows);
	}

	public void improperOrNoAttachmentMotionSense(List<DataPointQuality> windows) {
		List<Double> tmp = new ArrayList<Double>();

		for (int i = 0; i < windows.size(); i++) {
			int badPeaks = 0;
			if (windows.get(i).getQuality() == 999) {

					for (int k = 0; k < windows.get(i).getDataPoints().size(); k++) {
						tmp.add(windows.get(i).getDataPoints().get(k).getValue());
						if (windows.get(i).getDataPoints().get(k).getValue() > 3999
								|| windows.get(i).getDataPoints().get(k).getValue() < 50) {
							badPeaks++;
							tmp.add(windows.get(i).getDataPoints().get(k).getValue());
						}

					}
					double avg = (badPeaks) / windows.get(i).getDataPoints().size();
					if (avg > 0.95) {
						windows.get(i).setQuality(METADATA.SENSOR_OFF_BODY);
					} else {
						Statistics statistics = new Statistics(tmp);
						if (statistics.median() > 3800) {
							windows.get(i).setQuality(METADATA.IMPROPER_ATTACHMENT);
						}

					}


			}
			tmp.clear();

		}
		markedWindows.addAll(windows);
	}

	
	
	public void improperOrNoAttachmentECG(List<DataPointQuality> windows) {
		List<Double> tmp = new ArrayList<Double>();

		for (int i = 0; i < windows.size(); i++) {
			int badPeaks = 0;
			if (windows.get(i).getQuality() == 999) {

					for (int k = 0; k < windows.get(i).getDataPoints().size(); k++) {
						tmp.add(windows.get(i).getDataPoints().get(k).getValue());
						if (windows.get(i).getDataPoints().get(k).getValue() > 3999
								|| windows.get(i).getDataPoints().get(k).getValue() < 50) {
							badPeaks++;
							tmp.add(windows.get(i).getDataPoints().get(k).getValue());
						}

					}
					double avg = (badPeaks) / windows.get(i).getDataPoints().size();
					if (avg > 0.95) {
						windows.get(i).setQuality(METADATA.SENSOR_OFF_BODY);
					} else {
						Statistics statistics = new Statistics(tmp);
						if (statistics.median() > 3800) {
							windows.get(i).setQuality(METADATA.IMPROPER_ATTACHMENT);
						}

					}


			}
			tmp.clear();

		}
		markedWindows.addAll(windows);
	}
}
