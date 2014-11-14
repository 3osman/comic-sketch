/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.awt.Point;

/**
 * Class for setting an undoable item
 * @author Osman
 */
public class UndoableItem {

    private DrawableItem ditem;
    private Layer layer;
    private boolean isLayer;
    private int x;
    private int y;
    private int actionType; //0 add, 1 delete, 2 move, 3 resize
    private Point initialP;
    /**
     * Constructor
     * @param di the drawable item
     * @param at Action type: 0 add, 1 delete, 2 move, 3 resize
     */
    public UndoableItem(DrawableItem di, int at) {
        this.ditem = di;
        this.isLayer = false;
        actionType = at;
    }

    public UndoableItem(Layer l, int at) {
        this.layer = l;
        this.isLayer = true;
        actionType = at;

    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Point getInitialP() {
        return initialP;
    }

    public void setInitialP(Point initialP) {
        this.initialP = initialP;
    }

    public int getActionType() {
        return actionType;
    }

    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    public DrawableItem getDitem() {
        return ditem;
    }

    public void setDitem(DrawableItem ditem) {
        this.ditem = ditem;
    }

    public Layer getLayer() {
        return layer;
    }

    public void setLayer(Layer layer) {
        this.layer = layer;
    }

    public boolean isLayer() {
        return isLayer;
    }

    public void setIsLayer(boolean isLayer) {
        this.isLayer = isLayer;
    }

}
