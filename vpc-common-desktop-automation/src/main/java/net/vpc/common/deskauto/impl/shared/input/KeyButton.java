/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.deskauto.impl.shared.input;

/**
 *
 * @author vpc
 */
class KeyButton {
    String name;
    int vkey;
    boolean modifier;

    public KeyButton(String name, int vkey, boolean modifier) {
        this.name = name;
        this.vkey = vkey;
        this.modifier = modifier;
    }
    
}
