/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.jeep.core;

import net.vpc.common.jeep.impl.JEnum;
import net.vpc.common.jeep.impl.JEnumType;

/**
 *
 * @author vpc
 */
public abstract class JTokenState extends JEnum{

    protected JTokenState(JEnumType type, String name, int value) {
        super(type, name, value);
    }
    
}
