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
public class MdText extends MdAbstractElement{
    private String text;

    public MdText(String value) {
        this.text = value;
    }

    public String getText() {
        return text;
    }

    @Override
    public MdElementType getId() {
        return MdElementType.TEXT;
    }

    @Override
    public String toString() {
        return String.valueOf(text);
    }
    
    
    
}
