package com.malucha.worksstructure;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * CHANGE: 20181201 - additional fields in CSV file for 'Odwrotne Obciazenie' invoices
 */
public class WorksSprzedazReader {

    static ArrayList<WorksSprzedaz> worksSprzedazList = new ArrayList<>();;

    public static ArrayList<WorksSprzedaz> getWorksSprzedazList(String csvPath, String csvEncoding, String CSV_SPLIT_BY) {

        BufferedReader br = null;
        String csvLine = "";
        try{
            br = new BufferedReader(new InputStreamReader(new FileInputStream(csvPath), Charset.forName(csvEncoding)));
//            int ii = 1;
            while ((csvLine = br.readLine()) != null) {

//                System.err.println(ii+"="+csvLine);
//                ii++;

                /**
                 * 0 Data
                 * 1 Rok
                 * 2 Numer
                 * 3 Nabywca
                 * 4 Adres Nabywcy
                 * 5 Odbiorca
                 * 6 Adres Odbiorcy
                 * 7 NIP
                 * 8 Opis uslugi 1
                 * 9 23 netto
                 * 10 8 netto
                 * 11 Opis uslugi 2
                 * 12 23 netto 2
                 * 13 8 netto 2
                 * 14 Forma platnosci
                 * 15 Slownie
                 * 16 Do zaplaty
                 * 17 Data uslugi
                 * 18 Rok 2
                 * 19 23 cena jednostkowa
                 * 20 23 vat
                 * 21 23 brutto
                 * 22 8 cena jednostkowa
                 * 23 8 vat
                 * 24 8 brutto
                 * 25 23 cena jednostkowa 2
                 * 26 23 vat 2
                 * 27 23 brutto 2
                 * 28 8 cena jednostkowa 2
                 * 29 8 vat 2
                 * 30 8 brutto 2
                 * 31 23 suma netto
                 * 32 23 suma vat
                 * 33 23 suma brutto
                 * 34 8 suma netto
                 * 35 8 suma vat
                 * 36 8 suma brutto
                 * 37 suma netto
                 * 38 suma vat
                 * 39 suma brutto
                 * 40 dataJPK
                 * 41 Odwrotne Obciazenie [null/TAK]
                 * 42 Korygujaca [null/KORYGUJACA do Nr 100/2018]
                 * 43 OO_netto (added: 20181201)
                 * 44 OO_tekst_na_fak (added: 20181201)
                 * 45 OO_netto_wart (added: 20181201)
                 * 46 OO_brutto (added: 20181201)
                 * 47 OO_wtym_netto (added: 20181201)
                 * 48 OO_wtym_brutto (added: 20181201)
                 *
                 * length: 49 (20181201 - change from 43)
                 */
                String[] s = csvLine.split(CSV_SPLIT_BY);


                if(s.length != 0 && !s[2].equals("")) { //ZABEZPIECZENIE PRZED PUSTYMI REKORDAMI I REKORDAMI BEZ NUMERU FAKTURY

//                    System.out.println("sprzedaz kolumn: "+s.length);
//                    System.out.println(s[31]);
//                    System.out.println(removeQuotationSigns(s[45]));
//                    System.out.println(removeQuotationSigns(s[46]));
                    worksSprzedazList.add(
                            new WorksSprzedaz(
                                    Integer.parseInt(s[2].replaceAll("[^0-9]+", "")), //numer faktury SUFIX "/rok" dodawany w wierszu 152 przy konwersji formatu
                                    s[3], //nabywca
                                    s[4], //adresNabywcy
                                    Long.parseLong(s[7].replaceAll("[\\D]", "")), //NIP
                                    new BigDecimal(s[31].replace(",", ".")), //netto23
                                    new BigDecimal(s[32].replace(",", ".")), //vat23
                                    new BigDecimal(s[33].replace(",", ".")), //vat23
                                    new BigDecimal(s[34].replace(",", ".")), //netto8
                                    new BigDecimal(s[35].replace(",", ".")), //vat8
                                    new BigDecimal(s[36].replace(",", ".")), //vat23
                                    LocalDate.parse(s[40].replaceAll("[\\D]", ""), DateTimeFormatter.ofPattern("yyyyMMdd")), //dataJpk sprzedazy
                                    s.length >= 42 ? s[41] : "NIE" ,
                                    s.length >= 43 ? s[42] : "NIE" ,
//                                    s.length >= 49 ? (!s[45].equals("") ? new BigDecimal(s[45].replace(",", ".")) : BigDecimal.ZERO) : BigDecimal.ZERO,
//                                    s.length >= 49 ? (!s[46].equals("") ? new BigDecimal(s[46].replace(",", ".")) : BigDecimal.ZERO) : BigDecimal.ZERO
                                    new BigDecimal(removeQuotationSigns(s[45]).replace(",", ".")),
                                    new BigDecimal(removeQuotationSigns(s[46]).replace(",", "."))
                            )
                    );
//                    if(s.length >= 42) System.err.println(s[2]+" OO[]: ["+s[41]+"]"); else System.err.println("OO: NIE");
//                    if(s.length >= 43) System.err.println(s[2]+" KOR[]: ["+s[42]+"]"); else System.err.println(" KOR: NIE");
                }


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
        return worksSprzedazList;
    }

    private static String removeQuotationSigns(String in){
        return in.trim().replace(" ","").replace(" ","").replace("\"","");
    }
}
