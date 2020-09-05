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
public class MdTitle extends MdAbstractElement {

    private String code;
    private MdElementType id;
    private String value;
    private int depth;

    public MdTitle(String code, String value, int depth) {
        this.code = code;
        this.value = value;
        this.depth = depth;
        switch (depth) {
            case 1: {
                id = MdElementType.TITLE1;
                break;
            }
            case 2: {
                id = MdElementType.TITLE2;
                break;
            }
            case 3: {
                id = MdElementType.TITLE3;
                break;
            }
            case 4: {
                id = MdElementType.TITLE4;
                break;
            }
            case 5: {
                id = MdElementType.TITLE5;
                break;
            }
            case 6: {
                id = MdElementType.TITLE6;
                break;
            }
            default: {
                throw new IllegalArgumentException("Unsupported");
            }
        }
    }

    public int getDepth() {
        return depth;
    }

    public String getValue() {
        return value;
    }

    public String getCode() {
        return code;
    }

    @Override
    public MdElementType getId() {
        return id;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(code).append(" ").append(getValue());
        return sb.toString();
    }

}
