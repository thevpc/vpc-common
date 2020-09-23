/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.commons.filetemplate.processors;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UncheckedIOException;
import java.io.Writer;
import net.vpc.commons.filetemplate.ExprEvaluator;
import net.vpc.commons.filetemplate.FileTemplater;
import net.vpc.commons.filetemplate.StreamExecutor;
import net.vpc.commons.filetemplate.StreamProcessor;
import net.vpc.commons.filetemplate.util.FileProcessorUtils;

/**
 *
 * @author vpc
 */
public class DefaultExecutor implements StreamExecutor, StreamProcessor {

    private ExprEvaluator evaluator;

    public DefaultExecutor(ExprEvaluator evaluator) {
        this.evaluator = evaluator;
    }

    @Override
    public void processStream(InputStream source, OutputStream target, FileTemplater context) {
        String text = FileProcessorUtils.loadString(source, null);
        if (text != null) {
            Object z = evaluator.eval(text, context);
            if (z != null) {
                try {
                    Writer w = new OutputStreamWriter(target);
                    w.write(String.valueOf(z));
                    w.flush();
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            }
        }
    }

    @Override
    public Object execute(InputStream source, OutputStream target, FileTemplater context) {
        String text = FileProcessorUtils.loadString(source, null);
        if (text != null) {
            Object z = evaluator.eval(text, context);
            return z == null ? "" : z;
        }
        return "";
    }

    @Override
    public String toString() {
        return "Executor(" + evaluator + ")";
    }

}
