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
package net.thevpc.jshell.parser.nodes;

import java.util.*;
import net.thevpc.jshell.JShell;
import net.thevpc.jshell.JShellContext;

public class CommandNode extends InstructionNode {

    public ArrayList<CommandItemHolderNode> items = new ArrayList<CommandItemHolderNode>();
    public boolean nowait = false;
    public boolean aliases = true;
    public boolean builtins = true;
    public boolean external = true;

    /**
     * this is a string with \ and * and ?
     *
     * @param context
     */
    @Override
    public void eval(JShellContext context) {
        JShell shell = context.getShell();

        ArrayList<String> cmds = new ArrayList<String>();
        boolean acceptEnv = true;
        Properties usingItems = new Properties();
        for (int i = 0; i < items.size(); i++) {
            CommandItemHolderNode n = items.get(i);
            if (acceptEnv) {
                if (n.isVarAssignment()) {
                    StringBuilder sb = new StringBuilder();
                    boolean first = true;
                    for (String s : n.getVarAssignmentValue().evalStringArray(context)) {
                        if (first) {
                            first = false;
                        } else {
                            sb.append(" ");
                        }
                        sb.append(s);
                    }
                    usingItems.setProperty(
                            n.getVarAssignmentName(),
                            sb.toString()
                    );
                } else {
                    Collections.addAll(cmds, n.evalStringArray(context));
                    acceptEnv = false;
                }
            } else {
                Collections.addAll(cmds, n.evalStringArray(context));
            }
        }
        if (cmds.isEmpty()) {
            context.vars().set((Map) usingItems);
            return;
        } else {
            if (usingItems.size() > 0) {
                context = shell.createContext(context);
                context.setVars(context.vars().copy());
                context.vars().set((Map) usingItems);
            }
            shell.executePreparedCommand(cmds.toArray(new String[0]), aliases, builtins, external, context);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < items.size(); i++) {
            if (i > 0) {
                sb.append(" ");
            }
            sb.append(items.get(i).toString());
        }
        return sb.toString();
    }

    public String toDebugString() {
        StringBuilder sb = new StringBuilder();
        if (nowait) {
            sb.append("nowait(");
        }
        for (int i = 0; i < items.size(); i++) {
            CommandItemHolderNode item = items.get(i);
            if (sb.length() > 0) {
                sb.append(" ");
            }
            sb.append(item);
        }
        if (sb.length() == 0) {
            sb.append("<EMPTY COMMAND>");
        }
        if (nowait) {
            sb.append(")");
        }
        return sb.toString();
    }

    public void add(CommandItemHolderNode item) {
        items.add(item);
    }
}
