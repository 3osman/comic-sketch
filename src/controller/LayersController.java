/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import UI.PersistentCanvas;
import models.Layer;
import models.Panel;
import models.PathItem;

/**
 * Layers Controller
 *
 * @author Osman
 */
public class LayersController {

    /**
     * add drawn item to layer
     *
     * @param l Layer
     * @param p Item to be added
     */
    public void addToDrawn(Layer l, PathItem p) {
        l.getDrawn().add(p);
    }

    /**
     * Brings layer to top
     *
     * @param l layer
     * @param canvas canvas in which the layer is situated
     */
    public void bringToTop(Layer l, PersistentCanvas canvas) {
        for (PathItem pi : l.getDrawn()) {
            canvas.removeItem(pi);
        }

        for (PathItem pi : l.getDrawn()) {
            canvas.addItem(pi);
        }
    }

    /**
     * Toggles activity of a layer
     *
     * @param l Layer
     * @param active active or not
     */
    public void setActiveLayer(Layer l, boolean active) {
        if (active) {
            for (Layer other : l.getParentPanel().getLayers()) {

                other.setActive(false);

            }
            l.setActive(true);
        } else {

            l.getParentPanel().getLayers().get(1).setActive(true);

            l.setActive(false);
        }
    }

    /**
     * Gets the active layer in a panel
     *
     * @param p panel
     * @return active layer
     */
    public Layer getActiveLayer(Panel p) {
        for (Layer layer : p.getLayers()) {

            if (layer.isActive()) {
                return layer;
            }

        }
        return null;
    }
}
