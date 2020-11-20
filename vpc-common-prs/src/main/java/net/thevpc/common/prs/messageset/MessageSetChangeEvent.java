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

import java.util.EventObject;
import java.util.Locale;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * %creationtime 27 août 2007 22:29:55
 */
public class MessageSetChangeEvent extends EventObject {
    private Locale oldLocale;
    private Locale newLocale;
    public MessageSetChangeEvent(Object source) {
        super(source);
    }

    public MessageSetChangeEvent(Object source, Locale oldLocale, Locale newLocale) {
        super(source);
        this.oldLocale = oldLocale;
        this.newLocale = newLocale;
    }

    public boolean isLocaleChange(){
        return oldLocale!=newLocale;
    }

    public Locale getOldLocale() {
        return oldLocale;
    }

    public Locale getNewLocale() {
        return newLocale;
    }
}
