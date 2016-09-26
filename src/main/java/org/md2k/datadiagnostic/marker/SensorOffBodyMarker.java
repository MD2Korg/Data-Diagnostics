package org.md2k.datadiagnostic.marker;

import java.util.ArrayList;
import java.util.List;

import org.md2k.datadiagnostic.configurations.METADATA;
import org.md2k.datadiagnostic.data.DataLoader;
import org.md2k.datadiagnostic.struct.DataPointQuality;
import org.md2k.datadiagnostic.struct.DataPoints;
import org.md2k.datadiagnostic.util.Statistics;

public class SensorOffBodyMarker {

	public final List<DataPointQuality> improperOrNoAttachment;

	public SensorOffBodyMarker() {
		improperOrNoAttachment = new ArrayList<DataPointQuality>();
	}

	// load data and compute mandated of chest-based accelerometer (x, y, and z)

	public void improperOrNoAttachment(List<DataPointQuality> windows) {
		DataLoader dataLoader = new DataLoader();
		List<DataPoints> galvanicSkingResponse = new ArrayList<DataPoints>();
		long startTime = 0, endTime = 0;
		List<Double> tmp = new ArrayList<Double>();

		galvanicSkingResponse = dataLoader.loadCSV(
				"F:/workspace/memphis/md2k_projects/DataDiagnostics_v1/data/Ali09192016/merged/AUTOSENSE_GALVANIC_SKIN_RESPONSE.csv");

		for (int i = 0; i < windows.size(); i++) {
			if (windows.get(i).getQuality() == 999) {
				startTime = windows.get(i).getDataPoints().get(0).getTimestamp();
				endTime = windows.get(i).getDataPoints().get(windows.get(i).getDataPoints().size()-1).getTimestamp();
				for (int j = 0; j < galvanicSkingResponse.size(); j++) {
					if (galvanicSkingResponse.get(j).getTimestamp() > startTime
							&& galvanicSkingResponse.get(j).getTimestamp() < endTime) {
						tmp.add(galvanicSkingResponse.get(j).getValue());
					}
				}
				//if (tmp.size() != 0) 
				{
					Statistics statistics = new Statistics(tmp);
					if (statistics.median() < 750) {
						windows.get(i).setQuality(METADATA.IMPROPER_ATTACHMENT);
					}
					if (statistics.median() > 1800) {
						windows.get(i).setQuality(METADATA.SENSOR_OFF_BODY);
					}
				}

			}
			tmp.clear();
		}
		improperOrNoAttachment.addAll(windows);
	}
}
