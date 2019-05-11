package com.malucha;

//import com.malucha.jpkstructure2.*;
import com.malucha.wdb2csvconverter.Converter;
import com.malucha.worksstructure.WorksSprzedazReader;
import com.malucha.worksstructure.WorksSprzedaz;
import com.malucha.worksstructure.WorksZakup;
import com.malucha.worksstructure.WorksZakupyReader;

import java.math.BigDecimal;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

@Deprecated
public class MainNG {
    static List<WorksSprzedaz> worksSprzedazList;
    static List<WorksZakup> worksZakupList;
    static LocalDateTime now;
    static String basePath;
    static PropertyReader p;
    static BufferedWriter bw = null;
    static String csvSprzedaz;
    static String csvSprzedazEncoding;
    static String csvZakupy;
    static String csvZakupyEncoding;
    static String csvJpkVat;
    static final String CSV_SPLIT_BY = ";";
    private static String csvLine = "";
    public static DateTimeFormatter dataFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static DateTimeFormatter czasFormat = DateTimeFormatter.ofPattern("HH:mm:ss");
//    public static DecimalFormat decimalFormat = new DecimalFormat("0.00");

    static String wdbSprzedaz;
    static String wdbSprzedazEncoding;
    static String wdbZakupy;
    static String wdbZakupyEncoding;

    @Deprecated
    public static void main(String[] args) {

        // @Deprecated 23.12.2017 LOCALE SET aby zmienic przecinki w kropki w plikach CSV
//        Locale locale1 = new Locale("pl", "PL", "WIN");
//        // print locale
//        System.out.println("Locale:" + locale1);
//        // set another default locale
//        Locale.setDefault(locale1);

//        KONSOLA - URUCHOMIENIE KONSOLI WYSWIETLAJACEJ KOMUNIKATY PROGRAMU
//        @Deprecated 17.12./**/2017 Wlaczyc konsole w wersji PROD
//        new com.malucha.console.Console(); // comment this line for debugging purposes (this redirects System.out to com.malucha.console.Console

//        DATA I CZAS
        now = LocalDateTime.now(); //       System.out.println("Before : " + now);
        String data = now.format(dataFormat);
        String czas = now.format(DateTimeFormatter.ofPattern("HHmmss"));
        String nowFormated = data + "T" + czas;
        System.out.println("DATA GENERACJI = " + nowFormated);

//        SCIEZKI DO PLIKOW
        basePath = new File("").getAbsolutePath(); //        System.out.println("ABSOLUTE PATH (where to put files) = "+basePath);
        System.out.println("PROGRAM URUCHOMIONY Z LOKALIZACJI = " + basePath);
        p = PropertyReader.getInstance();//        System.out.println("plik wyjsciowy path = "+p.plikWyjsciowyProp.getProperty("path"));
        csvSprzedaz = p.plikiWejscioweProp.getProperty("csv_sprzedaz");//        System.out.println("VAT.csv PAHT = " + csvSprzedaz);
        csvSprzedazEncoding = p.plikiWejscioweProp.getProperty("csv_sprzedaz_encoding");
        csvZakupy = p.plikiWejscioweProp.getProperty("csv_zakupy");//        System.out.println("VAT-ZAKUPY.csv PAHT = " + csvZakupy);
        csvZakupyEncoding = p.plikiWejscioweProp.getProperty("csv_zakupy_encoding");

        wdbSprzedaz = p.plikiWejscioweProp.getProperty("wdb_sprzedaz");//        System.out.println("wdbSprzedaz = "+wdbSprzedaz);
        wdbSprzedazEncoding = p.plikiWejscioweProp.getProperty("wdb_sprzedaz_encoding");
        wdbZakupy = p.plikiWejscioweProp.getProperty("wdb_zakupy");//        System.out.println("wdbSprzedaz = "+wdbZakupy);
        wdbZakupyEncoding = p.plikiWejscioweProp.getProperty("wdb_zakupy_encoding");

//        WCZYTANIE PLIKOW WORKS CSV SPRZEDAZ I ZAKUPY
        worksSprzedazList = new ArrayList<>();
        worksSprzedazList = WorksSprzedazReader.getWorksSprzedazList(csvSprzedaz, csvSprzedazEncoding, CSV_SPLIT_BY);

        worksZakupList = new ArrayList<>();
        worksZakupList = WorksZakupyReader.getWorksZakupyList(csvZakupy, csvZakupyEncoding, CSV_SPLIT_BY);

        /** PRZEMAPOWANIE PLIKÓW JEST W KLASIE JPK, jeśli będzie nowa wersja to najlepiej skopiować cały pakiet i dostosować */
        //STWORZENIE STRUKTURY JPK i PRZEMAPOWANIE DANYCH Z PLIKOW WORKS (na odpowiednia wersje struktury
        String ktoryWariantUzyc = p.getNaglowekDaneProp().getProperty("WariantFormularza");
        if(ktoryWariantUzyc.equals("2")) {

            com.malucha.jpkstructure2.JPK jpk = new com.malucha.jpkstructure2.JPK(worksSprzedazList,worksZakupList);

            // SCIEZKA PLIKU WYJSCIOWEGO pobierana z pliku plik_wyjsciowy.properties
            csvJpkVat = p.plikWyjsciowyProp.getProperty("path") + "worksjpk"
                    +"_JPK_VAT("+ktoryWariantUzyc+")_"
                    +"_START_"+ jpk.getNaglowek().getDataOdFormated()
                    +"_STOP_"+ jpk.getNaglowek().getDataDoFormated()
                    +"__GENER_"+ nowFormated + ".csv";

            // ZAPIS PLIKU JPK.csv o nazwie zdefiniowanej powyżej
            com.malucha.jpkstructure2.JpkCsvWriter.saveJpkCsvToDisk(csvJpkVat, jpk, CSV_SPLIT_BY);

        } else if (ktoryWariantUzyc.equals("3")){

            com.malucha.jpkstructure3.JPK jpk = new com.malucha.jpkstructure3.JPK(worksSprzedazList,worksZakupList);

            // SCIEZKA PLIKU WYJSCIOWEGO pobierana z pliku plik_wyjsciowy.properties
            csvJpkVat = p.plikWyjsciowyProp.getProperty("path") + "worksjpk"
                    +"_JPK_VAT("+ktoryWariantUzyc+")_"
                    +"_START_"+ jpk.getNaglowek().getDataOdFormated()
                    +"_STOP_"+ jpk.getNaglowek().getDataDoFormated()
                    +"__GENER_"+ nowFormated + ".csv";

            // ZAPIS PLIKU JPK.csv o nazwie zdefiniowanej powyżej
            com.malucha.jpkstructure3.JpkCsvWriter.saveJpkCsvToDisk(csvJpkVat, jpk, CSV_SPLIT_BY);

        } else {
            System.err.println("WERSJA JPK NIEZAIMPLEMENTOWANA, skontaktuj się z Michałem");
        }

        // @Deprecated 28.12.2017 TYLKO TEST XML

        MainXML mainXml = new MainXML();
        mainXml.main(null);

    }

    @Deprecated
    private static void convertWDBFilesToCSV(){
        try {
            System.out.println("wdbSprzedaz = "+wdbSprzedaz);
            Converter wdbSprzedazConverter = new Converter(new File(wdbSprzedaz), new File(wdbSprzedaz+".csv"),false,false,false, CSV_SPLIT_BY);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
