/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Reseau;

import java.util.Vector;
import javax.swing.AbstractListModel;
import javax.swing.ListModel;

/**
 *
 * @author Dihya
 */
public class ListeArcs_ListModel extends AbstractListModel implements ListModel {

    Vector<Arc> liste_arc;

    public ListeArcs_ListModel(Vector<Arc> vect) {
        this.liste_arc = vect;
    }

    public int getSize() {
        return this.liste_arc.size();
    }

    public Object getElementAt(int index) {
        return this.liste_arc.get(index).code_arc;
    }

    public void raffraichir() {
        this.fireContentsChanged(this, 0, this.getSize() - 1);
    }

    public void supp_element(int index) {
        this.liste_arc.removeElementAt(index);
        this.raffraichir();
    }

    public Arc rechercheArcCode(int codArc) {
        Arc fina  = null ;
        System.out.println("recheche d'arcs");
        System.out.println("La taille de la liste d'ar est : " +liste_arc.size());
        for (int i = 0; i < this.liste_arc.size(); i++) {
            Arc a = liste_arc.elementAt(i);
           
            if (a.code_arc == codArc) {
                fina = a;
                System.out.println("arc touvé");
            }
            
        }
        return fina;
    }
}

