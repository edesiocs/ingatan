/*
 * AnsFieldTable.java
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
package org.ingatan.component.answerfield;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.util.Enumeration;
import java.util.EventObject;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import org.ingatan.ThemeConstants;
import org.ingatan.component.text.SimpleTextField;
import org.ingatan.io.IOManager;

/**
 * A fill in the table type question, with the ability to set some cells as labels.
 * @author Thomas Everingham
 * @version 1.0
 */
public class AnsFieldTable extends JPanel implements IAnswerField {

    //context variable
    private boolean inLibManager = true;
    //JButton shown initially so user can show the create table dialog
    private JButton btnCreateTable = new JButton(new CreateTableAction());
    //the table encapsulated by this answer field
    private MultiColumnTable table;

    public AnsFieldTable() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(btnCreateTable);
        this.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, ThemeConstants.borderUnselected));
        this.setOpaque(false);
    }

    public String getDisplayName() {
        return "Fill in the Table";
    }

    public boolean isOnlyForAnswerArea() {
        return true;
    }

    public float checkAnswer() {
        return 1.0f;
    }

    public int getMaxMarks() {
        return 1;
    }

    public int getMarksAwarded() {
        return 1;
    }

    public void displayCorrectAnswer() {
        
    }

    public void setContext(boolean inLibraryContext) {
        inLibManager = inLibraryContext;
    }

    public String writeToXML() {
        return "";
    }

    public void readInXML(String xml) {
        
    }

    public String getParentLibraryID() {
        //unimplemented, this answer field needs no resource read/write.
        return "";
    }

    public void setParentLibraryID(String id) {
        //unimplemented, this answer field requires no resource read/write.
    }

    public void setQuizContinueListener(ActionListener listener) {
        
    }

    public void resaveImagesAndResources(String newLibraryID) {
        //unimplemented, this answer field requires no resource read/write.
    }

    public class CreateTableAction extends AbstractAction {

        public CreateTableAction() {
            super("Create Table");
        }

        public void actionPerformed(ActionEvent e) {
            CreateTableDialog dialog = new CreateTableDialog();
            dialog.setVisible(true);
            table = new MultiColumnTable(dialog.getNumberOfColumns(), dialog.getColumnNames());
            table.setMaximumSize(new Dimension(500, 500));
            table.setMinimumSize(new Dimension(300, 80));
            table.setPreferredSize(null);
            table.setAlignmentX(LEFT_ALIGNMENT);
            table.getTableHeader().setAlignmentX(LEFT_ALIGNMENT);
            table.getTableHeader().setOpaque(false);
            AnsFieldTable.this.remove(btnCreateTable);
            AnsFieldTable.this.add(table.getTableHeader());
            AnsFieldTable.this.add(table);
        }
    }










    /***************************************************************************
     * Below: CreateTableDialog and then DataTable nested classes
     *
     */

    /**
     * A dialogue used when the user wishes to create the table for the answer field. Asks for the
     * number of columns that the table should have, and lets the user name them.
     *
     * @author Thomas Everingham
     * @version 1.0
     */
    public class CreateTableDialog extends JDialog {

        /**
         * The maximum number of columns allowed.
         */
        private final int MAX_COLS = 6;
        /**
         * The minimum number of columns allowed.
         */
        private final int MIN_COLS = 2;
        /**
         * Maximum length of column names.
         */
        private final int MAX_COL_NAME_CHARS = 15;
        /**
         * Flag set by the Cancel action to indicate that the user cancelled the question creation.
         */
        private boolean userCancelled = false;
        /**
         * Label for the number of columns field.
         */
        private JLabel lblNumCols = new JLabel("Number of Columns: ");
        /**
         * Label for the column name fields.
         */
        private JLabel lblColNames = new JLabel("Column names: ");
        /**
         *  Button the proceed with adding new TableQuestion.
         */
        private JButton btnOkay = new JButton(new ProceedAction());
        /**
         * Content pane of the dialog.
         */
        private JPanel contentPane = new JPanel();
        /**
         * Spinner to allow the user to set the number of columns for the table question.
         */
        private JSpinner spinnerColumns = new JSpinner(new SpinnerNumberModel(MIN_COLS, 2, MAX_COLS, 1));
        /**
         * Column headers for this TableQuestion.
         */
        private JTextField[] colNames = new JTextField[]{new JTextField("Col 1"), new JTextField("Col 2"), new JTextField("Col 3"), new JTextField("Col 4"), new JTextField("Col 5"), new JTextField("Col 6")};

        /**
         * Creates a new <code>LibraryEditorDialog</code>.
         * @param parent the parent window for this dialog.
         * @param newLibrary whether or not this dialog is being used to create a new library, false
         *        if this dialog is being used to edit a library.
         * @param groupName if creating a new library, then it will be added to the specified group. Put this
         * parameter as <code>null</code> if this is not desired.
         */
        public CreateTableDialog() {
            super();
            this.setModal(true);
            this.setIconImage(IOManager.windowIcon);
            this.setContentPane(contentPane);
            this.setTitle("Create Table");

            contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

            setUpGUI();

            this.setPreferredSize(new Dimension(220, 340));
            this.setMinimumSize(new Dimension(220, 340));
            this.setMaximumSize(new Dimension(220, 340));

            this.setResizable(false);

            this.setLocationRelativeTo(null);
        }

        private void setUpGUI() {
            contentPane.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
            lblNumCols.setAlignmentX(LEFT_ALIGNMENT);
            lblNumCols.setHorizontalAlignment(SwingConstants.LEFT);
            lblNumCols.setFont(ThemeConstants.niceFont);

            lblColNames.setAlignmentX(LEFT_ALIGNMENT);
            lblColNames.setHorizontalAlignment(SwingConstants.LEFT);
            lblColNames.setFont(ThemeConstants.niceFont);

            for (int i = 0; i < colNames.length; i++) {
                colNames[i].setMaximumSize(new Dimension(250, 20));
            }

            btnOkay.setMargin(new Insets(1, 1, 1, 1));
            btnOkay.setPreferredSize(new Dimension(60, 20));

            spinnerColumns.setFont(ThemeConstants.niceFont);
            spinnerColumns.setMaximumSize(new Dimension(35, 20));
            ((JSpinner.DefaultEditor) spinnerColumns.getEditor()).getTextField().setEditable(false);
            spinnerColumns.addChangeListener(new SpinChangeListener());

            Box horiz = Box.createHorizontalBox();
            horiz.add(lblNumCols);
            horiz.add(Box.createHorizontalStrut(10));
            horiz.add(spinnerColumns);
            horiz.setAlignmentX(LEFT_ALIGNMENT);
            horiz.setMaximumSize(new Dimension(200, 25));
            contentPane.add(horiz);
            contentPane.add(Box.createVerticalStrut(14));
            contentPane.add(lblColNames);

            for (int i = 0; i < colNames.length; i++) {
                contentPane.add(colNames[i]);
            }
            updateColNameFields();

            contentPane.add(Box.createVerticalStrut(10));
            btnOkay.setAlignmentX(LEFT_ALIGNMENT);
            contentPane.add(btnOkay);

            this.pack();

        }

        private void updateColNameFields() {
            for (int i = 0; i < colNames.length; i++) {
                colNames[i].setVisible((i <= (getNumberOfColumns() - 1)));
                colNames[i].setEnabled((i <= (getNumberOfColumns() - 1)));
            }
            this.repaint();
            contentPane.validate();
        }

        /**
         * Gets the number of columns that the user specified that the new TableQuestion should carry.
         * @return the number of columns, or -1 if the user cancelled.
         */
        public int getNumberOfColumns() {
            if (userCancelled) {
                return -1;
            }

            return ((SpinnerNumberModel) spinnerColumns.getModel()).getNumber().intValue();
        }

        /**
         * Gets the user-specified column names for the new TableQuestion. If the user
         * left any of the column name fields blank, they will be returned as "column #". If
         * any of the column names are > MAX_COL_NAME_CHARS characters, they are truncated to 12 characters.
         * @return the user-specified column names for the new TableQuestion. The length of the
         * returned array is the same as the number of columns that the user has chosen to use.
         */
        public String[] getColumnNames() {
            String[] retVal = new String[getNumberOfColumns()];
            String temp = "";
            for (int i = 0; i < retVal.length; i++) {
                temp = colNames[i].getText().trim();
                if (temp.isEmpty()) {
                    temp = "column " + i;
                } else if (temp.length() > MAX_COL_NAME_CHARS) {
                    temp = temp.substring(0, MAX_COL_NAME_CHARS);
                }

                retVal[i] = temp;
            }

            return retVal;
        }

        private class ProceedAction extends AbstractAction {

            public ProceedAction() {
                super("Okay");
            }

            public void actionPerformed(ActionEvent e) {
                CreateTableDialog.this.setVisible(false);
            }
        }

        private class SpinChangeListener implements ChangeListener {
            public void stateChanged(ChangeEvent e) {
                updateColNameFields();
            }
        }
    }




    //------------------------------------------------------
    /**
     * A <code>JTable</code> based table supporting dynamic resizing, swapping of
     * data columns, and addition of rows, etc.
     *
     * @author Thomas Everingham
     * @version 1.0
     */
    public class MultiColumnTable extends JTable {

        /**
         * Table cell editor.
         */
        private SimpleTextField editor = new SimpleTextField("");
        /**
         * Custom table cell editor allowing for automatic selection upon edit mode,
         * and other custom behaviour.
         */
        public CustomTableCellEditor mtce = new CustomTableCellEditor(editor);
        /**
         * Table model for this table. The override simply adds the custom cell editor
         * whenever the setDataVector method is called.
         */
        private DefaultTableModel tblModel = new DefaultTableModel() {

            @Override
            public void setDataVector(Object[][] dataVector, Object[] columnTitles) {
                super.setDataVector(dataVector, columnTitles);
                Enumeration en = MultiColumnTable.this.getColumnModel().getColumns();
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
        public MultiColumnTable(int numCols, String[] colNames) {
            super();
            super.setModel(tblModel);

            editor.addFocusListener(new EditorFocusListener());
            editor.setBorder(BorderFactory.createLineBorder(ThemeConstants.borderUnselected));
            editor.setFont(ThemeConstants.tableCellEditorFont);
            this.setRowHeight(ThemeConstants.tableRowHeights);

            this.setOpaque(false);

            //add columns
            for (int i = 0; i < numCols; i++) {
                tblModel.addColumn(colNames[i]);
                super.getColumnModel().getColumn(i).setCellEditor(mtce);
            }

            //create and add 2 rows
            String[] strRow = new String[numCols];
            for (int i = 0; i < strRow.length; i++) {
                strRow[i] = "";
            }
            tblModel.addRow(strRow);
            tblModel.addRow(strRow);


            super.setDefaultEditor(String.class, mtce);

            //this.setSelectionBackground(ThemeConstants.backgroundUnselectedHover);
            this.setBorder(BorderFactory.createLineBorder(gridColor));

            this.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
            this.setDragEnabled(false);


            this.setUpKeyBindings();
            this.setUpEditorInputMaps();
        }

        /**
         * Setup the input/action maps for this table.
         */
        public void setUpKeyBindings() {
            InputMap inMap = this.getInputMap(MultiColumnTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
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

            aMap.put("selectNextColumnCellCreateNew", new selectNextColumnCell());
            aMap.put("checkToDeleteLastRow", new DeleteRowAction());
            aMap.put("null", new NullAction());

            this.setInputMap(MultiColumnTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT, inMap);
        }

        /**
         * Sets up the cell editor's action and input maps.
         */
        protected void setUpEditorInputMaps() {
            //when  the ancestor of a focussed component, the symbol menu is probably showing, so don't
            //do anything. Let the symbol menu carry out its own actions.
            InputMap inMap = editor.getInputMap(MultiColumnTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
            ActionMap aMap = editor.getActionMap();
            inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "null");
            inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "null");
            inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "null");
            inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "null");
            inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "null");
            inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "null");
            inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0), "null");


            aMap.put("null", new NullAction());
            editor.setInputMap(MultiColumnTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT, inMap);
            editor.setActionMap(aMap);



            inMap = editor.getInputMap(MultiColumnTable.WHEN_FOCUSED);
            inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "selectNextColumnCellCreateNew");//"notify-field-accept");
            inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "caret-backward");
            inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "caret-forward");
            inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "caret-up");
            inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "caret-down");
            inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "cancel");

            aMap.put("selectNextColumnCellCreateNew", new selectNextColumnCell());
            aMap.put("cancel", new CancelAction());

            editor.setInputMap(MultiColumnTable.WHEN_FOCUSED, inMap);
            editor.setActionMap(aMap);
        }

        /**This action will select the next column cell, or create a new one if at the
         *bottom of the table.
         */
        private class selectNextColumnCell extends AbstractAction {

            public void actionPerformed(ActionEvent e) {
                //if editing a cell, then stop
                if (MultiColumnTable.this.isEditing()) {
                    MultiColumnTable.this.getCellEditor().stopCellEditing();
                }
                //if current cell is on the far right hand column
                if ((MultiColumnTable.this.getSelectedColumn() == MultiColumnTable.this.getColumnCount() - 1) || (!enterMovesLeftToRight)) {
                    //if current cell is on the very bottom row
                    if (MultiColumnTable.this.getSelectedRow() == MultiColumnTable.this.getRowCount() - 1) {
                        //create a new row and move to the first cell of that row.
                        tblModel.addRow(new String[]{"", ""});
                    }
                }
                if (enterMovesLeftToRight) {
                    MultiColumnTable.this.getActionMap().get("selectNextColumnCell").actionPerformed(new ActionEvent(MultiColumnTable.this, 0, ""));
                } else {
                    MultiColumnTable.this.getActionMap().get("selectNextRowCell").actionPerformed(new ActionEvent(MultiColumnTable.this, 0, ""));
                }
            }
        }

        //Do nothing. This is necessary so that an ancestor's action doesn't take over (added to an input map)
        private class NullAction extends AbstractAction {

            public void actionPerformed(ActionEvent e) {
            }
        }

        private class CancelAction extends AbstractAction {

            public void actionPerformed(ActionEvent e) {
                MultiColumnTable.this.getActionMap().get("cancel").actionPerformed(new ActionEvent(MultiColumnTable.this, 0, ""));
            }
        }

        /**
         * Deletes a row if it is empty, or otherwise clears the current cell and moves
         * the selection back by one position. If there is only one row left in the table,
         * then it will be cleared, but not deleted. Deletes the row from the synchronised data
         * arrays as well.
         */
        private class DeleteRowAction extends AbstractAction {

            public void actionPerformed(ActionEvent e) {
                boolean textFound = false;

                //if no selection is made
                if (MultiColumnTable.this.getSelectedRow() == -1) {
                    return;
                }
                //look for text anywhere in this row
                for (int i = 0; i < MultiColumnTable.this.getColumnCount(); i++) {
                    Object o = MultiColumnTable.this.getValueAt(MultiColumnTable.this.getSelectedRow(), i);
                    if ((o != null) && (((String) o).equals("") == false)) {
                        textFound = true;
                        break;
                    }
                }

                //if the row is empty, and there is more than 1 row left, delete the row
                if ((textFound == false) && (MultiColumnTable.this.getRowCount() > 1)) {
                    int selectedRow = MultiColumnTable.this.getSelectedRow();
                    //delete this row, as it is empty
                    tblModel.removeRow(selectedRow);
                    //move to the bottom right hand corner of the table
                    MultiColumnTable.this.setColumnSelectionInterval(MultiColumnTable.this.getColumnCount() - 1, MultiColumnTable.this.getColumnCount() - 1);
                    MultiColumnTable.this.setRowSelectionInterval(MultiColumnTable.this.getRowCount() - 1, MultiColumnTable.this.getRowCount() - 1);
                } else {
                    tblModel.setValueAt("", MultiColumnTable.this.getSelectedRow(), MultiColumnTable.this.getSelectedColumn());
                    if (MultiColumnTable.this.getSelectedColumn() == 0) {
                        textFound = false;
                        //look for text anywhere in this row
                        for (int i = 0; i < MultiColumnTable.this.getColumnCount(); i++) {
                            if (((String) MultiColumnTable.this.getValueAt(MultiColumnTable.this.getSelectedRow(), i)).equals("") == false) {
                                textFound = true;
                                break;
                            }
                        }

                        if ((textFound == false) && (MultiColumnTable.this.getRowCount() > 1)) {
                            int selectedRow = MultiColumnTable.this.getSelectedRow();
                            tblModel.removeRow(selectedRow);
                            //move to the bottom right hand corner of the table
                            MultiColumnTable.this.setColumnSelectionInterval(MultiColumnTable.this.getColumnCount() - 1, MultiColumnTable.this.getColumnCount() - 1);
                            MultiColumnTable.this.setRowSelectionInterval(MultiColumnTable.this.getRowCount() - 1, MultiColumnTable.this.getRowCount() - 1);
                            return;
                        }
                    }
                    MultiColumnTable.this.getActionMap().get("selectPreviousColumnCell").actionPerformed(new ActionEvent(MultiColumnTable.this, 0, ""));
                }
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
                field.add(new JCheckBox("label"));
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
    }
}
