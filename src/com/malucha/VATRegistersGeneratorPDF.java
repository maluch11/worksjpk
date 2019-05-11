package com.malucha;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.io.color.*;
import com.malucha.worksstructure.WorksSprzedaz;
import com.malucha.worksstructure.WorksZakup;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * HOW TO USE:
 * 1)
 * set worksSprzedazList
 * set worksZakupList
 *
 * 2)
 * set month
 * set year
 *
 * 3)
 * run writeAllRegisters()
 */

public class VATRegistersGeneratorPDF {
    String FILE_NAME = "test";
    List<WorksSprzedaz> worksSprzedazList;
    List<WorksZakup> worksZakupList;
    int month;
    int year;
    /** Fonts **/

    public VATRegistersGeneratorPDF(List<WorksSprzedaz> worksSprzedazList, List<WorksZakup> worksZakupList, int month, int year) {
        this.worksSprzedazList = worksSprzedazList;
        this.worksZakupList = worksZakupList;
        this.month = month;
        this.year = year;
    }

    public void writeAllRegisters(String fileName){
        try {
            PropertyReader p = PropertyReader.getInstance();
            String resultPath = p.plikWyjsciowyProp.getProperty("path");

            generateSalesRegisterVAT(resultPath+"SprzedazVAT"+"_"+year+"_"+month+"_"+fileName+".pdf");
            generateBuyRegisterVAT(resultPath+"ZakupyVAT"+"_"+year+"_"+month+"_"+fileName+".pdf");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void generateSalesRegisterVAT(String fileName) throws FileNotFoundException {
        PdfFont font;
        PdfFont bold;
        /** Fonts **/
        try {
            font = PdfFontFactory.createFont(FontConstants.HELVETICA);
            bold = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD);
        } catch (IOException e) {
            e.printStackTrace();
        }

        PdfWriter writer = new PdfWriter(fileName)  ;
        PdfDocument pdf = new PdfDocument(writer);
        PageSize pageSize = PageSize.A4;
        pageSize = pageSize.rotate();//Start in landscape
        Document document = new Document(pdf, pageSize);

        document.setMargins(15,15,15,15);

        /** Content **/
        //Image itext = new Image(ImageDataFactory.create("itext.png")).setWidth(75);
        //document.add(new Paragraph().add("Your developer journey with ").add(itext).add(" begins here..."));

        Paragraph title = new Paragraph();
        Paragraph head = new Paragraph();
//            Paragraph table = new Paragraph().setFont(font);

        title.add("Zaklad Elektromechaniki Dzwigowej Ryszard Malucha, NIP: 6480107355");
        document.add(title);
        head.add("Rejestr Sprzedazy VAT za miesiac: ").add(String.valueOf(month)).add("/").add(String.valueOf(year));
        document.add(head);

        List<WorksSprzedaz> worksSprzedazListFiltered;

//        example: https://developers.itextpdf.com/examples/tables/clone-large-tables
        String[] columnsHeader = {
          "1 Lp Ewidencji",
          "2 Nr dokumentu",
          "3 Korekta [TAK/NIE]",
          "4 odwrotne Obciazenie [TAK/NIE]",
          "5 Data wystawienia",
          "6 Nazwa nabywcy",
          "7 NIP Nabywcy",
          "8 Adres nabywcy",
                "9 Netto O.O.",
                "10 Brutto O.O.",
          "11 Netto 8%",
          "12 Vat 8%",
          "13 Brutto 8%",
          "14 Netto 23%",
          "15 Vat 23%",
          "16 Brutto 23%",
          "17 Netto SUM",
          "18 Vat SUM",
          "19 Brutto SUM"
        };
//        float[] columnsWidth = {10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10};
        float[] columnsWidth = {5,5,10,5,10,35,10,35,5,5,5,5,5,5,5,5,5,5,5};
        Table table = new Table(columnsWidth,true);
//        Table table = new Table(columnsHeader.length, true);
        table.setFontSize(6);
        table.setWidthPercent(100);

        /**
         * HEADERS
         */
        for (int i = 0; i <= 18; i++) {
            table.addHeaderCell(columnsHeader[i]);
        }

        //document.add(table);

        /** SUMY U GORY **/
        BigDecimal[][] sumyTable = new BigDecimal[5][19]; //5,19
        for (int j = 0; j < 5; j++) {
            for (int k = 0; k < 19; k++) {
                sumyTable[j][k] = BigDecimal.ZERO;
            }
        }
        calculateSprzedazSumRow(sumyTable, 0, "NIE", "NIE");
        calculateSprzedazSumRow(sumyTable, 2, "NIE", "TAK");
        calculateSprzedazSumRow(sumyTable, 1, "TAK", "NIE");
        calculateSprzedazSumRow(sumyTable, 3, "TAK", "TAK");
        calculateSprzedazSumRow(sumyTable, 4, null, null);

        addSprzedazSumRow(table, sumyTable[0], "NIE", "NIE");
        addSprzedazSumRow(table, sumyTable[2], "NIE", "TAK");
        addSprzedazSumRow(table, sumyTable[1], "TAK", "NIE");
        addSprzedazSumRow(table, sumyTable[3], "TAK", "TAK");
        addSprzedazSumRow(table, sumyTable[4], "", "");

        /** REKORDY REJESTRU SPRZEDAZY **/
        worksSprzedazListFiltered = filterSprzedazList(worksSprzedazList,month);
        Iterator i = worksSprzedazListFiltered.iterator();
        while (i.hasNext()){
            WorksSprzedaz tt = (WorksSprzedaz) i.next();
            table
                    .addCell(String.valueOf(tt.getNumer()))
                    .addCell(String.valueOf(tt.getNumer())+"/"+tt.getDataJpk().getYear())
                    .addCell(tt.getKorekta())
                    .addCell(tt.getOdwrotneObciazenie())
                    .addCell(tt.getDataJpk().format(MainXML.dataFormat))
                    .addCell(tt.getNabywca())
                    .addCell(String.valueOf(tt.getNip()))
                    .addCell(tt.getNabywcaAdres())
                    .addCell(tt.getNettoOdwrObciaz().toString()) // odwrotne obciazenie z faktury wystawionej dla kontrahenta
                    .addCell(tt.getBruttoOdwrObciaz().toString()) // odwrotne obciazenie z faktury wystawionej dla kontrahenta
                    .addCell(tt.getNetto8Sum().toString())
                    .addCell(tt.getVat8Sum().toString())
                    .addCell(tt.getBrutto8Sum().toString())
                    .addCell(tt.getNetto23Sum().toString())
                    .addCell(tt.getVat23Sum().toString())
                    .addCell(tt.getBrutto23Sum().toString())
                    .addCell(tt.getNetto8Sum().add(tt.getNetto23Sum()).add(tt.getNettoOdwrObciaz()).toString())
                    .addCell(tt.getVat8Sum().add(tt.getVat23Sum()).toString())
                    .addCell(tt.getBrutto8Sum().add(tt.getBrutto23Sum()).add(tt.getBruttoOdwrObciaz()).toString());
        }

        /** SUMY NA DOLE **/
        addSprzedazSumRow(table, sumyTable[0], "NIE", "NIE");
        addSprzedazSumRow(table, sumyTable[2], "NIE", "TAK");
        addSprzedazSumRow(table, sumyTable[1], "TAK", "NIE");
        addSprzedazSumRow(table, sumyTable[3], "TAK", "TAK");
        addSprzedazSumRow(table, sumyTable[4], "", "");


        /**
         * DODANIE TABELI DO DOKUMENTU
         */
        document.add(table);
        table.complete();
        //document.add(table);

        /** closing the writing **/
        document.close();
    }

    private void generateBuyRegisterVAT(String fileName) throws FileNotFoundException {
        PdfWriter writer = new PdfWriter(fileName)  ;
        PdfDocument pdf = new PdfDocument(writer);
        PageSize pageSize = PageSize.A4;
        pageSize = pageSize.rotate();//Start in landscape
        Document document = new Document(pdf, pageSize);

        document.setMargins(15,15,15,15);

        /** Content **/
        Paragraph title = new Paragraph();
        Paragraph head = new Paragraph();

        title.add("Zaklad Elektromechaniki Dzwigowej Ryszard Malucha, NIP: 6480107355");
        document.add(title);
        head.add("Rejestr Zakupu VAT za miesiac: ").add(String.valueOf(month)).add("/").add(String.valueOf(year));
        document.add(head);

        List<WorksZakup> worksZakupListFiltered;

//        example: https://developers.itextpdf.com/examples/tables/clone-large-tables
        String[] columnsHeader = {
                "1 Lp Ewidencji",
                "2 Nr dokumentu",
                "3 Data zakupuu",
                "4 Nazwa dostawcy",
                "5 NIP dostawcy",
                "6 Przedmiot zakupu",
                "7 Data wpisu",
                "8 23% netto",
                "9 23% vat",
                "10 12% netto",
                "11 12% vat",
                "12 8% netto",
                "13 8% vat",
                "14 5% netto",
                "15 5% vat",
                "16 3% netto",
                "17 3% vat",
                "18 0% netto",
                "19 Zwolnione",
                "20 Bez odliczenia",
                "21 Nie podlega",
                "22 Razem Brutto",
                "23 Razem Netto",
                "24 Razem VAT",
                "25 Korekta [TAK/NIE]",
                "26 Srodki trwale [TAK/NIE]",
                "27 Odwrotne obciazenie [TAK/NIE]"
        };

        float[] columnsWidth = {10,20,20,40,20,40,20,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10};
        Table table = new Table(columnsWidth, true);
        table.setFontSize(6);
        table.setWidthPercent(100);

        /** HEADERS */
        for (int i = 0; i <= 26; i++) {
            table.addHeaderCell(columnsHeader[i]);
        }

        /** SUMY U GORY **/
        BigDecimal[][] sumyTable = new BigDecimal[8][27]; //5,27
        for (int j = 0; j <= 7; j++) {
            for (int k = 0; k <= 26; k++) {
                sumyTable[j][k] = BigDecimal.ZERO;
            }
        }

        calculateZakupSumRow(sumyTable, 0, "NIE", "NIE","NIE");
        calculateZakupSumRow(sumyTable, 7, "NIE", "NIE","TAK");
        calculateZakupSumRow(sumyTable, 1, "NIE", "TAK","NIE");
        calculateZakupSumRow(sumyTable, 2, "NIE", "TAK","TAK");
        calculateZakupSumRow(sumyTable, 3, "TAK", "NIE","NIE");
        calculateZakupSumRow(sumyTable, 4, "TAK", "TAK","NIE");
        calculateZakupSumRow(sumyTable, 5, "TAK", "TAK","TAK");
        calculateZakupSumRow(sumyTable, 6, null, null, null);

        addZakupSumRow(table, sumyTable[0], "NIE", "NIE","NIE");
        addZakupSumRow(table, sumyTable[7], "NIE", "NIE","TAK");
        addZakupSumRow(table, sumyTable[1], "NIE", "TAK","NIE");
        addZakupSumRow(table, sumyTable[2], "NIE", "TAK","TAK");
        addZakupSumRow(table, sumyTable[3], "TAK", "NIE","NIE");
        addZakupSumRow(table, sumyTable[4], "TAK", "TAK","NIE");
        addZakupSumRow(table, sumyTable[5], "TAK", "TAK","TAK");
        addZakupSumRow(table, sumyTable[6], "", "","");

        /** REKORDY REJESTRU SPRZEDAZY **/
        worksZakupListFiltered = filtrZakupList(worksZakupList,month);
        Iterator i = worksZakupListFiltered.iterator();
        while (i.hasNext()){
            WorksZakup tt = (WorksZakup) i.next();
            table
                    .addCell(String.valueOf(tt.getLp()))
                    .addCell(tt.getDokument())
                    .addCell(tt.getDataWystFull().format(MainXML.dataFormat))
                    .addCell(tt.getKontrahent())
                    .addCell(tt.getNip())
                    .addCell(tt.getOpisUslugi1())
                    .addCell(tt.getDataWpisuFull().format(MainXML.dataFormat))
                    .addCell(tt.getNetto23().toString())
                    .addCell(tt.getVat23().toString())
                    .addCell(tt.getNetto12().toString())
                    .addCell(tt.getVat12().toString())
                    .addCell(tt.getNetto8().toString())
                    .addCell(tt.getVat8().toString())
                    .addCell(tt.getNetto5().toString())
                    .addCell(tt.getVat5().toString())
                    .addCell(tt.getNetto3().toString())
                    .addCell(tt.getVat3().toString())
                    .addCell(tt.getNetto0().toString())
                    .addCell(tt.getZwolnione().toString())
                    .addCell(tt.getBezOdliczen().toString())
                    .addCell(tt.getNiePodlega().toString())
                    .addCell(tt.getRazemBrutto().toString())
                    .addCell(tt.getRazemNetto().toString())
                    .addCell(tt.getRazemVat().toString())
                    .addCell(tt.getKorekta())
                    .addCell(tt.getSrodkiTrwale())
                    .addCell(tt.getOdwrotneObciazenie())
            ;
        }

        /** SUMY NA DOLE **/
        addZakupSumRow(table, sumyTable[0], "NIE", "NIE","NIE");
        addZakupSumRow(table, sumyTable[7], "NIE", "NIE","TAK");
        addZakupSumRow(table, sumyTable[1], "NIE", "TAK","NIE");
        addZakupSumRow(table, sumyTable[2], "NIE", "TAK","TAK");
        addZakupSumRow(table, sumyTable[3], "TAK", "NIE","NIE");
        addZakupSumRow(table, sumyTable[4], "TAK", "TAK","NIE");
        addZakupSumRow(table, sumyTable[5], "TAK", "TAK","TAK");
        addZakupSumRow(table, sumyTable[6], "", "","");


        /**
         * DODANIE TABELI DO DOKUMENTU
         */
        document.add(table);
        table.complete();

        /** closing the writing **/
        document.close();
    }

    private void addSprzedazSumRow(Table table, BigDecimal[] bigDecimals, String odwrotneObciazenie, String korekta) {
        table
                .addCell("")
                .addCell("")
                .addCell(korekta)
                .addCell(odwrotneObciazenie)
                .addCell("")
                .addCell("")
                .addCell("")
                .addCell("SUMA CZESCIOWA: ")
                .addCell(bigDecimals[8].toString())
                .addCell(bigDecimals[9].toString())
                .addCell(bigDecimals[10].toString())
                .addCell(bigDecimals[11].toString())
                .addCell(bigDecimals[12].toString())
                .addCell(bigDecimals[13].toString())
                .addCell(bigDecimals[14].toString())
                .addCell(bigDecimals[15].toString())
                .addCell(bigDecimals[16].toString())
                .addCell(bigDecimals[17].toString())
                .addCell(bigDecimals[18].toString());
    }

    private void addZakupSumRow(Table table, BigDecimal[] bigDecimals, String korekta, String srodkiTrwale, String odwrotneObciazenie) {
        table
                .addCell("")
                .addCell("")
                .addCell("")
                .addCell("")
                .addCell("")
                .addCell("")
                .addCell("SUMA CZESCIOWA: ")
                .addCell(bigDecimals[7].toString())
                .addCell(bigDecimals[8].toString())
                .addCell(bigDecimals[9].toString())
                .addCell(bigDecimals[10].toString())
                .addCell(bigDecimals[11].toString())
                .addCell(bigDecimals[12].toString())
                .addCell(bigDecimals[13].toString())
                .addCell(bigDecimals[14].toString())
                .addCell(bigDecimals[15].toString())
                .addCell(bigDecimals[16].toString())
                .addCell(bigDecimals[17].toString())
                .addCell(bigDecimals[18].toString())
                .addCell(bigDecimals[19].toString())
                .addCell(bigDecimals[20].toString())
                .addCell(bigDecimals[21].toString())
                .addCell(bigDecimals[22].toString())
                .addCell(bigDecimals[23].toString())
                .addCell(korekta)
                .addCell(srodkiTrwale)
                .addCell(odwrotneObciazenie)
        ;
    }

    private void calculateSprzedazSumRow(BigDecimal[][] sumy, int sumRow, String odwrotneObciazenie, String korekta) {
        List<WorksSprzedaz> worksSprzedazListFiltered;
        if(odwrotneObciazenie == null && korekta == null)
            worksSprzedazListFiltered = filterSprzedazList(worksSprzedazList,month);
        else
            worksSprzedazListFiltered = filterSprzedazList(worksSprzedazList,month,odwrotneObciazenie,korekta);

        Iterator j = worksSprzedazListFiltered.iterator();
        while (j.hasNext()){
            WorksSprzedaz tt = (WorksSprzedaz) j.next();

            sumy[sumRow][8] = sumy[sumRow][8].add(tt.getNettoOdwrObciaz() != null ? tt.getNettoOdwrObciaz() : BigDecimal.ZERO);
            sumy[sumRow][9] = sumy[sumRow][9].add(tt.getBruttoOdwrObciaz() != null ? tt.getBruttoOdwrObciaz() : BigDecimal.ZERO);

            sumy[sumRow][10] = sumy[sumRow][10].add(tt.getNetto8Sum() != null ? tt.getNetto8Sum() : BigDecimal.ZERO);
            sumy[sumRow][11] = sumy[sumRow][11].add(tt.getVat8Sum()!= null ? tt.getVat8Sum() : BigDecimal.ZERO);
            sumy[sumRow][12] = sumy[sumRow][12].add(tt.getBrutto8Sum()!= null ? tt.getBrutto8Sum() : BigDecimal.ZERO);
            sumy[sumRow][13] = sumy[sumRow][13].add(tt.getNetto23Sum()!= null ? tt.getNetto23Sum() : BigDecimal.ZERO);
            sumy[sumRow][14] = sumy[sumRow][14].add(tt.getVat23Sum()!= null ? tt.getVat23Sum() : BigDecimal.ZERO);
            sumy[sumRow][15] = sumy[sumRow][15].add(tt.getBrutto23Sum()!= null ? tt.getBrutto23Sum() : BigDecimal.ZERO);

            // TODO: 16.01.2018 Sumowanie chyba powinno byc poza iteracja i sumowac tabele sumy[][]
            sumy[sumRow][16] = sumy[sumRow][16].add(tt.getNetto8Sum().add(tt.getNetto23Sum()).add(tt.getNettoOdwrObciaz()));
            sumy[sumRow][17] = sumy[sumRow][17].add(tt.getVat8Sum().add(tt.getVat23Sum()));
            sumy[sumRow][18] = sumy[sumRow][18].add(tt.getBrutto8Sum().add(tt.getBrutto23Sum()).add(tt.getBruttoOdwrObciaz()));
        }

    }

    private void calculateZakupSumRow(BigDecimal[][] sumy, int sumRow, String korekta, String srodkiTrwale, String odwrotneObciazenie) {
        List<WorksZakup> worksZakupListFiltered;
        worksZakupListFiltered = (korekta == null && srodkiTrwale == null && odwrotneObciazenie == null) ? filtrZakupList(worksZakupList,month) : filtrZakupList(worksZakupList,month,korekta,srodkiTrwale,odwrotneObciazenie);
        Iterator j = worksZakupListFiltered.iterator();
        while (j.hasNext()){
            String[] columnsHeader = {
                    "1 Lp Ewidencji",
                    "2 Nr dokumentu",
                    "3 Data zakupuu",
                    "4 Nazwa dostawcy",
                    "5 NIP dostawcy",
                    "6 Przedmiot zakupu",
                    "7 Data wpisu",
                    "8 23 netto",
                    "9 23 vat",
                    "10 12 netto",
                    "11 12 vat",
                    "12 8 netto",
                    "13 8 vat",
                    "14 5 netto",
                    "15 5 vat",
                    "16 3 netto",
                    "17 3 vat",
                    "18 0 netto",
                    "19 Zwolnione",
                    "20 Bez odliczenia",
                    "21 Nie podlega",
                    "22 Razem Brutto",
                    "23 Razem Netto",
                    "24 Razem VAT"
            };

            WorksZakup tt = (WorksZakup) j.next();
            sumy[sumRow][7] = sumy[sumRow][7].add(tt.getNetto23() != null ? tt.getNetto23() : BigDecimal.ZERO);
            sumy[sumRow][8] = sumy[sumRow][8].add(tt.getVat23() != null ? tt.getVat23() : BigDecimal.ZERO);
            sumy[sumRow][9] = sumy[sumRow][9].add(tt.getNetto12()!= null ? tt.getNetto12() : BigDecimal.ZERO);
            sumy[sumRow][10] = sumy[sumRow][10].add(tt.getVat12()!= null ? tt.getVat12() : BigDecimal.ZERO);
            sumy[sumRow][11] = sumy[sumRow][11].add(tt.getNetto8()!= null ? tt.getNetto8() : BigDecimal.ZERO);
            sumy[sumRow][12] = sumy[sumRow][12].add(tt.getVat8()!= null ? tt.getVat8() : BigDecimal.ZERO);
            sumy[sumRow][13] = sumy[sumRow][13].add(tt.getNetto5()!= null ? tt.getNetto5() : BigDecimal.ZERO);
            sumy[sumRow][14] = sumy[sumRow][14].add(tt.getVat5()!= null ? tt.getVat5() : BigDecimal.ZERO);
            sumy[sumRow][15] = sumy[sumRow][15].add(tt.getNetto3()!= null ? tt.getNetto3() : BigDecimal.ZERO);
            sumy[sumRow][16] = sumy[sumRow][16].add(tt.getVat3()!= null ? tt.getVat3() : BigDecimal.ZERO);

            sumy[sumRow][17] = sumy[sumRow][17].add(tt.getNetto0()!= null ? tt.getNetto0() : BigDecimal.ZERO);
            sumy[sumRow][18] = sumy[sumRow][18].add(tt.getZwolnione()!= null ? tt.getZwolnione() : BigDecimal.ZERO);
            sumy[sumRow][19] = sumy[sumRow][19].add(tt.getBezOdliczen()!= null ? tt.getBezOdliczen() : BigDecimal.ZERO);
            sumy[sumRow][20] = sumy[sumRow][20].add(tt.getNiePodlega()!= null ? tt.getNiePodlega() : BigDecimal.ZERO);

            sumy[sumRow][21] = sumy[sumRow][21].add(tt.getRazemBrutto()!= null ? tt.getRazemBrutto() : BigDecimal.ZERO);
            sumy[sumRow][22] = sumy[sumRow][22].add(tt.getRazemNetto()!= null ? tt.getRazemNetto() : BigDecimal.ZERO);
            sumy[sumRow][23] = sumy[sumRow][23].add(tt.getRazemVat()!= null ? tt.getRazemVat() : BigDecimal.ZERO);
        }

    }

    /** Faktury korygujace Sprzedazy trzeba wystawiac dwie jedna zerujaca druga wlasciwa kwota) */
    /**     * Filtrowanie listy faktur sprzedazowych po parametrach: Miesiac, odwrotneObciazenie     */
    private List<WorksSprzedaz> filterSprzedazList(List<WorksSprzedaz> in, int month, String odwrotneObciazenie, String korekta){
//        System.err.println("FILTR: korekta = "+korekta+" odwrotne obciazenie = "+odwrotneObciazenie);
        List<WorksSprzedaz> s = new ArrayList<>();
        Iterator iterator = in.iterator();
        while(iterator.hasNext()){
            WorksSprzedaz row = (WorksSprzedaz) iterator.next();
            if(
                    row.getDataJpk().getMonth().getValue() == month
                    && row.getDataJpk().getYear() == year
                    && row.getOdwrotneObciazenie().trim().toUpperCase().contains(odwrotneObciazenie)
                    && row.getKorekta().trim().toUpperCase().contains(korekta.equals("NIE") ? "NIE" : "KOR")
            ){
//                System.err.println(row.getNumer()+" : "+ row.getOdwrotneObciazenie()+" , "+ row.getKorekta());
                s.add(row);
            }
        }
        return s;
    }

    /**     * Filtrowanie listy faktur sprzedazowych po parametrach: Miesiac     */
    private List<WorksSprzedaz> filterSprzedazList(List<WorksSprzedaz> in, int month){
        List<WorksSprzedaz> s = new ArrayList<>();
        Iterator iterator = in.iterator();
        while(iterator.hasNext()){
            WorksSprzedaz row = (WorksSprzedaz) iterator.next();
            if(row.getDataJpk().getMonth().getValue() == month
                    && row.getDataJpk().getYear() == year){
                s.add(row);
            }
        }
        return s;
    }

    /** Fakture korygujaca Zakupowa w rejestrze zakupow trzeba ksiegowac w dwoch rekordach (bylo ze znakiem ujemnym, powinno byc ze znakiem dodatnim) LUB delte */
    /**     * Filtrowanie listy faktur zakupowych po parametrach: Miesiac, Korekta, SrodkiTrwal, OdwrotneObciazenie     */
    private List<WorksZakup> filtrZakupList(List<WorksZakup> in, int month, String korekta, String srodkiTrwale, String odwrotneObciazenie){
        List<WorksZakup> s = new ArrayList<>();
        Iterator iterator = in.iterator();
        while(iterator.hasNext()){
            WorksZakup row = (WorksZakup) iterator.next();
            if( row.getDataWpisuFull().getMonth().getValue() == month
                    && row.getKorekta().equals(korekta)
                    && row.getSrodkiTrwale().contains(srodkiTrwale)
                    && row.getOdwrotneObciazenie().contains(odwrotneObciazenie )
                    && row.getDataWpisuFull().getYear() == year){
                s.add(row);
            }
        }
        return s;
    }

    /**     * Filtrowanie listy faktur zakupowych po parametrach: Miesiac     */
    private List<WorksZakup> filtrZakupList(List<WorksZakup> in, int month){
        List<WorksZakup> s = new ArrayList<>();
        Iterator iterator = in.iterator();
        while(iterator.hasNext()){
            WorksZakup row = (WorksZakup) iterator.next();
            if( row.getDataWpisuFull().getMonth().getValue() == month
                    && row.getDataWpisuFull().getYear() == year){
                s.add(row);
            }
        }
        return s;
    }

/** Getters setters **/
    public List<WorksSprzedaz> getWorksSprzedazList() {
        return worksSprzedazList;
    }

    public void setWorksSprzedazList(List<WorksSprzedaz> worksSprzedazList) {
        this.worksSprzedazList = worksSprzedazList;
    }

    public List<WorksZakup> getWorksZakupList() {
        return worksZakupList;
    }

    public void setWorksZakupList(List<WorksZakup> worksZakupList) {
        this.worksZakupList = worksZakupList;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
