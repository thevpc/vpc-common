package net.thevpc.commons.md;

public interface MdParser extends AutoCloseable{
    MdElement parse();

    @Override
    void close();
}
