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
 *
 * @author Osman
 */
public class PathItem extends DrawableItem {

    protected Layer layer;
    int thickness;
    Point firstpoint;
    boolean hidden;
    boolean hiddenWithLayer;

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

    /**
     *
     * @return
     */
    public DrawableItem duplicate() {
        return canvas.addItem(new PathItem(this));
    }

    /**
     *
     * @param p
     */
    public void update(Point p) {
        ((GeneralPath) shape).lineTo(p.x, p.y);
        canvas.repaint();
    }

    /**
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

}
