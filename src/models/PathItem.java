/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import UI.PersistentCanvas;
import java.awt.Color;
import java.awt.Point;
import java.awt.geom.GeneralPath;

/**
 *
 * @author Osman
 */
public class PathItem extends DrawableItem {

    
    protected Layer layer;
    
    Point firstpoint;

    public PathItem(PersistentCanvas c, Color o, Color f, Point p, Layer l) {
        super(c, o, f);
        layer = l;
        type = 1;
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(p.x, p.y);
        firstpoint = p;
    }

    public PathItem(PathItem other) {
        super(other.canvas, other.outline, other.fill);
        //shape = new Rectangle((Rectangle) other.shape);
        isSelected = false;
        layer = other.layer;
        type = 1;
        firstpoint = other.firstpoint;
    }

    public Layer getLayer() {
        return layer;
    }

    public void setLayer(Layer layer) {
        this.layer = layer;
    }

    public DrawableItem duplicate() {
        return canvas.addItem(new PathItem(this));
    }

    public void update(Point p) {
        ((GeneralPath) shape).lineTo(p.x, p.y);
        canvas.repaint();
    }

    public void move(int dx, int dy) {
		//((GeneralPath) shape).x += dx;
        //((Rectangle) shape).y += dy;
        canvas.repaint();
    }

    
}
