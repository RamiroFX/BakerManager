/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;

import javax.swing.table.DefaultTableModel;

/*
 * 
 */
public class MyDefaultTableModel extends DefaultTableModel {
    private boolean[][] editable_cells; // 2d array to represent rows and columns

    public MyDefaultTableModel() {
    }

    
    private MyDefaultTableModel(int rows, int cols) { // constructor
        super(rows, cols);
        this.editable_cells = new boolean[rows][cols];
    }

    @Override
    public boolean isCellEditable(int row, int col) { // custom isCellEditable function
        //return this.editable_cells[row][col];
        return col == 0 || col==2 || col==5 || col==6 || col==7 ? false : true;
    }

    public void setCellEditable(int row, int col, boolean value) {
        this.editable_cells[row][col] = value; // set cell true/false
        
        this.fireTableCellUpdated(row, col);
    }
}