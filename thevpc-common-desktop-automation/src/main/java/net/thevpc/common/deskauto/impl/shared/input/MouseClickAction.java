/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.deskauto.impl.shared.input;

/**
 *
 * @author thevpc
 */
class MouseClickAction extends InputAction {
    String name;
    int value;

    public MouseClickAction(String name, int value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public void run(InputCommandTracker t) {
        t.getRobot().click(value);
    }
    
}
