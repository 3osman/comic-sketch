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

    ArrayList<DrawableItem> ditems;
    
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
    public Layer(boolean isB) {
       
        this.ditems = new ArrayList<>();
        this.isBlueLayer = isB;
        this.active = false;
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
        for (DrawableItem pi : this.ditems) {
            pi.move(x, y);
        }

    }

    /**
     * Add a new object to the layer
     *
     * @param pi Curve to be added
     */
    public void addPanelToLayer(Panel pi) {
        this.ditems.add(pi);
    }
    
    
    public void addItemToLayer(PathItem pi) {
        this.ditems.add(pi);
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

    public ArrayList<DrawableItem> getDrawable() {
        return ditems;
    }

    public void setDrawable(ArrayList<DrawableItem> drawn) {
        this.ditems = drawn;
    }

    
    
 

}
