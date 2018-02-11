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
 *
 * Copyright (C) 2014-2015 Taha BEN SALAH
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 * ====================================================================
 */
package net.vpc.common.classpath;

/**
 * @author Taha BEN SALAH <taha.bensalah@gmail.com>
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
