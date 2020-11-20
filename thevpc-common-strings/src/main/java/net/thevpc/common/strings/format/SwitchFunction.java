/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.strings.format;

import net.thevpc.common.strings.MessageNameFormat;
import net.thevpc.common.strings.MessageNameFormatContext;
import net.thevpc.common.strings.StringToObject;

/**
 *
 * @author vpc
 */
class SwitchFunction implements MessageNameFormat.Function {
    
    public SwitchFunction() {
    }

    @Override
    public Object eval(MessageNameFormat.ExprNode[] args, MessageNameFormat format, StringToObject stringToObject, MessageNameFormatContext messageNameFormatContext) {
        if (args.length == 0) {
            return null;
        }
        if (args.length == 1) {
            return args[0].format(format, stringToObject, messageNameFormatContext);
        }
        Object instance = args[0].format(format, stringToObject, messageNameFormatContext);
        int c = (args.length - 1) / 2;
        for (int i = 0; i < c; i++) {
            Object k = args[1 + 2 * i].format(format, stringToObject, messageNameFormatContext);
            if (k == null) {
                k = "";
            }
            if (k.equals(instance) || k.toString().equals(instance.toString())) {
                Object v = args[1 + 2 * i + 1].format(format, stringToObject, messageNameFormatContext);
                return v;
            }
        }
        if (2 * c + 1 < args.length) {
            return args[2 * c + 1].format(format, stringToObject, messageNameFormatContext);
        }
        return instance;
    }
    
}
