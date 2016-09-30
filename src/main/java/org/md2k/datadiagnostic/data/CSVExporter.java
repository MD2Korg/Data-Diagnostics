package org.md2k.datadiagnostic.data;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.md2k.datadiagnostic.struct.DataPointQuality;
import org.md2k.datadiagnostic.struct.DataPoints;

public class CSVExporter {


	/**
	 * If no filename is provided then it will generate temp.csv file in output folder
	 * @param windows {@link DataPointQuality}
	 * @param outputFolderPath String
	 * @param fileName String
	 */
	public static void writeDataPointQualityToCSV(List<DataPointQuality> windows, String outputFolderPath, String fileName) {
		
		if(fileName.equals("")){
			fileName = outputFolderPath+"temp.csv";
		}else{
			fileName = outputFolderPath+fileName;
		}
		List<DataPoints> dp = new ArrayList<DataPoints>();
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), "utf-8"))) {
			for (int i = 0; i < windows.size(); i++) {

				dp = windows.get(i).getDataPoints();
				// for(int j=0;j<dp.size();j++)
				{
					writer.write(dp.get(0).getTimestamp() + ", " + dp.get(dp.size()-1).getTimestamp()+", "+windows.get(i).getQuality() + "\r");

				}

			}

		} catch (UnsupportedEncodingException e) {

			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * If no filename is provided then it will generate temp.csv file in output folder
	 * @param windows {@link DataPoints}
	 * @param outputFolderPath String
	 * @param fileName String
	 */
	public static void writeDataPointsToCSV(List<DataPoints> windows, String outputFolderPath, String fileName) {
		if(fileName.equals("")){
			fileName = outputFolderPath+"temp.csv";
		}else{
			fileName = outputFolderPath+fileName;
		}
		List<DataPoints> dp = new ArrayList<DataPoints>();
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), "utf-8"))) {
			for (int i = 0; i < windows.size(); i++) {

				// for(int j=0;j<dp.size();j++)
				{

					/*//human readable time and date
					  Date startTime = new Date(windows.get(i).getTimestamp());
					Date endTime = new Date(windows.get(i).getEndTimestamp());
					
					writer.write( startTime+ ", " + endTime + "\r");*/
					
					writer.write(windows.get(i).getTimestamp() + ", " + windows.get(i).getValue() + "\r");

				}

			}

		} catch (UnsupportedEncodingException e) {

			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
