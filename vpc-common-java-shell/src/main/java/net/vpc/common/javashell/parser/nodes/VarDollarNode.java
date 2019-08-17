/**
 * ====================================================================
 *             Doovos (Distributed Object Oriented Operating System)
 *
 * Doovos is a new Open Source Distributed Object Oriented Operating System
 * Design and implementation based on the Java Platform.
 * Actually, it is a try for designing a distributed operation system in
 * top of existing centralized/network OS.
 * Designed OS will follow the object oriented architecture for redefining
 * all OS resources (memory,process,file system,device,...etc.) in a highly
 * distributed context.
 * Doovos is also a distributed Java virtual machine that implements JVM
 * specification on top the distributed resources context.
 *
 * Doovos BIN is a standard implementation for Doovos boot sequence, shell and
 * common application tools. These applications are running onDoovos guest JVM
 * (distributed jvm).
 *
 * Copyright (C) 2008-2010 Taha BEN SALAH
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 * ====================================================================
 */
package net.vpc.common.javashell.parser.nodes;

import net.vpc.common.javashell.JShell;
import net.vpc.common.javashell.parser.Token;
import net.vpc.common.javashell.JShellContext;

public class VarDollarNode extends CommandItemNode {

    Token token;

    public VarDollarNode(Token token) {
        this.token = (token);
    }

    public Token getToken() {
        return token;
    }

    public String getImage() {
        String s = getToken().image;
        if(s.startsWith("${")) {
            //remove cotes
            return s.substring(2, s.length() - 1);
        }else if(s.startsWith("$")) {
            return s.substring(1, s.length() - 1);
        }else{
            throw new IllegalArgumentException("Unexpected");
        }
    }
    
    public String getEscapedString(JShellContext context) {
        String ii = getImage();
        //should i replace $?
        JShell shell = context.getShell();
        String v = shell.getWordEvaluator().evalDollarExpression(ii, context);
        return shell.escapeString(v);
    }

    public String toDebugString() {
        return "VarDollarNode{" +
                token +
                '}';
    }

    public String toString() {
        return "${"+token.image+"}";
    }

}
