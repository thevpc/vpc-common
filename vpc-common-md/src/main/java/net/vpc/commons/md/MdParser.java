package net.vpc.commons.md;

public interface MdParser extends AutoCloseable{
    MdElement parse();

    @Override
    void close();
}
