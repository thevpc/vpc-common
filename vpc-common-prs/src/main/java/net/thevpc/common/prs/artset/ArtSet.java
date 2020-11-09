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
 * @creationtime Dec 15, 2007 4:31:30 AM
 */
public interface ArtSet {
    public String getId();
    public String getName();
    public String getGroup();
    public String getProperty(String id);
    public ImageIcon getArtImage(String id);
    public URL getArtImageURL(String id);
    public Object getOwner();
//    public String getArtImageName(String id);
}
