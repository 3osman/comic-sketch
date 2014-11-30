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
import java.io.Serializable;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import models.DrawableItem;
import models.Layer;
import models.Panel;
import models.PathItem;

/**
 * Controller for drawable items (Panels, Lines)
 * @author Osman
 */
public class DrawableItemController implements Serializable{
   
    

    /**
     * Selects an item from the canvas
     * @param di the item to be selected
     */
    public void select(DrawableItem di) {
        
        di.setIsSelected(true);
        di.getCanvas().repaint();
    }

    /**
     * Deselects an item from the canvas
     * @param di the item to be deselected
     */
    public void deselect(DrawableItem di) {
        di.setIsSelected(false);
        
        di.getCanvas().repaint();
    }

    /**
     *  Fills a panel
     * @param di panel
     * @param g graphics for filling
     */
    protected void fillShape(DrawableItem di, Graphics2D g) {
        g.setColor(di.getFill());
        g.fill(di.getShape());
    }

    /**
     * Draws a shape using graphics2D
     * @param di shape to be drawn
     * @param g  graphics to draw
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
     * Creates eraser cursor
     * @return Cursor of the eraser
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
     * Paints a shape
     * @param di shape to be painted
     * @param g graphics to paint
     */
    public void paint(DrawableItem di, Graphics2D g) {
        
        fillShape(di, g);
        drawShape(di, g);
    }

    /**
     * Paints a curve/pathitem
     * @param di Curve to be painted
     * @param g Graphics to paint
     */
    public void paintPath(DrawableItem di, Graphics2D g) {
        
        drawShape(di, g);
    }
   

    /**
     * Checks if item contains a point
     * @param di Item or shape
     * @param p Point to be checked
     * @return
     */
    public Boolean contains(DrawableItem di, Point p) {
        return di.getShape().contains(p);
    }

    /**
     * Aligns panel on 9 anchors in the canvas, not finished
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
                
                Point temp = gd.getClosestAnchor(new Point(t.x, t.y), points);
                ((Panel) di).moveA(temp.x, temp.y);
                
            }
        }
        
    }
    
}
