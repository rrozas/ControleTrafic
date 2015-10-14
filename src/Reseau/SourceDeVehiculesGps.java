/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Reseau;

import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.UUID;

import Agents.AgentReseau;
import Agents.AgentVehiculeGps;

/**
 *
 * @author DIHYA
 */
public class SourceDeVehiculesGps extends Thread implements Serializable{

    public int periode;
    public Arc arc_dest;
    public Arc arc_depart;
    public int Code_src;
    public int Code_dest;
    private boolean cont;
     //AgentReseau AR;
    AgentContainer ac;
     AgentVehiculeGps AVG;
     public Reseau R;
     
     
    public SourceDeVehiculesGps(int cod,int p, int a, Reseau R, AgentContainer conteneur){
    	this.ac = conteneur;
        this.R=R;
        cont=true;
       
        this.Code_src=cod;
        this.Code_dest=a;
       // this.arc_dest=this.R.cfg.R.Rech_Arc(Code_dest);
        this.arc_dest= this.R.Rech_Arc(a);
        this.arc_depart=this.R.Rech_Arc(cod);
        this.periode=p*1000;
        this.start();        /// démarrage du Thread (la source)
    }


    public String generateUniqueId() {
    	UUID idOne = UUID.randomUUID();
    	String str=""+idOne;
    	int uid=str.hashCode();
    	
    	String filterStr=""+uid;
    	str=filterStr.replaceAll("-", "");
    	return (str);
    }
   
    @Override
    public void run() {
            while(cont){
                if(this.arc_depart.occuper(0,'v')){
                	
                    AgentController myagent;
                    
                    try {
                        String x = generateUniqueId();
                       Object[] argum={this.R,Code_src,Code_dest}; 
                        myagent= this.ac.createNewAgent(x, AgentVehiculeGps.class.getName(), argum);
						//myagent = this.AR.getContainerController().createNewAgent(generateUniqueId(), AgentVehiculeGps.class.getName(), args);
	                   
                        myagent.start();
	                    SourceDeVehiculesGps.sleep(periode); 
					} catch (StaleProxyException | InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace(); 
						Logger.getLogger(SourceDeVehiculesGps.class.getName()).log(Level.SEVERE, null, e);
					}
                }else{
                    try {
                       SourceDeVehiculesGps.sleep(500);
                    } catch (InterruptedException ex) {
                       Logger.getLogger(SourceDeVehiculesGps.class.getName()).log(Level.SEVERE, null, ex);
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
