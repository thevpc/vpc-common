/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.props;

/**
 *
 * @author vpc
 */
public abstract class SourcePropertyListener implements PropertyListener {

    private Object source;

    public SourcePropertyListener(Object source) {
        this.source = source;
    }

    public boolean is(Object source) {
        return isSource(source);
    }

    public boolean isSource(Object source) {
        return this.source == source;
    }

    public Object getSource() {
        return source;
    }
}
