/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.commons.md.asciidoctor;

import net.vpc.commons.md.MdAdmonition;
import net.vpc.commons.md.MdCode;
import net.vpc.commons.md.MdElement;
import net.vpc.commons.md.MdNumberedItem;
import net.vpc.commons.md.MdTitle;
import net.vpc.commons.md.MdUnNumberedItem;
import net.vpc.commons.md.MdSequence;
import net.vpc.commons.md.MdTable;
import net.vpc.commons.md.MdText;
import java.io.PrintStream;
import net.vpc.commons.md.MdColumn;
import net.vpc.commons.md.MdRow;

/**
 *
 * @author vpc
 */
public class AsciiDoctorWriter {

    PrintStream out;

    public AsciiDoctorWriter(PrintStream out) {
        this.out = out;
    }

    public void writeInline(MdElement node) {
        switch (node.getId()) {
            case TEXT: {
                out.print(node.asText().getText());
                return;
            }
            case BOLD: {
                out.print("**");
                writeInline(node.asBold().getContent());
                out.print("**");
                return;
            }
            case CODE: {
                out.print("```");
                out.print(node.asCode().getValue());
                out.print("```");
                return;
            }
            case LINK: {
                out.print("link:");
                out.print(node.asLink().getLinkUrl());
                out.print("[");
                out.print(node.asLink().getLinkTitle());
                out.print("]");
                return;
            }
            case IMAGE: {
                out.print("image:");
                out.print(node.asImage().getImageUrl());
                out.print("[");
                out.print(node.asImage().getImageTitle());
                out.print("]");
                return;
            }
            case SEQ: {
                MdSequence t = node.asSeq();
                for (MdElement mdElement : t.getContent()) {
                    writeInline(mdElement);
                }
                return;
            }
        }
        throw new IllegalArgumentException("Unable to inline");
    }

    public void write(MdElement node) {
        switch (node.getId()) {
            case TITLE1: {
                MdTitle t = (MdTitle) node;
                out.println();
                out.println("= " + t.getValue());
                break;
            }
            case TITLE2: {
                MdTitle t = (MdTitle) node;
                out.println();
                out.println("== " + t.getValue());
                break;
            }
            case TITLE3: {
                MdTitle t = (MdTitle) node;
                out.println();
                out.println("=== " + t.getValue());
                break;
            }
            case TITLE4: {
                MdTitle t = (MdTitle) node;
                out.println();
                out.println("==== " + t.getValue());
                break;
            }
            case TITLE5: {
                MdTitle t = (MdTitle) node;
                out.println();
                out.println("===== " + t.getValue());
                break;
            }
            case TITLE6: {
                MdTitle t = (MdTitle) node;
                out.println();
                out.println("====== " + t.getValue());
                break;
            }
            case UNNUMBRED_ITEM1: {
                MdUnNumberedItem t = (MdUnNumberedItem) node;
                out.println();
                out.print("* ");
                writeInline(t.getValue());
                out.println();
                break;
            }
            case UNNUMBRED_ITEM2: {
                MdUnNumberedItem t = (MdUnNumberedItem) node;
                out.println();
                out.print("** ");
                writeInline(t.getValue());
                out.println();
                break;
            }
            case UNNUMBRED_ITEM3: {
                MdUnNumberedItem t = (MdUnNumberedItem) node;
                out.println();
                out.print("*** ");
                writeInline(t.getValue());
                out.println();
                break;
            }
            case UNNUMBRED_ITEM4: {
                MdUnNumberedItem t = (MdUnNumberedItem) node;
                out.println();
                out.print("**** ");
                writeInline(t.getValue());
                out.println();
                break;
            }
            case UNNUMBRED_ITEM5: {
                MdUnNumberedItem t = (MdUnNumberedItem) node;
                out.println();
                out.print("***** ");
                writeInline(t.getValue());
                out.println();
                break;
            }
            case UNNUMBRED_ITEM6: {
                MdUnNumberedItem t = (MdUnNumberedItem) node;
                out.println();
                out.print("****** ");
                writeInline(t.getValue());
                out.println();
                break;
            }
            case NUMBRED_ITEM1: {
                MdNumberedItem t = (MdNumberedItem) node;
                out.println();
                out.print(". ");
                writeInline(t.getValue());
                out.println();
                break;
            }
            case NUMBRED_ITEM2: {
                MdNumberedItem t = (MdNumberedItem) node;
                out.println();
                out.print(".. ");
                writeInline(t.getValue());
                out.println();
                break;
            }
            case NUMBRED_ITEM3: {
                MdNumberedItem t = (MdNumberedItem) node;
                out.println();
                out.print("... ");
                writeInline(t.getValue());
                out.println();
                break;
            }
            case NUMBRED_ITEM4: {
                MdNumberedItem t = (MdNumberedItem) node;
                out.println();
                out.print(".... ");
                writeInline(t.getValue());
                out.println();
                break;
            }
            case NUMBRED_ITEM5: {
                MdNumberedItem t = (MdNumberedItem) node;
                out.println();
                out.print("..... ");
                writeInline(t.getValue());
                out.println();
                break;
            }
            case NUMBRED_ITEM6: {
                MdNumberedItem t = (MdNumberedItem) node;
                out.println();
                out.print("...... ");
                writeInline(t.getValue());
                out.println();
                break;
            }
            case LINE_SEPARATOR: {
                out.println();
                out.println();
                out.println("'''");
                out.println();
                break;
            }

            case ADMONITION: {
                MdAdmonition t = (MdAdmonition) node;
                out.println();
                out.print(t.getType().toString() + ": ");
                write(t.getContent());
                out.println();
                break;
            }
            case SEQ: {
                MdSequence t = (MdSequence) node;
                for (MdElement mdElement : t.getContent()) {
                    write(mdElement);
                }
                break;
            }
            case CODE: {
                MdCode c = (MdCode) node;
                if (c.isInline()) {
                    out.print("``" + c.getValue() + "``");
                } else {
//                out.println(".app.rb");
                    out.println();
                    out.println("[source," + convertLanguage(c.getLanguage()) + "]");
                    out.println("----");
                    out.println(c.getValue());
                    out.println("----");
                }
                break;
            }
            case TEXT: {
                MdText c = (MdText) node;
                out.println(c.getText());
                break;
            }
            case TABLE: {
                MdTable tab = (MdTable) node;
                out.println();
                out.println("|===");
                for (MdColumn cell : tab.getColumns()) {
                    out.print("|");
                    writeInline(cell.getName());
                    out.print(" ");
                }
                out.println();
                for (MdRow row : tab.getRows()) {
                    out.println();
                    for (MdElement cell : row.getCells()) {
                        out.print("|");
                        writeInline(cell);
                        out.print(" ");
                    }
                    out.println();
                }
                out.println("|===");

                break;
            }
        }
    }

    private static String convertLanguage(String c) {
        switch (c) {
            case "ruby":
            case "rb": {
                return "ruby";
            }
            case "py":
            case "python": {
                return "python";
            }
            case "js":
            case "javascript": {
                return "javascript";
            }
            case "cs":
            case "c#": 
            case "csharp": 
            {
                return "csharp";
            }
        }
        return c;
    }

}
