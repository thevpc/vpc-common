/**
 * ====================================================================
 *                        vpc-swingext library
 *
 * Description: <start><end>
 *
 * Copyright (C) 2006-2008 Taha BEN SALAH
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 * ====================================================================
 */
package net.vpc.common.swings;

import javax.swing.*;
import javax.swing.event.ListDataListener;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 14 août 2007 20:49:44
 */
public class DefaultListTwinsModel implements JListTwinsModel{
    private DefaultListModel leftModel;
    private DefaultListModel rightModel;
    public DefaultListTwinsModel(Object[] values,int[] selected) {
        leftModel =new DefaultListModel();
        rightModel =new DefaultListModel();
        for (int i = 0; i < values.length; i++) {
            Object value = values[i];
            boolean found=false;
            for (int j = 0; j < selected.length; j++) {
                if(i==j){
                    found=true;
                    break;
                }
            }
            if(found){
                rightModel.addElement(value);
            }else{
                leftModel.addElement(value);
            }
        }
    }


    public int getLeftSize() {
        return leftModel.getSize();
    }

    public int getRightSize() {
        return rightModel.getSize();
    }

    public Object getLeftElementAt(int index) {
        return leftModel.getElementAt(index);
    }


    public Object getRightElementAt(int index) {
        return rightModel.getElementAt(index);
    }


    public void addLeftListDataListener(ListDataListener l) {
        leftModel.addListDataListener(l);
    }

    public void addRightListDataListener(ListDataListener l) {
        rightModel.addListDataListener(l);
    }

    public void removeLeftListDataListener(ListDataListener l) {
        leftModel.removeListDataListener(l);
    }


    public void removeRightListDataListener(ListDataListener l) {
        rightModel.removeListDataListener(l);
    }


    public void moveAllLeft() {
        for(int i=getRightSize()-1;i>=0;i--){
            moveLeft(i);
        }
    }

    public void moveAllRight() {
        for(int i=getLeftSize()-1;i>=0;i--){
            moveRight(i);
        }
    }


    public void moveLeft(int rightIndex) {
        Object o = rightModel.remove(rightIndex);
        leftModel.addElement(o);
    }

    public void moveRight(int rightIndex) {
        Object o = leftModel.remove(rightIndex);
        rightModel.addElement(o);
    }
}
