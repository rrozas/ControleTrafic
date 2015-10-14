package Reseau;

import java.util.Vector;


import javax.swing.table.AbstractTableModel;


@SuppressWarnings("serial")
public class ListeCarrTabModel extends AbstractTableModel {
	Vector<Carrefour> LC;
	String[] titres={"Liste des carrefours"}; // Noms des colonnes
	
	public ListeCarrTabModel(Vector<Carrefour> V){
		this.LC=V;
	}
	@Override
	public int getColumnCount() { return this.titres.length; }
	@Override
	public int getRowCount() { return this.LC.size(); }
	@Override
	public Object getValueAt(int row, int col) {
		return this.LC.elementAt(row).code_carrefour;
                
	}
    @Override
	public String getColumnName(int col) {return titres[col];}	
	@Override
	public Class getColumnClass(int c) {return getValueAt(0,c).getClass();}
	
    @Override
	public boolean isCellEditable(int row, int col) {
        return false;
   }	
    @Override
	public void setValueAt(Object value, int row, int col) {
		this.LC.get(row).code_carrefour = ((Integer) value).intValue();
        this.fireTableRowsUpdated(row, row); 
    }
	

	

}
