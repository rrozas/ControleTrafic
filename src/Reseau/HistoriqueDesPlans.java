/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Reseau;

import java.io.Serializable;
import java.util.Date;
import java.util.Vector;

/**
 *
 * @author Dihya
 */
public class HistoriqueDesPlans implements Serializable{
    public String carrefour;
    public Vector<plan> ListeDesPlans;
    public HistoriqueDesPlans(String carr){
        this.ListeDesPlans=new Vector<plan>();
        this.carrefour=carr;
    }

    public void Ajouter_Plan(PlanDeFeux P){
        Date d;
        plan v=new plan();        
        for(int i=0; i<P.plan.size();i++){
            ElementPlan E=new ElementPlan();
            E.cod_phase=P.plan.elementAt(i).code_phase;
            d=new Date(P.plan.elementAt(i).date_d);
            String dd=d.getHours()+":"+d.getMinutes()+":"+d.getSeconds();
            E.date_deb=dd;
            d=new Date(P.plan.elementAt(i).date_f);
            String df=d.getHours()+":"+d.getMinutes()+":"+d.getSeconds();
            E.date_fin=df;
            int duree=(int) ((P.plan.elementAt(i).date_f - P.plan.elementAt(i).date_d)/1000);
            E.duree=Integer.toString(duree);
            v.Elements.addElement(E);

        }
        d=new Date(P.plan.firstElement().date_d);
        v.date_debut_plan=d.getHours()+":"+d.getMinutes()+":"+d.getSeconds();
        long d_deb=P.plan.firstElement().date_d;
        long d_fin=P.plan.lastElement().date_f;
        int cycle=(int) ((d_fin - d_deb) / 1000);
        v.taille_cycle=(Integer.toString(cycle));
        this.ListeDesPlans.addElement(v);
        if(this.ListeDesPlans.size()>=50){
            this.ListeDesPlans.removeElementAt(0);
        }
    }
    ////////////////////////////////////////:::::
    public class plan implements Serializable{
        public Vector<ElementPlan> Elements=new Vector<ElementPlan>();
        public String date_debut_plan;
        public String taille_cycle;
    }
    ////////////////////////////////////////////
    public class ElementPlan implements Serializable{
        public String cod_phase;
        public String date_deb;
        public String date_fin;
        public String duree;
    }

}
