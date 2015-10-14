/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Reseau;

import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Dihya
 */
public class ListeArretsTabModel extends AbstractTableModel{
    ListeArrets LA;
    String[] titres={"Code Arret","Arc", "Position","Durée"}; // Noms des colonnes
    public ListeArretsTabModel(ListeArrets la){
        this.LA=la;
    }

    public int getRowCount() {
        return this.LA.size();
    }

    public int getColumnCount() {
        return this.titres.length;
    }

    public Object getValueAt(int row, int col) {            
            if(col==0) {return this.LA.get(row).cod_arret;}
            else if(col==1) {return this.LA.get(row).arc.code_arc;}
            else if(col==2) {return this.LA.get(row).position;}            
            else {return this.LA.get(row).duree;}
    }
    @Override
    public boolean isCellEditable(int row, int col) {
        return (false);
    }
    @Override
	public Class getColumnClass(int c) {return getValueAt(0,c).getClass();}
    @Override
	public String getColumnName(int col) {return titres[col];}


}
