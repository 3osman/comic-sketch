package UI;

import controller.DrawableItemController;
import controller.UndoController;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.BorderFactory;
import java.awt.Container;
import java.awt.Point;
import java.awt.Color;
import java.awt.Dimension;
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
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import models.DrawableItem;
import models.Layer;
import models.Panel;
import models.PathItem;
import models.UndoableItem;

@SuppressWarnings("serial")
public class GraphicalEditor extends JFrame {

    // Graphical Interface
    private ArrayList<JButton> operations;
    private JPanel outline;
    private JPanel fill;
    private Point mousepos; // Stores the previous mouse position
    DrawableItemController dic = new DrawableItemController();
    UndoController udc = new UndoController();
    private JCheckBox jcb; //checkbox for blue ink
    private boolean isMoving; //is currently moving
    private String title; // Changes according to the mode
    private String mode;  // Mode of interaction
    private Boolean isBlue;
    private PersistentCanvas canvas; // Stores the created items
    private PersistentCanvas smallCanvas; //stores reduced size of canvas
    private DrawableItem selection; 	 // Stores the selected item
    private Layer globalLayer; //global layer without panel
    private Layer blueInkLayer; //blue ink layer
    // Listen the mode changes and update the Title
    private ActionListener modeListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            mode = e.getActionCommand();
            updateTitle();

        }
    };

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
                    for (Layer li : ((Panel) selection).getLayers()) {
                        for (PathItem pi : li.getDrawn()) {
                            canvas.removeItem(pi);
                        }
                    }
                }
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
            udc.addItemtoUndo(new UndoableItem(selection, 1));

            canvas.removeItem(selection);
            if (selection instanceof Panel) {
                for (Layer li : ((Panel) selection).getLayers()) {
                    for (PathItem pi : li.getDrawn()) {
                        canvas.removeItem(pi);
                    }
                }
            }
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

    // Create the radio button for the mode
    // Create the button for the operation
    private JButton createOperation(String description) {
        JButton btn = new JButton(description);
        btn.setActionCommand(description);
        btn.addActionListener(operationListener);
        operations.add(btn);
        return btn;
    }

    // Create the color sample used to choose the color
    private JPanel createColorSample(Color c) {
        JPanel p = new JPanel();
        p.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        p.setOpaque(true);
        p.setBackground(c);
        p.setMaximumSize(new Dimension(500, 150));
        p.addMouseListener(colorListener);
        return p;
    }

    // Constructor of the Graphical Editor
    public GraphicalEditor(String theTitle, int width, int height) {
        title = theTitle;
        selection = null;
        isMoving = false;
        Container pane = getContentPane();
        pane.setLayout(new BoxLayout(pane, BoxLayout.LINE_AXIS));

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create the mode selection button list
        mode = "Rectangle";
        //ButtonGroup group = new ButtonGroup();
        // panel.add(createMode("Select/Move", group));
        // panel.add(createMode("Rectangle", group));
        // panel.add(createMode("Path", group));
        panel.add(Box.createVerticalStrut(30));
        fill = createColorSample(Color.WHITE);
        panel.add(fill);
        panel.add(Box.createVerticalStrut(10));
        outline = createColorSample(Color.BLACK);
        panel.add(outline);
        panel.add(Box.createVerticalStrut(30));
        jcb = new JCheckBox("Blue Ink");
        jcb.addItemListener(new ItemListener() {
            @Override

            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == 1) {
                    isBlue = true;
                } else {
                    isBlue = false;
                }
            }

        });
        panel.add(jcb);

        operations = new ArrayList<JButton>();
        panel.add(createOperation("Undo"));
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(createOperation("Redo"));
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(createOperation("Add Layer"));
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(createOperation("Delete"));
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(createOperation("Clone"));
        panel.add(Box.createVerticalGlue());
        pane.add(panel);
        smallCanvas = new PersistentCanvas(dic);
        smallCanvas.setBackground(Color.WHITE);
        smallCanvas.setPreferredSize(new Dimension(width, height));
        // Create the canvas for drawing
        canvas = new PersistentCanvas(dic);
        canvas.setBackground(Color.WHITE);
        canvas.setPreferredSize(new Dimension(width, height));
        pane.add(canvas);
        globalLayer = new Layer(null, false);
        blueInkLayer = new Layer(null, true);
        isBlue = false;
        // canvas.addUndoableEditListener(um);
        AbstractAction deleteAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeShape();
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

        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke("DELETE"), "DELETE");
        panel.getActionMap()
                .put("DELETE", deleteAction);
        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke("UP"), "UP");
        panel.getActionMap()
                .put("UP", moveUpAction);
        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke("DOWN"), "DOWN");
        panel.getActionMap()
                .put("DOWN", moveDownAction);
        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke("RIGHT"), "RIGHT");
        panel.getActionMap()
                .put("RIGHT", moveRightAction);
        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke("LEFT"), "LEFT");
        panel.getActionMap()
                .put("LEFT", moveLeftAction);
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                if (e.getID() == KeyEvent.KEY_PRESSED) {
                    System.out.println(e.getExtendedKeyCode());
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

                        } else if (e.getExtendedKeyCode() == 39) { //right
                            ((Panel) selection).resize(2, 0);
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
                }
            }

            public void mouseClicked(MouseEvent e) {
                Point p = e.getPoint();
                if (mode.equals("Select/Move")) {
                    DrawableItem item = canvas.getItemAt(p);
                    if (item != null) {
                        select(item);
                        fill.setBackground(item.getFill());
                        outline.setBackground(item.getOutline());
                        selection = item;
                    } else {
                        select(null);
                    }
                }
            }

            public void mousePressed(MouseEvent e) {
                Point p = e.getPoint();
                if (!mode.equals("Select/Move")) {
                    DrawableItem item = null;
                    Color o = outline.getBackground();
                    Color f = fill.getBackground();
                    if (mode.equals("Rectangle")) {
                        item = new Panel(canvas, o, f, p);
                        ((Panel) item).setInitialPoint(p);

                    } else if (mode.equals("Path")) {
                        Panel insidePanel = (Panel) canvas.getItemAt(p);
                        if (insidePanel == null) {
                            if (!isBlue) {
                                item = new PathItem(canvas, o, f, p, globalLayer);
                            } else {
                                item = new PathItem(canvas, Color.BLUE.brighter().brighter().brighter(), f, p, blueInkLayer);
                            }

                        } else {
                            if (!isBlue) {

                                item = new PathItem(canvas, o, f, p, insidePanel.getLayers().get(1));

                            } else {
                                item = new PathItem(canvas, Color.BLUE.brighter().brighter().brighter(), f, p, insidePanel.getLayers().get(0));
                            }
                        }
                    }

                    canvas.addItem(item);
                    udc.addItemtoUndo(new UndoableItem(item, 0));
                    select(item);
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
                        Panel item = ((PathItem) selection).getLayer().getParentPanel();
                        if (item != null) {
                            select(item);
                            fill.setBackground(item.getFill());
                            outline.setBackground(item.getOutline());
                            selection = item;
                        }
                    }
                    int dx = e.getX() - mousepos.x;
                    int dy = e.getY() - mousepos.y;
                    for (Layer la : ((Panel) selection).getLayers()) {
                        la.moveLayer(dx, dy);
                    }

                    selection.move(dx, dy);

                } else {
                    selection.update(e.getPoint());
                }
                mousepos = e.getPoint();
            }

        });

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

            dic.select(selection);

            for (JButton op : operations) {
                op.setEnabled(true);
            }
        } else {
            for (JButton op : operations) {
                op.setEnabled(false);
            }
        }

    }

    public static void main(String[] args) {
        GraphicalEditor editor = new GraphicalEditor("GraphicalEditor", 800, 600);
        editor.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}
