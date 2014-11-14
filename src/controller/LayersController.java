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
 *
 * @author Osman
 */
public class LayersController {

    /**
     *
     * @param l
     * @param p
     */
    public void addToDrawn(Layer l, PathItem p) {
        l.getDrawn().add(p);
    }

    /**
     *
     * @param l
     * @param canvas
     */
    public void bringToTop(Layer l, PersistentCanvas canvas) {
        for (PathItem pi : l.getDrawn()) {
            canvas.removeItem(pi);
        }

        for (PathItem pi : l.getDrawn()) {
            canvas.addItem(pi);
        }
    }

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

    public Layer getActiveLayer(Panel p) {
        for (Layer layer : p.getLayers()) {

            if (layer.isActive()) {
                return layer;
            }

        }
        return null;
    }
}
