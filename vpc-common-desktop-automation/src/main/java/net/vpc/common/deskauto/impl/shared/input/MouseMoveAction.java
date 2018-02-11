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
class MouseMoveAction extends InputAction {

    int x;
    int y;

    public MouseMoveAction(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    @Override
    public void run(InputCommandTracker t) {
        t.getRobot().mouseMove(x, y);
    }

}
