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
public class MdUnNumberedItem extends MdAbstractElement {

    private String type;
    private MdElement value;
    private int depth;
    private MdElementType id;
    private MdElement[] children;

    public MdUnNumberedItem(String type, int depth, MdElement value,MdElement[] children) {
        this.type = type;
        this.value = value;
        this.depth = depth;
        this.children = children;
        switch (depth) {
            case 1: {
                id = MdElementType.UNNUMBRED_ITEM1;
                break;
            }
            case 2: {
                id = MdElementType.UNNUMBRED_ITEM2;
                break;
            }
            case 3: {
                id = MdElementType.UNNUMBRED_ITEM3;
                break;
            }
            case 4: {
                id = MdElementType.UNNUMBRED_ITEM4;
                break;
            }
            case 5: {
                id = MdElementType.UNNUMBRED_ITEM5;
                break;
            }
            case 6: {
                id = MdElementType.UNNUMBRED_ITEM6;
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

    public String getType() {
        return type;
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
        sb.append(type).append(" ").append(getValue());
        return sb.toString();
    }
    
    

}
