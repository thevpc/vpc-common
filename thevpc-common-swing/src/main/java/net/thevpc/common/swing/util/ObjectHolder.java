/**
 * ====================================================================
 *                        vpc-commons library
 *
 * Description: <start><end>
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
package net.thevpc.common.swing.util;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * %creationtime 4 sept. 2007 17:51:11
 */
public class ObjectHolder<T> {

    public ObjectHolder() {
    }

    public ObjectHolder(T value) {
        this.value = value;
    }

    private T value;
    public T get(){
        return value;
    }
    public void set(T t){
        value=t;
    }
}
