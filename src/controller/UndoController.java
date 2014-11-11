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
import java.util.Stack;
import models.DrawableItem;
import models.Layer;
import models.Panel;
import models.PathItem;
import models.UndoableItem;

/**
 *
 * @author Osman
 */
public class UndoController {

    Stack<UndoableItem> undoStack;
    Stack<UndoableItem> redoStack;

    public UndoController() {
        undoStack = new Stack<>();
        redoStack = new Stack<>();
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

                        canvas.addItem(ui.getDitem());
                    }
                } else if (toUndo.get(0).getActionType() == 0) {
                    for (UndoableItem ui : toUndo) {
                        canvas.removeItem(ui.getDitem());
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

                        canvas.addItem(ui.getDitem());
                    }

                } else if (toUndo.get(0).getActionType() == 1) {
                    for (UndoableItem ui : toUndo) {
                        canvas.removeItem(ui.getDitem());
                    }
                } else {
                    for (UndoableItem ui : toUndo) {
                        ui.getDitem().move(toUndo.get(0).getX() * -1, toUndo.get(0).getY() * -1);
                    }
                }
            }
        }
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
                        toReturn.add(new UndoableItem(pi, cur.getActionType()));
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
                        toReturn.add(new UndoableItem(pi, cur.getActionType()));
                    }
                }
            }
            return toReturn;
        }

    }
}
