/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import UI.PersistentCanvas;
import java.io.Serializable;
import java.util.ArrayList;
import models.Layer;
import models.Panel;
import models.PathItem;

/**
 * Layers Controller
 *
 * @author Osman
 */
public class LayersController implements Serializable {

    /**
     * add drawn item to layer
     *
     * @param l Layer
     * @param p Item to be added
     */
    public void addToPanels(Layer l, Panel p) {
        l.getDrawable().add(p);
    }
    /*
    
     public void bringToTop(Layer l, PersistentCanvas canvas) {
     for (PathItem pi : l.getDrawn()) {
     canvas.removeItem(pi);
     }

     for (PathItem pi : l.getDrawn()) {
     canvas.addItem(pi);
     }
     }*/

    /**
     * Toggles activity of a layer
     *
     * @param l Layer
     * @param active active or not
     */
    public void setActiveLayer(Layer l, boolean active, ArrayList<Layer> layers) {
        if (active) {
            for (Layer other : layers) {

                other.setActive(false);

            }
            l.setActive(true);
        } else {

            layers.get(1).setActive(true);

            l.setActive(false);
        }
    }

    /**
     * Gets the active layer in a panel
     *
     * @param l
     * @param p panel
     * @return active layer
     */
    public Layer getActiveLayer(ArrayList<Layer> l) {
        for (Layer layer : l) {

            if (layer.isActive()) {
                return layer;
            }

        }
        return null;
    }
}
