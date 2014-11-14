/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import UI.PersistentCanvas;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import models.DrawableItem;
import models.Layer;
import models.Panel;
import models.PathItem;

/**
 *
 * @author Osman
 */
public class DrawableItemController {
    
    public Color getOutline(DrawableItem di) {
        return di.getOutline();
    }
    
    public Color getFill(DrawableItem di) {
        return di.getFill();
    }
    
    public void setOutlineColor(DrawableItem di, Color c) {
        di.setOutline(c);
        di.getCanvas().repaint();
    }
    
    public void setFillColor(DrawableItem di, Color c) {
        di.setFill(c);
        di.getCanvas().repaint();
    }

    /**
     *
     * @param di
     */
    public void select(DrawableItem di) {
        
        di.setIsSelected(true);
        di.getCanvas().repaint();
    }

    /**
     *
     * @param di
     */
    public void deselect(DrawableItem di) {
        di.setIsSelected(false);
        
        di.getCanvas().repaint();
    }

    /**
     *
     * @param di
     * @param g
     */
    protected void fillShape(DrawableItem di, Graphics2D g) {
        g.setColor(di.getFill());
        g.fill(di.getShape());
    }

    /**
     *
     * @param di
     * @param g
     */
    protected void drawShape(DrawableItem di, Graphics2D g) {
        
        Stroke oldstrk = null;
        if (di.getIsSelected()) {
            oldstrk = g.getStroke();
            g.setStroke(new BasicStroke(2));
            if (di instanceof PathItem) {
                g.setStroke(new BasicStroke(((PathItem) di).getThickness()));
            }
        }
        g.setColor(di.getOutline());
        g.draw(di.getShape());
        if (di instanceof Panel && di.getIsSelected()) {
            g.draw(((Panel) di).getFirstCorner());
            g.draw(((Panel) di).getSecondCorner());
            g.draw(((Panel) di).getThirdCorner());
            g.draw(((Panel) di).getFourthCorner());
        }
        if (oldstrk != null) {
            g.setStroke(oldstrk);
        }
    }

    /**
     *
     * @return
     */
    public Cursor createEraserCursor() {
        ImageIcon image = new ImageIcon(this.getClass().getResource("/rubber.gif"));
        Toolkit kit = Toolkit.getDefaultToolkit();
        
        Image img = image.getImage();
        
        BufferedImage bi = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        
        Graphics g = bi.createGraphics();
        g.drawImage(img, 0, 0, 10, 10, null);
        ImageIcon newIcon = new ImageIcon(bi);
        return kit.createCustomCursor(newIcon.getImage(), new Point(7, 7), "rubber");
    }

    /**
     *
     * @param di
     * @param g
     */
    public void paint(DrawableItem di, Graphics2D g) {
        
        fillShape(di, g);
        drawShape(di, g);
    }

    /**
     *
     * @param di
     * @param g
     */
    public void paintPath(DrawableItem di, Graphics2D g) {
        
        drawShape(di, g);
    }
    
    public JPanel getLayerDrawing(Layer l,   JButton deleteButton, JToggleButton show, JCheckBox select) {
        
        JPanel panel3 = new JPanel();
        panel3.setLayout(new FlowLayout());
        JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayout(3, 1));//buttons for layer
        panel4.add(select);
        panel4.add(show);
        panel4.add(deleteButton);
        // panel4.add(new JButton("dfsf"));
        panel3.add(panel4);//each layer}
      
        //=========
        //add images here
        //=========

        return panel3;
    }

    /**
     *
     * @param di
     * @param p
     * @return
     */
    public Boolean contains(DrawableItem di, Point p) {
        return di.getShape().contains(p);
    }

    /**
     *
     * @param canvas
     * @param gd
     * @param points
     * @param width
     * @param height
     */
    public void allign(PersistentCanvas canvas, GroupingController gd, ArrayList<Point> points, int width, int height) {
        
        for (DrawableItem di : canvas.getItems()) {
            if (di instanceof Panel) {
                Rectangle t = ((Rectangle) (((Panel) di).getShape()));
                
                Point temp = gd.getClosestAnchor(new Point(t.x, t.y), points, width, height);
                ((Panel) di).moveA(temp.x, temp.y);
                
            }
        }
        
    }
    
}
