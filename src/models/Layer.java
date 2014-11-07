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

    public Layer(Panel p, boolean isB) {
        this.parentPanel = p;
        this.drawn = new ArrayList<>();
        this.isBlueLayer = isB;
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
