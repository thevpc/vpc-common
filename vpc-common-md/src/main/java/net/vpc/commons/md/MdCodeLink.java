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
public class MdCodeLink extends MdAbstractElement {

    private String type;
    private String linkCode;
    public MdCodeLink(String type, String linkCode) {
        this.type = type;
        this.linkCode = linkCode;
    }

    public String getType() {
        return type;
    }

    public String getLinkCode() {
        return linkCode;
    }

    @Override
    public MdElementType getElementType() {
        return MdElementType.CODE_LINK;
    }

    @Override
    public String toString() {
        return "(@code-link:" + linkCode + ")";
    }
}
