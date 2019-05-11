package com.malucha.deprecated;
@Deprecated
public class CSVReader {
    private static CSVReader ourInstance = new CSVReader();

    public static CSVReader getInstance() {
        return ourInstance;
    }

    private CSVReader() {

    }
}
