/*
 * TableQuestionOptionPane.java
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

import java.awt.event.ActionEvent;
import org.ingatan.ThemeConstants;
import org.ingatan.component.text.NumericJTextField;
import org.ingatan.component.text.SimpleTextField;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionListener;
import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

/**
 * Option pane that appears below the table in the TableQuestionContainer. Includes
 * options for how the table questions are asked, whether the data is reversible,
 * and also a couple of checkboxes for the input behaviour of the table.
 *
 * @author Thomas Everingham
 * @version 1.0
 */
public class TableQuestionOptionPane extends JPanel {

    /**
     * Maximum size of the text fields and combo boxes.
     */
    private final static Dimension MAX_FIELD_SIZE = new Dimension(140, 20);
    /**
     * Minimum size of the text fields and combo boxes.
     */
    private final static Dimension MIN_FIELD_SIZE = new Dimension(50, 20);
    /**
     * The ask style determines how the questions will be asked at quiz time. A question taken
     * from the first column can be put to the user, and the answer can then be typed. This is
     * the written style. Alternatively, multiple choice questions can automatically be generated
     * from the other data in the table. This is good for the early stages of learning.
     * Both methods may be used, in which case they are used randomly during quiz time.
     */
    JComboBox askStyle = new JComboBox(new String[]{"Text field", "Multiple choice", "Random"});
    /**
     * If this is checked then, at quiz time, questions will be generated from both columns, rather than
     * just one. This is useful for vocabulary training, where the user would not want to rote learn
     * just Spanish to Swedish, for example, but also Swedish to Spanish.
     */
    JCheckBox askInReverse = new JCheckBox("can ask in reverse");
    /**
     * If this is checked then, at quiz time, questions will be generated from both columns, rather than
     * just one. This is useful for vocabulary training, where the user would not want to rote learn
     * just Spanish to Swedish, for example, but also Swedish to Spanish.
     */
    JCheckBox enterKeyMoveLeft = new JCheckBox("set by action in container class");
    /**
     * The question template allows the user to set a template for each question, where
     * the data taken from the table is substituted into the template. This makes things
     * a little nicer than just being presented with a word.
     */
    SimpleTextField fwdQuestionTemplate = new SimpleTextField();
    /**
     * The question template allows the user to set a template for each question, where
     * the data taken from the table is substituted into the template. This makes things
     * a little nicer than just being presented with a word.
     *
     * This is a second, bwd (backward) template. This field only appears if the
     * 'can ask in reverse' checkbox is ticked.
     */
    SimpleTextField bwdQuestionTemplate = new SimpleTextField();
    /**
     * listener that is notified when the fonts combo box changes value.
     */
    ActionListener listener = null;
    /**
     * Number of marks to award per correct answer.
     */
    NumericJTextField marksPerAnswer = new NumericJTextField(1);
    /**
     * Font chooser combo box.
     */
    JComboBox comboFonts;
    /**
     * Spinner for font size.
     */
    JSpinner spinnerFontSize = new JSpinner(new SpinnerNumberModel(12, 3, 200, 1));
    /**
     * "Answer field to use" label.
     */
    JLabel lblAskStyle = new JLabel("Answer field to use: ");
    /**
     * "Marks per correct answer:" label.
     */
    JLabel lblNumberMarks = new JLabel("Marks per correct answer: ");
    /**
     * "Question template (forward)" label.
     */
    JLabel lblForwardTemplate = new JLabel("Question template (forward): ");
    /**
     * "Question template (backward)" label.
     */
    JLabel lblBackwardTemplate = new JLabel("Question template (backward): ");
    /**
     * "[q] is the placeholder for both templates." label.
     */
    JLabel lblTemplateInfo = new JLabel("[q] is the placeholder for both templates.");
    /**
     * "Font:" label.
     */
    JLabel lblFont = new JLabel("Font and Size: ");

    /**
     * Creates a new <code>TableQuestionOptionPane<code>.
     */
    public TableQuestionOptionPane() {

        lblAskStyle.setFont(ThemeConstants.niceFont);
        lblNumberMarks.setFont(ThemeConstants.niceFont);
        lblForwardTemplate.setFont(ThemeConstants.niceFont);
        lblBackwardTemplate.setFont(ThemeConstants.niceFont);
        lblFont.setFont(ThemeConstants.niceFont);
        askStyle.setFont(ThemeConstants.niceFont);
        askInReverse.setFont(ThemeConstants.niceFont);
        enterKeyMoveLeft.setFont(ThemeConstants.niceFont);
        fwdQuestionTemplate.setFont(ThemeConstants.niceFont);
        bwdQuestionTemplate.setFont(ThemeConstants.niceFont);
        lblTemplateInfo.setFont(ThemeConstants.niceFont);

        comboFonts = createCombo(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
        
        JSpinner.NumberEditor numberEditor = new JSpinner.NumberEditor(spinnerFontSize);
        spinnerFontSize.setFont(ThemeConstants.niceFont);
        spinnerFontSize.setEditor(numberEditor);
        spinnerFontSize.setMaximumSize(new Dimension(50,MAX_FIELD_SIZE.height));
        spinnerFontSize.setMinimumSize(new Dimension(50,MAX_FIELD_SIZE.height));
        spinnerFontSize.setPreferredSize(new Dimension(50,MAX_FIELD_SIZE.height));
        spinnerFontSize.setToolTipText("The size of the font for display during a quiz.");
        comboFonts.setToolTipText("The font to use for the table data. Allows you to use kanji, etc. as part of the table question.");


        bwdQuestionTemplate.setToolTipText("The question if asking backwards - use [q] where you would like the question word to appear.");
        fwdQuestionTemplate.setToolTipText("The question if asking forwards - use [q] where you would like the question word to appear.");



        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.setOpaque(false);
        askInReverse.setOpaque(false);
        enterKeyMoveLeft.setOpaque(false);
        askStyle.setBackground(Color.white);
        comboFonts.setBackground(Color.white);


        this.add(askInReverse);
        this.add(enterKeyMoveLeft);
        askInReverse.setAlignmentX(LEFT_ALIGNMENT);
        enterKeyMoveLeft.setAlignmentX(LEFT_ALIGNMENT);

        Box b = Box.createHorizontalBox();
        b.setAlignmentX(LEFT_ALIGNMENT);
        b.add(lblFont);
        b.add(Box.createHorizontalGlue());
        b.add(spinnerFontSize);
        b.add(Box.createHorizontalStrut(2));
        b.add(comboFonts);
        comboFonts.setMaximumSize(MAX_FIELD_SIZE);
        comboFonts.setPreferredSize(MAX_FIELD_SIZE);
        comboFonts.setMinimumSize(MIN_FIELD_SIZE);

        this.add(b);

        b = Box.createHorizontalBox();
        b.setAlignmentX(LEFT_ALIGNMENT);
        b.add(lblAskStyle);
        b.add(Box.createHorizontalGlue());
        b.add(askStyle);
        askStyle.setMaximumSize(MAX_FIELD_SIZE);
        askStyle.setPreferredSize(MAX_FIELD_SIZE);
        askStyle.setMinimumSize(MIN_FIELD_SIZE);

        this.add(b);

        b = Box.createHorizontalBox();
        b.setAlignmentX(LEFT_ALIGNMENT);
        b.add(lblNumberMarks);
        b.add(Box.createHorizontalGlue());
        b.add(marksPerAnswer);
        marksPerAnswer.setMaximumSize(MAX_FIELD_SIZE);
        marksPerAnswer.setPreferredSize(MAX_FIELD_SIZE);
        marksPerAnswer.setMinimumSize(MIN_FIELD_SIZE);

        this.add(b);

        b = Box.createHorizontalBox();
        b.setAlignmentX(LEFT_ALIGNMENT);
        b.add(lblForwardTemplate);
        b.add(Box.createHorizontalGlue());
        b.add(fwdQuestionTemplate);
        fwdQuestionTemplate.setMaximumSize(MAX_FIELD_SIZE);
        fwdQuestionTemplate.setPreferredSize(MAX_FIELD_SIZE);
        fwdQuestionTemplate.setMinimumSize(MIN_FIELD_SIZE);

        this.add(b);

        b = Box.createHorizontalBox();
        b.setAlignmentX(LEFT_ALIGNMENT);
        b.add(lblBackwardTemplate);
        b.add(Box.createHorizontalGlue());
        b.add(bwdQuestionTemplate);
        bwdQuestionTemplate.setMaximumSize(MAX_FIELD_SIZE);
        bwdQuestionTemplate.setPreferredSize(MAX_FIELD_SIZE);
        bwdQuestionTemplate.setMinimumSize(MIN_FIELD_SIZE);

        this.add(b);

        lblTemplateInfo.setAlignmentX(LEFT_ALIGNMENT);
        this.add(Box.createVerticalStrut(5));
        this.add(lblTemplateInfo);

        this.validate();
    }

    private JComboBox createCombo(String[] listItems) {
        JComboBox combo = new JComboBox(listItems);
        combo.setFont(new Font(this.getFont().getFamily(), Font.PLAIN, 9));
        combo.addActionListener(new ComboActionListener());
        return combo;
    }

    public JCheckBox getAskInReverse() {
        return askInReverse;
    }

    public JComboBox getAskStyle() {
        return askStyle;
    }

    public SimpleTextField getBwdQuestionTemplate() {
        return bwdQuestionTemplate;
    }

    public JCheckBox getEnterKeyMoveRight() {
        return enterKeyMoveLeft;
    }

    public SimpleTextField getFwdQuestionTemplate() {
        return fwdQuestionTemplate;
    }

    public NumericJTextField getMarksPerAnswer() {
        return marksPerAnswer;
    }

    private class ComboActionListener extends AbstractAction {

        public void actionPerformed(ActionEvent e) {
            if (listener == null)
                return;

            listener.actionPerformed(e);
        }

    }

    /**
     * Sets the action listener is fired when a change in font selection occurs.
     */
    public void setFontsComboActionListener(ActionListener listener) {
        this.listener = listener;
    }

    public Font getSelectedFont() {
        return new Font((String) comboFonts.getSelectedItem(),Font.PLAIN,ThemeConstants.tableCellEditorFont.getSize());
    }

    /**
     * Gets the specified quiz-time display font size.
     * @return the quiz-time display font size.
     */
    public int getSelectedFontSize() {
        return ((SpinnerNumberModel) spinnerFontSize.getModel()).getNumber().intValue();
    }
}

