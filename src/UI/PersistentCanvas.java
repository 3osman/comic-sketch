package UI;

import controller.DrawableItemController;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.util.ArrayList;
import models.DrawableItem;
import models.Panel;
import models.PathItem;

@SuppressWarnings("serial")
public class PersistentCanvas extends Component {

    private ArrayList<DrawableItem> items;
    private DrawableItemController dic;

    PersistentCanvas(DrawableItemController dicon) {
        items = new ArrayList<DrawableItem>();
        dic = dicon;
    }

    /**
     *
     * @param p
     * @return
     */
    public DrawableItem getItemAt(Point p) {

        DrawableItem item = null;

        for (DrawableItem i : items) {
            if (i instanceof Panel) {
                Point anchor = ((Panel) i).getKnobContainingPoint(p);

                if (dic.contains(i, p) || anchor != null) {
                    ((Panel) i).setAnchor(anchor);
                    item = i;
                    break;
                }
            }
        }
        return item;
    }

    /**
     *
     * @param item
     * @return
     */
    public DrawableItem addItem(DrawableItem item) {
        if (item == null) {
            return null;
        }
        items.add(item);

        if (item instanceof PathItem) {

            ((PathItem) item).getLayer().setActive(true);

        }
        repaint();
        return item;
    }

    /**
     *
     * @param item
     */
    public void removeItem(DrawableItem item) {
        if (item == null) {
            return;
        }
        if (item instanceof PathItem) {
            boolean active = false;
            for (PathItem pi : ((PathItem) item).getLayer().getDrawn()) {
                if (!pi.isHidden()) {
                    active = true;
                }
            }

            ((PathItem) item).getLayer().setActive(active);

        }
        items.remove(item);
        repaint();
    }

    /**
     *
     */
    public void clear() {
        items.clear();
        repaint();
    }

    /**
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
     *
     * @return
     */
    public ArrayList<DrawableItem> getItems() {
        return items;
    }

}
