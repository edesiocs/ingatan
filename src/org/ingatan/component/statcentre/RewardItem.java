/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ingatan.component.statcentre;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import org.ingatan.ThemeConstants;

/**
 * A panel that displays a reward icon and its price. Encapsulates and displays
 * a selected/unselected state.
 * @author Thomas Everingham
 */
public class RewardItem extends JPanel implements MouseListener {

    /** The size of the reward item */
    public static final float SIDE_DIMENSION = 140;
    /** The size of the icon */
    private static final float ICON_DIMENSION = 48;
    /** The line weight of the outline of the reward item. */
    private static final float OUTLINE_WEIGHT = 1.5f;
    /** The circle shape border of the reward item. */
    private static final Ellipse2D CIRC = new Ellipse2D.Float(3, 3, SIDE_DIMENSION - 6, SIDE_DIMENSION - 6);
    /** Icon drawn to the reward item. */
    private ImageIcon rewardIcon = null;
    /** Brief description of the reward, for example "Bottle of Wine". */
    private String description = "Reward";
    /** Path to the rewards icon. If it is prefixed with 'jar://', then class loader should be used.*/
    private String rewardIconPath = "";
    /** How many points the item costs. */
    private int price = 0;
    /** Flag indicating whether or not this item is selected. */
    private boolean selected = false;

    /**
     * Creates a new RewardItem object.
     * @param description the description of the reward (e.g. "Bar of Chocolate").
     * @param price the price of the reward in points.
     * @param iconPath The path to the icon to be used. If prefixed with "jar://", then
     * class loader will be used.
     */
    public RewardItem(String description, int price, String iconPath) {
        this.setSize((int) SIDE_DIMENSION, (int) SIDE_DIMENSION);
        this.setMaximumSize(new Dimension((int) SIDE_DIMENSION, (int) SIDE_DIMENSION));
        this.setPreferredSize(new Dimension((int) SIDE_DIMENSION, (int) SIDE_DIMENSION));
        this.addMouseListener(this);
        this.description = description;
        this.price = price;
        this.rewardIconPath = iconPath;
        //load the icon
        if (rewardIconPath.startsWith("jar://")) {
            URL tmpURL = RewardItem.class.getResource(rewardIconPath.replace("jar://", "/"));
            if (tmpURL == null) {
                //this is the default iamge if the other cannot be loaded
                tmpURL = RewardItem.class.getResource("/resources/rewards/qmark.png");
            }
            rewardIcon = new ImageIcon(tmpURL);
        } else {
            //load from disk
        }
    }

    /**
     * Checks if this RewardItem is selected.
     * @return whether or not this RewardItem is selected.
     */
    public boolean isSelected() {
        return selected;
    }

    public String getDescription() {
        return description;
    }

    public int getPrice() {
        return price;
    }

    public String getRewardIconPath() {
        return rewardIconPath;
    }

    public ImageIcon getIcon() {
        return rewardIcon;
    }

    public void resetData(String description, int price, String iconPath) {
        this.description = description;
        this.price = price;
        this.rewardIconPath = iconPath;
        //load the icon
        if (rewardIconPath.startsWith("jar://")) {
            URL tmpURL = RewardItem.class.getResource(rewardIconPath.replace("jar://", "/"));
            if (tmpURL == null) {
                //this is the default iamge if the other cannot be loaded
                tmpURL = RewardItem.class.getResource("/resources/rewards/qmark.png");
            }
            rewardIcon = new ImageIcon(tmpURL);
        } else {
            //load from disk
        }
        this.repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        //show selection through bg colour
        if (selected) {
            g2d.setPaint(ThemeConstants.backgroundUnselected);
        } else {
            g2d.setPaint(Color.white);
        }

        g2d.fill(CIRC);
        g2d.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, OUTLINE_WEIGHT, new float[]{7, 5}, 0));
        g2d.setPaint(ThemeConstants.textColour);
        g2d.draw(CIRC);

        g2d.setFont(ThemeConstants.niceFont.deriveFont(12.0f));
        int descWidth = g2d.getFontMetrics().stringWidth(description);
        int priceWidth = g2d.getFontMetrics().stringWidth(price + " pts");
        g2d.drawString(description, (int) ((SIDE_DIMENSION - descWidth)/2.0), (int) (SIDE_DIMENSION * 0.23));
        g2d.drawString(price + " pts", (int) ((SIDE_DIMENSION - priceWidth)/2.0), (int) (SIDE_DIMENSION - (SIDE_DIMENSION * 0.18)));

        if (rewardIcon != null) {
            g2d.drawImage(rewardIcon.getImage(), (int) ((SIDE_DIMENSION / 2) - (ICON_DIMENSION / 2)), (int) ((SIDE_DIMENSION / 2) - (ICON_DIMENSION / 2)), null);
        }
    }

    public void mouseClicked(MouseEvent e) {
        if (CIRC.contains(e.getX(), e.getY())) {
            selected = !selected;
        }
        this.repaint();
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
