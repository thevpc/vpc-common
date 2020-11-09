/**
 * ====================================================================
 *                        vpc-prs library
 *
 * Pluggable Resources Set is a small library for simplifying
 * plugin based applications
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

package net.thevpc.common.prs.messageset;

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
