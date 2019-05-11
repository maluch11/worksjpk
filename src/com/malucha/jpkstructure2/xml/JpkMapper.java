package com.malucha.jpkstructure2.xml;

import com.malucha.PropertyReader;
import com.malucha.jpkstructure2.*;
import com.malucha.worksstructure.WorksSprzedaz;
import com.malucha.worksstructure.WorksZakup;
import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import static javax.xml.datatype.DatatypeFactory.newInstance;

public class JpkMapper {
    List<WorksSprzedaz> worksSprzedazList;
    List<WorksZakup> worksZakupList;
    ObjectFactory factory = new ObjectFactory();
    JPK jpk;
    TNaglowek tnaglowek;
    JAXBContext context; //
    Marshaller xmlStructureFromContext;

    public JpkMapper(List<WorksSprzedaz> worksSprzedazList, List<WorksZakup> worksZakupList) {
        this.worksSprzedazList = worksSprzedazList;
        this.worksZakupList = worksZakupList;

        /**

         SprzedazWiersz oraz ZakupWiersz. Obie sekcje są wymagane, co wprowadzać w pliku jeśli w danym miesiącu sprzedaż lub zakup nie występuje.
         W takiej sytuacji niezbędne jest struktury z wierszem o wartości „0”.

         Czy faktury zakupowe skutkujące po stronie nabywcy rozliczeniem VAT należnego (tj. import usług, WNT, import towarów), od których podatnik nie odlicza VAT, nie są one w ogóle wykazywane w części dotyczącej ewidencji zakupu, a jedynie w części ewidencji sprzedaży. Czy w ewidencji sprzedaży powinien być wykazany numer faktury zakupowej z danymi podatnika jako nabywcy, czy numer dokumentu księgowego własnego, na podstawie którego dokonano rozliczenia VAT należnego?
         Z treści art. 109 ust. 3 ustawy o VAT nie wynika obowiązek ujmowania zakupów związanych wyłącznie z czynnościami zwolnionymi, nieopodatkowanymi oraz od których nie przysługuje prawo do odliczenia podatku naliczonego zatem nie podlegają również wykazaniu w JPK_V A T w części dotyczącej zakupów. W ewidencji sprzedaży należy wpisać numer faktury zakupu, na podstawie której podatnik dokonuje rozliczenia podatku należnego.
         W ewidencji sprzedaży powinien być wykazywany numer faktury zakupowej z danymi sprzedawcy – kontrahenta podatnika (jego NIPem), na podstawie którego podatnik będzie dokonywał obliczenia podatku należnego. W przypadku importu towarów istotny dla celów analiz kontrolnych byłby także dokument celny. Przepisy art. 109 ust. 3 ustawy o VAT w brzmieniu obowiązującym od 1 stycznia 2017 r. będą umożliwiać taką interpretację (w ewidencji powinny bowiem znaleźć się również inne dane służące identyfikacji poszczególnych transakcji, w tym numer V A T kontrahenta).

         **/

        {
            try {

                context = JAXBContext.newInstance(JPK.class);

                //xmlStructureFromContext.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
                //javax.xml.bind.annotation.XmlNsForm.QUALIFIED

                jpk = factory.createJPK();
                jpk.setNaglowek(getNaglowekFromProperties());
                jpk.setPodmiot1(getPodmiot1FromProperties());

                jpk.sprzedazWiersz = getSprzedazListFromWorksModel(worksSprzedazList);

                if(jpk.sprzedazWiersz.size() > 0) jpk.setSprzedazCtrl(getCalculatedSprzedazCtrl());

                jpk.zakupWiersz = getZakupListFromWorksModel(worksZakupList);

                if(jpk.zakupWiersz.size() > 0) jpk.setZakupCtrl(getCalculatedZakupCtrl());

                xmlStructureFromContext = context.createMarshaller();
                xmlStructureFromContext.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            } catch (JAXBException e) {
                e.printStackTrace();
            }
        }

    }

    /** Constructing parts of JPK  **/
    private TNaglowek getNaglowekFromProperties(){
        Properties p = PropertyReader.getInstance().getNaglowekDaneProp();
        TNaglowek naglowek = factory.createTNaglowek();

        naglowek.setWariantFormularza(Byte.parseByte(p.getProperty("WariantFormularza")));
        naglowek.setCelZlozenia(Byte.parseByte(p.getProperty("CelZlozenia","1"))); //1;);
        try {
            naglowek.setDataWytworzeniaJPK(newInstance().newXMLGregorianCalendar(LocalDateTime.now().toString()));
        } catch (DatatypeConfigurationException e) {
            e.printStackTrace();
        }
        naglowek.setDataDo(XMLGregorianCalendarImpl.parse(p.getProperty("DataDo")));
        naglowek.setDataOd(XMLGregorianCalendarImpl.parse(p.getProperty("DataOd")));
        naglowek.setDomyslnyKodWaluty(CurrCodeType.PLN);
        naglowek.setKodUrzedu(p.getProperty("KodUrzedu"));


        TNaglowek.KodFormularza kodFormularza;
        kodFormularza = factory.createTNaglowekKodFormularza();
        kodFormularza.setValue(TKodFormularza.JPK_VAT);
        kodFormularza.setKodSystemowy(kodFormularza.getKodSystemowy());
        kodFormularza.setWersjaSchemy(kodFormularza.getWersjaSchemy());
        naglowek.setKodFormularza(kodFormularza);

        return naglowek;
    }

    private JPK.Podmiot1 getPodmiot1FromProperties(){
        Properties p = PropertyReader.getInstance().getNaglowekDaneProp();
        JPK.Podmiot1 podmiot1 = factory.createJPKPodmiot1();
        TIdentyfikatorOsobyNiefizycznej id = factory.createTIdentyfikatorOsobyNiefizycznej();

        id.setNIP(p.getProperty("NIP"));
        id.setPelnaNazwa(p.getProperty("PelnaNazwa"));
        id.setREGON(p.getProperty("REGON"));

        podmiot1.setIdentyfikatorPodmiotu(id);

        TAdresJPK adres = factory.createTAdresJPK();

        adres.setKodKraju(TKodKraju.PL);
        adres.setWojewodztwo(p.getProperty("Wojewodztwo"));
        adres.setPowiat(p.getProperty("Powiat"));
        adres.setGmina(p.getProperty("Gmina"));
        adres.setUlica(p.getProperty("Ulica"));
        adres.setNrDomu(p.getProperty("NrDomu"));
        adres.setNrLokalu(p.getProperty("NrLokalu"));
        adres.setMiejscowosc(p.getProperty("Miejscowosc"));
        adres.setKodPocztowy(p.getProperty("KodPocztowy"));
        adres.setPoczta(p.getProperty("Poczta"));

        podmiot1.setAdresPodmiotu(adres);

        return podmiot1;
    }

    private List<JPK.SprzedazWiersz> getSprzedazListFromWorksModel(List<WorksSprzedaz> worksSprzedazList) {
        List<JPK.SprzedazWiersz> sprzedazWierszJPKList = new ArrayList<JPK.SprzedazWiersz>();

        Iterator iterator = worksSprzedazList.iterator();
        while (iterator.hasNext()) {
            WorksSprzedaz worksSprzedazWiersz = (WorksSprzedaz) iterator.next();
            if (
                    worksSprzedazWiersz.getDataJpk().isAfter(jpk.getNaglowek().getDataOd().toGregorianCalendar().toZonedDateTime().toLocalDate().minusDays(1))
                            && worksSprzedazWiersz.getDataJpk().isBefore(jpk.getNaglowek().getDataDo().toGregorianCalendar().toZonedDateTime().toLocalDate().plusDays(1))
                    )
            {
                //SprzedazWiersz
                JPK.SprzedazWiersz jpkSprzedazWiersz = factory.createJPKSprzedazWiersz();
                jpkSprzedazWiersz.setTyp("G");
                jpkSprzedazWiersz.setLpSprzedazy(BigInteger.valueOf(worksSprzedazWiersz.getNumer()));
                jpkSprzedazWiersz.setNrKontrahenta(String.valueOf(worksSprzedazWiersz.getNip()));
                jpkSprzedazWiersz.setNazwaKontrahenta(worksSprzedazWiersz.getNabywca());
                jpkSprzedazWiersz.setAdresKontrahenta(worksSprzedazWiersz.getNabywcaAdres());
                jpkSprzedazWiersz.setDowodSprzedazy(String.valueOf(worksSprzedazWiersz.getNumer()));
                jpkSprzedazWiersz.setDataWystawienia(toGregorian(worksSprzedazWiersz.getDataJpk()));
                jpkSprzedazWiersz.setDataSprzedazy(toGregorian(worksSprzedazWiersz.getDataJpk()));
                if(!worksSprzedazWiersz.getOdwrotneObciazenie().toUpperCase().equals("TAK")) jpkSprzedazWiersz.setK17(worksSprzedazWiersz.getNetto8Sum()); //8netto
                if(!worksSprzedazWiersz.getOdwrotneObciazenie().toUpperCase().equals("TAK")) jpkSprzedazWiersz.setK18(worksSprzedazWiersz.getVat8Sum()); //8 vat
                if(!worksSprzedazWiersz.getOdwrotneObciazenie().toUpperCase().equals("TAK")) jpkSprzedazWiersz.setK19(worksSprzedazWiersz.getNetto23Sum()); //23 netto
                if(!worksSprzedazWiersz.getOdwrotneObciazenie().toUpperCase().equals("TAK")) jpkSprzedazWiersz.setK20(worksSprzedazWiersz.getVat23Sum()); //23 vat
                if(worksSprzedazWiersz.getOdwrotneObciazenie().toUpperCase().equals("TAK")) jpkSprzedazWiersz.setK34(worksSprzedazWiersz.getNetto8Sum().add(worksSprzedazWiersz.getNetto23Sum())); //odwrotne obciazenie netto
                if(worksSprzedazWiersz.getOdwrotneObciazenie().toUpperCase().equals("TAK")) jpkSprzedazWiersz.setK35(worksSprzedazWiersz.getVat8Sum().add(worksSprzedazWiersz.getVat23Sum())); //odwrotne obciazenie vat
                sprzedazWierszJPKList.add(jpkSprzedazWiersz);
            }
        }
        return sprzedazWierszJPKList;
    }

    private JPK.SprzedazCtrl getCalculatedSprzedazCtrl(){
        JPK.SprzedazCtrl result = factory.createJPKSprzedazCtrl();
        result.setLiczbaWierszySprzedazy(BigInteger.ZERO);
        result.setPodatekNalezny(BigDecimal.ZERO);

        Iterator iterator = jpk.getSprzedazWiersz().iterator();
        while (iterator.hasNext()){
            JPK.SprzedazWiersz sw = (JPK.SprzedazWiersz) iterator.next();
            result.setLiczbaWierszySprzedazy(result.getLiczbaWierszySprzedazy().add(BigInteger.ONE));
            result.setPodatekNalezny(result.getPodatekNalezny()
                    .add(sw.getK18() != null ? sw.getK18() : BigDecimal.ZERO)
                    .add(sw.getK20() != null ? sw.getK20() : BigDecimal.ZERO)
                    .add(sw.getK35() != null ? sw.getK35() : BigDecimal.ZERO)
            );
        }

        return result;
    }

    private List<JPK.ZakupWiersz> getZakupListFromWorksModel(List<WorksZakup> worksZakupList) {
        List<JPK.ZakupWiersz> zakupWierszJpkList = new ArrayList<>();

        Iterator iterator = worksZakupList.iterator();
        while (iterator.hasNext()){
            WorksZakup worksZakupIterator = (WorksZakup) iterator.next();
            if (
                    worksZakupIterator.getDataWpisuFull().isAfter(jpk.getNaglowek().getDataOd().toGregorianCalendar().toZonedDateTime().toLocalDate().minusDays(1))
                            && worksZakupIterator.getDataWpisuFull().isBefore(jpk.getNaglowek().getDataDo().toGregorianCalendar().toZonedDateTime().toLocalDate().plusDays(1))
                    )
            {
                //ZakupWiersz
                JPK.ZakupWiersz zakupWierszJpkTemp = factory.createJPKZakupWiersz();
                zakupWierszJpkTemp.setTyp("G");
                zakupWierszJpkTemp.setLpZakupu(BigInteger.valueOf(worksZakupIterator.getLp()));
                zakupWierszJpkTemp.setNrDostawcy(worksZakupIterator.getNip());
                zakupWierszJpkTemp.setNazwaDostawcy(worksZakupIterator.getKontrahent());
                zakupWierszJpkTemp.setAdresDostawcy(worksZakupIterator.getAdres());
                zakupWierszJpkTemp.setDowodZakupu(worksZakupIterator.getDokument());
                zakupWierszJpkTemp.setDataZakupu(toGregorian(worksZakupIterator.getDataWystFull()));
                zakupWierszJpkTemp.setDataWplywu(toGregorian(worksZakupIterator.getDataWpisuFull())); // DATA WPLYWU JEST OPCJONALNA
                /** NORMALNE FAKTURY ZAKUPOWE **/
                if(!worksZakupIterator.getKorekta().equals("TAK") && worksZakupIterator.getSrodkiTrwale().equals("TAK")) zakupWierszJpkTemp.setK43(worksZakupIterator.getRazemNetto()); //srodki trwale netto
                if(!worksZakupIterator.getKorekta().equals("TAK") && worksZakupIterator.getSrodkiTrwale().equals("TAK")) zakupWierszJpkTemp.setK44(worksZakupIterator.getRazemVat()); //srodki trwale vat
                if(!worksZakupIterator.getKorekta().equals("TAK") && !worksZakupIterator.getSrodkiTrwale().equals("TAK")) zakupWierszJpkTemp.setK45(worksZakupIterator.getRazemNetto()); //nie srodki trwale netto
                if(!worksZakupIterator.getKorekta().equals("TAK") && !worksZakupIterator.getSrodkiTrwale().equals("TAK")) zakupWierszJpkTemp.setK46(worksZakupIterator.getRazemVat()); // nie srodki trwale vat
                /** KOREKTY ZAKUPOWE **/
                if(worksZakupIterator.getKorekta().equals("TAK") && worksZakupIterator.getSrodkiTrwale().equals("TAK")) zakupWierszJpkTemp.setK47(worksZakupIterator.getRazemVat()); //korekta srodki trwale vat
                if(worksZakupIterator.getKorekta().equals("TAK") && !worksZakupIterator.getSrodkiTrwale().equals("TAK")) zakupWierszJpkTemp.setK48(worksZakupIterator.getRazemVat()); //korekta pozostale vat
                //if(worksZakupIterator.getKorekta().equals("TAK") && !worksZakupIterator.getSrodkiTrwale().equals("TAK")) zakupWierszJpkTemp.setK49(worksZakupIterator.getRezemNetto()); //korekta korekta za zle dlugi vat
                //if(worksZakupIterator.getKorekta().equals("TAK") && !worksZakupIterator.getSrodkiTrwale().equals("TAK")) zakupWierszJpkTemp.setK50(worksZakupIterator.getRazemVat()); //korekta wierzytelnosci niesciagalne vat

                zakupWierszJpkList.add(zakupWierszJpkTemp);
            }
        }

        return zakupWierszJpkList;
    }

    private JPK.ZakupCtrl getCalculatedZakupCtrl(){
        JPK.ZakupCtrl result = factory.createJPKZakupCtrl();
        result.setLiczbaWierszyZakupow(BigInteger.ZERO);
        result.setPodatekNaliczony(BigDecimal.ZERO);

        Iterator iterator = jpk.getZakupWiersz().iterator();
        while (iterator.hasNext()){
            JPK.ZakupWiersz sw = (JPK.ZakupWiersz) iterator.next();
            result.setLiczbaWierszyZakupow(result.getLiczbaWierszyZakupow().add(BigInteger.ONE));
            result.setPodatekNaliczony(result.getPodatekNaliczony()
                    .add(sw.getK44() != null ? sw.getK44() : BigDecimal.ZERO)
                    .add(sw.getK46() != null ? sw.getK46() : BigDecimal.ZERO)
                    .add(sw.getK47() != null ? sw.getK47() : BigDecimal.ZERO)
                    .add(sw.getK48() != null ? sw.getK48() : BigDecimal.ZERO)
                    .add(sw.getK49() != null ? sw.getK49() : BigDecimal.ZERO)
                    .add(sw.getK50() != null ? sw.getK50() : BigDecimal.ZERO)
            );
        }

        return result;
    }


    /** Supportive functions **/
    private XMLGregorianCalendar toGregorian(LocalDate local){
        XMLGregorianCalendar xcal = new XMLGregorianCalendarImpl();
        try {
//            GregorianCalendar gcal = GregorianCalendar.from(local.atStartOfDay(ZoneId.systemDefault()));
//            xcal = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcal);
            xcal = DatatypeFactory.newInstance().newXMLGregorianCalendar(local.toString());
        } catch (DatatypeConfigurationException e) {
            e.printStackTrace();
        }


        return xcal;
    }

    /** Getters and setters **/
    public JPK getJpk() {
        return jpk;
    }

    public Marshaller getXmlStructureFromContext() {
        return xmlStructureFromContext;
    }
}
