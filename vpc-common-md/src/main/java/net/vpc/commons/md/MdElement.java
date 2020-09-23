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
public interface MdElement {
    MdElementType getElementType();

    MdAdmonition asAdmonition();

    MdBold asBold();

    MdCode asCode();

    MdImage asImage();

    MdLineSeparator asLineSeparator();

    MdNumberedItem asNumItem();

    MdSequence asSeq();

    MdTable asTable();

    MdText asText();

    MdTitle asTitle();

    MdUnNumberedItem asUnNumItem();

    MdLink asLink();

    MdCodeLink asCodeLink();

    MdXml asXml();

    boolean isColumn();

    boolean isLink();

    boolean isRow();

    boolean isSequence();

    boolean isTable();

    boolean isText();

    boolean isCodeLink();

    boolean isTitle();

    boolean isXml();

    MdColumn asColumn();

    MdRow asRow();

    boolean isUnNumberedItem();

    boolean isImage();

    boolean isAdmonition();

    boolean isNumberedItem();

    MdItalic asItalic();

    boolean isBold();

    boolean isItalic();
}
