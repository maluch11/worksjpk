package com.malucha.jpkstructure2;

import com.malucha.MainNG;
import com.malucha.PropertyReader;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Zakup {
    private String typ = "G";
    private int lpZakupu = 1;
    private long nrDostawcy;
    private String nazwaDostawcy = "";
    private String adresDostawcy = "";
    private String dowodZakupu = "";
    private LocalDate dataZakupu;
    private LocalDate dataWplywu;
    private BigDecimal k_43 = BigDecimal.ZERO; //nettoSrodkiTrwale
    private BigDecimal k_44 = BigDecimal.ZERO; //vatSrodkiTrwale
    private BigDecimal k_45 = BigDecimal.ZERO; //netto
    private BigDecimal k_46 = BigDecimal.ZERO; //vat
    private BigDecimal k_47 = BigDecimal.ZERO; //korektaPodatkuOdNabyciaSrodkowTrwalych
    private BigDecimal k_48 = BigDecimal.ZERO; //korektaPodatku
    private BigDecimal k_49 = BigDecimal.ZERO; //korektaArt89bust1
    private BigDecimal k_50 = BigDecimal.ZERO; //korektaArt89bust4

    private static DateTimeFormatter dataFormat = MainNG.dataFormat;
    private static DateTimeFormatter czasFormat = MainNG.czasFormat;
    private final int ZAKUP_OFFSET = Integer.parseInt(PropertyReader.getInstance().plikWyjsciowyProp.getProperty("zakupOffset"));
    private final int ILOSC_KOLUMN_W_PLIKU = Integer.parseInt(PropertyReader.getInstance().plikWyjsciowyProp.getProperty("iloscKolumnWPliku"));

    String[] zakupCSV;

    public Zakup(int lpZakupu, long nrDostawcy, String nazwaDostawcy, String adresDostawcy, String dowodZakupu, LocalDate dataZakupu, LocalDate dataWplywu, BigDecimal k_43, BigDecimal k_44, BigDecimal k_45, BigDecimal k_46) {
        this.lpZakupu = lpZakupu;
        this.nrDostawcy = nrDostawcy;
        this.nazwaDostawcy = nazwaDostawcy;
        this.adresDostawcy = adresDostawcy;
        this.dowodZakupu = dowodZakupu;
        this.dataZakupu = dataZakupu;
        this.dataWplywu = dataWplywu;
        this.k_43 = k_43;
        this.k_44 = k_44;
        this.k_45 = k_45;
        this.k_46 = k_46;

        String[] csv = {
                typ,
                String.valueOf(lpZakupu),
                String.valueOf(nrDostawcy),
                nazwaDostawcy,
                adresDostawcy,
                dowodZakupu,
                dataZakupu.format(dataFormat),
                dataWplywu.format(dataFormat),
                String.valueOf(k_43),
                String.valueOf(k_44),
                String.valueOf(k_45),
                String.valueOf(k_46),
                String.valueOf(k_47),
                String.valueOf(k_48),
                String.valueOf(k_49),
                String.valueOf(k_50)
        };

        this.zakupCSV = csv;
    }

    public String getTyp() {
        return typ;
    }

    public void setTyp(String typ) {
        this.typ = typ;
    }

    public int getLpZakupu() {
        return lpZakupu;
    }

    public void setLpZakupu(int lpZakupu) {
        this.lpZakupu = lpZakupu;
    }

    public long getNrDostawcy() {
        return nrDostawcy;
    }

    public void setNrDostawcy(long nrDostawcy) {
        this.nrDostawcy = nrDostawcy;
    }

    public String getNazwaDostawcy() {
        return nazwaDostawcy;
    }

    public void setNazwaDostawcy(String nazwaDostawcy) {
        this.nazwaDostawcy = nazwaDostawcy;
    }

    public String getAdresDostawcy() {
        return adresDostawcy;
    }

    public void setAdresDostawcy(String adresDostawcy) {
        this.adresDostawcy = adresDostawcy;
    }

    public String getDowodZakupu() {
        return dowodZakupu;
    }

    public void setDowodZakupu(String dowodZakupu) {
        this.dowodZakupu = dowodZakupu;
    }

    public LocalDate getDataZakupu() {
        return dataZakupu;
    }

    public void setDataZakupu(LocalDate dataZakupu) {
        this.dataZakupu = dataZakupu;
    }

    public LocalDate getDataWplywu() {
        return dataWplywu;
    }

    public void setDataWplywu(LocalDate dataWplywu) {
        this.dataWplywu = dataWplywu;
    }

    public BigDecimal getK_43() {
        return k_43;
    }

    public void setK_43(BigDecimal k_43) {
        this.k_43 = k_43;
    }

    public BigDecimal getK_44() {
        return k_44;
    }

    public void setK_44(BigDecimal k_44) {
        this.k_44 = k_44;
    }

    public BigDecimal getK_45() {
        return k_45;
    }

    public void setK_45(BigDecimal k_45) {
        this.k_45 = k_45;
    }

    public BigDecimal getK_46() {
        return k_46;
    }

    public void setK_46(BigDecimal k_46) {
        this.k_46 = k_46;
    }

    public BigDecimal getK_47() {
        return k_47;
    }

    public void setK_47(BigDecimal k_47) {
        this.k_47 = k_47;
    }

    public BigDecimal getK_48() {
        return k_48;
    }

    public void setK_48(BigDecimal k_48) {
        this.k_48 = k_48;
    }

    public BigDecimal getK_49() {
        return k_49;
    }

    public void setK_49(BigDecimal k_49) {
        this.k_49 = k_49;
    }

    public BigDecimal getK_50() {
        return k_50;
    }

    public void setK_50(BigDecimal k_50) {
        this.k_50 = k_50;
    }

    public String[] getZakupCSV() {
        String[] returnvalue = new String[ILOSC_KOLUMN_W_PLIKU];
        for (int i = 0; i < zakupCSV.length; i++) {
            returnvalue[i+ZAKUP_OFFSET]=zakupCSV[i];
        }
        return returnvalue;
    }
}
