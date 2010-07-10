/*
 * QuizHistoryWindow.java
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

package org.ingatan.component;

import org.ingatan.ThemeConstants;
import org.ingatan.data.QuizHistoryEntry;
import org.ingatan.io.IOManager;
import org.ingatan.io.ParserWriter;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Iterator;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * Simple window displaying past quiz results. Results are displayed in the following
 * format:<br><br>
 * Date: X%, Y asked, Z skipped. Score=W. Libraries used=.
 * 
 * @author ThomasEveringham
 * @version 1.0
 */
public class QuizHistoryWindow extends JFrame implements WindowListener {

    /**
     * Content of the scroll pane, contains each record.
     */
    private JPanel scrollerContent = new JPanel();
    /**
     * Scroll pane for the records.
     */
    private JScrollPane scroller = new JScrollPane();
    /**
     * The window to return to when this window is closed.
     */
    private Window returnToOnClose;
    /**
     * The label that displays the total score accumulated over all quizes.
     */
    private JLabel lblTotalScore = new JLabel();

    /**
     * Creates a new <code>QuizHistoryWindow</code>.
     * @param returnToOnClose the window to return to once this window has closed.
     */
    public QuizHistoryWindow(Window returnToOnClose) {
        this.returnToOnClose = returnToOnClose;

        this.setTitle("Quiz Records");
        this.setIconImage(IOManager.windowIcon);
        
        this.setSize(new Dimension(420, 500));
        scroller.setViewportView(scrollerContent);
        scroller.setPreferredSize(new Dimension(300, 300));
        this.setLocationRelativeTo(null);
        this.addWindowListener(this);

        scrollerContent.setLayout(new BoxLayout(scrollerContent, BoxLayout.Y_AXIS));

        lblTotalScore.setText("<html><h2>Total Score: " + IOManager.getQuizHistoryFile().getTotalScore() + "</h2>");
        lblTotalScore.setAlignmentX(LEFT_ALIGNMENT);
        scroller.setAlignmentX(LEFT_ALIGNMENT);
        this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
        this.getContentPane().add(lblTotalScore);
        this.getContentPane().add(Box.createVerticalStrut(10));
        this.getContentPane().add(scroller);

        rebuild();

    }

    /**
     * Rebuild the record list based on the IOManager's file.
     */
    public void rebuild() {
        scrollerContent.removeAll();

        if (IOManager.getQuizHistoryFile().getEntries().size() == 0) {
            scrollerContent.add(new JLabel("<html><h3>No Quiz History Entries Exist</h3>"));
            this.validate();
        } else {

            Iterator<QuizHistoryEntry> iterate = IOManager.getQuizHistoryFile().getEntries().iterator();
            while (iterate.hasNext()) {
                scrollerContent.add(new QuizRecord(iterate.next()));
                scrollerContent.add(Box.createVerticalStrut(10));
            }
        }
        QuizHistoryWindow.this.validate();
        scrollerContent.repaint();
    }

    public void windowOpened(WindowEvent e) {
    }

    public void windowClosing(WindowEvent e) {
        returnToOnClose.setVisible(true);
        //any changes (i.e. deleted records) can be saved as follows
        ParserWriter.writeQuizHistoryFile(IOManager.getQuizHistoryFile());
        this.dispose();
    }

    public void windowClosed(WindowEvent e) {
    }

    public void windowIconified(WindowEvent e) {
    }

    public void windowDeiconified(WindowEvent e) {
    }

    public void windowActivated(WindowEvent e) {
    }

    public void windowDeactivated(WindowEvent e) {
    }

    /**
     * Single quiz result entry.
     */
    private class QuizRecord extends PaintedJPanel {

        /**
         * JLabel showing the data for this record.
         */
        private JLabel lblRecord = new JLabel();
        /**
         * Button that allows the user to remove this particular record.
         */
        private JButton btnDelete = new JButton(new QuizRecordDeleteAction());
        /**
         * The entry that this quiz record represents.
         */
        private QuizHistoryEntry record;

        /**
         * Creates a new QuizRecord.
         * @param record the QuizHistoryEntry to create this record from.
         */
        public QuizRecord(QuizHistoryEntry record) {
            this.record = record;
            this.setBorder(BorderFactory.createEmptyBorder(0, 8, 8, 8));

            btnDelete.setMargin(new Insets(1, 1, 1, 1));
            btnDelete.setFont(ThemeConstants.niceFont);
            btnDelete.setMaximumSize(new Dimension(20, 15));

            lblRecord.setFont(ThemeConstants.niceFont);

            this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
            this.add(lblRecord);
            this.add(btnDelete);

            String buildString = "<html><h4>" + record.getLibraries() + " - " + record.getPercentage() + "%</h4>";
            buildString += "Taken on " + record.getDate() + ": " + record.getQuestionsAnswered() + " answered, " + record.getQuestionsSkipped() + " skipped. "
                    + "Score awarded: " + record.getScore();
            lblRecord.setText(buildString);
        }

        /**
         * Action for the delete button.
         */
        private class QuizRecordDeleteAction extends AbstractAction {

            public QuizRecordDeleteAction() {
                super("X");
            }

            public void actionPerformed(ActionEvent e) {
                //this change will be saved to file when this window is closed.
                IOManager.getQuizHistoryFile().removeEntry(record);
                rebuild();
            }
        }
    }
}
