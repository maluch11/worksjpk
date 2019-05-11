package com.malucha.jpkstructure3;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

@Deprecated
public class JpkCsvWriter {
    static BufferedWriter bw = null;

    /**
     * Dziala tylko dla wersji JPK 2.0. Dla wersji 3.0 generowany jest bezposrednio XML.
     * @param csvJpkVat
     * @param jpk
     * @param CSV_SPLIT_BY
     */
    @Deprecated
   public static void saveJpkCsvToDisk(String csvJpkVat, JPK jpk, String CSV_SPLIT_BY){
        try {
//           String csvJpkVat = p.plikWyjsciowyProp.getProperty("path") + "worksjpk"
//                    +"_START_"+ jpk.getNaglowek().getDataOdFormated()
//                    +"_STOP_"+ jpk.getNaglowek().getDataDoFormated()
//                    +"__GENER_"+ nowFormated + ".csv";

//            writeAllRegisters JPK to CSV
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csvJpkVat), StandardCharsets.UTF_8));

//            Header0
            // @Deprecated TODOO: 17.12.2017 HEADER SIE NIE ZGADZA!!!! niektore pola usunac:
            String naglowek0 = "KodFormularza;kodSystemowy;wersjaSchemy;WariantFormularza;CelZlozenia;DataWytworzeniaJPK;DataOd;DataDo;NazwaSystemu;NIP;PelnaNazwa;EMAIL;KodKraju;Wojewodztwo;Powiat;Gmina;Ulica;NrDomu;NrLokalu;Miejscowosc;KodPocztowy;Poczta;LpSprzedazy;NrKontrahenta;NazwaKontrahenta;AdresKontrahenta;DowodSprzedazy;DataWystawienia;DataSprzedazy;K_10;K_11;K_12;K_13;K_14;K_15;K_16;K_17;K_18;K_19;K_20;K_21;K_22;K_23;K_24;K_25;K_26;K_27;K_28;K_29;K_30;K_31;K_32;K_33;K_34;K_35;K_36;K_37;K_38;K_39;LiczbaWierszySprzedazy;PodatekNalezny;LpZakupu;NrDostawcy;NazwaDostawcy;AdresDostawcy;DowodZakupu;DataZakupu;DataWplywu;K_43;K_44;K_45;K_46;K_47;K_48;K_49;K_50;LiczbaWierszyZakupow;PodatekNaliczony";
            bw.write(naglowek0);
            bw.newLine();

//            Naglowek1
            String stringsTable[];
            stringsTable = jpk.getNaglowek().getNaglowekCSV();
            writeLineBasedOnStringTable(stringsTable, CSV_SPLIT_BY);
//            IdentyfikatorPodmiotu2
            stringsTable = jpk.getPodmiot1().getIdentyfikatorPodmiotu().getIdentyfikatorPodmiotuCSV();
            writeLineBasedOnStringTable(stringsTable, CSV_SPLIT_BY);
////            AdresPodmiotu3
//            stringsTable = jpk.getPodmiot1().getAdresPodmiotu().getAdresPodmiotuCSV();
//            writeLineBasedOnStringTable(stringsTable, CSV_SPLIT_BY);

//            SprzedazM..N
            Iterator it = jpk.getSprzedaz().iterator();
            while (it.hasNext()) {
                Sprzedaz sprzedazLine = (Sprzedaz) it.next();
                stringsTable = sprzedazLine.getSprzedazCSV();
                writeLineBasedOnStringTable(stringsTable, CSV_SPLIT_BY);
            }
//            SprzedazCtrl
            if(jpk.getSprzedazCtrl().getLiczbaWierszySprzedazy()>0) {
                stringsTable = jpk.getSprzedazCtrl().getSprzedazCtrlCSV();
                writeLineBasedOnStringTable(stringsTable, CSV_SPLIT_BY);
            }

//            ZakupO..P
            Iterator it1 = jpk.getZakup().iterator();
            while (it1.hasNext()) {
                Zakup zakupLine = (Zakup) it1.next();
                stringsTable = zakupLine.getZakupCSV();
                writeLineBasedOnStringTable(stringsTable, CSV_SPLIT_BY);
            }
//            ZakupCtrl
            if(jpk.getZakupCtrl().getLiczbaWierszyZakupu()>0){
                stringsTable = jpk.getZakupCtrl().getZakupCtrlCSV();
                writeLineBasedOnStringTable(stringsTable, CSV_SPLIT_BY);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void writeLineBasedOnStringTable(String[] lineTable, String CSV_SPLIT_BY) throws IOException {
        for (int i = 0; i < lineTable.length; i++) {
            bw.append(lineTable[i]!=null?lineTable[i]:"");
            bw.append(i != lineTable.length-1 ? CSV_SPLIT_BY : "");
        }
        bw.newLine();
    }
}
