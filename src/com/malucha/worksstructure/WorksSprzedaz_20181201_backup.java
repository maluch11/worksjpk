package com.malucha.worksstructure;

import java.math.BigDecimal;
import java.time.LocalDate;

public class WorksSprzedaz_20181201_backup {
    private String data; //0
    private int rok; //1
    private int numer; //2
    private String nabywca; //3
    private String nabywcaAdres; //4
    private String odbiorca;  //5
    private String odbiorcaAdres;  //6
    private long nip;  //7
//    private String usluga1;
//    private BigDecimal netto1_23;
//    private BigDecimal netto1_8;
//    private String usluga2;
//    private BigDecimal netto2_23;
//    private BigDecimal netto2_8;

    private BigDecimal netto23Sum;
    private BigDecimal vat23Sum;
    private BigDecimal brutto23Sum;

    private BigDecimal netto8Sum;
    private BigDecimal vat8Sum;
    private BigDecimal brutto8Sum;

    private LocalDate dataJpk;
    private String odwrotneObciazenie = "NIE";
    private String korekta = "NIE";

    /**
        BigDecimal total = BigDecimal.ZERO;
        for (OrderLine line : lines) {
          BigDecimal price = new BigDecimal(line.price);
          BigDecimal count = new BigDecimal(line.count);
          total = total.add(price.multiply(count)); // BigDecimal jest niemutowalny, przez co troche niewygodny ;(
        }
        total = total.setScale(2, RoundingMode.HALF_UP);
    **/

    public WorksSprzedaz_20181201_backup(int numer, String nabywca, String nabywcaAdres, long nip, BigDecimal netto23Sum,
                                         BigDecimal vat23Sum, BigDecimal brutto23Sum, BigDecimal netto8Sum, BigDecimal vat8Sum,
                                         BigDecimal brutto8Sum,
                                         LocalDate dataJpk,
                                         String odwrotneObciazenie,
                                         String korekta) {
        this.numer = numer;
        this.nabywca = nabywca;
        this.nabywcaAdres = nabywcaAdres;
        this.nip = nip;
        this.netto23Sum = netto23Sum;
        this.vat23Sum = vat23Sum;
        this.brutto23Sum = brutto23Sum;
        this.netto8Sum = netto8Sum;
        this.vat8Sum = vat8Sum;
        this.brutto8Sum = brutto8Sum;
        this.dataJpk = dataJpk;
        this.setOdwrotneObciazenie(odwrotneObciazenie);
        this.setKorekta(korekta);
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getRok() {
        return rok;
    }

    public void setRok(int rok) {
        this.rok = rok;
    }

    public int getNumer() {
        return numer;
    }

    public void setNumer(int numer) {
        this.numer = numer;
    }

    public String getNabywca() {
        return nabywca;
    }

    public void setNabywca(String nabywca) {
        this.nabywca = nabywca;
    }

    public String getNabywcaAdres() {
        return nabywcaAdres;
    }

    public void setNabywcaAdres(String nabywcaAdres) {
        this.nabywcaAdres = nabywcaAdres;
    }

    public String getOdbiorca() {
        return odbiorca;
    }

    public void setOdbiorca(String odbiorca) {
        this.odbiorca = odbiorca;
    }

    public String getOdbiorcaAdres() {
        return odbiorcaAdres;
    }

    public void setOdbiorcaAdres(String odbiorcaAdres) {
        this.odbiorcaAdres = odbiorcaAdres;
    }

    public long getNip() {
        return nip;
    }

    public void setNip(long nip) {
        this.nip = nip;
    }

    public BigDecimal getNetto23Sum() {
        return netto23Sum;
    }

    public void setNetto23Sum(BigDecimal netto23Sum) {
        this.netto23Sum = netto23Sum;
    }

    public BigDecimal getVat23Sum() {
        return vat23Sum;
    }

    public void setVat23Sum(BigDecimal vat23Sum) {
        this.vat23Sum = vat23Sum;
    }

    public BigDecimal getNetto8Sum() {
        return netto8Sum;
    }

    public void setNetto8Sum(BigDecimal netto8Sum) {
        this.netto8Sum = netto8Sum;
    }

    public BigDecimal getVat8Sum() {
        return vat8Sum;
    }

    public void setVat8Sum(BigDecimal vat8Sum) {
        this.vat8Sum = vat8Sum;
    }

    public LocalDate getDataJpk() {
        return dataJpk;
    }

    public void setDataJpk(LocalDate dataJpk) {
        this.dataJpk = dataJpk;
    }

    public String getOdwrotneObciazenie() {
        return odwrotneObciazenie;
    }

    public void setOdwrotneObciazenie(String odwrotneObciazenie) {
        this.odwrotneObciazenie = odwrotneObciazenie.trim().toUpperCase().contains("TAK") ? "TAK" : "NIE";
    }

    public BigDecimal getZERO(){
        return BigDecimal.valueOf(0.00);
    }

    public String getKorekta() {
        return korekta;
    }

    public void setKorekta(String korekta) {
        this.korekta = korekta.trim().equals("") || korekta.trim().toUpperCase().contains("NIE") ? "NIE" : korekta.trim().toUpperCase();
    }

    public BigDecimal getBrutto23Sum() {
        return brutto23Sum;
    }

    public void setBrutto23Sum(BigDecimal brutto23Sum) {
        this.brutto23Sum = brutto23Sum;
    }

    public BigDecimal getBrutto8Sum() {
        return brutto8Sum;
    }

    public void setBrutto8Sum(BigDecimal brutto8Sum) {
        this.brutto8Sum = brutto8Sum;
    }
}
