/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Reseau;

import jade.core.AID;
import java.util.Vector;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Dihya
 */
public class ListeBusTabModel extends AbstractTableModel{
    Vector<AID> LB;
     String[] titres={"Liste des bus" }; // Noms des colonnes
    public ListeBusTabModel(Vector<AID> vect){
        this.LB=vect;
    }

    public int getRowCount() {
        return this.LB.size();
    }

    public int getColumnCount() {
        return this.titres.length;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        return this.LB.get(rowIndex).getLocalName();
    }
    @Override
	public Class getColumnClass(int c) {return getValueAt(0,c).getClass();}
    @Override
	public String getColumnName(int col) {return titres[col];}
	

}
