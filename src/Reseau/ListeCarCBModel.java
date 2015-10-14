/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Reseau;


import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

/**
 *
 * @author Dihya
 */
public class ListeCarCBModel extends AbstractListModel implements ComboBoxModel{
    Reseau reseau;
    String sel_item;
    public ListeCarCBModel(Reseau R){
        this.reseau=R;        
        this.sel_item="Aucun";
    }

    public int getSize() {
        return this.reseau.ListeCarrefour.size()+1;
    }

    public Object getElementAt(int index) {
        if(index>0){
            return this.reseau.ListeCarrefour.get(index-1).code_carrefour;
        }else{
            return "Aucun";
        }
    }

    public void setSelectedItem(Object item) {
        this.sel_item=(String)item;
    }

    public Object getSelectedItem() {
        return this.sel_item;
    }

}
