package UI;

import controller.DrawableItemController;
import controller.GroupingController;
import controller.LayersController;
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
import java.awt.GridLayout;
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
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;
import models.DrawableItem;
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
    //buttons
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
    private Point mousepos; // Stores the previous mouse position
    //controllers declaration
    DrawableItemController dic = new DrawableItemController(); //controls all drawing
    UndoController udc = new UndoController(); //undo controller
    GroupingController gc = new GroupingController(); //grouping, not finished
    LayersController lc = new LayersController(); //layers controller
    SavingController sc = new SavingController();
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
    private Layer globalLayer; //global layer without panel
    private Layer whiteLayer; // white layer
    private Layer blueInkLayer; //blue ink layer
    private int globalWidth; //width
    private int globalHeight; //height
    private Layer activeLayer;

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
        isMoving = false;
        thickness = 2;
        o = Color.BLACK;
        globalWidth = width;
        globalHeight = height;
        allLayers = new ArrayList<>();

        scroller = new JScrollPane();
        pane.setLayout(new BoxLayout(pane, BoxLayout.LINE_AXIS));

        mode = "Path";

        //Main buttons start here
        //=====================================
        saveButton = new JButton();
        AbstractAction saveCanvas = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                chooser.setCurrentDirectory(new java.io.File("."));
                chooser.setDialogTitle("Save As File");

                chooser.setAcceptAllFileFilterUsed(false);
                chooser.addChoosableFileFilter(new FileNameExtensionFilter("Comico", Variables.EXTENSION));
                //    
                if (chooser.showSaveDialog(canvas) == JFileChooser.APPROVE_OPTION) {
                   // BufferedImage image = new BufferedImage(canvas.getWidth(), canvas.getHeight(), BufferedImage.TYPE_INT_ARGB);
                    // Graphics g = image.getGraphics();

                    //   canvas.paint(g);
                    // try {
                    //  ImageIO.write(image, "png", new File(chooser.getSelectedFile() + ".png"));
                    select(null, false);
                    ArrayList<DrawableItem> allItems = canvas.getItems();
                        //====================================
                    //+++++++++++++++++++++++++++++++++++

                    //Call the function of save here
                    sc.save(allLayers, chooser.getSelectedFile().getAbsolutePath());

                    //++++++++++++++++++++++++++++++++++++++
                    //=======================================
                    // } catch (IOException ex) {
                    //System.out.println("eror");
                    // }
                }

            }
        };
        saveButton.setAction(saveCanvas);

        loadButton = new JButton();
        AbstractAction loadCanvas = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                chooser.setCurrentDirectory(new java.io.File("."));
                chooser.setDialogTitle("Open File");

                chooser.setAcceptAllFileFilterUsed(false);
                chooser.addChoosableFileFilter(new FileNameExtensionFilter("Comico", Variables.EXTENSION));
                //    
                int result = chooser.showOpenDialog(canvas);
                if (result == JFileChooser.APPROVE_OPTION) {
                    canvas.clear();
                    allLayers.clear();
                    udc.clearAll();

                    for (Layer di : sc.load(chooser.getSelectedFile().getAbsolutePath())) {
                        // canvas.addItem(di);
                        allLayers.add(di);
                        for (DrawableItem dii : di.getDrawable()) {
                            canvas.addItem(dii);
                        }
                        //sc.save(allItems, chooser.getSelectedFile().getAbsolutePath());
                    }
                }

            }
        };
        loadButton.setAction(loadCanvas);

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
                Panel item = new Panel(canvas, Color.BLACK, new Color(255, 255, 255, 128), new Point(10, 10), activeLayer);
                ((Panel) item).setInitialPoint(new Point(10, 10));
                ((Panel) item).setInitialResizePoint(new Point(10, 10));
                Rectangle thisRect = (Rectangle) (((Panel) item).getShape());
                thisRect.width = 200;
                thisRect.height = 200;
                item.update(new Point(210, 210));
                canvas.addItem(item);
                udc.addItemtoUndo(new UndoableItem(item, 0));

                select(item, false);
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
                        canvas.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    }
                } else if (ev.getStateChange() == ItemEvent.DESELECTED) {
                    isBlue = false;
                    //if (selection != null) {
                    lc.setActiveLayer(globalLayer, true, allLayers);
                    resetLayerPanel();
                    select(null, false);
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
                    canvas.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            }
        });
        setToggleButtonImage("/eraser.png", eraser);
        eraser.setToolTipText("Eraser");

        JSeparator separator1 = new JSeparator(JSeparator.VERTICAL);
        Dimension size = new Dimension(
                separator1.getMaximumSize().width,
                separator1.getMaximumSize().height);
        separator1.setMaximumSize(size);
        pane.add(separator1);

        // Canvas Panel starts here
        //==========================================
        canvasPanel.setLayout(new BoxLayout(canvasPanel, BoxLayout.PAGE_AXIS));
        canvasPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        canvasPanel.setPreferredSize(new Dimension((5 * width) / 6, height));

        JPanel canvasOpsPanel = new JPanel();
        canvasOpsPanel.setLayout(new FlowLayout());
        canvasOpsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        canvasOpsPanel.setPreferredSize(new Dimension((5 * width) / 6, height / 12));
        canvasOpsPanel.add(loadButton);
        canvasOpsPanel.add(saveButton);
        canvasOpsPanel.add(addPanel);
        canvasOpsPanel.add(oneButton);
        canvasOpsPanel.add(twoButton);
        canvasOpsPanel.add(threeButton);
        canvasOpsPanel.add(fourButton);
        canvasOpsPanel.add(styleButton);
        canvasOpsPanel.add(jcb);
        canvasOpsPanel.add(eraser);

        canvasOpsPanel.add(clearButton);
        canvasOpsPanel.add(undoButton);
        canvasOpsPanel.add(redoButton);

        canvas = new PersistentCanvas(dic);
        canvas.setBackground(Color.WHITE);
        canvas.setPreferredSize(new Dimension((width - 5 * (width / 6)), height));
        canvasPanel.add(canvasOpsPanel, BorderLayout.PAGE_START);
        JSeparator separator2 = new JSeparator(JSeparator.HORIZONTAL);
        Dimension size2 = new Dimension(
                separator2.getMaximumSize().width,
                separator2.getMaximumSize().height);
        separator2.setMaximumSize(size2);
        canvasPanel.add(separator2);
        canvasPanel.add(canvas, BorderLayout.CENTER);
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
                        //    for (Layer la : ((Panel) selection).getLayers()) {
                        //       la.moveLayer(0, 2);
                        //  }
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

                    if (e.getKeyCode() == 18) {
                        mode = "Rectangle";
                    } else if (e.getKeyCode() == 17) {
                        mode = "Select/Move";
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
                        } else if (e.getKeyCode() == 65) {
                            // anchorP = gc.getDistinctivePoints(width, height);
                            //dic.allign(canvas, gc, anchorP, width, height);
                            gc.allign(true, canvas.getItems());
                            gc.allign(false, canvas.getItems());
                        }

                    }
                } else if (e.getID() == KeyEvent.KEY_RELEASED) {
                    if (e.getKeyCode() == 18 || e.getKeyCode() == 17) {
                        mode = "Path";
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
                } else if (mode.equals("Resize")) {
                    udc.saveResizeToUndo(selection);
                } else if (mode.equals("Res")) {
                    mode = "Path";
                    ((Panel) selection).setAnchor(null);
                    
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
                if (!mode.equals("Select/Move")) {
                    canvas.getItemAt(p);
                    if (selection != null && ((Panel) selection).getAnchor() != null) {

                        mode = "Res";
                    } else {
                        DrawableItem item = null;
                        if (o == null) {
                            o = Color.BLACK;
                        }
                        Color f = new Color(255, 255, 255, 128);
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
                if (selection == null) {
                    return;
                }

                if (mode.equals("Select/Move")) {

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
                    selectionAll.clear();
                    select(selection, false);
                    ((Panel) selection).resize(((Panel) selection).getAnchor(), e.getPoint());
                   
                    udc.saveResizeToUndo(selection);
                } else {
                    selection.update(e.getPoint());

                }
                mousepos = e.getPoint();
            }

        });
        JSeparator separator = new JSeparator(JSeparator.VERTICAL);
        Dimension size1 = new Dimension(
                separator.getMaximumSize().width,
                separator.getMaximumSize().height);
        separator.setMaximumSize(size1);
        pane.add(separator);

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
        panel2.setLayout(new BoxLayout(panel2, BoxLayout.Y_AXIS));
        buttonspanel.add(addLayer);
        buttonspanel.add(mergeLayer);

        panel2.add(buttonspanel);

        pane.add(panel2);
        resetLayerPanel();

        pack();
        updateTitle();
        setVisible(true);
    }

    //functions for managing UI, not drawable items
    /**
     * Empties the panel of the layers, called when nothing is selected
     */
    private void emptyLayerPanel() {

        /* pane.remove(panel2);
         pane.remove(scroller);
         panel2.removeAll();
         panel2 = new JPanel();
         JPanel buttonspanel = new JPanel();
         buttonspanel.setLayout(new FlowLayout());
         buttonspanel.setPreferredSize(new Dimension(globalWidth / 6, globalHeight / 12));
         panel2.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
         panel2.setPreferredSize(new Dimension(globalWidth / 6, globalHeight));
         panel2.setLayout(new BoxLayout(panel2, BoxLayout.Y_AXIS));
         buttonspanel.add(addLayer);

         addLayer.setEnabled(false);

         panel2.add(buttonspanel);

         pane.add(panel2);
       
         validate();
         repaint();*/
    }

    /**
     * Resets the layer side panel on any change
     *
     * @param p selected panel
     */
    private void resetLayerPanel() {

        pane.remove(panel2);
        pane.remove(scroller);
        panel2.removeAll();
        panel2 = new JPanel();
        JPanel buttonspanel = new JPanel();
        buttonspanel.setLayout(new GridLayout(1, 0));
        //buttonspanel.setMaximumSize(new Dimension(100, 100));
        //buttonspanel.setPreferredSize(new Dimension(globalWidth / 6, globalHeight / 12));
        // panel2.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        //panel2.setPreferredSize(new Dimension(globalWidth / 6, globalHeight));
        panel2.setLayout(new GridLayout(0, 1));
        buttonspanel.add(addLayer);

        addLayer.setEnabled(true);

        buttonspanel.add(mergeLayer);
        mergeLayer.setEnabled(true);

        //mergeLayer.setPreferredSize(new Dimension(10, 10));
        //   addLayer.setPreferredSize(new Dimension(10, 10));
        panel2.add(buttonspanel);
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
                                } else {
                                    isBlue = false;
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
                                    ((PathItem) pi).setHiddenWithLayer(true);
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
                    // JPanel panel3 = new JPanel();
                    //  panel3.setLayout(new GridLayout(1, 0));
                    //panel3.setPreferredSize(new Dimension(200, 200));

                    JPanel panel4 = new JPanel();
                    panel4.setLayout(new GridLayout(3, 1));//buttons for layer

                    panel4.add(select);
                    panel4.add(show);
                    panel4.add(deleteButton);

                    JPanel panel5 = new JPanel();
                    //panel5.setPreferredSize(new Dimension(150, 100));
                    ThumbNailCanvas tempcanvas = new ThumbNailCanvas(dic);
                    tempcanvas.setPreferredSize(new Dimension(Variables.THUMBNAIL_WIDTH, Variables.THUMBNAIL_HEIGHT));
                    panel5.add(tempcanvas);

                    tempcanvas.setBackground(Color.WHITE);
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

                    //panel3.setPreferredSize(new Dimension(globalWidth / 6, globalHeight / 12));
                    if (i == 0) {
                        JLabel label = new JLabel("<html><b><font size=\"4\" color=\"0000ff\">Blue layer</font><b></html>");

                        panel2.add(label);
                    } else if (i == 1) {
                        JLabel label = new JLabel("<html><b><font size=\"4\" color=\"000000\">Ground layer</font><b></html>");
                        panel2.add(label);
                    }
                    panel2.add(panel5);
                    panel2.add(panel4);//each layer}
                    //panel2.add(panel3);

                    panel2.revalidate();

                }
            }
            i++;
        }

        pane.add(panel2);
        scroller = new JScrollPane(panel2);
        pane.add(scroller, BorderLayout.CENTER);

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
        resetLayerPanel();
        canvas.clear();

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
