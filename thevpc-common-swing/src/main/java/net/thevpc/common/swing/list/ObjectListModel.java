/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.swing.list;

/**
 *
 * @author thevpc
 */
public interface ObjectListModel {

    int size();

    String getName(Object obj);

    Object getObjectAt(int i);
    
}
