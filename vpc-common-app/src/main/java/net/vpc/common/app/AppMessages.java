/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.app;

import net.vpc.common.props.PList;
import net.vpc.common.props.WritablePList;
import net.vpc.common.msg.Message;

/**
 *
 * @author vpc
 */
public interface AppMessages extends PList<Message> {

    WritablePList<AppMessageProducer> producers();

    void update();

}
