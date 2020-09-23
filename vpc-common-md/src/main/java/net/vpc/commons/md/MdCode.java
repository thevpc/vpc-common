/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.commons.md;

/**
 *
 * @author vpc
 */
public class MdCode extends MdAbstractElement {

    private String language;
    private String value;
    private boolean inline;

    public MdCode(String code, String value, boolean inline) {
        this.language = code;
        this.value = value;
        this.inline = inline;
    }

    public boolean isInline() {
        return inline;
    }

    public String getLanguage() {
        return language;
    }

    public String getValue() {
        return value;
    }

    @Override
    public MdElementType getElementType() {
        return MdElementType.CODE;
    }

    @Override
    public String toString() {
        return "```" + language + "\n"
                + value + "\n"
                + "```";
    }

}
