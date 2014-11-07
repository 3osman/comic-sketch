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

    public Panel(PersistentCanvas c, Color o, Color f, Point p) {
        super(c, o, f);
        type = 0;
        layers = new ArrayList<>();
        layers.add(new Layer(this, true));
        shape = new Rectangle(p.x, p.y, 0, 0);
        firstpoint = p;
    }

    public Panel(Panel other) {
        super(other.canvas, other.outline, other.fill);
        shape = new Rectangle((Rectangle) other.shape);
        type = 0;
        isSelected = false;
        layers = other.layers;
        firstpoint = other.firstpoint;
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
        canvas.repaint();
    }

    public void move(int dx, int dy) {
        ((Rectangle) shape).x += dx;
        ((Rectangle) shape).y += dy;
        canvas.repaint();
    }
}
