/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Reseau;

import java.io.Serializable;
import java.util.Vector;

/**
 *
 * @author Dihya
 */
public class Configuration implements Serializable{
   public Configuration(){
        this.R=new Reseau();
        this.SIM=new Simul();
        this.ListeLignes=new Vector<LigneBus>();
    }
    public Reseau R;
    public Simul SIM;
    public Vector<LigneBus> ListeLignes;
}
