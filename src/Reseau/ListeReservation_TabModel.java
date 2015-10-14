/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Reseau;

import java.util.Date;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author DIHYA
 */
public class ListeReservation_TabModel extends AbstractTableModel{

    PlanDeFeux PF;
    String[] titres={"Bus","Phase","Date début","Date fin", "Priorité"}; // Noms des colonnes

    public ListeReservation_TabModel(PlanDeFeux pf){
        this.PF=pf;
    }
    public int getRowCount() {
        return this.PF.reservations.size();
    }

    public int getColumnCount() {
        return this.titres.length;
    }

    public Object getValueAt(int row, int col) {
        Object obj=null;
        if(col==0){obj=PF.reservations.elementAt(row).cod_bus;}
        if(col==1){obj=this.PF.reservations.elementAt(row).cod_phase;}
        if(col==2){
            Date d=new Date(this.PF.reservations.elementAt(row).dd);
            obj=d.getHours()+":"+d.getMinutes()+":"+d.getSeconds();
        }
        if(col==3){
            Date d=new Date(this.PF.reservations.elementAt(row).df);
            obj=d.getHours()+":"+d.getMinutes()+":"+d.getSeconds();
        }
        if(col==4){
            obj=PF.reservations.elementAt(row).priorite;
        }
        return obj;
    }
    @Override
	public Class getColumnClass(int c) {return getValueAt(0,c).getClass();}
    @Override
	public String getColumnName(int col) {return titres[col];}

}
