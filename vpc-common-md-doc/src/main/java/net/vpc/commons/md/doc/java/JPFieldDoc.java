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
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.expr.Expression;
import java.util.Arrays;
import java.util.stream.Collectors;
import net.vpc.commons.md.doc.JDDoc;
import net.vpc.commons.md.doc.JDFieldDoc;
import net.vpc.commons.md.doc.JDType;

/**
 *
 * @author vpc
 */
public class JPFieldDoc implements JDFieldDoc {

    private FieldDeclaration fieldDeclaration;
    private VariableDeclarator variableDeclarator;
    private JPClassDoc cls;

    public JPFieldDoc(FieldDeclaration fieldDeclaration, VariableDeclarator variableDeclarator, JPClassDoc cls) {
        this.fieldDeclaration = fieldDeclaration;
        this.variableDeclarator = variableDeclarator;
        this.cls = cls;
    }

    @Override
    public boolean isStatic() {
        for (Modifier modifier : fieldDeclaration.getModifiers()) {
            if (modifier.getKeyword() == Modifier.Keyword.STATIC) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String name() {
        return variableDeclarator.getName().getIdentifier();
    }

    @Override
    public String qualifiedName() {
        return cls.qualifiedName() + "." + name();
    }

    @Override
    public JDType type() {
        return new JPType(variableDeclarator.getType());
    }

    @Override
    public String constantValueExpression() {
        Expression e = variableDeclarator.getInitializer().orElse(null);
        return e == null ? null : e.toString();
    }

    @Override
    public boolean isFinal() {
        for (Modifier modifier : fieldDeclaration.getModifiers()) {
            if (modifier.getKeyword() == Modifier.Keyword.FINAL) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String modifiers() {
        return Arrays.stream(fieldDeclaration.getModifiers().toArray()).map(x -> x.toString()).collect(Collectors.joining(" "));
    }

    @Override
    public JDDoc commentText() {
        if (fieldDeclaration.getComment().isPresent() && fieldDeclaration.getComment().get() instanceof JavadocComment) {
            JavadocComment jc = (JavadocComment) fieldDeclaration.getComment().get();
            return new JPDoc(StaticJavaParser.parseJavadoc(jc.getContent()));
        }
        return null;
    }

}
