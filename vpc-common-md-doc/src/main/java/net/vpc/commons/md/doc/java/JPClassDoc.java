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
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.comments.JavadocComment;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import net.vpc.commons.md.doc.JDClassDoc;
import net.vpc.commons.md.doc.JDConstructorDoc;
import net.vpc.commons.md.doc.JDDoc;
import net.vpc.commons.md.doc.JDFieldDoc;
import net.vpc.commons.md.doc.JDMethodDoc;

/**
 *
 * @author vpc
 */
public class JPClassDoc implements JDClassDoc {

    private JPRootDoc root;
    private String packageName;
    private ClassOrInterfaceDeclaration declaration;
    private List<JDMethodDoc> methods = new ArrayList<>();
    private List<JDConstructorDoc> constructors = new ArrayList<>();
    private List<JDFieldDoc> fields = new ArrayList<>();

    public JPClassDoc(JPRootDoc root, ClassOrInterfaceDeclaration declaration, String packageName) {
        this.root = root;
        this.declaration = declaration;
        this.packageName = packageName;

//                    if (n.getComment().isPresent() && n.getComment().get() instanceof JavadocComment) {
//                        JavadocComment jc = (JavadocComment) n.getComment().get();
//                        Javadoc d = StaticJavaParser.parseJavadoc(jc.getContent());
//                        String title = String.format("%s (%s)", n.getName(), file);
//                        System.out.println(title);
//                        System.out.println(Strings.repeat("=", title.length()));
//                        System.out.println(n.getComment());
//                    }
        for (BodyDeclaration<?> member : declaration.getMembers()) {
            if (member instanceof MethodDeclaration) {
                methods.add(new JPMethodDoc((MethodDeclaration) member, this));
            }
            if (member instanceof ConstructorDeclaration) {
                constructors.add(new JPConstructorDoc((ConstructorDeclaration) member,this));
            }
            if (member instanceof FieldDeclaration) {
                FieldDeclaration vv = (FieldDeclaration) member;
                for (VariableDeclarator variable : vv.getVariables()) {
                    fields.add(new JPFieldDoc(vv,variable,this));
                }
            }
        }
    }

    @Override
    public JDFieldDoc[] fields() {
        return fields.toArray(new JDFieldDoc[0]);
    }

    @Override
    public JDConstructorDoc[] constructors() {
        return constructors.toArray(new JDConstructorDoc[0]);
    }

    @Override
    public JDMethodDoc[] methods() {
        return methods.toArray(new JDMethodDoc[0]);
    }

    @Override
    public String modifiers() {
        return Arrays.stream(declaration.getModifiers().toArray()).map(x -> x.toString()).collect(Collectors.joining(" "));
    }

    @Override
    public String name() {
        return declaration.getName().asString();
    }

    @Override
    public String qualifiedName() {
        if (packageName != null) {
            return packageName + "." + name();
        }
        return name();
    }

    @Override
    public JDDoc comments() {
        if (declaration.getComment().isPresent() && declaration.getComment().get() instanceof JavadocComment) {
            JavadocComment jc = (JavadocComment) declaration.getComment().get();
            return new JPDoc(StaticJavaParser.parseJavadoc(jc.getContent()));
        }
        return null;
    }

    public JPRootDoc getRoot() {
        return root;
    }

}
