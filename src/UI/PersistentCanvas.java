package UI;

import controller.DrawableItemController;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import models.DrawableItem;
import models.Gesture;
import models.Panel;
import models.PathItem;

/**
 * Persistent canvas class, holds all the drawn items
 *
 * @author Osman
 */
@SuppressWarnings("serial")
public class PersistentCanvas extends Component {

    private ArrayList<DrawableItem> items;
    private DrawableItemController dic;
    private Gesture gest;

    /**
     * Constructor for the canvas
     *
     * @param dicon Controller for drawing objects
     */
    PersistentCanvas(DrawableItemController dicon) {
        items = new ArrayList<>();
        gest = new Gesture();
        dic = dicon;
    }

    public Gesture getGest() {
        return gest;
    }

    public void setGest(Gesture gest) {
        this.gest = gest;
    }
    

    /**
     * Returns the item in which the point lies
     *
     * @param p the point
     * @return drawable item
     */
    public DrawableItem getItemAt(Point p) {

        DrawableItem item = null;

        for (DrawableItem i : items) {
            if (i instanceof Panel) {
               // int[] ind = {0};
                Point anchor = ((Panel) i).getKnobContainingPoint(p);

                if (dic.contains(i, p) || anchor != null) {
                    ((Panel) i).setAnchor(anchor);
                    //setAnchorForSelected(i, allSelected, ind);
                    item = i;
                    break;
                }
            }
        }
        return item;
    }

    public void setAnchorForSelected(DrawableItem di, ArrayList<DrawableItem> allSelected, int[] indexOfAnchor) {

        for (DrawableItem i : allSelected) {
            if (i instanceof Panel && !di.equals(i)) {

                Point anchor = ((Panel) i).getKnobFromIndex(indexOfAnchor[0]);

                ((Panel) i).setAnchor(anchor);

            }
        }
    }

    /**
     * Adds item to canvas, and updates the canvas
     *
     * @param item item to add
     * @return the same item, null otherwise
     */
    public DrawableItem addItem(DrawableItem item) {
        if (item == null) {
            return null;
        }
        items.add(item);

        /*    if (item instanceof PathItem) {

         ((PathItem) item).getLayer().setActive(true);

         }*/
        repaint();
        return item;
    }

    /**
     * Removes item from canvas, and updates it
     *
     * @param item item to be removed
     */
    public void removeItem(DrawableItem item) {
        if (item == null) {
            return;
        }
        if (item instanceof PathItem) {
            boolean active = false;
            if (((PathItem) item).getPanel() != null) {
                for (PathItem pi : ((PathItem) item).getPanel().getLines()) {
                    if (!pi.isHidden()) {
                        active = true;
                    }
                }
            }

            // ((PathItem) item).getLayer().setActive(active);
        }
        items.remove(item);
        repaint();
    }

    /**
     * Clears the canvas
     */
    public void clear() {
        items.clear();
        repaint();
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
        for (DrawableItem item : items) {
            if (item.getType() == 0) {
                dic.paint(item, g);
            } else {
                dic.paintPath(item, g);
            }
        }
        
    }

    /**
     * Returns all items in canvas
     *
     * @return Arraylist of canvas items
     */
    public ArrayList<DrawableItem> getItems() {
        return items;
    }

}
