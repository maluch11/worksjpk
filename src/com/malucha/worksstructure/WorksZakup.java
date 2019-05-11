package com.malucha.worksstructure;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class WorksZakup {
    int lp; //0
    int rok; //1
    String dataWyst; //Data wyst //2
    String dokument; //3
    String kontrahent; //4
    String adres; //5
    String nip; //6
    String opisUslugi1; //Opis uslugi 1 //7
    BigDecimal netto23; //8
    BigDecimal netto12; //9
    BigDecimal netto8; //10
    BigDecimal netto5; //11
    BigDecimal netto3; //12
    BigDecimal netto0; //13
    BigDecimal zwolnione; //14
    BigDecimal bezOdliczen; //15
    BigDecimal niePodlega; //16
    String dataWpisu; //Data wpisu //17
    int rok2 = rok; //18
    BigDecimal vat23; //19
    BigDecimal brutto23; //20
    BigDecimal vat12; //21
    BigDecimal brutto12; //22
    BigDecimal vat8; //23
    BigDecimal brutto8; //24
    BigDecimal vat5; //25
    BigDecimal brutto5; //26
    BigDecimal vat3; //27
    BigDecimal brutto3; //28
    BigDecimal razemBrutto; //29
    BigDecimal razemNetto; //30
    BigDecimal razemVat; //31
    String srodkiTrwale = "NIE"; //32
    String korekta = "NIE"; //33
    String odwrotneObciazenie = "NIE"; //34


    LocalDate dataWystFull;
    LocalDate dataWpisuFull;

    public static DecimalFormat decimalFormat = new DecimalFormat("0.00");

//    public WorksZakup(int lp, int rok, String dataWyst, String dokument, String kontrahent, String adres, String nip, String dataWpisu, BigDecimal razemNetto, BigDecimal razemVat, String srodkiTrwale, String korekta, String odwrotneObciazenie) {
//        this.lp = lp;
//        this.rok = rok;
//        this.dataWyst = dataWyst;
//        this.dokument = dokument;
//        this.kontrahent = kontrahent;
//        this.adres = adres;
//        this.nip = nip;
//        this.dataWpisu = dataWpisu;
//        this.razemNetto = razemNetto;
//        this.razemVat = razemVat;
//        this.srodkiTrwale = srodkiTrwale;
//        this.korekta = korekta;
//        this.odwrotneObciazenie = odwrotneObciazenie;
//
////        FOR LOGGING PURPOSES WHEN LOADING FAILS
////        System.out.println("LP" + lp);
////        System.out.println(adres);
////        System.out.println("WYST = " + dataWyst.replaceAll("[^0-9]+", ""));
////        System.out.println("WPIS = " + dataWpisu.replaceAll("[^0-9]+", ""));
////        System.out.println("");
//
//        this.dataWystFull = LocalDate.parse(String.valueOf(rok)+dataWyst.replaceAll("[^0-9]+", ""), DateTimeFormatter.ofPattern("yyyyMMdd"));
//        this.dataWpisuFull = LocalDate.parse(String.valueOf(rok)+dataWpisu.replaceAll("[^0-9]+", ""), DateTimeFormatter.ofPattern("yyyyMMdd"));
//    }

    public WorksZakup(int lp, int rok, String dataWyst, String dokument, String kontrahent, String adres, String nip,
                      String dataWpisu, BigDecimal razemNetto, BigDecimal razemVat, String srodkiTrwale, String korekta,
                      String odwrotneObciazenie,
                      BigDecimal netto23, BigDecimal vat23,
                      BigDecimal netto12, BigDecimal vat12,
                      BigDecimal netto8, BigDecimal vat8,
                      BigDecimal netto5, BigDecimal vat5,
                      BigDecimal netto3, BigDecimal vat3,
                      BigDecimal netto0,
                      BigDecimal zwolnione,
                      BigDecimal bezOdliczen,
                      BigDecimal niePodlega,
                      BigDecimal razemBrutto,
                      String opisUslugi1
    )
    {
        this.lp = lp;
        this.rok = rok;
        this.dataWyst = dataWyst;
        this.dokument = dokument;
        this.kontrahent = kontrahent;
        this.adres = adres;
        this.nip = nip;
        this.dataWpisu = dataWpisu;
        this.razemNetto = razemNetto;
        this.razemVat = razemVat;

        this.setSrodkiTrwale(srodkiTrwale);
        this.setKorekta(korekta);
        this.setOdwrotneObciazenie(odwrotneObciazenie);

        this.netto23 = netto23;
        this.vat23 = vat23;
        this.netto12 = netto12;
        this.vat12 = vat12;
        this.netto8 = netto8;
        this.vat8 = vat8;
        this.netto5 = netto5;
        this.vat5 = vat5;
        this.netto3 = netto3;
        this.vat3 = vat3;
        this.netto0 = netto0;
        this.zwolnione = zwolnione;
        this.bezOdliczen = bezOdliczen;
        this.niePodlega = niePodlega;
        this.razemBrutto = razemBrutto;
        this.opisUslugi1 = opisUslugi1;

        this.dataWystFull = LocalDate.parse(String.valueOf(rok)+dataWyst.replaceAll("[^0-9]+", ""), DateTimeFormatter.ofPattern("yyyyMMdd"));
        this.dataWpisuFull = LocalDate.parse(String.valueOf(rok)+dataWpisu.replaceAll("[^0-9]+", ""), DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

    public int getLp() {
        return lp;
    }

    public void setLp(int lp) {
        this.lp = lp;
    }

    public int getRok() {
        return rok;
    }

    public void setRok(int rok) {
        this.rok = rok;
    }

    public String getDataWyst() {
        return dataWyst;
    }

    public void setDataWyst(String dataWyst) {
        this.dataWyst = dataWyst;
    }

    public String getDokument() {
        return dokument;
    }

    public void setDokument(String dokument) {
        this.dokument = dokument;
    }

    public String getKontrahent() {
        return kontrahent;
    }

    public void setKontrahent(String kontrahent) {
        this.kontrahent = kontrahent;
    }

    public String getAdres() {
        return adres;
    }

    public void setAdres(String adres) {
        this.adres = adres;
    }

    public String getNip() {
        return nip;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }

    public String getDataWpisu() {
        return dataWpisu;
    }

    public void setDataWpisu(String dataWpisu) {
        this.dataWpisu = dataWpisu;
    }

    public BigDecimal getrazemNetto() {
        return razemNetto;
    }

    public void setrazemNetto(BigDecimal razemNetto) {
        this.razemNetto = razemNetto;
    }

    public BigDecimal getRazemVat() {
        return razemVat;
    }

    public void setRazemVat(BigDecimal razemVat) {
        this.razemVat = razemVat;
    }

    public LocalDate getDataWystFull() {
        return dataWystFull;
    }

    public LocalDate getDataWpisuFull() {
        return dataWpisuFull;
    }

    public String getSrodkiTrwale() {
        return srodkiTrwale;
    }

    public void setSrodkiTrwale(String srodkiTrwale) {
        this.srodkiTrwale = srodkiTrwale.trim().equals("") || srodkiTrwale.trim().toUpperCase().contains("NIE") ? "NIE" : "TAK";
    }

    public String getKorekta() {
        return korekta;
    }

    public void setKorekta(String korekta) {
        this.korekta = korekta.trim().equals("") || korekta.trim().toUpperCase().contains("NIE") ? "NIE" : "TAK";
    }

    public String getOdwrotneObciazenie() {
        return odwrotneObciazenie;
    }

    public void setOdwrotneObciazenie(String odwrotneObciazenie) {
        this.odwrotneObciazenie = odwrotneObciazenie.trim().equals("") || odwrotneObciazenie.trim().toUpperCase().contains("NIE") ? "NIE" : "TAK";
    }

    public String getOpisUslugi1() {
        return opisUslugi1;
    }

    public void setOpisUslugi1(String opisUslugi1) {
        this.opisUslugi1 = opisUslugi1;
    }

    public BigDecimal getNetto23() {
        return netto23;
    }

    public void setNetto23(BigDecimal netto23) {
        this.netto23 = netto23;
    }

    public BigDecimal getNetto12() {
        return netto12;
    }

    public void setNetto12(BigDecimal netto12) {
        this.netto12 = netto12;
    }

    public BigDecimal getNetto8() {
        return netto8;
    }

    public void setNetto8(BigDecimal netto8) {
        this.netto8 = netto8;
    }

    public BigDecimal getNetto5() {
        return netto5;
    }

    public void setNetto5(BigDecimal netto5) {
        this.netto5 = netto5;
    }

    public BigDecimal getNetto3() {
        return netto3;
    }

    public void setNetto3(BigDecimal netto3) {
        this.netto3 = netto3;
    }

    public BigDecimal getNetto0() {
        return netto0;
    }

    public void setNetto0(BigDecimal netto0) {
        this.netto0 = netto0;
    }

    public BigDecimal getZwolnione() {
        return zwolnione;
    }

    public void setZwolnione(BigDecimal zwolnione) {
        this.zwolnione = zwolnione;
    }

    public BigDecimal getBezOdliczen() {
        return bezOdliczen;
    }

    public void setBezOdliczen(BigDecimal bezOdliczen) {
        this.bezOdliczen = bezOdliczen;
    }

    public BigDecimal getNiePodlega() {
        return niePodlega;
    }

    public void setNiePodlega(BigDecimal niePodlega) {
        this.niePodlega = niePodlega;
    }

    public int getRok2() {
        return rok2;
    }

    public void setRok2(int rok2) {
        this.rok2 = rok2;
    }

    public BigDecimal getVat23() {
        return vat23;
    }

    public void setVat23(BigDecimal vat23) {
        this.vat23 = vat23;
    }

    public BigDecimal getBrutto23() {
        return brutto23;
    }

    public void setBrutto23(BigDecimal brutto23) {
        this.brutto23 = brutto23;
    }

    public BigDecimal getVat12() {
        return vat12;
    }

    public void setVat12(BigDecimal vat12) {
        this.vat12 = vat12;
    }

    public BigDecimal getBrutto12() {
        return brutto12;
    }

    public void setBrutto12(BigDecimal brutto12) {
        this.brutto12 = brutto12;
    }

    public BigDecimal getVat8() {
        return vat8;
    }

    public void setVat8(BigDecimal vat8) {
        this.vat8 = vat8;
    }

    public BigDecimal getBrutto8() {
        return brutto8;
    }

    public void setBrutto8(BigDecimal brutto8) {
        this.brutto8 = brutto8;
    }

    public BigDecimal getVat5() {
        return vat5;
    }

    public void setVat5(BigDecimal vat5) {
        this.vat5 = vat5;
    }

    public BigDecimal getBrutto5() {
        return brutto5;
    }

    public void setBrutto5(BigDecimal brutto5) {
        this.brutto5 = brutto5;
    }

    public BigDecimal getVat3() {
        return vat3;
    }

    public void setVat3(BigDecimal vat3) {
        this.vat3 = vat3;
    }

    public BigDecimal getBrutto3() {
        return brutto3;
    }

    public void setBrutto3(BigDecimal brutto3) {
        this.brutto3 = brutto3;
    }

    public BigDecimal getRazemBrutto() {
        return razemBrutto;
    }

    public void setRazemBrutto(BigDecimal razemBrutto) {
        this.razemBrutto = razemBrutto;
    }

    public BigDecimal getRazemNetto() {
        return razemNetto;
    }

    public void setRazemNetto(BigDecimal razemNetto) {
        this.razemNetto = razemNetto;
    }

    public void setDataWystFull(LocalDate dataWystFull) {
        this.dataWystFull = dataWystFull;
    }

    public void setDataWpisuFull(LocalDate dataWpisuFull) {
        this.dataWpisuFull = dataWpisuFull;
    }

    public static DecimalFormat getDecimalFormat() {
        return decimalFormat;
    }

    public static void setDecimalFormat(DecimalFormat decimalFormat) {
        WorksZakup.decimalFormat = decimalFormat;
    }
}
