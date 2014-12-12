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
import java.io.Serializable;

/**
 * Drawable item is an abstract class that has all the basic drawing
 * functionalities, and any drawing class can extend it
 *
 * @author Osman
 */
public abstract class DrawableItem implements Serializable{

    protected PersistentCanvas canvas;
    protected Color outline, fill;
    protected Shape shape;
    protected Boolean isSelected;
    protected int type;

    /**
     * Constructor
     *
     * @param c Canvas in which it lies
     * @param o Outline color
     * @param f Fill color
     */
    public DrawableItem(PersistentCanvas c, Color o, Color f) {
        canvas = c;
        fill = f;
        outline = o;
        shape = null;
        isSelected = false;
    }

    /**
     * Duplicate item
     *
     * @return Duplicated item
     */
    public abstract DrawableItem duplicate();

    /**
     * Updates position of item
     *
     * @param p new position
     */
    public abstract void update(Point p);

    /**
     * Moves item by dx horizontally and dy vertically
     *
     * @param dx Delta x
     * @param dy Delta y
     */
    public abstract void move(int dx, int dy);
    
    //setters and getters
    //===============
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
