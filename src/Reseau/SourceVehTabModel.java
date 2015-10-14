/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Reseau;

import java.util.Vector;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author DIHYA
 */
public class SourceVehTabModel extends AbstractTableModel{

    String[] titres={"Code source", "Période", "Arc"}; // Noms des colonnes
    Vector<SourceDeVehicules> LS;

    public SourceVehTabModel(Vector<SourceDeVehicules> v){
        this.LS=v;
    }

    public int getRowCount() {
        return this.LS.size();
    }

    public int getColumnCount() {
        return this.titres.length;
    }

    public Object getValueAt(int row, int col) {
        Object obj=null;
        if(col==0){
            obj=this.LS.elementAt(row).Code_src;
        }
        if(col==1){
            obj=this.LS.elementAt(row).periode/1000;
        }
        if(col==2){
            obj=this.LS.elementAt(row).arc.code_arc;
        }
        return obj;
    }


    @Override
    public String getColumnName(int col) {return titres[col];}
	@Override
	public Class getColumnClass(int c) {return getValueAt(0,c).getClass();}
    @Override
	public boolean isCellEditable(int row, int col) {
        return (col==1);
   }
   @Override
	public void setValueAt(Object value, int row, int col) {
		this.LS.elementAt(row).periode=((Integer)value).intValue()*1000;
        this.fireTableRowsUpdated(row, row);
    }

}
