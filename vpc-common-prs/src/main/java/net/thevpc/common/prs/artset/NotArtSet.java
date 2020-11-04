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

package net.thevpc.common.prs.artset;

import javax.swing.*;
import java.net.URL;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime Dec 15, 2007 4:32:41 AM
 */
public class NotArtSet implements ArtSet{
    public static final NotArtSet INSTANCE=new NotArtSet();
    private NotArtSet() {
    }

    public String getProperty(String id) {
        return null;
    }

    
    public ImageIcon getArtImage(String id) {
        return null;
    }

    public String getId() {
        return "none";
    }

    public String getName() {
        return "-";
    }

    public String getGroup() {
        return null;
    }

    public URL getArtImageURL(String id) {
        return null;
    }

    public boolean equals(Object other){
        if(other==null || !(other instanceof NotArtSet)){
            return false;
        }
        return getId().equals(((NotArtSet)other).getId());
    }

    public int hashCode(){
        return getClass().getName().hashCode()*31+getId().hashCode();
    }

    public String getArtImageName(String id) {
        return null;
    }

    public Object getOwner() {
        return null;
    }
    
}
