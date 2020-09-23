package net.vpc.common.jeep.core.nodes;

import net.vpc.common.jeep.JCompilationUnit;
import net.vpc.common.jeep.JContext;
import net.vpc.common.jeep.JTokenizer;
import net.vpc.common.jeep.core.DefaultJParser;
import net.vpc.common.jeep.core.DefaultJParserNodeFactory;
import net.vpc.common.jeep.core.JExpressionOptions;
import net.vpc.common.jeep.core.SimpleNodeNodeFactory;

public class JDefaultNodeParser extends DefaultJParser<JDefaultNode,JExpressionOptions<JExpressionOptions>> {
    public JDefaultNodeParser(JTokenizer tokenizer, JCompilationUnit compilationUnit, JContext context) {
        super(tokenizer, compilationUnit, context, new DefaultJParserNodeFactory(compilationUnit,context));
    }
}
