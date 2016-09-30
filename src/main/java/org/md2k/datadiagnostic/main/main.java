/**
 * 
 */
package org.md2k.datadiagnostic.main;

public class main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String inputPath = "F:/workspace/memphis/md2k_projects/DataDiagnostics_v1/data/students/azeem/merged/";
		String outputPath = "F:/workspace/memphis/md2k_projects/DataDiagnostics_v1/data/students/output/";
		
		new Runner(inputPath, outputPath);

		System.out.println("Diagnostic results are in: "+outputPath);
	}
}
