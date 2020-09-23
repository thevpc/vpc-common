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

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.javadoc.Javadoc;
import com.github.javaparser.javadoc.JavadocBlockTag;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import net.vpc.commons.md.doc.JDConstructorDoc;
import net.vpc.commons.md.doc.JDDoc;
import net.vpc.commons.md.doc.JDParameter;

/**
 *
 * @author vpc
 */
public class JPConstructorDoc implements JDConstructorDoc {

    private ConstructorDeclaration declaration;
    private JPClassDoc cls;

    public JPConstructorDoc(ConstructorDeclaration declaration, JPClassDoc cls) {
        this.declaration = declaration;
        this.cls = cls;
    }

    @Override
    public JDParameter[] parameters() {
        List<JDParameter> param = new ArrayList<>();
        for (Parameter parameter : declaration.getParameters()) {
            String n = parameter.getName().toString();
            String javadocContent = null;
            Javadoc jd = declaration.getJavadoc().orElse(null);
            if (jd != null) {
                for (JavadocBlockTag blockTag : jd.getBlockTags()) {
                    if (blockTag.getType() == JavadocBlockTag.Type.PARAM && blockTag.getName().orElse("").equals(n)) {
                        javadocContent = blockTag.getContent().toText();
                    }
                }
            }
            param.add(new JPParameter(parameter, javadocContent));
        }
        return param.toArray(new JDParameter[0]);
    }

    @Override
    public String name() {
        return declaration.getName().toString();
    }

    @Override
    public String qualifiedName() {
        return cls.qualifiedName() + "." + name();
    }

    @Override
    public String modifiers() {
        return Arrays.stream(declaration.getModifiers().toArray()).map(x -> x.toString()).collect(Collectors.joining(" "));
    }

    @Override
    public JDDoc commentText() {
        if (declaration.getComment().isPresent() && declaration.getComment().get() instanceof JavadocComment) {
            JavadocComment jc = (JavadocComment) declaration.getComment().get();
            return new JPDoc(StaticJavaParser.parseJavadoc(jc.getContent()));
        }
        return null;
    }

}
