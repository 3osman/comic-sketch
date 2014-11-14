/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

/**
 * Class for sized stack for undo/redo
 *
 * @author Osman
 */
import java.util.Stack;

public class SizedStack<T> extends Stack<T> {

    private int maxSize;

    public SizedStack(int size) {
        super();
        this.maxSize = size;
    }

    /**
     * Overrides the push function of stack to handle only maxSize items
     *
     * @param object Object to be added
     * @return object added
     */
    @Override
    public Object push(Object object) {
        //If the stack is too big, remove elements until it's the right size.
        while (this.size() > maxSize) {
            this.remove(0);
        }
        return super.push((T) object);
    }
}
