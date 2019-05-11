package com.malucha.deprecated;

import com.malucha.PropertyReader;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.*;
import java.time.LocalDateTime;

//LEARN: https://softwarecave.org/2014/02/15/write-xml-documents-using-streaming-api-for-xml-stax/

//XML based on XSD: https://sanaulla.info/2010/08/29/using-jaxb-to-generate-xml-from-the-java-xsd-2/
@Deprecated
public class MainXML {

    private static final String NS_URI = "http://crd.gov.pl/xml/schematy/dziedzinowe/mf/2016/01/25/eD/DefinicjeTypy/";
    private static String basePath;
    private static String csvSprzedaz;
    private static String csvZakupy;
    private static String xml;
    private static String csvJpkVat;
    static LocalDateTime now;
    static PropertyReader p;

    public static void main2(String[] args) {
//        init zmienne
        now = LocalDateTime.now(); //        System.out.println(now);
        basePath = new File("").getAbsolutePath(); //        System.out.println("ABSOLUTE PATH (where to put files) = "+basePath);
        p = PropertyReader.getInstance();//        System.out.println("plik wyjsciowy path = "+p.plikWyjsciowyProp.getProperty("path"));
//        ustaw sciezki do plikow
        csvSprzedaz = p.plikiWejscioweProp.getProperty("csv_sprzedaz");
        System.out.println("VAT.csv PAHT = " + csvSprzedaz);
        csvZakupy = p.plikiWejscioweProp.getProperty("csv_zakupy");
        System.out.println("VAT-ZAKUPY.csv PAHT = " + csvZakupy);
        xml = p.plikWyjsciowyProp.getProperty("path") + "worksjpk_" + now + ".xml";
        csvJpkVat = p.plikWyjsciowyProp.getProperty("path") + "worksjpk_" + now + ".csv";

//        ustawienia znakow specjalnych w csv
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ";";

//        zapisz naglowek


//        zapisz sprzedaz
        try {

            br = new BufferedReader(new FileReader(csvSprzedaz));
            while ((line = br.readLine()) != null) {

                String[] sales = line.split(cvsSplitBy);

                System.out.println("numer = [" + sales[2] + "]");
                break;

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


//WRITING XML
//        try {
//            writeXML();
//        } catch (XMLStreamException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

}

    static void writeXML() throws XMLStreamException, IOException {


        try (FileOutputStream fos = new FileOutputStream(xml)) {
            XMLOutputFactory xmlOutFact = XMLOutputFactory.newInstance();
            xmlOutFact.setProperty(XMLOutputFactory.IS_REPAIRING_NAMESPACES, true);

            XMLStreamWriter writer = xmlOutFact.createXMLStreamWriter(fos);
            writer.writeStartDocument("UTF-8", "1.0");

            writer.writeStartElement("tns", "JPK", NS_URI);
            writer.writeDefaultNamespace(NS_URI);


            // writeAllRegisters Naglowek
//                    writer.writeStartElement(NS_URI,"tns:Naglowek");
//                  /*      <tns:KodFormularza kodSystemowy="JPK_VAT (2)" wersjaSchemy="1-0">JPK_VAT</tns:KodFormularza>
//                        <tns:WariantFormularza>2</tns:WariantFormularza>
//                        <tns:CelZlozenia>1</tns:CelZlozenia>
//                        <tns:DataWytworzeniaJPK>2017-02-17T09:30:47</tns:DataWytworzeniaJPK>
//                        <tns:DataOd>2017-01-01</tns:DataOd>
//                        <tns:DataDo>2017-01-31</tns:DataDo>
//                        <tns:DomyslnyKodWaluty>PLN</tns:DomyslnyKodWaluty>
//                        <tns:KodUrzedu>0202</tns:KodUrzedu>*/
//                        writer.writeStartElement("KodFormularza");
//                            writer.writeAttribute("kodSystemowy","JPK_VAT (2)");
//                            writer.writeAttribute("wersjaSchemy","1-0");
//                            writer.writeCharacters("JPK_VAT");
//                        writer.writeEndElement();
//                        writer.writeStartElement("WariantFormularza");
//                            writer.writeCharacters("2");
//                        writer.writeEndElement();
//                    writer.writeEndElement();

            writeNaglowek(writer);

            //writeAllRegisters Podmiot1
            writer.writeStartElement("tns:Podmiot1");
            writer.writeEndElement();

            //writeAllRegisters Sprzedaz
            writer.writeStartElement("tns:SprzedazWiersz");
            writer.writeAttribute("typ", "G");
            writer.writeStartElement("tns:LpSprzedazy");
            writer.writeCharacters("AA");
            writer.writeEndElement();
            writer.writeEndElement();

            writer.writeEndElement();
            writer.writeEndDocument();

            writer.flush();
            writer.close();
        }
    }

//    <tns:Naglowek>
//        <tns:KodFormularza kodSystemowy="JPK_VAT (2)" wersjaSchemy="1-0">JPK_VAT</tns:KodFormularza>
//        <tns:WariantFormularza>2</tns:WariantFormularza>
//        <tns:CelZlozenia>1</tns:CelZlozenia>
//        <tns:DataWytworzeniaJPK>2017-02-17T09:30:47</tns:DataWytworzeniaJPK>
//        <tns:DataOd>2017-01-01</tns:DataOd>
//        <tns:DataDo>2017-01-31</tns:DataDo>
//        <tns:DomyslnyKodWaluty>PLN</tns:DomyslnyKodWaluty>
//        <tns:KodUrzedu>0202</tns:KodUrzedu>
//    </tns:Naglowek>
//    <tns:Podmiot1>
//        <tns:IdentyfikatorPodmiotu>
//            <etd:NIP>1111111111</etd:NIP>
//            <etd:PelnaNazwa>ABCDF sp. z o.o.</etd:PelnaNazwa>
//            <etd:REGON>123456789</etd:REGON>
//        </tns:IdentyfikatorPodmiotu>
//        <tns:AdresPodmiotu>
//            <tns:KodKraju>PL</tns:KodKraju>
//            <tns:Wojewodztwo>MAZOWIECKIE</tns:Wojewodztwo>
//            <tns:Powiat>a</tns:Powiat>
//            <tns:Gmina>a</tns:Gmina>
//            <tns:Ulica>YYYYYYY</tns:Ulica>
//            <tns:NrDomu>12</tns:NrDomu>
//            <tns:NrLokalu>2</tns:NrLokalu>
//            <tns:Miejscowosc>WARSZAWA</tns:Miejscowosc>
//            <tns:KodPocztowy>00-000</tns:KodPocztowy>
//            <tns:Poczta>WARSZAWA</tns:Poczta>
//        </tns:AdresPodmiotu>
//    </tns:Podmiot1>

    private static void writeNaglowek(XMLStreamWriter writer) throws XMLStreamException {
        //writer.writeStartElement(NS_URI,"Naglowek");
        writer.writeStartElement("tns", "Naglowek", NS_URI);
                  /*      <tns:KodFormularza kodSystemowy="JPK_VAT (2)" wersjaSchemy="1-0">JPK_VAT</tns:KodFormularza>
                        <tns:WariantFormularza>2</tns:WariantFormularza>
                        <tns:CelZlozenia>1</tns:CelZlozenia>
                        <tns:DataWytworzeniaJPK>2017-02-17T09:30:47</tns:DataWytworzeniaJPK>
                        <tns:DataOd>2017-01-01</tns:DataOd>
                        <tns:DataDo>2017-01-31</tns:DataDo>
                        <tns:DomyslnyKodWaluty>PLN</tns:DomyslnyKodWaluty>
                        <tns:KodUrzedu>0202</tns:KodUrzedu>*/
        writer.writeStartElement("KodFormularza");
        writer.writeAttribute("kodSystemowy", p.naglowekDaneProp.getProperty("kodSystemowy"));
        writer.writeAttribute("wersjaSchemy", p.naglowekDaneProp.getProperty("wersjaSchemy"));
        writer.writeCharacters(p.naglowekDaneProp.getProperty("KodFormularza"));
        writer.writeEndElement();

//            writer.writeStartElement("WariantFormularza");
//            writer.writeCharacters(p.naglowekDaneProp.getProperty("WariantFormularza"));
//            writer.writeEndElement();

        writeSimpleElement(writer, "WariantFormularza", "");


        writer.writeEndElement();
    }

    private static void writeSimpleElement(XMLStreamWriter writer, String localName, String prefix) throws XMLStreamException {
        writer.writeStartElement(prefix, localName, "");
        writer.writeCharacters(p.naglowekDaneProp.getProperty(localName));
        writer.writeEndElement();
    }

}
