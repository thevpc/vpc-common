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
public class MdRow extends MdAbstractElement {

    private MdElement[] cells;
    private boolean header;

    public MdRow(MdElement[] cells, boolean header) {
        this.cells = cells;
        this.header = header;
    }

    public boolean isHeader() {
        return header;
    }

    public MdElement[] getCells() {
        return cells;
    }

    public MdElement get(int index) {
        return cells[index];
    }

    public int size(){
        return cells.length;
    }
    
    @Override
    public MdElementType getId() {
        return MdElementType.ROW;
    }

}
