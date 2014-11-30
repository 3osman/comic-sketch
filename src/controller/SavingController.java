/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import models.DrawableItem;

/**
 *
 * @author Osman
 */
public class SavingController {

    public static final String EXTENSION = "comico";

    public boolean save(ArrayList<DrawableItem> drawings, String name) {
        //todo

        //open the file
        try {
            File file = new File(name + "." + EXTENSION);

            // if file doesn't exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            FileOutputStream saveFile = new FileOutputStream(file);
            ObjectOutputStream saveObject = new ObjectOutputStream(saveFile);

            for (DrawableItem item : drawings) {
                saveObject.writeObject(item);
            }
            saveObject.close();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;

    }

    public ArrayList<DrawableItem> load(String path) {
        //to-do
        ArrayList<DrawableItem> itemList = new ArrayList<>();

        try {
            FileInputStream inputFile = new FileInputStream(path);
            ObjectInputStream inputObject = new ObjectInputStream(inputFile);

            Object item = null;
            while ((item = inputObject.readObject()) != null) {
                if (item instanceof DrawableItem) {
                    itemList.add((DrawableItem) item);
                }
            }
        } catch (EOFException e) {

        } catch (Exception e) {
            e.printStackTrace();
        }
        return itemList;
    }

}
