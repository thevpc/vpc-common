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
 * <br>
 *
 * Copyright [2020] [thevpc]
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain a
 * copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 * <br>
 * ====================================================================
*/
package net.thevpc.jshell.parser.nodes;


import java.util.ArrayList;
import java.util.List;
import net.thevpc.jshell.JShellContext;

/**
 * Created by IntelliJ IDEA. User: vpc Date: 5 août 2008 Time: 20:31:48 To
 * change this template use File | Settings | File Templates.
 */
public class CommandItemHolderNode implements Node {
    List<CommandItemNode> commandItemHolderNodes=new ArrayList();
    public CommandItemHolderNode() {
    }

    public String[] evalStringArray(JShellContext context){
        return context.expandPaths(getPathString(context));
    }

    public String getPathString(JShellContext context){
        StringBuilder sb=new StringBuilder();
        for (Object o : commandItemHolderNodes) {
            sb.append(((CommandItemNode)o).getPathString(context));
        }
        return (sb.toString());
    }

    public String getEscapedString(JShellContext context){
        StringBuilder sb=new StringBuilder();
        for (Object o : commandItemHolderNodes) {
            sb.append(((CommandItemNode)o).getEscapedString(context));
        }
        return sb.toString();
    }

    public void add(CommandItemNode node){
        commandItemHolderNodes.add(node);
//        System.out.println("add "+node+" => "+this);
    }

    public CommandItemNode removeLast(){
        return commandItemHolderNodes.remove(commandItemHolderNodes.size()-1);
//        System.out.println("add "+node+" => "+this);
    }

    public String toDebugString() {
        return "{" +
                commandItemHolderNodes +
                '}';
    }

    public boolean isVarAssignment(){
        if(commandItemHolderNodes.size()>0){
            CommandItemNode i = commandItemHolderNodes.get(0);
            if(i instanceof WordNode){
                String image = ((WordNode) i).getImage();
                if(image.contains("=")){
                    return true;
                }
            }
        }
        return false;
    }

    public String getVarAssignmentName(){
        if(commandItemHolderNodes.size()>0){
            CommandItemNode i = commandItemHolderNodes.get(0);
            if(i instanceof WordNode){
                String image = ((WordNode) i).getImage();
                if(image.contains("=")){
                    return image.substring(0,image.indexOf('='));
                }
            }
        }
        throw new IllegalArgumentException();
    }

    public CommandItemHolderNode getVarAssignmentValue(){
        CommandItemHolderNode val=new CommandItemHolderNode();
        if(commandItemHolderNodes.size()>0){
            CommandItemNode i = commandItemHolderNodes.get(0);
            if(i instanceof WordNode){
                String image = ((WordNode) i).getImage();
                if(image.contains("=")){
                    val.add(new WordNode(image.substring(image.indexOf('=')+1)));
                    for (int j = 1; j < commandItemHolderNodes.size(); j++) {
                        val.add(commandItemHolderNodes.get(j));
                    }
                    return val;
                }
            }
        }
        throw new IllegalArgumentException();
    }

    @Override
    public String toString() {
        if(commandItemHolderNodes.size()==0){
            return "\"\"";
        }
        StringBuilder sb=new StringBuilder();
        for (int i = 0; i < commandItemHolderNodes.size(); i++) {
            sb.append(commandItemHolderNodes.get(i).toString());
        }
        String s = sb.toString();
        if(s.isEmpty()){
            return "\"\"";
        }
        return s;
    }

}
