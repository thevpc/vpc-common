/**
 * ====================================================================
 * Doovos (Distributed Object Oriented Operating System)
 * <p>
 * Doovos is a new Open Source Distributed Object Oriented Operating System
 * Design and implementation based on the Java Platform.
 * Actually, it is a try for designing a distributed operation system in
 * top of existing centralized/network OS.
 * Designed OS will follow the object oriented architecture for redefining
 * all OS resources (memory,process,file system,device,...etc.) in a highly
 * distributed context.
 * Doovos is also a distributed Java virtual machine that implements JVM
 * specification on top the distributed resources context.
 * <p>
 * Doovos BIN is a standard implementation for Doovos boot sequence, shell and
 * common application tools. These applications are running onDoovos guest JVM
 * (distributed jvm).
 * <p>
 * Copyright (C) 2008-2010 Taha BEN SALAH
 * <p>
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 * ====================================================================
 */
package net.vpc.common.javashell.parser.nodes;

import net.vpc.common.javashell.parser.Token;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import net.vpc.common.javashell.JShellContext;

public class StringAntiCotedNode extends CommandItemNode {

    Token token;
    Node n;

    public StringAntiCotedNode(Token t, Node n) {
        this.token = t;
        this.n = n;
    }

    public Token getToken() {
        return token;
    }

    public String getImage() {
        String s = getToken().image;
        //remove cotes
        return s.substring(1, s.length() - 1);
    }

    public String toDebugString() {
        String t = getToken().toString();
        String u = n.toString();
        if (t.equals("$((")) {
            return t + " " + u + " ))";
        }
        if (t.equals("$(")) {
            return t + " " + u + " )";
        }
        if (t.equals("`")) {
            return t + u + "`";
        }
        return "StringAntiCotedNode{" + "n=" + n + '}';
    }

    public String getEscapedString(JShellContext context) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        JShellContext c2 = context.getShell().createContext(context);
        PrintStream p = new PrintStream(out);
        c2.setOut(p);
        ((InstructionNode) n).eval(c2);
        p.flush();
        return context.getShell().escapeString(out.toString());
    }
    public String toString() {
        String t = getToken().toString();
        String u = n.toString();
        if (t.equals("$((")) {
            return "$(("+u+"))";
        }
        if (t.equals("$(")) {
            return "$("+u+")";
        }
        if (t.equals("`")) {
            return "`"+u.replace("`","\\`")+"`";
        }
        if(token.image.length()>0){
            return "`"+token.image.replace("`","\\`")+"`";
        }
        return "";
    }

}
