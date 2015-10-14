/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Reseau;

import javax.swing.table.AbstractTableModel;

/**
 *
 * @author DIHYA
 */
public class ListeSuivantTabModel extends AbstractTableModel{

    Arc arc;
    public ListeSuivantTabModel(Arc a){
        this.arc=a;
    }
    public int getRowCount() {
        return arc.suivants.size();

    }

    public int getColumnCount() {
        return 2;
    }

    public Object getValueAt(int row, int col) {
        if(col==0){
            return new Integer(arc.suivants.get(row).a.code_arc);
        }else{
            return new Integer(arc.suivants.get(row).pourcentage);
        }

    }
    @Override
    public Class getColumnClass(int c) {
        return getValueAt(0,c).getClass();
    }
    @Override
	public String getColumnName(int col) {
        if(col==0){
            return "Code arc";
        }else{
            return "Pourcentage";
        }
    }

    @Override
	public boolean isCellEditable(int row, int col) {
        return (col==1);
    }
    @Override
	public void setValueAt(Object value, int row, int col) {
		if(col==1){
            arc.suivants.get(row).pourcentage=((Integer)value).intValue();
            arc.Calculer_Proportions();
            this.fireTableDataChanged();
        }
    }

    public void raffraichir(){
        this.fireTableDataChanged();
    }

}
