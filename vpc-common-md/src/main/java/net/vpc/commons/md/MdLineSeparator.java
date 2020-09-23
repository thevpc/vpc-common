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
public class MdLineSeparator extends MdAbstractElement{
    private String code;
    private String value;

    public MdLineSeparator(String id, String value) {
        this.code = id;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String getCode() {
        return code;
    }

    @Override
    public MdElementType getElementType() {
        return MdElementType.LINE_SEPARATOR;
    }
    
    
}
