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

package net.thevpc.common.prs.plugin;

import java.io.Serializable;
import java.text.ParseException;

/**
 *
 * @author vpc
 */
public class PluginDependency implements Serializable {
    private String id;
    private VersionInterval versionInterval;

    /**
     * never use this constructor
     */
    public PluginDependency(){
        
    }
    public PluginDependency(String str) throws ParseException {
        int x=-1;
        str=str.trim();
        int strLen = str.length();
        for(int i=0;i< strLen;i++){
            char c=str.charAt(i);
            if(c==']' || c=='['){
                x=i;
                break;
            }
        }
        if(x>0){
          if(x+1>=(strLen) || (str.charAt(strLen-1)!=']' && str.charAt(strLen-1)!='[')){
              throw new ParseException("Expected ]",strLen-1);
          }
          id=str.substring(0,x).trim();
          versionInterval =new VersionInterval(str.substring(x));
        }else{
          id=str;
          versionInterval = VersionInterval.ANY_VERSION;
        }
    }
    
    public PluginDependency(String id, VersionInterval versionInterval) {
        this.id = id;
        this.versionInterval = versionInterval;
    }

    public String getId() {
        return id;
    }

    public VersionInterval getVersionInterval() {
        return versionInterval;
    }
    
    @Override
    public String toString() {
        return (id==null?"?":id)+(versionInterval ==null?"": versionInterval.toString());
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PluginDependency other = (PluginDependency) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        if (this.versionInterval != other.versionInterval && (this.versionInterval == null || !this.versionInterval.equals(other.versionInterval))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 73 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 73 * hash + (this.versionInterval != null ? this.versionInterval.hashCode() : 0);
        return hash;
    }
    
    public static PluginDependency parse(String str) throws ParseException{
        return (str==null || str.trim().length()==0)?null:new PluginDependency(str);
    }
}
