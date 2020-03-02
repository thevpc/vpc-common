package net.vpc.common.tson.impl.parser;

import net.vpc.common.tson.TsonParserVisitor;

public class EmptyTsonParserVisitor implements TsonParserVisitor {
    public static final TsonParserVisitor INSTANCE=new EmptyTsonParserVisitor();
}
