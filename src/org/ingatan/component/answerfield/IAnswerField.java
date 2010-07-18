/*
 * IAnswerField.java
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

import java.awt.event.ActionListener;

/**
 * Interface for the answer fields. Answer fields are used in Ingatan as an alternative
 * to set question types. Rather than inserting a 'multiple choice question' or
 * 'short answer question' into a library at design time, the user simply inserts a
 * question container, where any combination of answer fields can be added to the
 * answer text area. This allows the user to generate 'fill in the missing word' questions,
 * and things like that, but also all the basic question types. It also allows the user to
 * have questions with multiple parts.
 *
 * Note; there are classes you may use to load images to your answer field so that they
 * will be saved in the library as resources.
 *
 * @author Thomas Everingham
 * @version 1.0
 */
public interface IAnswerField {

    /**
     * The user is presented with a palette of answer fields that may be added.
     * This string is used as the display text for this answer field on the palette.
     * @return the display text for this answer field.
     */
    public String getDisplayName();

    /**
     * This method looks at the answer provided by the user and compares it to the correct
     * answer. The returned value should be a decimal percentage (i.e. 0 for 0%, 0.5 for 50%, 1.0 for 100%).
     * @return the correctness of the given answer as a decimal percentage.
     */
    public float checkAnswer();

    /**
     * Gets the maximum number of marks that can be awarded for this question. This may be preset, or there
     * may be an interface as part of this answer field allowing the user to alter this.
     * @return the maximum number of marks that can be awarded for this question.
     */
    public int getMaxMarks();

    /**
     * Marks that have been awarded for the given answer. Depending on the implementation, this may be
     * given by getMaxMarks() * checkAnswer().
     * @return the number of marks awarded for the given answer. Returns 0 if no answer has yet been given.
     */
    public int getMarksAwarded();

    /**
     * Display the correct answer(s) on the answer field. This should also make the answer field
     * uneditable. This is used at quiz time during the post-answer stage so that the user can see what
     * the answers should have been.
     */
    public void displayCorrectAnswer();

    /**
     * Sets whether the answer field is in the library manager or in quiz time.
     * If in the library manager, the answer field should be editable, and allow the user
     * to set the correct answer and possibly other options, depending on the answer field
     * designer's requirements. When in the quiz time context, the answer field should allow the user to enter a
     * possible answer, but not edit the answer field.
     *
     * @param inLibraryContext <code>true</code> if the answer field currently exists within the
     * library manager context.
     */
    public void setContext(boolean inLibraryContext);

    /**
     * Every AnswerField must be responsible for serialising itself. This string
     * should contain all the information required to reconstruct the answer field
     * by passing the string to the <code>readInXML</code> method.<br>
     * <br>
     * The output of this method must not contain an opening or closing component tag
     * as used by the Parser class.<br>
     * <br>
     * <b>Note:</b> if this answer field saves an image based on the IOManager's
     * methods, you will need to serialise using the imageID passed back. Every time
     * the library is packaged, a clean-out of images is performed. The library file is
     * searched and any image whose ID does not appear anywhere in the library file will
     * be deleted. As a result, all image IDs must appear in the serialised String representation of the
     * answer field. This will most likely be the case, but I have included this comment just in case.<br>
     *
     * @return the String representing the serialised answer field.
     */
    public String writeToXML();

    /**
     * Every AnswerField must be able to reconstruct itself from the output of the
     * <code>writeToXML</code> method. This method does this.
     *
     * @param xml the String to parse.
     */
    public void readInXML(String xml);

    /**
     * The library within which this answer field exists. This is important for the access
     * of images. This is originally set when the answer field is added by the AnswerFieldPalette
     * in the LibraryManagerWindow. It is then the AnswerField's responsibility to ensure that
     * the value is serialised and de-serialised (thorugh write/readIn XML methods). If this functionality is
     * not required (usually if you do not intend to save/load images) then simply set this method to return "".
     * @return the ID of the library within which this answer field exists.
     */
    public String getParentLibraryID();

    /**
     * Sets the library within which this answer field exists. This is important for the
     * access of images. This is originally set when the answer field is added by the AnswerFieldPalette
     * in the LibraryManagerWindow. It is then the AnswerField's responsibility to ensure that
     * the value is serialised and de-serialised (thorugh write/readIn XML methods). If this functionality is
     * not required (usually if you do not intend to save/load images) then simply set this method to return.
     * @param id the ID of the library within which this answer field exists.
     */
    public void setParentLibraryID(String id);
    /**
     * Sets the <code>ActionListener</code> for this answer field. This method will be called by the quiz window
     * when the answer field is created. When the user interacts with the answer field in a way that should submit their answer,
     * then the action listener should have its <code>actionPerformed</code> method fired with an <code>ActionEvent</code> that contains
     * the source of the event (i.e. the answer field object). This will tell the <code>QuizWindow</code> that the user has submitted the answer.
     * An example is the user pressing enter when in a text field, or double clicking a multiple choice option.<br>
     * <br>
     * <b>Note:<b> if the answer field is the only one for the current question, firing the actionPerformed method of this listener will
     * be the equivalent of the user pressing the 'Continue' button at quiz time. If there are other answer fields present, then all that
     * occurs is that the focus is shifted to the next answer field in the area.
     * @param listener the <code>ActionListener</code> set by the <code>QuizWindow</code> for this answer field instance.
     */
    public void setQuizContinueListener(ActionListener listener);
}
