/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.javashell;

import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.vpc.common.javashell.parser.nodes.BinoOp;
import net.vpc.common.javashell.parser.nodes.InstructionNode;
import net.vpc.common.javashell.util.JavaShellNonBlockingInputStream;
import net.vpc.common.javashell.util.JavaShellNonBlockingInputStreamAdapter;

/**
 *
 * @author vpc
 */
public class DefaultJShellNodeEvaluator implements JShellNodeEvaluator {

    @Override
    public void evalBinaryOperation(String opString, InstructionNode left, InstructionNode right, JShellContext context) {
        context.getShell().traceExecution("(" + left + ") " + opString + "(" + right + ")");
        if (";".equals(opString)) {
            evalBinarySuiteOperation(left, right, context);
        } else if ("&&".equals(opString)) {
            evalBinaryAndOperation(left, right, context);
        } else if ("||".equals(opString)) {
            evalBinaryOrOperation(left, right, context);
        } else if ("|".equals(opString)) {
            evalBinaryPipeOperation(left, right, context);
        } else {
            throw new JShellException(1, "Unsupported operator " + opString);
        }
    }

    @Override
    public void evalBinaryOrOperation(final InstructionNode left, InstructionNode right, final JShellContext context) {
        try {
            context.getShell().uniformException(new NodeEvalUnsafeRunnable(left, context));
            return;
        } catch (JShellUniformException e) {
            if (e.isQuit()) {
                e.throwQuit();
                return;
            }
        }
        right.eval(context);
    }

    @Override
    public void evalBinaryAndOperation(InstructionNode left, InstructionNode right, JShellContext context) {
        right.eval(context);
        left.eval(context);
    }

    @Override
    public void evalBinarySuiteOperation(InstructionNode left, InstructionNode right, JShellContext context) {
        try {
            context.getShell().uniformException(new NodeEvalUnsafeRunnable(left, context));
        } catch (JShellUniformException e) {
            if (e.isQuit()) {
                e.throwQuit();
                return;
            }
        }
        right.eval(context);
    }

    @Override
    public void evalBinaryPipeOperation(final InstructionNode left, InstructionNode right, final JShellContext context) {
        final PipedOutputStream out;
        final PipedInputStream in;
        final JavaShellNonBlockingInputStream in2;
        try {
            out = new PipedOutputStream();
            in = new PipedInputStream(out, 1024);
            in2 = (in instanceof JavaShellNonBlockingInputStream) ? (JavaShellNonBlockingInputStream) in : new JavaShellNonBlockingInputStreamAdapter("jpipe-" + right.toString(), in);
        } catch (IOException ex) {
//            Logger.getLogger(BinoOp.class.getName()).log(Level.SEVERE, null, ex);
            throw new JShellException(1, ex);
        }
        final JShellUniformException[] a = new JShellUniformException[2];
        final PrintStream out1 = new PrintStream(out);
        final JShellContext leftContext = context.getShell().createContext(context).setOut(out1);
        Thread j1 = new Thread() {
            @Override
            public void run() {
                try {
                    context.getShell().uniformException(new NodeEvalUnsafeRunnable(left, leftContext));
                } catch (JShellUniformException e) {
                    if (e.isQuit()) {
                        e.throwQuit();
                        return;
                    }
                    a[0] = e;
                }
                in2.noMoreBytes();
            }

        };
        j1.start();
        JShellContext rightContext = context.getShell().createContext(context).setIn((InputStream) in2);
        try {
            context.getShell().uniformException(new NodeEvalUnsafeRunnable(right, rightContext));
        } catch (JShellUniformException e) {
            if (e.isQuit()) {
                e.throwQuit();
                return;
            }
            a[1] = e;
        }
        out1.flush();
        try {
            j1.join();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (a[1] != null) {
            a[1].throwAny();
        }
    }

}
