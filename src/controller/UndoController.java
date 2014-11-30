/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import UI.PersistentCanvas;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import models.DrawableItem;
import models.Layer;
import models.Panel;
import models.PathItem;
import models.SizedStack;
import models.UndoableItem;

/**
 * Undo controller for the undo/redo process
 *
 * @author Osman
 */
public class UndoController {

    SizedStack<UndoableItem> undoStack;
    SizedStack<UndoableItem> redoStack;
    LayersController lc = new LayersController();

    /**
     * Constructor limits size to 25
     */
    public UndoController() {
        undoStack = new SizedStack<>(25);
        redoStack = new SizedStack<>(25);
    }

    /**
     * Add undoable item to undo stack
     *
     * @param udi Item to be added
     */
    public void addItemtoUndo(UndoableItem udi) {
        undoStack.push(udi);
    }

    /**
     * Add undoable item to redo stack
     *
     * @param udi item to be added
     */
    public void addItemtoRedo(UndoableItem udi) {
        redoStack.push(udi);
    }

    /**
     * Undo process
     *
     * @param canvas canvas to draw/delete/move/resize the popped item in
     */
    public boolean undoProcess(PersistentCanvas canvas) {
        ArrayList<UndoableItem> toUndo = this.undoFunction();
        boolean toret = false;
        if (toUndo != null) {
            if (toUndo.get(0).isLayer()) {

                if (toUndo.get(0).getActionType() == 0) {
                    lc.setActiveLayer(toUndo.get(0).getLayer(), false);
                    toUndo.get(0).getLayer().setDeleted(true);
                    for (PathItem pi : toUndo.get(0).getLayer().getDrawn()) {
                        pi.setHiddenWithLayer(true);
                        canvas.removeItem(pi);
                    }
                } else if (toUndo.get(0).getActionType() == 1) {
                    lc.setActiveLayer(toUndo.get(0).getLayer(), true);
                    toUndo.get(0).getLayer().setDeleted(false);
                    for (PathItem pi : toUndo.get(0).getLayer().getDrawn()) {

                        pi.setHiddenWithLayer(false);
                        if (!pi.isHidden()) {

                            canvas.addItem(pi);
                        }
                    }
                }

            } else {
                if (toUndo.get(0).getActionType() == 1) {
                    for (UndoableItem ui : toUndo) {
                        if (ui.getDitem() instanceof PathItem && (!((PathItem) ui.getDitem()).getLayer().isDeleted())) {
                            if (!((PathItem) ui.getDitem()).isHiddenWithLayer()) {
                                canvas.addItem(ui.getDitem());
                            }

                            ((PathItem) ui.getDitem()).setHidden(false);

                        } else {
                            canvas.addItem(ui.getDitem());
                        }
                    }
                    return false;
                } else if (toUndo.get(0).getActionType() == 0) {
                    for (UndoableItem ui : toUndo) {
                        canvas.removeItem(ui.getDitem());
                        if (ui.getDitem() instanceof PathItem) {
                            ((PathItem) ui.getDitem()).setHidden(true);

                        } else {
                            toret = true;
                        }
                    }
                } else if (toUndo.get(0).getActionType() == 3) {
                    for (UndoableItem ui : toUndo) {
                        if (ui.getDitem() instanceof Panel) {
                            Panel temp = (Panel) ui.getDitem();
                            temp.moveAnchor(toUndo.get(0).getInitialP().x, toUndo.get(0).getInitialP().y, toUndo.get(0).getX(), toUndo.get(0).getY());
                        }
                    }
                } else {
                    for (UndoableItem ui : toUndo) {
                        ui.getDitem().move(toUndo.get(0).getX(), toUndo.get(0).getY());
                    }
                }
            }
        }
        return toret;
    }

    /**
     * Redo process
     *
     * @param canvas canvas to draw/delete/move/resize the popped item in
     */
    public boolean redoProcess(PersistentCanvas canvas) {
        ArrayList<UndoableItem> toUndo = this.redoFunction();
        boolean toret = false;

        if (toUndo != null) {
            if (toUndo.get(0).isLayer()) {

                if (toUndo.get(0).getActionType() == 0) {
                    lc.setActiveLayer(toUndo.get(0).getLayer(), true);
                    toUndo.get(0).getLayer().setDeleted(false);
                    for (PathItem pi : toUndo.get(0).getLayer().getDrawn()) {
                        pi.setHiddenWithLayer(false);
                        if (!pi.isHidden()) {
                            canvas.addItem(pi);
                        }
                    }
                } else if (toUndo.get(0).getActionType() == 1) {
                    lc.setActiveLayer(toUndo.get(0).getLayer(), false);
                    toUndo.get(0).getLayer().setDeleted(true);
                    for (PathItem pi : toUndo.get(0).getLayer().getDrawn()) {
                        pi.setHiddenWithLayer(true);
                        canvas.removeItem(pi);
                    }
                }

            } else {
                if (toUndo.get(0).getActionType() == 0) {
                    for (UndoableItem ui : toUndo) {
                        if (ui.getDitem() instanceof PathItem && (!((PathItem) ui.getDitem()).getLayer().isDeleted())) {
                            if (!((PathItem) ui.getDitem()).isHiddenWithLayer()) {
                                canvas.addItem(ui.getDitem());
                            }
                            ((PathItem) ui.getDitem()).setHidden(false);
                        } else {
                            canvas.addItem(ui.getDitem());
                        }
                    }

                } else if (toUndo.get(0).getActionType() == 1) {
                    for (UndoableItem ui : toUndo) {
                        canvas.removeItem(ui.getDitem());
                        if (ui.getDitem() instanceof PathItem) {

                            ((PathItem) ui.getDitem()).setHidden(true);

                        } else {
                            toret = true;
                        }
                    }
                } else if (toUndo.get(0).getActionType() == 3) {
                    for (UndoableItem ui : toUndo) {
                        Panel temp = (Panel) ui.getDitem();
                        (temp).resize(toUndo.get(0).getX() * -1, toUndo.get(0).getY() * -1);
                    }
                } else {
                    for (UndoableItem ui : toUndo) {
                        ui.getDitem().move(toUndo.get(0).getX() * -1, toUndo.get(0).getY() * -1);
                    }
                }
            }
        }
        return toret;
    }

    /**
     * Saves the resize operation to undo, as it keeps track of the original
     * size of the object, and ignores the dragging in between
     *
     * @param selection Item to be saved
     */
    public void saveResizeToUndo(DrawableItem selection) {
        UndoableItem ud = new UndoableItem(selection, 3);
        Panel temp = (Panel) selection;
        int x = ((Panel) selection).getInitialResizePoint().x;
        int y = ((Panel) selection).getInitialResizePoint().y;
        int width = ((Panel) selection).getInitialWidth() - ((Rectangle) temp.getShape()).width;
        int height = ((Panel) selection).getInitialHeight() - ((Rectangle) temp.getShape()).height;
        ud.setX(width);
        ud.setY(height);
        ud.setInitialP(new Point(x, y));
        this.addItemtoUndo(ud);
        ((Panel) selection).setInitialWidth(((Rectangle) temp.getShape()).width);
        ((Panel) selection).setInitialHeight(((Rectangle) temp.getShape()).height);
        ((Panel) selection).setInitialResizePoint(new Point(((Rectangle) temp.getShape()).x, ((Rectangle) temp.getShape()).y));

    }

    /**
     * Saves the move to the undo, ignoring all the positions in between the
     * click and raise
     *
     * @param selection item to be saved
     */
    public void saveMoveToUndo(DrawableItem selection) {
        UndoableItem ud = new UndoableItem(selection, 2);
        Panel temp = (Panel) selection;
        int dx = ((Panel) selection).getInitialPoint().x - ((Rectangle) temp.getShape()).x;
        int dy = ((Panel) selection).getInitialPoint().y - ((Rectangle) temp.getShape()).y;
        ud.setX(dx);
        ud.setY(dy);
        this.addItemtoUndo(ud);
        ((Panel) selection).setInitialPoint(new Point(((Rectangle) temp.getShape()).x, ((Rectangle) temp.getShape()).y));
    }

    /**
     * Undo function that gets the items to be undone
     *
     * @return Item that needs undoing
     */
    public ArrayList<UndoableItem> undoFunction() {
        ArrayList<UndoableItem> toReturn = new ArrayList<>();
        if (undoStack.isEmpty()) {
            return null;
        } else {
            UndoableItem cur = (UndoableItem) this.undoStack.pop();

            this.redoStack.push(cur);
            toReturn.add(cur);
            if (cur.getDitem() instanceof Panel) {
                for (Layer l : ((Panel) cur.getDitem()).getLayers()) {
                    for (PathItem pi : l.getDrawn()) {
                        if (!pi.isHidden()) {
                            toReturn.add(new UndoableItem(pi, cur.getActionType()));
                        }
                    }
                }
            }
            return toReturn;
        }

    }

    /**
     * Redo function that gets the items to be redone
     *
     * @return Item that needs redoing
     */
    public ArrayList<UndoableItem> redoFunction() {
        ArrayList<UndoableItem> toReturn = new ArrayList<>();

        if (redoStack.isEmpty()) {
            return null;
        } else {
            UndoableItem cur = (UndoableItem) this.redoStack.pop();

            this.undoStack.push(cur);
            toReturn.add(cur);
            if (cur.getDitem() instanceof Panel) {
                for (Layer l : ((Panel) cur.getDitem()).getLayers()) {
                    for (PathItem pi : l.getDrawn()) {
                        if (!pi.isHidden()) {
                            toReturn.add(new UndoableItem(pi, cur.getActionType()));
                        }
                    }
                }
            }
            return toReturn;
        }

    }
}
