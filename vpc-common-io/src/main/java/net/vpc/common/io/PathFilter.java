/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.io;

/**
 *
 * @author vpc
 */
public interface PathFilter {

    boolean accept(String path);

}
