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
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.ingatan.ThemeConstants;
import org.ingatan.component.librarymanager.LibraryManagerWindow;
import org.ingatan.component.librarymanager.TableQuestionContainer;
import org.ingatan.component.text.EmbeddedImage;
import org.ingatan.component.text.RichTextArea;
import org.ingatan.io.IOManager;
import org.ingatan.io.ParserWriter;

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
public class RewardsPane extends JPanel implements ContainerListener {

    /** Vertical spacing between reward items. */
    private static final int VERT_SPACING = 15;
    /** Horizontal spacing between reward items. */
    private static final int HORIZ_SPACING = 15;
    /** number of rewards per row. */
    private static final int COLS = 3;
    /** Button for adding a reward. */
    JButton btnAddReward = new JButton(new AddRewardAction());
    /** Button for editing reward(s). */
    JButton btnEditRewards = new JButton(new EditRewardsAction());
    /** Button for deleting reward(s). */
    JButton btnDeleteRewards = new JButton(new DeleteRewardsAction());
    /** Button for showing a dialog that explains the rewards system. */
    JButton btnHelp = new JButton(new HelpAction());
    /** Button for the user to 'buy' the selected items */
    JButton btnBuy = new JButton(new BuyAction());
    /** Label to show the user's pts balance */
    JLabel lblPoints = new JLabel("Balance: ");
    /** Panel to hold the rewards in the scroller. */
    JPanel scrollerContent = new JPanel(new FlowLayout(FlowLayout.LEFT, HORIZ_SPACING, VERT_SPACING));
    /** Scroller for the rewards */
    JScrollPane scroller = new JScrollPane(scrollerContent);
    /**
     * Keeps track of items that have been added.
     */
    ArrayList<RewardItem> rewardItems = new ArrayList<RewardItem>();

    /**
     * Creates a new <code>QuizHistoryWindow</code>.
     * @param returnToOnClose the window to return to once this window has closed.
     */
    public RewardsPane() {
        this.setSize(new Dimension(600, 500));
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        lblPoints.setText("Balance: " + IOManager.getQuizHistoryFile().getTotalScore() + " pts");
        lblPoints.setFont(ThemeConstants.hugeFont.deriveFont(16.0f));
        lblPoints.setAlignmentX(LEFT_ALIGNMENT);
        ArrayList<String> descriptions = IOManager.getQuizHistoryFile().getRewardDescriptions();
        ArrayList<String> iconPaths = IOManager.getQuizHistoryFile().getRewardIconPaths();
        ArrayList<Number> prices = IOManager.getQuizHistoryFile().getRewardPrices();

        scrollerContent.addContainerListener(this);

        if (descriptions.size() == 0) {
            //add a label that says there are no rewards added.
            JLabel lblNoRewards = new JLabel("No rewards have been added yet.");
            lblNoRewards.setFont(ThemeConstants.niceFont.deriveFont(14.0f));
            scrollerContent.add(lblNoRewards);
        } else {
            for (int i = 0; i < descriptions.size(); i++) {
                RewardItem item = new RewardItem(descriptions.get(i), prices.get(i).intValue(), iconPaths.get(i));
                rewardItems.add(item);
                scrollerContent.add(item);
            }
        }

        scrollerContent.setPreferredSize(new Dimension(450, 500));

        scroller.setAlignmentX(LEFT_ALIGNMENT);
        scroller.setMaximumSize(new Dimension(560, 320));
        scroller.setMinimumSize(new Dimension(485, 320));
        scroller.setPreferredSize(new Dimension(485, 320));
        scroller.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, ThemeConstants.borderUnselected));

        this.add(Box.createHorizontalStrut(15));
        Box vert = Box.createVerticalBox();
        vert.add(Box.createVerticalStrut(10));
        vert.add(lblPoints);
        vert.add(Box.createVerticalStrut(20));
        vert.add(scroller);
        vert.setAlignmentY(TOP_ALIGNMENT);
        this.add(vert);

        this.add(Box.createHorizontalStrut(20));

        vert = Box.createVerticalBox();
        vert.add(Box.createVerticalStrut(50));
        vert.add(btnAddReward);
        btnAddReward.setMargin(new Insets(1, 1, 1, 1));
        btnAddReward.setToolTipText("Add a reward");
        vert.add(Box.createVerticalStrut(5));
        vert.add(btnDeleteRewards);
        btnDeleteRewards.setMargin(new Insets(1, 1, 1, 1));
        btnDeleteRewards.setToolTipText("Delete selected rewards");
        vert.add(Box.createVerticalStrut(5));
        vert.add(btnEditRewards);
        btnEditRewards.setMargin(new Insets(1, 1, 1, 1));
        btnEditRewards.setToolTipText("Edit selected rewards");
        vert.add(Box.createVerticalStrut(5));
        vert.add(btnHelp);
        btnHelp.setMargin(new Insets(1, 1, 1, 1));
        btnHelp.setToolTipText("About the rewards list");
        vert.add(Box.createVerticalStrut(20));
        vert.add(btnBuy);
        btnBuy.setMargin(new Insets(1, 1, 1, 1));
        btnBuy.setToolTipText("'Buy' selected rewards");
        vert.setAlignmentY(TOP_ALIGNMENT);
        this.add(vert);

    }

    public void componentAdded(ContainerEvent e) {
        //this is the listener method for the scrollerContent pane
        int rows = rewardItems.size() / COLS;
        rows++;
        int height = (int) (rows * (RewardItem.SIDE_DIMENSION + VERT_SPACING));
        int width = (int) (COLS * (RewardItem.SIDE_DIMENSION + HORIZ_SPACING));
        scrollerContent.setPreferredSize(new Dimension(width, height));
        scrollerContent.validate();
        scrollerContent.repaint();
        scroller.validate();
    }

    public void componentRemoved(ContainerEvent e) {
        //this is the listener method for the scrollerContent pane
        int rows = rewardItems.size() / COLS;
        rows++;
        int height = (int) (rows * (RewardItem.SIDE_DIMENSION + VERT_SPACING));
        int width = (int) (COLS * (RewardItem.SIDE_DIMENSION + HORIZ_SPACING));
        scrollerContent.setPreferredSize(new Dimension(width, height));
        scrollerContent.validate();
        scrollerContent.repaint();
        scroller.validate();
    }

    /** Action for the edit rewards button. */
    private class EditRewardsAction extends AbstractAction {

        public EditRewardsAction() {
            super("", new ImageIcon(RewardsPane.class.getResource("/resources/icons/image/pencil.png")));
        }

        public void actionPerformed(ActionEvent e) {
            RewardEditorDialog editor;
            //temporarily hold reward items to edit.
            ArrayList<RewardItem> toEdit = new ArrayList<RewardItem>();
            //get all selected reward items.
            for (int i = 0; i < rewardItems.size(); i++) {
                if (rewardItems.get(i).isSelected()) {
                    toEdit.add(rewardItems.get(i));
                }
            }

            if (toEdit.size() == 0) {
                JOptionPane.showMessageDialog(RewardsPane.this, "There are no rewards selected.", "No Selection", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            for (int i = 0; i < toEdit.size(); i++) {
                editor = new RewardEditorDialog(false);
                editor.setNameText(toEdit.get(i).getDescription());
                editor.setPriceText(String.valueOf(toEdit.get(i).getPrice()));
                editor.setIconFilename(toEdit.get(i).getRewardIconPath());
                //this causes the preview reward item to be updated
                editor.focusLost(null);
                editor.setVisible(true);

                //if the user cancelled, don't save any of that data
                if (editor.isUserCancelled()) {
                    continue;
                }
                toEdit.get(i).resetData(editor.getNameText(), Integer.valueOf(editor.getPriceText()), editor.getIconPath());
            }

            //save changes
            IOManager.getQuizHistoryFile().replaceRewardsData(rewardItems);
            ParserWriter.writeQuizHistoryFile(IOManager.getQuizHistoryFile());
        }
    }

    /** Action for the add rewards button. */
    private class AddRewardAction extends AbstractAction {

        public AddRewardAction() {
            super("", new ImageIcon(RewardsPane.class.getResource("/resources/icons/add.png")));
        }

        public void actionPerformed(ActionEvent e) {
            RewardEditorDialog editor = new RewardEditorDialog(true);
            editor.setVisible(true);
            if (editor.isUserCancelled()) {
                return;
            }
            //if there are no rewards in the scroller, must remove the label
            if (IOManager.getQuizHistoryFile().getRewardDescriptions().isEmpty()) {
                scrollerContent.removeAll();
            }

            IOManager.getQuizHistoryFile().getRewardDescriptions().add(editor.getNameText());
            IOManager.getQuizHistoryFile().getRewardPrices().add(Integer.valueOf(editor.getPriceText()));
            IOManager.getQuizHistoryFile().getRewardIconPaths().add(editor.getIconPath());
            RewardItem item = new RewardItem(editor.getNameText(), Integer.valueOf(editor.getPriceText()), editor.getIconPath());
            rewardItems.add(item);
            scrollerContent.add(item);
            ParserWriter.writeQuizHistoryFile(IOManager.getQuizHistoryFile());
            scrollerContent.validate();
            scrollerContent.repaint();
        }
    }

    /** Action for the add rewards button. */
    private class DeleteRewardsAction extends AbstractAction {

        public DeleteRewardsAction() {
            super("", new ImageIcon(RewardsPane.class.getResource("/resources/icons/remove.png")));
        }

        public void actionPerformed(ActionEvent e) {
            //temporarily hold reward items to delete.
            ArrayList<RewardItem> delete = new ArrayList<RewardItem>();

            //get all selected reward items.
            for (int i = 0; i < rewardItems.size(); i++) {
                if (rewardItems.get(i).isSelected()) {
                    delete.add(rewardItems.get(i));
                }
            }

            //tell user if none are selected
            if (delete.size() == 0) {
                JOptionPane.showMessageDialog(RewardsPane.this, "There are no rewards selected.", "No Selection", JOptionPane.INFORMATION_MESSAGE);
                return;
            } else {
                int resp = JOptionPane.showConfirmDialog(RewardsPane.this, "Are you sure you wish to delete all selected rewards?", "Delete Rewards?", JOptionPane.YES_NO_OPTION);
                if (resp == JOptionPane.YES_OPTION) {
                    for (int i = 0; i < delete.size(); i++) {
                        rewardItems.remove(delete.get(i));
                        scrollerContent.remove(delete.get(i));
                    }
                    //write changes to file
                    IOManager.getQuizHistoryFile().replaceRewardsData(rewardItems);
                    ParserWriter.writeQuizHistoryFile(IOManager.getQuizHistoryFile());
                    scrollerContent.validate();
                    scrollerContent.repaint();
                } else {
                    return;
                }
            }
        }
    }

    /** Action for the add rewards button. */
    private class HelpAction extends AbstractAction {

        public HelpAction() {
            super("", new ImageIcon(RewardsPane.class.getResource("/resources/icons/help.png")));
        }

        public void actionPerformed(ActionEvent e) {
            RichTextArea dispArea = new RichTextArea();

            dispArea.setPreferredSize(new Dimension(450, 415));
            dispArea.setSize(new Dimension(450, 415));
            dispArea.setMinimumSize(new Dimension(450, 415));

            dispArea.setBorder(BorderFactory.createEmptyBorder());
            dispArea.setEditable(false);
            dispArea.setOpaque(false);

            dispArea.setRichText("[aln]0[!aln][fam]Dialog[!fam][sze]16[!sze][col]51,51,51[!col]Rewards Centre Help[sze]12[!sze][br]"
                    + "The rewards centre is a motivational tool that lets you set rewards for yourself with a corresponding price in points. Whenever you take a quiz in Ingatan "
                    + "you earn points; you earn more if you improve on a question, or if you get many questions correct in a row. "
                    + "When you've studied enough, you can buy the reward and indulge![br][br]"
                    + "[u]Create a Reward[u][br]"
                    + "Press the green !osqb;+!csqb; button and enter a short name for the reward and how many points it should cost. Set an icon "
                    + "for the reward by clicking the Set Icon button and then choosing one from the popup list.[br][br]"
                    + "[u]Editing Rewards[u][br]"
                    + "Click on the reward(s) you want to edit to select them, and then press the pencil button to the right of the rewards pane.[br][br]"
                    + "[u]Deleting Rewards[u][br]"
                    + "Click on the reward(s) you want to delete to select them, and then press the red !osqb;-!csqb; button to the right of the rewards pane.[br][br]"
                    + "[u]Buying Rewards[u][br]"
                    + "Once you have enough points, click on the reward(s) you want to buy and then click on the large shopping trolley button. Enjoy![br][br]"
                    + "[end]");

            JOptionPane.showMessageDialog(RewardsPane.this, dispArea, "Rewards Centre Help", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /** Action for the add rewards button. */
    private class BuyAction extends AbstractAction {

        public BuyAction() {
            super("", new ImageIcon(RewardsPane.class.getResource("/resources/rewards/shopping.png")));
        }

        public void actionPerformed(ActionEvent e) {
            //temporarily hold reward items to buy.
            ArrayList<RewardItem> toBuy = new ArrayList<RewardItem>();
            //get all selected reward items.
            for (int i = 0; i < rewardItems.size(); i++) {
                if (rewardItems.get(i).isSelected()) {
                    toBuy.add(rewardItems.get(i));
                }
            }

            //if there aren't any selected rewards to 'buy'
            if (toBuy.size() == 0) {
                JOptionPane.showMessageDialog(RewardsPane.this, "There are no rewards selected.", "No Selection", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            //confirm purchase
            int resp;
            if (toBuy.size() > 1) {
                resp = JOptionPane.showConfirmDialog(RewardsPane.this, "Are you sure you want to buy these " + toBuy.size() + " rewards?", "Confirm Purchase", JOptionPane.YES_NO_OPTION);
            } else {
                resp = JOptionPane.showConfirmDialog(RewardsPane.this, "Are you sure you want to buy this reward?", "Confirm Purchase", JOptionPane.YES_NO_OPTION);
            }
            if (resp == JOptionPane.NO_OPTION) {
                return;
            }

            for (int i = 0; i < toBuy.size(); i++) {
                if (IOManager.getQuizHistoryFile().getTotalScore() - toBuy.get(i).getPrice() >= 0) {
                    JOptionPane.showMessageDialog(RewardsPane.this, "You have traded " + toBuy.get(i).getPrice() + " pts for the reward: " + toBuy.get(i).getDescription() + "."
                            + "\n\nEnjoy!", "Reward", JOptionPane.PLAIN_MESSAGE, toBuy.get(i).getIcon());
                    IOManager.getQuizHistoryFile().addToTotalScore(-1 * toBuy.get(i).getPrice());
                    rewardItems.remove(toBuy.get(i));
                    scrollerContent.remove(toBuy.get(i));
                } else {
                    JOptionPane.showMessageDialog(RewardsPane.this, "You do not have enough points for this reward.", "Reward", JOptionPane.INFORMATION_MESSAGE);
                    break;
                }
            }

            scrollerContent.validate();
            scrollerContent.repaint();
            lblPoints.setText("Balance: " + IOManager.getQuizHistoryFile().getTotalScore() + " pts");

            //write all changes to file
            IOManager.getQuizHistoryFile().replaceRewardsData(rewardItems);
            ParserWriter.writeQuizHistoryFile(IOManager.getQuizHistoryFile());
        }
    }
}
