/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

/**
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

    @Override
    public Object push(Object object) {
        //If the stack is too big, remove elements until it's the right size.
        while (this.size() > maxSize) {
            this.remove(0);
        }
        return super.push((T) object);
    }
}
