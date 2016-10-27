package net.vpc.common.util;

/**
 * Created by vpc on 7/1/16.
 */
public interface Filter<T> {
    boolean accept(T value);
}
