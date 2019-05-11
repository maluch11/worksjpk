package com.malucha.jpkstructure2;

import com.malucha.MainNG;
import com.malucha.PropertyReader;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

public class Naglowek {
    Properties p = PropertyReader.getInstance().getNaglowekDaneProp();
    private KodFormularza kodFormularza = new KodFormularza();
    private int wariantFormularza = Integer.parseInt(p.getProperty("WariantFormularza","2")); //2;
    private int celZlozenia = Integer.parseInt(p.getProperty("CelZlozenia","1")); //1;

    private String czyDatyAutomatyczne = p.getProperty("CzyDatyAutomatyczne");
    private LocalDate dataOdProperties = LocalDate.parse(p.getProperty("DataOd"),DateTimeFormatter.ofPattern("yyyy-MM-dd")); //dana z pliku naglowek_dane.properties
    private LocalDate dataDoProperties = LocalDate.parse(p.getProperty("DataDo"),DateTimeFormatter.ofPattern("yyyy-MM-dd")); //dana z pliku naglowek_dane.properties

    private LocalDateTime dataWytworzeniaJPK = LocalDateTime.now();
    private LocalDate dataOd = czyDatyAutomatyczne.equals("TAK") ? LocalDate.now().minusMonths(1).withDayOfMonth(1) : dataOdProperties;
    private LocalDate dataDo = czyDatyAutomatyczne.equals("TAK") ? LocalDate.now().minusMonths(1).withDayOfMonth(LocalDate.now().minusMonths(1).lengthOfMonth()) : dataDoProperties;
    private String domyslnyKodWaluty = p.getProperty("DomyslnyKodWaluty","PLN");//"PLN";
    private int kodUrzedu = Integer.parseInt(p.getProperty("KodUrzedu","2433"));//2433;

    private static DateTimeFormatter dataFormat = MainNG.dataFormat;
    private static DateTimeFormatter czasFormat = MainNG.czasFormat;
    private static final int NAGLOWEK_OFFSET = Integer.parseInt(PropertyReader.getInstance().plikWyjsciowyProp.getProperty("naglowekOffset"));

    private String[] naglowekCSV;

    @Deprecated
    public Naglowek(KodFormularza kodFormularza, int wariantFormularza, int celZlozenia, LocalDateTime dataWytworzeniaJPK, LocalDate dataOd, LocalDate dataDo, String domyslnyKodWaluty, int kodUrzedu) {
        this.kodFormularza = kodFormularza;
        this.wariantFormularza = wariantFormularza;
        this.celZlozenia = celZlozenia;
        this.dataWytworzeniaJPK = dataWytworzeniaJPK;
        this.dataOd = dataOd;
        this.dataDo = dataDo;
        this.domyslnyKodWaluty = domyslnyKodWaluty;
        this.kodUrzedu = kodUrzedu;
    }

    public Naglowek() {
        String[] naglowekCSV = {
                kodFormularza.getKodFormularza(),
                kodFormularza.getKodSystemowy(),
                kodFormularza.getWersjaSchemy(),
                String.valueOf(getWariantFormularza()),
                String.valueOf(getCelZlozenia()),
                getDataWytworzeniaJPKFormated(),
                getDataOdFormated(),
                getDataDoFormated(),
                getDomyslnyKodWaluty(),
                String.valueOf(getKodUrzedu())
        };
        System.out.println("Kod Formularza = "+kodFormularza.getKodFormularza());
        System.out.println("Wariant Formularza = "+getWariantFormularza());
        System.out.println("Cel Zlozenia = "+String.valueOf(getCelZlozenia()));
        System.out.println("Data START = "+getDataOdFormated());
        System.out.println("Data STOP = "+getDataDoFormated());
        System.out.println("");
        this.naglowekCSV = naglowekCSV;
    }

    public KodFormularza getKodFormularza() {
        return kodFormularza;
    }

    public void setKodFormularza(KodFormularza kodFormularza) {
        this.kodFormularza = kodFormularza;
    }

    public int getWariantFormularza() {
        return wariantFormularza;
    }

    public void setWariantFormularza(int wariantFormularza) {
        this.wariantFormularza = wariantFormularza;
    }

    public int getCelZlozenia() {
        return celZlozenia;
    }

    public void setCelZlozenia(int celZlozenia) {
        this.celZlozenia = celZlozenia;
    }

    public LocalDateTime getDataWytworzeniaJPK() {
        return dataWytworzeniaJPK;
    }

    public String getDataWytworzeniaJPKFormated() {
       String temp =  dataWytworzeniaJPK.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))+"T"+dataWytworzeniaJPK.format(czasFormat);
       return temp;
    }

    public void setDataWytworzeniaJPK(LocalDateTime dataWytworzeniaJPK) {
        this.dataWytworzeniaJPK = dataWytworzeniaJPK;
    }

    public LocalDate getDataOd() {
        return dataOd;
    }

    public String getDataOdFormated() {
        return dataOd.format(dataFormat);
    }

    public void setDataOd(LocalDate dataOd) {
        this.dataOd = dataOd;
    }

    public LocalDate getDataDo() {
        return dataDo;
    }

    public String getDataDoFormated() {
        return dataDo.format(dataFormat);
    }

    public void setDataDo(LocalDate dataDo) {
        this.dataDo = dataDo;
    }

    public String getDomyslnyKodWaluty() {
        return domyslnyKodWaluty;
    }

    public void setDomyslnyKodWaluty(String domyslnyKodWaluty) {
        this.domyslnyKodWaluty = domyslnyKodWaluty;
    }

    public int getKodUrzedu() {
        return kodUrzedu;
    }

    public void setKodUrzedu(int kodUrzedu) {
        this.kodUrzedu = kodUrzedu;
    }

    public String[] getNaglowekCSV() {
        String[] returnvalue = new String[81];
        for (int i = 0; i < naglowekCSV.length; i++) {
            returnvalue[i+ NAGLOWEK_OFFSET]=naglowekCSV[i];
        }
        return returnvalue;
    }

    public class KodFormularza{
        private String kodFormularza = "JPK_VAT";//"JPK_VAT";
        private String kodSystemowy = "JPK_VAT (2)";//"JPK_VAT (2)";
        private String wersjaSchemy = "1-0";//"1-0";

        public KodFormularza(String kodSystemowy, String wersjaSchemy) {
            this.kodSystemowy = kodSystemowy;
            this.wersjaSchemy = wersjaSchemy;
        }

        public KodFormularza() {
        }

        public String getKodSystemowy() {
            return kodSystemowy;
        }

        public void setKodSystemowy(String kodSystemowy) {
            this.kodSystemowy = kodSystemowy;
        }

        public String getWersjaSchemy() {
            return wersjaSchemy;
        }

        public void setWersjaSchemy(String wersjaSchemy) {
            this.wersjaSchemy = wersjaSchemy;
        }

        public String getKodFormularza() {
            return kodFormularza;
        }

        public void setKodFormularza(String kodFormularza) {
            this.kodFormularza = kodFormularza;
        }
    }
}

