/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import controller.DrawableItemController;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.util.ArrayList;

/**
 * Canvas for drawing thumbnails of layers
 * @author Osman
 */
@SuppressWarnings("serial")

public class ThumbNailCanvas extends Component {

    private ArrayList<Shape> items;
    private DrawableItemController dic;
   // private ArrayList<Rectangle> panels;

    /**
     * Constructor for the canvas
     *
     * @param dicon Controller for drawing objects
     */
    ThumbNailCanvas(DrawableItemController dicon) {
        items = new ArrayList<>();
        //panels =new ArrayList<>();
        dic = dicon;
    }

    /**
     * Adds item to canvas, and updates the canvas
     *
     * @param item item to add
     * @return the same item, null otherwise
     */
    public Shape addItem(Shape item) {
        if (item == null) {
            return null;
        }
        items.add(item);

        repaint();
        return item;
    }

    /**
     * Paints different drawable items on canvas
     *
     * @param graphics
     */
    public void paint(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setStroke(new BasicStroke(2));

        
        for (Shape gp : items) {
            g.setColor(Color.BLACK);
            g.draw(gp);
        }
       

    }

}
