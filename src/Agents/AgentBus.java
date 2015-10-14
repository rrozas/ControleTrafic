/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Agents;

import Reseau.Arc;
import Reseau.Arret;
import Reseau.Carrefour;
import Reseau.ListeArrets;
import Reseau.Reseau;
import Reseau.ListeArcs_ListModel;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dihya
 */
public class AgentBus extends Agent {

    ListeArrets L_Arrets;
    Vector<Arc> L_arcs;
    AID agent_ligne;
    Arret prochain_arret;
    Object[] objj;
    ACLMessage msgg;
    MessageTemplate templatee;
    int index_prochain_arret;
    int codArcDestination;
    int codArcDepart;
    int index_Arc_Actuel;
    long date_depart;
    String etat = "si";        // "si": dans la situation initiale, "ed": en deplacement.
    boolean firs;
    Arc arc_actuel; // l'arc en cours
    int position; // Position sur l'arc
    int pas_simul = 100;      // pas de simulation, 1000 ms par défaut.
    int Vitesse_max = 50;      // vitesse maximum.
    int Vitesse_actuel = 40;
    Arc arcdes;
    BusGui mygui;
    Reseau R;
    ListeArcs_ListModel listearc;
    long date;
    VehGps affgui;
    /**
     * *************************protocoles**********
     */
    String ProtoArret = "Arret";
    String ProtoCmd = "commandes";
    String ProtoRecherche = "chemin";
    String ProtoReponse = "reponse";
    String ProtoInscription = "Inscription";

    protected void setup() {
        L_Arrets = (ListeArrets) this.getArguments()[0];
        L_arcs = (Vector<Arc>) this.getArguments()[1];
        agent_ligne = (AID) this.getArguments()[2];
        arcdes = L_arcs.firstElement();
        this.prochain_arret = this.L_Arrets.get(1);
        codArcDestination = L_arcs.lastElement().code_arc;
        codArcDepart = L_arcs.firstElement().code_arc;
        index_Arc_Actuel = 0;
        index_prochain_arret = 1;
        R = (Reseau) this.getArguments()[3];
        templatee = MessageTemplate.MatchProtocol(this.ProtoReponse);
        this.arc_actuel = L_arcs.firstElement();
        //this.arc_actuel = this.listearc.rechercheArcCode(codArcDepart);
        //this.position = 0;
        this.position = this.L_Arrets.get(0).position;
        // arcdes = null;
        firs = true;
        Inscrire();
        ////////////////////// Ajout des comportements de l'agent Vehicule GPS //////////////
        this.addBehaviour(new AgentBus.commandes(this));

    }

    public void RetourStationInitiale(Behaviour b) {
        this.arc_actuel = this.L_arcs.get(0);
        this.prochain_arret = this.L_Arrets.get(1);
        this.index_Arc_Actuel = 0;
        this.index_prochain_arret = 1;
        this.position = this.L_Arrets.get(0).position;
        this.etat = "si";
        this.removeBehaviour(b);



        //
        this.doDelete();

    }

    private void Inscrire() {
        ACLMessage msg = new ACLMessage(ACLMessage.SUBSCRIBE);
        msg.addReceiver(agent_ligne);
        msg.setProtocol(this.ProtoInscription);
        this.send(msg);
    }

    public void Demarrer() {
        this.date = System.currentTimeMillis();
        this.addBehaviour(new ComportementAgentBus(this));
    }

    @SuppressWarnings("empty-statement")
    private void Envoyer_demandeRech() {
        boolean envoyer = false;
        if (this.arc_actuel.Des != null) {

            envoyer = true;
            if (envoyer) {

                int a1 = this.arc_actuel.code_arc;
                //     int a2=this.L_arcs.elementAt(this.index_arc_actuel+1).code_arc;
                if (firs) {

                    firs = false;
                }
                //      this.listearc.rechercheArcCode(codArcDestination);
                Object[] obj = {this.getLocalName(), this.L_arcs.lastElement(), arcdes, firs};
                Carrefour c = this.arc_actuel.Des;
                ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
                msg.addReceiver(c.agent_carrefour);
                msg.setProtocol(this.ProtoRecherche);
                try {
                    msg.setContentObject(obj);
                    this.send(msg);
                } catch (IOException ex) {
                    Logger.getLogger(AgentBus.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                System.out.println("Le message n'est pas envoye");
            }

        }
    }

    class commandes extends CyclicBehaviour {

        AgentBus AB;
        Object[] obj;
        MessageTemplate template;
        ACLMessage msg;
        String s;

        commandes(AgentBus ab) {
            this.AB = ab;

            template = MessageTemplate.MatchProtocol(ab.ProtoCmd);
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
                    AB.Demarrer();
                }
                if (s.equals("show")) {
                    AB.mygui = new BusGui(AB);
                }
            } else {
                this.block();
            }
        }
    }

    class ComportementAgentBus extends CyclicBehaviour {

        AgentBus AB;
        int etape;
        int pos;
        Arc arcActuel;
        Arret proch_arret;
        int t;

        ComportementAgentBus(AgentBus ab) {
            this.AB = ab;
            etape = 0;
            AB.date_depart = System.currentTimeMillis();
            pos = AB.position;
            proch_arret = AB.prochain_arret;
            // ACLMessage msg;
            arcActuel = AB.arc_actuel;
            t = 36 * AB.pas_simul / AB.Vitesse_actuel;
            arcActuel.occuper(pos, 'b');
           
//arcActuel.nbre_veh++;
//            arcActuel.nbre_bus++;
        }

        private void Informer_Arret(int index_prochain_arret, int d) {
            int[] cont = {index_prochain_arret, d};
            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
            try {
                msg.setContentObject(cont);
                msg.addReceiver(agent_ligne);
                msg.setProtocol(AB.ProtoArret);
                AB.send(msg);
            } catch (IOException ex) {
                Logger.getLogger(AgentBus.class.getName()).log(Level.SEVERE, null, ex);
            }
            //

            //
        }

        public void action() {
            t = 36 * AB.pas_simul / AB.Vitesse_actuel;
          
            try {
                FileWriter fichier = new FileWriter(new File("log.csv"), true);
                long tempsParcour =  System.currentTimeMillis()-date ;
                fichier.write(AB.getName() + ";" + arc_actuel + ";" + arc_actuel.code_arc + ";" + pos + ";" + etape + ";"+arc_actuel.nbre_bus+";"+arc_actuel.nbre_veh+";"+tempsParcour+";"+AB.date+"\n");
                fichier.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            // Debug
            //System.out.println("bus : " + AB.getName() + " arc :" + arc_actuel.code_arc + " position : " + pos);
            try {
                Thread.sleep(t);
            } catch (InterruptedException ex) {
                Logger.getLogger(AgentBus.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (arc_actuel.code_arc != AB.codArcDestination) {
                switch (etape) {
                    case 0: {
                        // Debug
                        // System.out.println("bus : " + AB.getName() + " Etape0");
                        pos++;
                        if (pos >= arcActuel.nbre_cel) {
                            etape = 3;
                        } else {
                            etape = 2;
                        }


                    }
                    break;
                    case 1: {// changement d'arc
                        if ((arcdes.occuper(0, 'b'))) {
                            // Debug
                            //System.out.println("bus : " + AB.getName() + " Etape1 : Chagement arc " + arcActuel.code_arc + "->" + arcdes.code_arc);
                            arcActuel.liberer(pos - 1, 'b');
                            arcActuel = arcdes;
                            pos = 0;
                            AB.index_Arc_Actuel = pos;
                            AB.arc_actuel = arcActuel;
                            //etape=5;
                            etape = 0;
                            AB.Vitesse_actuel = arcActuel.vitesse_moyenne;
                            /* long tempsParcourt =  System.currentTimeMillis()-date ;
                        // l'appel de la methode d'enregistrement des resultats dans un fichier texte
String affi = "\n Agent "+ this.AB.getLocalName()+" je viens d'etre affecté a l'arc : "+arcActuel.code_arc+ "le temps de parcourt est : "+tempsParcourt;
                             AB.ecrire("resultats.txt", affi);*/
                        }
                     }
                    break;
                    case 2: {
                        System.out.println("bus : " + AB.getName() + " Etape2 : Avancé dans arc " + arcActuel.code_arc);
                        if (arcActuel.occuper(pos, 'b')) {
                            arcActuel.liberer(pos - 1, 'b');
                            //etape = 5;
                            etape = 0;
                        }
                    }
                    break;

                    case 3: {
                        // Debug
                        // System.out.println("bus : " + AB.getName() + " Etape3 : demande recherche ");
                        AB.Envoyer_demandeRech();
                        etape = 4;
                    }
                    break;
                    case 4: {
                        // Debug
                        //System.out.println("bus : " + AB.getName() + " Etape4 : Attente message");
                        msgg = this.AB.receive(templatee);
                        if ((msgg != null)) {
                            try {
                                objj = (Object[]) msgg.getContentObject();
                                arcdes = (Arc) objj[0];
                                arcdes = R.Rech_Arc(arcdes.code_arc);
                                if (arcdes.nbre_bus==(arcdes.capacite-2))
                                {
                                    
                        {
                         
                        
                          /*long ta=1;
                           ta=(AB.date_depart)+1;
                           AB.date_depart=ta;
                           */
                         R.Ajouter_Retard(1, 'b');
                             
                         /*   catch (InterruptedException ex) {
                Logger.getLogger(AgentBus.class.getName()).log(Level.SEVERE, null, ex);
                        }*/
                          //  etape=0;

                    }
                                }
                                etape = 1;
                            } catch (Exception ex) {
                                System.err.println(ex);
                            }

                        } else {
                            this.block();

                        }
                    }
                    break;

                    /*     case 5: {
                     System.out.println("bus : "+AB.getName()+" Etape5");
                     AB.position=pos;
                     // AB.etat=etats[1]; //"ed"
                     if((proch_arret.arc==arcActuel) && (proch_arret.position==pos)){ // si arret:
                     // envoyer les info a l'agent ligne:
                     int d=(int) (System.currentTimeMillis() - AB.date_depart);

                     Informer_Arret(AB.index_prochain_arret,d);
                     if(AB.index_prochain_arret==(AB.L_Arrets.size()-1)){ // si dernier arret
                     arcActuel.liberer(pos,'b');
                     //arcActuel.nbre_veh--;
                     //arcActuel.nbre_bus--;
                     AB.RetourStationInitiale(this);
                     }

                     }


                     etape = 0;
                     }
                     break;
                     */

                }
            } else {
                // Suppression de l'agent si arrivé à destination
                arcActuel.liberer(0, 'b');
                AB.doDelete();
            }
            
        }
    }
}
