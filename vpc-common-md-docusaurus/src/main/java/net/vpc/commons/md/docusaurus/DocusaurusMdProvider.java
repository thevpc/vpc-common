package net.vpc.commons.md.docusaurus;

import net.vpc.commons.md.MdParser;
import net.vpc.commons.md.MdProvider;
import net.vpc.commons.md.MdWriter;

import java.io.Reader;
import java.io.Writer;

public class DocusaurusMdProvider implements MdProvider {
    @Override
    public String getMimeType() {
        return "text/markdown-docusaurus";
    }

    @Override
    public MdParser createParser(Reader reader) {
        return new DocusaurusMdParser(reader);
    }

    @Override
    public MdWriter createWriter(Writer writer) {
        return new DocusaurusWriter(writer);
    }
}
