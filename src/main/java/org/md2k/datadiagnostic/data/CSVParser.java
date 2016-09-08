package org.md2k.datadiagnostic.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import org.md2k.datadiagnostic.struct.DataPoints;

public class CSVParser implements Iterable<DataPoints> {

    private final List<DataPoints> data;


    public CSVParser() {
        this.data = new ArrayList<DataPoints>();
    }

    public List<DataPoints> importData(String filename) {

        DataPoints tempPacket;

        String[] tokens;
        double data;
        long timestamp;

        File file = new File(filename);
        Scanner scanner;
        try {
            scanner = new Scanner(file);
            while (scanner.hasNext()) {
                tokens = scanner.nextLine().split(",");
                double ts = Double.parseDouble(tokens[0]);
                timestamp = (long) ts;
                data = Double.parseDouble(tokens[2]);

                tempPacket = new DataPoints(timestamp, data);
                this.data.add(tempPacket);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Collections.sort(this.data);
        return this.data;
    }


    public void sort() {
        Collections.sort(this.data);
    }

    @Override
    public Iterator<DataPoints> iterator() {
        return this.data.iterator();
    }
}