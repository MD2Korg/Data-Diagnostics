package org.md2k.datadiagnostic.data;

import java.util.List;

import org.md2k.datadiagnostic.struct.DataPoints;

public class DataLoader {

	/**
	 * Load sensor data data
	 * 
	 * @param csvFileName CSV file name of a sensor. 
	 * @return {@link DataPoints}
	 */
	public List<DataPoints> loadCSV(String csvFileName){
		CSVParser csvParser = new CSVParser();	
		return csvParser.importData(csvFileName);
	}
	
	public List<DataPoints> loadWristCSV(String csvFileName){
		CSVParser csvParser = new CSVParser();	
		return csvParser.importWristData(csvFileName);
	}
}
