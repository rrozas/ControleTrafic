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
public class Ligne implements Serializable{
        public ListeArrets L_Arrets;
        public Vector<String> liste_bus;
        public Vector<Arc> listedesarcs;
        public int frequence;
        public String Cod_ligne;
        public Ligne(){
            this.liste_bus=new Vector<String>();
            this.L_Arrets=new ListeArrets();
            this.listedesarcs=new Vector<Arc>();
        }
}