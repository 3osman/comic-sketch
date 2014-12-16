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
import models.Variables;

/**
 * Layout controller
 *
 * @author Osman
 */
public class LayoutController {
        Variables variables = new Variables();

    /**
     * Sets layout for predefined templates
     *
     * @param row number of rows
     * @param column number of columns per each row
     * @param mainLayer Active layer
     * @param c Canvas to draw on
     * @return ArrayList of panels
     */
    public ArrayList<DrawableItem> setLayoutPanels(int row, int[] column, Layer mainLayer, PersistentCanvas c) {
        ArrayList<DrawableItem> allPanels = new ArrayList<>();
        int heightOfPanel = (variables.CANVAS_HEIGHT / row) - 10;
        for (int i = 0; i < row; i++) {
            int widthOfPanel = ((variables.CANVAS_WIDTH - 50) / column[i]) - 5;
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

    /**
     * Sets layout for predefined templates
     *
     * @param row number of rows per each row
     * @param column number of columns
     * @param mainLayer Active layer
     * @param c Canvas to draw on
     * @return Arraylist of panels to add to canvas
     */
    public ArrayList<DrawableItem> setLayoutVerticalPanels(int[] row, int column, Layer mainLayer, PersistentCanvas c) {
        ArrayList<DrawableItem> allPanels = new ArrayList<>();
        int widthOfPanel = ((variables.CANVAS_WIDTH - 50) / column) - 10;
        for (int i = 0; i < column; i++) {
            int heightOfPanel = (variables.CANVAS_HEIGHT  / row[i]) - 5;
            for (int j = 0; j < row[i]; j++) {
                Point p = new Point(10 + (widthOfPanel + 10) * i, 10 + (heightOfPanel + 10) * j);
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
