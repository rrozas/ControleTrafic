/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Reseau;

import java.util.Vector;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Dihya
 */
public class ListeArcs2TabModel extends AbstractTableModel{

    Vector<Arc> LA;
    String[] titres={"Liste des arcs"}; // Noms des colonnes

    public ListeArcs2TabModel(Vector<Arc> la){
        this.LA=la;
    }

    public int getRowCount() {
        return this.LA.size();
    }

    public int getColumnCount() {
        return this.titres.length;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        return this.LA.get(rowIndex).code_arc;
    }

    @Override
	public Class getColumnClass(int c) {return getValueAt(0,c).getClass();}
    @Override
	public String getColumnName(int col) {return titres[col];}



}
