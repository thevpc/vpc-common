package net.vpc.common.util;

import java.io.Serializable;

/**
 * Created by vpc on 6/6/17.
 */
public interface DoubleFormat extends Serializable {
    String formatDouble(double value);
}
