/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.jeep;


/**
 * @author vpc
 */
public interface JFunction extends JInvokable{

    default JDeclaration declaration(){
        return null;
    }

}
