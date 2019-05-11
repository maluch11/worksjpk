package com.malucha.jpkstructure2;

import com.malucha.PropertyReader;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Iterator;
import java.util.List;

public class ZakupCtrl{
    private int liczbaWierszyZakupu = 0;
    private BigDecimal podatekNalezny = BigDecimal.ZERO;
    private List<Zakup> zakup;

    private final int ZAKUP_CTRL_OFFSET = Integer.parseInt(PropertyReader.getInstance().plikWyjsciowyProp.getProperty("zakupCtrlOffset"));
    private final int ILOSC_KOLUMN_W_PLIKU = Integer.parseInt(PropertyReader.getInstance().plikWyjsciowyProp.getProperty("iloscKolumnWPliku"));

    private String[] zakupCtrlCSV;

    public ZakupCtrl(List<Zakup> zakup) {
        this.zakup = zakup;
    }

    public ZakupCtrl() {
    }

    public int getLiczbaWierszyZakupu() {
        return liczbaWierszyZakupu;
    }

    public void setLiczbaWierszyZakupu(int liczbaWierszyZakupu) {
        this.liczbaWierszyZakupu = liczbaWierszyZakupu;
    }

    public BigDecimal getPodatekNalezny() {
        return podatekNalezny;
    }

    public void setPodatekNalezny(BigDecimal podatekNalezny) {
        this.podatekNalezny = podatekNalezny;
    }

    public void calculatePodatekNaleznyAndLiczkaWierszyZakupu() {
        setPodatekNalezny(BigDecimal.ZERO);
        Iterator iterator = this.zakup.iterator();
        while(iterator.hasNext()){
            Zakup element = (Zakup) iterator.next();
            this.podatekNalezny = this.podatekNalezny.add(element.getK_44()).add(element.getK_46()).add(element.getK_47()).add(element.getK_48()).add(element.getK_49()).add(element.getK_50());
            this.liczbaWierszyZakupu++;
        }
    }

    public String[] getZakupCtrlCSV() {
        String[] zakupCtrlCSV = {
                String.valueOf(liczbaWierszyZakupu),
                String.valueOf(podatekNalezny.setScale(2, RoundingMode.HALF_UP))
        };
        this.zakupCtrlCSV = zakupCtrlCSV;

        String[] returnvalue = new String[ILOSC_KOLUMN_W_PLIKU];
        for (int i = 0; i < zakupCtrlCSV.length; i++) {
            returnvalue[i+ ZAKUP_CTRL_OFFSET]=zakupCtrlCSV[i];
        }
        return returnvalue;
    }

}
