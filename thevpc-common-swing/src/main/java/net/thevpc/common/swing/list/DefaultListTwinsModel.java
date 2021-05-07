/**
 * ====================================================================
 *                        vpc-swingext library
 *
 * Description: <start><end>
 *
 * <br>
 *
 * Copyright [2020] [thevpc]
 * Licensed under the Apache License, Version 2.0 (the "License"); you may 
 * not use this file except in compliance with the License. You may obtain a 
 * copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an 
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, 
 * either express or implied. See the License for the specific language 
 * governing permissions and limitations under the License.
 * <br>
 * ====================================================================
*/
package net.thevpc.common.swing.list;

import net.thevpc.common.swing.list.JListTwinsModel;
import javax.swing.*;
import javax.swing.event.ListDataListener;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * %creationtime 14 août 2007 20:49:44
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
