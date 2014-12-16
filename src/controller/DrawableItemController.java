/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.io.Serializable;

import models.DrawableItem;
import models.Panel;
import models.PathItem;

/**
 * Controller for drawable items (Panels, Lines)
 *
 * @author Osman
 */
public class DrawableItemController implements Serializable {

    /**
     * Selects an item from the canvas
     *
     * @param di the item to be selected
     */
    public void select(DrawableItem di) {

        di.setIsSelected(true);
        di.getCanvas().repaint();
    }

    /**
     * Deselects an item from the canvas
     *
     * @param di the item to be deselected
     */
    public void deselect(DrawableItem di) {
        di.setIsSelected(false);

        di.getCanvas().repaint();
    }

    /**
     * Fills a panel
     *
     * @param di panel
     * @param g graphics for filling
     */
    protected void fillShape(DrawableItem di, Graphics2D g) {
        g.setColor(di.getFill());
        g.fill(di.getShape());
    }

    /**
     * Draws a shape using graphics2D
     *
     * @param di shape to be drawn
     * @param g graphics to draw
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
     * Paints a shape
     *
     * @param di shape to be painted
     * @param g graphics to paint
     */
    public void paint(DrawableItem di, Graphics2D g) {

        fillShape(di, g);
        drawShape(di, g);
    }

    /**
     * Paints a curve/pathitem
     *
     * @param di Curve to be painted
     * @param g Graphics to paint
     */
    public void paintPath(DrawableItem di, Graphics2D g) {

        drawShape(di, g);
    }

    /**
     * Checks if item contains a point
     *
     * @param di Item or shape
     * @param p Point to be checked
     * @return
     */
    public Boolean contains(DrawableItem di, Point p) {
        return di.getShape().contains(p);
    }
}