/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.deskauto.impl.shared.input;

import net.vpc.common.deskauto.ContextualRobot;
import java.util.Stack;

/**
 *
 * @author vpc
 */
class InputCommandTracker {

    private ContextualRobot robot;
    private Stack<InputAction> keys = new Stack<>();

    public InputCommandTracker(ContextualRobot robot) {
        this.robot = robot;
    }

    public ContextualRobot getRobot() {
        return robot;
    }

    public void run(InputAction a) {
        robot.think();
        a.run(this);
    }

    public void pushPressed(InputAction a) {
        keys.push(a);
    }
    
    public void releaseAll() {
        while (!keys.isEmpty()) {
            release();
        }
    }

    public void release() {
        InputAction k = keys.pop();
        if (k instanceof PressKeyboardAction) {
            PressKeyboardAction d = (PressKeyboardAction) k;
            robot.keyRelease(d.value);
        } else {
            PressMouseAction d = (PressMouseAction) k;
            robot.mouseRelease(d.value);
        }
    }

}
