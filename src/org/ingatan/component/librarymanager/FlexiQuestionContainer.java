/*
 * FlexiQuestionContainer.java
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
import org.ingatan.component.answerfield.IAnswerField;
import org.ingatan.component.image.ImageAcquisitionDialog;
import org.ingatan.component.text.EmbeddedGraphic;
import org.ingatan.component.text.EmbeddedImage;
import org.ingatan.component.text.EmbeddedMathTeX;
import org.ingatan.component.text.RichTextArea;
import org.ingatan.component.text.RichTextToolbar;
import org.ingatan.data.FlexiQuestion;
import org.ingatan.event.RichTextToolbarEvent;
import org.ingatan.event.RichTextToolbarListener;
import org.ingatan.io.IOManager;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.KeyboardFocusManager;
import java.awt.Rectangle;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.InputMap;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.Element;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledEditorKit;
import org.ingatan.image.ImageUtils;

/**
 * This question container type is used for freeform questions. It consists of three rich text
 * fields; question text, answer text, and post-answer text. The post-answer text field is optional,
 * and is hidden when the option is not selected.
 *
 * @author Thomas Everingham
 * @version 1.0
 */
public class FlexiQuestionContainer extends AbstractQuestionContainer {

    private static Dimension TEXT_AREA_MAX_SIZE = new Dimension(1000, 600);
    private static Dimension TEXT_AREA_MIN_SIZE = new Dimension(200, 80);
    private static Dimension TEXT_AREA_PREF_SIZE = new Dimension(400, 300);
    /**
     * Static text focus listener is added to every <code>RichTextArea</code> contained
     * within a flexi question container. This allows the rich text toolbar to
     * 'follow the focus' of the rich text areas. Because this object is static, only
     * one rich text toolbar is shown between all instances of FlexiQuestionContainer. If this
     * does not suit, this listener can be manually removed from the <code>RichTextArea</code>s of
     * a particular <code>FlexiQuestionContainer</code>.
     */
    private static TextFocusListener textFocusListener = new TextFocusListener();
    /**
     * The question text area.
     */
    protected RichTextArea questionText = new RichTextArea();
    /**
     * The post answer text area.
     */
    protected RichTextArea postAnswerText = new RichTextArea();
    /**
     * The answer text area.
     */
    protected RichTextArea answerText = new RichTextArea();
    /**
     * The post-answer text is optional, and if not used, the field will be hidden.
     * This checkbox allows for the option to be set.
     */
    protected JCheckBox usePostAnswerText = new JCheckBox(new UsePostAnswerTextAction());
    /**
     * Label for question text field.
     */
    protected JLabel lblQuestion = new JLabel("Question text: ");
    /**
     * Label for answer text field.
     */
    protected JLabel lblAnswer = new JLabel("Answer text:");
    /**
     * Label for post-answer text field.
     */
    protected JLabel lblPostAnswer = new JLabel("Post-answer text:");
    /**
     * The question object encapsulating the data represented by this component.
     */
    protected FlexiQuestion flexiQuestion;

    /**
     * Create a new <code>FlexiQuestionContainer</code>.
     */
    public FlexiQuestionContainer(FlexiQuestion ques) {
        super(ques);
        this.flexiQuestion = ques;

        setUpInputMap(questionText);
        setUpInputMap(answerText);
        setUpInputMap(postAnswerText);

        //set the maximum and minimum sizes for rich text area scrollers. These
        //sizes are used when resizing the areas to match their content so that
        //the fields are not made too small or too large.
        questionText.getScroller().setMaximumSize(TEXT_AREA_MAX_SIZE);
        questionText.getScroller().setMinimumSize(TEXT_AREA_MIN_SIZE);
        questionText.getScroller().setPreferredSize(TEXT_AREA_PREF_SIZE);
        answerText.getScroller().setMaximumSize(TEXT_AREA_MAX_SIZE);
        answerText.getScroller().setMinimumSize(TEXT_AREA_MIN_SIZE);
        answerText.getScroller().setPreferredSize(TEXT_AREA_PREF_SIZE);
        postAnswerText.getScroller().setMaximumSize(TEXT_AREA_MAX_SIZE);
        postAnswerText.getScroller().setMinimumSize(TEXT_AREA_MIN_SIZE);
        postAnswerText.getScroller().setPreferredSize(TEXT_AREA_PREF_SIZE);

        //this following are set so that when we use the modelToView method, the area has positive size
        //otherwise the method returns false.
        questionText.setSize(TEXT_AREA_PREF_SIZE);
        answerText.setSize(TEXT_AREA_PREF_SIZE);
        postAnswerText.setSize(TEXT_AREA_PREF_SIZE);


        lblQuestion.setFont(ThemeConstants.niceFont);
        lblAnswer.setFont(ThemeConstants.niceFont);
        lblPostAnswer.setFont(ThemeConstants.niceFont);
        usePostAnswerText.setFont(ThemeConstants.niceFont);

        RichTextToolbarListener textToolbarListener = new TextToolbarListener();
        answerText.setToolbarVisible(false);
        answerText.getToolbar().addRichTextToolbarListener(textToolbarListener);
        postAnswerText.setToolbarVisible(false);
        postAnswerText.getToolbar().addRichTextToolbarListener(textToolbarListener);
        postAnswerText.getScroller().setVisible(false);
        lblPostAnswer.setVisible(false);
        questionText.setToolbarVisible(false);
        questionText.getToolbar().addRichTextToolbarListener(textToolbarListener);
        usePostAnswerText.setOpaque(false);

        //set layout and add components
        this.setLayoutOfContentPane(new BoxLayout(contentPanel, BoxLayout.PAGE_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(7, 7, 7, 7));

        this.addToContentPane(lblQuestion, true);
        this.addToContentPane(questionText.getScroller(), false);
        this.addToContentPane(lblAnswer, true);
        this.addToContentPane(answerText.getScroller(), false);
        this.addToContentPane(lblPostAnswer, true);
        this.addToContentPane(postAnswerText.getScroller(), false);
        this.addToContentPane(usePostAnswerText, true);

        this.validate();
        questionText.getScroller().validate();
        answerText.getScroller().validate();
        postAnswerText.getScroller().validate();


        //add static focus listener so that the toolbar follows the focus of the
        //text fields
        questionText.addFocusListener(textFocusListener);
        answerText.addFocusListener(textFocusListener);
        postAnswerText.addFocusListener(textFocusListener);

        //set data
        questionText.setRichText(flexiQuestion.getQuestionText());
        answerText.setRichText(flexiQuestion.getAnswerText());
        postAnswerText.setRichText(flexiQuestion.getPostAnswerText());

        usePostAnswerText.setSelected(flexiQuestion.isUsingPostAnswerText());
        postAnswerText.getScroller().setVisible(flexiQuestion.isUsingPostAnswerText());
        lblPostAnswer.setVisible(flexiQuestion.isUsingPostAnswerText());

        //must traverse answerText field and find any pre-existing answer fields
        //so that they can be made aware of edit context
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                contextualiseAnswerFields(answerText);
                //answer fields are not confined to the answerText area, and so
                //the other text areas must also be contextualised (this was not
                //always the case).
                contextualiseAnswerFields(questionText);
                contextualiseAnswerFields(postAnswerText);
            }
        });

        //set the listeners that will resize the text areas upon document change
        questionText.getStyledDocument().addDocumentListener(new TextAreaListener(questionText));
        answerText.getStyledDocument().addDocumentListener(new TextAreaListener(answerText));
        postAnswerText.getStyledDocument().addDocumentListener(new TextAreaListener(postAnswerText));

        //do an initial resize based on the newly set text
        resetSize(questionText);
        resetSize(answerText);
        resetSize(postAnswerText);
    }

    /**
     * Set up the input and action maps for the specified rich text area.
     * @param map the RichTextArea for which the input and action maps should be set up.
     */
    private void setUpInputMap(RichTextArea map) {
        InputMap iMap = map.getInputMap(WHEN_FOCUSED);
        ActionMap aMap = map.getActionMap();

        iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_DOWN_MASK), "PasteAction");
        aMap.put("PasteAction", new PasteAction());

        map.setInputMap(WHEN_FOCUSED, iMap);
        map.setActionMap(aMap);
    }

    /**
     * Traverses the elements of the answerText <code>RichTextArea</code> and tells
     * all IAnswerField components found that they exist in the library editor context.
     */
    public void contextualiseAnswerFields(RichTextArea textArea) {
        int runCount;
        int paragraphCount = textArea.getDocument().getDefaultRootElement().getElementCount();
        Element curEl = null;
        AttributeSet curAttr = null;
        AttributeSet prevAttr = null;

        for (int i = 0; i < paragraphCount; i++) {
            //each paragraph has 'runCount' runs
            runCount = textArea.getDocument().getDefaultRootElement().getElement(i).getElementCount();
            for (int j = 0; j < runCount; j++) {
                curEl = textArea.getDocument().getDefaultRootElement().getElement(i).getElement(j);
                curAttr = curEl.getAttributes();

                if (curEl.getName().equals(StyleConstants.ComponentElementName)) //this is a component
                {
                    //this run is a component. May be an answer field, picture or math text component.
                    Component o = (Component) curAttr.getAttribute(StyleConstants.ComponentAttribute);
                    if (o instanceof IAnswerField) {
                        ((IAnswerField) o).setContext(true);
                    }
                }
            }
        }
    }

    private class PasteAction extends AbstractAction {

        public void actionPerformed(ActionEvent e) {
            paste();
            new StyledEditorKit.PasteAction().actionPerformed(e);
        }
    }

    /**
     * Pastes the content of the clipboard into the flexi question container.
     */
    private void paste() {
        Component focusOwner = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
        if ((focusOwner instanceof RichTextArea == false) || (FlexiQuestionContainer.this.isAncestorOf(focusOwner) == false)) {
            //in either of these cases, the focus owner should not be pasted into.
            return;
        }

        BufferedImage imgFromClipboard = null;
        String imageID = "";

        try {
            imgFromClipboard = ImageUtils.getImageFromClipboard(false, true);
        } catch (UnsupportedFlavorException ex) {
            return;
        } catch (IOException ex) {
            return;
        }

        //double check that an image was retreived from the clipboard
        if (imgFromClipboard == null) {
            return;
        }
        try {
            imageID = IOManager.saveImage(FlexiQuestionContainer.this.getQuestion().getParentLibrary(), imgFromClipboard, "paste(" + imgFromClipboard.getWidth() + "x" + imgFromClipboard.getHeight() + ")");
        } catch (IOException ex) {
            Logger.getLogger(FlexiQuestionContainer.class.getName()).log(Level.SEVERE, "While trying to save an image pasted from the clipboard to the library: " + FlexiQuestionContainer.this.getQuestion().getParentLibrary(), ex);
        }

        if (imageID.equals("")) {
            //IOManager did not successfully save the image.
            return;
        }

        //otherwise, create the embedded image
        EmbeddedImage eg = new EmbeddedImage(imgFromClipboard, imageID, FlexiQuestionContainer.this.getQuestion().getParentLibrary());

        if (eg.isImageTooLarge()) {
            int resp = JOptionPane.showConfirmDialog(FlexiQuestionContainer.this, "This image is larger than the recommended maximum size. Would you\n"
                    + "like Ingatan to shrink the image to the largest recommended size?", "Large Image", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            if (resp == JOptionPane.YES_OPTION) {
                EmbeddedImage ei = (EmbeddedImage) eg;
                ei.resizeTo(ei.getMaxRecommendedSize());
                try {
                    IOManager.saveImageWithOverWrite(ei.getImage(), ei.getParentLibraryID(), ei.getImageID());
                } catch (IOException ex) {
                    Logger.getLogger(FlexiQuestionContainer.class.getName()).log(Level.SEVERE, "ocurred while trying to save a embeddedImage with rewrite using IOManager\n"
                            + "in order to save a resized version of the PASTED image upon user request (user was just told the image is larger than recommended, and asked if they would\n"
                            + "like it resized).", ex);
                }
            }
        }

        ((RichTextArea) focusOwner).insertComponent(eg);
    }

    @Override
    public void maximise() {
        super.maximise();
        int prefHeight = questionText.getSize().height + answerText.getSize().height + 50;
        if (usePostAnswerText.isSelected()) {
            prefHeight += postAnswerText.getSize().height + 30;
        }
        this.setPreferredSize(new Dimension(this.getPreferredSize().width, prefHeight));
        this.setMaximumSize(new Dimension((int) 1000, 500));
    }

    @Override
    protected void paintContentPanel(Graphics2D g2d) {
        if (minimised) {
            contentPanel.setBorder(BorderFactory.createEmptyBorder(50, 1, 1, 1));
            String printMe = questionText.getText();
            if (printMe.length() > 50) {
                printMe = printMe.substring(0, 48) + "...";
            }

            if (printMe.isEmpty()) {
                printMe = "empty question";
            }

            g2d.drawString(printMe, 10, 20);
        } else {
            contentPanel.setBorder(BorderFactory.createEmptyBorder(7, 7, 7, 7));
        }
    }

    /**
     * Static text focus listener allows for the rich text toolbar to follow which
     * <code>RichTextArea</code> has focus. The same instance of this listener is added
     * to every <code>RichTextArea</code> contained by a <code>FlexiQuestionContainer</code>.
     */
    public static class TextFocusListener implements FocusListener {

        public void focusGained(FocusEvent e) {
            ((RichTextArea) e.getComponent()).setToolbarVisible(true);
        }

        public void focusLost(FocusEvent e) {
            if (!(e.getOppositeComponent() instanceof RichTextToolbar)) {
                ((RichTextArea) e.getComponent()).setToolbarVisible(false);
            }
        }
    }

    /**
     * Listens for changes to the document, and then calls the <code>resetSize</code>
     * method. This method resets the preferred size of the <code>RichTextArea</code> whose
     * document has changed to suit the size of the content, within the bounds of
     * TEXT_AREA_MAX_SIZE and TEXT_AREA_MIN_SIZE fields of <code>FlexiQuestionContainer</code>.
     */
    private class TextAreaListener implements DocumentListener {

        RichTextArea listenee;
        Runnable resetSize;

        public TextAreaListener(RichTextArea listenee) {
            this.listenee = listenee;

            resetSize = new Runnable() {

                public void run() {
                    resetSize(TextAreaListener.this.listenee);
                }
            };
        }

        public void insertUpdate(DocumentEvent e) {
            SwingUtilities.invokeLater(resetSize);
        }

        public void removeUpdate(DocumentEvent e) {
            SwingUtilities.invokeLater(resetSize);
        }

        public void changedUpdate(DocumentEvent e) {
            SwingUtilities.invokeLater(resetSize);
        }
    }

    public void resetSize(RichTextArea txtArea) {
        try {
            javax.swing.text.Document doc = txtArea.getDocument();
            Dimension d = txtArea.getPreferredSize();
            Rectangle r = txtArea.modelToView(doc.getLength());

            d.height = r.y + r.height + txtArea.getToolbar().getHeight();

            if (d.getHeight() < txtArea.getScroller().getMaximumSize().getHeight()) {
                if (d.getHeight() < txtArea.getScroller().getMinimumSize().getHeight()) {
                    d.height = (int) txtArea.getScroller().getMinimumSize().getHeight();
                }
                txtArea.getScroller().setPreferredSize(d);
            } else {
                txtArea.getScroller().setPreferredSize(txtArea.getScroller().getMaximumSize());
            }

            txtArea.getScroller().validate();
            FlexiQuestionContainer.this.validate();

        } catch (Exception e2) {
            Logger.getLogger(FlexiQuestionContainer.class.getName()).log(Level.WARNING, "Could not resize the text area to fit content.", e2);
        }
    }

    /**
     * Updates the interface and question object when the use post answer option
     * is changed via the supplied checkbox.
     */
    private class UsePostAnswerTextAction extends AbstractAction {

        public UsePostAnswerTextAction() {
            super("use post-answer text");
        }

        public void actionPerformed(ActionEvent e) {
            if (usePostAnswerText.isSelected()) {
                flexiQuestion.setUsePostAnswerText(true);
                postAnswerText.getScroller().setVisible(true);
                lblPostAnswer.setVisible(true);
            } else {
                flexiQuestion.setUsePostAnswerText(false);
                postAnswerText.getScroller().setVisible(false);
                lblPostAnswer.setVisible(false);
            }
        }
    }

    /**
     * This toolbar listener just listens for the RichTextToolbarEvent.INSERT_PICTURE event ID.
     * The class takes care of inserting a picture from the many sources available, into a RichTextArea.
     */
    private class TextToolbarListener implements RichTextToolbarListener {

        public void buttonPressed(RichTextToolbarEvent e) {
            if (e.getEventID() == RichTextToolbarEvent.INSERT_PICTURE) {
                //open an image acquisition dialog
                ImageAcquisitionDialog imgDialog = new ImageAcquisitionDialog((JFrame) FlexiQuestionContainer.this.getRootPane().getParent());
                imgDialog.setVisible(true);

                EmbeddedGraphic eg = null;
                String id = "";
                //if the image is from a library, this is where it is from
                String fromLib = "";
                //if we're dealing with an image.
                if ((imgDialog.getAcquisitionSource() != ImageAcquisitionDialog.NONE) && (imgDialog.getAcquisitionSource() != ImageAcquisitionDialog.FROM_MATH_TEXT)) {
                    //FROM LIBRARY------------------------------------------------------------------------------
                    if (imgDialog.getAcquisitionSource() == ImageAcquisitionDialog.FROM_LIBRARY) {
                        fromLib = imgDialog.getAcquiredImageData().split("\n")[1];
                        id = imgDialog.getAcquiredImageData().split("\n")[0];
                        //if this is from the library to which this question belongs, then we do not need to resave it, simply insert it.
                        if (fromLib.equals(FlexiQuestionContainer.this.getQuestion().getParentLibrary())) {
                            try {
                                eg = new EmbeddedImage(IOManager.loadImage(fromLib, id), id, FlexiQuestionContainer.this.getQuestion().getParentLibrary());
                            } catch (IOException ex) {
                                Logger.getLogger(FlexiQuestionContainer.class.getName()).log(Level.SEVERE, "IO Exception occurred while attempting to load an image through the IOManager\n"
                                        + "to create an EmbeddedImage for a RichTextArea contained by a FlexiQuestionContainer. This occurred during user initiated ImageAcquisition from\n"
                                        + "a resource already added to the library. Image ID=" + id + " library=" + fromLib, ex);
                            }
                        } else {
                            BufferedImage img = null;
                            try {
                                //otherwise the image must be copied into the current library from the source library.
                                img = IOManager.loadImage(fromLib, id);
                            } catch (IOException ex) {
                                Logger.getLogger(FlexiQuestionContainer.class.getName()).log(Level.SEVERE, "IO Exception occurred while attempting to load an image through the IOManager\n"
                                        + " to create an EmbeddedImage for a RichTextArea contained by a FlexiQuestionContainer. This occurred during user initiated ImageAcquisition from\n"
                                        + "a resource from a different library to the one containing the destination FlexiQuestionContainer. Image ID=" + id + " , from library=" + fromLib
                                        + " , destination library=" + FlexiQuestionContainer.this.getQuestion().getParentLibrary(), ex);
                            }
                            try {
                                id = IOManager.saveImage(FlexiQuestionContainer.this.getQuestion().getParentLibrary(), img, id);
                            } catch (IOException ex) {
                                Logger.getLogger(FlexiQuestionContainer.class.getName()).log(Level.SEVERE, "IO Exception occurred while attempting to save an image to a library through the IOManager\n"
                                        + ". This occurred during user initiated ImageAcquisition from a resource from a different library to the one containing the destination FlexiQuestionContainer.\n"
                                        + "Image ID=" + id + " , from library=" + fromLib + " , destination library=" + FlexiQuestionContainer.this.getQuestion().getParentLibrary(), ex);
                            }
                            eg = new EmbeddedImage(img, id, FlexiQuestionContainer.this.getQuestion().getParentLibrary());
                        }
                        //FROM COLLECTION/NEW/FILE------------------------------------------------------------------------------
                    } else if ((imgDialog.getAcquisitionSource() == ImageAcquisitionDialog.FROM_COLLECTION) || (imgDialog.getAcquisitionSource() == ImageAcquisitionDialog.FROM_FILE)
                            || (imgDialog.getAcquisitionSource() == ImageAcquisitionDialog.FROM_NEW) || (imgDialog.getAcquisitionSource() == ImageAcquisitionDialog.FROM_CHEM_STRUCTURE)) {
                        try {
                            id = IOManager.saveImage(flexiQuestion.getParentLibrary(), imgDialog.getAcquiredImage(), imgDialog.getAcquiredImageData());
                        } catch (IOException ex) {
                            Logger.getLogger(FlexiQuestionContainer.class.getName()).log(Level.SEVERE, "IO Exception occurred while attempting to save an image to a library through the IOManager"
                                    + ". This occurred during user initiated ImageAcquisition from a collection, from file or from a newly created image."
                                    + " Destination library=" + FlexiQuestionContainer.this.getQuestion().getParentLibrary(), ex);
                        }
                        eg = new EmbeddedImage(imgDialog.getAcquiredImage(), id, FlexiQuestionContainer.this.getQuestion().getParentLibrary());
                    }
                    //MATH-TEXT or USER CANCELLED------------------------------------------------------------------------------
                } else { //we're dealing with 'user cancelled' or math text
                    if (imgDialog.getAcquisitionSource() == ImageAcquisitionDialog.NONE) {
                        return;
                    } else {
                        //split into math-text-data and render-size
                        String[] mathText = imgDialog.getAcquiredImageData().split("\n");
                        String[] colourVals = mathText[2].split(",");
                        eg = new EmbeddedMathTeX(mathText[0], Integer.valueOf(mathText[1]), new Color(Integer.valueOf(colourVals[0]), Integer.valueOf(colourVals[1]), Integer.valueOf(colourVals[2])));
                    }
                }

                if (eg instanceof EmbeddedImage) {
                    if (((EmbeddedImage) eg).isImageTooLarge()) {
                        int resp = JOptionPane.showConfirmDialog(FlexiQuestionContainer.this, "This image is larger than the recommended maximum size. Would you\n"
                                + "like Ingatan to shrink the image to the largest recommended size?", "Large Image", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                        if (resp == JOptionPane.YES_OPTION) {
                            EmbeddedImage ei = (EmbeddedImage) eg;
                            ei.resizeTo(ei.getMaxRecommendedSize());
                            try {
                                IOManager.saveImageWithOverWrite(ei.getImage(), ei.getParentLibraryID(), ei.getImageID());
                            } catch (IOException ex) {
                                Logger.getLogger(FlexiQuestionContainer.class.getName()).log(Level.SEVERE, "ocurred while trying to save an embeddedImage with rewrite using IOManager\n"
                                        + "in order to save a resized version of the image upon user request (user was just told the image is larger than recommended, and asked if they would\n"
                                        + "like it resized).", ex);
                            }
                        }
                    }
                }

                //lastly, add the image to the appropriate text box
                if (e.getSource().equals(questionText.getToolbar())) {
                    questionText.insertComponent(eg);
                    resetSize(questionText);
                } else if (e.getSource().equals(answerText.getToolbar())) {
                    answerText.insertComponent(eg);
                    resetSize(answerText);
                } else if (e.getSource().equals(postAnswerText.getToolbar())) {
                    postAnswerText.insertComponent(eg);
                    resetSize(postAnswerText);
                }
            }
        }

        public void fontChanged(RichTextToolbarEvent e) {
        }
    }

    /**
     * Get the question text in rich text format.
     * @return the question text in rich text format.
     */
    public String getQuestionText() {
        return questionText.getRichText();
    }

    /**
     * Get the answer text in rich text format.
     * @return the answer text in rich text format.
     */
    public String getAnswerText() {
        return answerText.getRichText();
    }

    /**
     * Get the post answer text in rich text format.
     * @return the post answer text in rich text format.
     */
    public String getPostAnswerText() {
        return postAnswerText.getRichText();
    }

    /**
     * Gets whether or not the post answer text is being used.
     * @return <code>true</code> if the post answer text is being used.
     */
    public boolean getUsePostAnswerText() {
        return usePostAnswerText.isSelected();
    }
}
