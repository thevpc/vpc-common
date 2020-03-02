package net.vpc.common.tson;

import java.math.BigDecimal;

public interface TsonBigComplex extends TsonNumber {
    BigDecimal getReal();

    BigDecimal getImag();

    TsonPrimitiveBuilder builder();
}
