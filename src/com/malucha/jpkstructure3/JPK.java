package com.malucha.jpkstructure3;

import com.malucha.worksstructure.WorksSprzedaz;
import com.malucha.worksstructure.WorksZakup;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Deprecated
public class JPK {
    private Naglowek naglowek;
    private Podmiot1 podmiot1;
    private SprzedazCtrl sprzedazCtrl;
    private ZakupCtrl zakupCtrl;

    private List<Sprzedaz> sprzedaz;
    private List<Zakup> zakup;


    /**
     * Konstruktor JPK zawiera kod mapujący pliki WORKS SPRZEDAZ i ZAKUPY .csv na strukture JPK.
     * @param worksSprzedazList
     * @param worksZakupList
     */
    public JPK(List<WorksSprzedaz> worksSprzedazList, List<WorksZakup> worksZakupList) {

//      create default Naglowek
        Naglowek jpkN = new Naglowek();
        this.setNaglowek(jpkN);
//      create default Podmiot1
        Podmiot1 jpkP = new Podmiot1();
        this.setPodmiot1(jpkP);

//      create ListSprzedaz
        List<Sprzedaz> jpkS = new ArrayList<>();
//      load ListSprzedaz
        Iterator iterator = worksSprzedazList.iterator();
        while (iterator.hasNext()) {
            WorksSprzedaz input = (WorksSprzedaz) iterator.next();
                /*System.out.println(input.getNumer());
                Sprzedaz(int lpSprzedazy, long nrKontrahenta, String nazwaKontrahenta, String adreskontrahenta, String dowodSprzedazy, LocalDate dataWystawienia, LocalDate dataSprzedazy, float k_17, float k_18, float k_19, float k_20) {} */
            if (input.getDataJpk().isAfter(this.getNaglowek().getDataOd().minusDays(1)) && input.getDataJpk().isBefore(this.getNaglowek().getDataDo().plusDays(1))) {
                jpkS.add(new Sprzedaz(input.getNumer(),
                                input.getNip(),
                                input.getNabywca(),
                                input.getNabywcaAdres(),
                                input.getNumer() + "/" + input.getDataJpk().getYear(),
                                input.getDataJpk(),
                                input.getDataJpk(),
                                input.getNetto8Sum(),
                                input.getVat8Sum(),
                                input.getNetto23Sum(),
                                input.getVat23Sum(),
                                input.getNettoOdwrObciaz() //20181201: added due to new type of invoice which needs to be issued 'Odwrotne Obsciazenie'
                        )
                );
            }
                /*System.out.println(input.getDataJpk());
                System.out.println(deprecated.getNaglowek().getDataOd());
                System.out.println(deprecated.getNaglowek().getDataDo());*/
        }
        this.setSprzedaz(jpkS);

//            create SprzedazCtrl
        SprzedazCtrl jpkSCtrl = new SprzedazCtrl(this.getSprzedaz());
//            load SprzedazCtrl
        jpkSCtrl.calculatePodatekNaleznyAndLiczkaWierszySprzedazy();
        System.out.println("Sprzedaz liczba wierszy = "+jpkSCtrl.getLiczbaWierszySprzedazy());
        System.out.println("Sprzedaz podatek nalezny = "+jpkSCtrl.getPodatekNalezny());
        this.setSprzedazCtrl(jpkSCtrl);

        // create ListZakup
        List<Zakup> jpkZ = new ArrayList<>();
        // load ListZakup
        Iterator iterator1 = worksZakupList.iterator();
        while(iterator1.hasNext()){
            WorksZakup input = (WorksZakup) iterator1.next();
//                if (input.getDataWystFull().isAfter(this.getNaglowek().getDataOd().minusDays(1)) && input.getDataWystFull().isBefore(this.getNaglowek().getDataDo().plusDays(1))) {
            if (input.getDataWpisuFull().isAfter(this.getNaglowek().getDataOd().minusDays(1)) && input.getDataWpisuFull().isBefore(this.getNaglowek().getDataDo().plusDays(1))) {
                jpkZ.add(new Zakup(input.getLp(),
                                Long.parseLong(input.getNip().replaceAll("[^0-9]+", "")),
                                input.getKontrahent(),
                                input.getAdres(),
                                input.getDokument(),
                                input.getDataWystFull(),
                                input.getDataWpisuFull(),
                                input.getSrodkiTrwale().equals("TAK") ? input.getRazemNetto(): BigDecimal.ZERO,
                                input.getSrodkiTrwale().equals("TAK") ? input.getRazemVat():BigDecimal.ZERO,
                                input.getSrodkiTrwale().equals("NIE") ? input.getRazemNetto():BigDecimal.ZERO,
                                input.getSrodkiTrwale().equals("NIE") ? input.getRazemVat():BigDecimal.ZERO
                        )
                );
            }
        }
        this.setZakup(jpkZ);

        // create and load ZakupCtrl
        ZakupCtrl jpkZCtrl = new ZakupCtrl(this.getZakup());
//            load SprzedazCtrl
        jpkZCtrl.calculatePodatekNaleznyAndLiczkaWierszyZakupu();
        System.out.println("Zakup liczba wierszy = "+jpkZCtrl.getLiczbaWierszyZakupu());
        System.out.println("Zakup podatek nalezny = "+jpkZCtrl.getPodatekNalezny());
        this.setZakupCtrl(jpkZCtrl);

    }

    public Naglowek getNaglowek() {
        return naglowek;
    }

    public void setNaglowek(Naglowek naglowek) {
        this.naglowek = naglowek;
    }

    public Podmiot1 getPodmiot1() {
        return podmiot1;
    }

    public void setPodmiot1(Podmiot1 podmiot1) {
        this.podmiot1 = podmiot1;
    }

    public List<Sprzedaz> getSprzedaz() {
        return sprzedaz;
    }

    public void setSprzedaz(List<Sprzedaz> sprzedaz) {
        this.sprzedaz = sprzedaz;
    }

    public List<Zakup> getZakup() {
        return zakup;
    }

    public void setZakup(List<Zakup> zakup) {
        this.zakup = zakup;
    }

    public SprzedazCtrl getSprzedazCtrl() {
        return sprzedazCtrl;
    }

    public void setSprzedazCtrl(SprzedazCtrl sprzedazCtrl) {
        this.sprzedazCtrl = sprzedazCtrl;
    }

    public ZakupCtrl getZakupCtrl() {
        return zakupCtrl;
    }

    public void setZakupCtrl(ZakupCtrl zakupCtrl) {
        this.zakupCtrl = zakupCtrl;
    }

}
