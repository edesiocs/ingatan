/*
 * RewardsPane.java
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
package org.ingatan.component.statcentre;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.ingatan.ThemeConstants;
import org.ingatan.io.IOManager;

/**
 * A little store with icons (+caption) that cost a particular number of points. Several
 * default icons come bundled with Ingatan, with the user able to add new ones (these are
 * added to the collections folder under new category 'rewards'). The user can add rewards
 * such as "Bottle of Wine", or "Chocolate Bar", etc. and set a number of points required
 * to 'buy' that reward. This is a motivational tool that the user can either enjoy or ignore.
 * 
 * @author Thomas Everingham
 * @version 1.0
 */
public class RewardsPane extends JPanel {

    /** Button for adding a reward. */
    JButton btnAddReward = new JButton(new AddRewardAction());
    /** Button for editing a reward. */
    JButton btnEditRewards = new JButton(new AddEditRewardsAction());
    /** Button for showing a dialog that explains the rewards system. */
    JButton btnHelp = new JButton(new HelpAction());
    /** Label to show the user's pts balance */
    JLabel lblPoints = new JLabel("Balance: ");
    /** Panel to hold the rewards in the scroller. */
    JPanel scrollerContent = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
    /** Scroller for the rewards */
    JScrollPane scroller = new JScrollPane(scrollerContent);

    /**
     * Creates a new <code>QuizHistoryWindow</code>.
     * @param returnToOnClose the window to return to once this window has closed.
     */
    public RewardsPane() {
        this.setSize(new Dimension(500, 500));
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        lblPoints.setText("Balance: " + IOManager.getQuizHistoryFile().getTotalScore() + " pts");
        lblPoints.setFont(ThemeConstants.hugeFont.deriveFont(16.0f));
        lblPoints.setAlignmentX(LEFT_ALIGNMENT);
        ArrayList<String> descriptions = IOManager.getQuizHistoryFile().getRewardDescriptions();
        ArrayList<String> iconPaths = IOManager.getQuizHistoryFile().getRewardIconPaths();
        ArrayList<Number> prices = IOManager.getQuizHistoryFile().getRewardPrices();

        if (descriptions.size() == 0) {
            //add a label that says there are no rewards added.
            JLabel lblNoRewards = new JLabel("No rewards have been added yet.");
            lblNoRewards.setFont(ThemeConstants.niceFont.deriveFont(14.0f));
            scrollerContent.add(lblNoRewards);
        } else {
            for (int i = 0; i < descriptions.size(); i++) {
                scrollerContent.add(new RewardItem(descriptions.get(i), prices.get(i).intValue(), iconPaths.get(i)));
            }
        }

        scrollerContent.setPreferredSize(new Dimension(450, 500));

        scroller.setAlignmentX(LEFT_ALIGNMENT);
        scroller.setMaximumSize(new Dimension(560, 300));
        scroller.setPreferredSize(new Dimension(485, 300));
        scroller.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, ThemeConstants.borderUnselected));

        this.add(Box.createHorizontalStrut(15));
        Box vert = Box.createVerticalBox();
        vert.add(Box.createVerticalStrut(10));
        vert.add(lblPoints);
        vert.add(Box.createVerticalStrut(20));
        vert.add(scroller);
        vert.setAlignmentY(TOP_ALIGNMENT);
        this.add(vert);

        this.add(Box.createHorizontalStrut(8));

        vert = Box.createVerticalBox();
        vert.add(Box.createVerticalStrut(50));
        vert.add(btnAddReward);
        btnAddReward.setMargin(new Insets(1, 1, 1, 1));
        btnAddReward.setToolTipText("Add a reward");
        vert.add(Box.createVerticalStrut(5));
        vert.add(btnEditRewards);
        btnEditRewards.setMargin(new Insets(1, 1, 1, 1));
        btnEditRewards.setToolTipText("Edit/Remove selected rewards");
        vert.add(Box.createVerticalStrut(5));
        vert.add(btnHelp);
        btnHelp.setMargin(new Insets(1, 1, 1, 1));
        btnHelp.setToolTipText("About the rewards list");
        vert.setAlignmentY(TOP_ALIGNMENT);
        this.add(vert);

    }

    /** Action for the edit rewards button. */
    private static class AddEditRewardsAction extends AbstractAction {

        public AddEditRewardsAction() {
            super("", new ImageIcon(RewardsPane.class.getResource("/resources/icons/image/pencil.png")));
        }

        public void actionPerformed(ActionEvent e) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    /** Action for the add rewards button. */
    private static class AddRewardAction extends AbstractAction {

        public AddRewardAction() {
            super("", new ImageIcon(RewardsPane.class.getResource("/resources/icons/add.png")));
        }

        public void actionPerformed(ActionEvent e) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    /** Action for the add rewards button. */
    private static class HelpAction extends AbstractAction {

        public HelpAction() {
            super("", new ImageIcon(RewardsPane.class.getResource("/resources/icons/help.png")));
        }

        public void actionPerformed(ActionEvent e) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
}
