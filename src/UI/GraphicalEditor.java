package UI;

import controller.DrawableItemController;
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
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JSeparator;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import models.DrawableItem;
import models.Layer;
import models.Panel;
import models.PathItem;
import models.UndoableItem;

@SuppressWarnings("serial")
public class GraphicalEditor extends JFrame {

    // Graphical Interface
    private JPanel panel2;
    private JButton addLayer;
    private JButton mergeLayers;
    private JButton deleteLayers;
    private JToggleButton eraser;
    private JButton clearButton;
    private JButton undoButton;
    private JToggleButton oneButton;
    private JToggleButton twoButton;
    private JToggleButton threeButton;
    private JToggleButton fourButton;
    private JButton redoButton;
    private JButton styleButton;
    private Container pane;
    private Color o;

    private Point mousepos; // Stores the previous mouse position
    DrawableItemController dic = new DrawableItemController();
    UndoController udc = new UndoController();
    private JToggleButton jcb; //checkbox for blue ink
    private boolean isMoving; //is currently moving
    private String title; // Changes according to the mode
    private String mode;  // Mode of interaction
    private Boolean isBlue;
    private Boolean isWhite;
    private PersistentCanvas canvas; // Stores the created items
    private int thickness;
    private DrawableItem selection; 	 // Stores the selected item
    private Layer globalLayer; //global layer without panel
    private Layer whiteLayer; // white layer
    private Layer blueInkLayer; //blue ink layer
    // Listen the mode changes and update the Title
    private ActionListener modeListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            mode = e.getActionCommand();
            updateTitle();

        }
    };

    private boolean clearAll() {
        canvas.clear();
        return true;
    }
    // Listen the action on the button
    private ActionListener operationListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if (selection == null) {
                return;
            }

            String op = e.getActionCommand();
            if (op.equals("Delete")) {
                udc.addItemtoUndo(new UndoableItem(selection, 1));

                canvas.removeItem(selection);

                if (selection instanceof Panel) {
                    //  smallCanvas.show(false);
                    //addLayer.show(false);
                    for (Layer li : ((Panel) selection).getLayers()) {
                        li.setActive(false);
                        for (PathItem pi : li.getDrawn()) {
                            canvas.removeItem(pi);
                        }
                    }
                }
                selection = null;
            } else if (op.equals("Add Layer")) {
                if (selection != null) {

                }

            } else if (op.equals("Undo")) {

                udc.undoProcess(canvas);

            } else if (op.equals("Redo")) {
                udc.redoProcess(canvas);

            } else if (op.equals("Clone")) {
                DrawableItem i = selection.duplicate();
                i.move(5, 5);
                select(i);

            }
        }
    };

    private boolean removeShape() {
        if (selection == null) {
            return false;
        } else {

            canvas.removeItem(selection);
            if (selection instanceof Panel) {
                // smallCanvas.show(false);
                //addLayer.show(false);
                for (Layer li : ((Panel) selection).getLayers()) {
                    li.setActive(false);
                    for (PathItem pi : li.getDrawn()) {
                        canvas.removeItem(pi);
                    }
                }
            }
            udc.addItemtoUndo(new UndoableItem(selection, 1));
            return true;
        }
    }
    // Listen the click on the color chooser
    private MouseAdapter colorListener = new MouseAdapter() {
        public void mouseClicked(MouseEvent e) {
            JPanel p = (JPanel) e.getSource();
            Color c = p.getBackground();
            c = JColorChooser.showDialog(null, "Select a color", c);

            p.setBackground(c);
        }
    };

    // Constructor of the Graphical Editor
    public GraphicalEditor(String theTitle, int width, int height) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            // UIManager.setLookAndFeel("com.seaglasslookandfeel.SeaGlassLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
        title = theTitle;
        selection = null;
        isMoving = false;
        thickness = 2;
        o = Color.BLACK;
        pane = getContentPane();
        pane.setLayout(new BoxLayout(pane, BoxLayout.LINE_AXIS));

        //  JPanel panel = new JPanel();
        // panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        // panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        // panel.setPreferredSize(new Dimension(width / 5, height));
        mode = "Path";
        oneButton = new JToggleButton();
        oneButton.setSelected(true);
        twoButton = new JToggleButton();
        threeButton = new JToggleButton();
        fourButton = new JToggleButton();
        // Icon undoIcon = new ImageIcon(this.getClass().getResource("/Undo-icon.png"));

        undoButton = new JButton();

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
        try {
            Image img = ImageIO.read(getClass().getResource("/1s.gif")).getScaledInstance(25, 25, 1);
            oneButton.setIcon(new ImageIcon(img));
            img = ImageIO.read(getClass().getResource("/2s.gif")).getScaledInstance(25, 25, 1);
            twoButton.setIcon(new ImageIcon(img));

            img = ImageIO.read(getClass().getResource("/3s.gif")).getScaledInstance(25, 25, 1);
            threeButton.setIcon(new ImageIcon(img));

            img = ImageIO.read(getClass().getResource("/4s.gif")).getScaledInstance(25, 25, 1);
            fourButton.setIcon(new ImageIcon(img));

        } catch (IOException ex) {
            Logger.getLogger(GraphicalEditor.class.getName()).log(Level.SEVERE, null, ex);
        }
        AbstractAction undoAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                udc.undoProcess(canvas);
            }
        };
        undoButton.setAction(undoAction);
        try {
            Image img = ImageIO.read(getClass().getResource("/Undo-icon.png")).getScaledInstance(25, 25, 1);
            undoButton.setIcon(new ImageIcon(img));
            undoButton.setToolTipText("Undo");

        } catch (IOException ex) {
            Logger.getLogger(GraphicalEditor.class.getName()).log(Level.SEVERE, null, ex);
        }
        //undoButton.setText("Undo");
        redoButton = new JButton();

        AbstractAction redoAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                udc.redoProcess(canvas);
            }
        };
        redoButton.setAction(redoAction);
        try {
            Image img = ImageIO.read(getClass().getResource("/Redo-icon.png")).getScaledInstance(25, 25, 1);
            redoButton.setIcon(new ImageIcon(img));
            redoButton.setToolTipText("Redo");

        } catch (IOException ex) {
            Logger.getLogger(GraphicalEditor.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        try {
            Image img = ImageIO.read(getClass().getResource("/color.png")).getScaledInstance(25, 25, 1);
            styleButton.setIcon(new ImageIcon(img));
            styleButton.setToolTipText("Color");

        } catch (IOException ex) {
            Logger.getLogger(GraphicalEditor.class.getName()).log(Level.SEVERE, null, ex);
        }
        clearButton = new JButton();

        AbstractAction clearAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearAll();
            }
        };
        clearButton.setAction(clearAction);
        try {
            Image img = ImageIO.read(getClass().getResource("/clear.png")).getScaledInstance(25, 25, 1);
            clearButton.setIcon(new ImageIcon(img));
            clearButton.setToolTipText("Clear All");

        } catch (IOException ex) {
            Logger.getLogger(GraphicalEditor.class.getName()).log(Level.SEVERE, null, ex);
        }
        //  panel.add(clearButton);
        //panel.add(Box.createVerticalStrut(30));
        JPanel canvasPanel = new JPanel();
        jcb = new JToggleButton();

        jcb.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent ev) {
                if (ev.getStateChange() == ItemEvent.SELECTED) {
                    isBlue = true;
                    if (isWhite) {
                        isWhite = false;
                        eraser.setSelected(false);
                        canvas.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    }
                } else if (ev.getStateChange() == ItemEvent.DESELECTED) {
                    isBlue = false;
                }
            }
        });
        try {
            Image img = ImageIO.read(getClass().getResource("/blue.png")).getScaledInstance(25, 25, 1);
            jcb.setIcon(new ImageIcon(img));
            jcb.setToolTipText("Blue Ink");

        } catch (IOException ex) {
            Logger.getLogger(GraphicalEditor.class.getName()).log(Level.SEVERE, null, ex);
        }
        eraser = new JToggleButton();
        eraser.addItemListener(new ItemListener() {
            //private Object GraphicsUtilities;
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
                    canvas.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            }
        });
        try {
            Image img = ImageIO.read(getClass().getResource("/eraser.png")).getScaledInstance(25, 25, 1);
            eraser.setIcon(new ImageIcon(img));
            eraser.setToolTipText("Eraser");

        } catch (IOException ex) {
            Logger.getLogger(GraphicalEditor.class.getName()).log(Level.SEVERE, null, ex);
        }
        JSeparator separator1 = new JSeparator(JSeparator.VERTICAL);
        Dimension size = new Dimension(
                separator1.getMaximumSize().width,
                separator1.getMaximumSize().height);
        separator1.setMaximumSize(size);
        pane.add(separator1);

        // Create the canvas for drawing
        canvasPanel.setLayout(new BoxLayout(canvasPanel, BoxLayout.PAGE_AXIS));
        canvasPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        canvasPanel.setPreferredSize(new Dimension((5 * width) / 6, height));

        JPanel canvasOpsPanel = new JPanel();
        canvasOpsPanel.setLayout(new FlowLayout());
        canvasOpsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        canvasOpsPanel.setPreferredSize(new Dimension((5 * width) / 6, height / 12));
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
        canvas.setPreferredSize(new Dimension(width - 5 * (width / 6), height));
        canvasPanel.add(canvasOpsPanel, BorderLayout.PAGE_START);
        JSeparator separator2 = new JSeparator(JSeparator.HORIZONTAL);
        Dimension size2 = new Dimension(
                separator2.getMaximumSize().width,
                separator2.getMaximumSize().height);
        separator2.setMaximumSize(size2);
        canvasPanel.add(separator2);
        canvasPanel.add(canvas, BorderLayout.CENTER);
        pane.add(canvasPanel);
        globalLayer = new Layer(null, false);
        blueInkLayer = new Layer(null, true);
        whiteLayer = new Layer(null, false);
        isBlue = false;
        isWhite = false;
        // canvas.addUndoableEditListener(um);
        AbstractAction deleteAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeShape();
                selection = null;
            }
        };
        AbstractAction moveUpAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selection instanceof Panel) {
                    if (selection != null) {
                        for (Layer la : ((Panel) selection).getLayers()) {
                            la.moveLayer(0, -2);
                        }

                        selection.move(0, -2);
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
                        for (Layer la : ((Panel) selection).getLayers()) {
                            la.moveLayer(0, 2);
                        }

                        selection.move(0, 2);
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
                        for (Layer la : ((Panel) selection).getLayers()) {
                            la.moveLayer(-2, 0);
                        }

                        selection.move(-2, 0);
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
                        for (Layer la : ((Panel) selection).getLayers()) {
                            la.moveLayer(2, 0);
                        }
                        selection.move(2, 0);
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
                    //System.out.println(e.getExtendedKeyCode());
                    if (e.getKeyCode() == 18) {
                        mode = "Rectangle";
                    } else if (e.getKeyCode() == 17) {
                        mode = "Select/Move";
                    }
                    if (mode.equals("Select/Move")) {
                        if (e.getExtendedKeyCode() == 90) {
                            udc.undoProcess(canvas);
                        } else if (e.getExtendedKeyCode() == 89) {
                            udc.redoProcess(canvas);
                        } else if (e.getExtendedKeyCode() == 40) { //down
                            ((Panel) selection).resize(0, 2);
                            udc.saveResizeToUndo(selection);
                        } else if (e.getExtendedKeyCode() == 39) { //right
                            ((Panel) selection).resize(2, 0);
                            udc.saveResizeToUndo(selection);
                        } else if (e.getExtendedKeyCode() == 38) { //up
                            ((Panel) selection).resize(0, -2);
                            udc.saveResizeToUndo(selection);
                        } else if (e.getExtendedKeyCode() == 37) { //left
                            ((Panel) selection).resize(-2, 0);
                            udc.saveResizeToUndo(selection);
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
        canvas.addMouseListener(new MouseAdapter() {

            public void mouseReleased(MouseEvent e) {
                if (isMoving) {
                    if (selection instanceof Panel) {
                        udc.saveMoveToUndo(selection);
                    }
                    isMoving = false;
                } else if (mode.equals("Resize")) {
                    udc.saveResizeToUndo(selection);
                } else if (selection instanceof PathItem) {
                    selection = ((PathItem) selection).getLayer().getParentPanel();
                    if (selection == null) {
                        //showButton.show(false);
                        //  smallCanvas.show(false);
                        // addLayer.show(false);
                    } else {

                        select(selection);
                    }
                }
            }

            public void mouseClicked(MouseEvent e) {

            }

            public void mousePressed(MouseEvent e) {
                Point p = e.getPoint();
                if (!mode.equals("Select/Move")) {
                    DrawableItem item = null;
                    if (o == null) {
                        o = Color.BLACK;
                    }
                    Color f = Color.WHITE;
                    if (mode.equals("Rectangle")) {
                        item = new Panel(canvas, o, f, p);
                        ((Panel) item).setInitialPoint(p);
                        ((Panel) item).setInitialResizePoint(p);

                    } else if (mode.equals("Path")) {
                        Panel insidePanel = (Panel) canvas.getItemAt(p);
                        if (insidePanel == null) {
                            if (!isBlue) {
                                if (isWhite) {
                                    item = new PathItem(canvas, Color.WHITE, f, p, whiteLayer);
                                    ((PathItem) item).setThickness(20);

                                } else {
                                    item = new PathItem(canvas, o, f, p, globalLayer);
                                    ((PathItem) item).setThickness(thickness);
                                }
                            } else {
                                item = new PathItem(canvas, Color.BLUE.brighter().brighter().brighter(), f, p, blueInkLayer);
                                ((PathItem) item).setThickness(thickness);
                            }

                        } else {
                            if (!isBlue) {
                                if (isWhite) {
                                    item = new PathItem(canvas, Color.WHITE, f, p, insidePanel.getLayers().get(2));
                                    ((PathItem) item).setThickness(20);

                                } else {
                                    item = new PathItem(canvas, o, f, p, insidePanel.getLayers().get(1));
                                    ((PathItem) item).setThickness(thickness);
                                }

                            } else {
                                item = new PathItem(canvas, Color.BLUE.brighter().brighter().brighter(), f, p, insidePanel.getLayers().get(0));
                                ((PathItem) item).setThickness(thickness);
                            }
                        }
                    }

                    canvas.addItem(item);
                    udc.addItemtoUndo(new UndoableItem(item, 0));

                    select(item);
                } else if (mode.equals("Select/Move")) {
                    //Point which = null;

                    DrawableItem item = canvas.getItemAt(p);
                    if (item != null) {
                        select(item);
                        //fill.setBackground(Color.BLACK);
                        //outline.setBackground(Color.WHITE);
                        selection = item;

                    } else {

                        select(null);
                    }
                } else {
                    select(null);
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
                    if (((Panel) selection).getAnchor() != null) {
                        ((Panel) selection).resize(((Panel) selection).getAnchor(), e.getPoint());
                        udc.saveResizeToUndo(selection);
                    } else {
                        isMoving = true;
                        if (selection instanceof PathItem) {
                            Panel item = ((PathItem) selection).getLayer().getParentPanel();
                            if (item != null) {
                                select(item);
                                // fill.setBackground(item.getFill());
                                // outline.setBackground(item.getOutline());
                                selection = item;
                            }
                        }
                        int dx = e.getX() - mousepos.x;
                        int dy = e.getY() - mousepos.y;
                        for (Layer la : ((Panel) selection).getLayers()) {
                            la.moveLayer(dx, dy);
                        }

                        selection.move(dx, dy);

                    }
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
        addLayer = new JButton();
        mergeLayers = new JButton();
        deleteLayers = new JButton();
        // addLayer.show(true);

        AbstractAction addLayerAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((Panel) selection).addToLayers(new Layer(((Panel) selection), false));
            }
        };
        addLayer.setAction(addLayerAction);
        addLayer.show(true);
        addLayer.setEnabled(false);
        mergeLayers.show(true);
        mergeLayers.setEnabled(false);
        try {
            Image img = ImageIO.read(getClass().getResource("/delete.png")).getScaledInstance(25, 25, 1);
            deleteLayers.setIcon(new ImageIcon(img));
            deleteLayers.setToolTipText("Delete Layers");
            img = ImageIO.read(getClass().getResource("/add.png")).getScaledInstance(25, 25, 1);
            addLayer.setIcon(new ImageIcon(img));
            addLayer.setToolTipText("Add Layer");
            img = ImageIO.read(getClass().getResource("/merge.png")).getScaledInstance(25, 25, 1);
            mergeLayers.setIcon(new ImageIcon(img));
            mergeLayers.setToolTipText("Merge Layers");

        } catch (IOException ex) {
            Logger.getLogger(GraphicalEditor.class.getName()).log(Level.SEVERE, null, ex);
        }
        deleteLayers.show(true);
        deleteLayers.setEnabled(false);
        JPanel buttonspanel = new JPanel();
        buttonspanel.setLayout(new FlowLayout());
        buttonspanel.setPreferredSize(new Dimension(width / 6, height));
        panel2 = new JPanel();
        //panel2.setLayout(new BoxLayout(panel2, BoxLayout.PAGE_AXIS));
        panel2.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel2.setPreferredSize(new Dimension(width / 6, height));

        buttonspanel.add(addLayer);
        buttonspanel.add(mergeLayers);
        buttonspanel.add(deleteLayers);
        panel2.add(buttonspanel, BorderLayout.PAGE_START);
        pane.add(panel2);
        pack();
        updateTitle();
        setVisible(true);
    }

    // Update the Title
    private void updateTitle() {
        setTitle(title + " - " + mode);
    }

    // Select an Item
    private void select(DrawableItem item) {
        if (selection != null) {
            dic.deselect(selection);
        }

        selection = item;
        if (selection != null) {
            if (selection instanceof Panel) {
                // smallCanvas.show(true);
                // addLayer.show(true);
                pane.repaint();

            }
            dic.select(selection);
        } else {
            //  smallCanvas.show(false);
            //addLayer.show(false);
        }
    }

    public static void main(String[] args) {
        GraphicalEditor editor = new GraphicalEditor("GraphicalEditor", 1500, 1000);
        editor.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}
