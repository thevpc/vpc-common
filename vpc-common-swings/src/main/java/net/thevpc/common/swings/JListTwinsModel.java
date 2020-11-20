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
package net.thevpc.common.swings;

import javax.swing.event.ListDataListener;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * %creationtime 14 août 2007 20:48:17
 */
public interface JListTwinsModel {
      /**
       * Returns the length of the list.
       * @return the length of the list
       */
      int getLeftSize();
      int getRightSize();
      Object getLeftElementAt(int index);
      Object getRightElementAt(int index);
      void moveLeft(int rightIndex);
      void moveRight(int leftIndex);
      void moveAllLeft();
      void moveAllRight();


      /**
       * Adds a listener to the list that's notified each time a change
       * to the data model occurs.
       * @param l the <code>ListDataListener</code> to be added
       */
      void addLeftListDataListener(ListDataListener l);
      void addRightListDataListener(ListDataListener l);

      /**
       * Removes a listener from the list that's notified each time a
       * change to the data model occurs.
       * @param l the <code>ListDataListener</code> to be removed
       */
      void removeLeftListDataListener(ListDataListener l);
      void removeRightListDataListener(ListDataListener l);
}
