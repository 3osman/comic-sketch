/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.awt.Dimension;
import java.awt.Toolkit;

/**
 * Class for final values
 *
 * @author Osman
 */
public class Variables {

    public int WINDOW_HEIGHT;
    public int WINDOW_WIDTH;
    public int CANVAS_WIDTH;
    public int CANVAS_HEIGHT;
    public int THUMBNAIL_HEIGHT;
    public int THUMBNAIL_WIDTH;

    public Variables() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        WINDOW_HEIGHT = screenSize.height;
        WINDOW_WIDTH = screenSize.width;

        CANVAS_HEIGHT = WINDOW_HEIGHT * 4/5 ;
        CANVAS_WIDTH = WINDOW_WIDTH *4/5;
        
        THUMBNAIL_HEIGHT = WINDOW_HEIGHT * 1/5;
        THUMBNAIL_WIDTH = CANVAS_WIDTH * 1/5;
    }

    

    public static final String EXTENSION = "comico";
    public final static int PANEL_5_HEIGHT = 2;
    public final static int PANEL_5_WIDTH = 2;
    public final static int PANEL_4_HEIGHT = 2;
    public final static int PANEL_4_WIDTH = 2;
    public final static int LABEL_HEIGHT = 1;
    public final static int LABEL_WIDTH = 2;
    public final static int BUTTON_WIDHT = 2;
    public final static int BUTTON_HEIGHT = 1;
    public final static int SEPARATOR_HEIGTH = 1;
    public final static int SEPARATOR_WIDTH = 2;

}
