/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
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
     * Aligns panels
     * @param horizontal Horizontal or vertical
     * @param allItems All panels to align 
     */
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
                    // for (int i = 0; i < nearDifference; i = i + 2) {
                    ((Panel) (di)).move(horizontal ? 0 : nearDifference, horizontal ? nearDifference : 0);
                       // Thread.sleep(100);

                   // }
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
