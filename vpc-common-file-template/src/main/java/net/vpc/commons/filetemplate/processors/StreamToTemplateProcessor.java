/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.commons.filetemplate.processors;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import net.vpc.commons.filetemplate.FileTemplater;
import net.vpc.commons.filetemplate.StreamProcessor;
import net.vpc.commons.filetemplate.TemplateProcessor;
import net.vpc.commons.filetemplate.util.FileProcessorUtils;

/**
 *
 * @author vpc
 */
public class StreamToTemplateProcessor implements TemplateProcessor {

    private StreamProcessor streamProcessor;

    public StreamToTemplateProcessor(StreamProcessor streamProcessor) {
        this.streamProcessor = streamProcessor;
    }

    @Override
    public void processPath(Path source, String mimeType, FileTemplater context) {
        String p = context.getPathTranslator().translatePath(source.toString());
        if (p != null) {
            Path targetPath = Paths.get(p);
            FileProcessorUtils.mkdirs(targetPath.getParent());
            try (InputStream in = Files.newInputStream(source);
                    OutputStream out = Files.newOutputStream(targetPath);) {

                streamProcessor.processStream(in, out, context);
            } catch (IOException ex) {
                throw new UncheckedIOException(ex);
            }
        }
    }

    @Override
    public void processStream(InputStream source, OutputStream target, FileTemplater context) {
        streamProcessor.processStream(source, target, context);
    }

    @Override
    public String toString() {
        return String.valueOf(streamProcessor);
    }

}
