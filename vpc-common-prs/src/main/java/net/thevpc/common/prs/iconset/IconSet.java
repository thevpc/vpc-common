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

package net.thevpc.common.prs.iconset;

import javax.swing.*;
import java.util.Locale;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * %creationtime 23 juin 2006 17:22:04
 */
public interface IconSet {
    Icon getIconByPath(String path) throws IconNotFoundException;

    static enum ErrorType {
        ERROR, REQUIRED, WARN, SILENT
    }

    String getId();

    String getIconPath(String iconName);
    
    String getName();

    Icon getIcon(String id) throws IconNotFoundException;

    Icon getUnknowIcon() throws IconNotFoundException;

    Locale getLocale();

    Icon getIcon(String icon, ErrorType errorType) throws IconNotFoundException;

    Icon getIconE(String icon) throws IconNotFoundException;

    Icon getIconR(String icon) throws IconNotFoundException;

    Icon getIconW(String icon) throws IconNotFoundException;

    Icon getIconS(String icon) throws IconNotFoundException;

    void setLocale(Locale locale);

    Icon loadImageIcon(String path);
}
