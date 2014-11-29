/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import models.DrawableItem;
import models.Layer;
import models.Panel;

/**
 *
 * @author Osman
 */
public class SavingController {
    
    public boolean save(ArrayList<DrawableItem> drawings, String name) {
        try {
            FileOutputStream fos = new FileOutputStream(name);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            
            for (DrawableItem di : drawings) {
                oos.writeObject(di);
                if (di instanceof Panel) {
                    for (Layer l : ((Panel) di).getLayers()) {
                        oos.writeObject(l);
                    }
                }
            }
            
            oos.close();
            fos.close();
        } catch (IOException ex) {
            return false;
        }
        return true;
        
    }
    
    public ArrayList<DrawableItem> load(String path) {
        ArrayList<DrawableItem> toRet = new ArrayList<>();
        try {
            FileInputStream fis = new FileInputStream(new File(path));
            ObjectInputStream ois = new ObjectInputStream(fis);
            this.games = (HashMap<Game,GameStatus>)ois.readObject();
            this.bookedTickets = (HashSet<Ticket>)ois.readObject();
                this.baggage = (HashMap<Ticket,ArrayList<Object>>)ois.readObject();
            ois.close();
            fis.close();
        } catch (IOException e) {
            return false;
        } catch (ClassNotFoundException e) {
            return false;
        }
        return true;
    }
    
}
