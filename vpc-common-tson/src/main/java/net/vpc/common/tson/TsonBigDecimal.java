package net.vpc.common.tson;

import java.math.BigDecimal;

public interface TsonBigDecimal extends TsonNumber {
    BigDecimal getValue();

    TsonPrimitiveBuilder builder();
}
