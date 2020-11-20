/**
 * ====================================================================
 *            thevpc-common-md : Simple Markdown Manipulation Library
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
package net.thevpc.commons.md;

/**
 *
 * @author vpc
 */
public class MdTable extends MdAbstractElement{
    private MdColumn[] columns;
    private MdRow[] rows;

    public MdTable(MdColumn[] columns, MdRow[] rows) {
        this.columns = columns;
        this.rows = rows;
    }

    public MdColumn[] getColumns() {
        return columns;
    }

    public MdRow[] getRows() {
        return rows;
    }

    @Override
    public MdElementType getElementType() {
        return MdElementType.TABLE;
    }
    
    
}
