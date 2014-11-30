/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import UI.PersistentCanvas;
import java.awt.Color;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;

/**
 * Class for sketching lines/curves
 *
 * @author Osman
 */
public class PathItem extends DrawableItem {

    protected Layer layer;
    int thickness;
    Point firstpoint;
    boolean hidden;
    boolean hiddenWithLayer;

    /**
     * Constructor
     *
     * @param c canvas in which the line lies
     * @param o Outline color
     * @param f Fill color
     * @param p Current end
     * @param l Layer it belongs to
     */
    public PathItem(PersistentCanvas c, Color o, Color f, Point p, Layer l) {
        super(c, o, f);
        layer = l;
        type = 1;
        thickness = 2;
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(p.x, p.y);
        l.addObjectToLayer(this);
        hidden = false;
        hiddenWithLayer = false;
        firstpoint = p;
    }

    public PathItem(PathItem other) {
        super(other.canvas, other.outline, other.fill);
        //shape = new Rectangle((Rectangle) other.shape);
        isSelected = false;
        layer = other.layer;
        type = 1;
        hidden = other.hidden;
        firstpoint = other.firstpoint;
    }

    /**
     * Duplicate item
     *
     * @return Duplicated item
     */
    public DrawableItem duplicate() {
        return canvas.addItem(new PathItem(this));
    }

    /**
     * Updates item
     *
     * @param p Point to be drawn a line to
     */
    public void update(Point p) {
        ((GeneralPath) shape).lineTo(p.x, p.y);
        canvas.repaint();
    }

    /**
     * Move path item on moving the panel by dx and dy
     *
     * @param dx
     * @param dy
     */
    public void move(int dx, int dy) {
        AffineTransform at = new AffineTransform();
        at.translate(dx, dy);

        ((GeneralPath) shape).transform(at);

        canvas.repaint();
    }

    //Getters and setters 
    //=========================
    public int getThickness() {
        return thickness;
    }

    public void setThickness(int thickness) {
        this.thickness = thickness;
    }

    public Layer getLayer() {
        return layer;
    }

    public void setLayer(Layer layer) {
        this.layer = layer;
    }

    public boolean isHiddenWithLayer() {
        return hiddenWithLayer;
    }

    public void setHiddenWithLayer(boolean hiddenWithLayer) {
        this.hiddenWithLayer = hiddenWithLayer;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

}
