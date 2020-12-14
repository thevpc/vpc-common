/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.jshell.parser.nodes;


import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import net.thevpc.jshell.JShellContext;

/**
 * @author thevpc
 */
public interface InstructionNode extends Node {

    void eval(JShellContext context);


}
