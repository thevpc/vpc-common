/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.props.impl;

import java.util.ArrayList;
import java.util.List;
import net.thevpc.common.props.*;

/**
 *
 * @author thevpc
 */
public class PrpBindUtils {

    public static String[] splitPath(String path) {
        List<String> a = new ArrayList<>();
        if (path != null) {
            for (String s : path.split("[/\\\\]+")) {
                if (!s.isEmpty()) {
                    a.add(s);
                }
            }
        }
        return a.toArray(new String[0]);
    }
}
