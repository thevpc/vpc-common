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
public class MdColumn extends MdAbstractElement{

    private MdElement name;
    private MdHorizontalAlign horizontalAlign;

    public MdColumn(MdElement name, MdHorizontalAlign horizontalAlign) {
        this.name = name;
        this.horizontalAlign = horizontalAlign;
    }

    
    public MdElement getName() {
        return name;
    }

    public MdHorizontalAlign getHorizontalAlign() {
        return horizontalAlign;
    }

    @Override
    public MdElementType getId() {
        return MdElementType.COLUMN;
    }

}
