package com.malucha;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class PropertyReader {

    private static PropertyReader ourInstance = new PropertyReader();
    public static PropertyReader getInstance() {
        return ourInstance;
    }

    public Properties plikiWejscioweProp = new Properties();
    public Properties naglowekDaneProp = new Properties();
    public Properties plikWyjsciowyProp = new Properties();

    private InputStreamReader input = null;

    public PropertyReader(){
        String sy = System.getProperty("os.name");
        System.err.println("system = "+sy);

        String PATH = new File("").getAbsolutePath()+(sy.contains("Mac")?"/CONFIG/":"\\CONFIG\\");
        System.out.println("PropertyReader PATH = "+PATH);

        loadPropertyFile(plikiWejscioweProp,PATH+ "pliki_wejsciowe.properties");
        loadPropertyFile(naglowekDaneProp, PATH+ "naglowek_dane.properties");
        loadPropertyFile(plikWyjsciowyProp, PATH+ "plik_wyjsciowy.properties");
    }

    private void loadPropertyFile(Properties propName, String fileName){
        try {
            //input = new FileInputStream(fileName);
            input = new InputStreamReader(new FileInputStream(fileName), StandardCharsets.UTF_8);
            propName.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Properties getPlikiWejscioweProp() {
        return plikiWejscioweProp;
    }

    public Properties getNaglowekDaneProp() {
        return naglowekDaneProp;
    }

    public Properties getPlikWyjsciowyProp() {
        return plikWyjsciowyProp;
    }

//    public static String utf8(String latin1){
//            return utf8StandardCharset(latin1);
//    }


//    private static String utf8Charset(String latin1){
//        byte[] latin = latin1.getBytes(Charset.forName("ISO-8859-1"));
//        //System.out.println(latin);
//        String utf8 = new String(latin, Charset.forName("UTF-8"));
//        //System.out.println(utf8);
//        return utf8;
//    }
//
//    private static String utf8StandardCharset(String latin1){
//        byte[] latin = latin1.getBytes(StandardCharsets.ISO_8859_1);
//        //System.out.println(latin);
//        String utf8 = new String(latin, StandardCharsets.UTF_8);
//        //System.out.println(utf8);
//        return utf8;
//    }

}


