/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Reseau;

import java.util.Date;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Dihya
 */
public class DigrammePhases_TabModel extends AbstractTableModel {

    PlanDeFeux PF;
    String[] titres={"Code phase","Durée","Date debut","Date fin"}; // Noms des colonnes

    public DigrammePhases_TabModel(PlanDeFeux pf){
        this.PF=pf;
    }
    public int getRowCount() {
        return PF.plan.size();
    }

    public int getColumnCount() {
        return this.titres.length;
    }

    public Object getValueAt(int row, int col) {
        Object obj=null;
        if(col==0){obj=this.PF.plan.elementAt(row).code_phase;}
        if(col==1){obj=(PF.plan.elementAt(row).date_f-PF.plan.elementAt(row).date_d)/1000;}
        if(col==2){
            Date d=new Date(this.PF.plan.elementAt(row).date_d);
            obj=d.getHours()+":"+d.getMinutes()+":"+d.getSeconds();
        }
        if(col==3){
            Date d=new Date(this.PF.plan.elementAt(row).date_f);
            obj=d.getHours()+":"+d.getMinutes()+":"+d.getSeconds();                
        }
        if(col==4){obj=this.PF.plan.elementAt(row).etat;}
        return obj;
    }
    @Override
	public Class getColumnClass(int c) {return getValueAt(0,c).getClass();}
    @Override
	public String getColumnName(int col) {return titres[col];}

    public void Raffraichir(){
        this.fireTableDataChanged();
    }


}
