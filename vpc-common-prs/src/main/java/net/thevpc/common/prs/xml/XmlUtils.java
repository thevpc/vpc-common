package net.thevpc.common.prs.xml;

import java.io.*;
import java.util.Map;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * @creationtime  13 juil. 2006 22:14:21
 */
public final class XmlUtils {
//    private static XmlSerializer defaultSerializer;
//
//    public static XmlSerializer getDefaultSerializer() {
//        if (defaultSerializer == null) {
//            defaultSerializer = new DefaultXmlSerializer();
//        }
//        return defaultSerializer;
//    }

    public static String plainTextToXML(String str){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++){
            char c=str.charAt(i);
            // do not use Character.isLetterOrDigit(str.charAt(i)) || str.charAt(i)==' '
            if((c>='a' && c<='z') || (c>='A' && c<='Z') || (c>='0' && c<='9')){
                sb.append(str.charAt(i));
            }else{
                sb.append("&#").append((int) str.charAt(i)).append(";");
            }
        }
        return sb.toString();
    }

    public static void plainTextToXML(InputStream in,Writer out) throws IOException{
        int  ci=-1;
        while((ci=in.read())!=-1){
            char c=(char) ci;
            // do not use Character.isLetterOrDigit(str.charAt(i)) || str.charAt(i)==' '
            if((c>='a' && c<='z') || (c>='A' && c<='Z') || (c>='0' && c<='9')){
                out.write(c);
            }else{
                out.write("&#" + ci + ";");
            }
        }
    }

    public static void xmlToPlainText(InputStream in,Writer out) throws IOException{
        int  ci=-1;
        while((ci=in.read())!=-1){
            char c=(char) ci;
            if(c=='<'){
                // ignore tags
                StringBuffer tag=new StringBuffer();
                while((ci=in.read())!=-1){
                    c=(char) ci;
                    tag.append(c);
                    if(c=='>'){
                        break;
                    }
                }
//                if(tag.toString().equalsIgnoreCase("br")){
//                    out.write(System.getProperty("line.separator") );
//                }
                if(c<0){
                    break;
                }
            }else if(c=='&'){
                StringBuffer sbChar=new StringBuffer();
                while((ci=in.read())!=-1){
                    c=(char) ci;
                    if(c==';'){
                        break;
                    }else{
                      sbChar.append(c);
                    }
                }
                String theChar=sbChar.toString();
                if(theChar.charAt(0)=='#'){
                    out.write((char) Integer.parseInt(theChar.substring(1)));
                }
                continue;
            }else{
                out.write(c);
            }
        }
    }

    public static String xmlToPlainText(String str){
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < str.length(); i++){
            char c=str.charAt(i);
            if(c=='<'){
                i++;
                // ignore tags
                while((c=str.charAt(i))!='>'){
                    i++;
                }
                continue;
            }else if(c=='&'){
                StringBuffer sbChar=new StringBuffer();
                i++;
                while((c=str.charAt(i))!=';'){
                    sbChar.append(c);
                    i++;
                }
                String theChar=sbChar.toString();
                if(theChar.charAt(0)=='#'){
                    sb.append((char) Integer.parseInt(theChar.substring(1)));
                }
                continue;
            }else{
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static Object xmlToObject(File file,ClassLoader classLoader,Map<Class,XmlSerializationDelegate> delegates) throws IOException {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            return xmlToObject(fis,classLoader, delegates);
        } finally {
            if (fis != null) {
                fis.close();
            }
        }
    }

    public static Object xmlToObject(InputStream inputStream,ClassLoader classLoader, Map<Class,XmlSerializationDelegate> delegates) throws IOException {
        DefaultXmlSerializer d=new DefaultXmlSerializer();
        if(delegates!=null){
            for (Map.Entry<Class, XmlSerializationDelegate> entry : delegates.entrySet()) {
                d.setHandler(entry.getKey(),entry.getValue());
            }
        }
        d.setDefaultClassLoader(classLoader);
        try {
            return d.xmlToObject(inputStream);
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

 public static void objectToXml(Object o, File file,ClassLoader classLoader, Map<Class,XmlSerializationDelegate> delegates) throws IOException {
        FileOutputStream fw = null;
        try {
            fw = new FileOutputStream(file);
            objectToXml(o, fw,classLoader, delegates);
        } finally {
            if (fw != null) {
                fw.close();
            }
        }
    }

    public static void objectToXml(Object o,OutputStream outputStream,ClassLoader classLoader, Map<Class,XmlSerializationDelegate> delegates) throws IOException {
        objectToXml(o, outputStream, classLoader, delegates,true);
    }
    public static void objectToXml(Object o,OutputStream outputStream,ClassLoader classLoader, Map<Class,XmlSerializationDelegate> delegates,boolean closeStream) throws IOException {

        DefaultXmlSerializer d=new DefaultXmlSerializer();
        if(delegates!=null){
            for (Map.Entry<Class, XmlSerializationDelegate> entry : delegates.entrySet()) {
                d.setHandler(entry.getKey(),entry.getValue());
            }
        }
        d.setDefaultClassLoader(classLoader);
        try {
            d.objectToXml(o,outputStream);
        } finally {
            if (outputStream != null && closeStream) {
                outputStream.close();
            }
        }
    }

    public static String tagToString(String name,Map props,boolean startSlash,boolean endSlash){
        StringBuilder sb=new StringBuilder("<");
        if(startSlash){
            sb.append("/");
        }
        sb.append(name);
        if(props!=null){
            for (Object o : props.entrySet()) {
                Map.Entry entry = (Map.Entry) o;
                String k = (String) entry.getKey();
                String v = (String) entry.getValue();
                sb.append(" ");
                sb.append(k);
                sb.append("=");
                sb.append("\"");
                sb.append(plainTextToXML(v));
                sb.append("\"");
            }
        }
        if(endSlash){
            sb.append("/");
        }
        sb.append(">");
        return sb.toString();
    }

    private XmlUtils() {
    }

}
