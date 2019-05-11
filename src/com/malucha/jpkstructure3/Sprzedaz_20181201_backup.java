package com.malucha.jpkstructure3;

import com.malucha.MainNG;
import com.malucha.PropertyReader;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Sprzedaz_20181201_backup {
    int lpSprzedazy;
    long nrKontrahenta;
    String nazwaKontrahenta;
    String adreskontrahenta;
    String dowodSprzedazy;
    LocalDate dataWystawienia = LocalDate.ofYearDay(1990,1);
    LocalDate dataSprzedazy = LocalDate.ofYearDay(1990,1);
    BigDecimal k_10 = BigDecimal.ZERO;
    BigDecimal k_11 = BigDecimal.ZERO;
    BigDecimal k_12 = BigDecimal.ZERO;
    BigDecimal k_13 = BigDecimal.ZERO;
    BigDecimal k_14 = BigDecimal.ZERO;
    BigDecimal k_15 = BigDecimal.ZERO; //5%netto
    BigDecimal k_16 = BigDecimal.ZERO; //5%vat
    BigDecimal k_17 = BigDecimal.ZERO; //8%netto
    BigDecimal k_18 = BigDecimal.ZERO; //8%vat
    BigDecimal k_19 = BigDecimal.ZERO; //23%netto
    BigDecimal k_20 = BigDecimal.ZERO; //23%vat
    BigDecimal k_21 = BigDecimal.ZERO;
    BigDecimal k_22 = BigDecimal.ZERO;
    BigDecimal k_23 = BigDecimal.ZERO;
    BigDecimal k_24 = BigDecimal.ZERO;
    BigDecimal k_25 = BigDecimal.ZERO;
    BigDecimal k_26 = BigDecimal.ZERO;
    BigDecimal k_27 = BigDecimal.ZERO;
    BigDecimal k_28 = BigDecimal.ZERO;
    BigDecimal k_29 = BigDecimal.ZERO;
    BigDecimal k_30 = BigDecimal.ZERO;
    BigDecimal k_31 = BigDecimal.ZERO;
    BigDecimal k_32 = BigDecimal.ZERO;
    BigDecimal k_33 = BigDecimal.ZERO;
    BigDecimal k_34 = BigDecimal.ZERO;
    BigDecimal k_35 = BigDecimal.ZERO;
    BigDecimal k_36 = BigDecimal.ZERO;
    BigDecimal k_37 = BigDecimal.ZERO;
    BigDecimal k_38 = BigDecimal.ZERO;
    BigDecimal k_39 = BigDecimal.ZERO;

    private static DateTimeFormatter dataFormat = MainNG.dataFormat;
    private static DateTimeFormatter czasFormat = MainNG.czasFormat;
    private final int SPRZEDAZ_OFFSET = Integer.parseInt(PropertyReader.getInstance().plikWyjsciowyProp.getProperty("v3sprzedazOffset"));
    private final int ILOSC_KOLUMN_W_PLIKU = Integer.parseInt(PropertyReader.getInstance().plikWyjsciowyProp.getProperty("v3iloscKolumnWPliku"));

    String[] sprzedazCSV;

    public Sprzedaz_20181201_backup(int lpSprzedazy, long nrKontrahenta, String nazwaKontrahenta, String adreskontrahenta, String dowodSprzedazy, LocalDate dataWystawienia, LocalDate dataSprzedazy, BigDecimal k_17, BigDecimal k_18, BigDecimal k_19, BigDecimal k_20) {
        this.lpSprzedazy = lpSprzedazy;
        this.nrKontrahenta = nrKontrahenta;
        this.nazwaKontrahenta = nazwaKontrahenta;
        this.adreskontrahenta = adreskontrahenta;
        this.dowodSprzedazy = dowodSprzedazy;
        this.dataWystawienia = dataWystawienia;
        this.dataSprzedazy = dataSprzedazy;
        this.k_17 = k_17; //8%netto
        this.k_18 = k_18; //8%vat
        this.k_19 = k_19; //23%netto
        this.k_20 = k_20; //23&vat

        String[] csv = {
                String.valueOf(lpSprzedazy),
                String.valueOf(nrKontrahenta),
                nazwaKontrahenta,
                adreskontrahenta,
                dowodSprzedazy,
                dataWystawienia.format(dataFormat),
                dataSprzedazy.format(dataFormat),
                String.valueOf(k_10),
                String.valueOf(k_11),
                String.valueOf(k_12),
                String.valueOf(k_13),
                String.valueOf(k_14),
                String.valueOf(k_15), //5%netto
                String.valueOf(k_16), //5%vat
                String.valueOf(k_17), //8%netto
                String.valueOf(k_18), //8%vat
                String.valueOf(k_19), //23%netto
                String.valueOf(k_20), //23%vat
                String.valueOf(k_21),
                String.valueOf(k_22),
                String.valueOf(k_23),
                String.valueOf(k_24),
                String.valueOf(k_25),
                String.valueOf(k_26),
                String.valueOf(k_27),
                String.valueOf(k_28),
                String.valueOf(k_29),
                String.valueOf(k_30),
                String.valueOf(k_31), //OO_netto_wart
                String.valueOf(k_32),
                String.valueOf(k_33),
                String.valueOf(k_34),
                String.valueOf(k_35),
                String.valueOf(k_36),
                String.valueOf(k_37),
                String.valueOf(k_38),
                String.valueOf(k_39)
        };

        this.sprzedazCSV = csv;
    }

    public int getLpSprzedazy() {
        return lpSprzedazy;
    }

    public void setLpSprzedazy(int lpSprzedazy) {
        this.lpSprzedazy = lpSprzedazy;
    }

    public long getNrKontrahenta() {
        return nrKontrahenta;
    }

    public void setNrKontrahenta(long nrKontrahenta) {
        this.nrKontrahenta = nrKontrahenta;
    }

    public String getNazwaKontrahenta() {
        return nazwaKontrahenta;
    }

    public void setNazwaKontrahenta(String nazwaKontrahenta) {
        this.nazwaKontrahenta = nazwaKontrahenta;
    }

    public String getAdreskontrahenta() {
        return adreskontrahenta;
    }

    public void setAdreskontrahenta(String adreskontrahenta) {
        this.adreskontrahenta = adreskontrahenta;
    }

    public String getDowodSprzedazy() {
        return dowodSprzedazy;
    }

    public void setDowodSprzedazy(String dowodSprzedazy) {
        this.dowodSprzedazy = dowodSprzedazy;
    }

    public LocalDate getDataWystawienia() {
        return dataWystawienia;
    }

    public void setDataWystawienia(LocalDate dataWystawienia) {
        this.dataWystawienia = dataWystawienia;
    }

    public LocalDate getDataSprzedazy() {
        return dataSprzedazy;
    }

    public void setDataSprzedazy(LocalDate dataSprzedazy) {
        this.dataSprzedazy = dataSprzedazy;
    }

    public BigDecimal getK_10() {
        return k_10;
    }

    public void setK_10(BigDecimal k_10) {
        this.k_10 = k_10;
    }

    public BigDecimal getK_11() {
        return k_11;
    }

    public void setK_11(BigDecimal k_11) {
        this.k_11 = k_11;
    }

    public BigDecimal getK_12() {
        return k_12;
    }

    public void setK_12(BigDecimal k_12) {
        this.k_12 = k_12;
    }

    public BigDecimal getK_13() {
        return k_13;
    }

    public void setK_13(BigDecimal k_13) {
        this.k_13 = k_13;
    }

    public BigDecimal getK_14() {
        return k_14;
    }

    public void setK_14(BigDecimal k_14) {
        this.k_14 = k_14;
    }

    public BigDecimal getK_15() {
        return k_15;
    }

    public void setK_15(BigDecimal k_15) {
        this.k_15 = k_15;
    }

    public BigDecimal getK_16() {
        return k_16;
    }

    public void setK_16(BigDecimal k_16) {
        this.k_16 = k_16;
    }

    public BigDecimal getK_17() {
        return k_17;
    }

    public void setK_17(BigDecimal k_17) {
        this.k_17 = k_17;
    }

    public BigDecimal getK_18() {
        return k_18;
    }

    public void setK_18(BigDecimal k_18) {
        this.k_18 = k_18;
    }

    public BigDecimal getK_19() {
        return k_19;
    }

    public void setK_19(BigDecimal k_19) {
        this.k_19 = k_19;
    }

    public BigDecimal getK_20() {
        return k_20;
    }

    public void setK_20(BigDecimal k_20) {
        this.k_20 = k_20;
    }

    public BigDecimal getK_21() {
        return k_21;
    }

    public void setK_21(BigDecimal k_21) {
        this.k_21 = k_21;
    }

    public BigDecimal getK_22() {
        return k_22;
    }

    public void setK_22(BigDecimal k_22) {
        this.k_22 = k_22;
    }

    public BigDecimal getK_23() {
        return k_23;
    }

    public void setK_23(BigDecimal k_23) {
        this.k_23 = k_23;
    }

    public BigDecimal getK_24() {
        return k_24;
    }

    public void setK_24(BigDecimal k_24) {
        this.k_24 = k_24;
    }

    public BigDecimal getK_25() {
        return k_25;
    }

    public void setK_25(BigDecimal k_25) {
        this.k_25 = k_25;
    }

    public BigDecimal getK_26() {
        return k_26;
    }

    public void setK_26(BigDecimal k_26) {
        this.k_26 = k_26;
    }

    public BigDecimal getK_27() {
        return k_27;
    }

    public void setK_27(BigDecimal k_27) {
        this.k_27 = k_27;
    }

    public BigDecimal getK_28() {
        return k_28;
    }

    public void setK_28(BigDecimal k_28) {
        this.k_28 = k_28;
    }

    public BigDecimal getK_29() {
        return k_29;
    }

    public void setK_29(BigDecimal k_29) {
        this.k_29 = k_29;
    }

    public BigDecimal getK_30() {
        return k_30;
    }

    public void setK_30(BigDecimal k_30) {
        this.k_30 = k_30;
    }

    public BigDecimal getK_31() {
        return k_31;
    }

    public void setK_31(BigDecimal k_31) {
        this.k_31 = k_31;
    }

    public BigDecimal getK_32() {
        return k_32;
    }

    public void setK_32(BigDecimal k_32) {
        this.k_32 = k_32;
    }

    public BigDecimal getK_33() {
        return k_33;
    }

    public void setK_33(BigDecimal k_33) {
        this.k_33 = k_33;
    }

    public BigDecimal getK_34() {
        return k_34;
    }

    public void setK_34(BigDecimal k_34) {
        this.k_34 = k_34;
    }

    public BigDecimal getK_35() {
        return k_35;
    }

    public void setK_35(BigDecimal k_35) {
        this.k_35 = k_35;
    }

    public BigDecimal getK_36() {
        return k_36;
    }

    public void setK_36(BigDecimal k_36) {
        this.k_36 = k_36;
    }

    public BigDecimal getK_37() {
        return k_37;
    }

    public void setK_37(BigDecimal k_37) {
        this.k_37 = k_37;
    }

    public BigDecimal getK_38() {
        return k_38;
    }

    public void setK_38(BigDecimal k_38) {
        this.k_38 = k_38;
    }

    public BigDecimal getK_39() {
        return k_39;
    }

    public void setK_39(BigDecimal k_39) {
        this.k_39 = k_39;
    }

    public String[] getSprzedazCSV() {
        String[] returnvalue = new String[ILOSC_KOLUMN_W_PLIKU];
        for (int i = 0; i < sprzedazCSV.length; i++) {
            returnvalue[i+SPRZEDAZ_OFFSET]=sprzedazCSV[i];
        }
        return returnvalue;
    }
}
