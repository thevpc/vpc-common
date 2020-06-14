/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.app;

import net.vpc.common.props.WritablePValue;

/**
 *
 * @author vpc
 */
public interface AppToolWindow {

    String id();

    WritablePValue<Boolean> active();

}
