/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.deskauto.impl.shared.input;

/**
 *
 * @author vpc
 */
class ReleaseAllAction extends InputAction {

    public ReleaseAllAction() {
    }

    @Override
    public void run(InputCommandTracker t) {
        t.releaseAll();
    }
    
}
