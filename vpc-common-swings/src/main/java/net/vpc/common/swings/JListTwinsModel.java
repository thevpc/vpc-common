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

import javax.swing.event.ListDataListener;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 14 août 2007 20:48:17
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
