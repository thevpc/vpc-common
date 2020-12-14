/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.strings.format;

import net.thevpc.common.strings.MessageNameFormat;
import net.thevpc.common.strings.StringToObject;

/**
 *
 * @author thevpc
 */
class DoubleFunction extends AbstractFunction {
    
    public DoubleFunction() {
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
        return ((Number) instance).doubleValue();
    }
    
}
