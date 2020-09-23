package net.vpc.commons.md;

public interface MdWriter extends AutoCloseable{

    void setHeader(String name,Object value);
    void write(MdElement element);

    @Override
    void close();
}
