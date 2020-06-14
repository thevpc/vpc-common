package net.vpc.common.jeep.core.nodes;

import net.vpc.common.jeep.JConverter;
import net.vpc.common.jeep.JNode;
import net.vpc.common.jeep.util.JTypeUtils;

public class JNodeConverter extends JDefaultNode {

    private final JConverter currentConverter;
    private final JNode node;

    public JNodeConverter(JNode node, JConverter currentConverter) {
        super();
        this.currentConverter = currentConverter;
        this.node = node;
    }
    @Override
    public int id() {
        return JNodeDefaultIds.NODE_CONVERTER;
    }

//    @Override
//    public JType getType(JContext context) {
//        return currentConverter.targetType();
//    }


//    @Override
//    public Object evaluate(JContext context) {
//        return currentConverter.convert(node.evaluate(context), context);
//    }

    public JConverter getCurrentConverter() {
        return currentConverter;
    }

    public JNode getNode() {
        return node;
    }

    public String toString() {
        return "(" + JTypeUtils.str(currentConverter.targetType()) + ")" + node.toString()
                ;
    }

}
