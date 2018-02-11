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

import java.util.HashMap;
import java.util.MissingResourceException;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 27 août 2007 22:34:52
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