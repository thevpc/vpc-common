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
public class MdSequence extends MdAbstractElement {

    private String code;
    private MdElement[] content;
    private boolean inline;

    public MdSequence(String code, MdElement[] content, boolean inline) {
        this.code = code;
        this.content = content;
        this.inline = inline;
    }

    public boolean isInline() {
        return inline;
    }

    public String getCode() {
        return code;
    }

    public MdElement get(int i) {
        return content[i];
    }
    
    public MdElement[] getContent() {
        return content;
    }

    @Override
    public MdElementType getId() {
        return MdElementType.SEQ;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < content.length; i++) {
            if (i > 0) {
                if (!inline) {
                    sb.append("\n");
                }
            }
            sb.append(content[i]);
        }
        return sb.toString();
    }

}
