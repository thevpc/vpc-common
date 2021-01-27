package net.thevpc.common.webxml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.URI;
import java.net.URL;
import java.util.*;

public class WebXmlParser {
    public static WebXml parse(URL url) throws IOException, SAXException, ParserConfigurationException {
        return parse(url, null);
    }

    public static WebXml parse(URL url, WebXmlVisitor visitor) throws IOException, SAXException, ParserConfigurationException {
        InputStream is = null;
        try {
            return parse((is = url.openStream()), visitor);
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    public static WebXml parse(URI uri) throws IOException, SAXException, ParserConfigurationException {
        return parse(uri, null);
    }

    public static WebXml parse(URI uri, WebXmlVisitor visitor) throws IOException, SAXException, ParserConfigurationException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(uri.toString());
        return parse(doc, visitor);
    }

    public static WebXml parse(File file) throws IOException, SAXException, ParserConfigurationException {
        return parse(file, null);
    }

    public static WebXml parse(File file, WebXmlVisitor visitor) throws IOException, SAXException, ParserConfigurationException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(file);
        return parse(doc, visitor);
    }

    public static WebXml parse(InputStream stream) throws IOException, SAXException, ParserConfigurationException {
        return parse(stream, null);
    }

    public static WebXml parse(InputStream stream, WebXmlVisitor visitor) throws IOException, SAXException, ParserConfigurationException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(stream);
        return parse(doc, visitor);
    }

    public static WebXml parse(Document doc) {
        return parse(doc, null);
    }

    public static WebServlet parseServlet(Element elem1, WebXmlVisitor visitor) {
        WebServlet s = new WebServlet();
        if (visitor != null) {
            visitor.visitStartServlet(elem1);
        }
        NodeList rootChildList = elem1.getChildNodes();
        for (int i = 0; i < rootChildList.getLength(); i++) {
            Element cc = toElement(rootChildList.item(i));
            if (cc != null) {
                switch (cc.getTagName()) {
                    case "icon": {
                        s.setIcon(elemToStr(elem1));
                        break;
                    }
                    case "servlet-name": {
                        s.setServletName(elemToStr(elem1));
                        break;
                    }
                    case "display-name": {
                        s.setDisplayName(elemToStr(elem1));
                        break;
                    }
                    case "description": {
                        s.setDescription(elemToStr(elem1));
                        break;
                    }
                    case "servlet-class": {
                        s.setServletClass(elemToStr(elem1));
                        break;
                    }
                    case "jsp-file": {
                        s.setJspFile(elemToStr(elem1));
                        break;
                    }
                    case "init-param": {
                        if(visitor!=null){
                            visitor.visitStartServletParam(elem1,s);
                        }
                        KeyVal keyVal = parseInitParams(elem1);
                        s.getInitParams().put(keyVal.key,keyVal.val);
                        if(visitor!=null){
                            visitor.visitEndServletParam(elem1,s,keyVal.key,keyVal.val);
                        }
                        break;
                    }
                    case "load-on-startup": {
                        s.setLoadOnStartup(Integer.parseInt(elemToStr(elem1)));
                        break;
                    }
                }
            }
        }
        if (visitor != null) {
            visitor.visitEndServlet(elem1, s);
        }
        return s;
    }

    public static WebFilter parseFilter(Element elem1, WebXmlVisitor visitor) {
        WebFilter s = new WebFilter();
        if (visitor != null) {
            visitor.visitStartFilter(elem1);
        }
        NodeList rootChildList = elem1.getChildNodes();
        for (int i = 0; i < rootChildList.getLength(); i++) {
            Element cc = toElement(rootChildList.item(i));
            if (cc != null) {
                switch (cc.getTagName()) {
                    case "icon": {
                        s.setIcon(elemToStr(elem1));
                        break;
                    }
                    case "filter-name": {
                        s.setFilterName(elemToStr(elem1));
                        break;
                    }
                    case "display-name": {
                        s.setDisplayName(elemToStr(elem1));
                        break;
                    }
                    case "description": {
                        s.setDescription(elemToStr(elem1));
                        break;
                    }
                    case "filter-class": {
                        s.setFilterClass(elemToStr(elem1));
                        break;
                    }
                    case "init-param": {
                        if(visitor!=null){
                            visitor.visitStartFilterParam(elem1,s);
                        }
                        KeyVal keyVal = parseInitParams(elem1);
                        s.getInitParams().put(keyVal.key,keyVal.val);
                        if(visitor!=null){
                            visitor.visitEndFilterParam(elem1,s,keyVal.key,keyVal.val);
                        }

                        break;
                    }
                }
            }
        }
        if (visitor != null) {
            visitor.visitEndFilter(elem1, s);
        }
        return s;
    }

    public static WebListener parseListener(Element elem1, WebXmlVisitor visitor) {
        WebListener s = new WebListener();
        if (visitor != null) {
            visitor.visitStartListener(elem1);
        }
        NodeList rootChildList = elem1.getChildNodes();
        for (int i = 0; i < rootChildList.getLength(); i++) {
            Element cc = toElement(rootChildList.item(i));
            if (cc != null) {
                switch (cc.getTagName()) {
                    case "icon": {
                        s.setIcon(elemToStr(elem1));
                        break;
                    }
                    case "display-name": {
                        s.setDisplayName(elemToStr(elem1));
                        break;
                    }
                    case "description": {
                        s.setDescription(elemToStr(elem1));
                        break;
                    }
                    case "listener-class": {
                        s.setListenerClass(elemToStr(elem1));
                        break;
                    }
                }
            }
        }
        if (visitor != null) {
            visitor.visitEndListener(elem1, s);
        }
        return s;
    }

    public static WebServletMapping parseServletMapping(Element elem1, WebXmlVisitor visitor) {
        WebServletMapping s = new WebServletMapping();
        if (visitor != null) {
            visitor.visitStartServletMapping(elem1);
        }
        NodeList rootChildList = elem1.getChildNodes();
        for (int i = 0; i < rootChildList.getLength(); i++) {
            Element cc = toElement(rootChildList.item(i));
            if (cc != null) {
                switch (cc.getTagName()) {
                    case "servlet-name": {
                        s.setServletName(elemToStr(elem1));
                        break;
                    }
                    case "url-pattern": {
                        s.setUrlPattern(elemToStr(elem1));
                        break;
                    }
                }
            }
        }
        if (visitor != null) {
            visitor.visitEndServletMapping(elem1, s);
        }
        return s;
    }

    public static WebSessionConfig parseSessionConfig(Element elem1, WebXmlVisitor visitor) {
        WebSessionConfig s = new WebSessionConfig();
//        if (visitor != null) {
//            visitor.visitStartServletMapping(elem1);
//        }
        NodeList rootChildList = elem1.getChildNodes();
        for (int i = 0; i < rootChildList.getLength(); i++) {
            Element cc = toElement(rootChildList.item(i));
            if (cc != null) {
                switch (cc.getTagName()) {
                    case "session-timeout": {
                        s.setSessionTimeout(Integer.parseInt(elemToStr(elem1)));
                        break;
                    }
                    case "tracking-mode": {
                        s.getTrackingModes().add(elemToStr(elem1));
                        break;
                    }
                }
            }
        }
//        if (visitor != null) {
//            visitor.visitEndServletMapping(elem1, s);
//        }
        return s;
    }

    public static WebFilterMapping parseFilterMapping(Element elem1, WebXmlVisitor visitor) {
        WebFilterMapping s = new WebFilterMapping();
        if (visitor != null) {
            visitor.visitStartFilterMapping(elem1);
        }
        NodeList rootChildList = elem1.getChildNodes();
        for (int i = 0; i < rootChildList.getLength(); i++) {
            Element cc = toElement(rootChildList.item(i));
            if (cc != null) {
                switch (cc.getTagName()) {
                    case "filter-name": {
                        s.setFilterName(elemToStr(elem1));
                        break;
                    }
                    case "url-pattern": {
                        s.setUrlPattern(elemToStr(elem1));
                        break;
                    }
                }
            }
        }
        if (visitor != null) {
            visitor.visitEndFilterMapping(elem1, s);
        }
        return s;
    }

    private static KeyVal parseInitParams(Element elem1) {
        NodeList rootChildList2 = elem1.getChildNodes();
        String pn = "";
        String pv = "";
        for (int j = 0; j < rootChildList2.getLength(); j++) {
            Element cc2 = toElement(rootChildList2.item(j));
            if (cc2 != null) {
                switch (cc2.getTagName()) {
                    case "param-name": {
                        pn = (elemToStr(cc2));
                        break;
                    }
                    case "param-value": {
                        pv = (elemToStr(cc2));
                        break;
                    }
                }
            }
        }
        return new KeyVal(pn, pv);
    }

    public static WebXml parse(Document doc, WebXmlVisitor visitor) {
        WebXml w = new WebXml();
        doc.getDocumentElement().normalize();
        NodeList rootChildList = doc.getDocumentElement().getChildNodes();
        if (visitor != null) {
            visitor.visitStartDocument(doc);
        }
        Map<String, String> props = new LinkedHashMap<>();
        for (int i = 0; i < rootChildList.getLength(); i++) {
            Element elem1 = toElement(rootChildList.item(i));
            if (elem1 != null) {
                switch (elem1.getTagName()) {
                    case "servlet": {
                        w.getServlets().add(parseServlet(elem1, visitor));
                        break;
                    }
                    case "servlet-mapping": {
                        w.getServletMappings().add(parseServletMapping(elem1, visitor));
                        break;
                    }
                    case "filter": {
                        w.getFilters().add(parseFilter(elem1, visitor));
                        break;
                    }
                    case "listener": {
                        w.getListeners().add(parseListener(elem1, visitor));
                        break;
                    }
                    case "filter-mapping": {
                        w.getFilterMappings().add(parseFilterMapping(elem1, visitor));
                        break;
                    }
                    case "context-param": {
                        if(visitor!=null){
                            visitor.visitStartContextParam(elem1);
                        }
                        KeyVal keyVal = parseInitParams(elem1);
                        w.getContextParams().put(keyVal.key,keyVal.val);
                        if(visitor!=null){
                            visitor.visitEndContextParam(elem1,keyVal.key,keyVal.val);
                        }
                        break;
                    }
                    case "session-config": {
                        w.setSessionConfig(parseSessionConfig(elem1, visitor));
                        break;
                    }
                }
            }
        }
        if (visitor != null) {
            visitor.visitEndDocument(doc, w);
        }
        return w;
    }
    private static class KeyVal{
        String key;
        String val;

        public KeyVal(String key, String val) {
            this.key = key;
            this.val = val;
        }
    }

    private static String elemToStr(Element ex) {
        return ex.getTextContent() == null ? "" : ex.getTextContent().trim();
    }

    private static Element toElement(Node n) {
        if (n instanceof Element) {
            return (Element) n;
        }
        return null;
    }

    private static Element toElement(Node n, String name) {
        if (n instanceof Element) {
            if (((Element) n).getTagName().equals(name)) {
                return (Element) n;
            }
        }
        return null;
    }


    public static Element createContextParamElement(Document doc, String k,String v) {
        Element e = doc.createElement("context-param");
        e.appendChild(createNameTextTag(doc,"param-name",k));
        e.appendChild(createNameTextTag(doc,"param-value",v));
        return e;
    }

    public static Element createInitParamElement(Document doc, String k,String v) {
        Element e = doc.createElement("init-param");
        e.appendChild(createNameTextTag(doc,"param-name",k));
        e.appendChild(createNameTextTag(doc,"param-value",v));
        return e;
    }
    public static Element createServletElement(Document doc, WebServlet s) {
        Element serv = doc.createElement("servlet");
        serv.appendChild(createNameTextTag(doc, "servlet-class", s.getServletClass()));
        serv.appendChild(createNameTextTag(doc, "servlet-name", s.getServletName()));
        if (!isEmpty(s.getDescription())) {
            serv.appendChild(createNameTextTag(doc, "description", s.getDescription()));
        }
        if (!isEmpty(s.getDisplayName())) {
            serv.appendChild(createNameTextTag(doc, "display-name", s.getDisplayName()));
        }
        if (!isEmpty(s.getIcon())) {
            serv.appendChild(createNameTextTag(doc, "icon", s.getIcon()));
        }
        if (!isEmpty(s.getJspFile())) {
            serv.appendChild(createNameTextTag(doc, "jsp-file", s.getJspFile()));
        }
        if (s.getLoadOnStartup() != 0) {
            serv.appendChild(createNameTextTag(doc, "load-on-startup", String.valueOf(s.getLoadOnStartup())));
        }
        for (Map.Entry<String, String> entry : s.getInitParams().entrySet()) {
            Element elem = doc.createElement("init-param");
            serv.appendChild(elem);
            elem.appendChild(createNameTextTag(doc, "param-name", entry.getKey()));
            elem.appendChild(createNameTextTag(doc, "param-value", entry.getValue()));
        }
        return serv;
    }

    public static Element createFilterMappingElement(Document doc, WebFilterMapping s) {
        Element elem = doc.createElement("filter-mapping");
        elem.appendChild(createNameTextTag(doc, "filter-name", s.getFilterName()));
        elem.appendChild(createNameTextTag(doc, "url-pattern", s.getUrlPattern()));
        return elem;
    }

    public static Element createServletMappingElement(Document doc, WebServletMapping s) {
        Element elem = doc.createElement("filter-mapping");
        elem.appendChild(createNameTextTag(doc, "servlet-name", s.getServletName()));
        elem.appendChild(createNameTextTag(doc, "url-pattern", s.getUrlPattern()));
        return elem;
    }

    public static Element createListenerElement(Document doc, WebListener s) {
        Element elem = doc.createElement("listener");
        elem.appendChild(createNameTextTag(doc, "listener-name", s.getListenerClass()));
        if (!isEmpty(s.getDescription())) {
            elem.appendChild(createNameTextTag(doc, "description", s.getDescription()));
        }
        if (!isEmpty(s.getDisplayName())) {
            elem.appendChild(createNameTextTag(doc, "display-name", s.getDisplayName()));
        }
        if (!isEmpty(s.getIcon())) {
            elem.appendChild(createNameTextTag(doc, "icon", s.getIcon()));
        }
        return elem;
    }

    public static Element createFilterElement(Document doc, WebFilter s) {
        Element serv = doc.createElement("filter");
        serv.appendChild(createNameTextTag(doc, "filter-class", s.getFilterClass()));
        serv.appendChild(createNameTextTag(doc, "display-name", s.getDisplayName()));
        if (!isEmpty(s.getDescription())) {
            serv.appendChild(createNameTextTag(doc, "description", s.getDescription()));
        }
        if (!isEmpty(s.getIcon())) {
            serv.appendChild(createNameTextTag(doc, "icon", s.getIcon()));
        }
        if (!isEmpty(s.getFilterName())) {
            serv.appendChild(createNameTextTag(doc, "filter-name", s.getFilterName()));
        }
        for (Map.Entry<String, String> entry : s.getInitParams().entrySet()) {
            Element elem = doc.createElement("init-param");
            serv.appendChild(elem);
            elem.appendChild(createNameTextTag(doc, "param-name", entry.getKey()));
            elem.appendChild(createNameTextTag(doc, "param-value", entry.getValue()));
        }
        return serv;
    }

    public static Element createNameTextTag(Document doc, String name, String value) {
        Element elem = doc.createElement(name);
        elem.appendChild(doc.createTextNode(value));
        return elem;
    }

    public static void writeDocument(Document doc, File result) throws TransformerException {
        writeDocument(doc, new StreamResult(result));
    }

    public static void writeDocument(Document doc, Writer result) throws TransformerException {
        writeDocument(doc, new StreamResult(result));
    }

    public static void writeDocument(Document doc, OutputStream result) throws TransformerException {
        writeDocument(doc, new StreamResult(result));
    }

    public static void writeDocument(Document doc, StreamResult result) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        DOMSource source = new DOMSource(doc);
        transformer.transform(source, result);
    }

    public static boolean appendOrReplaceServlet(WebServlet servlet, Element servletElement, Document doc) {
        if (servletElement == null) {
            doc.getDocumentElement().appendChild(createServletElement(doc, servlet));
            return true;
        } else {
            WebServlet old = parseServlet(servletElement, null);
            if (old == null || !old.equals(servlet)) {
                doc.getDocumentElement().replaceChild(createServletElement(doc, servlet),servletElement);
                return true;
            }
            return false;
        }
    }

    public static boolean appendOrReplaceServletMapping(WebServletMapping servlet, Element servletElement, Document doc) {
        if (servletElement == null) {
            doc.getDocumentElement().appendChild(createServletMappingElement(doc, servlet));
            return true;
        } else {
            WebServletMapping old = parseServletMapping(servletElement, null);
            if (old == null || !old.equals(servlet)) {
                doc.getDocumentElement().replaceChild(createServletMappingElement(doc, servlet),servletElement);
                return true;
            }
            return false;
        }
    }

    public static boolean appendOrReplaceFilter(WebFilter filter, Element servletElement, Document doc) {
        if (servletElement == null) {
            doc.getDocumentElement().appendChild(createFilterElement(doc, filter));
            return true;
        } else {
            WebFilter old = parseFilter(servletElement, null);
            if (old == null || !old.equals(filter)) {
                doc.getDocumentElement().replaceChild(createFilterElement(doc, filter),servletElement);
                return true;
            }
            return false;
        }
    }

    public static boolean appendOrReplaceServletMapping(WebFilterMapping filterMapping, Element servletElement, Document doc) {
        if (servletElement == null) {
            doc.getDocumentElement().appendChild(createFilterMappingElement(doc, filterMapping));
            return true;
        } else {
            WebFilterMapping old = parseFilterMapping(servletElement, null);
            if (old == null || !old.equals(filterMapping)) {
                doc.getDocumentElement().replaceChild(createFilterMappingElement(doc, filterMapping),servletElement);
                return true;
            }
            return false;
        }
    }

    public static boolean appendOrReplaceListener(WebListener listener, Element servletElement, Document doc) {
        if (servletElement == null) {
            doc.getDocumentElement().appendChild(createListenerElement(doc, listener));
            return true;
        } else {
            WebListener old = parseListener(servletElement, null);
            if (old == null || !old.equals(listener)) {
                doc.getDocumentElement().replaceChild(createListenerElement(doc, listener),servletElement);
                return true;
            }
            return false;
        }
    }

    private static boolean isEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }
}
