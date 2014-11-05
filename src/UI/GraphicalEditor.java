package UI;

import controller.DrawableItemController;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.BorderFactory;

import java.awt.Container;
import java.awt.Point;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import models.DrawableItem;
import models.Layer;
import models.Panel;
import models.PathItem;

@SuppressWarnings("serial")
public class GraphicalEditor extends JFrame {

    // Graphical Interface
    private ArrayList<JButton> operations;
    private JPanel outline;
    private JPanel fill;
    private Point mousepos; // Stores the previous mouse position
    DrawableItemController dic = new DrawableItemController();

    private String title; // Changes according to the mode
    private String mode;  // Mode of interaction

    private PersistentCanvas canvas; // Stores the created items
    private DrawableItem selection; 	 // Stores the selected item

    // Listen the mode changes and update the Title
    private ActionListener modeListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            // TODO you can use the function updateTitle();
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
				//TODO delete the selected item

                canvas.removeItem(selection);

            } else if (op.equals("Clone")) {
                //TODO duplicate and translate the selected item
                DrawableItem i = selection.duplicate();
                i.move(5, 5);
                select(i);

            }
        }
    };

    // Listen the click on the color chooser
    private MouseAdapter colorListener = new MouseAdapter() {
        public void mouseClicked(MouseEvent e) {
            JPanel p = (JPanel) e.getSource();
            Color c = p.getBackground();
            c = JColorChooser.showDialog(null, "Select a color", c);
			// TODO Manage the color change
            // You can test if the action have been done 
            // on the fill JPpanel or on the outline JPanel 
            p.setBackground(c);
        }
    };

    // Create the radio button for the mode
    private JRadioButton createMode(String description, ButtonGroup group) {
        JRadioButton rbtn = new JRadioButton(description);
        rbtn.setActionCommand(description);
        if (mode == description) {
            rbtn.setSelected(true);
        }
        rbtn.addActionListener(modeListener);
        group.add(rbtn);
        return rbtn;
    }

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

        Container pane = getContentPane();
        pane.setLayout(new BoxLayout(pane, BoxLayout.LINE_AXIS));

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create the mode selection button list
        mode = "Rectangle"; // TODO you can change that later
        ButtonGroup group = new ButtonGroup();
        panel.add(createMode("Select/Move", group));
        panel.add(createMode("Rectangle", group));
        panel.add(createMode("Ellipse", group));
        panel.add(createMode("Line", group));
        panel.add(createMode("Path", group));
        panel.add(Box.createVerticalStrut(30));
        fill = createColorSample(Color.LIGHT_GRAY);
        panel.add(fill);
        panel.add(Box.createVerticalStrut(10));
        outline = createColorSample(Color.BLACK);
        panel.add(outline);
        panel.add(Box.createVerticalStrut(30));
        operations = new ArrayList<JButton>();
        panel.add(createOperation("Delete"));
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(createOperation("Clone"));
        panel.add(Box.createVerticalGlue());
        pane.add(panel);

        // Create the canvas for drawing
        canvas = new PersistentCanvas(dic);
        canvas.setBackground(Color.WHITE);
        canvas.setPreferredSize(new Dimension(width, height));
        pane.add(canvas);

        canvas.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                Point p = e.getPoint();
                if (mode.equals("Select/Move")) {
                    // TODO you can use the function select(DrawableItem item);
                    DrawableItem item = canvas.getItemAt(p);
                    select(item);
                    fill.setBackground(item.getFill());
                    outline.setBackground(item.getOutline());
                    selection = item;
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

                    } else if (mode.equals("Path")) {

                        Layer l = new Layer(new Panel(canvas, o, f, p));
                        item = new PathItem(canvas, o, f, p, l);
                    }
                    canvas.addItem(item);
                    select(item);
                }
                mousepos = p;
            }
        });

        canvas.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                if (selection == null) {
                    return;
                }
                if (mode.equals("Select/Move")) {
					// TODO move the selected object

                    selection.move(e.getX() - mousepos.x, e.getY() - mousepos.y);
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
			// TODO set the color of the fill and outline JPanel 
            // to the color of the selected object
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
