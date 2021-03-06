/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.swing;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author thevpc
 */
public class SwingComponentConfigurerFactory {

    private static final SwingComponentConfigurerFactory INSTANCE = new SwingComponentConfigurerFactory();

    public static SwingComponentConfigurerFactory getInstance() {
        return INSTANCE;
    }
    public Map<Class, SwingComponentConfigurer> configurers = new HashMap<>();

    public void register(Class c, SwingComponentConfigurer s) {
        configurers.put(c, s);
    }

    public SwingComponentConfigurer get(Class c) {
        return configurers.get(c);
    }
}
