package net.vpc.common.util;

/**
 * Created by vpc on 7/1/16.
 * @param <T>
 */
public interface Filter<T> {
    boolean accept(T value);
}
