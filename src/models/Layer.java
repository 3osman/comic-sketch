/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Class to handle layers, note that layers are not drawable objects, so they
 * don't extend DrawableItem
 *
 * @author Osman
 */
public class Layer implements Serializable{

    ArrayList<PathItem> drawn;
    Panel parentPanel;
    boolean isBlueLayer;
    boolean active;
    boolean deleted;
    boolean hidden;

    /**
     * Constructor
     *
     * @param p panel that layer belongs to
     * @param isB Blue ink layer or not
     */
    public Layer(Panel p, boolean isB) {
        this.parentPanel = p;
        if (p != null && isB == false) {
            this.parentPanel.addToLayers(this);
        }
        this.drawn = new ArrayList<>();
        this.isBlueLayer = isB;
        this.active = true;
        this.hidden = false;
        this.deleted = false;
    }
/**
     * Moves the layer with all its contents
     *
     * @param x
     * @param y
     */
    public void moveLayer(int x, int y) {
        for (PathItem pi : this.drawn) {
            pi.move(x, y);
        }

    }

    /**
     * Add a new object to the layer
     *
     * @param pi Curve to be added
     */
    public void addObjectToLayer(PathItem pi) {
        this.drawn.add(pi);
    }
    //Setters and getters 
    //===================
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }
       public boolean isIsBlueLayer() {
        return isBlueLayer;
    }

    public void setIsBlueLayer(boolean isBlueLayer) {
        this.isBlueLayer = isBlueLayer;
    }

    public ArrayList<PathItem> getDrawn() {
        return drawn;
    }

    public void setDrawn(ArrayList<PathItem> drawn) {
        this.drawn = drawn;
    }

    public Panel getParentPanel() {
        return parentPanel;
    }

    public void setParentPanel(Panel parentPanel) {
        this.parentPanel = parentPanel;
    }

    
 

}
