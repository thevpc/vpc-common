/**
 * ====================================================================
 *                        vpc-prs library
 *
 * Pluggable Resources Set is a small library for simplifying
 * plugin based applications
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

package net.vpc.common.prs.messageset;

import javax.swing.event.EventListenerList;
import java.util.Locale;
import java.util.MissingResourceException;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 27 août 2007 22:34:52
 */
public abstract class AbstractMessageSetBundle implements MessageSetBundle {
    private EventListenerList listenerList;
    private MessageSetChangeEvent anyChangeEvent;
    private Locale locale = Locale.getDefault();

    public Locale getLocale() {
        return locale;
    }


    public abstract String getString(String key) throws MissingResourceException ;

    public void setLocale(Locale locale) {
        Locale old = this.locale;
        if (!(old == locale || old != null && old.equals(locale))) {
            this.locale = locale;
            fireLocaleChanged(old, locale);
        }
    }

    public void addChangeListener(MessageSetChangeListener listener) {
        if (listenerList == null) {
            listenerList = new EventListenerList();
        }
        listenerList.add(MessageSetChangeListener.class, listener);
    }

    public void removeChangeListener(MessageSetChangeListener listener) {
        if (listenerList != null) {
            listenerList.remove(MessageSetChangeListener.class, listener);
        }
    }

    // Notify all listeners that have registered interest for
    // notification on this event type.  The event instance
    // is lazily created using the parameters passed into
    // the fire method.
    protected void fireChanged() {
        revalidate();
        if (listenerList != null) {
            // Guaranteed to return a non-null array
            Object[] listeners = listenerList.getListenerList();
            // Process the listeners last to first, notifying
            // those that are interested in this event
            for (int i = listeners.length - 2; i >= 0; i -= 2) {
                if (listeners[i] == MessageSetChangeListener.class) {
                    // Lazily create the event:
                    if (anyChangeEvent == null)
                        anyChangeEvent = new MessageSetChangeEvent(this);
                    ((MessageSetChangeListener) listeners[i + 1]).messageSetChanged(anyChangeEvent);
                }
            }
        }
    }

    // Notify all listeners that have registered interest for
    // notification on this event type.  The event instance
    // is lazily created using the parameters passed into
    // the fire method.
    protected void fireLocaleChanged(Locale oldLocale, Locale newLocale) {
        revalidate();
        if (listenerList != null) {
// Guaranteed to return a non-null array
            Object[] listeners = listenerList.getListenerList();
            // Process the listeners last to first, notifying
            // those that are interested in this event
            MessageSetChangeEvent e = null;
            for (int i = listeners.length - 2; i >= 0; i -= 2) {
                if (listeners[i] == MessageSetChangeListener.class) {
                    // Lazily create the event:
                    if (e == null)
                        e = new MessageSetChangeEvent(this, oldLocale, newLocale);
                    ((MessageSetChangeListener) listeners[i + 1]).messageSetChanged(e);
                }
            }
        }
    }

    // Notify all listeners that have registered interest for
    // notification on this event type.  The event instance
    // is lazily created using the parameters passed into
    // the fire method.
    protected void fireLocaleChanged(MessageSetChangeEvent e) {
        revalidate();
        if (listenerList != null) {
            // Guaranteed to return a non-null array
            Object[] listeners = listenerList.getListenerList();
            // Process the listeners last to first, notifying
            // those that are interested in this event
            for (int i = listeners.length - 2; i >= 0; i -= 2) {
                if (listeners[i] == MessageSetChangeListener.class) {
                    // Lazily create the event:
                    ((MessageSetChangeListener) listeners[i + 1]).messageSetChanged(e);
                }
            }
        }
    }

    public void revalidate() {

    }


}
