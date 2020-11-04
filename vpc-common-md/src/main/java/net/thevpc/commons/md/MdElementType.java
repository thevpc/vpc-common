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
public enum MdElementType {
    TITLE1,
    TITLE2,
    TITLE3,
    TITLE4,
    TITLE5,
    TITLE6,
    TABLE,
    COLUMN,
    ROW,
    XML,
    SEQ,
    TEXT,
    BOLD,
    ITALIC,
    UNNUMBRED_ITEM1,
    UNNUMBRED_ITEM2,
    UNNUMBRED_ITEM3,
    UNNUMBRED_ITEM4,
    UNNUMBRED_ITEM5,
    UNNUMBRED_ITEM6,
    NUMBRED_ITEM1,
    NUMBRED_ITEM2,
    NUMBRED_ITEM3,
    NUMBRED_ITEM4,
    NUMBRED_ITEM5,
    NUMBRED_ITEM6,
    CODE,
    ADMONITION,
    LINE_SEPARATOR,
    LINK,
    CODE_LINK,
    LINE_BREAK,
    HORIZONTAL_RULE,
    IMAGE;

    public static MdElementType title(int depth) {
        return values()[depth - 1 + TITLE1.ordinal()];
    }

    public static MdElementType unnumberedItem(int depth) {
        return values()[depth - 1 + UNNUMBRED_ITEM1.ordinal()];
    }

    public static MdElementType numberedItem(int depth) {
        return values()[depth - 1 + NUMBRED_ITEM1.ordinal()];
    }
}
