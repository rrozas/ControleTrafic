/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Reseau;

import jade.core.AID;
import java.io.Serializable;
import java.util.Vector;

/**
 *
 * @author DIHYA
 */
public class Phase implements Serializable{
    public String code_phase;
    public char etat_phase='r';
    public long date_debut=0;
    public long date_fin=0;
    public Vector<Arc> E;
    public Vector<Arc> S;
    public AID agent;
    public Phase(String cod){
        E=new Vector<Arc>();
        S=new Vector<Arc>();
        this.code_phase=cod;

    }
    public boolean Satisfait(int a1, int a2){
        boolean sat=false;
        int i=E.size()-1, j;
        Arc a;
        /// verifie que a1 est un arc entrant de la phase:
        while (! sat && (i>=0)){
            a=E.elementAt(i);
            if(a.code_arc==a1){
                j=0;
                while(j<a.suivants.size() && ! sat){
                    if(a.suivants.elementAt(j).a.code_arc==a2){
                        sat=true;
                    }else{
                        j++;
                    }
                }
                
            }else{
                i--;
            }
        }        
        return sat;
    }
    /// a modifier !!!!!!! volume envoyer vers un arc et non pas vers un carrefour !!
    public int VolumeEnvoyerA(int cod_arc){
        int v=0; Arc a = null, suiv;
        for (int i=0; i<this.E.size();i++){
            a=E.elementAt(i);            
            for(int j=0; j<a.suivants.size(); j++){
                suiv=a.suivants.elementAt(j).a;
                if(suiv.code_arc==cod_arc){
                    v=v+(a.nbre_veh*a.suivants.elementAt(j).pourcentage)/100;                   
                }
            }
        }        
        return v;
    }

}
