/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.commons.md;

/**
 *
 * @author vpc
 */
public class MdBr extends MdAbstractElement {

    private String type;
    public MdBr() {
        this("");
    }

    public MdBr(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    @Override
    public MdElementType getElementType() {
        return MdElementType.LINE_BREAK;
    }

    @Override
    public String toString() {
        return "\n";
    }

}
