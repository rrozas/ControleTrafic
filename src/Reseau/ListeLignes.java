/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Reseau;

import java.io.Serializable;
import java.util.Vector;

/**
 *
 * @author DIHYA
 */
public class ListeLignes implements Serializable {
    private  static  final  long serialVersionUID =  1350092881346723535L;
    public Vector<Ligne> ListeDesLigne;
    public ListeLignes(){
        this.ListeDesLigne=new Vector<Ligne>();
    }
    
}
