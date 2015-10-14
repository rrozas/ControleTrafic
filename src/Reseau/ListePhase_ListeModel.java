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
public class ListePhase_ListeModel extends AbstractListModel implements ListModel{

    Vector<Phase> Liste_Phases;
    public ListePhase_ListeModel(Vector<Phase> lp){
        this.Liste_Phases=lp;
    }
    public int getSize() {
        return this.Liste_Phases.size();
    }

    public Object getElementAt(int index) {
        return this.Liste_Phases.get(index).code_phase;
    }

    public void raffraichir(){
        this.fireContentsChanged(this, 0, this.getSize()-1);
    }
    public void supp_element(int index){
        this.Liste_Phases.removeElementAt(index);
        this.raffraichir();
    }

}
