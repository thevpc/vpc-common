/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.swing.anim;

/**
 *
 * @author vpc
 */
public class Animators {

    public static LinearAnimator linear(AnimPoint from, AnimPoint to, long period) {
        return new LinearAnimator(from, to, period);
    }
}
