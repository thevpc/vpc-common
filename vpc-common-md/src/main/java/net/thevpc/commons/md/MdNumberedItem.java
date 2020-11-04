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
public class MdNumberedItem extends MdAbstractElement {

    private int number;
    private String sep;
    private MdElement value;
    private int depth;
    private MdElementType id;
    private MdElement[] children;

    public MdNumberedItem(int number, int depth, String sep, MdElement value,MdElement[] children) {
        this.number = number;
        this.sep = sep;
        this.value = value;
        this.depth = depth;
        this.children = children;
        switch (depth) {
            case 1: {
                id = MdElementType.NUMBRED_ITEM1;
                break;
            }
            case 2: {
                id = MdElementType.NUMBRED_ITEM2;
                break;
            }
            case 3: {
                id = MdElementType.NUMBRED_ITEM3;
                break;
            }
            case 4: {
                id = MdElementType.NUMBRED_ITEM4;
                break;
            }
            case 5: {
                id = MdElementType.NUMBRED_ITEM5;
                break;
            }
            case 6: {
                id = MdElementType.NUMBRED_ITEM6;
                break;
            }
            default: {
                throw new IllegalArgumentException("Unsupported");
            }
        }
    }

    public MdElement[] getChildren() {
        return children;
    }

    public int getDepth() {
        return depth;
    }

    public String getSep() {
        return sep;
    }

    public int getNumber() {
        return number;
    }

    public MdElement getValue() {
        return value;
    }

    @Override
    public MdElementType getElementType() {
        return id;
    }

    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();
        sb.append(number).append(sep).append(" ").append(getValue());
        return sb.toString();
    }
    
}
