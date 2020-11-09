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

import java.util.Locale;
import java.util.MissingResourceException;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 28 avr. 2007 21:07:13
 */
public abstract class MessageSetBundleAdapter extends AbstractMessageSetBundle{

    public abstract MessageSetBundle getMessageSetBundle();

    public String getString/*NoCache*/(String key) throws MissingResourceException {
        return getMessageSetBundle().getString(key);
    }

    public Locale getLocale() {
        return getMessageSetBundle().getLocale();
    }

    public void setLocale(Locale locale) {
        //do nothing
    }
}
