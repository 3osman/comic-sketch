/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import UI.PersistentCanvas;
import java.awt.Color;
import java.awt.Point;
import java.awt.Shape;

/**
 *
 * @author Osman
 */
public abstract class DrawableItem {

    protected PersistentCanvas canvas;
    protected Color outline, fill;
    protected Shape shape;
    protected Boolean isSelected;

    public DrawableItem(PersistentCanvas c, Color o, Color f) {
        canvas = c;
        fill = f;
        outline = o;
        shape = null;
        isSelected = false;
    }

   
    public abstract DrawableItem duplicate();

    public abstract void update(Point p);

    public abstract void move(int dx, int dy);

    public PersistentCanvas getCanvas() {
        return canvas;
    }

    public void setCanvas(PersistentCanvas canvas) {
        this.canvas = canvas;
    }

    public Color getOutline() {
        return outline;
    }

    public void setOutline(Color outline) {
        this.outline = outline;
    }

    public Color getFill() {
        return fill;
    }

    public void setFill(Color fill) {
        this.fill = fill;
    }

    public Shape getShape() {
        return shape;
    }

    public void setShape(Shape shape) {
        this.shape = shape;
    }

    public Boolean getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(Boolean isSelected) {
        this.isSelected = isSelected;
    }

    
    

}
