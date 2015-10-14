/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Reseau;

import java.util.Vector;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

/**
 *
 * @author Dihya
 */
public class ListeArcsCBModel extends AbstractListModel implements ComboBoxModel{
    Vector<Arc> LA;
    String sel_item;
    public ListeArcsCBModel(Vector<Arc> la){
        this.LA=la;
        if(la.size()>0){
            this.sel_item=Integer.toString(la.get(0).code_arc);
        }
    }

    public int getSize() {
        return this.LA.size();
    }

    public Object getElementAt(int index) {
        return Integer.toString(this.LA.get(index).code_arc);
    }

    public void setSelectedItem(Object item) {
        this.sel_item=(String) item;
    }

    public Object getSelectedItem() {
        return this.sel_item;
    }

}
