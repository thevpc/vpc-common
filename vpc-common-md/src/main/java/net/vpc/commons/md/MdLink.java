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
public class MdLink extends MdAbstractElement {

    private String type;
    private String linkTitle;
    private String linkUrl;

    public MdLink(String type, String linkTitle, String linkUrl) {
        this.type = type;
        this.linkTitle = linkTitle;
        this.linkUrl = linkUrl;
    }

    public String getType() {
        return type;
    }

    public String getLinkTitle() {
        return linkTitle;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    @Override
    public MdElementType getElementType() {
        return MdElementType.LINK;
    }

    @Override
    public String toString() {
        return "[" + linkTitle + "](" + linkUrl + ")";
    }
}
