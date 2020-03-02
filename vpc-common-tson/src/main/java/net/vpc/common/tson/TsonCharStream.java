package net.vpc.common.tson;

import net.vpc.common.tson.TsonElement;
import net.vpc.common.tson.TsonPrimitiveBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.Writer;

public interface TsonCharStream extends TsonElement {
    String getStreamType();

    Reader getValue();

    TsonPrimitiveBuilder builder();
}
