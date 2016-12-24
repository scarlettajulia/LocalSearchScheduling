/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import javax.swing.JTable;

/**
 *
 * @author taufic
 */
public class Cell {
    private int row;
    private int col;
    private JTable table;
    private Object value;

    public Cell(JTable table) {
        this.table = table;
        row = table.getSelectedRow();
        col = table.getSelectedColumn();
        value = table.getValueAt(row, col);
    }

    Cell() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    public int getColumn() {
        return col;
    }
    public int getRow() {
        return row;
    }
    public Object getValue() {
        return value;
    }
    public Object getValue(int row, int col) {
        return table.getValueAt(row, col);
    }
    public JTable getTable() {
        return table;
    }
    public void swapValue(int targetRow, int targetCol) {
        Object exportValue = table.getValueAt(targetRow, targetCol);
        table.setValueAt(value, targetRow, targetCol);
        table.setValueAt(exportValue, row, col);
    }
}
