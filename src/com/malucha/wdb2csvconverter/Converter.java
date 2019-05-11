package com.malucha.wdb2csvconverter;

import java.io.*;
import java.util.Iterator;
import java.util.Map;

/*  USAGE *******************************
        String filename = "";
        String outputFilename = filename.substring(0, filename.length() - 4) + ".csv";

        boolean dumpBytefile = false;
        boolean dumpDebugfile = false;
        boolean includeRecordIDColumn = false;

        Converter c = new Converter(new File(filename), new File(outputFilename), dumpBytefile, dumpDebugfile, includeRecordIDColumn, ",");
*/
@Deprecated
public class Converter
{
    private int[] buffer = new int[5];
    private int p = 0;
    private Database database = new Database();
    private int recordCount = 0;

    public Converter(File input, File output, boolean dumpBytefile, boolean dumpDebugFile, boolean includeIDColumn, String FIELD_SEPARATOR)
            throws IOException
    {
        byte[] data = readFile(input);

        if (dumpBytefile)
        {
            File byteFile = new File(output.getParentFile(), "bytes.dat");
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(byteFile)));
            for (int i = 0; i < data.length; i++)
            {
                byte b = data[i];
                int v = convertSignedByteToInt(b);

                out.print("" + i + "   ");
                if (isNormalChar(v)) {
                    out.print((char)v);
                } else {
                    out.print("*");
                }
                out.println("   " + convertSignedByteToInt(b));
            }
            out.close();
        }

        if (dumpDebugFile)
        {
            File byteFile = new File(output.getParentFile(), "bytes.html");
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(byteFile)));
            out.println("<html><head><title>" + input.getPath() + "</title></head><body><h1>" + input.getPath() + "</h1><br>");

            String tableStart = "<table border=\"1\" bordercolor=\"#EEEEEE\" cellspacing=\"0\" cellpadding=\"0\">";

            StringBuffer characterString = new StringBuffer();
            StringBuffer positions = new StringBuffer();
            StringBuffer characters = new StringBuffer();
            StringBuffer values = new StringBuffer();
            for (int i = 0; i < data.length; i++)
            {
                byte b = data[i];
                int v = convertSignedByteToInt(b);

                positions.append("<td>" + i + "</td>");
                if (isNormalChar(v))
                {
                    characters.append("<td bgcolor=\"#AAAAFF\">" + (char)v + "</td>");
                    characterString.append((char)v);
                }
                else
                {
                    characters.append("<td bgcolor=\"#DDDDDD\">*</td>");
                    characterString.append("*");
                }
                int value = convertSignedByteToInt(b);
                String colouring = "";
                if (value == 0) {
                    colouring = " bgcolor=\"yellow\"";
                }
                if (value == 15) {
                    colouring = " bgcolor=\"#88FF33\"";
                }
                values.append("<td" + colouring + ">" + value + "</td>");
                if ((i % 100 == 99) || (i == data.length - 1))
                {
                    out.println(characterString.toString());
                    out.println(tableStart);
                    out.println("<tr><td>Positions</td>" + positions.toString() + "</tr>");
                    out.println("<tr><td>Characters</td>" + characters.toString() + "</tr>");
                    out.println("<tr><td>Values</td>" + values.toString() + "</tr>");
                    out.println("</table><br>");

                    positions = new StringBuffer();
                    characters = new StringBuffer();
                    values = new StringBuffer();
                    characterString = new StringBuffer();
                }
            }
            out.println("</body></html>");
            out.close();
        }

        String currentHeaderTitle = null;
        boolean expectingHeaderEnd = false;

        for (int i = 0; i < data.length; i++)
        {
            int v = convertSignedByteToInt(data[i]);
            if (isHeaderStart())
            {
                String header = readString(data, i, -1);

                currentHeaderTitle = header;

                expectingHeaderEnd = true;
            }
            if ((isHeaderEnd()) && (expectingHeaderEnd))
            {
                int headerIndex = data[(i - 4)];

                this.database.addHeader(headerIndex, currentHeaderTitle);
                System.out.println("Header (" + headerIndex + "): " + currentHeaderTitle);

                expectingHeaderEnd = false;
            }
            addToBuffer(v);
        }

        int currentRecordNumber = -1;
        int previousFieldNumber = 0;
        for (int i = 0; i < data.length; i++)
        {
            int v = convertSignedByteToInt(data[i]);

            int bytesRead = 0;
            try
            {

                //JESLI WYKRYTO POLE
                if (isFieldBoundary())
                {
//                    for (int j = i; j < i+11; j++) {
//                        System.err.println("["+j+"] = ["+data[j]+"]");
//                    }

                    int fieldLength = -7 + data[i];
                    if (fieldLength > 0)
                    {
                        int fieldNumber = data[(i + 2)];
                        int recordNumber = convertSignedBytePairToInt(data[(i + 4)], data[(i + 5)]);
                        String field = readString(data, i + 7, fieldLength);

                        bytesRead += 7;

                        System.out.println("" + i + ":" + recordNumber + ":" + fieldNumber + ":" + field + ":" + fieldLength);
                        this.database.addField(recordNumber, fieldNumber, field);
                    }
                }


            }
            catch (ArrayIndexOutOfBoundsException e)
            {
                System.out.println("Warning: passed end of file reading data - perhaps database file is corrupt or truncated?");
            }
            addToBuffer(v);
            i += bytesRead;
        }
        System.out.println("Headers map: " + this.database.getAllHeaders());

//ZAPIS DO PLIKU CSV
        this.recordCount = 0;
        try
        {
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(output)));
//            Zapis header do pliku CSV
            Map headers = this.database.getAllHeaders();
            StringBuffer headerLine = new StringBuffer();
            if (includeIDColumn)
            {
                headerLine.append("Record ID");
                headerLine.append(FIELD_SEPARATOR);
            }


            for (Iterator it = headers.keySet().iterator(); it.hasNext();)
            {
                Integer key = (Integer)it.next();
                String value = (String)headers.get(key);
                headerLine.append(value.trim());
                headerLine.append(FIELD_SEPARATOR);
            }
            out.println(headerLine.toString());

//            Zapis pól do pliku CSV z MapTree
            Map records = this.database.getAllRecords();
            for (Iterator it = records.keySet().iterator(); it.hasNext();)
            {
                Integer key = (Integer)it.next();
                Map record = (Map)records.get(key);

                this.recordCount += 1;
                if (includeIDColumn)
                {
                    out.print(key);
                    out.print(FIELD_SEPARATOR);
                }
                int lastFieldNumber = 0;
                for (Iterator it2 = record.keySet().iterator(); it2.hasNext();)
                {
                    Integer fieldNumber = (Integer)it2.next();
                    String value = (String)record.get(fieldNumber);

                    int commaCount = fieldNumber.intValue() - lastFieldNumber;
                    for (int cc = 0; cc < commaCount; cc++) {
                        out.print(FIELD_SEPARATOR);
                    }
                    out.print(value.trim());

                    lastFieldNumber = fieldNumber.intValue();
                }
                out.println();
            }
            out.close();
        }
        catch (IOException e)
        {
            System.out.println(e);
        }
        System.out.println("Written " + this.recordCount + " records to " + output.getAbsolutePath());
    }


    // @Deprecated CLASS  17.12.2017 PRZEANALIZOWAC TA METODE BYC MOZE ONA JEST BLEDNA
    private String readString(byte[] data, int i, int length)
    {
        if (data[i] != 0) {
            return "";
        }
        i++;

        StringBuffer sb = new StringBuffer();
        while (data[i] == 0) {
            i++;
        }
        int count = 0;
        while ((i < data.length) && (data[i] > 0))
        {
            sb.append((char)data[i]);
            count++;
            i++;
        }
        if ((count != length) && (length != -1)) {
            System.out.println("Warning: string length was " + count + " instead of " + length + " at byte " + (i - count));
        }
        return sb.toString();
    }

    private void printSnippet(byte[] data, int p)
    {
        for (int i = p - 16; i < p + 20; i++)
        {
            int v = data[i];
            if (p == i) {
                System.out.print("<");
            }
            System.out.print(v);
            if (p == i) {
                System.out.print(">");
            }
            System.out.print(" ");
        }
        System.out.println();
    }

    private boolean isHeaderStart()
    {
        String boundary = ",11,0,24,";
        String fromBuffer = getFromBuffer();
        return fromBuffer.endsWith(boundary);
    }

    private boolean isHeaderEnd()
    {
        String boundary = ",127,15,";

        String fromBuffer = getFromBuffer();
        return fromBuffer.endsWith(boundary);
    }

    private boolean isFieldBoundary()
    {
        //String boundary = ",15,0,";
        String boundary = ",15,0,";
        String fromBuffer = getFromBuffer();
        return fromBuffer.endsWith(boundary);
    }

    private String getFromBuffer()
    {
        StringBuffer sb = new StringBuffer();
        sb.append(",");
        for (int i = 0; i < this.buffer.length; i++)
        {
            int actual = this.p + i;
            if (actual >= this.buffer.length) {
                actual -= this.buffer.length;
            }
            sb.append(this.buffer[actual]);
            sb.append(",");
        }
        return sb.toString();
    }

    //5 znakowy bufor
    private void addToBuffer(int v)
    {
        this.buffer[this.p] = v;
        this.p += 1;
        if (this.p >= this.buffer.length) {
            this.p = 0;
        }
    }

/*     Znaki
     65 = A .. 90 = Z
     97 = a .. 122 = z
     48 = 0 .. 57 = 9
     161 = Ą, 177 = ą (czyli + 16)
     163 = Ł
     166 - Ś
     172 = duze ź, 188 = male ź
     175 = Ż,
     198 = duze ć, 230 = male ć
     202 = duze ę, 234 = małe ę
     209 = duze ń, 241 = male ń
     211 = duze ó, 243 = male ó
*/
//public enum ZnakiPL (161,163,166,172,175,198,202,209,211,177,179,182,188,191,230,234,241,243);
    private boolean isNormalChar(int v)
    {
        boolean isNormalPL = false;
        if ((v >= 32) && (v <= 126)) {
            isNormalPL = true;
        }
        if ((v >= 161) && (v <= 243)) {
            isNormalPL = true;
        }
        return isNormalPL;
    }

    private byte[] readFile(File file)
            throws IOException
    {
        //new BufferedReader(new InputStreamReader(new FileInputStream(csvSprzedaz), Charset.forName(csvSprzedazEncoding)));
        InputStream is = new FileInputStream(file);
        //InputStreamReader isr = new InputStreamReader(new FileInputStream(file), Charset.forName("Cp1250"));
        //BufferedReader br = new BufferedReader(isr);

        long length = file.length();
        if (length > 2147483647L) {}
        byte[] bytes = new byte[(int)length];

        int offset = 0;
        int numRead = 0;
        while ((offset < bytes.length) && ((numRead = is.read(bytes, offset, bytes.length - offset)) >= 0)) {
            offset += numRead;
        }
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }
        is.close();
        return bytes;
    }

    public int getRecordCount()
    {
        return this.recordCount;
    }

    private int convertSignedBytePairToInt(byte first, byte second)
    {
        int a = first;
        int b = second;

        int result = b * 256;
        if (a < 0) {
            result += a + 256;
        } else {
            result += a;
        }
        return result;
    }

    private int convertSignedByteToInt(byte b)
    {
        int v = b;
        if (v < 0) {
            v += 128;
        }
        return v;
    }
}
