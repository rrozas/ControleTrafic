package Reseau;

import java.util.Vector;

import javax.swing.table.AbstractTableModel;


@SuppressWarnings("serial")
public class ListeArcTabModel extends AbstractTableModel {
	
	Vector<Arc> LA;
	String[] titres={"Code arc","Longueur","Capacité"}; // Noms des colonnes
	public ListeArcTabModel(Vector<Arc> A, String titre){
		this.LA=A;
        this.titres[0]=titre;
	}
	@Override
	public int getColumnCount() {		
		return this.titres.length;
	}
	@Override
	public int getRowCount() {		
		return this.LA.size();
	}
	@Override
	public Object getValueAt(int row, int col) {
        Object res=null;
        switch(col){
            case 0:
		       res=Integer.valueOf(this.LA.elementAt(row).code_arc);
               break;
            case 1:
                res=Integer.valueOf(this.LA.elementAt(row).longueur);
                break;
            case 2:
                res=Integer.valueOf(this.LA.elementAt(row).capacite);
                break;
        }
        return res;
	}
    @Override
	public Class getColumnClass(int c) {return getValueAt(0,c).getClass();}
    @Override
	public String getColumnName(int col) {return titres[col];}
	
    @Override
	public boolean isCellEditable(int row, int col) {
        return false;
    }
    @Override
	public void setValueAt(Object value, int row, int col) {
		this.LA.get(row).code_arc=((Integer)value).intValue();
        this.fireTableRowsUpdated(row, row); 
    }
	

}
