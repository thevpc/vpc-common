/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.commons.md;

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
            MdElement u = transformElement(parentPath.append(mdElement));
            if (u != null) {
                all.add(u);
            }
        }
        if (all.isEmpty()) {
            return null;
        }
        return all.toArray(new MdElement[0]);
    }

    protected MdColumn[] transformColumns(MdColumn[] e, MdElementPath parentPath) {
        List<MdColumn> all = new ArrayList<>();
        for (MdElement mdElement : e) {
            MdElement c = transformElement(parentPath.append(mdElement));
            if (c != null) {
                MdColumn u = (MdColumn) c;
                all.add(u);
            }
        }
        if (all.isEmpty()) {
            return null;
        }
        return all.toArray(new MdColumn[0]);
    }

    protected MdRow[] transformRows(MdRow[] e, MdElementPath parentPath) {
        List<MdRow> all = new ArrayList<>();
        for (MdElement mdElement : e) {
            MdElement c = transformElement(parentPath.append(mdElement));
            if (c != null) {
                MdRow u = (MdRow) c;
                all.add(u);
            }
        }
        if (all.isEmpty()) {
            return null;
        }
        return all.toArray(new MdRow[0]);
    }

    public MdElement transformDocument(MdElement e) {
        return transformElement(MdElementPath.ROOT.append(e));
    }

    public MdElement transformElement(MdElementPath path) {
        MdElement e = path.getElement();
        if (e == null) {
            return null;
        }
        switch (e.getElementType()) {
            case SEQ: {
                return transformSequence((MdElementPath<MdSequence>) path);
            }
            case XML: {
                return transformXml((MdElementPath<MdXml>) path);
            }
            case TITLE1:
            case TITLE2:
            case TITLE3:
            case TITLE4:
            case TITLE5:
            case TITLE6: {
                return transformTitle((MdElementPath<MdTitle>) path);
            }
            case NUMBRED_ITEM1:
            case NUMBRED_ITEM2:
            case NUMBRED_ITEM3:
            case NUMBRED_ITEM4:
            case NUMBRED_ITEM5:
            case NUMBRED_ITEM6: {
                return transformNumberedItem((MdElementPath<MdNumberedItem>) path);
            }
            case UNNUMBRED_ITEM1:
            case UNNUMBRED_ITEM2:
            case UNNUMBRED_ITEM3:
            case UNNUMBRED_ITEM4:
            case UNNUMBRED_ITEM5:
            case UNNUMBRED_ITEM6: {
                return transformUnNumberedItem((MdElementPath<MdUnNumberedItem>) path);
            }
            case ADMONITION: {
                return transformAdmonition((MdElementPath<MdAdmonition>) path);
            }
            case TEXT: {
                return transformText((MdElementPath<MdText>) path);
            }
            case CODE: {
                return transformCode((MdElementPath<MdCode>) path);
            }
            case LINE_SEPARATOR: {
                return transformLineSeparator((MdElementPath<MdLineSeparator>) path);
            }
            case TABLE: {
                return transformTable((MdElementPath<MdTable>) path);
            }
            case BOLD: {
                return transformBold((MdElementPath<MdBold>) path);
            }
            case ITALIC: {
                return transformItalic((MdElementPath<MdItalic>) path);
            }
            case IMAGE: {
                return transformImage((MdElementPath<MdImage>) path);
            }
            case LINK: {
                return transformURL((MdElementPath<MdLink>) path);
            }
            case COLUMN: {
                return transformColumn((MdElementPath<MdColumn>) path);
            }
            case ROW: {
                return transformRow((MdElementPath<MdRow>) path);
            }
        }
        return e;
    }

    protected MdTable transformTable(MdElementPath<MdTable> path) {
        MdColumn[] h = transformColumns(path.getElement().getColumns(), path);
        MdRow[] r = transformRows(path.getElement().getRows(), path);
        return new MdTable(h, r);
    }

    protected MdElement transformLineSeparator(MdElementPath<MdLineSeparator> path) {
        return path.getElement();
    }

    protected MdElement transformCode(MdElementPath<MdCode> path) {
        return path.getElement();
    }

    protected MdElement transformText(MdElementPath<MdText> path) {
        return path.getElement();
    }

    protected MdElement transformAdmonition(MdElementPath<MdAdmonition> path) {
        return path.getElement();
    }

    protected MdElement transformNumberedItem(MdElementPath<MdNumberedItem> path) {
        MdNumberedItem e = path.getElement();
        return new MdNumberedItem(e.getNumber(), e.getDepth(), e.getSep(), transformElement(path.append(e.getValue())),new MdElement[0]);
    }

    protected MdElement transformUnNumberedItem(MdElementPath<MdUnNumberedItem> path) {
        MdUnNumberedItem e = path.getElement();
        return new MdUnNumberedItem(e.getType(), e.getDepth(), transformElement(path.append(e.getValue())),new MdElement[0]);
    }

    protected MdElement transformTitle(MdElementPath<MdTitle> path) {
        return path.getElement();
    }

    protected MdElement transformXml(MdElementPath<MdXml> path) {
        MdElement r = transformElement(path.append(path.getElement().getContent()));
        return new MdXml(path.getElement().getTag(), path.getElement().getPropertiesString(), r);
    }

    protected MdElement transformSequence(MdElementPath<MdSequence> path) {
        List<MdElement> a = new ArrayList<MdElement>();
        MdSequence e = path.getElement();
        for (MdElement mdElement : e.getElements()) {
            MdElement v = transformElement(path.append(mdElement));
            if (v != null) {
                a.add(v);
            }
        }
        if (a.isEmpty()) {
            return null;
        }
        if (a.size() == 1) {
            return a.get(0);
        }
        return new MdSequence(e.getCode(), a.toArray(new MdElement[0]), e.isInline());
    }

    public MdElement transformBold(MdElementPath<MdBold> path) {
        MdBold e = path.getElement();
        return new MdBold(e.getType(), transformElement(path.append(e.getContent())));
    }

    public MdElement transformItalic(MdElementPath<MdItalic> path) {
        MdItalic e = path.getElement();
        return new MdItalic(e.getType(), transformElement(path.append(e.getContent())));
    }

    protected MdElement transformURL(MdElementPath<MdLink> path) {
        return path.getElement();
    }

    protected MdElement transformImage(MdElementPath<MdImage> path) {
        return path.getElement();
    }

    private MdElement transformRow(MdElementPath<MdRow> path) {
        MdRow e = path.getElement();
        return new MdRow(transformArray(e.getCells(), path), e.isHeader());
    }

    private MdElement transformColumn(MdElementPath<MdColumn> path) {
        return new MdColumn(
                transformElement(path.append(path.getElement().getName())),
                path.getElement().getHorizontalAlign()
        );
    }

}
