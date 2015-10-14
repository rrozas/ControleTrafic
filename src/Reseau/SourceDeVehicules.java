/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Reseau;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author DIHYA
 */
public class SourceDeVehicules extends Thread implements Serializable{

    public int periode;
    public Arc arc;
    public String Code_src;
    private boolean cont;
     Reseau R;
    SourceDeVehicules(String cod,int p, Arc a, Reseau r){
        cont=true;
        this.R=r;
        this.Code_src=cod;
        this.arc=a;
        this.periode=p*1000;
        this.start();        /// démarrage du Thread (la source)
    }

    @Override
    public void run() {
            while(cont){
                if(this.arc.occuper(0,'v')){
                    new Vehicule(arc, R);
                    try {
                       SourceDeVehicules.sleep(periode);                       
                    } catch (InterruptedException ex) {
                       Logger.getLogger(SourceDeVehicules.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }else{
                    try {
                       SourceDeVehicules.sleep(500);
                    } catch (InterruptedException ex) {
                       Logger.getLogger(SourceDeVehicules.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
            }
    }
    // arreter la source
    public void arreter_source(){
        this.cont=false;
    }
    // arreter la source
    public void demarrer_source(){
        this.cont=true;
      // this.start();
    }

}
