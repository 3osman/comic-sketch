/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import models.DrawableItem;
import models.Layer;
import models.Panel;

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
     * Gets closest anchor/distinctive point to a given point
     *
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

    public void allign(boolean horizontal, ArrayList<DrawableItem> allItems) {

        boolean isFirst = true;
        double value[] = {0, 0, 0, 0, 0, 0};
        for (DrawableItem di : allItems) {
            //check if it is only a customShapeView
            if (di instanceof Panel) {
                //set the variables for the first shape
                if (isFirst) {
                    value[0] = horizontal ? ((Rectangle) (((Panel) (di)).getShape())).y : ((Rectangle) (((Panel) (di)).getShape())).x;
                    value[2] = horizontal ? ((Rectangle) (((Panel) (di)).getShape())).y + ((Rectangle) (((Panel) (di)).getShape())).height : ((Rectangle) (((Panel) (di)).getShape())).x + ((Rectangle) (((Panel) (di)).getShape())).width;
                    value[1] = (value[0] + value[2]) / 2.0;
                    isFirst = false;
                } else {
                    //assign the value for the second shape
                    value[3] = horizontal ? ((Rectangle) (((Panel) (di)).getShape())).y : ((Rectangle) (((Panel) (di)).getShape())).x;
                    value[5] = horizontal ? ((Rectangle) (((Panel) (di)).getShape())).y + ((Rectangle) (((Panel) (di)).getShape())).height : ((Rectangle) (((Panel) (di)).getShape())).x + ((Rectangle) (((Panel) (di)).getShape())).width;
                    value[4] = (value[3] + value[5]) / 2.0;

                    int nearDifference = (int) (value[0] - value[5]);

                    for (int i = 0; i < 3; i++) {
                        for (int j = 3; j < 6; j++) {
                            if (Math.abs(value[i] - value[j]) < Math.abs(nearDifference)) {
                                nearDifference = (int) (value[i] - value[j]);
                            }
                        }
                    }
                    //animate the view

                    ((Panel) (di)).move(horizontal ? 0 : nearDifference, horizontal ? nearDifference : 0);
                        //la.moveLayer(horizontal ? 0 : nearDifference, horizontal ? nearDifference : 0);

                    //at the end update the values for all the y1 not in the first round
                    value[0] = value[3];
                    value[1] = value[4];
                    value[2] = value[5];
                }
            }
        }
    }

}
