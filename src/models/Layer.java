/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.util.ArrayList;

/**
 *
 * @author Osman
 */
public class Layer {

    ArrayList<PathItem> drawn;
    Panel parentPanel;
    boolean isBlueLayer;
    boolean active;
    public Layer(Panel p, boolean isB) {
        this.parentPanel = p;
        if (p != null && isB == false) {
            this.parentPanel.addToLayers(this);
        }
        this.drawn = new ArrayList<>();
        this.isBlueLayer = isB;
        this.active = true;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void moveLayer(int x, int y) {
        for (PathItem pi : this.drawn) {
            pi.move(x, y);
        }

    }

    public void addObjectToLayer(PathItem pi) {
        this.drawn.add(pi);
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
