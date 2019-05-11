package com.malucha;

import com.malucha.wdb2csvconverter.Converter;
import com.malucha.worksstructure.WorksSprzedaz;
import com.malucha.worksstructure.WorksSprzedazReader;
import com.malucha.worksstructure.WorksZakup;
import com.malucha.worksstructure.WorksZakupyReader;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.OptionalDouble;

public class MainXML {
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
    public static LocalDate automatycznaDataOd;
    public static LocalDate automatycznaDataDo;

    static String wdbSprzedaz;
    static String wdbSprzedazEncoding;
    static String wdbZakupy;
    static String wdbZakupyEncoding;

    static String xmlJpkVat;

    /** worksjpk.jar generuje sie w folderze TEST projektu intelliJ **/
    public static void main(String[] args) {
        /** Zmiana przecinkow na kropki w plikach CSV **/
        Locale locale1 = new Locale("pl", "PL", "WIN");
        // print locale
        System.out.println("Locale:" + locale1);
        // set another default locale
        Locale.setDefault(locale1);

//        KONSOLA - URUCHOMIENIE KONSOLI WYSWIETLAJACEJ KOMUNIKATY PROGRAMU
//        TODO: 17.12./**/2017 Wlaczyc konsole w wersji PROD
        new com.malucha.console.Console(); // comment this line for debugging purposes (this redirects System.out to com.malucha.console.Console

//        DATA I CZAS
        now = LocalDateTime.now(); //       System.out.println("Before : " + now);
        String data = now.format(dataFormat);
        String czas = now.format(DateTimeFormatter.ofPattern("HHmmss"));
        String nowFormated = data + "T" + czas;
        String nowDateOnlyFormated = data;
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

//        USTAWIENIE AUTOMATYCZNYCH DAT OD I DO
        automatycznaDataOd = p.naglowekDaneProp.getProperty("CzyDatyAutomatyczne").equals("TAK")
                ? LocalDate.now().minusMonths(1).withDayOfMonth(1)
                : LocalDate.parse(p.naglowekDaneProp.getProperty("DataOd"), dataFormat)
        ;
        automatycznaDataDo = p.naglowekDaneProp.getProperty("CzyDatyAutomatyczne").equals("TAK")
                ? LocalDate.now().minusMonths(1).withDayOfMonth(LocalDate.now().minusMonths(1).lengthOfMonth())
                : LocalDate.parse(p.naglowekDaneProp.getProperty("DataDo"), dataFormat)
        ;


        /** PRZEMAPOWANIE PLIKÓW JEST W KLASIE JPK, jeśli będzie nowa wersja to najlepiej skopiować cały pakiet i dostosować */
        //STWORZENIE STRUKTURY JPK i PRZEMAPOWANIE DANYCH Z PLIKOW WORKS (na odpowiednia wersje struktury
        String ktoryWariantUzyc = p.getNaglowekDaneProp().getProperty("WariantFormularza");
        if(ktoryWariantUzyc.equals("2")) {

            /** CSV - not needed anymore **/
//            com.malucha.jpkstructure2.JPK jpk = new com.malucha.jpkstructure2.JPK(worksSprzedazList,worksZakupList);
//
//            // SCIEZKA PLIKU WYJSCIOWEGO pobierana z pliku plik_wyjsciowy.properties
//            csvJpkVat = p.plikWyjsciowyProp.getProperty("path") + "worksjpk"
//                    +"_JPK_VAT("+ktoryWariantUzyc+")_"
//                    +"_START_"+ jpk.getNaglowek().getDataOdFormated()
//                    +"_STOP_"+ jpk.getNaglowek().getDataDoFormated()
//                    +"__GENER_"+ nowFormated + ".csv";
//
//            // ZAPIS PLIKU JPK.csv o nazwie zdefiniowanej powyżej
//            com.malucha.jpkstructure2.JpkCsvWriter.saveJpkCsvToDisk(csvJpkVat, jpk, CSV_SPLIT_BY);

            com.malucha.jpkstructure2.xml.JpkMapper jpkMapper = new com.malucha.jpkstructure2.xml.JpkMapper(worksSprzedazList,worksZakupList);
            com.malucha.jpkstructure2.xml.JPK jpk = jpkMapper.getJpk();
            xmlJpkVat = p.plikWyjsciowyProp.getProperty("path") + "worksjpk"
                    +"_JPK-"+ktoryWariantUzyc+"_"
                    +"_M-"+jpk.getNaglowek().getDataDo().getMonth()+"-"+jpk.getNaglowek().getDataDo().getYear()+"_CEL-"+jpk.getNaglowek().getCelZlozenia()
                    +"_GEN-"+ nowDateOnlyFormated + ".xml";
            Marshaller marshaller = jpkMapper.getXmlStructureFromContext();

            try {
                marshaller.marshal(jpk, new FileOutputStream(xmlJpkVat));
            } catch (JAXBException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            /**
             * OUTPUT:
             Kod Formularza = JPK_VAT
             Kod Systemowy i Wersja Schemy = JPK_VAT (3) 1-1
             Cel Zlozenia = 0
             Data START = 2017-11-01
             Data STOP = 2017-11-30

             Sprzedaz liczba wierszy = 52
             Sprzedaz podatek nalezny = 5802.42
             Zakup liczba wierszy = 25
             Zakup podatek nalezny = 0
             DATA GENERACJI = 2018-01-14T150609
             */

            System.out.println("Kod Formularza = "+jpk.getNaglowek().getKodFormularza().getValue());
            System.out.println("Kod Systemowy i Wersja Schemy = "+jpk.getNaglowek().getKodFormularza().getKodSystemowy()+" "+jpk.getNaglowek().getKodFormularza().getWersjaSchemy());
            System.out.println("Cel zlozenia = "+jpk.getNaglowek().getCelZlozenia());
            System.out.println("Data START = "+jpk.getNaglowek().getDataOd().toString());
            System.out.println("Data STOP = "+jpk.getNaglowek().getDataDo().toString());

            if(jpk.getSprzedazCtrl() != null) {
                System.out.println("Sprzedaz liczba wierszy = " + jpk.getSprzedazCtrl().getLiczbaWierszySprzedazy());
                System.out.println("Sprzedaz podatek nalezny = " + jpk.getSprzedazCtrl().getPodatekNalezny());
            }
            if(jpk.getZakupCtrl() != null) {
                System.out.println("Zakup liczba wierszy = " + jpk.getZakupCtrl().getLiczbaWierszyZakupow());
                System.out.println("Zakup podatek naliczony = " + jpk.getZakupCtrl().getPodatekNaliczony());
            }
            System.out.println("PLIK WYJSCIOWY = "+xmlJpkVat);

        } else if (ktoryWariantUzyc.equals("3")){

            com.malucha.jpkstructure3.xml.JpkMapper jpkMapper = new com.malucha.jpkstructure3.xml.JpkMapper(worksSprzedazList,worksZakupList);
            com.malucha.jpkstructure3.xml.JPK jpk = jpkMapper.getJpk();

            // SCIEZKA PLIKU WYJSCIOWEGO pobierana z pliku plik_wyjsciowy.properties
            xmlJpkVat = p.plikWyjsciowyProp.getProperty("path") + "worksjpk"
                    +"_JPK-"+ktoryWariantUzyc+"_"
                    +"_M-"+jpk.getNaglowek().getDataDo().getMonth()+"-"+jpk.getNaglowek().getDataDo().getYear()+"_CEL-"+jpk.getNaglowek().getCelZlozenia()
                    +"_GEN-"+ nowDateOnlyFormated + ".xml";

//            // ZAPIS PLIKU JPK.xml o nazwie zdefiniowanej powyżej

            Marshaller marshaller = jpkMapper.getXmlStructureFromContext();

            try {
                marshaller.marshal(jpk, new FileOutputStream(xmlJpkVat));
            } catch (JAXBException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            /**
             * OUTPUT:
             Kod Formularza = JPK_VAT
             Kod Systemowy i Wersja Schemy = JPK_VAT (3) 1-1
             Cel Zlozenia = 0
             Data START = 2017-11-01
             Data STOP = 2017-11-30

             Sprzedaz liczba wierszy = 52
             Sprzedaz podatek nalezny = 5802.42
             Zakup liczba wierszy = 25
             Zakup podatek nalezny = 0
             DATA GENERACJI = 2018-01-14T150609
             */

            System.out.println("Kod Formularza = "+jpk.getNaglowek().getKodFormularza().getValue());
            System.out.println("Kod Systemowy i Wersja Schemy = "+jpk.getNaglowek().getKodFormularza().getKodSystemowy()+" "+jpk.getNaglowek().getKodFormularza().getWersjaSchemy());
            System.out.println("Cel zlozenia = "+jpk.getNaglowek().getCelZlozenia());
            System.out.println("Data START = "+jpk.getNaglowek().getDataOd().toString());
            System.out.println("Data STOP = "+jpk.getNaglowek().getDataDo().toString());

            if(jpk.getSprzedazCtrl() != null) {
                System.out.println("Sprzedaz liczba wierszy = " + jpk.getSprzedazCtrl().getLiczbaWierszySprzedazy());
                System.out.println("Sprzedaz podatek nalezny = " + jpk.getSprzedazCtrl().getPodatekNalezny());
            }
            if(jpk.getZakupCtrl() != null) {
                System.out.println("Zakup liczba wierszy = " + jpk.getZakupCtrl().getLiczbaWierszyZakupow());
                System.out.println("Zakup podatek naliczony = " + jpk.getZakupCtrl().getPodatekNaliczony());
            }
            System.out.println("PLIK WYJSCIOWY = "+xmlJpkVat);

        } else {
            System.err.println("WERSJA JPK NIEZAIMPLEMENTOWANA, skontaktuj się z Michałem");
        }

        generateVATRegisters();

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

    private static void generateVATRegisters(){
        VATRegistersGeneratorPDF generator = new VATRegistersGeneratorPDF(
                worksSprzedazList,
                worksZakupList,
                automatycznaDataOd.getMonthValue(),
                automatycznaDataOd.getYear());
        generator.writeAllRegisters("REJESTR");
    }
}
