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
 * @author DIHYA
 */
public class VectorListModel extends AbstractListModel implements ListModel{

    Vector<String> ListeLignes;

    public VectorListModel(Vector<String> ll){
        this.ListeLignes=ll;
    }

    public int getSize() {
        return this.ListeLignes.size();
    }

    public Object getElementAt(int index) {
        return this.ListeLignes.elementAt(index);
    }
    public void raffraichir(){
        this.fireContentsChanged(this, 0, this.getSize()-1);
    }
    public void supp_element(int index){
        this.ListeLignes.removeElementAt(index);
        this.raffraichir();
    }

}
