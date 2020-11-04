/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.commons.md;

import java.util.Map;

/**
 *
 * @author vpc
 */
public class MdXml extends MdAbstractElement {

    private String tag;
    private String propertiesString;
    private MdElement content;

    public MdXml(String tag, String properties, MdElement content) {
        this.tag = tag;
        this.propertiesString = properties;
        this.content = content;
    }

    @Override
    public MdElementType getElementType() {
        return MdElementType.XML;
    }

    public String getTag() {
        return tag;
    }

    public Map<String,String> getProperties() {
        return new PropertiesParser(propertiesString).parseMap();
    }
    public String getPropertiesString() {
        return propertiesString;
    }

    public MdElement getContent() {
        return content;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("<").append(tag);
        if (propertiesString != null) {
            sb.append(" ").append(propertiesString);
        }
        sb.append(">");
        sb.append(content);
        sb.append("</").append(tag);
        sb.append(">");
        return sb.toString();
    }

}
