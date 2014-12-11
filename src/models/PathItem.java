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
import lx.interaction.dollar.Dollar;
import lx.interaction.dollar.DollarListener;

/**
 * Class for sketching lines/curves
 *
 * @author Osman
 */
public class PathItem extends DrawableItem implements DollarListener {

    protected Panel panel;
    int thickness;
    Point firstpoint;
    boolean hidden;
    String gestureName;
    boolean hiddenWithPanel;
    boolean gestureOk;
    boolean isGesture;
    Dollar dollar = new Dollar(Dollar.GESTURES_DEFAULT);

    /**
     * Constructor
     *
     * @param c canvas in which the line lies
     * @param o Outline color
     * @param f Fill color
     * @param p Current end
     * @param l Layer it belongs to
     */
    public PathItem(PersistentCanvas c, Color o, Color f, Point p, Panel l, boolean gesture) {
        super(c, o, f);
        panel = l;
        type = 1;
        thickness = 2;
        shape = new GeneralPath();

        ((GeneralPath) shape).moveTo(p.x, p.y);
        if (l != null) {
            l.addToLines(this);
        }
        hidden = false;
        hiddenWithPanel = false;
        firstpoint = p;
        isGesture = gesture;
        if (isGesture) {
            dollar.setListener(this);
            dollar.setActive(true);
            dollar.pointerPressed(p.x, p.y);
        }
    }

    public PathItem(PathItem other) {
        super(other.canvas, other.outline, other.fill);
        //shape = new Rectangle((Rectangle) other.shape);
        isSelected = false;
        panel = other.panel;
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
        dollar.pointerDragged(p.x, p.y);
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

    public Panel getPanel() {
        return panel;
    }

    public void setPanel(Panel p) {
        this.panel = p;
    }

    public boolean isHiddenWithPanel() {
        return hiddenWithPanel;
    }

    public void setHiddenWithPanel(boolean hiddenWithPanel) {
        this.hiddenWithPanel = hiddenWithPanel;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public boolean isGesture() {
        return isGesture;
    }

    public void setIsGesture(boolean isGesture) {
        this.isGesture = isGesture;
    }

    public Dollar getDollar() {
        return dollar;
    }

    public void setDollar(Dollar dollar) {
        this.dollar = dollar;
    }

    public String getGestureName() {
        return gestureName;
    }

    public void setGestureName(String gestureName) {
        this.gestureName = gestureName;
    }

    public boolean isGestureOk() {
        return gestureOk;
    }

    public void setGestureOk(boolean gestureOk) {
        this.gestureOk = gestureOk;
    }

    public Point getFirstpoint() {
        return firstpoint;
    }

    public void setFirstpoint(Point firstpoint) {
        this.firstpoint = firstpoint;
    }

    @Override
    public void dollarDetected(Dollar dollar) {

        gestureName = dollar.getName();
       // System.out.println(gestureName);

        gestureOk = dollar.getScore() > 0.80;
    }

}
