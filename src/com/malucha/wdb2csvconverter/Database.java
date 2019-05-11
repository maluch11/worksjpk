package com.malucha.wdb2csvconverter;


import java.util.Map;
import java.util.TreeMap;
@Deprecated
public class Database
{
    private Map headerMap = new TreeMap();
    private Map records = new TreeMap();

    public void addHeader(int fieldId, String label)
    {
        this.headerMap.put(Integer.valueOf(fieldId), label);
    }

    public void addField(int recordId, int fieldId, String value)
    {
        Map record = getRecord(recordId);
        record.put(Integer.valueOf(fieldId), value);
    }

    public Map getRecord(int recordId)
    {
        Map record = (Map)this.records.get(Integer.valueOf(recordId));
        if (record == null)
        {
            record = new TreeMap();
            this.records.put(Integer.valueOf(recordId), record);
        }
        return record;
    }

    public Map getAllRecords()
    {
        return this.records;
    }

    public Map getAllHeaders()
    {
        return this.headerMap;
    }
}