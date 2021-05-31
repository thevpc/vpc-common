/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.props;

import net.thevpc.common.props.impl.WritableValueImpl;

/**
 * @author vpc
 */
public interface WritableInt extends WritableValue<Integer> {

    public void set(int value);

    public void inc(int value);

    public void inc();
}
