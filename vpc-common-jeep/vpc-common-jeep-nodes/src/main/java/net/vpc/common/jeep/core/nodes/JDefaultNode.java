package net.vpc.common.jeep.core.nodes;

import net.vpc.common.jeep.JNode;
import net.vpc.common.jeep.JNodeCopyFactory;
import net.vpc.common.jeep.JType;

public abstract class JDefaultNode extends AbstractJNode{
    private JType type;
    public JDefaultNode() {
    }

    public JType getType() {
        return type;
    }

    @Deprecated
    public JNode setType(JType type) {
        this.type = type;
        return this;
    }

    public abstract int id();

    @Override
    public void copyFrom(JNode other,JNodeCopyFactory copyFactory) {
        super.copyFrom(other);
        if (other instanceof JDefaultNode) {
            setType(((JDefaultNode)other).getType());
        }
    }
}
