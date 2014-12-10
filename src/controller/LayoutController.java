/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import UI.PersistentCanvas;
import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import models.DrawableItem;
import models.Layer;
import models.Panel;
import models.UndoableItem;
import models.Variables;

/**
 *
 * @author Osman
 */
public class LayoutController {

    public ArrayList<DrawableItem> setLayoutPanels(int row, int[] column, Layer mainLayer, PersistentCanvas c) {
        ArrayList<DrawableItem> allPanels = new ArrayList<>();
        int heightOfPanel = (Variables.CANVAS_HEIGHT / row) - 10;
        for (int i = 0; i < row; i++) {
            int widthOfPanel = (Variables.CANVAS_WIDTH / column[i]) - 5;
            for (int j = 0; j < column[i]; j++) {
                Point p = new Point(10 + (widthOfPanel + 10) * j, 10 + (heightOfPanel + 10) * i);
                Color f = new Color(255, 255, 255, 128);
                Panel item = new Panel(c, Color.BLACK, f, p, mainLayer);
                item.setInitialPoint(p);
                item.setInitialResizePoint(p);
                Rectangle thisRect = (Rectangle) (((Panel) item).getShape());
                thisRect.width = widthOfPanel;
                thisRect.height = heightOfPanel;
                item.update(new Point(widthOfPanel + p.x, heightOfPanel + p.y));
                allPanels.add(item);
            }
        }
        return allPanels;
    }
}
