package org.md2k.datadiagnostic.data;

import java.util.ArrayList;
import java.util.List;

import org.md2k.datadiagnostic.struct.DataPoints;

public class DataLoader {

	public List<DataPoints> dataPoints;
	
	public DataLoader(){
		
	}
	
	public DataLoader(String csvFileName){
		dataPoints = new ArrayList<DataPoints>();
		CSVParser csvParser = new CSVParser();	
		if(csvFileName!="")
		dataPoints = csvParser.importData(csvFileName);
	}
	/**
	 * Load sensor data data
	 * 
	 * @param csvFileName CSV file name of a sensor. 
	 * @return 
	 */
	public List<DataPoints> loadCSV(String csvFileName){
		CSVParser csvParser = new CSVParser();	
		return csvParser.importData(csvFileName);
	}
}
