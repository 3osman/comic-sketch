/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import UI.PersistentCanvas;
import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.util.ArrayList;

/**
 *
 * @author Osman
 */
public class Panel extends DrawableItem {

    Point firstpoint;
    ArrayList<Layer> layers;
    private Point initialPoint;
    private Point initialResizePoint;
    private int initialWidth;
    private int initialHeight;
    Rectangle firstCorner;
    Rectangle secondCorner;

    Rectangle thirdCorner;

    Rectangle fourthCorner;
    Point anchor;

    public Panel(PersistentCanvas c, Color o, Color f, Point p) {
        super(c, o, f);
        type = 0;
        layers = new ArrayList<>();
        layers.add(new Layer(this, true));
        new Layer(this, false);
        new Layer(this, false);
        shape = new Rectangle(p.x, p.y, 0, 0);
        firstpoint = p;
        updateCornerRects();
    }

    public Panel(Panel other) {
        super(other.canvas, other.outline, other.fill);
        shape = new Rectangle((Rectangle) other.shape);
        type = 0;
        isSelected = false;
        layers = other.layers;
        firstpoint = other.firstpoint;
    }

    public Rectangle getFirstCorner() {
        return firstCorner;
    }

    public Point getAnchor() {
        return anchor;
    }

    public void setAnchor(Point anchor) {
        this.anchor = anchor;
    }

    public void setFirstCorner(Rectangle firstCorner) {
        this.firstCorner = firstCorner;
    }

    public Rectangle getSecondCorner() {
        return secondCorner;
    }

    public void setSecondCorner(Rectangle secondCorner) {
        this.secondCorner = secondCorner;
    }

    public Rectangle getThirdCorner() {
        return thirdCorner;
    }

    public void setThirdCorner(Rectangle thirdCorner) {
        this.thirdCorner = thirdCorner;
    }

    public Rectangle getFourthCorner() {
        return fourthCorner;
    }

    public void setFourthCorner(Rectangle fourthCorner) {
        this.fourthCorner = fourthCorner;
    }

    public int getInitialWidth() {
        return initialWidth;
    }

    public void setInitialWidth(int initialWidth) {
        this.initialWidth = initialWidth;
    }

    public int getInitialHeight() {
        return initialHeight;
    }

    public void setInitialHeight(int initialHeight) {
        this.initialHeight = initialHeight;
    }

    public Point getInitialPoint() {
        return initialPoint;
    }

    public Point getInitialResizePoint() {
        return initialResizePoint;
    }

    public void setInitialResizePoint(Point initialResizePoint) {
        this.initialResizePoint = initialResizePoint;
    }

    public void setInitialPoint(Point initialPoint) {
        this.initialPoint = initialPoint;
    }

    public Point getFirstpoint() {
        return firstpoint;
    }

    public ArrayList<Layer> getLayers() {
        return layers;
    }

    public void setLayers(ArrayList<Layer> layers) {
        this.layers = layers;
    }

    public void addToLayers(Layer l) {
        layers.add(l);
    }

    public DrawableItem duplicate() {
        return canvas.addItem(new Panel(this));
    }

    public void update(Point p) {
        ((Rectangle) shape).setFrameFromDiagonal(firstpoint, p);
        this.initialHeight = ((Rectangle) shape).height;
        this.initialWidth = ((Rectangle) shape).width;
        this.initialPoint = new Point(((Rectangle) shape).x, ((Rectangle) shape).y);
        updateCornerRects();
        canvas.repaint();
    }

    public void move(int dx, int dy) {
        ((Rectangle) shape).x += dx;
        ((Rectangle) shape).y += dy;

        updateCornerRects();
        canvas.repaint();
    }

    public void moveAnchor(int x, int y) {
        ((Rectangle) shape).x = x;
        ((Rectangle) shape).y = y;

        updateCornerRects();
        canvas.repaint();

    }

    public void resize(int dx, int dy) {
        ((Rectangle) shape).height += dy;
        ((Rectangle) shape).width += dx;
        updateCornerRects();
        canvas.repaint();
    }

    public void resize(Point anchor, Point end) {
        (shape) = new Rectangle(anchor);
        ((Rectangle) shape).add(end);		// creates smallest rectange which includes both anchor & end
        updateCornerRects();
        canvas.repaint();
    }

    public Point getKnobContainingPoint(Point pt) {

        if (firstCorner.contains(pt)) {
            return new Point(((Rectangle) shape).x + ((Rectangle) shape).width, ((Rectangle) shape).y + ((Rectangle) shape).height);

        } else if (secondCorner.contains(pt)) {
            return new Point(((Rectangle) shape).x, ((Rectangle) shape).y + ((Rectangle) shape).height);
        } else if (thirdCorner.contains(pt)) {
            return new Point(((Rectangle) shape).x, ((Rectangle) shape).y);

        } else if (fourthCorner.contains(pt)) {
            return new Point(((Rectangle) shape).x + ((Rectangle) shape).width, ((Rectangle) shape).y);
        }
        return null;

    }

    public void updateCornerRects() {
        int width = 7;
        int height = 7;
        Point fi = new Point(((Rectangle) getShape()).x - 5, ((Rectangle) getShape()).y - 5);
        Point sec = new Point(((Rectangle) getShape()).x + ((Rectangle) getShape()).width, ((Rectangle) getShape()).y - 5);
        Point thi = new Point(((Rectangle) getShape()).x + ((Rectangle) getShape()).width, ((Rectangle) getShape()).y + ((Rectangle) getShape()).height);
        Point four = new Point(((Rectangle) getShape()).x - 5, ((Rectangle) getShape()).y + ((Rectangle) getShape()).height);
        firstCorner = (new Rectangle(fi.x, fi.y, width, height));
        secondCorner = (new Rectangle(sec.x, sec.y, width, height));
        thirdCorner = (new Rectangle(thi.x, thi.y, width, height));
        fourthCorner = (new Rectangle(four.x, four.y, width, height));
    }
}
