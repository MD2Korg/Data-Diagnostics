package org.md2k.mcerebrum.utils;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import org.md2k.cerebralcortex.struct.*;

public class TimestampSampleVals {

	public ArrayList<DataPoints> SampleData(){
		ArrayList<DataPoints> dataPoints = new ArrayList<DataPoints>();
		
		/*dataPoints.add(new DataPoints(105, 01, 1434294100261.50));
		dataPoints.add(new DataPoints(105, 01, 1434294100308.38));
		dataPoints.add(new DataPoints(105, 01, 1434294100355.25));
		dataPoints.add(new DataPoints(105, 01, 1434294100402.13));
		dataPoints.add(new DataPoints(105, 01, 1434294100449.00));
		dataPoints.add(new DataPoints(105, 01, 1434294100495.88));
		dataPoints.add(new DataPoints(105, 01, 1434294100542.75));
		dataPoints.add(new DataPoints(105, 01, 1434294100589.63));
		dataPoints.add(new DataPoints(105, 01, 1434294100636.50));
		dataPoints.add(new DataPoints(105, 01, 1434294100683.38));
		dataPoints.add(new DataPoints(105, 01, 1434294100730.25));
		dataPoints.add(new DataPoints(105, 01, 1434294100777.13));
		dataPoints.add(new DataPoints(105, 01, 1434294100824.00));
		dataPoints.add(new DataPoints(105, 01, 1434294100870.88));
		dataPoints.add(new DataPoints(105, 01, 1434294100917.75));
		dataPoints.add(new DataPoints(105, 01, 1434294100964.63));
		dataPoints.add(new DataPoints(105, 01, 1434294101011.50));
		dataPoints.add(new DataPoints(105, 01, 1434294101058.38));
		dataPoints.add(new DataPoints(105, 01, 1434294101105.25));
		dataPoints.add(new DataPoints(105, 01, 1434294101152.13));
		dataPoints.add(new DataPoints(105, 01, 1434294101199.00));
		dataPoints.add(new DataPoints(105, 01, 1434294101245.88));
		dataPoints.add(new DataPoints(105, 01, 1434294101292.75));
		dataPoints.add(new DataPoints(105, 01, 1434294101339.63));
		dataPoints.add(new DataPoints(105, 01, 1434294101386.50));
		dataPoints.add(new DataPoints(105, 01, 1434294101433.38));
		dataPoints.add(new DataPoints(105, 01, 1434294101480.25));
		dataPoints.add(new DataPoints(105, 01, 1434294101527.13));
		dataPoints.add(new DataPoints(105, 01, 1434294101574.00));
		dataPoints.add(new DataPoints(105, 01, 1434294101620.88));*/
		
		return dataPoints;
	}
	
	public ArrayList<Double> timestampArray(){
		ArrayList<Double> values = new ArrayList<Double>();
		System.out.println("Loading timestamps from a file.");
		
		Scanner scan;
	    File file = new File("F:/workspace/memphis/md2k_projects/DataDiagnostics/data/ali804/filtered/rip.txt");
	    try {
	        scan = new Scanner(file);
	        scan.useDelimiter(", ");
	        int i=0;
	        while(scan.hasNextDouble())
	        {
	        	values.add(scan.nextDouble());
	            //System.out.println( scan.nextDouble() );
	            i++;
	        }

	    } catch (FileNotFoundException e1) {
	            e1.printStackTrace();
	    }
	    Collections.sort(values);
		return values;
	}
	
	public ArrayList<Double> timestampArrayPhone(){
		ArrayList<Double> values = new ArrayList<Double>();
		System.out.println("Loading timestamps from a file.");
		
		Scanner scan;
	    File file = new File("F:/workspace/memphis/md2k_projects/DataDiagnostics/data/ali804/filtered/phone_accel.txt");
	    try {
	        scan = new Scanner(file);
	        scan.useDelimiter(", ");
	        int i=0;
	        while(scan.hasNextDouble())
	        {
	        	values.add(scan.nextDouble());
	            //System.out.println( scan.nextDouble() );
	            i++;
	        }

	    } catch (FileNotFoundException e1) {
	            e1.printStackTrace();
	    }
		
		return values;
	}
	
	public ArrayList<Double> timestampArrayTOSFileTimes(){
		ArrayList<Double> values = new ArrayList<Double>();
		System.out.println("Loading timestamps from a file.");
		
		Scanner scan;
	    File file = new File("F:/workspace/memphis/md2k_projects/DataDiagnostics/data/ali804/filtered/tosfiletime.txt");
	    try {
	        scan = new Scanner(file);
	        scan.useDelimiter(", ");
	        int i=0;
	        while(scan.hasNextDouble())
	        {
	        	values.add(scan.nextDouble());
	            //System.out.println( scan.nextDouble() );
	            i++;
	        }

	    } catch (FileNotFoundException e1) {
	            e1.printStackTrace();
	    }
		
		return values;
	}
	
	public ArrayList<Double> timestampArrayBattery(){
		ArrayList<Double> values = new ArrayList<Double>();
		System.out.println("Loading timestamps from a file.");
		
		Scanner scan;
	    File file = new File("F:/workspace/memphis/md2k_projects/DataDiagnostics/data/ali804/filtered/battery.txt");
	    try {
	        scan = new Scanner(file);
	        scan.useDelimiter(", ");
	        int i=0;
	        while(scan.hasNextDouble())
	        {
	        	values.add(scan.nextDouble());
	            //System.out.println( scan.nextDouble() );
	            i++;
	        }

	    } catch (FileNotFoundException e1) {
	            e1.printStackTrace();
	    }
		
		return values;
	}
	
	public ArrayList<Double> sampleArrayBattery(){
		ArrayList<Double> values = new ArrayList<Double>();
		System.out.println("Loading timestamps from a file.");
		
		Scanner scan;
	    File file = new File("F:/workspace/memphis/md2k_projects/DataDiagnostics/data/ali804/filtered/battery_samples.txt");
	    try {
	        scan = new Scanner(file);
	        scan.useDelimiter(", ");
	        int i=0;
	        while(scan.hasNextDouble())
	        {
	        	values.add(scan.nextDouble());
	            //System.out.println( scan.nextDouble() );
	            i++;
	        }

	    } catch (FileNotFoundException e1) {
	            e1.printStackTrace();
	    }
		
		return values;
	}
	
	
	
	
	
	
}
