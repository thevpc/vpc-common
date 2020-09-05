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
public abstract class MdAbstractElement implements MdElement {

    @Override
    public MdAdmonition asAdmonition() {
        return (MdAdmonition) this;
    }

    @Override
    public MdBold asBold() {
        return (MdBold) this;
    }

    @Override
    public MdCode asCode() {
        return (MdCode) this;
    }

    @Override
    public MdImage asImage() {
        return (MdImage) this;
    }

    @Override
    public MdLineSeparator asLineSeparator() {
        return (MdLineSeparator) this;
    }

    @Override
    public MdNumberedItem asNumItem() {
        return (MdNumberedItem) this;
    }

    @Override
    public MdUnNumberedItem asUnNumItem() {
        return (MdUnNumberedItem) this;
    }

    @Override
    public MdSequence asSeq() {
        return (MdSequence) this;
    }

    @Override
    public MdColumn asColumn() {
        return (MdColumn) this;
    }

    @Override
    public MdRow asRow() {
        return (MdRow) this;
    }

    @Override
    public MdTable asTable() {
        return (MdTable) this;
    }

    @Override
    public MdText asText() {
        return (MdText) this;
    }

    @Override
    public MdTitle asTitle() {
        return (MdTitle) this;
    }

    @Override
    public MdLink asLink() {
        return (MdLink) this;
    }

    @Override
    public MdXml asXml() {
        return (MdXml) this;
    }

    @Override
    public boolean isText() {
        return this instanceof MdText;
    }

    @Override
    public boolean isXml() {
        return this instanceof MdXml;
    }

    @Override
    public boolean isLink() {
        return this instanceof MdLink;
    }

    @Override
    public boolean isTitle() {
        return this instanceof MdTitle;
    }

    @Override
    public boolean isTable() {
        return this instanceof MdTable;
    }

    @Override
    public boolean isSequence() {
        return this instanceof MdSequence;
    }

    @Override
    public boolean isRow() {
        return this instanceof MdRow;
    }

    @Override
    public boolean isColumn() {
        return this instanceof MdColumn;
    }

    @Override
    public boolean isNumberedItem() {
        return this instanceof MdNumberedItem;
    }

    @Override
    public boolean isUnNumberedItem() {
        return this instanceof MdUnNumberedItem;
    }

    @Override
    public boolean isAdmonition() {
        return this instanceof MdAdmonition;
    }

    @Override
    public boolean isImage() {
        return this instanceof MdImage;
    }

}