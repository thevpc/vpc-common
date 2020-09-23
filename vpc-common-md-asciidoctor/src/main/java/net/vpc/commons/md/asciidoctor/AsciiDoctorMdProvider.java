package net.vpc.commons.md.asciidoctor;

import net.vpc.commons.md.MdParser;
import net.vpc.commons.md.MdProvider;
import net.vpc.commons.md.MdWriter;

import java.io.Reader;
import java.io.Writer;

public class AsciiDoctorMdProvider implements MdProvider {
    @Override
    public String getMimeType() {
        return "text/markdown-asciidoctor";
    }

    @Override
    public MdParser createParser(Reader reader) {
        return null;
    }

    @Override
    public MdWriter createWriter(Writer writer) {
        return new AsciiDoctorWriter(writer);
    }
}
