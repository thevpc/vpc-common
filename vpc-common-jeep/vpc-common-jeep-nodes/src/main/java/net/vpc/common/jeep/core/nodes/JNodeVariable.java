/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.jeep.core.nodes;

/**
 * @author vpc
 */
public abstract class JNodeVariable extends JDefaultNode {

    private final String name;

    public JNodeVariable(String name) {
        super();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return getName();
    }
}
