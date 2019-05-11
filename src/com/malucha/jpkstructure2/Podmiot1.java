package com.malucha.jpkstructure2;

import com.malucha.PropertyReader;

import java.util.Properties;

public class Podmiot1 {
    Properties p = PropertyReader.getInstance().getNaglowekDaneProp();
    private final int ILOSC_KOLUMN_W_PLIKU = Integer.parseInt(PropertyReader.getInstance().plikWyjsciowyProp.getProperty("iloscKolumnWPliku"));

    private IdentyfikatorPodmiotu identyfikatorPodmiotu =new IdentyfikatorPodmiotu();
    private AdresPodmiotu adresPodmiotu = new AdresPodmiotu();

    public Podmiot1() {
    }

    public class IdentyfikatorPodmiotu{
        private long nip = Long.parseLong(p.getProperty("NIP","6480107355")); //Long.parseLong("6480107355");
        private String pelnaNazwa = p.getProperty("PelnaNazwa","Zakład Elektromechaniki Dźwigowej Ryszard Malucha"); //"Zakład Elektromechaniki Dźwigowej Ryszard Malucha"
        private long regon = Long.parseLong(p.getProperty("REGON")); //Long.parseLong("123456789");

        private final int IDENTYFIKATOR_PODMIOTU_OFFSET = Integer.parseInt(PropertyReader.getInstance().plikWyjsciowyProp.getProperty("identyfikatorPodmiotuOffset"));

        private String[] identyfikatorPodmiotuCSV = {
                String.valueOf(nip),
                pelnaNazwa,
                String.valueOf(regon)
        };

        public IdentyfikatorPodmiotu(long nip, String pelnaNazwa) {
            this.nip = nip;
            this.pelnaNazwa = pelnaNazwa;
        }

        public IdentyfikatorPodmiotu() {
        }

        public long getNip() {
            return nip;
        }

        public void setNip(long nip) {
            this.nip = nip;
        }

        public String getPelnaNazwa() {
            return pelnaNazwa;
        }

        public void setPelnaNazwa(String pelnaNazwa) {
            this.pelnaNazwa = pelnaNazwa;
        }

        public long getRegon() {
            return regon;
        }

        public void setRegon(long regon) {
            this.regon = regon;
        }

        public String[] getIdentyfikatorPodmiotuCSV() {
            String[] returnvalue = new String[ILOSC_KOLUMN_W_PLIKU];
            for (int i = 0; i < identyfikatorPodmiotuCSV.length; i++) {
                returnvalue[i+ IDENTYFIKATOR_PODMIOTU_OFFSET]=identyfikatorPodmiotuCSV[i];
            }
            return returnvalue;
        }
    }

    public class AdresPodmiotu{
        private String kodKraju = p.getProperty("KodKraju","PL");
        private String wojewodztwo = p.getProperty("Wojewodztwo","ŚLĄSKIE");
        private String powiat = p.getProperty("Powiat","ZABRZE");
        private String gmina = p.getProperty("Gmina","ZABRZE");
        private String ulica = p.getProperty("Ulica","TATARKIEWICZA");
        private String nrDomu = p.getProperty("NrDomu","2A");
        private String nrLokalu = p.getProperty("NrLokalu","7");
        private String miejscowosc = p.getProperty("Miejscowosc","ZABRZE");
        private String kodPocztowy = p.getProperty("KodPocztowy","41-819");
        private String poczta = p.getProperty("Poczta","ZABRZE");

        private final int ADRES_PODMIOTU_OFFSET = Integer.parseInt(PropertyReader.getInstance().plikWyjsciowyProp.getProperty("adresPodmiotuOffset"));

        String[] adresPodmiotuCSV = {
                kodKraju,
                wojewodztwo,
                powiat,
                gmina,
                ulica,
                nrDomu,
                nrLokalu,
                miejscowosc,
                kodPocztowy,
                poczta
        };

        public AdresPodmiotu() {
        }

        public String getKodKraju() {
            return kodKraju;
        }

        public void setKodKraju(String kodKraju) {
            this.kodKraju = kodKraju;
        }

        public String getWojewodztwo() {
            return wojewodztwo;
        }

        public void setWojewodztwo(String wojewodztwo) {
            this.wojewodztwo = wojewodztwo;
        }

        public String getPowiat() {
            return powiat;
        }

        public void setPowiat(String powiat) {
            this.powiat = powiat;
        }

        public String getGmina() {
            return gmina;
        }

        public void setGmina(String gmina) {
            this.gmina = gmina;
        }

        public String getUlica() {
            return ulica;
        }

        public void setUlica(String ulica) {
            this.ulica = ulica;
        }

        public String getNrDomu() {
            return nrDomu;
        }

        public void setNrDomu(String nrDomu) {
            this.nrDomu = nrDomu;
        }

        public String getNrLokalu() {
            return nrLokalu;
        }

        public void setNrLokalu(String nrLokalu) {
            this.nrLokalu = nrLokalu;
        }

        public String getMiejscowosc() {
            return miejscowosc;
        }

        public void setMiejscowosc(String miejscowosc) {
            this.miejscowosc = miejscowosc;
        }

        public String getKodPocztowy() {
            return kodPocztowy;
        }

        public void setKodPocztowy(String kodPocztowy) {
            this.kodPocztowy = kodPocztowy;
        }

        public String getPoczta() {
            return poczta;
        }

        public void setPoczta(String poczta) {
            this.poczta = poczta;
        }

        public String[] getAdresPodmiotuCSV() {
            String[] returnvalue = new String[ILOSC_KOLUMN_W_PLIKU];
            for (int i = 0; i < adresPodmiotuCSV.length; i++) {
                returnvalue[i+ADRES_PODMIOTU_OFFSET]=adresPodmiotuCSV[i];
            }
            return returnvalue;
        }
    }

    public IdentyfikatorPodmiotu getIdentyfikatorPodmiotu() {
        return identyfikatorPodmiotu;
    }

    public void setIdentyfikatorPodmiotu(IdentyfikatorPodmiotu identyfikatorPodmiotu) {
        this.identyfikatorPodmiotu = identyfikatorPodmiotu;
    }

    public AdresPodmiotu getAdresPodmiotu() {
        return adresPodmiotu;
    }

    public void setAdresPodmiotu(AdresPodmiotu adresPodmiotu) {
        this.adresPodmiotu = adresPodmiotu;
    }
}
