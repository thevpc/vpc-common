package net.vpc.common.tson;

import net.vpc.common.tson.TsonElement;
import net.vpc.common.tson.TsonPrimitiveBuilder;

import java.io.*;

public interface TsonBinaryStream extends TsonElement {
    InputStream getValue();

    Reader getBase64Value();

    Reader getBase64Value(int lineMax);

    TsonPrimitiveBuilder builder();
}
