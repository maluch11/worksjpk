package com.malucha.worksstructure;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class WorksZakupyReader {

    static ArrayList<WorksZakup> worksZakupyList = new ArrayList<>();

    public static ArrayList<WorksZakup> getWorksZakupyList(String csvPath, String csvEncoding, String CSV_SPLIT_BY) {

        BufferedReader br = null;
        String csvLine = "";
        try{
            br = new BufferedReader(new InputStreamReader(new FileInputStream(csvPath), Charset.forName(csvEncoding)));
            while ((csvLine = br.readLine()) != null) {
                /** CSV FILE:
                 * 0 Lp ewid
                 * 1 Rok
                 * 2 Data wystawienia
                 * 3 Dokument
                 * 4 Kontrahent
                 * 5 Adres kontrahenta
                 * 6 NIP
                 * 7 Opis uslugi 1
                 * 8 23 netto
                 * 9 12 netto
                 * 10 8 netto
                 * 11 5 netto
                 * 12 3 netto
                 * 13 0 netto
                 * 14 Zwolnione
                 * 15 Bez odliczenia
                 * 16 Nie podlega
                 * 17 Data wpisu
                 * 18 Rok2
                 * 19 23 vat
                 * 20 23 brutto
                 * 21 12 vat
                 * 22 12 brutto
                 * 23 8 vat
                 * 24 8 brutto
                 * 25 5 vat
                 * 26 5 brutto
                 * 27 3 vat
                 * 28 3 brutto
                 * 29 Razem brutto
                 * 30 Razem netto
                 * 31 Razem vat
                 * 32 Srodki trwale
                 * 33 Korekta
                 * 34 Odwrotne obciazenie
                 */
                String[] s = csvLine.split(CSV_SPLIT_BY);
                worksZakupyList.add(
                        new WorksZakup(
                                Integer.parseInt(s[0].replaceAll("[^0-9]+", "")),
                                Integer.parseInt(s[1].replaceAll("[^0-9]+", "")),
                                s[2].replaceAll("[^0-9]+", ""),
                                s[3].replaceAll("\"",""),
                                s[4],
                                s[5].replaceAll("\"",""),
                                s[6].replaceAll("[\\D]", ""), //NIP
                                s[17].replaceAll("[^0-9]+", ""),
                                new BigDecimal(s[30].replace(",", ".")),
                                new BigDecimal(s[31].replace(",", ".")),
                                s.length <= 32 ? "NIE" : s[32].replace("0","NIE").replace("1","TAK"),
                                s.length <= 33 ? "NIE" : s[33],
                                s.length <= 34 ? "NIE" : s[34],
                                new BigDecimal(s[8].replace(",", ".")),
                                new BigDecimal(s[19].replace(",", ".")),
                                new BigDecimal(s[9].replace(",", ".")),
                                new BigDecimal(s[21].replace(",", ".")),
                                new BigDecimal(s[10].replace(",", ".")),
                                new BigDecimal(s[23].replace(",", ".")),
                                new BigDecimal(s[11].replace(",", ".")),
                                new BigDecimal(s[25].replace(",", ".")),
                                new BigDecimal(s[12].replace(",", ".")),
                                new BigDecimal(s[27].replace(",", ".")),
                                new BigDecimal(s[13].replace(",", ".")),
                                new BigDecimal(s[14].replace(",", ".").replace("\"","")),
                                new BigDecimal(s[15].replace(",", ".").replace("\"","")),
                                new BigDecimal(s[16].replace(",", ".").replace("\"","")),
                                new BigDecimal(s[29].replace(",", ".").replace("\"","")),
                                s[7]
                        )
                );

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return worksZakupyList;
    }
}
