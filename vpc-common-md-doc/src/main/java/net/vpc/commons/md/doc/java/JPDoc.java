/**
 * ====================================================================
 *            Nuts : Network Updatable Things Service
 *                  (universal package manager)
 * <br>
 * is a new Open Source Package Manager to help install packages
 * and libraries for runtime execution. Nuts is the ultimate companion for
 * maven (and other build managers) as it helps installing all package
 * dependencies at runtime. Nuts is not tied to java and is a good choice
 * to share shell scripts and other 'things' . Its based on an extensible
 * architecture to help supporting a large range of sub managers / repositories.
 * <br>
 * Copyright (C) 2016-2020 thevpc
 * <br>
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 * <br>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <br>
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 * ====================================================================
 */
package net.vpc.commons.md.doc.java;

import net.vpc.commons.md.MdElement;
import net.vpc.commons.md.doc.util.DocReader;
import com.github.javaparser.javadoc.Javadoc;
import com.github.javaparser.javadoc.JavadocBlockTag;
import com.github.javaparser.javadoc.description.JavadocDescriptionElement;
import net.vpc.commons.md.doc.JDDoc;

/**
 *
 * @author vpc
 */
public class JPDoc implements JDDoc {

    Javadoc jd;

    public JPDoc(Javadoc jd) {
        this.jd = jd;
    }

    @Override
    public String getTag(String tag) {
        for (JavadocBlockTag blockTag : jd.getBlockTags()) {
            if (blockTag.getTagName().equals(tag)) {
                return blockTag.getContent().toText().trim();
            }
        }
        return null;
    }

    @Override
    public MdElement getDescription() {
        if (jd.getDescription() == null) {
            return null;
        }
        DocReader dr = new DocReader();
        for (JavadocDescriptionElement element : jd.getDescription().getElements()) {
            dr.add(element);
        }
        return dr.parse();
//        return new JPDocElementList(jd.getDescription().getElements().stream().map(x -> _JDDocElement(x))
//                .toArray(JDDocElement[]::new));
    }

//    private static JDDocElement _JDDocElement(JavadocDescriptionElement e) {
//        if (e instanceof JavadocSnippet) {
//            return _JDDocElement(e.toText());
//        }
//        if (e instanceof JavadocInlineTag) {
//            return new JPDocElementTag((JavadocInlineTag) e);
//        }
//        throw new IllegalArgumentException("Unsupported " + e);
//    }
//    public static JDDocElement _JDDocElement(String str) {
//        List<JDDocElement> all = new ArrayList<JDDocElement>();
//        CharReader in = new CharReader(str);
//        while (!in.isEmpty()) {
//            JDDocElement a = readAny(in);
//            if (a == null) {
//                System.err.println("Unable to read");
//                break;
//            } else {
//                all.add(a);
//            }
//        }
//        if (all.size() == 0) {
//            return new JPDocElementString("");
//        }
//        if (all.size() == 1) {
//            return all.get(0);
//        }
//        return new JPDocElementList(all.toArray(new JDDocElement[0]));
//    }



//    private static String readHtmlTagEnd(StringBuilder in, String name) {
//        StringBuilder sb = new StringBuilder();
//        int y = name.length() + 3;
//        String end = "</" + name + ">";
//        while (in.length() > y) {
//            if (in.substring(0, y).equals(end)) {
//                in.delete(0, y);
//                return sb.toString();
//            }
//            sb.append(in.charAt(0));
//            in.delete(0, 1);
//        }
//        while (in.length() > 0) {
//            sb.append(in.charAt(0));
//            in.delete(0, 1);
//        }
//        return sb.toString();
//    }

    /*
    
    for (String line : c.getRawCommentText().split("\n")) {
            line = line.trim();
            if (line.startsWith("@category ")) {
                String cat = line.substring("@category ".length()).trim();
                if (cat.length() > 0) {
                    System.out.println("found " + cat);
                    return cat;
                }
            }
        }
        return "Other";
    
     */
}
