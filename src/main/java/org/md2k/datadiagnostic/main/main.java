/**
 * 
 */
package org.md2k.datadiagnostic.main;

public class main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String sensorType = "respiration";
		String inputPath = "F:/workspace/memphis/md2k_projects/DataDiagnostics_v1/data/Ali09192016/merged/";
		String outputPath = "F:/workspace/memphis/md2k_projects/DataDiagnostics_v1/data/ali804/output/";
		
		new Runner(sensorType, inputPath, outputPath);

		System.out.println("Diagnostic results are in: "+outputPath);
	}
}
