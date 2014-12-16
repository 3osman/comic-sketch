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
    Variables variables = new Variables();

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
            if (pa instanceof Panel && !((Panel) pa).deleted) {
                Point p = getPanelAnchor((Panel) pa);
                double hfactor = -(p.x - (((double) variables.THUMBNAIL_WIDTH * p.x) / (double) variables.CANVAS_WIDTH));
                double wfactor = -(p.y - (((double) variables.THUMBNAIL_HEIGHT * p.y) / (double) variables.CANVAS_HEIGHT));
                Rectangle newRect = (Rectangle) ((Rectangle) ((Panel) pa).getShape()).clone();
                newRect.translate((int) hfactor, ((int) wfactor));
                panels.add(newRect);
            } else if (l.isBlueLayer) {
                if (((PathItem) pa).getPanel() != null) {

                    Point p = getPanelAnchor(((PathItem) pa).getPanel());
                    double hfactor = -(p.x - (((double) variables.THUMBNAIL_WIDTH * p.x) / (double) variables.CANVAS_WIDTH));
                    double wfactor = -(p.y - (((double) variables.THUMBNAIL_HEIGHT * p.y) / (double) variables.CANVAS_HEIGHT));
                    AffineTransform at = new AffineTransform();
                    at.translate((int) Math.ceil(hfactor), (int) Math.ceil(wfactor));
                }
            }
        }
    }

    /**
     * Resizes all drawings to fit
     */
    public void resizeAll() {
        double hfactor = (double) variables.THUMBNAIL_HEIGHT / (double) variables.CANVAS_HEIGHT;
        double wfactor = (double) variables.THUMBNAIL_WIDTH / (double) variables.CANVAS_WIDTH;

        for (GeneralPath pi : lines) {
            AffineTransform at = new AffineTransform();
            at.scale(wfactor, hfactor);
            pi.transform(at);
        }
        for (Rectangle re : panels) {
            re.setSize((int) ((double) re.width * wfactor), (int) ((double) re.height * hfactor));
        }

    }
}
