/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import models.DrawableItem;
import models.Panel;

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
        di.setFill(c);;
        di.getCanvas().repaint();
    }

    public void select(DrawableItem di) {

        di.setIsSelected(true);
        di.getCanvas().repaint();
    }

    public void deselect(DrawableItem di) {
        di.setIsSelected(false);

        di.getCanvas().repaint();
    }

    protected void fillShape(DrawableItem di, Graphics2D g) {
        g.setColor(di.getFill());
        g.fill(di.getShape());
    }

    protected void drawShape(DrawableItem di, Graphics2D g) {
        Stroke oldstrk = null;
        if (di.getIsSelected()) {
            oldstrk = g.getStroke();
            g.setStroke(new BasicStroke(2));
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

    public void paint(DrawableItem di, Graphics2D g) {

        fillShape(di, g);
        drawShape(di, g);
    }

    public void paintPath(DrawableItem di, Graphics2D g) {
        drawShape(di, g);
    }

    public Boolean contains(DrawableItem di, Point p) {
        return di.getShape().contains(p);
    }

}
