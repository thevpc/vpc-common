/**
 * ==================================================================== 
 * UPA (Unstructured Persistence API)
 *    Yet another ORM Framework
 * ++++++++++++++++++++++++++++++++++
 * Unstructured Persistence API, referred to as UPA, is a genuine effort 
 * to raise programming language frameworks managing relational data in 
 * applications using Java Platform, Standard Edition and Java Platform, 
 * Enterprise Edition and Dot Net Framework equally to the next level of 
 * handling ORM for mutable data structures. UPA is intended to provide 
 * a solid reflection mechanisms to the mapped data structures while 
 * affording to make changes at runtime of those data structures. 
 * Besides, UPA has learned considerably of the leading ORM 
 * (JPA, Hibernate/NHibernate, MyBatis and Entity Framework to name a few) 
 * failures to satisfy very common even known to be trivial requirement in 
 * enterprise applications. 
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
package net.thevpc.common.classpath;

/**
 * @author thevpc
 */
public class ScanFilter {

    private String libs;
    private String types;

    public ScanFilter(String libs, String types) {
        this.libs = libs == null ? "" : libs.trim();
        this.types = types == null ? "" : types.trim();
    }

    public String getLibs() {
        return libs;
    }

    public String getTypes() {
        return types;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + (this.libs != null ? this.libs.hashCode() : 0);
        hash = 59 * hash + (this.types != null ? this.types.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ScanFilter other = (ScanFilter) obj;
        if ((this.libs == null) ? (other.libs != null) : !this.libs.equals(other.libs)) {
            return false;
        }
        if ((this.types == null) ? (other.types != null) : !this.types.equals(other.types)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ContextAnnotationStrategyFilter{" + "libs=" + libs + ", types=" + types + '}';
    }

}
