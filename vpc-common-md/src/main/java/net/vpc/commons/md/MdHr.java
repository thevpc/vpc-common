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
public class MdHr extends MdAbstractElement {

    private String type;
    public MdHr() {
        this("");
    }

    public MdHr(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    @Override
    public MdElementType getElementType() {
        return MdElementType.HORIZONTAL_RULE;
    }

    @Override
    public String toString() {
        return "\n";
    }

}
