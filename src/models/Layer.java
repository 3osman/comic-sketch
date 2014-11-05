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

    public Layer(Panel p) {
        this.parentPanel = p;
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
