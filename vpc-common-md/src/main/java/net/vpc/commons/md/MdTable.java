/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.commons.md;

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
    public MdElementType getId() {
        return MdElementType.TABLE;
    }
    
    
}
