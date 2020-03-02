package net.vpc.common.tson;

import java.time.LocalDate;

public interface TsonDate extends TsonTemporal {
    LocalDate getValue();

    TsonPrimitiveBuilder builder();
}
