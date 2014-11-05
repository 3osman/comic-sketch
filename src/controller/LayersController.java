/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import models.Layer;
import models.PathItem;

/**
 *
 * @author Osman
 */
public class LayersController {

    public void addToDrawn(Layer l, PathItem p) {
        l.getDrawn().add(p);
    }
}
