/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.Point;
import java.util.ArrayList;

/**
 * Controller for grouping and alignment, not finished
 *
 * @author Osman
 */
public class GroupingController {

    /**
     * Splits the canvas into 9 distinctive points and returns them
     *  
     * @param width width of canvas
     * @param height height of canvas
     * @return List of anchor/distinctive points
     */
    public ArrayList<Point> getDistinctivePoints(int width, int height) {

        int xstep = width / 3;
        int ystep = height / 3;
        ArrayList<Point> temp = new ArrayList<>();
        for (int i = 0; i < 3; i++) {

            for (int j = 0; j < 3; j++) {
                temp.add(new Point(xstep * j, ystep * i));
            }

        }
        return temp;
    }

    /**
     *  Gets closest anchor/distinctive point to a given point
     * @param p the point inspected
     * @param points All the anchor points
     * @return Closest anchor to inspected point
     */
    public Point getClosestAnchor(Point p, ArrayList<Point> points) {
        double smallest = Double.MAX_VALUE;
        Point index = new Point();
        ArrayList<Integer> finished = new ArrayList<>();
        for (int i = 0; i < points.size(); i++) {
            double diff = Math.sqrt(Math.pow((points.get(i).x - p.x), 2) + Math.pow((points.get(i).y - p.y), 2));
            if (diff < smallest) {
                index = points.get(i);
                smallest = diff;
                points.remove(i);
            }
        }
        return index;
    }

}
