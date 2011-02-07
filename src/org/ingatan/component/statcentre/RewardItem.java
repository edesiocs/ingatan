/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ingatan.component.statcentre;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import org.ingatan.ThemeConstants;

/**
 * A panel that displays a reward icon and its price. Encapsulates and displays
 * a selected/unselected state.
 * @author Thomas Everingham
 */
public class RewardItem extends JPanel implements MouseListener {
    
    private static final float SIDE_DIMENSION = 120;
    private static final float ICON_DIMENSION = 32;
    private static final float OUTLINE_WEIGHT = 1.5f;
    private static final Ellipse2D CIRC = new Ellipse2D.Float(3, 3, SIDE_DIMENSION-6, SIDE_DIMENSION-6);
    /** icon drawn to the reward item. */
    private ImageIcon rewardIcon = null;
    private boolean selected = false;

    public RewardItem() {
        this.setSize((int) SIDE_DIMENSION, (int) SIDE_DIMENSION);
        this.setMaximumSize(new Dimension((int) SIDE_DIMENSION, (int) SIDE_DIMENSION));
        this.setPreferredSize(new Dimension((int) SIDE_DIMENSION, (int) SIDE_DIMENSION));
        this.addMouseListener(this);
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

        if (rewardIcon != null) {
            g2d.drawImage(rewardIcon.getImage(), (int) ((SIDE_DIMENSION / 2) - (ICON_DIMENSION / 2)), (int) ((SIDE_DIMENSION / 2) - (ICON_DIMENSION / 2)), null);
        }
    }

    public void mouseClicked(MouseEvent e) {
        if (CIRC.contains(e.getX(), e.getY()))
            selected = !selected;
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
