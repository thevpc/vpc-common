package net.thevpc.commons.md.asciidoctor;

import net.thevpc.commons.md.MdParser;
import net.thevpc.commons.md.MdProvider;
import net.thevpc.commons.md.MdWriter;

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
