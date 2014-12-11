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
 * Panel object
 *
 * @author Osman
 */
public class Panel extends DrawableItem {

    Point firstpoint;
    ArrayList<PathItem> lines;
    Layer parentLayer;

    private Point initialPoint;
    private Point initialResizePoint;
    private int initialWidth;
    private int initialHeight;
    Rectangle firstCorner;
    Rectangle secondCorner;
    //LayersController lc = new LayersController();
    Rectangle thirdCorner;
    Rectangle fourthCorner;
    Point anchor;
    boolean deleted;
    boolean hiddenWithLayer;

    /**
     * Constructor
     *
     * @param c Canvas it belongs to
     * @param o Outline color
     * @param f Fill color
     * @param p Starting point
     */
    public Panel(PersistentCanvas c, Color o, Color f, Point p, Layer parentL) {
        super(c, o, f);
        type = 0;
        lines = new ArrayList<>();
        //layers.add(new Layer(this, true));
        if (parentL != null) {
            parentL.addPanelToLayer(this);
            parentLayer = parentL;
        }
        // lc.setActiveLayer(l, true);

        shape = new Rectangle(p.x, p.y, 0, 0);
        firstpoint = p;
        deleted = false;
        hiddenWithLayer = false;
        updateCornerRects();
    }

    public Panel(Panel other) {
        super(other.canvas, other.outline, other.fill);
        shape = new Rectangle((Rectangle) other.shape);
        type = 0;
        isSelected = false;
        lines = other.lines;
        parentLayer = other.parentLayer;
        firstpoint = other.firstpoint;
    }

    /**
     * Duplicates the item
     *
     * @return the duplicated item
     */
    public DrawableItem duplicate() {
        return canvas.addItem(new Panel(this));
    }

    /**
     * Updates the panel
     *
     * @param p point to be added
     */
    public void update(Point p) {
        ((Rectangle) shape).setFrameFromDiagonal(firstpoint, p);
        this.initialHeight = ((Rectangle) shape).height;
        this.initialWidth = ((Rectangle) shape).width;
        this.initialPoint = new Point(((Rectangle) shape).x, ((Rectangle) shape).y);
        updateCornerRects();
        canvas.repaint();
    }

    /**
     * Moves the panel
     *
     * @param dx Horizontal difference
     * @param dy Vertical difference
     */
    public void move(int dx, int dy) {
        ((Rectangle) shape).x += dx;
        ((Rectangle) shape).y += dy;
        for (PathItem pi : this.getLines()) {
            pi.move(dx, dy);
        }
        updateCornerRects();
        canvas.repaint();
    }

    /**
     * Moves the anchor point of the rectangle and updates width/height
     *
     * @param x
     * @param y
     * @param width
     * @param height
     */
    public void moveAnchor(int x, int y, int width, int height) {
        ((Rectangle) shape).x = x;
        ((Rectangle) shape).y = y;
        ((Rectangle) shape).height += height;
        ((Rectangle) shape).width += width;
        updateCornerRects();
        canvas.repaint();

    }

    /**
     * Moves the anchor point of the rectangle and updates width/height
     *
     * @param x
     * @param y
     */
    public void moveA(int x, int y) {
        ((Rectangle) shape).x = x;
        ((Rectangle) shape).y = y;
        updateCornerRects();
        canvas.repaint();

    }

    /**
     * Resizes the panel by dx and dy
     *
     * @param dx
     * @param dy
     */
    public void resize(int dx, int dy) {
        ((Rectangle) shape).height += dy;
        ((Rectangle) shape).width += dx;
        updateCornerRects();
        canvas.repaint();
    }

    /**
     * Resizes and changes anchor, for resize by dragging
     *
     * @param anchor new anchor for rectangle
     * @param end ed of rectangle
     */
    public void resize(Point anchor, Point end) {
        (shape) = new Rectangle(anchor);
        ((Rectangle) shape).add(end);		// creates smallest rectange which includes both anchor & end
        updateCornerRects();
        canvas.repaint();
    }

    /**
     * Gets which knob you are dragging during resize
     *
     * @param pt Point clicked
     * @return point of Knob
     */
    public Point getKnobContainingPoint(Point pt) {

        if (firstCorner.contains(pt)) {
           // index[0] = 0;
            return new Point(((Rectangle) shape).x + ((Rectangle) shape).width, ((Rectangle) shape).y + ((Rectangle) shape).height);

        } else if (secondCorner.contains(pt)) {
           // index[0] = 1;
            return new Point(((Rectangle) shape).x, ((Rectangle) shape).y + ((Rectangle) shape).height);
        } else if (thirdCorner.contains(pt)) {
           // index[0] = 2;
            return new Point(((Rectangle) shape).x, ((Rectangle) shape).y);

        } else if (fourthCorner.contains(pt)) {
          //  index[0] = 3;
            return new Point(((Rectangle) shape).x + ((Rectangle) shape).width, ((Rectangle) shape).y);
        }
        return null;

    }

    public Point getKnobFromIndex(int index) {

        switch (index) {
            case 0:
                return new Point(((Rectangle) shape).x + ((Rectangle) shape).width, ((Rectangle) shape).y + ((Rectangle) shape).height);

            case 1:
                return new Point(((Rectangle) shape).x, ((Rectangle) shape).y + ((Rectangle) shape).height);

            case 2:
                return new Point(((Rectangle) shape).x, ((Rectangle) shape).y);

            case 3:
                return new Point(((Rectangle) shape).x + ((Rectangle) shape).width, ((Rectangle) shape).y);

            default:
                return null;

        }

    }

    /**
     * Updates corner knobs on every action (move/add/delete)
     */
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

    //Setters and getters
    //=====================
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

    public ArrayList<PathItem> getLines() {
        return lines;
    }

    public void setLines(ArrayList<PathItem> lines) {
        this.lines = lines;
    }

    public void addToLines(PathItem l) {
        lines.add(l);
    }

    public Layer getParentLayer() {
        return parentLayer;
    }

    public void setParentLayer(Layer parentLayer) {
        this.parentLayer = parentLayer;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public boolean isHiddenWithLayer() {
        return hiddenWithLayer;
    }

    public void setHiddenWithLayer(boolean hiddenWithLayer) {
        this.hiddenWithLayer = hiddenWithLayer;
    }

}
