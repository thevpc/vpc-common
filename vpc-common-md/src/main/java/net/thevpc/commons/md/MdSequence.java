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
public class MdSequence extends MdAbstractElement {

    private String code;
    private MdElement[] elements;
    private boolean inline;

    public MdSequence(String code, MdElement[] content, boolean inline) {
        this.code = code;
        this.elements = content;
//        if (inline) {
//            for (MdElement mdElement : content) {
//                if (mdElement.toString().startsWith("##")) {
//                    System.out.println("Why");
//                }
//            }
//        }
        this.inline = inline;
    }

    public boolean isInline() {
        return inline;
    }

    public String getCode() {
        return code;
    }

    public MdElement get(int i) {
        return elements[i];
    }

    public int size() {
        return elements.length;
    }

    public MdElement[] getElements() {
        return elements;
    }

    @Override
    public MdElementType getElementType() {
        return MdElementType.SEQ;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < elements.length; i++) {
            if (i > 0) {
                if (!inline) {
                    sb.append("\n");
                }
            }
            sb.append(elements[i]);
        }
        return sb.toString();
    }

}
