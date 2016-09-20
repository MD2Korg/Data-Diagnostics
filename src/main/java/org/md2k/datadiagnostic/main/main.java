/**
 * 
 */
package org.md2k.datadiagnostic.main;

import java.text.SimpleDateFormat;
import java.util.Date;

public class main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//System.out.println(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()));
		String sensorType = "respiration";
		String inputPath = "F:/workspace/memphis/md2k_projects/DataDiagnostics_v1/data/Ali09192016/merged/";
		String outputPath = "F:/workspace/memphis/md2k_projects/DataDiagnostics_v1/data/ali804/output/";
		
		new Runner(sensorType, inputPath, outputPath);

	}
}
