/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.commons.md;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author vpc
 */
public class MdElementTransformBase {

    protected MdElement[][] transformArray(MdElement[][] e, MdElementPath parentPath) {
        List<MdElement[]> all = new ArrayList<>();
        for (MdElement[] mdElement : e) {
            MdElement[] u = transformArray(mdElement, parentPath);
            if (u != null) {
                all.add(u);
            }
        }
        if (all.size() == 0) {
            return null;
        }
        return all.toArray(new MdElement[0][]);
    }

    protected MdElement[] transformArray(MdElement[] e, MdElementPath parentPath) {
        List<MdElement> all = new ArrayList<>();
        for (MdElement mdElement : e) {
            MdElement u = transformElement(mdElement, parentPath);
            if (u != null) {
                all.add(u);
            }
        }
        if (all.size() == 0) {
            return null;
        }
        return all.toArray(new MdElement[0]);
    }

    protected MdColumn[] transformColumns(MdColumn[] e, MdElementPath parentPath) {
        List<MdElement> all = new ArrayList<>();
        for (MdElement mdElement : e) {
            MdElement c = transformElement(mdElement, parentPath);
            if (c != null) {
                MdElement u = (MdColumn) c;
                all.add(u);
            }
        }
        if (all.size() == 0) {
            return null;
        }
        return all.toArray(new MdColumn[0]);
    }

    protected MdRow[] transformRows(MdRow[] e, MdElementPath parentPath) {
        List<MdRow> all = new ArrayList<>();
        for (MdElement mdElement : e) {
            MdElement c = transformElement(mdElement, parentPath);
            if (c != null) {
                MdRow u = (MdRow) c;
                all.add(u);
            }
        }
        if (all.size() == 0) {
            return null;
        }
        return all.toArray(new MdRow[0]);
    }

    public MdElement transformDocument(MdElement e) {
        return transformElement(e, MdElementPath.ROOT);
    }

    public MdElement transformElement(MdElement e, MdElementPath parentPath) {
        if (e == null) {
            return null;
        }
        switch (e.getId()) {
            case SEQ: {
                return transformSequence((MdSequence) e, parentPath);
            }
            case XML: {
                return transformXml((MdXml) e, parentPath);
            }
            case TITLE1:
            case TITLE2:
            case TITLE3:
            case TITLE4:
            case TITLE5:
            case TITLE6: {
                return transformTitle((MdTitle) e, parentPath);
            }
            case NUMBRED_ITEM1:
            case NUMBRED_ITEM2:
            case NUMBRED_ITEM3:
            case NUMBRED_ITEM4:
            case NUMBRED_ITEM5:
            case NUMBRED_ITEM6: {
                return transformNumberedItem((MdNumberedItem) e, parentPath);
            }
            case UNNUMBRED_ITEM1:
            case UNNUMBRED_ITEM2:
            case UNNUMBRED_ITEM3:
            case UNNUMBRED_ITEM4:
            case UNNUMBRED_ITEM5:
            case UNNUMBRED_ITEM6: {
                return transformUnNumberedItem((MdUnNumberedItem) e, parentPath);
            }
            case ADMONITION: {
                return transformAdmonition((MdAdmonition) e, parentPath);
            }
            case TEXT: {
                return transformText((MdText) e, parentPath);
            }
            case CODE: {
                return transformCode((MdCode) e, parentPath);
            }
            case LINE_SEPARATOR: {
                return transformLineSeparator((MdLineSeparator) e, parentPath);
            }
            case TABLE: {
                return transformTable((MdTable) e, parentPath);
            }
            case BOLD: {
                return transformBold((MdBold) e, parentPath);
            }
            case IMAGE: {
                return transformImage((MdImage) e, parentPath);
            }
            case LINK: {
                return transformURL((MdLink) e, parentPath);
            }
            case COLUMN: {
                return transformColumn((MdColumn) e, parentPath);
            }
            case ROW: {
                return transformRow((MdRow) e, parentPath);
            }
        }
        return e;
    }

    protected MdTable transformTable(MdTable e, MdElementPath parentPath) {
        MdElementPath newParent = parentPath.append(e);
        MdColumn[] h = transformColumns(e.getColumns(), newParent);
        MdRow[] r = transformRows(e.getRows(), newParent);
        return new MdTable(h, r);
    }

    protected MdElement transformLineSeparator(MdLineSeparator e, MdElementPath parentPath) {
        return e;
    }

    protected MdElement transformCode(MdCode e, MdElementPath parentPath) {
        return e;
    }

    protected MdElement transformText(MdText e, MdElementPath parentPath) {
        return e;
    }

    protected MdElement transformAdmonition(MdAdmonition e, MdElementPath parentPath) {
        return e;
    }

    protected MdElement transformNumberedItem(MdNumberedItem e, MdElementPath parentPath) {
        MdElementPath newParent = parentPath.append(e);
        return new MdNumberedItem(e.getNumber(), e.getDepth(), e.getSep(), transformElement(e.getValue(), newParent));
    }

    protected MdElement transformUnNumberedItem(MdUnNumberedItem e, MdElementPath parentPath) {
        MdElementPath newParent = parentPath.append(e);
        return new MdUnNumberedItem(e.getType(), e.getDepth(), transformElement(e.getValue(), newParent));
    }

    protected MdElement transformTitle(MdTitle e, MdElementPath parentPath) {
        return e;
    }

    protected MdElement transformXml(MdXml e, MdElementPath parentPath) {
        MdElementPath newParent = parentPath.append(e);
        MdElement r = transformElement(e.getContent(), newParent);
        return new MdXml(e.getTag(), e.getPropertiesString(), r);
    }

    protected MdElement transformSequence(MdSequence e, MdElementPath parentPath) {
        MdElementPath newParent = parentPath.append(e);
        List<MdElement> a = new ArrayList<MdElement>();
        for (MdElement mdElement : e.getContent()) {
            MdElement v = transformElement(mdElement, newParent);
            if (v != null) {
                a.add(v);
            }
        }
        if (a.size() == 0) {
            return null;
        }
        if (a.size() == 1) {
            return a.get(0);
        }
        return new MdSequence(e.getCode(), a.toArray(new MdElement[0]), e.isInline());
    }

    public MdElement transformBold(MdBold element, MdElementPath parentPath) {
        MdElementPath newParent = parentPath.append(element);
        return new MdBold(element.getType(), transformElement(element.getContent(), newParent));
    }

    protected MdElement transformURL(MdLink element, MdElementPath parent) {
        return element;
    }

    protected MdElement transformImage(MdImage element, MdElementPath parent) {
        return element;
    }

    private MdElement transformRow(MdRow element, MdElementPath parentPath) {
        MdElementPath newParent = parentPath.append(element);
        return new MdRow(transformArray(element.getCells(), parentPath), element.isHeader());
    }

    private MdElement transformColumn(MdColumn element, MdElementPath parentPath) {
        MdElementPath newParent = parentPath.append(element);
        return new MdColumn(
                transformElement(element.getName(), newParent),
                element.getHorizontalAlign()
        );
    }

}
