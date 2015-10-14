/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Reseau;

import java.io.Serializable;
import java.util.Vector;


public class ListeArrets extends Vector<Arret> implements Serializable{
   public void Ajouter_arret(String cod, Arc a, int pos){
       Arret ar=new Arret(cod, a, pos);
       this.addElement(ar);
   }
}
