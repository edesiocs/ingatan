/*
 * RewardEditorDialog.java
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

import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;
import org.ingatan.ThemeConstants;
import org.ingatan.component.text.SimpleTextField;
import org.ingatan.io.IOManager;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseListener;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import org.ingatan.component.image.Thumbnail;
import org.ingatan.component.image.ThumbnailPane;
import org.ingatan.component.text.NumericJTextField;

/**
 * Provides a dialog allowing the user to edit the reward and its price, as well as the icon
 * to use.
 *
 * @author Thomas Everingham
 * @version 1.0
 */
public class RewardEditorDialog extends JDialog implements FocusListener {

    private String[] iconFilenames = new String[]{"wine.png", "coffee.png", "choc.png", "fruit.png", "tree.png", "sun.png", "soccer.png", "cards.png", "game.png", "movie.png", "birdwatch.png", "swissarmy.png", "book.png", "mail.png", "music.png", "origami.png", "net.png", "camera.png", "webcam.png", "phone.png", "radio.png"};
    /**
     * Label for the reward name field.
     */
    private JLabel lblRewardDesc = new JLabel("Reward: ");
    /**
     * Label for the reward price field.
     */
    private JLabel lblRewardPrice = new JLabel("Price: ");
    /**
     * Reward name edit field.
     */
    private SimpleTextField rewardName = new SimpleTextField("Chocolate Bar");
    /**
     * Price edit field.
     */
    private NumericJTextField rewardPrice = new NumericJTextField(350);
    /**
     * Button to accept changes.
     */
    private JButton btnOkay = new JButton(new ProceedAction());
    /**
     * Button to cancel changes.
     */
    private JButton btnCancel = new JButton(new CancelAction());
    /**
     * Content pane of the dialog.
     */
    private JPanel contentPane = new JPanel();
    /**
     * Button for showing the icon selection pane.
     */
    private JButton btnSetIcon = new JButton(new IconAction());
    /**
     * Preview of the reward.
     */
    private RewardItem reward = new RewardItem("Reward", 100, "jar://resources/rewards/qmark.png");
    /**
     * Thumbnail pane with possible icons.
     */
    private ThumbnailPane iconsPane;
    /**
     * Scroll pane for the icons thumbnail pane.
     */
    private JScrollPane iconsScroller;
    /**
     * Popup menu to hold the icon thumbnail pane.
     */
    private JPopupMenu iconsPopup = new JPopupMenu();
    /**
     * Flag that indicates whether this dialog is being used to create a new reward, or edit an existing one.
     */
    private boolean newReward = true;
    /** Set if the user cancelled. True default value protects against user closing window. */
    private boolean userCancelled = true;
    /**
     * Filename of the selected icon.
     */
    private String iconFilename = "jar://resoures/rewards/qmark.png";

    /**
     * Creates a new <code>RewardEditorDialog</code>.
     * @param parent the parent window for this dialog.
     * @param newReward whether or not this dialog is being used to create a new reward, false
     *        if this dialog is being used to edit a reward.
     */
    public RewardEditorDialog(boolean newReward) {
        this.setModal(true);
        this.setIconImage(IOManager.windowIcon);
        this.setContentPane(contentPane);
        this.newReward = newReward;
        if (newReward) {
            this.setTitle("Add a new Reward");
        } else {
            this.setTitle("Edit Reward");
        }

        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));
        loadIcons();
        setUpGUI();

        rewardName.requestFocus();
        rewardName.selectAll();
        rewardName.addFocusListener(this);
        rewardPrice.addFocusListener(this);

        this.setPreferredSize(new Dimension(400, 250));
        this.setMinimumSize(new Dimension(400, 250));
        this.setMaximumSize(new Dimension(400, 250));

        this.setLocationRelativeTo(null);

        this.setResizable(false);
    }

    public void setIconFilename(String iconFilename) {
        this.iconFilename = iconFilename;
    }

    private void loadIcons() {
        ImageIcon[] thumbnails = new ImageIcon[iconFilenames.length];
        for (int i = 0; i < iconFilenames.length; i++) {
            thumbnails[i] = new ImageIcon(RewardsPane.class.getResource("/resources/rewards/" + iconFilenames[i]));
        }
        iconsPane = new ThumbnailPane(thumbnails, 48);
        iconsPane.addThumbnailMouseListener(new IconThumbnailListener());
    }

    private void setUpGUI() {
        contentPane.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        rewardName.setMaximumSize(new Dimension(200, 25));
        lblRewardDesc.setAlignmentX(LEFT_ALIGNMENT);
        lblRewardDesc.setHorizontalAlignment(SwingConstants.LEFT);
        rewardName.setBorder(BorderFactory.createLineBorder(ThemeConstants.borderUnselected));
        lblRewardPrice.setHorizontalAlignment(SwingConstants.LEFT);
        lblRewardPrice.setAlignmentX(LEFT_ALIGNMENT);
        rewardPrice.setBorder(BorderFactory.createLineBorder(ThemeConstants.borderUnselected));
        rewardName.setAlignmentX(LEFT_ALIGNMENT);
        rewardPrice.setMaximumSize(new Dimension(200, 25));
        rewardPrice.setAlignmentX(LEFT_ALIGNMENT);

        btnOkay.setPreferredSize(new Dimension(80, 25));
        iconsScroller = new JScrollPane(iconsPane);
        iconsScroller.setPreferredSize(new Dimension(350, 200));
        iconsPopup.add(iconsScroller);


        Box vert = Box.createVerticalBox();
        vert.add(lblRewardDesc);
        vert.add(Box.createVerticalStrut(3));
        vert.add(rewardName);
        vert.add(Box.createVerticalStrut(10));
        vert.add(lblRewardPrice);
        vert.add(Box.createVerticalStrut(3));
        vert.add(rewardPrice);
        vert.add(Box.createVerticalStrut(5));
        vert.add(btnSetIcon);
        vert.add(Box.createVerticalStrut(20));
        Box horiz = Box.createHorizontalBox();
        horiz.setAlignmentX(LEFT_ALIGNMENT);
        horiz.setMaximumSize(new Dimension(200, 40));
        horiz.add(btnOkay);
        horiz.add(Box.createHorizontalGlue());
        horiz.add(btnCancel);
        vert.add(horiz);

        this.add(reward);
        this.add(Box.createHorizontalStrut(40));
        this.add(vert);

        this.pack();

    }

    /**
     * Sets the reward name text.
     * @param text the new reward name text.
     */
    public void setNameText(String text) {
        rewardName.setText(text);
    }

    /**
     * Sets the reward description text.
     * @param text the reward description text.
     */
    public void setPriceText(String text) {
        rewardPrice.setText(text);
    }

    /**
     * Gets the reward name text.
     * @return the reward name text.
     */
    public String getNameText() {
        return rewardName.getText();
    }

    /**
     * Gets the reward description text.
     * @return the reward description text.
     */
    public String getPriceText() {
        return rewardPrice.getText();
    }

    /**
     * Gets the path to the selected icon.
     * @return the path to the selected icon.
     */
    public String getIconPath() {
        return iconFilename;
    }

    public boolean isUserCancelled() {
        return userCancelled;
    }

    public void focusGained(FocusEvent e) {}

    public void focusLost(FocusEvent e) {
        reward.resetData(rewardName.getText(), rewardPrice.getValue(), iconFilename);
        reward.repaint();
    }

    private class ProceedAction extends AbstractAction {

        public ProceedAction() {
            super("Okay");
        }

        public void actionPerformed(ActionEvent e) {
            userCancelled = false;
            RewardEditorDialog.this.setVisible(false);
        }
    }

    private class CancelAction extends AbstractAction {

        public CancelAction() {
            super("Cancel");
        }

        public void actionPerformed(ActionEvent e) {
            userCancelled = true;
            RewardEditorDialog.this.setVisible(false);
        }
    }

    private class IconAction extends AbstractAction {

        public IconAction() {
            super("Set Icon");
        }

        public void actionPerformed(ActionEvent e) {
            iconsPopup.show(btnOkay, 40, 40);
        }
    }

    private class IconThumbnailListener implements MouseListener {

        public void mouseClicked(MouseEvent e) {
            iconFilename = "jar://resources/rewards/" + iconFilenames[((Thumbnail) e.getSource()).getIndex()];
            reward.resetData(rewardName.getText(), rewardPrice.getValue(), iconFilename);
            iconsPopup.setVisible(false);
            reward.repaint();
        }

        public void mousePressed(MouseEvent e) {
        }

        public void mouseReleased(MouseEvent e) {
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }
    }
}
