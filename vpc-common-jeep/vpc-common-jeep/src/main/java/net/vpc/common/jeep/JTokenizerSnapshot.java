package net.vpc.common.jeep;

public interface JTokenizerSnapshot extends AutoCloseable{
    void rollback();
    void dispose();

    int getStartTokenNumber();

    void invalidate();

    /**
     * equivalent to dispose
     */
    void close();
}
