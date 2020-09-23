package net.vpc.common.jeep.core;

import net.vpc.common.jeep.JCompilationUnit;
import net.vpc.common.jeep.JContext;
import net.vpc.common.jeep.JTokenizer;
import net.vpc.common.jeep.core.nodes.JSimpleNode;

public class SimpleJParser extends DefaultJParser<JSimpleNode,JExpressionOptions<JExpressionOptions>> {
    public SimpleJParser(JTokenizer tokenizer, JCompilationUnit compilationUnit, JContext context) {
        super(tokenizer, compilationUnit, context, new SimpleNodeNodeFactory());
    }
}
