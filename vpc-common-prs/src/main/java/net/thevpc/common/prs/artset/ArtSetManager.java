/**
 * ====================================================================
 *                        vpc-prs library
 *
 * Pluggable Resources Set is a small library for simplifying
 * plugin based applications
 * 
 * Copyright (C) 2006-2008 Taha BEN SALAH
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 * ====================================================================
 */

package net.thevpc.common.prs.artset;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Properties;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime Dec 15, 2007 4:34:51 AM
 */
public class ArtSetManager {

    private static LinkedHashMap<String, ArtSet> artsets = new LinkedHashMap<String, ArtSet>();
    private static ArtSet current;

    static {
        register(current=NotArtSet.INSTANCE);
    }

    public static void register(ArtSet set) {
        artsets.put(set.getId(), set);
    }

    public static void unregister(ArtSet set) {
        artsets.remove(set.getId());
    }

    public static ArtSet[] getArtSets() {
        return artsets.values().toArray(new ArtSet[artsets.size()]);
    }

    public static void setCurrent(String id) {
        current = artsets.get(id);
        if (current == null) {
            current = NotArtSet.INSTANCE;
        }
    }

    public static ArtSet getCurrent() {
        return current;
    }

    public static ArtSet getArtSet(String id) {
        return artsets.get(id);
    }

    public static ArtSet[] lookupArtSets(URL url, ClassLoader parent,final Object owner) throws IOException {
        final ArrayList<ArtSet> descArray = new ArrayList<ArtSet>();
        final URLClassLoader ucl = new URLClassLoader(new URL[]{url}, ArtSetManager.class.getClassLoader());
        URL iconsetxml = ucl.getResource("META-INF/artsets.xml");
        if (iconsetxml == null) {
            iconsetxml = ucl.getResource("meta-inf/artsets.xml");
        }
        if (iconsetxml == null) {
            throw new NoSuchElementException("missing artsets.xml");
        }
//        final PluggableResourcesClassLoader loader = new PluggableResourcesClassLoader(url, parent, IOUtils.getFileName(new File(url.getFile())));

        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            SAXParser saxParser = factory.newSAXParser();
            InputStream stream = iconsetxml.openStream();
            saxParser.parse(stream, new DefaultHandler() {

                private String qName = null;
                private String id = null;
                private String name = null;
                private String group = null;
                private String urlMask = null;
                private Properties props; 

                public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                    if ("artset".equalsIgnoreCase(qName)) {
                        props=new Properties();
                        for(int i=0;i<attributes.getLength();i++){
                            setUserValue(attributes.getQName(i), attributes.getValue(i));
                        }
                    }else if("property".equalsIgnoreCase(qName)){
                        props.setProperty(attributes.getValue("name"), attributes.getValue("value"));
                    }
                    this.qName = qName;
                }
                private void setUserValue(String _key,String _value){
                    if(_key!=null){
                        _key=_key.toLowerCase();
                        if ("id".equals(_key)) {
                            id = _value;
                        } else if ("name".equalsIgnoreCase(_key)) {
                            name = _value;
                        } else if ("group".equalsIgnoreCase(_key)) {
                            group = _value;
                        } else if ("urlmask".equalsIgnoreCase(_key)) {
                            urlMask = _value;
                        }
                    //}else{
                    //    System.out.println("_value = " + _value);
                    }
                }
                public void characters(char ch[], int start, int length) throws SAXException {
                    setUserValue(qName, new String(ch, start, length).trim());
                }

                public void endElement(String uri, String localName, String qName) throws SAXException {
                    if (qName.equals("artset")) {
                        if(id==null){
                            id="artset-"+Math.random();
                        }
                        if (name == null || name.trim().length() == 0) {
                            name = id;
                        }
                        DefaultArtSet das=new DefaultArtSet(
                                id, name,
                                group,
                                urlMask,ucl,
                                owner);
                        for (Entry<Object, Object> entry : props.entrySet()) {
                            das.setProperty((String)entry.getKey(), (String)entry.getValue());
                        }
                        descArray.add(das);
                    }
                    this.qName = null;
                }
            });
        } catch (Throwable t) {
            throw new InvalidArtSetException(t);
        }
        return descArray.toArray(new ArtSet[descArray.size()]);
    }

    public static void registerArtSets(URL url, ClassLoader parent,Object owner) throws IOException {
        for (ArtSet artSet : lookupArtSets(url,parent,owner)) {
            ArtSetManager.register(artSet);
        }
    }    
}
