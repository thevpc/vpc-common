/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.strings.format;

import net.vpc.common.strings.MessageNameFormat;
import net.vpc.common.strings.StringToObject;
import net.vpc.common.strings.format.AbstractFunction;

/**
 *
 * @author vpc
 */
class IntegerFunction extends AbstractFunction {
    
    public IntegerFunction() {
    }

    @Override
    public Object evalArgs(Object[] args, MessageNameFormat format, StringToObject stringToObject) {
        if (args.length == 0) {
            return null;
        }
        if (args.length == 1) {
            return args[0];
        }
        Object instance = args[0];
        if (!(instance instanceof Number)) {
            instance = Double.parseDouble(instance.toString());
        }
        return ((Number) instance).intValue();
    }
    
}
