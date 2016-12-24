/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import javax.swing.DropMode;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.TransferHandler;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author taufic
 */
public class TableTransferable {
    private JTable table;
    
    public JTable createTable(String[][] jadwal) {
        String[] columnNames = {"Senin", "Selasa", "Rabu", "Kamis", "Jumat"};
        boolean[][] doneColoring = new boolean[12][6];
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 5; j++) {
                doneColoring[i][j] = false;
            }
        }
        Color[] color = new Color[18];
        table = new JTable(jadwal, columnNames);
//        {
//            int colorNumber = 0;
//            @Override
//            public Component prepareRenderer(TableCellRenderer tcr, int row, int col) {
//                Component comp = super.prepareRenderer(tcr, row, col);
//                Object value = getModel().getValueAt(row, col);
//                System.out.println("color" + colorNumber);
//                int j = col;
//                int i = row;
//                System.out.println("row :" + row);
//                System.out.println("col : " + col);
//                
//                boolean stop = false;
//                color[0] = Color.GREEN;
//                color[1] = Color.RED;
//                color[2] = Color.BLUE;
//                color[3] = Color.YELLOW;
//                color[4] = Color.PINK;
//                color[5] = Color.ORANGE;
//                color[6] = Color.MAGENTA;
//                color[7] = Color.CYAN;
//                color[8] = Color.LIGHT_GRAY;
//                color[9] = Color.GREEN;
//                color[10] = Color.RED;
//                color[11] = Color.BLUE;
//                color[12] = Color.YELLOW;
//                color[13] = Color.PINK;
//                color[14] = Color.ORANGE;
//                color[15] = Color.MAGENTA;
//                color[16] = Color.CYAN;
//                color[17] = Color.LIGHT_GRAY;
//                // mengecek apakah yang dibawah matkul itu sama..
//                
//                
//                    //System.out.println(i);
//                    
//                    
//                    if (value != null) {
//                        System.out.println(value);
//                        System.out.println(getModel().getValueAt(i+1, col));
//                        if (doneColoring[i][j] == false) {
//                            colorNumber++;
//                            comp.setBackground(color[colorNumber]);
//                            doneColoring[i][col] = true;
//                            while (j < 5 && !stop && i < 10) {
//                                if (value == getModel().getValueAt(i+1, j)) {
//                                    comp.setBackground(color[colorNumber]);
//                                    doneColoring[i+1][j] = true;
//                                } else {
//                                    stop = true;
//                                }
//                                
//                                i++;
//                            }
//                        }
//                    }
//                
//                /*if (row < 10) {
//                if (value != getModel().getValueAt(row+1, col) && getModel().getValueAt(row+1, col) != null ) {
//                    colorNumber++;
//                }}
//                if (row > 0) {
//                if (value != getModel().getValueAt(row-1, col) && getModel().getValueAt(row-1, col) != null ) {
//                    colorNumber++;
//                }}
//                if (col < 4) {
//                if (value != getModel().getValueAt(row, col+1) && getModel().getValueAt(row, col+1) != null ) {
//                    colorNumber++;
//                }}
//                if (col > 0) { 
//                if (value != getModel().getValueAt(row, col-1) && getModel().getValueAt(row, col-1) != null ) {
//                    colorNumber++;
//                }}*/
//
//                
//
//                return comp;
//            }
//        };

        table.setDragEnabled(true);
        table.setDropMode(DropMode.USE_SELECTION);
        table.setTransferHandler(new TransferAids());
        table.setRowSelectionAllowed(false);
        table.setCellSelectionEnabled(true);
        return table;
    }
    public JTable getTable() {
        return table;
    }
    
    public static final DataFlavor DATAFLAVOR_CELL = createConstanta(
            Cell.class, "application/x-java-cell");
    public class CellTransferable implements Transferable {
        private Cell cell;
        
        public CellTransferable(Cell cell) {
            this.cell = cell;
        }
        @Override
        public DataFlavor[] getTransferDataFlavors() {
            return new DataFlavor[]{DATAFLAVOR_CELL}; 
        }

        @Override
        public boolean isDataFlavorSupported(DataFlavor df) {
            boolean supported = false;
            for (DataFlavor ready : getTransferDataFlavors()) {
                if (ready.equals(df)) {
                    supported = true;
                }
            }
            return supported;
        }

        @Override
        public Object getTransferData(DataFlavor df) throws UnsupportedFlavorException, IOException {
            return cell;
        }
    }
    static protected DataFlavor createConstanta(Class kelas, String name) {
        try {
            return new DataFlavor(kelas, name);
        } catch (Exception e) {
            return null;
        }
    }
    
    public class TransferAids extends TransferHandler {

        public TransferAids() {
        }

        @Override
        public int getSourceActions(JComponent jc) {
            return MOVE;
        }

        @Override
        protected Transferable createTransferable(JComponent jc) {
            JTable table = (JTable) jc;
            int row = table.getSelectedRow();
            int col = table.getSelectedColumn();
            Object value = table.getValueAt(row, col);
            return new CellTransferable(new Cell(table));
        }

        @Override
        protected void exportDone(JComponent jc, Transferable t, int i) {
            super.exportDone(jc, t, i); //To change body of generated methods, choose Tools | Templates.
        }
        
        
        @Override
        public boolean canImport(TransferSupport ts) {
            return true;
        }

        @Override
        public boolean importData(TransferSupport ts) {
            boolean imported = false;
            Component comp = ts.getComponent();
            if (comp instanceof JTable) {
                JTable target = (JTable) comp;
                DropLocation droplocation = ts.getDropLocation();
                Point point = droplocation.getDropPoint();
                int col = target.columnAtPoint(point);
                int row = target.rowAtPoint(point);
                try {
                    Transferable transferable = ts.getTransferable();
                    Cell cell = (Cell) transferable.getTransferData(DATAFLAVOR_CELL);
                    cell.swapValue(row, col);
                    imported = true;
                } catch(UnsupportedFlavorException | IOException e) {
                    e.printStackTrace();
                }
            }
            return imported;
        }
        
    }
   
}
