/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Agents;

import Reseau.Arc;
import Reseau.Carrefour;
import Reseau.ListeArrets;
import Reseau.Reseau;
import Reseau.ListeArcs_ListModel;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dihya
 */
public class AgentVehiculeGps extends Agent {

	String id;
    int codArcDestination;
    int codArcDepart;
    int index_Arc_Actuel;
    String etat = "si";        // "si": dans la situation initiale, "ed": en deplacement.
    Arc arcActuel; // l'arc en cours 
    int position; // Position sur l'arc 
    int pas_simul = 1000;      // pas de simulation, 1000 ms par défaut.
    int Vitesse_max = 50;      // vitesse maximum.
    int Vitesse_actuel = 40;
    Arc arcDestination;
    /*Arc arcdes;*/
    BusGui mygui;
    Reseau R;
    ListeArcs_ListModel listearc;
    long date;
    VehGpsGui affgui;
    /**
     * *************************protocoles**********
     */
    String ProtoCmd = "commandes";
    String ProtoRecherche="chemin";
    String ProtoReponse = "reponse";
    protected void setup() {
       // codArcDepart = 0;
        //codArcDestination = 5;

       codArcDepart = (int) this.getArguments()[1];
        codArcDestination= (int) this.getArguments()[2];
        index_Arc_Actuel = 0;
        id="voiture";
        R=(Reseau) this.getArguments()[0]; // c'est bon
        this.listearc=new ListeArcs_ListModel(R.ListeArcs);
        this.arcActuel=R.Rech_Arc(codArcDepart);
        this.arcDestination=R.Rech_Arc(codArcDestination);
        this.affgui=new VehGpsGui(this);
        this.affgui.setVisible(true);
        /*this.arcdes=R.Rech_Arc(codArcDestination);*/
        /*Vector<Arc> LARC=new Vector<Arc>();
        for(int j=0; j<listedesarcs.size();j++){
            LARC.addElement(R.Rech_Arc(listedesarcs.elementAt(j).code_arc));
        }
       listedesarcs=LARC;  */
         //this.arc_actuel = this.listearc.rechercheArcCode(codArcDepart);
        this.position = 0;
        //arcdes = null;
        ////////////////////// Ajout des comportements de l'agent Vehicule GPS //////////////   
        System.out.format("Arc actuel :%d\n"+"Arc Destination: %d\n",this.arcActuel.code_arc, this.arcDestination.code_arc);
        this.addBehaviour(new AgentVehiculeGps.commandes(this));
        this.Demarrer();
    }

    public void Demarrer() {
        this.date = System.currentTimeMillis();
        this.addBehaviour(new ComportementAgentVehiculeGps(this));
    }
     private void Envoyer_demandeRech(){
        boolean envoyer=false;

 
        if(this.arcActuel.Des!=null){
           
           envoyer = true;
            if(envoyer){

              Arc a2=this.arcActuel;
              Arc a1=this.arcDestination;
             // int a2=this.L_arcs.elementAt(this.index_arc_actuel+1).code_arc;
              Object[] obj={this.getLocalName(),a1,a2,false};
            	//int a1=this.arcActuel.code_arc;
            	//int a2=this.arcDestination.code_arc;
              System.out.format("message envoyé : %s : %d to %d",this.getLocalName(),a1.code_arc,a2.code_arc );
             // Object[] obj={this.getLocalName(),a1,a2};
              Carrefour c=this.arcActuel.Des;
              ACLMessage msg=new ACLMessage(ACLMessage.REQUEST);
            //  ACLMessage msg=new ACLMessage(ACLMessage.INFORM);
              msg.addReceiver(c.agent_carrefour);
              msg.setProtocol(this.ProtoRecherche);
              try {
                msg.setContentObject(obj);
                this.send(msg);
                System.out.println("Le message est envoyé");

              } catch (IOException ex) {
                 Logger.getLogger(AgentVehiculeGps.class.getName()).log(Level.SEVERE, null, ex);
              }
            }else{
                System.out.println("Le message n'est pas envoye");
            }
            
        }
    }
     public Vector<Arc> reponseReche()
     {
     Vector<Arc> path = new Vector<Arc>() ;
     MessageTemplate template = MessageTemplate.MatchProtocol(this.ProtoReponse);
     ACLMessage msg;
      Object[] obj;
      msg = this.blockingReceive(template);
      //msg=this.blockingReceive();
            if(msg!=null){                
                try {
                    System.out.format("message received\n");
                    obj = (Object[]) msg.getContentObject();
                    System.out.println(obj);
                    path = (Vector<Arc>)( obj[0]);
                   //System.exit(0);
                }catch (Exception ex) {
                    System.out.println(ex);}                
            }
     return path;
     }

	class commandes extends CyclicBehaviour {

        AgentVehiculeGps AVG;
        MessageTemplate template;
        ACLMessage msg;
        String s;

        commandes(AgentVehiculeGps avg) {
            this.AVG = avg;
            template = MessageTemplate.MatchProtocol(avg.ProtoCmd);
        }

        @Override
        public void action() {
            msg = myAgent.receive(template);
            if (msg != null) {
                s = msg.getContent();
                if (s.equals("arreter")) {
                    ACLMessage reply = msg.createReply();
                    reply.setContent("ok");
                    myAgent.send(reply);
                    ////System.out.println("Agent Bus "+myAgent.getLocalName()+" arrêté !!");
                    myAgent.doDelete();
                }
                if (s.equals("demarrer")) {
                    AVG.Demarrer();
                }
                if (s.equals("show")) {
                    AVG.affgui = new VehGpsGui(AVG);
                }
            } else {
                this.block();
            }
        }
    }

    class ComportementAgentVehiculeGps extends CyclicBehaviour {

        AgentVehiculeGps AVG;
        int etape;
        int pos;
        Arc arcActuel;
        Arc arcDestination;
        Arc arcSuivant;
        ComportementAgentVehiculeGps(AgentVehiculeGps avg) {
            this.AVG = avg;
            etape = 0;
            pos = 0;
            arcActuel = AVG.arcActuel ; 
            arcActuel.nbre_veh++;
            arcDestination=AVG.arcDestination;
            arcSuivant=arcActuel.suivant();
            //arcSuivant=arcActuel.suivant();
            /*arcActuel.nbre_bus++;*/
        }

        public void action() {

        	AVG.Envoyer_demandeRech();
        	Vector<Arc> path = reponseReche();
        	int path_step = 1;
        	
        	while(arcActuel.code_arc!=arcDestination.code_arc){
        		int t = 36 * AVG.pas_simul / AVG.Vitesse_actuel;

                try {
                    Thread.sleep(t);
                } catch (InterruptedException ex) {
                    Logger.getLogger(AgentBus.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            	try {
                    FileWriter fichier = new FileWriter(new File("logGpsCyril28012015_autre_route_200.csv"), true);
                    long tempsParcour =  System.currentTimeMillis()-date ;
                    fichier.write(AVG.getName() + ";" + arcActuel.code_arc + ";" + arcDestination.code_arc + ";" + pos + ";" + etape + ";"+arcActuel.nbre_veh+";"+tempsParcour+"\n");//+";"+AVG.date+"\n");
                    fichier.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
        		System.out.format("Arc actuel :%d",arcActuel.code_arc);
        		//System.exit(0);
            switch (etape) {
                case 0: {// changement d'arc 
                   if(arcSuivant.occuper(0,'g')){
                	arcActuel.liberer(pos,'g');
                	arcActuel = arcSuivant; 
                	//arcSuivant=arcActuel.suivant();
                	System.out.format("Arc changé à : %d",arcActuel.code_arc);
                	//System.out.format("Arc suivant : %d",arcSuivant.code_arc);
                    pos=0;
                    //AVG.index_Arc_Actuel ++;
                    //  AB.index_arc_actuel++;
                    AVG.arcActuel=arcActuel;

                    AVG.Vitesse_actuel=arcActuel.vitesse_moyenne; 
                }
                   if(pos==arcActuel.nbre_cel-1)
                   	etape=2;
                   else    
                   	etape=1;
                   
                }break;
                case 1: {
                    if(arcActuel.occuper(pos+1, 'g')){
                        arcActuel.liberer(pos, 'g');
                        pos++;
                    }               
                    
                    if(pos==arcActuel.nbre_cel-1)
                    	etape=2;
                    else    
                    	etape=1;
                }
                break;
                case  2:{
                	 arcSuivant = path.get(path_step);
                	 path_step ++;		
		             System.out.println("reponse depuis gps ARc " + arcSuivant);
		
		             etape=0;

                }
                break;
            }

        	//System.out.format("Etape :%d\n\n\n ", etape);
        }
            if(arcActuel.code_arc==arcDestination.code_arc){
        		arcActuel.liberer(pos,'g');
        		arcActuel.liberer(pos, 'g');
        		AVG.doDelete();
//            	  System.out.format("Arrivé!!!");
//            	  System.exit(0);
              }
        }
    }
}
