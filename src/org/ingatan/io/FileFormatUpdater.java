/*
 * FileFormatUpdater.java
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
package org.ingatan.io;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import org.ingatan.component.text.RichTextArea;
import org.ingatan.data.FlexiQuestion;
import org.ingatan.data.HistoryEntry;
import org.ingatan.data.IQuestion;
import org.ingatan.data.Library;
import org.jdom.DataConversionException;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

/**
 * This class will hold the methods to update old Ingatan file types as they are encountered.
 * It is currently empty, as there are no file types that need updating.
 *
 * @author Thomas Everingham
 * @version 1.0
 */
public class FileFormatUpdater {

    public static final float CURRENT_VERSION_LIBRARY_FILE = 2.0f;

    /**
     * Loads the specified file as an XML document and ensures it is up to date.
     * @param libraryFile the file to read in and update.
     * @return The root element of the updated XML file, if changes need to be made, or
     * else the original root element is returned. Returns null if the XML document couldn't be loaded.
     * @throws DataConversionException If there is a problem converting XML data.
     */
    public static Element updateLibraryFileType(File libraryFile) throws DataConversionException {
        //build the XML document
        SAXBuilder sax = new SAXBuilder();
        Document doc = null;
        try {
            doc = sax.build(libraryFile);
        } catch (JDOMException ex) {
            Logger.getLogger(ParserWriter.class.getName()).log(Level.SEVERE, "While trying to build xml document from library file", ex);
        } catch (IOException ex) {
            Logger.getLogger(ParserWriter.class.getName()).log(Level.SEVERE, "While trying to build xml document from library file", ex);
        }

        //if we couldn't build the document, return null
        if (doc == null) {
            return null;
        }

        Element e = doc.getRootElement();

        //some pre-file-version-attribute libraries are still floating around on my system,
        //these should all be compatible with version 1.0.
        if (e.getAttribute("fileVersion") == null)
            e.setAttribute("fileVersion","1.0");
        
        ///update the library if required
        if (e.getAttribute("fileVersion").getFloatValue() == CURRENT_VERSION_LIBRARY_FILE) {
            return e; //the element is already up to date.
        } else if (e.getAttribute("fileVersion").getFloatValue() == 1.0f) {
            String libraryName = e.getAttributeValue("name");
            String libraryID = e.getAttributeValue("id");
            String libraryDescription = e.getChildText("libDesc");
            Date creationDate = new Date(e.getAttributeValue("created"));
            ListIterator questionList = e.getChildren("question").listIterator();

            //create list of questions
            IQuestion[] questions = new IQuestion[0];
            IQuestion[] temp;
            while (questionList.hasNext()) {
                temp = new IQuestion[questions.length + 1];
                System.arraycopy(questions, 0, temp, 0, questions.length);
                temp[questions.length] = ParserWriter.questionFromElement((Element) questionList.next(), libraryID);
                questions = temp;
            }

            //create id to file hashtable
            Hashtable images = new Hashtable<String, File>();
            FlexiQuestion ques;
            for (int i = 0; i < questions.length; i++) {
                //only care about flexi-questions, as table questions do not contain
                //images
                if (questions[i] instanceof FlexiQuestion) {
                    ques = (FlexiQuestion) questions[i];
                    Pattern p = Pattern.compile("\\[" + RichTextArea.TAG_IMAGE + "\\](.*?)\\[!" + RichTextArea.TAG_IMAGE + "\\]");
                    Matcher m = p.matcher(ques.getQuestionText());
                    while (m.find()) {
                        images.put(m.group(1).split("<;>")[0], new File(libraryFile.getParent() + "/" + m.group(1).split("<;>")[0]));
                    }
                    m = p.matcher(ques.getAnswerText());
                    while (m.find()) {
                        images.put(m.group(1).split("<;>")[0], new File(libraryFile.getParent() + "/" + m.group(1).split("<;>")[0]));
                    }
                    m = p.matcher(ques.getPostAnswerText());
                    while (m.find()) {
                        images.put(m.group(1).split("<;>")[0], new File(libraryFile.getParent() + "/" + m.group(1).split("<;>")[0]));
                    }

                }
            }


            //must create a list of history elements to create the new library file type
            List<HistoryEntry> historyEntries = new ArrayList<HistoryEntry>();
            Library buildLib = new Library(libraryName, libraryID, libraryDescription, creationDate, questions, new File(libraryFile.getParent()), libraryFile, historyEntries, images);
            return ParserWriter.writeLibraryFile(buildLib, false);
        } else {
            return null; //this should never occur
        }

    }
}
