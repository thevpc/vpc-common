/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.util;

import java.net.URL;

/**
 *
 * @author vpc
 */
public interface URLFilter {

    public boolean accept(URL path);
    
}
