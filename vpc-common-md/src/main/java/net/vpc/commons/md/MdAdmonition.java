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
public class MdAdmonition extends MdAbstractElement{

    private String code;
    private MdAdmonitionType type;
    private MdElement content;

    public MdAdmonition(String code, MdAdmonitionType type,MdElement content) {
        this.code = code;
        this.content = content;
        this.type = type;
    }

    public MdAdmonitionType getType() {
        return type;
    }
    

    public String getCode() {
        return code;
    }

    public MdElement getContent() {
        return content;
    }

    @Override
    public MdElementType getElementType() {
        return MdElementType.ADMONITION;
    }

}
