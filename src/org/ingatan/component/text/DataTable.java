/*
 * DataTable.java
 *
 * Copyright (C) 2011 Thomas Everingham
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; version 3 of the License.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * A copy of the GNU General Public License can be found in the file
 * LICENSE.txt provided with the source distribution of this program (see
 * the META-INF directory in the source jar). This license can also be
 * found on the GNU website at http://www.gnu.org/licenses/gpl.html.
 *
 * If you did not receive a copy of the GNU General Public License along
 * with this program, contact the lead developer, or write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301, USA.
 *
 * If you find this program useful, please tell me about it! I would be delighted
 * to hear from you at tom.ingatan@gmail.com.
 */
package org.ingatan.component.text;

import org.ingatan.ThemeConstants;
import java.awt.Component;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Enumeration;
import java.util.EventObject;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.DropMode;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.TransferHandler;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 * A <code>JTable</code> based table supporting dynamic resizing, swapping of
 * data columns, and addition of rows, etc. This is for use in the <code>TableQuestion</code>
 * question type. This question type is for flashcard style questions, such as vocabulary
 * training. Two columns of data are present, each row corresponding to one flash card.
 *
 * @author Thomas Everingham
 * @version 1.0
 */
public class DataTable extends JTable {

    /**
     * Table cell editor.
     */
    protected SimpleTextField editor = new SimpleTextField("");
    /**
     * Custom table cell editor allowing for automatic selection upon edit mode,
     * and other custom behaviour.
     */
    public CustomTableCellEditor mtce = new CustomTableCellEditor(editor);
    /**
     * Table model for this table. The override simply adds the custom cell editor
     * whenever the setDataVector method is called.
     */
    protected DefaultTableModel tblModel = new DefaultTableModel() {

        @Override
        public void setDataVector(Object[][] dataVector, Object[] columnTitles) {
            super.setDataVector(dataVector, columnTitles);
            Enumeration en = DataTable.this.getColumnModel().getColumns();
            while (en.hasMoreElements()) {
                ((TableColumn) en.nextElement()).setCellEditor(mtce);
            }
        }
    };
    /**
     * Whether the enter key moves the cell selection to next column, or
     * whether it moves the selection down a row.
     */
    public boolean enterMovesLeftToRight = true;

    /**
     * Creates a new DataTable instance.
     */
    public DataTable() {
        super();
        super.setModel(tblModel);

        editor.addFocusListener(new EditorFocusListener());
        editor.setBorder(BorderFactory.createLineBorder(ThemeConstants.borderUnselected));
        editor.setFont(ThemeConstants.tableCellEditorFont);
        this.setRowHeight(ThemeConstants.tableRowHeights);

        this.setOpaque(false);

        //set up the table
        tblModel.addColumn("Questions");
        tblModel.addColumn("Answers");

        String[] strRow = new String[tblModel.getColumnCount()];
        for (int i = 0; i < strRow.length; i++) {
            strRow[i] = "";
        }
        tblModel.addRow(strRow);
        tblModel.addRow(strRow);


        super.getColumnModel().getColumn(0).setCellEditor(mtce);
        super.getColumnModel().getColumn(1).setCellEditor(mtce);
        super.setDefaultEditor(String.class, mtce);

        //this.setSelectionBackground(ThemeConstants.backgroundUnselectedHover);
        this.setBorder(BorderFactory.createLineBorder(gridColor));

        this.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        //this.setCellSelectionEnabled(true);
        this.setDragEnabled(true);
        this.setTransferHandler(new TableTransferHandler());
        this.setDropMode(DropMode.INSERT_ROWS);


        this.setUpKeyBindings();
        this.setUpEditorInputMaps();
    }

    /**
     * Setup the input/action maps for this table.
     */
    public void setUpKeyBindings() {
        InputMap inMap = this.getInputMap(DataTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        ActionMap aMap = this.getActionMap();

        inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0), "checkToDeleteLastRow");
        inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "checkToDeleteLastRow");
        inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "selectNextColumnCellCreateNew");
        inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "cancel");
        inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "selectPreviousColumnCell");
        inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "selectNextColumnCell");
        inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0), "selectNextColumnCell");
        inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "selectPreviousRowCell");
        inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "selectNextRowCell");
        inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP, 0), "selectFirstRow");
        inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_DOWN, 0), "selectLastRow");
        inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, KeyEvent.CTRL_DOWN_MASK), "null");
        inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK), "copy");
        inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_DOWN_MASK), "paste");
        inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_DOWN_MASK), "cut");

        aMap.put("selectNextColumnCellCreateNew", new selectNextColumnCell());
        aMap.put("checkToDeleteLastRow", new DeleteRowAction());
        aMap.put("null", new NullAction());
        aMap.put("copy", new CopyCellsAction());
        aMap.put("cut", new CutCellsAction());
        aMap.put("paste", new PasteCellsAction());


        this.setInputMap(DataTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT, inMap);
    }

    /**
     * Sets up the cell editor's action and input maps.
     */
    protected void setUpEditorInputMaps() {
        //when  the ancestor of a focussed component, the symbol menu is probably showing, so don't
        //do anything. Let the symbol menu carry out its own actions.
        InputMap inMap = editor.getInputMap(DataTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        ActionMap aMap = editor.getActionMap();
        inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "null");
        inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "null");
        inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "null");
        inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "null");
        inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "null");
        inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "null");
        inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0), "null");


        aMap.put("null", new NullAction());
        editor.setInputMap(DataTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT, inMap);
        editor.setActionMap(aMap);



        inMap = editor.getInputMap(DataTable.WHEN_FOCUSED);
        inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "selectNextColumnCellCreateNew");//"notify-field-accept");
        inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "caret-backward");
        inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "caret-forward");
        inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "caret-up");
        inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "caret-down");
        inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "cancel");

        aMap.put("selectNextColumnCellCreateNew", new selectNextColumnCell());
        aMap.put("cancel", new CancelAction());

        editor.setInputMap(DataTable.WHEN_FOCUSED, inMap);
        editor.setActionMap(aMap);
    }

    //This action will select the next column cell, or create a new one if at the
    //bottom of the table.
    private class selectNextColumnCell extends AbstractAction {

        public void actionPerformed(ActionEvent e) {
            if (DataTable.this.isEditing()) {
                DataTable.this.getCellEditor().stopCellEditing();
            }
            //if current cell is on the far right hand column
            if ((DataTable.this.getSelectedColumn() == DataTable.this.getColumnCount() - 1) || (!enterMovesLeftToRight)) {
                //if current cell is on the very bottom row
                if (DataTable.this.getSelectedRow() == DataTable.this.getRowCount() - 1) {
                    //create a new row and move to the first cell of that row.
                    tblModel.addRow(new String[]{"", ""});
                }
            }
            if (enterMovesLeftToRight) {
                DataTable.this.getActionMap().get("selectNextColumnCell").actionPerformed(new ActionEvent(DataTable.this, 0, ""));
            } else {
                DataTable.this.getActionMap().get("selectNextRowCell").actionPerformed(new ActionEvent(DataTable.this, 0, ""));
            }
        }
    }

    //Do nothing. This is necessary so that an ancestor's action doesn't take over.
    private class NullAction extends AbstractAction {

        public void actionPerformed(ActionEvent e) {
        }
    }

    private class CancelAction extends AbstractAction {

        public void actionPerformed(ActionEvent e) {
            DataTable.this.getActionMap().get("cancel").actionPerformed(new ActionEvent(DataTable.this, 0, ""));
        }
    }

    /**
     * Deletes a row if it is empty, or otherwise clears the current cell and moves
     * the selection back by one position. If there is only one row left in the table,
     * then it will be cleared, but not deleted.
     */
    private class DeleteRowAction extends AbstractAction {

        public void actionPerformed(ActionEvent e) {
            boolean textFound = false;
            //look for text anywhere in this row
            for (int i = 0; i < DataTable.this.getColumnCount(); i++) {
                if (((String) DataTable.this.getValueAt(DataTable.this.getSelectedRow(), i)).equals("") == false) {
                    textFound = true;
                    break;
                }
            }

            if ((textFound == false) && (DataTable.this.getRowCount() > 1)) {
                //delete this row, as it is empty
                tblModel.removeRow(DataTable.this.getSelectedRow());
                //move to the bottom right hand corner of the table
                DataTable.this.setColumnSelectionInterval(DataTable.this.getColumnCount() - 1, DataTable.this.getColumnCount() - 1);
                DataTable.this.setRowSelectionInterval(DataTable.this.getRowCount() - 1, DataTable.this.getRowCount() - 1);
            } else {
                tblModel.setValueAt("", DataTable.this.getSelectedRow(), DataTable.this.getSelectedColumn());
                if (DataTable.this.getSelectedColumn() == 0) {
                    textFound = false;
                    //look for text anywhere in this row
                    for (int i = 0; i < DataTable.this.getColumnCount(); i++) {
                        if (((String) DataTable.this.getValueAt(DataTable.this.getSelectedRow(), i)).equals("") == false) {
                            textFound = true;
                            break;
                        }
                    }

                    if ((textFound == false) && (DataTable.this.getRowCount() > 1)) {
                        tblModel.removeRow(DataTable.this.getSelectedRow());
                        //move to the bottom right hand corner of the table
                        DataTable.this.setColumnSelectionInterval(DataTable.this.getColumnCount() - 1, DataTable.this.getColumnCount() - 1);
                        DataTable.this.setRowSelectionInterval(DataTable.this.getRowCount() - 1, DataTable.this.getRowCount() - 1);
                        return;
                    }
                }
                DataTable.this.getActionMap().get("selectPreviousColumnCell").actionPerformed(new ActionEvent(DataTable.this, 0, ""));
            }
        }
    }

    /**
     * Copy the content of the currently selected cells.
     */
    private class CopyCellsAction extends AbstractAction {

        public void actionPerformed(ActionEvent e) {
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            DataTable.this.getTransferHandler().exportToClipboard(DataTable.this, clipboard, TransferHandler.COPY);
        }
    }

    /**
     * Paste current clipboard data onto the table. If in the correct format,
     * (rows separated by a new line, entries within a row separated by <;>,
     * then the rows and columns are reconstructed.
     */
    private class PasteCellsAction extends AbstractAction {

        public void actionPerformed(ActionEvent e) {
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            DataTable.this.getTransferHandler().importData(DataTable.this, clipboard.getContents(DataTable.this));
        }
    }

    /**
     * Cut the currently selected cells.
     */
    private class CutCellsAction extends AbstractAction {

        public void actionPerformed(ActionEvent e) {
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            DataTable.this.getTransferHandler().exportToClipboard(DataTable.this, clipboard, TransferHandler.MOVE);
        }
    }

    /**
     * Ensures that when the editor loses focus to the symbol menu, the symbol
     * menu gets the focus.
     */
    private class EditorFocusListener implements FocusListener {

        public void focusGained(FocusEvent e) {
        }

        public void focusLost(FocusEvent e) {
            if (editor.getSymbolMenu().isVisible()) {
                editor.getSymbolMenu().requestFocus();
            }
        }
    }

    /*
     * This override simply adds that the editor component should be given focus
     * at commencement of the edit.
     */
    @Override
    public boolean editCellAt(int row, int column, EventObject e) {
        boolean retVal = super.editCellAt(row, column, e);
        if (retVal) {
            editorComp.requestFocus();
        }
        return retVal;
    }

    /**
     * Selects all text when the cell editor is initiated so that the user may easily
     * type over what has already been entered.
     */
    public class CustomTableCellEditor extends DefaultCellEditor {

        public CustomTableCellEditor(JTextField field) {
            super(field);
            super.setClickCountToStart(2);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            if (value == null) {
                ((JTextField) editorComponent).setText("");
            } else {
                ((JTextField) editorComponent).setText(value.toString());
            }
            ((JTextField) editorComponent).selectAll();
            return ((JTextField) editorComponent);
        }

        @Override
        public boolean shouldSelectCell(EventObject anEvent) {
            ((JTextField) editorComponent).selectAll();
            return super.shouldSelectCell(anEvent);
        }
    }

    /**
     * String transfer handler abstract class.
     */
    private abstract class StringTransferHandler extends TransferHandler {

        protected abstract String exportString(JComponent c);

        protected abstract void importString(JComponent c, String str);

        protected abstract void cleanup(JComponent c, boolean remove);

        @Override
        protected Transferable createTransferable(JComponent c) {
            return new StringSelection(exportString(c));
        }

        @Override
        public int getSourceActions(JComponent c) {
            return COPY_OR_MOVE;
        }

        @Override
        public boolean importData(JComponent c, Transferable t) {
            if (canImport(c, t.getTransferDataFlavors())) {
                try {
                    String str = (String) t.getTransferData(DataFlavor.stringFlavor);
                    importString(c, str);
                    return true;
                } catch (UnsupportedFlavorException ufe) {
                } catch (IOException ioe) {
                }
            }

            return false;
        }

        @Override
        protected void exportDone(JComponent c, Transferable data, int action) {
            cleanup(c, action == MOVE);
        }

        @Override
        public boolean canImport(JComponent c, DataFlavor[] flavors) {
            for (int i = 0; i < flavors.length; i++) {
                if (DataFlavor.stringFlavor.equals(flavors[i])) {
                    return true;
                }
            }
            return false;
        }
    }

    /*
     * Table Transfer Handler
     */
    private class TableTransferHandler extends StringTransferHandler {

        private int[] rows = null; //indices of selected rows
        private int addIndex = -1; //Location where items were added
        private int addCount = 0; //Number of items added.

        protected String exportString(JComponent c) {
            JTable table = (JTable) c;
            rows = table.getSelectedRows();
            int colCount = table.getColumnCount();

            StringBuffer buff = new StringBuffer();

            for (int i = 0; i < rows.length; i++) {
                for (int j = 0; j < colCount; j++) {
                    Object val = table.getValueAt(rows[i], j);
                    buff.append(val == null ? "" : val.toString());
                    if (j != colCount - 1) {
                        buff.append("<;>");
                    }
                }
                if (i != rows.length - 1) { //delimit rows from one another with new line
                    buff.append("\n");
                }
            }


            return buff.toString();
        }

        protected void importString(JComponent c, String str) {
            JTable target = (JTable) c;
            DefaultTableModel model = (DefaultTableModel) target.getModel();
            int index = -1;
            try {
                index = target.getDropLocation().getRow();
            } catch (NullPointerException e) {
                index = target.getSelectedRow() + 1;
            }

            //Prevent the user from dropping data back on itself.
            //For example, if the user is moving rows #4,#5,#6 and #7 and
            //attempts to insert the rows after row #5, this would
            //be problematic when removing the original rows.
            //So this is not allowed.
            if (rows != null && index >= rows[0]
                    && index <= rows[rows.length - 1]) {
                rows = null;
                return;
            }

            int max = model.getRowCount();
            if (index < 0) {
                index = max;
            } else if (index > max) {
                index = max;
            }
            addIndex = index;
            String[] values = str.split("\n");
            addCount = values.length;
            for (int i = 0; i < values.length; i++) {
                model.insertRow(index, values[i].split("<;>"));
                index++;
            }
        }

        protected void cleanup(JComponent c, boolean remove) {
            JTable source = (JTable) c;
            if (remove && rows != null) {
                DefaultTableModel model = (DefaultTableModel) source.getModel();

                //If we are moving items around in the same table, we
                //need to adjust the rows accordingly, since those
                //after the insertion point have moved.
                if (addCount > 0) {
                    for (int i = 0; i < rows.length; i++) {
                        if (rows[i] > addIndex) {
                            rows[i] += addCount;
                        }
                    }
                }
                for (int i = rows.length - 1; i >= 0; i--) {
                    if (rows[i] < model.getRowCount()) {
                        model.removeRow(rows[i]);
                    }
                }
                if (model.getRowCount() == 0) // if this operation has removed all rows
                {
                    String[] strRow = new String[model.getColumnCount()];
                    for (int i = 0; i < strRow.length; i++) {
                        strRow[i] = "";
                    }

                    model.addRow(strRow);
                }

            }
            rows = null;
            addCount = 0;
            addIndex = -1;
        }
    }
}
