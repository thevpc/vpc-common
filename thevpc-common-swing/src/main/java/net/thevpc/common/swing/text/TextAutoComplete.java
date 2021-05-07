/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.swing.text;

import java.util.List;

/**
 *
 * @author vpc
 */
public interface TextAutoComplete {

    List<String> autoComplete(String current);
    
}
