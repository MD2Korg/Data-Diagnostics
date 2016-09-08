/**
 * 
 */
package org.md2k.datadiagnostic.main;


public class main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		String sensorType = "acc_microsoft_band";
		String inputPath = "F:/workspace/memphis/md2k_projects/DataDiagnostics_v1/data/Shahin/data/merged/";
		String outputPath = "F:/workspace/memphis/md2k_projects/DataDiagnostics_v1/data/ali804/output/";
		
		Runner run = new Runner(sensorType, inputPath, outputPath);
		//run.diagnose("rip");
		

	}
}
