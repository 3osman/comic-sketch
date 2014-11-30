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
    Panel p;
    Layer l;

    public ArrayList<GeneralPath> getLines() {
        return lines;
    }

    public void setLines() {
        lines = new ArrayList<>();
        for (PathItem pi : this.l.getDrawn()) {
            if (!pi.hidden) {
                lines.add((GeneralPath) ((GeneralPath) (pi.getShape())).clone());
            }
        }

    }

    public Panel getP() {
        return p;
    }

    public void setP(Panel p) {
        this.p = p;
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
    public Point getPanelAnchor() {
        return new Point(((Rectangle) (this.p.getShape())).x, ((Rectangle) (this.p.getShape())).y);
    }

    /**
     * Translates all drawings to 0,0
     */
    public void translateAll() {
        Point p = getPanelAnchor();
        AffineTransform at = new AffineTransform();
        at.translate(p.x * -1, p.y * -1);
        for (GeneralPath pp : this.lines) {
            pp.transform(at);
        }
    }

    /**
     * Resizes all drawings to fit
     */
    public void resizeAll() {
        double hfactor = 0.5;
        double wfactor = 0.5;
        try {
            hfactor = 150.0 / ((Rectangle) (this.p.getShape())).height;
            wfactor = 150.0 / ((Rectangle) (this.p.getShape())).width;
        } catch (Exception e) {
            hfactor = 0.5;
            wfactor = 0.5;
        }
        AffineTransform at = new AffineTransform();
        at.scale(wfactor, hfactor);
        for (GeneralPath pp : this.lines) {
            pp.transform(at);
        }

    }
}
