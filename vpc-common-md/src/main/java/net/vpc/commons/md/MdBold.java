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
public class MdBold extends MdAbstractElement {

    private String type;
    private MdElement content;

    public MdBold(MdElement content) {
        this("**",content);
    }

    public MdBold(String type, MdElement content) {
        this.type = type;
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public MdElement getContent() {
        return content;
    }

    @Override
    public MdElementType getElementType() {
        return MdElementType.BOLD;
    }

    @Override
    public String toString() {
        return "**" + content + "**";
    }

}
