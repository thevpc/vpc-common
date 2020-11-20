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

import java.util.HashMap;
import java.util.MissingResourceException;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * %creationtime 27 août 2007 22:34:52
 */
public abstract class AbstractCachedMessageSetBundle extends AbstractMessageSetBundle {
    private HashMap<String, String> cache = new HashMap<String, String>();
    private boolean cacheEnabled = true;
    private int cacheSize = 100000;

    public boolean isCacheEnabled() {
        return cacheEnabled;
    }

    public void setCacheEnabled(boolean cacheEnabled) {
        this.cacheEnabled = cacheEnabled;
        if (!cacheEnabled) {
            revalidate();
            fireChanged();
        }
    }

    public final String getString(String key) throws MissingResourceException {
        String ss = cache.get(key);
        if (ss != null) {
            return ss;
        } else if (cache.containsKey(key)) {
            throw new MissingResourceException("Key not found", getClass().getName(), key);
        }
        try {
            ss = getStringNoCache(key);
            if (isCacheEnabled()) {
                cache.put(key, ss);
            }
            return ss;
        } catch (MissingResourceException e) {
            int cs=getCacheSize();
            if (cs>0 && cache.size() < cs) {
                cache.put(key, null);
            }
            throw e;
        }
    }

    public abstract String getStringNoCache(String key) throws MissingResourceException;

    public void revalidate() {
        if (cache.size() > 0) {
            cache.clear();
            fireChanged();
        }
    }


    public int getCacheSize() {
        return cacheSize;
    }

    public void setCacheSize(int cacheSize) {
        this.cacheSize = cacheSize;
    }
}