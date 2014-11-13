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
 *
 * @author Osman
 */
public class UndoController {

    SizedStack<UndoableItem> undoStack;
    SizedStack<UndoableItem> redoStack;

    public UndoController() {
        undoStack = new SizedStack<>(25);
        redoStack = new SizedStack<>(25);
    }

    public void addItemtoUndo(UndoableItem udi) {
        undoStack.push(udi);
    }

    public void addItemtoRedo(UndoableItem udi) {
        redoStack.push(udi);
    }

    public void undoProcess(PersistentCanvas canvas) {
        ArrayList<UndoableItem> toUndo = this.undoFunction();
        if (toUndo != null) {
            if (toUndo.get(0).isLayer()) {
                toUndo.get(0).getLayer().setActive(false);
            } else {
                if (toUndo.get(0).getActionType() == 1) {
                    for (UndoableItem ui : toUndo) {
                        if (ui.getDitem() instanceof PathItem && ((PathItem) ui.getDitem()).getLayer().isActive()) {
                            canvas.addItem(ui.getDitem());

                            ((PathItem) ui.getDitem()).setHidden(false);

                        } else {
                            canvas.addItem(ui.getDitem());
                        }
                    }
                } else if (toUndo.get(0).getActionType() == 0) {
                    for (UndoableItem ui : toUndo) {
                        canvas.removeItem(ui.getDitem());
                        if (ui.getDitem() instanceof PathItem) {
                            ((PathItem) ui.getDitem()).setHidden(true);
                            boolean active = false;
                            for (PathItem pi : ((PathItem) ui.getDitem()).getLayer().getDrawn()) {
                                if (!pi.isHidden()) {
                                    active = true;
                                }
                            }

                            ((PathItem) ui.getDitem()).getLayer().setActive(active);
                        }
                    }
                } else if (toUndo.get(0).getActionType() == 3) {
                    for (UndoableItem ui : toUndo) {
                        if (ui.getDitem() instanceof Panel) {
                        Panel temp = (Panel) ui.getDitem();
                            (temp).resize(toUndo.get(0).getX(), toUndo.get(0).getY());
                        }
                    }
                } else {
                    for (UndoableItem ui : toUndo) {
                        ui.getDitem().move(toUndo.get(0).getX(), toUndo.get(0).getY());
                    }
                }
            }
        }
    }

    public void redoProcess(PersistentCanvas canvas) {
        ArrayList<UndoableItem> toUndo = this.redoFunction();
        if (toUndo != null) {
            if (toUndo.get(0).isLayer()) {
                toUndo.get(0).getLayer().setActive(false);
            } else {
                if (toUndo.get(0).getActionType() == 0) {
                    for (UndoableItem ui : toUndo) {
                        if (ui.getDitem() instanceof PathItem && ((PathItem) ui.getDitem()).getLayer().isActive()) {
                            canvas.addItem(ui.getDitem());

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
                            boolean active = false;
                            for (PathItem pi : ((PathItem) ui.getDitem()).getLayer().getDrawn()) {
                                if (!pi.isHidden()) {
                                    active = true;
                                }
                            }

                            ((PathItem) ui.getDitem()).getLayer().setActive(active);
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
    }

    public void saveResizeToUndo(DrawableItem selection) {
        UndoableItem ud = new UndoableItem(selection, 3);
        Panel temp = (Panel) selection;
        int width = ((Panel) selection).getInitialWidth() - ((Rectangle) temp.getShape()).width;
        int height = ((Panel) selection).getInitialHeight() - ((Rectangle) temp.getShape()).height;
        ud.setX(width);
        ud.setY(height);
        this.addItemtoUndo(ud);
        ((Panel) selection).setInitialWidth(((Rectangle) temp.getShape()).width);
        ((Panel) selection).setInitialHeight(((Rectangle) temp.getShape()).height);
    }

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
