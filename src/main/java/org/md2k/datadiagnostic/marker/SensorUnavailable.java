package org.md2k.datadiagnostic.marker;

import java.util.ArrayList;
import java.util.List;

import org.md2k.datadiagnostic.configurations.DDT_PARAMETERS;
import org.md2k.datadiagnostic.configurations.METADATA;
import org.md2k.datadiagnostic.data.CSVExporter;
import org.md2k.datadiagnostic.data.DataLoader;
import org.md2k.datadiagnostic.struct.DataPointQuality;
import org.md2k.datadiagnostic.struct.DataPoints;
import org.md2k.datadiagnostic.util.Statistics;
import org.md2k.datadiagnostic.util.Util;

import demo.SampleData;

/**
 * This class contains algorithms that distinguish various types (e.g., sensor
 * battery down vs. wireless disconnection) of sensor unavailablity
 *
 */
public class SensorUnavailable {

	public final List<DataPointQuality> markedWindows;

	public SensorUnavailable() {
		markedWindows = new ArrayList<DataPointQuality>();
	}

	// load data and compute mandated of chest-based accelerometer (x, y, and z)

	public void autoSenseWirelessDC(List<DataPointQuality> windows) {
		DataLoader dataLoader = new DataLoader();
		List<DataPoints> accelerometerX = new ArrayList<DataPoints>();
		
		
		List<DataPoints> accelerometerY = new ArrayList<DataPoints>();
		List<DataPoints> accelerometerZ = new ArrayList<DataPoints>();

		List<DataPoints> accelerometerMagnitude = new ArrayList<DataPoints>();
		double magnitude;

		accelerometerX = dataLoader.loadCSV(SampleData.AUTOSENSE_ACCELEROMETER_X);
		accelerometerY = dataLoader.loadCSV(SampleData.AUTOSENSE_ACCELEROMETER_Y);
		accelerometerZ = dataLoader.loadCSV(SampleData.AUTOSENSE_ACCELEROMETER_Z);

		int size = Math.max(Math.max(accelerometerX.size(), accelerometerY.size()), accelerometerZ.size());
		// System.out.println(accelerometerX.size() +" - "+
		// accelerometerY.size()+" - "+accelerometerZ.size());
		// System.out.println(size);
		double x, y, z;
		long timestamp = 0;

		for (int i = 0; i < size; i++) {
			if (accelerometerX.size() - 1 < i)
				x = 0;
			else
				x = accelerometerX.get(i).getValue();
			if (accelerometerY.size() - 1 < i)
				y = 0;
			else
				y = accelerometerY.get(i).getValue();
			if (accelerometerZ.size() - 1 < i)
				z = 0;
			else
				z = accelerometerZ.get(i).getValue();

			if (accelerometerX.size() == size)
				timestamp = accelerometerX.get(i).getTimestamp();
			else if (accelerometerY.size() == size)
				timestamp = accelerometerY.get(i).getTimestamp();
			else if (accelerometerZ.size() == size)
				timestamp = accelerometerZ.get(i).getTimestamp();

			magnitude = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
			accelerometerMagnitude.add(new DataPoints(timestamp, magnitude));

		}
		Util.createWindows(accelerometerMagnitude, 10000);
		CSVExporter.writeDataPointsToCSV(accelerometerMagnitude,"F:/workspace/memphis/md2k_projects/DataDiagnostics_v1/data/students/output/", "acce_merg.csv");
		// accelerometerMagnitude.forEach(System.out::println);

		WirelessDC((List<DataPoints>) accelerometerMagnitude, windows);
	}
	
	public void motionsenseWirelessDC(List<DataPointQuality> windows) {
		
	}

	public void WirelessDC(List<DataPoints> accelerometer, List<DataPointQuality> windows) {
		long startDCTime = 0, endDCTime = 0;
		List<Double> tmp = new ArrayList<Double>();
		
		List<Double> tmp2 = new ArrayList<Double>();
		for (int j = 0; j < accelerometer.size(); j++) {
			
				tmp2.add(accelerometer.get(j).getValue());
			
		}
		Statistics statistics1 = new Statistics(tmp2);
		double vari = statistics1.getVariance();
		
		for (int i = 0; i < windows.size(); i++) {
			if (windows.get(i).getQuality() == METADATA.SENSOR_POWERED_OFF) {
				if (windows.get(i - 1).getQuality() != METADATA.SENSOR_POWERED_OFF) {
					// start disconnection time
					startDCTime = windows.get(i).getDataPoints().get(0).getTimestamp();
				}
				if (windows.get(i + 1).getQuality() != METADATA.SENSOR_POWERED_OFF) {
					// end disconnection time
					endDCTime = windows.get(i).getDataPoints().get(0).getTimestamp();
				}

				if (windows.get(i).getQuality() == METADATA.SENSOR_POWERED_OFF
						&& windows.get(i - 1).getQuality() != METADATA.SENSOR_POWERED_OFF) {
					for (int j = 0; j < accelerometer.size(); j++) {
						if (accelerometer.get(j).getTimestamp() <= startDCTime
								&& accelerometer.get(j).getTimestamp() >= (startDCTime - 60000)) {
							tmp.add(accelerometer.get(j).getValue());
						}
					}

					Statistics statistics = new Statistics(tmp);
					System.out.println(startDCTime + " - " + tmp.size() + " - " + statistics.getVariance()+" - "+vari);
					if (statistics.getVariance() > 4000) {
						for (int k = i; k < windows.size() - 1; k++) {
							if (windows.get(k).getQuality() != METADATA.SENSOR_POWERED_OFF) {
								break;
							} else {
								windows.get(k).setQuality(METADATA.SENSOR_UNAVAILABLE);
							}
						}

					}
					tmp.clear();
				}

			}
		}
		markedWindows.addAll(windows);
	}
}
