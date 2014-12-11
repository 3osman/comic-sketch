package UI;

import controller.DrawableItemController;
import controller.GroupingController;
import controller.LayersController;
import controller.LayoutController;
import controller.SavingController;
import controller.UndoController;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.BoxLayout;
import javax.swing.BorderFactory;
import java.awt.Container;
import java.awt.Point;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;
import models.DrawableItem;
import models.Gesture;
import models.Layer;
import models.Panel;
import models.PathItem;
import models.ThumbnailLayer;
import models.UndoableItem;
import models.Variables;

/**
 * Editor for the graphical interface
 *
 * @author Osman
 */
@SuppressWarnings("serial")
public class GraphicalEditor extends JFrame {

    //Global variables definition
    private JPanel panel2;
    private JDialog jd;
    //buttons
    private JButton panelStyleButton;
    private JButton addLayer;
    private JButton mergeLayer;
    private JToggleButton eraser;
    private JButton clearButton;
    private JButton undoButton;
    private JButton addPanel;
    private JToggleButton oneButton;
    private JToggleButton twoButton;
    private JToggleButton threeButton;
    private JToggleButton fourButton;
    private JButton redoButton;
    private JButton styleButton;

    private JButton saveButton;
    private JButton loadButton;
    private JScrollPane scroller;
    private ArrayList<Layer> allLayers;
    private Container pane;//main container
    private Color o; //color
    private Color panelColor; //panel color
    private Point mousepos; // Stores the previous mouse position
    //controllers declaration
    DrawableItemController dic = new DrawableItemController(); //controls all drawing
    UndoController udc = new UndoController(); //undo controller
    GroupingController gc = new GroupingController(); //grouping, not finished
    LayersController lc = new LayersController(); //layers controller
    SavingController sc = new SavingController();
    LayoutController layc = new LayoutController();
    private JToggleButton jcb; //checkbox for blue ink
    private boolean isMoving; //is currently moving
    private String mode;  // Mode of interaction
    private Boolean isBlue; //blue ink
    private Boolean isWhite; //eraser
    private ArrayList<Point> anchorP; //acnhor points for aligining panels
    private PersistentCanvas canvas; // Stores the created items
    private int thickness; //thickness of drawing
    private ArrayList<DrawableItem> selectionAll; 	 // Stores the selected item
    private DrawableItem selection;
    private Gesture gesture;
    private Layer globalLayer; //global layer without panel
    private Layer whiteLayer; // white layer
    private Layer blueInkLayer; //blue ink layer

    private Layer activeLayer;

    private JPanel canvasOpsPanel;
    private JPanel rightPanel;

    // Constructor of the Graphical Editor
    public GraphicalEditor(int width, int height) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
        setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);

        pane = getContentPane();

        anchorP = gc.getDistinctivePoints(width, height);
        selectionAll = new ArrayList<>();
        selection = null;
        gesture = null;
        isMoving = false;
        thickness = 2;
        o = Color.BLACK;

        allLayers = new ArrayList<>();

        scroller = new JScrollPane();
        pane.setLayout(new BoxLayout(pane, BoxLayout.LINE_AXIS));

        mode = "Path";

        oneButton = new JToggleButton();

        oneButton.setSelected(
                true);
        twoButton = new JToggleButton();
        threeButton = new JToggleButton();
        fourButton = new JToggleButton();

        undoButton = new JButton();
        addPanel = new JButton();
        AbstractAction addPanelAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createNewPanel(new Point(10, 10));
            }
        };
        AbstractAction oneAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                thickness = 2;
                twoButton.setSelected(false);
                threeButton.setSelected(false);
                fourButton.setSelected(false);
                oneButton.setSelected(true);
            }
        };

        AbstractAction twoAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                thickness = 6;
                oneButton.setSelected(false);
                threeButton.setSelected(false);
                fourButton.setSelected(false);
                twoButton.setSelected(true);
            }
        };
        AbstractAction threeAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                thickness = 10;
                oneButton.setSelected(false);
                twoButton.setSelected(false);
                fourButton.setSelected(false);
                threeButton.setSelected(true);
            }
        };
        AbstractAction fourAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                thickness = 14;
                twoButton.setSelected(false);
                threeButton.setSelected(false);
                oneButton.setSelected(false);
                fourButton.setSelected(true);
            }
        };

        oneButton.setAction(oneAction);

        twoButton.setAction(twoAction);

        threeButton.setAction(threeAction);

        fourButton.setAction(fourAction);

        addPanel.setAction(addPanelAction);

        setToggleButtonImage("/1s.gif", oneButton);
        setToggleButtonImage("/2s.gif", twoButton);
        setToggleButtonImage("/3s.gif", threeButton);
        setToggleButtonImage("/4s.gif", fourButton);
        setButtonImage("/addPanel.png", addPanel);
        setButtonImage("/save-icon.png", saveButton);
        setButtonImage("/load.png", loadButton);

        AbstractAction undoAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {

                udc.undoProcess(canvas);
                resetLayerPanel();

            }
        };

        undoButton.setAction(undoAction);
        setButtonImage("/Undo-icon.png", undoButton);
        undoButton.setToolTipText("Undo");

        redoButton = new JButton();

        AbstractAction redoAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                udc.redoProcess(canvas);
                resetLayerPanel();
            }
        };

        redoButton.setAction(redoAction);
        setButtonImage("/Redo-icon.png", redoButton);
        redoButton.setToolTipText("Redo");

        styleButton = new JButton();
        AbstractAction styleAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                o = JColorChooser.showDialog(
                        canvas,
                        "Choose Color",
                        o);

            }

        };

        styleButton.setAction(styleAction);

        setButtonImage("/color.png", styleButton);
        styleButton.setToolTipText("Color");

        panelStyleButton = new JButton();
        AbstractAction panelStyleAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panelColor();

            }

        };

        panelStyleButton.setAction(panelStyleAction);

        setButtonImage("/color.png", panelStyleButton);
        panelStyleButton.setToolTipText("Panel Color");
        clearButton = new JButton();

        AbstractAction clearAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearAll();
                resetLayerPanel();
            }
        };

        clearButton.setAction(clearAction);

        setButtonImage("/clear.png", clearButton);
        clearButton.setToolTipText("Clear All");

        JPanel canvasPanel = new JPanel();
        jcb = new JToggleButton();

        jcb.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent ev) {
                if (ev.getStateChange() == ItemEvent.SELECTED) {
                    isBlue = true;
                    //if (selection != null) {
                    lc.setActiveLayer(blueInkLayer, true, allLayers);
                    resetLayerPanel();
                    select(null, false);
                    // resetLayerPanel((Panel) selection);
                    //}
                    if (isWhite) {
                        isWhite = false;
                        eraser.setSelected(false);
                        //canvas.setCursor(dic.createBlueCursor());
                    }
                    canvas.setCursor(dic.createBlueCursor());

                } else if (ev.getStateChange() == ItemEvent.DESELECTED) {
                    isBlue = false;
                    //if (selection != null) {
                    lc.setActiveLayer(globalLayer, true, allLayers);
                    resetLayerPanel();
                    select(null, false);
                    canvas.setCursor(dic.createPathCursor());

                    //canvas.setCursor(Cursor.getDefaultCursor());
                    //}
                }
            }
        }
        );
        setToggleButtonImage("/blue.png", jcb);
        jcb.setToolTipText("Blue Ink");

        eraser = new JToggleButton();
        eraser.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent ev) {
                if (ev.getStateChange() == ItemEvent.SELECTED) {
                    isWhite = true;
                    twoButton.setEnabled(false);
                    threeButton.setEnabled(false);
                    oneButton.setEnabled(false);
                    fourButton.setEnabled(false);
                    try {

                        Cursor custom = dic.createEraserCursor();
                        canvas.setCursor(custom);
                    } catch (Exception e) {
                    }
                    if (selection != null) {
                        lc.setActiveLayer(whiteLayer, true, allLayers);
                        select(null, false);
                    }
                    if (isBlue) {
                        isBlue = false;
                        jcb.setSelected(false);
                    }
                } else if (ev.getStateChange() == ItemEvent.DESELECTED) {
                    isWhite = false;
                    twoButton.setEnabled(true);
                    threeButton.setEnabled(true);
                    oneButton.setEnabled(true);
                    fourButton.setEnabled(true);
                    if (selection != null) {
                        lc.setActiveLayer(globalLayer, true, allLayers);
                        select(null, false);
                    }
                    canvas.setCursor(dic.createPathCursor());
                }
            }
        });
        setToggleButtonImage("/eraser.png", eraser);
        eraser.setToolTipText("Eraser");

        // Canvas Panel starts here
        //==========================================
        canvasPanel.setLayout(new BoxLayout(canvasPanel, BoxLayout.X_AXIS));
        canvasPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        //canvasPanel.setPreferredSize(new Dimension((5 * width) / 6, height));

        canvasOpsPanel = new JPanel();
        canvasOpsPanel.setLayout(new GridBagLayout());
        canvasOpsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        //canvasOpsPanel.setMaximumSize(new Dimension((3 * width) / 6, height + 200));
        //canvasOpsPanel.add(loadButton);
        //canvasOpsPanel.add(saveButton);
        GridBagConstraints costraintsForOperationPanel = new GridBagConstraints();

        int currentX = 0, currentY = 0;
        int buttonXDimension = 1, buttonYDimension = 1;

        costraintsForOperationPanel.fill = GridBagConstraints.BOTH;
        costraintsForOperationPanel.gridheight = buttonXDimension;
        costraintsForOperationPanel.gridwidth = buttonYDimension * 2;
        costraintsForOperationPanel.gridx = currentX;
        costraintsForOperationPanel.gridy = currentY;
        canvasOpsPanel.add(new JLabel("Panel"), costraintsForOperationPanel);
        costraintsForOperationPanel.gridwidth = buttonYDimension;

        currentY += buttonYDimension;
        costraintsForOperationPanel.gridx = currentX;
        costraintsForOperationPanel.gridy = currentY;
        canvasOpsPanel.add(addPanel, costraintsForOperationPanel);

        costraintsForOperationPanel.gridx = currentX + buttonXDimension;
        canvasOpsPanel.add(panelStyleButton, costraintsForOperationPanel);

        currentY += buttonYDimension;
        costraintsForOperationPanel.gridx = currentX;
        costraintsForOperationPanel.gridy = currentY;
        costraintsForOperationPanel.gridwidth = buttonYDimension * 2;
        costraintsForOperationPanel.insets = new Insets(10, 0, 0, 0);
        canvasOpsPanel.add(new JSeparator(), costraintsForOperationPanel);
        costraintsForOperationPanel.insets = new Insets(0, 0, 0, 0);

        currentY += buttonYDimension;
        costraintsForOperationPanel.gridwidth = buttonYDimension * 2;
        costraintsForOperationPanel.gridx = currentX;
        costraintsForOperationPanel.gridy = currentY;
        canvasOpsPanel.add(new JLabel("Stroke"), costraintsForOperationPanel);
        costraintsForOperationPanel.gridwidth = buttonYDimension;

        currentY += buttonYDimension;
        costraintsForOperationPanel.gridx = currentX;
        costraintsForOperationPanel.gridy = currentY;
        canvasOpsPanel.add(oneButton, costraintsForOperationPanel);

        costraintsForOperationPanel.gridx = currentX + buttonXDimension;
        canvasOpsPanel.add(twoButton, costraintsForOperationPanel);

        currentY += buttonYDimension;
        costraintsForOperationPanel.gridx = currentX;
        costraintsForOperationPanel.gridy = currentY;
        canvasOpsPanel.add(threeButton, costraintsForOperationPanel);

        costraintsForOperationPanel.gridx = currentX + buttonXDimension;
        canvasOpsPanel.add(fourButton, costraintsForOperationPanel);

        currentY += buttonYDimension;
        costraintsForOperationPanel.gridx = currentX;
        costraintsForOperationPanel.gridy = currentY;
        canvasOpsPanel.add(styleButton, costraintsForOperationPanel);

        costraintsForOperationPanel.gridx = currentX + buttonXDimension;
        canvasOpsPanel.add(jcb, costraintsForOperationPanel);

        currentY += buttonYDimension;
        costraintsForOperationPanel.gridx = currentX;
        costraintsForOperationPanel.gridy = currentY;
        costraintsForOperationPanel.gridwidth = 2;
        costraintsForOperationPanel.insets = new Insets(10, 0, 0, 0);
        canvasOpsPanel.add(new JSeparator(), costraintsForOperationPanel);
        costraintsForOperationPanel.insets = new Insets(0, 0, 0, 0);

        currentY += buttonYDimension;
        costraintsForOperationPanel.gridwidth = buttonYDimension * 2;
        costraintsForOperationPanel.gridx = currentX;
        costraintsForOperationPanel.gridy = currentY;
        canvasOpsPanel.add(new JLabel("Eraser"), costraintsForOperationPanel);
        costraintsForOperationPanel.gridwidth = buttonYDimension;

        currentY += buttonYDimension;
        costraintsForOperationPanel.gridx = currentX;
        costraintsForOperationPanel.gridy = currentY;
        canvasOpsPanel.add(eraser, costraintsForOperationPanel);
        currentY += buttonYDimension;
        costraintsForOperationPanel.gridx = currentX;
        costraintsForOperationPanel.gridy = currentY;
        costraintsForOperationPanel.gridwidth = 2;
        costraintsForOperationPanel.insets = new Insets(30, 0, 0, 0);
        canvasOpsPanel.add(new JSeparator(), costraintsForOperationPanel);
        //canvasOpsPanel.add(clearButton);
        //canvasOpsPanel.add(undoButton);
        //canvasOpsPanel.add(redoButton);
        canvas = new PersistentCanvas(dic);
        canvas.setBackground(Color.WHITE);
        canvas.setPreferredSize(new Dimension(Variables.CANVAS_WIDTH, Variables.CANVAS_HEIGHT));

        canvasPanel.add(new JScrollPane(canvas), BorderLayout.CENTER);
        canvas.setCursor(dic.createPathCursor());
        pane.add(canvasPanel);
        globalLayer = new Layer(false);
        globalLayer.setActive(true);
        blueInkLayer = new Layer(true);
        whiteLayer = new Layer(false);

        allLayers.add(blueInkLayer);
        allLayers.add(globalLayer);
        allLayers.add(whiteLayer);
        activeLayer = globalLayer;
        isBlue = false;
        isWhite = false;

        //Keyboard buttons management 
        //=======================
        AbstractAction deleteAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeShape();
                selection = null;
                selectionAll = new ArrayList<>();
            }
        };
        AbstractAction moveUpAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selection instanceof Panel) {
                    if (selection != null) {
                        //  for (Layer la : ((Panel) selection).getLayers()) {
                        //     la.moveLayer(0, -2);
                        //  }
                        for (DrawableItem di : selectionAll) {
                            di.move(0, -2);
                        }
                        udc.saveMoveToUndo(selection);

                    }
                }
            }
        };
        AbstractAction moveDownAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selection instanceof Panel) {
                    if (selection != null) {

                        for (DrawableItem di : selectionAll) {
                            di.move(0, 2);
                        }
                        udc.saveMoveToUndo(selection);

                    }
                }
            }
        };
        AbstractAction moveLeftAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selection instanceof Panel) {
                    if (selection != null) {
                        //   for (Layer la : ((Panel) selection).getLayers()) {
                        //      la.moveLayer(-2, 0);
                        //  }
                        for (DrawableItem di : selectionAll) {
                            di.move(-2, 0);
                        }
                        udc.saveMoveToUndo(selection);

                    }
                }
            }
        };
        AbstractAction moveRightAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selection instanceof Panel) {
                    if (selection != null) {
                        //  for (Layer la : ((Panel) selection).getLayers()) {
                        //    / la.moveLayer(2, 0);
                        //  }
                        for (DrawableItem di : selectionAll) {
                            di.move(2, 0);
                        }
                        udc.saveMoveToUndo(selection);
                    }
                }
            }
        };

        canvasPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke("DELETE"), "DELETE");
        canvasPanel.getActionMap()
                .put("DELETE", deleteAction);

        canvasPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke("UP"), "UP");
        canvasPanel.getActionMap()
                .put("UP", moveUpAction);
        canvasPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke("DOWN"), "DOWN");
        canvasPanel.getActionMap()
                .put("DOWN", moveDownAction);
        canvasPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke("RIGHT"), "RIGHT");
        canvasPanel.getActionMap()
                .put("RIGHT", moveRightAction);
        canvasPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke("LEFT"), "LEFT");
        canvasPanel.getActionMap()
                .put("LEFT", moveLeftAction);
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                if (e.getID() == KeyEvent.KEY_PRESSED) {
                    // System.out.println(e.getExtendedKeyCode());
                    if (e.getKeyCode() == 18) {
                        canvas.setCursor(dic.createAddCursor());
                        mode = "Rectangle";
                    } else if (e.getKeyCode() == 17) {
                        mode = "Select/Move";
                        canvas.setCursor(dic.createCtrlCursor());
                    } else if (e.getKeyCode() == 16) {
                        mode = "Gesture";
                        canvas.setCursor(dic.createGestureCursor());
                    }
                    if (mode.equals("Select/Move")) {
                        if (e.getExtendedKeyCode() == 90) {

                            udc.undoProcess(canvas);
                            resetLayerPanel();

                        } else if (e.getExtendedKeyCode() == 89) {
                            udc.redoProcess(canvas);
                            resetLayerPanel();
                        } else if (e.getExtendedKeyCode() == 40) { //down
                            for (DrawableItem di : selectionAll) {
                                ((Panel) di).resize(0, 2);
                            }
                            udc.saveResizeToUndo(selection);
                        } else if (e.getExtendedKeyCode() == 39) { //right
                            for (DrawableItem di : selectionAll) {
                                ((Panel) di).resize(2, 0);
                            }
                            udc.saveResizeToUndo(selection);
                        } else if (e.getExtendedKeyCode() == 38) { //up
                            for (DrawableItem di : selectionAll) {
                                ((Panel) di).resize(0, -2);
                            }
                            udc.saveResizeToUndo(selection);
                        } else if (e.getExtendedKeyCode() == 37) { //left
                            for (DrawableItem di : selectionAll) {
                                ((Panel) di).resize(-2, 0);
                            }
                            udc.saveResizeToUndo(selection);
                        } else if (e.getKeyCode() == 65) {//select all

                            for (DrawableItem di : canvas.getItems()) {
                                if (di instanceof Panel) {

                                    select(di, true);

                                }
                            }

                        } else if (e.getKeyCode() == 76) { //allign
                            gc.allign(true, canvas.getItems());
                            gc.allign(false, canvas.getItems());
                        } else if (e.getKeyCode() == 83) { //save
                            saveOptions();
                            //  saveCanvasFn();
                        } else if (e.getKeyCode() == 79) { //open
                            loadCanvasFn();
                        } else if (e.getKeyCode() == 78) { //new
                            newOptions();
                        }

                    }
                } else if (e.getID() == KeyEvent.KEY_RELEASED) {
                    if (e.getKeyCode() == 18 || e.getKeyCode() == 17 || e.getKeyCode() == 16) {
                        mode = "Path";
                        canvas.setCursor(dic.createPathCursor());

                    }

                }
                return false;
            }
        });

        //Mouse events management 
        //===================================
        canvas.addMouseListener(new MouseAdapter() {

            public void mouseReleased(MouseEvent e) {
                if (isMoving) {
                    if (selection instanceof Panel) {
                        udc.saveMoveToUndo(selection);
                    }
                    isMoving = false;
                    canvas.setCursor(dic.createCtrlCursor());

                } else if (mode.equals("Resize")) {
                    udc.saveResizeToUndo(selection);
                } else if (mode.equals("Res")) {
                    mode = "Path";
                    canvas.setCursor(dic.createPathCursor());

                    ((Panel) selection).setAnchor(null);

                }
                if (mode.equals("Gesture")) {
                    gesture.getDollar().pointerReleased(e.getX(), e.getY());
                    if (gesture.getPanel() != null) {
                        select(gesture.getPanel(), true);
                    }

                    //=============================
                    if (gesture.isGestureOk()) {
                        String gest = gesture.getGestureName();
                        switch (gest) {
                            case "x":
                                removeShape();
                                selection = null;
                                selectionAll = new ArrayList<>();
                                select(null, false);
                                break;
                            case "check":
                                createNewPanel(gesture.getFirstpoint());

                                break;
                            case "v":
                                gc.allign(true, selectionAll);
                                gc.allign(false, selectionAll);
                                break;
                            case "pigTail":
                                panelColor();
                                break;
                            case "leftSquareBracket":
                                saveOptions();
                                break;
                            case "rightSquareBracket":
                                newOptions();
                                select(null, false);
                                break;

                            case "circle CCW":
                                loadCanvasFn();
                                select(null, false);
                                break;
                        }
                    }

                    //=============================
                    canvas.removeItem(gesture);
                    gesture=null;
                    
                } else if (selection instanceof PathItem) {

                    selection = ((PathItem) selection).getPanel();
                    if (selection != null) {

                        select(selection, false);
                    }

                }
                resetLayerPanel();
            }

            public void mouseClicked(MouseEvent e) {

            }

            public void mousePressed(MouseEvent e) {
                Point p = e.getPoint();
                if (!mode.equals("Select/Move") && !mode.equals("Gesture")) {
                    canvas.getItemAt(p);
                    if (selection != null && ((Panel) selection).getAnchor() != null) {
                        canvas.setCursor(dic.createAddCursor());
                        mode = "Res";
                    } else {
                        DrawableItem item = null;
                        if (o == null) {
                            o = Color.BLACK;
                        }
                        Color f = panelColor;
                        if (panelColor == null) {
                            f = new Color(255, 255, 255, 128);
                        }
                        if (mode.equals("Rectangle")) {
                            item = new Panel(canvas, o, f, p, activeLayer);
                            ((Panel) item).setInitialPoint(p);
                            ((Panel) item).setInitialResizePoint(p);

                        } else if (mode.equals("Path")) {
                            Panel insidePanel = (Panel) canvas.getItemAt(p);
                            if (insidePanel == null) {
                                if (!isBlue) {
                                    if (isWhite) {
                                        item = new PathItem(canvas, Color.WHITE, f, p, null);
                                        whiteLayer.addItemToLayer((PathItem) item);
                                        ((PathItem) item).setThickness(20);

                                    } else {
                                        item = new PathItem(canvas, o, f, p, null);
                                        globalLayer.addItemToLayer((PathItem) item);

                                        ((PathItem) item).setThickness(thickness);
                                    }
                                } else {
                                    item = new PathItem(canvas, Color.BLUE.brighter().brighter().brighter(), f, p, null);
                                    blueInkLayer.addItemToLayer((PathItem) item);

                                    ((PathItem) item).setThickness(thickness);
                                }

                            } else {
                                if (!isBlue) {
                                    if (isWhite) {
                                        item = new PathItem(canvas, Color.WHITE, f, p, insidePanel);
                                        // insidePanel.getParentLayer().addItemToLayer((PathItem) item);
                                        whiteLayer.addItemToLayer((PathItem) item);
                                        ((PathItem) item).setThickness(20);

                                    } else {
                                        //  Layer l = lc.getActiveLayer(insidePanel);
                                        if (activeLayer.isIsBlueLayer()) {
                                            item = new PathItem(canvas, Color.BLUE.brighter().brighter().brighter(), f, p, insidePanel);

                                        } else {
                                            item = new PathItem(canvas, o, f, p, insidePanel);
                                        }
                                        activeLayer.addItemToLayer((PathItem) item);
                                        ((PathItem) item).setThickness(thickness);
                                    }

                                } else {
                                    item = new PathItem(canvas, Color.BLUE.brighter().brighter().brighter(), f, p, insidePanel);
                                    ((PathItem) item).setThickness(thickness);
                                    blueInkLayer.addItemToLayer((PathItem) item);
                                }
                            }
                        }

                        canvas.addItem(item);
                        udc.addItemtoUndo(new UndoableItem(item, 0));

                        select(item, false);
                    }
                } else if (mode.equals("Gesture")) {
                    Panel insidePanel = (Panel) canvas.getItemAt(p);

                    DrawableItem item = null;
                    Color f = panelColor;
                    if (panelColor == null) {
                        f = new Color(255, 255, 255, 128);
                    }

                    if (insidePanel == null) {
                        item = new Gesture(canvas, Color.BLACK, f, p, null, true);
                    } else {
                        item = new Gesture(canvas, Color.BLACK, f, p, insidePanel, true);
                    }
                    ((Gesture) item).setThickness(thickness);
                    canvas.addItem(item);
                    gesture = (Gesture) item;
                    //select(item, false);

                } else if (mode.equals("Select/Move")) {

                    DrawableItem item = canvas.getItemAt(p);
                    if (item != null) {
                        select(item, true);

                        selection = item;

                    } else {

                        select(null, false);
                    }
                } else {
                    select(null, false);
                    selection = null;
                }
                mousepos = p;
            }
        }
        );

        canvas.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                if (mode.equals("Gesture")) {
                    gesture.update(e.getPoint());
                    mousepos = e.getPoint();

                } else if (selection == null) {
                    return;
                } else {
                    if (mode.equals("Select/Move")) {
                        canvas.setCursor(dic.createDragCursor());
                        isMoving = true;
                        if (selection instanceof PathItem) {
                            Panel item = ((PathItem) selection).getPanel();
                            if (item != null) {
                                select(item, true);

                                selection = item;
                            }
                        }
                        int dx = e.getX() - mousepos.x;
                        int dy = e.getY() - mousepos.y;
                        for (DrawableItem di : selectionAll) {
                            di.move(dx, dy);
                        }

                    } else if (mode.equals("Res")) {
                        //selectionAll.clear();
                        select(selection, false);
                        ((Panel) selection).resize(((Panel) selection).getAnchor(), e.getPoint());

                        udc.saveResizeToUndo(selection);
                    } else {
                        selection.update(e.getPoint());

                    }
                    mousepos = e.getPoint();
                }
            }

        });

        //Layers panel start here
        //============================
        addLayer = new JButton();

        AbstractAction addLayerAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Layer l = new Layer(false);
                allLayers.add(l);
                lc.setActiveLayer(l, true, allLayers);
                activeLayer = l;
                resetLayerPanel();
                udc.addItemtoUndo(new UndoableItem(l, 0));
            }
        };
        addLayer.setAction(addLayerAction);
        addLayer.show(true);
        addLayer.setEnabled(true);

        mergeLayer = new JButton();

        AbstractAction mergeLayerAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Layer l = new Layer(false);
                allLayers.add(l);
                lc.setActiveLayer(l, true, allLayers);
                activeLayer = l;
                resetLayerPanel();
                udc.addItemtoUndo(new UndoableItem(l, 0));
            }
        };
        mergeLayer.setAction(mergeLayerAction);
        mergeLayer.show(true);
        mergeLayer.setEnabled(true);

        setButtonImage("/add.png", addLayer);
        addLayer.setToolTipText("Add Layer");

        setButtonImage("/Merge_Icon.png", mergeLayer);
        mergeLayer.setToolTipText("Merge Layer");

        JPanel buttonspanel = new JPanel();
        panel2 = new JPanel();
        buttonspanel.setLayout(new FlowLayout());
        buttonspanel.setPreferredSize(new Dimension(width / 6, height / 12));
        panel2.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel2.setPreferredSize(new Dimension(width / 6, height));
        buttonspanel.add(addLayer);
        buttonspanel.add(mergeLayer);

        rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        //  rightPanel.setPreferredSize(new Dimension(width / 1000, height));
        rightPanel.add(canvasOpsPanel);
        panel2.setBackground(Color.BLACK);
        pane.add(rightPanel);
        resetLayerPanel();

        pack();
        updateTitle();
        setVisible(true);
    }

    public void newOptions() {

        JDialog jda = new JDialog(GraphicalEditor.this, "New");
        jda.setLayout(new BorderLayout());
        JPanel emp = new JPanel(new BorderLayout());
        JButton newCanvas = new JButton();
        AbstractAction newCanv = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jda.setVisible(false);
                
                clearAll();
            }
        };
        newCanvas.setAction(newCanv);
        // newCanvas.setLabel("Empty Page");
        setButtonLayoutImage("/0.png", newCanvas);
        JPanel l = new JPanel(new GridLayout(2, 4));
        emp.add(newCanvas, BorderLayout.LINE_START);
        addToLayouts(l);
        jda.add(emp,BorderLayout.PAGE_START);
        jda.add(l,BorderLayout.PAGE_END);
        jda.setPreferredSize(new Dimension(600, 400));
        jda.setModal(true);
        final int x = ((Variables.CANVAS_WIDTH - jda.getWidth()) / 2) - 100;
        final int y = ((Variables.CANVAS_HEIGHT - jda.getHeight()) / 2) - 50;
        jda.setLocation(x, y);
        jda.setResizable(false);
        jda.pack();
        jda.setVisible(true);

    }

    public void saveOptions() {
        jd = new JDialog(GraphicalEditor.this, "Save");
        jd.setLayout(new BorderLayout());
        jd.setModal(true);
        JButton savePng = new JButton();
        AbstractAction savePngA = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jd.setVisible(false);
                saveAsImage();
            }
        };

        savePng.setAction(savePngA);
        savePng.setLabel("Save as PNG");
        JButton saveFile = new JButton();

        AbstractAction saveFileA = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jd.setVisible(false);
                saveCanvasFn();

            }
        };

        saveFile.setAction(saveFileA);
        saveFile.setLabel("Save As File");
        jd.add(savePng, BorderLayout.LINE_START);
        jd.add(saveFile, BorderLayout.LINE_END);
        jd.setPreferredSize(new Dimension(205, 100));
        final int x = (Variables.CANVAS_WIDTH - jd.getWidth()) / 2;
        final int y = (Variables.CANVAS_HEIGHT - jd.getHeight()) / 2;
        jd.setLocation(x, y);

        jd.setResizable(false);
        jd.pack();
        jd.setVisible(true);
    }

    public AbstractAction setAbsAction(int index, JPanel jda) {
        AbstractAction absAction = null;
        switch (index) {
            case 1:
                absAction = new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int[] c = {2, 2, 1};
                        loadTemplates(3, c);

                    }
                };
                break;
            case 2:
                absAction = new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int[] c = {2, 1, 2};
                        loadTemplates(3, c);

                    }
                };
                break;
            case 3:
                absAction = new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int[] c = {1, 2, 2};
                        loadTemplates(3, c);

                    }
                };
                break;
            case 4:
                absAction = new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int[] c = {1, 1, 2};
                        loadTemplates(3, c);

                    }
                };
                break;
            case 5:
                absAction = new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int[] c = {1, 1};
                        loadVerticalTemplates(c, 2);

                    }
                };
                break;
            case 6:
                absAction = new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int[] c = {1, 2};
                        loadVerticalTemplates(c, 2);

                    }
                };
                break;
            case 7:
                absAction = new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int[] c = {2, 1};
                        loadVerticalTemplates(c, 2);

                    }
                };
                break;
            case 8:
                absAction = new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int[] c = {1, 2};
                        loadTemplates(2, c);

                    }
                };
                break;
        }
        return absAction;
    }

    public void addToLayouts(JPanel jda) {

        JButton first = new JButton();
        JButton second = new JButton();
        JButton third = new JButton();
        JButton fourth = new JButton();
        JButton fifth = new JButton();
        JButton sixth = new JButton();
        JButton seventh = new JButton();
        JButton eight = new JButton();

        first.setAction(setAbsAction(1, jda));
        second.setAction(setAbsAction(2, jda));
        third.setAction(setAbsAction(3, jda));
        fourth.setAction(setAbsAction(4, jda));
        fifth.setAction(setAbsAction(5, jda));
        sixth.setAction(setAbsAction(6, jda));
        seventh.setAction(setAbsAction(7, jda));
        eight.setAction(setAbsAction(8, jda));

        setButtonLayoutImage("/1.png", first);
        setButtonLayoutImage("/2.png", second);
        setButtonLayoutImage("/3.png", third);
        setButtonLayoutImage("/4.png", fourth);
        setButtonLayoutImage("/5.png", fifth);
        setButtonLayoutImage("/6.png", sixth);
        setButtonLayoutImage("/7.png", seventh);
        setButtonLayoutImage("/8.png", eight);

        jda.add(first);
        jda.add(second);
        jda.add(third);
        jda.add(fourth);
        jda.add(fifth);
        jda.add(sixth);
        jda.add(seventh);
        jda.add(eight);

    }

    public void loadTemplates(int row, int[] column) {
        clearAll();
        for (DrawableItem di : layc.setLayoutPanels(row, column, globalLayer, canvas)) {
            canvas.addItem(di);

        }
        resetLayerPanel();
    }

    public void loadVerticalTemplates(int[] row, int column) {
        clearAll();
        for (DrawableItem di : layc.setLayoutVerticalPanels(row, column, globalLayer, canvas)) {
            canvas.addItem(di);

        }
        resetLayerPanel();
    }

    public void createNewPanel(Point p) {
        if (panelColor == null) {
            panelColor = new Color(255, 255, 255, 128);
        }
        Panel item = new Panel(canvas, Color.BLACK, panelColor, p, activeLayer);
        ((Panel) item).setInitialPoint(p);
        ((Panel) item).setInitialResizePoint(p);
        Rectangle thisRect = (Rectangle) (((Panel) item).getShape());
        thisRect.width = 200;
        thisRect.height = 200;
        item.update(new Point(200 + p.x, 200 + p.y));
        canvas.addItem(item);
        udc.addItemtoUndo(new UndoableItem(item, 0));
        select(item, false);
    }

    public void panelColor() {
        panelColor = JColorChooser.showDialog(
                canvas,
                "Choose Color",
                panelColor);
        if (panelColor != null) {
            panelColor = new Color(panelColor.getRed(), panelColor.getGreen(), panelColor.getBlue(), 128);
            if (selectionAll != null) {
                for (DrawableItem di : selectionAll) {
                    ((Panel) di).setFill(panelColor);
                }
                repaint();
            }
        } else {
            panelColor = new Color(255, 255, 255, 128);
        }
    }

    public void loadCanvasFn() {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File("."));
        chooser.setDialogTitle("Open File");

        chooser.setAcceptAllFileFilterUsed(false);
        chooser.setFileFilter(new FileNameExtensionFilter("Comico (*.comico)", "comico"));

        //    
        int result = chooser.showOpenDialog(canvas);
        if (result == JFileChooser.APPROVE_OPTION) {
            canvas.clear();
            allLayers.clear();
            udc.clearAll();

            for (Layer di : sc.load(chooser.getSelectedFile().getAbsolutePath())) {
                allLayers.add(di);
                for (DrawableItem dii : di.getDrawable()) {
                    canvas.addItem(dii);
                }
            }
        }
    }

    public void saveAsImage() {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File("."));
        chooser.setDialogTitle("Save As File");

        chooser.setAcceptAllFileFilterUsed(false);

        chooser.setFileFilter(new FileNameExtensionFilter("PNG (*.png)", "png"));

        if (chooser.showSaveDialog(canvas) == JFileChooser.APPROVE_OPTION) {
            BufferedImage image = new BufferedImage(canvas.getWidth(), canvas.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics g = image.getGraphics();

            canvas.paint(g);
            try {
                ImageIO.write(image, "png", new File(chooser.getSelectedFile() + ".png"));
                select(null, false);
                selectionAll = new ArrayList<>();

            } catch (IOException ex) {
                System.out.println("eror");
            }

        }

    }

    public void saveCanvasFn() {

        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File("."));
        chooser.setDialogTitle("Save As File");

        chooser.setAcceptAllFileFilterUsed(false);
        chooser.addChoosableFileFilter(new FileNameExtensionFilter("Comico", Variables.EXTENSION));

        if (chooser.showSaveDialog(canvas) == JFileChooser.APPROVE_OPTION) {

            select(null, false);
            selectionAll = new ArrayList<>();

            sc.save(allLayers, chooser.getSelectedFile().getAbsolutePath());

        }

    }

    /**
     * Resets the layer side panel on any change
     *
     * @param p selected panel
     */
    private void resetLayerPanel() {

        rightPanel.remove(panel2);
        rightPanel.remove(scroller);
        panel2 = new JPanel();
        JPanel buttonspanel = new JPanel();
        buttonspanel.setLayout(new GridBagLayout());

        panel2.setLayout(new GridBagLayout());

        GridBagConstraints costraintsForButtonPanel = new GridBagConstraints();
        GridBagConstraints costraintsForPanel2 = new GridBagConstraints();

        costraintsForPanel2.fill = GridBagConstraints.BOTH;
        costraintsForButtonPanel.gridx = 0;
        costraintsForButtonPanel.gridy = 0;
        buttonspanel.add(addLayer, costraintsForButtonPanel);

        addLayer.setEnabled(true);

        costraintsForButtonPanel.gridx = Variables.BUTTON_HEIGHT;
        costraintsForButtonPanel.gridy = 0;
        buttonspanel.add(mergeLayer, costraintsForButtonPanel);
        mergeLayer.setEnabled(true);

        costraintsForPanel2.gridx = 0;
        costraintsForPanel2.gridy = 0;
        costraintsForPanel2.gridwidth = Variables.BUTTON_WIDHT;
        costraintsForPanel2.gridheight = Variables.BUTTON_HEIGHT;
        rightPanel.add(buttonspanel);
        //panel2.add(buttonspanel, costraintsForPanel2);

        int i = 0;
        for (Layer l : allLayers) {
            if (i != 2) {
                if (!l.isDeleted()) {
                    JButton deleteButton = new JButton();
                    JToggleButton show = new JToggleButton();

                    JCheckBox select = new JCheckBox("Shown");
                    if (l.isActive()) {
                        show.setSelected(true);
                    }
                    if (!l.isHidden()) {
                        select.setSelected(true);
                    }

                    show.addItemListener(new ItemListener() { //getting a layer on top

                        public void itemStateChanged(ItemEvent ev) {
                            if (ev.getStateChange() == ItemEvent.SELECTED) {
                                lc.setActiveLayer(l, true, allLayers);
                                if (l.isIsBlueLayer()) {
                                    isBlue = true;
                                    jcb.setSelected(true);
                                    //if (selection != null) {
                                    lc.setActiveLayer(blueInkLayer, true, allLayers);
                                    canvas.setCursor(dic.createBlueCursor());
                                } else {
                                    isBlue = false;
                                    canvas.setCursor(dic.createPathCursor());

                                    //canvas.setCursor(Cursor.getDefaultCursor());
                                    jcb.setSelected(false);
                                }
                                resetLayerPanel();
                            } else if (ev.getStateChange() == ItemEvent.DESELECTED) {
                                lc.setActiveLayer(l, false, allLayers);
                                resetLayerPanel();

                            }
                        }
                    }
                    );
                    select.addItemListener(new ItemListener() { //show/hide layer

                        public void itemStateChanged(ItemEvent e) {
                            if (e.getStateChange() == 2) {
                                for (DrawableItem pi : l.getDrawable()) {
                                    canvas.removeItem(pi);
                                }

                                l.setHidden(false);
                            } else {
                                for (DrawableItem pi : l.getDrawable()) {
                                    canvas.addItem(pi);
                                }

                                l.setHidden(true);
                            }
                        }
                    });
                    AbstractAction deleteLayer = new AbstractAction() { //delete layer action
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            lc.setActiveLayer(l, false, allLayers);
                            l.setDeleted(true);
                            for (DrawableItem pi : l.getDrawable()) {
                                if (pi instanceof PathItem) {
                                    ((PathItem) pi).setHiddenWithPanel(true);
                                }
                                canvas.removeItem(pi);
                            }
                            resetLayerPanel();
                            udc.addItemtoUndo(new UndoableItem(l, 1));
                        }
                    };
                    deleteButton.setAction(deleteLayer);
                    if (i == 0 || i == 1) { //for blue and ground
                        deleteButton.setEnabled(false);
                    }

                    JPanel panel4 = new JPanel();
                    panel4.setLayout(new GridBagLayout());//buttons for layer
                    costraintsForButtonPanel.gridheight = 1;
                    costraintsForButtonPanel.gridwidth = 1;
                    costraintsForButtonPanel.gridx = 0;
                    costraintsForButtonPanel.gridy = 1;
                    panel4.add(show, costraintsForButtonPanel);
                    costraintsForButtonPanel.gridheight = 1;
                    costraintsForButtonPanel.gridwidth = 1;
                    costraintsForButtonPanel.gridx = 1;
                    costraintsForButtonPanel.gridy = 1;
                    panel4.add(deleteButton, costraintsForButtonPanel);
                    costraintsForButtonPanel.gridheight = 1;
                    costraintsForButtonPanel.gridwidth = 2;
                    costraintsForButtonPanel.gridx = 0;
                    costraintsForButtonPanel.gridy = 0;
                    panel4.add(select, costraintsForButtonPanel);

                    JPanel panel5 = new JPanel();
                    //panel5.setPreferredSize(new Dimension(150, 100));
                    ThumbNailCanvas tempcanvas = new ThumbNailCanvas(dic);
                    tempcanvas.setPreferredSize(new Dimension(Variables.THUMBNAIL_WIDTH, Variables.THUMBNAIL_HEIGHT));
                    panel5.add(tempcanvas);

                    tempcanvas.setBackground(Color.WHITE);

                    //here it set the objects inside the small canvas
                    //============================================
                    ThumbnailLayer tl = new ThumbnailLayer();

                    tl.setL(l);
                    tl.setLines();
                    tl.translateAll();
                    tl.resizeAll();
                    for (GeneralPath gp : tl.getLines()) {
                        tempcanvas.addItem(gp);
                    }
                    for (Rectangle gp : tl.getPanels()) {
                        tempcanvas.addItem(gp);
                    }

                    //panel3.add();
                    deleteButton.setToolTipText("Delete");
                    show.setToolTipText("On Top");
                    setToggleButtonImage("/merge.png", show);

                    setButtonImage("/delete.png", deleteButton);

                    if (i == 0) {
                        JLabel label = new JLabel("<html><b><font size=\"4\" color=\"0000ff\">Blue layer</font><b></html>");
                        costraintsForPanel2.gridx = 0;
                        costraintsForPanel2.gridy = Variables.BUTTON_HEIGHT;
                        costraintsForPanel2.gridwidth = Variables.LABEL_WIDTH;
                        costraintsForPanel2.gridheight = Variables.LABEL_HEIGHT;
                        panel2.add(label, costraintsForPanel2);
                    } else if (i == 1) {
                        JLabel label = new JLabel("<html><b><font size=\"4\" color=\"000000\">Ground layer</font><b></html>");
                        costraintsForPanel2.gridx = 0;
                        costraintsForPanel2.gridy = Variables.BUTTON_HEIGHT + Variables.LABEL_HEIGHT + Variables.PANEL_4_HEIGHT + Variables.PANEL_5_HEIGHT + Variables.SEPARATOR_HEIGTH;
                        costraintsForPanel2.gridwidth = Variables.LABEL_WIDTH;
                        costraintsForPanel2.gridheight = Variables.LABEL_HEIGHT;
                        panel2.add(label, costraintsForPanel2);
                    }

                    costraintsForPanel2.gridx = 0;
                    if (i == 0) {
                        costraintsForPanel2.gridy = Variables.BUTTON_HEIGHT + Variables.LABEL_HEIGHT;
                    } else {
                        if (i == 1) {
                            costraintsForPanel2.gridy = Variables.BUTTON_HEIGHT + Variables.LABEL_HEIGHT + Variables.PANEL_4_HEIGHT + Variables.PANEL_5_HEIGHT;
                            costraintsForPanel2.gridheight = Variables.SEPARATOR_HEIGTH;
                            costraintsForPanel2.gridwidth = Variables.SEPARATOR_WIDTH;
                            costraintsForPanel2.insets = new Insets(10, 0, 0, 0);
                            panel2.add(new JSeparator(), costraintsForPanel2);
                            costraintsForPanel2.insets = new Insets(0, 0, 0, 0);
                            costraintsForPanel2.gridy = Variables.BUTTON_HEIGHT + 2 * Variables.LABEL_HEIGHT + Variables.PANEL_4_HEIGHT + Variables.PANEL_5_HEIGHT + Variables.SEPARATOR_HEIGTH;
                        } else {
                            costraintsForPanel2.gridy = Variables.BUTTON_HEIGHT + 2 * Variables.LABEL_HEIGHT + 2 * Variables.PANEL_4_HEIGHT + 2 * Variables.PANEL_5_HEIGHT + Variables.SEPARATOR_HEIGTH + (i - 2) * (Variables.SEPARATOR_HEIGTH + Variables.PANEL_5_HEIGHT + Variables.PANEL_4_HEIGHT) + Variables.SEPARATOR_HEIGTH;
                        }
                    }
                    costraintsForPanel2.gridwidth = Variables.PANEL_5_WIDTH;
                    costraintsForPanel2.gridheight = Variables.PANEL_5_WIDTH;
                    panel2.add(panel5, costraintsForPanel2);

                    costraintsForPanel2.gridx = 0;
                    if (i == 0) {
                        costraintsForPanel2.gridy = Variables.BUTTON_HEIGHT + Variables.LABEL_HEIGHT + Variables.PANEL_5_HEIGHT;
                    } else {
                        if (i == 1) {
                            costraintsForPanel2.gridy = Variables.BUTTON_HEIGHT + 2 * Variables.LABEL_HEIGHT + Variables.PANEL_4_HEIGHT + 2 * Variables.PANEL_5_HEIGHT + Variables.SEPARATOR_HEIGTH;
                        } else {
                            costraintsForPanel2.gridy = Variables.BUTTON_HEIGHT + 2 * Variables.LABEL_HEIGHT + 2 * Variables.PANEL_4_HEIGHT + 2 * Variables.PANEL_5_HEIGHT + 2 * Variables.SEPARATOR_HEIGTH + Variables.PANEL_5_HEIGHT + (i - 2) * (Variables.SEPARATOR_HEIGTH + Variables.PANEL_5_HEIGHT + Variables.PANEL_4_HEIGHT);
                        }
                    }
                    costraintsForPanel2.gridwidth = Variables.PANEL_4_WIDTH;
                    costraintsForPanel2.gridheight = Variables.PANEL_4_HEIGHT;
                    panel2.add(panel4, costraintsForPanel2);
                    if (i > 1) {
                        costraintsForPanel2.gridy = Variables.BUTTON_HEIGHT + 2 * Variables.LABEL_HEIGHT + 2 * Variables.PANEL_4_HEIGHT + 2 * Variables.PANEL_5_HEIGHT + Variables.SEPARATOR_HEIGTH + (i - 2) * (Variables.SEPARATOR_HEIGTH + Variables.PANEL_5_HEIGHT + Variables.PANEL_4_HEIGHT);
                        costraintsForPanel2.gridheight = Variables.SEPARATOR_HEIGTH;
                        costraintsForPanel2.gridwidth = Variables.SEPARATOR_WIDTH;
                        costraintsForPanel2.insets = new Insets(10, 0, 0, 0);
                        panel2.add(new JSeparator(), costraintsForPanel2);
                        costraintsForPanel2.insets = new Insets(0, 0, 0, 0);
                    }

                    panel2.revalidate();

                }
            }
            i++;
        }

        rightPanel.add(panel2);
        scroller = new JScrollPane(panel2);
        rightPanel.add(scroller);

        validate();
        repaint();

    }

    /**
     * Updates title
     */
    private void updateTitle() {
        setTitle("Comico - Simple Comic Sketching App");
    }

    /**
     * Selects a specific item
     *
     * @param item DrawableItem to be selected
     */
    private void select(DrawableItem item, boolean addToSelection) {
        // 

        if (!addToSelection) {

            if (selection != null) {
                for (DrawableItem di : selectionAll) {
                    dic.deselect(di);
                }
            }
            selectionAll = new ArrayList<>();

        }
        selection = item;
        addToSelect();
        if (selection != null) {

            dic.select(selection);
        }
    }

    private void addToSelect() {
        if (!selectionAll.contains(selection) && selection instanceof Panel) {
            selectionAll.add(selection);
        }
    }

    /**
     * Clears the canvas
     */
    private void clearAll() {
        allLayers.clear();
        globalLayer = new Layer(false);
        globalLayer.setActive(true);
        blueInkLayer = new Layer(true);
        whiteLayer = new Layer(false);

        allLayers.add(blueInkLayer);
        allLayers.add(globalLayer);
        allLayers.add(whiteLayer);
        activeLayer = globalLayer;
        canvas.clear();
        udc.clearAll();
        resetLayerPanel();

    }

    /**
     * Sets the icon for a button
     *
     * @param path path of icon
     * @param toSet JButton to be set
     */
    public void setButtonImage(String path, JButton toSet) {
        Image img;
        try {
            img = ImageIO.read(getClass().getResource(path)).getScaledInstance(25, 25, 1);
            toSet.setIcon(new ImageIcon(img));
        } catch (Exception e) {
        }
    }

    public void setButtonLayoutImage(String path, JButton toSet) {
        Image img;
        try {
            img = ImageIO.read(getClass().getResource(path)).getScaledInstance(100, 100, 1);
            toSet.setIcon(new ImageIcon(img));
        } catch (Exception e) {
        }
    }

    /**
     * Sets the icon for a toggle button
     *
     * @param path path of icon
     * @param toSet toggle button to be set
     */
    public void setToggleButtonImage(String path, JToggleButton toSet) {
        Image img;
        try {
            img = ImageIO.read(getClass().getResource(path)).getScaledInstance(25, 25, 1);
            toSet.setIcon(new ImageIcon(img));
        } catch (Exception e) {
        }
    }

    /**
     * Removes selected item
     *
     * @return item removed or not
     */
    private boolean removeShape() {
        if (selection == null) {
            return false;
        } else {
            //emptyLayerPanel();
            for (DrawableItem di : selectionAll) {
                canvas.removeItem(di);
                if (di instanceof Panel) {

                    //   for (Layer li : ((Panel) selection).getLayers()) {
                    //  li.setActive(false);
                    for (PathItem pi : ((Panel) di).getLines()) {
                        canvas.removeItem(pi);
                    }
                    dic.deselect(di);
                    // }
                }
            }
            resetLayerPanel();
            udc.addItemtoUndo(new UndoableItem(selection, 1));
            return true;
        }
    }

    public static void main(String[] args) {
        GraphicalEditor editor = new GraphicalEditor(Variables.CANVAS_WIDTH, Variables.CANVAS_HEIGHT);

        editor.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}
