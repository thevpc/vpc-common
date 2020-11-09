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

import net.thevpc.common.prs.log.LoggerProvider;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 28 avr. 2007 20:40:04
 */
public class IconSetWrapper extends IconSetAdapter{
    private IconSet iconSet;
    private LoggerProvider loggerProvider;
    public IconSetWrapper(LoggerProvider loggerProvider) {
        setIdPrefix("IconSetWrapper");
        this.loggerProvider=loggerProvider;
    }
    public IconSet getIconSet(){
        return iconSet;
    }

    public void setIconSet(IconSet iconSet) {
        this.iconSet = iconSet;
    }

    @Override
    protected LoggerProvider getLoggerProvider() {
        return loggerProvider;
    }
    
    
}