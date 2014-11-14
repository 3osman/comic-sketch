/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.Point;
import java.util.ArrayList;

/**
 *
 * @author Osman
 */
public class GroupingController {
/**
 * 
 * @param width
 * @param height
 * @return 
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
 * 
 * @param p
 * @param points
 * @param width
 * @param height
 * @return 
 */
    public Point getClosestAnchor(Point p, ArrayList<Point> points, int width, int height) {
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
