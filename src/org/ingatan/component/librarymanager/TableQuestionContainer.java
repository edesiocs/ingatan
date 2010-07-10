/*
 * TableQuestionContainer.java
 *
 * Copyright (C) 2010 Thomas Everingham
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

package org.ingatan.component.librarymanager;

import org.ingatan.ThemeConstants;
import org.ingatan.component.text.DataTable;
import org.ingatan.data.TableQuestion;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

/**
 * This question container type is used for more flashcard style questions. It is good
 * for things like vocabulary training. It consists of a dynamic table with two columns.
 * The user may set whether the questions are asked in written form (where the answer is
 * typed in by the user), multiple choice form (where several randomly chosen options are
 * taken from the data, or random alternation between written and multiple choice.
 *
 * There is also an option for setting whether or not the table data is reversible; whether
 * questions can be asked back to front.
 * @author Thomas Everingham
 * @version 1.0
 */
public class TableQuestionContainer extends AbstractQuestionContainer {

    /**
     * The table used in this container.
     */
    DataTable table = new DataTable();
    /**
     * The JScrollPane that holds the table.
     */
    JScrollPane scroller = new JScrollPane(table);
    /**
     * The pane displaying standard options for the whole table question
     */
    TableQuestionOptionPane optionPane = new TableQuestionOptionPane();
    /**
     * The question that this <code>TableQuestionContainer</code> holds.
     */
    TableQuestion tblQuestion;
    /**
     * Label indicating how the user may separate possible questions/answers in the same cell
     */
    JLabel lblSeparateAnswers = new JLabel("Separate possible answers using two commas in either column.");

    /**
     * Create a new <code>TableQuestionContainer</code> object.
     */
    public TableQuestionContainer(TableQuestion ques) {
        super(ques);
        tblQuestion = ques;
        //this sets the action for the 'enter key move right' checkbox of the option pane.
        //this checkbox allows the user to specify whether the enter key will shift the cell
        //right or down.
        optionPane.getEnterKeyMoveRight().setAction(new EnterKeyActionAssignmentAction());
        optionPane.getEnterKeyMoveRight().setSelected(true);

        lblSeparateAnswers.setFont(ThemeConstants.niceFont);

        //vertical box layout
        this.setLayoutOfContentPane(new BoxLayout(contentPanel, BoxLayout.PAGE_AXIS));
        this.contentPanel.setBorder(BorderFactory.createEmptyBorder(7, 20, 7, 20));
        this.addToContentPane(lblSeparateAnswers, false);
        this.addToContentPane(Box.createVerticalStrut(3), false);


        //add components
        this.addToContentPane(scroller, false);
        scroller.setAlignmentX(LEFT_ALIGNMENT);
        scroller.setOpaque(false);
        table.setOpaque(false);
        table.getTableHeader().setReorderingAllowed(false);
        table.getModel().addTableModelListener(new TableListener());

        this.addToContentPane(Box.createVerticalGlue(), false);
        this.addToContentPane(optionPane, false);
        optionPane.setAlignmentX(LEFT_ALIGNMENT);
        optionPane.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        optionPane.setMaximumSize(new Dimension(500,120));

        //set data
        String[] col1Data = tblQuestion.getCol1Data();
        String[] col2Data = tblQuestion.getCol2Data();
        String[][] newData = new String[col1Data.length][2];

        for (int i = 0; i < col1Data.length && i < col2Data.length; i++) {
            //there are only two columns in the TableQuestion table.
            newData[i][0] = col1Data[i];
            newData[i][1] = col2Data[i];
        }

        ((DefaultTableModel) table.getModel()).setDataVector(newData, new String[]{"Questions", "Answers"});


        //set option panel data
        optionPane.getAskInReverse().setSelected(tblQuestion.isAskInReverse());
        optionPane.getAskStyle().setSelectedIndex(tblQuestion.getQuizMethod());
        optionPane.getMarksPerAnswer().setText("" + tblQuestion.getMarksPerCorrectAnswer());
        optionPane.getFwdQuestionTemplate().setText(tblQuestion.getQuestionTemplateFwd());
        optionPane.getBwdQuestionTemplate().setText(tblQuestion.getQuestionTemplateBwd());

    }

    /**
     * This method is called by the override of the content panel's paintComponent() method
     * in the AbstractQuestionContainer. This allows for general content panel painting to
     * be taken care of by the abstract container (i.e. borders and background), and any
     * extra painting work to be carried out here. For the TableQuestionContainer,
     * when the container is minimised, a preview of table content is drawn to the content
     * pane and the table is made invisible.
     * @param g2d Graphics2D object as specified by the content panel's paintComponent() method.
     */
    @Override
    protected void paintContentPanel(Graphics2D g2d) {
        //this override is called by the override of paintComponent() of the content panel in the 
        //AbstractQuestionContainer, so that extra
        if (minimised) {
            contentPanel.setBorder(BorderFactory.createEmptyBorder(50, 1, 1, 1));
            
            //construct a string containing the first 5 rows of data, if that much
            //data exists
            String strPrint = "";
            int rowsToPreview = 5;
            for (int i = 0; i < rowsToPreview && i < table.getRowCount(); i++) {
                for (int j = 0; j < table.getColumnCount(); j++) {
                    //don't want to preview empty cells
                    if (((String) table.getValueAt(i, j)).trim().equals("") == false) {
                        strPrint += table.getValueAt(i, j);
                        if ((i < table.getRowCount() - 1) || (j < table.getColumnCount() - 1)) {
                            strPrint += ", ";
                        }
                    }
                }
            }
            if (strPrint.equals("")) {
                strPrint = "empty table";
            }

            g2d.drawString(strPrint, 10, 20);
        } else {
            scroller.setVisible(true);
            contentPanel.setBorder(BorderFactory.createEmptyBorder(7, 20, 7, 20));
            this.revalidate();
        }
    }

    private class EnterKeyActionAssignmentAction extends AbstractAction {

        public EnterKeyActionAssignmentAction() {
            super("enter key moves cell right");
        }

        public void actionPerformed(ActionEvent e) {
            if (optionPane.getEnterKeyMoveRight().isSelected()) {
                table.enterMovesLeftToRight = true;
            } else {
                table.enterMovesLeftToRight = false;
            }
        }
    }

    /**
     * Get the column 1 data
     * @return the column 1 table data.
     */
    public String[] getColumn1Data() {
        Vector v = ((DefaultTableModel) table.getModel()).getDataVector();
        String[] column = new String[v.size()];
        for (int i = 0; i < column.length; i++) {
            column[i] = (String) ((Vector) v.get(i)).get(0);
        }

        return column;
    }

    /**
     * Get the column 2 data
     * @return the column 2 table data.
     */
    public String[] getColumn2Data() {
        Vector v = ((DefaultTableModel) table.getModel()).getDataVector();
        String[] column = new String[v.size()];
        for (int i = 0; i < column.length; i++) {
            column[i] = (String) ((Vector) v.get(i)).get(1);
        }

        return column;
    }

    /**
     * Gets the option pane, from which the options can be retrieved.
     * @return the option pane for this TableQuestionContainer.
     */
    public TableQuestionOptionPane getOptionPane() {
        return optionPane;
    }


    private class TableListener implements TableModelListener {

        public void tableChanged(TableModelEvent e) {
            table.setSize(table.getWidth(), table.getModel().getRowCount()*table.getRowHeight() + 20);
            scroller.setPreferredSize(new Dimension((table.getWidth() > 30) ? table.getWidth() : 100, (table.getHeight() >= 40) ? table.getHeight() : 40));
            scroller.setMaximumSize(new Dimension(500, 500));
            Dimension d = TableQuestionContainer.this.getLayout().minimumLayoutSize(TableQuestionContainer.this);
            TableQuestionContainer.this.setPreferredSize(new Dimension((int) d.getWidth(), (int) (d.getHeight() + scroller.getPreferredSize().getHeight())));
            
        }

    }
}
