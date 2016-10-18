/**
 * 
 */
package org.md2k.datadiagnostic.main;

import demo.SampleData;

public class main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		new DiagnoseData();

		System.out.println("Diagnostic results are in: "+SampleData.OUTPUT_PATH);
	}
}
