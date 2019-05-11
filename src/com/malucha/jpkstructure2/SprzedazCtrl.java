package com.malucha.jpkstructure2;

import com.malucha.PropertyReader;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Iterator;
import java.util.List;

public class SprzedazCtrl{
    private int liczbaWierszySprzedazy = 0;
    private BigDecimal podatekNalezny = BigDecimal.ZERO;
    private List<Sprzedaz> sprzedaz;

    private final int SPRZEDAZ_CTRL_OFFSET = Integer.parseInt(PropertyReader.getInstance().plikWyjsciowyProp.getProperty("sprzedazCtrlOffset"));
    private final int ILOSC_KOLUMN_W_PLIKU = Integer.parseInt(PropertyReader.getInstance().plikWyjsciowyProp.getProperty("iloscKolumnWPliku"));

    private String[] sprzedazCtrlCSV;

    public SprzedazCtrl(List<Sprzedaz> sprzedaz) {
        this.sprzedaz = sprzedaz;
    }

    public int getLiczbaWierszySprzedazy() {
        return liczbaWierszySprzedazy;
    }

    public void setLiczbaWierszySprzedazy(int liczbaWierszySprzedazy) {
        this.liczbaWierszySprzedazy = liczbaWierszySprzedazy;
    }

    public BigDecimal getPodatekNalezny() {
        return podatekNalezny;
    }

    public void setPodatekNalezny(BigDecimal podatekNalezny) {
        this.podatekNalezny = podatekNalezny;
    }

    public void calculatePodatekNaleznyAndLiczkaWierszySprzedazy() {
        setPodatekNalezny(BigDecimal.ZERO);
        Iterator iterator = this.sprzedaz.iterator();
        while(iterator.hasNext()){
            Sprzedaz element = (Sprzedaz) iterator.next();
            this.podatekNalezny = this.podatekNalezny.add(element.getK_16());
            this.podatekNalezny = this.podatekNalezny.add(element.getK_18());
            this.podatekNalezny = this.podatekNalezny.add(element.getK_20());
            this.liczbaWierszySprzedazy++;
        }
    }

    public String[] getSprzedazCtrlCSV() {
        String[] sprzedazCtrlCSV = {
                String.valueOf(liczbaWierszySprzedazy),
                String.valueOf(podatekNalezny.setScale(2, RoundingMode.HALF_UP))
        };
        this.sprzedazCtrlCSV = sprzedazCtrlCSV;

        String[] returnvalue = new String[ILOSC_KOLUMN_W_PLIKU];
        for (int i = 0; i < sprzedazCtrlCSV.length; i++) {
            returnvalue[i+ SPRZEDAZ_CTRL_OFFSET]=sprzedazCtrlCSV[i];
        }
        return returnvalue;
    }

}
