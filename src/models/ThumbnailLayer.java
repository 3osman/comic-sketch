/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;

/**
 * Thumbnail layer to be displayed on side panel
 *
 * @author Osman
 */
public class ThumbnailLayer {

    ArrayList<GeneralPath> lines;
    ArrayList<Rectangle> panels;
    // Panel p;
    Layer l;

    public ArrayList<GeneralPath> getLines() {
        return lines;
    }

    public void setLines() {
        lines = new ArrayList<>();
        panels = new ArrayList<>();
        for (DrawableItem pi : this.l.getDrawable()) {
            if (pi instanceof PathItem && !((PathItem) pi).hidden) {
                lines.add((GeneralPath) ((GeneralPath) (pi.getShape())).clone());
            }
        }

    }

    public ArrayList<Rectangle> getPanels() {
        return panels;
    }

    public void setPanels(ArrayList<Rectangle> panels) {
        this.panels = panels;
    }

    public Layer getL() {
        return l;
    }

    public void setL(Layer l) {
        this.l = l;
    }

    /**
     * Gets anchor for original panel
     *
     * @return Point of anchor
     */
    public Point getPanelAnchor(Panel p) {
        return new Point(((Rectangle) (p.getShape())).x, ((Rectangle) (p.getShape())).y);
    }

    /**
     * Translates all drawings to 0,0
     */
    public void translateAll() {

        for (DrawableItem pa : l.getDrawable()) {
            if (pa instanceof Panel) {
                Point p = getPanelAnchor((Panel) pa);
                double hfactor = (p.x - ((Variables.THUMBNAIL_WIDTH * p.x) / Variables.CANVAS_WIDTH)) * -1;
                double wfactor = (p.y - ((Variables.THUMBNAIL_HEIGHT * p.y) / Variables.CANVAS_HEIGHT)) * -1;
                Rectangle newRect = (Rectangle) ((Rectangle) ((Panel) pa).getShape()).clone();
                newRect.translate((int) hfactor, (int) wfactor);
                panels.add(newRect);
                for (GeneralPath pp : this.lines) {
                  //  hfactor = (pp.getCurrentPoint().getX() - ((150 * pp.getCurrentPoint().getX()) / Variables.CANVAS_WIDTH)) * -1;
                    // wfactor = (pp.getCurrentPoint().getY() - ((150 * pp.getCurrentPoint().getY()) / Variables.CANVAS_HEIGHT)) * -1;
                    AffineTransform at = new AffineTransform();
                    at.translate(Variables.THUMBNAIL_WIDTH * pp.getCurrentPoint().getX() / Variables.CANVAS_WIDTH, Variables.THUMBNAIL_HEIGHT * pp.getCurrentPoint().getY() / Variables.CANVAS_HEIGHT);
                    pp.transform(at);
                }

            } else if (l.isBlueLayer) {
                if (((PathItem) pa).getPanel() != null) {

                    Point p = getPanelAnchor(((PathItem) pa).getPanel());
                    double hfactor = (p.x - ((Variables.THUMBNAIL_WIDTH * p.x) / Variables.CANVAS_WIDTH)) * -1;
                    double wfactor = (p.y - ((Variables.THUMBNAIL_HEIGHT * p.y) / Variables.CANVAS_HEIGHT)) * -1;
                    AffineTransform at = new AffineTransform();
                    at.translate((int) hfactor, (int) wfactor);
                    for (GeneralPath pp : this.lines) {
                        // pp.getCurrentPoint();
                        pp.transform(at);
                    }
                    // Rectangle newRect = (Rectangle) ((Rectangle) ((Panel) pa).getShape()).clone();
                    //  newRect.translate(p.x * -1, p.y * -1);
                    // panels.add(newRect);
                }
            }
        }
    }

    /**
     * Resizes all drawings to fit
     */
    public void resizeAll() {
        double hfactor = 0.5;
        double wfactor = 0.5;
        try {
            hfactor = Variables.THUMBNAIL_HEIGHT / Variables.CANVAS_HEIGHT;
            wfactor = Variables.THUMBNAIL_WIDTH / Variables.CANVAS_WIDTH;
        } catch (Exception e) {
            hfactor = 0.5;
            wfactor = 0.5;
        }
        for (GeneralPath pi : lines) {

            AffineTransform at = new AffineTransform();
            at.scale(wfactor, hfactor);

            pi.transform(at);

        }
        for (Rectangle re : panels) {

            re.resize((int) ((double) re.width * wfactor), (int) ((double) re.height * hfactor));

        }
    }
}
