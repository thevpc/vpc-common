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

package net.thevpc.common.prs.artset;

import javax.swing.*;
import java.net.URL;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * %creationtime Dec 15, 2007 4:32:41 AM
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
