/*
 * AnsFieldList.java
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
package org.ingatan.component.answerfield;

import java.awt.AWTKeyStroke;
import org.ingatan.ThemeConstants;
import org.ingatan.component.text.SimpleTextArea;
import org.ingatan.component.text.SimpleTextField;
import org.ingatan.io.IOManager;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import org.jdom.DataConversionException;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

/**
 * BasicTextField answer field. At library manager time, takes a list of possible correct answers, and at
 * quiz time it allows the user to enter some text.
 * @author Thomas Everingham
 * @version 1.o
 */
public class AnsFieldList extends JPanel implements IAnswerField {

    /**
     * Correct answers of the list. Each element represents one list
     * item, and possible correct answers for list items are separated within
     * elements by the double comma (,,).
     */
    private String[] correctAnswers = new String[0];
    /**
     * Array of simple text fields that have been added in the quiz context. Each field
     * corresponds to the list entry in the <code>correctAnswers</code> array.
     */
    private QuizTimeEditor[] txtListItemFields = new QuizTimeEditor[0];
    /**
     * Spinner for the user to set the number of marks to award per correct list item entry.
     */
    private JSpinner spinMarks = new JSpinner(new SpinnerNumberModel(1, 0, 20, 1));
    /**
     * Checkbox allowing user to set wheter or not the order of the list items matters when marking.
     */
    private JCheckBox chkOrder = new JCheckBox("Order matters", false);
    /**
     * Checkbox allowing the user to set whether or not a hints button should be shown. The hints button
     * will give the first letter of a randomly selected list item of an empty list item field, or the
     * next letter of a partially filled (and correct) list item.
     */
    private JCheckBox chkHints = new JCheckBox("Allow hints");
    /**
     * Whether or not this component is currently being displayed in the library
     * manager (<code>true</code> value) or quiz time (<code>false</code> value).
     */
    private boolean inLibManager = false;
    /**
     * Label for the marks per entry spinner.
     */
    private JLabel lblMarks = new JLabel("Marks per list entry: ");
    /**
     * Text entry field. List items are separated in this text area at
     * edit time by putting each item on a new line.
     */
    private SimpleTextArea txtArea = new SimpleTextArea();
    /**
     * Instruction label indicating how to specify multiple correct answers and list items
     */
    private JLabel lblInstruct = new JLabel("<html>Separate acceptable answers using double comma ,,<br>"
            + "Separate list items by putting each on a new line.");
    /**
     * Scroller for the txtArea used in the edit context.
     */
    private JScrollPane scroller = new JScrollPane(txtArea);
    /**
     * Button for giving the user a hint during quiz time. Adds a letter to a randomly selected text field.
     */
    private JButton btnGiveHint = new JButton(new GiveHintAction());

    /**
     * Create a new instance of <code>AnsFieldList</code>.
     */
    public AnsFieldList() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setOpaque(false);
        this.setMaximumSize(new Dimension(350, 700));

        lblInstruct.setFont(ThemeConstants.niceFont.deriveFont(Font.ITALIC).deriveFont(9.5f));
        lblMarks.setFont(ThemeConstants.niceFont.deriveFont(Font.ITALIC));
        lblInstruct.setHorizontalTextPosition(SwingConstants.LEFT);
        lblInstruct.setAlignmentX(LEFT_ALIGNMENT);

        ((JSpinner.DefaultEditor) spinMarks.getEditor()).getTextField().setEditable(false);

        chkOrder.setFont(ThemeConstants.niceFont);
        chkOrder.setOpaque(false);
        chkOrder.setToolTipText("If this is selected, the user must fill in the list in the order that it appears here.");
        chkHints.setFont(ThemeConstants.niceFont);
        chkHints.setOpaque(false);
        chkHints.setToolTipText("If selected, a 'hint' button is shown at quiz time. Shows one letter per click.");

        btnGiveHint.setFont(ThemeConstants.niceFont);
        btnGiveHint.setAlignmentX(LEFT_ALIGNMENT);
        btnGiveHint.setToolTipText("Give one letter for a randomly selected item.");

        txtArea.setAlignmentX(LEFT_ALIGNMENT);


        rebuild();
    }

    /**
     * Removes all components and adds them again based on the current context (library editor or quiz mode).
     */
    private void rebuild() {
        this.removeAll();

        if (inLibManager) {
            txtArea.setMaximumSize(new Dimension(400, 100));
            txtArea.setFont(ThemeConstants.tableCellEditorFont);
            txtArea.setBorder(BorderFactory.createLineBorder(ThemeConstants.backgroundUnselected.darker()));
            txtArea.setAlignmentX(LEFT_ALIGNMENT);

            this.add(lblInstruct);
            lblInstruct.setAlignmentX(LEFT_ALIGNMENT);
            this.add(Box.createVerticalStrut(4));
            this.add(scroller);
            scroller.setAlignmentX(LEFT_ALIGNMENT);
            this.add(Box.createVerticalStrut(4));

            Box horiz = Box.createHorizontalBox();
            horiz.setMaximumSize(new Dimension(160, 30));
            horiz.add(lblMarks);
            horiz.setAlignmentX(LEFT_ALIGNMENT);
            horiz.add(spinMarks);
            this.add(horiz);

            horiz = Box.createHorizontalBox();
            horiz.setMaximumSize(new Dimension(200, 30));
            horiz.add(chkOrder);
            horiz.setAlignmentX(LEFT_ALIGNMENT);
            horiz.add(Box.createHorizontalStrut(4));
            horiz.add(chkHints);
            this.add(horiz);

            String cat = "";
            for (int i = 0; i < correctAnswers.length; i++) {
                cat += correctAnswers[i] + ((i == correctAnswers.length - 1) ? "" : "\n");
            }
            txtArea.setText(cat);
        } else {
            txtListItemFields = new QuizTimeEditor[correctAnswers.length];
            for (int i = 0; i < correctAnswers.length; i++) {
                //add a text field for each item
                txtListItemFields[i] = new QuizTimeEditor(i);
                txtListItemFields[i].setMaximumSize(new Dimension(250, 30));
                txtListItemFields[i].setAlignmentX(LEFT_ALIGNMENT);
                this.add(txtListItemFields[i]);
                if (chkHints.isSelected()) {
                    this.add(btnGiveHint);
                }
            }
        }
    }

    @Override
    public void requestFocus() {
        if (txtListItemFields.length >= 1) {
            txtListItemFields[0].requestFocus();
        }
    }

    public String getDisplayName() {
        return "Fill in the List";
    }

    public float checkAnswer() {
        int correctAnsCount = 0;

        for (int i = 0; i < txtListItemFields.length; i++) {
            if (txtListItemFields[i].isCorrect()) {
                correctAnsCount++;
            }
        }

        return ((float) correctAnsCount / txtListItemFields.length);
    }

    /**
     * Checks whether or not the specified <code>query</code> exists within the
     * <code>correctAnswers</code> array. Note: case insensitive.
     * @param query the string to look for.
     * @return <code>true</code> if the specified <code>query</code> exists as a
     * correct answer within the <code>correctAnswers</code> array.
     */
    private boolean isInAnswerArray(String[] possible, String query) {
        for (int i = 0; i < possible.length; i++) {
            if (possible[i].trim().toLowerCase().equals(query.toLowerCase().trim())) {
                //query was found, return true
                return true;
            }
        }

        //query was not found.
        return false;
    }

    public int getMaxMarks() {
        return correctAnswers.length * ((SpinnerNumberModel) spinMarks.getModel()).getNumber().intValue();
    }

    public int getMarksAwarded() {
        return (int) (getMaxMarks() * checkAnswer());
    }

    public void displayCorrectAnswer() {
        //get an array of the text field 
        String[] txtListStringArray = new String[txtListItemFields.length];
        for (int i = 0; i < txtListItemFields.length; i++) {
            txtListStringArray[i] = txtListItemFields[i].getText().trim().toLowerCase();
            txtListItemFields[i].setEnabled(false);
        }

        btnGiveHint.setVisible(false);

        //temp variable for the label of the current quiz time editor; set the
        //correct answer to this label.
        JLabel curLabel;
        for (int i = 0; i < txtListItemFields.length; i++) {
            txtListItemFields[i].setEnabled(false);
            curLabel = txtListItemFields[i].getAnswerDisplayLabel();
            curLabel.setText(correctAnswers[i].replace(",,", "|"));
            curLabel.setVisible(true);

            //set up appropriately coloured correct answer labels
            if ((chkOrder.isSelected() == false) && (isInAnswerArray(txtListStringArray, correctAnswers[i].trim().toLowerCase()))) {
                curLabel.setForeground(ThemeConstants.quizPassGreen);
            } else if ((chkOrder.isSelected() == false) && (!isInAnswerArray(txtListStringArray, correctAnswers[i].trim().toLowerCase()))) {
                curLabel.setForeground(ThemeConstants.quizFailRed);
            } else if ((chkOrder.isSelected()) && (txtListItemFields[i].getText().trim().toLowerCase().equals(correctAnswers[i].trim().toLowerCase()))) {
                curLabel.setForeground(ThemeConstants.quizPassGreen);
            } else if ((chkOrder.isSelected()) && (!txtListItemFields[i].getText().trim().toLowerCase().equals(correctAnswers[i].trim().toLowerCase()))) {
                curLabel.setForeground(ThemeConstants.quizFailRed);
            }

        }

        this.validate();
    }

    public void setContext(boolean inLibraryContext) {
        inLibManager = inLibraryContext;
        rebuild();
    }

    public String writeToXML() {
        //create JDOM document and root element
        Document doc = new Document();
        Element rootElement = new Element(this.getClass().getName());
        doc.setRootElement(rootElement);
        rootElement.setAttribute("marks", String.valueOf(((SpinnerNumberModel) spinMarks.getModel()).getNumber().intValue()));
        rootElement.setAttribute("allowHints", String.valueOf(chkHints.isSelected()));
        rootElement.setAttribute("orderMatters", String.valueOf(chkOrder.isSelected()));
        //version field allows future versions of this field to be back compatible.
        //especially important for default fields!
        rootElement.setAttribute("version", "1.0");

        correctAnswers = txtArea.getText().split("\n");
        for (int i = 0; i < correctAnswers.length; i++) {
            rootElement.addContent(new Element("item").setText(correctAnswers[i]));
        }

        //return the XML document as String representation
        return new XMLOutputter().outputString(doc);
    }

    public void readInXML(String xml) {
        //nothing to parse, so leave
        if (xml.trim().equals("") == true) {
            return;
        }


        //try to build document from input string
        SAXBuilder sax = new SAXBuilder();
        Document doc = null;
        try {
            doc = sax.build(new StringReader(xml));
        } catch (JDOMException ex) {
            Logger.getLogger(AnsFieldList.class.getName()).log(Level.SEVERE, "While trying to create a JDOM document in the readInXML method.", ex);
        } catch (IOException ex) {
            Logger.getLogger(AnsFieldList.class.getName()).log(Level.SEVERE, "While trying to create a JDOM document in the readInXML method.", ex);
        }

        //nothing to parse, so leave
        if (doc == null) {
            return;
        }

        try {
            ((SpinnerNumberModel) spinMarks.getModel()).setValue(Integer.valueOf(doc.getRootElement().getAttributeValue("marks")));
            chkHints.setSelected(doc.getRootElement().getAttribute("allowHints").getBooleanValue());
            chkOrder.setSelected(doc.getRootElement().getAttribute("orderMatters").getBooleanValue());

            //read in the correct answers.
            Object[] elements = doc.getRootElement().getChildren("item").toArray();
            correctAnswers = new String[elements.length];
            txtArea.setText("");
            for (int i = 0; i < elements.length; i++) {
                correctAnswers[i] = ((Element) elements[i]).getText();
                txtArea.setText(txtArea.getText() + ((i == 0) ? "" : "\n") + correctAnswers[i]);
            }
        } catch (DataConversionException ex) {
            Logger.getLogger(AnsFieldList.class.getName()).log(Level.SEVERE, "While trying to get the boolean values for allowHints or orderMatters (reading in XML)", ex);
        }

    }

    public String getParentLibraryID() {
        return ""; //not implemented as this answer field does not require access to images.
    }

    public void setParentLibraryID(String id) {
        return; //not implemented as this answer field does not require access to images.
    }

    /**
     * Simply a <code>JPanel</code> containing a <code>SimpleTextField</code> for user input and
     * a <code>JLabel</code> to display the correct answer beside the field at the appropraite time.
     * This is used at quiz time for user input.
     */
    private class QuizTimeEditor extends JPanel implements Comparable<QuizTimeEditor> {

        /**
         * The text field that accepts the user's guess for this list item at
         * quiz time.
         */
        private SimpleTextField txtField = new SimpleTextField();
        /**
         * The label used to show the correct answer when appropriate.
         */
        private JLabel lblAnswerDisplay = new JLabel();
        /**
         * The index of this editor in the array of editors. This allows access to the correctAnswer
         * element that corresponds to this editor, when order is of importance.
         */
        private int arrayIndex;

        /**
         * Creates a new instance of <code>QuizTimeEditor</code>.
         * @param arrayIndex the index of this editor in the array of editors. This allows access to the correctAnswer
         * element that corresponds to this editor, when order is of importance.
         */
        public QuizTimeEditor(int arrayIndex) {
            this.arrayIndex = arrayIndex;
            this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
            lblAnswerDisplay.setFont(ThemeConstants.niceFont);
            this.add(txtField);
            this.add(Box.createHorizontalStrut(4));
            this.add(lblAnswerDisplay);
            //not visible until the answers are displayed
            lblAnswerDisplay.setVisible(false);
            txtField.setMaximumSize(new Dimension(160, 30));
            txtField.setMinimumSize(new Dimension(160, 30));

            //set both tab and enter to be active focus traversal keys
            HashSet<AWTKeyStroke> keyset = new HashSet<AWTKeyStroke>();
            keyset.add(AWTKeyStroke.getAWTKeyStroke(KeyEvent.VK_ENTER, 0));
            keyset.add(AWTKeyStroke.getAWTKeyStroke(KeyEvent.VK_TAB, 0));
            txtField.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, keyset);
        }

        @Override
        public void setEnabled(boolean enabled) {
            super.setEnabled(enabled);
            txtField.setEnabled(false);
        }

        /**
         * Get the content of the text field.
         * @return the content of the text field.
         */
        public String getText() {
            return txtField.getText();
        }

        /**
         * Sets the content of the text field.
         * @param text the required content of the text field.
         */
        public void setText(String text) {
            txtField.setText(text);
        }

        /**
         * Gets the label used to display the correct answer.
         * @return the label used to display the correct answer.
         */
        public JLabel getAnswerDisplayLabel() {
            return lblAnswerDisplay;
        }

        /**
         * Checks whether the content of this quiz-time answer field is correct, depending on whether or not
         * order has been marked as important.
         * @return <code>true</code> if the answer provided in this field is correct.
         */
        public boolean isCorrect() {
            //order matters, and this entry is correct
            if ((chkOrder.isSelected()) && (txtField.getText().toLowerCase().trim().equals(correctAnswers[arrayIndex].toLowerCase().trim()))) {
                return true;
            } //order does not matter and this entry is correct
            else if ((chkOrder.isSelected() == false) && (isInAnswerArray(correctAnswers, txtField.getText()))) {
                return true;
            } //order matters, and this entry is NOT correct
            else if ((chkOrder.isSelected()) && (txtField.getText().toLowerCase().trim().equals(correctAnswers[arrayIndex].toLowerCase().trim()) == false)) {
                return false;
            } //order does not matter and this entry is NOT correct
            else if ((chkOrder.isSelected() == false) && (isInAnswerArray(correctAnswers, txtField.getText()) == false)) {
                return false;
            }

            //should never happen
            return false;
        }

        public int compareTo(QuizTimeEditor o) {
            if ((this.lblAnswerDisplay.equals(o.lblAnswerDisplay)) && (this.txtField.equals(o.txtField))) {
                return 0;
            } else {
                return 1;
            }
        }
    }

    /**
     * Action fired when the user presses the 'give hint' button in quiz time.
     */
    private class GiveHintAction extends AbstractAction {

        public GiveHintAction() {
            super("Hint");
        }

        public void actionPerformed(ActionEvent e) {
            //whether or not an appropriate editor has been found. An appropriate
            //editor is one that is either empty, or that already starts with the letters
            //of the correct answer. Not one that has incorrect content.
            boolean foundAppropriate = false;
            //only want to look for an editor a maximum of 40 times.
            int timesLooked = 0;
            //The appropriate editor that has been chosen.
            QuizTimeEditor chosenEditor;

            while (!foundAppropriate && (timesLooked < 40)) {
                chosenEditor = txtListItemFields[IOManager.random.nextInt(txtListItemFields.length)];
                if ((chosenEditor.getText().isEmpty()) || (startsWithCorrectAnswer(chosenEditor.getText()))) {
                    //ensure that the correctanswer is long enough
                    if (correctAnswers[chosenEditor.arrayIndex].length() > chosenEditor.getText().length()) {
                        chosenEditor.setText(correctAnswers[chosenEditor.arrayIndex].substring(0, chosenEditor.getText().length() + 1).toLowerCase());
                        foundAppropriate = true;
                    }
                }
                timesLooked++;
            }
        }

        public boolean startsWithCorrectAnswer(String test) {
            for (int i = 0; i < correctAnswers.length; i++) {
                if (correctAnswers[i].trim().toLowerCase().startsWith(test.trim().toLowerCase())) {
                    //do not want to indicate that this is an appropriate editor if the answer is already fully entered.
                    if (correctAnswers[i].trim().toLowerCase().equals(test.trim().toLowerCase())) {
                        return false;
                    } else {
                        return true;
                    }
                }
            }
            return false;
        }
    }
}
