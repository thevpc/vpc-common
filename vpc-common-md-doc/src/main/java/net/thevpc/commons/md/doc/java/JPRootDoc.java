/**
 * ====================================================================
 * Nuts : Network Updatable Things Service
 * (universal package manager)
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
package net.thevpc.commons.md.doc.java;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import net.thevpc.commons.md.doc.JDClassDoc;
import net.thevpc.commons.md.doc.JDRootDoc;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 *
 * @author vpc
 */
public class JPRootDoc implements JDRootDoc {

    private Map<String, JDClassDoc> classes = new HashMap<String, JDClassDoc>();

    public void parseSrcFolder(Path path, Predicate<String> packageFilter) {
        try {
            //support for maven
            if(Files.isRegularFile(path.resolve("pom.xml"))
                    && Files.isDirectory(path.resolve("src/main/java"))
            ){
                path=path.resolve("src/main/java");
            }
            Path path0=path;
            Files.walk(path0).filter(x -> Files.isRegularFile(x)
                    && x.getFileName().toString().endsWith(".java")
            ).forEach(file -> {
                String pck =
                        StreamSupport.stream(file.subpath(path0.getNameCount(), file.getNameCount()).spliterator(), false)
                                .map(Path::toString)
                                .collect(Collectors.joining("."));
                if (packageFilter == null || packageFilter.test(pck)) {
                    parseFile(file);
                }
            });
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    public void parseFile(Path path) {
        try {
            new VoidVisitorAdapter<Object>() {
                PackageDeclaration p;

                @Override
                public void visit(ClassOrInterfaceDeclaration n, Object arg) {
                    super.visit(n, arg);
                    add(new JPClassDoc(JPRootDoc.this, n, p.getName().asString()));
                }

                @Override
                public void visit(PackageDeclaration p, Object arg) {
                    super.visit(p, arg);
                    this.p = p;
                }

            }.visit(StaticJavaParser.parse(path), null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public JDClassDoc get(String qualifiedName) {
        return classes.get(qualifiedName);
    }

    public JPRootDoc add(JDClassDoc c) {
        classes.put(c.qualifiedName(), c);
        return this;
    }

    @Override
    public JDClassDoc[] classes() {
        return classes.values().toArray(new JDClassDoc[0]);
    }

}
