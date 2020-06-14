/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.app;

import net.vpc.common.props.WritablePDispatcher;
import net.vpc.common.msg.Message;

/**
 *
 * @author vpc
 */
public interface AppErrors extends WritablePDispatcher<Message> {

    void add(Throwable item);

    void add(String item);

}
