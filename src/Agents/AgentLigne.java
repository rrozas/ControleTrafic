/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Agents;

import Reseau.Arc;
import Reseau.Arret;
import Reseau.FrequencesPassageBus.InfoFreqLigne;
import Reseau.Ligne;
import Reseau.ListeArrets;
import Reseau.Reseau;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import java.io.IOException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dihya
 */
public class AgentLigne extends Agent{
    
    ///////////// Donnée statiques ///////////////////
    
    ListeArrets L_Arrets;       // la liste des arrets de la ligne.
    Vector<AID> liste_bus;      // la liste des bus de la ligne (leur Identifiants).
    Vector<String> ListeNomBus;
    Vector<Arc> listedesarcs;   // la liste des arcs par lesquels passent les bus de la ligne
    int frequence=10*60;         // frequence d'injection des bus sur la ligne; en secondes
    final int attente_max=2*60*1000;
    InfoFreqLigne Frequences;
    Ligne ligne;
    ///////////// Donnée dynamique ///////////////////

    //--------------------protocoles supportes par l'agent ------------------//
    String ProtoArret="Arret";
    String ProtoCmd="commandes";
    String ProtoInscription="Inscription";
    //-----------------------------------------------------------------------//
    Vector<AID> liste_bus_arret;
    Injection_Bus Injection;
    int pas_simul=1000;
    ALGui MyGui;
    Reseau R;
    AgentContainer ac;


    @Override
    protected void setup(){
        /////////////////////// init  /////////////////////
        R=(Reseau) this.getArguments()[0]; // c'est bon
        this.Frequences=(InfoFreqLigne) this.getArguments()[1];// c'est bon 
        this.liste_bus=new Vector<AID>(); // c'est bon 
        Ligne L=(Ligne) this.getArguments()[2];// c'est bon 
        this.ligne=L; // c'est bon 
        this.L_Arrets=L.L_Arrets; // c'est bon 
        this.listedesarcs=(Vector<Arc>) L.listedesarcs; // c'est bon 
        this.ListeNomBus=(Vector<String>) L.liste_bus; // c'est bon 
        this.frequence=L.frequence; // c'est bon 
        String nombus; // c'est bon 
        for(int i=0; i<this.ListeNomBus.size();i++){
            nombus=this.ListeNomBus.elementAt(i);
            this.Create_Bus(nombus,'a');
        } // boucle de creation de bus  //c'est bon 
        /////

        this.MyGui=new ALGui(this);
        this.MyGui.setVisible(false);
        this.ac=this.getContainerController();
        /////////////////Ajout des comportements initiaux/////////////////
        this.addBehaviour(new commandes(this));
        this.addBehaviour(new ArretBus(this));
        this.demarrer_ligne();

    }
    //*************************** Méthodes de l'agent ligne *******************//
    // demarre la ligne
    public void demarrer_ligne(){
        if(this.Injection!=null){
            this.removeBehaviour(Injection);
            this.Injection=null;
        }
        this.liste_bus_arret=(Vector<AID>) this.liste_bus.clone();
        this.Injection=new Injection_Bus(this);
        this.addBehaviour(Injection);
    }
    ///arrete la ligne (tout bus retournant à la station initiale ne sera plus reinjecte
    public void arreter(){
        this.removeBehaviour(Injection);
        this.Injection=null;
    }
    ///creer un nouveau bus sur la ligne
    public AID Create_Bus(String cod_bus, char type){ // cette methode est a garder enb totalité
        Object[] args={this.L_Arrets,this.listedesarcs, this.getAID(), this.R};
        AID aid=null;
        AgentController myagent;
        try {
            myagent = ac.createNewAgent(cod_bus, AgentBus.class.getName(), args);
            myagent.start();
            MessageTemplate mt=MessageTemplate.MatchProtocol(this.ProtoInscription);
            ACLMessage reply=this.blockingReceive(mt,40000);
            if((reply!=null)){
                aid=reply.getSender();
                this.liste_bus.addElement(reply.getSender());
                if(type!='a'){
                    //this.ListeNomBus.add(cod_bus);
                }
            }
        } catch (StaleProxyException ex) {
            ////System.out.println(ex.toString());
        }
        return aid;
    }
    ///supprime un bus de la ligne
    void delete_bus(int index) {
        AID aid= this.liste_bus.get(index);
        ACLMessage msg=new ACLMessage(ACLMessage.REQUEST);
        msg.setProtocol(this.ProtoCmd);
        msg.setContent("arreter");
        msg.addReceiver(aid);
        this.send(msg);
        MessageTemplate mt=MessageTemplate.MatchProtocol(this.ProtoCmd);
        ACLMessage reply=this.blockingReceive(mt,40000);
        if(reply!=null){
            this.liste_bus_arret.removeElement(this.liste_bus.remove(index));
        }
    }
    // exécute avant l'arret de l'agent
    @Override
    protected void takeDown() {
        ACLMessage msg=new ACLMessage(ACLMessage.REQUEST);
        msg.setProtocol(ProtoCmd);
        msg.setContent("arreter");
        for (int i=0; i<this.liste_bus.size(); i++){
            msg.addReceiver(this.liste_bus.elementAt(i));
        }
        this.send(msg);
    }
//************************** Fin méthodes ************************************//

///////////////////// Classes utilisées par l'agent bus //////////////////
    class commandes extends CyclicBehaviour{
        MessageTemplate template;
        ACLMessage msg;
        String s;
        AgentLigne AL;
        commandes(AgentLigne al){
           this.AL=al;
           template=MessageTemplate.MatchProtocol(AL.ProtoCmd);
        }

        @Override
        public void action() {
            msg = AL.receive(template);
            
            if(msg!=null){
                s=msg.getContent();
                if(s.equals("arreter")){
                    System.out.println("l'envoie est fait par "+msg.getSender().getLocalName());
                    ////System.out.println("Agent ligne "+myAgent.getLocalName()+" arrêté !!");
                    myAgent.doDelete();
                }
                if(s.equals("show")){
                    if(this.AL.MyGui!=null){
                        AL.MyGui.setVisible(true);
                    }else{
                        this.AL.MyGui=new ALGui(AL);
                    }
                    
                }
            }else{
                this.block();
            }
        }
    }
    ////////// reception des renseignement sur l'arret des bus
    class ArretBus extends CyclicBehaviour{
        MessageTemplate template;
        ACLMessage msg;
        String s;
        AgentLigne AL;
        ///// var
        int[] contenu;
        int index;
        int duree;
        int ancienne_duree;
        long date_demarrage;
        ArretBus(AgentLigne al){
            this.AL=al;
            this.date_demarrage=System.currentTimeMillis();
            template=MessageTemplate.MatchProtocol(AL.ProtoArret); // filtre de reception
        }

        @Override
        public void action() {
            msg = AL.receive(template);
            if(msg!=null){
                try {
                    contenu = (int[]) msg.getContentObject();
                    index=contenu[0];
                    duree=contenu[1];
                    ancienne_duree=AL.L_Arrets.get(index).duree;
                    Arret a=this.AL.L_Arrets.get(index);
                    a.duree=duree;
                    if(a.date_arrive_dernier>0){
                        int f=(int) ((System.currentTimeMillis() - a.date_arrive_dernier) / 1000);
                        this.AL.Frequences.Ajouter_Freq((int) ((System.currentTimeMillis() - this.date_demarrage)/1000), f);
                    }else{
                        a.date_arrive_dernier=System.currentTimeMillis();
                    }
                    if(index<AL.L_Arrets.size()-1){  // le bus n'est pas arrive a la station finale
                        ACLMessage reply=msg.createReply();  // la reponse.
                        contenu=new int[2];

                        if(duree<=ancienne_duree){  // si le bus est en avance
                            contenu[0]=0;
                            contenu[1]=Math.min(ancienne_duree-duree, this.AL.attente_max);
                        }else{                    // si le bus est en retard
                            int t=duree-ancienne_duree;
                            if(t>=0 && t<0.1*ancienne_duree){
                                contenu[0]=1;
                            }
                            if(t>=0.1*ancienne_duree && t<0.2*ancienne_duree){
                                contenu[0]=1;
                            }
                            if(t>=0.2*ancienne_duree && t<0.3*ancienne_duree){
                                contenu[0]=3;
                            }
                            if(t>=0.3*ancienne_duree && t<0.4*ancienne_duree){
                                contenu[0]=4;
                            }
                            if(t>=0.4*ancienne_duree){
                                contenu[0]=5;
                            }
                            contenu[1]=0;         // fonction du retard.
                        }
                        try {
                            reply.setContentObject(contenu);
                        }catch (IOException ex) {
                            Logger.getLogger(AgentLigne.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        this.AL.send(reply);
                    }else{
                        this.AL.liste_bus_arret.addElement(msg.getSender());
                    }
                    this.AL.MyGui.Raffraichir();
                } catch (UnreadableException ex) {
                    Logger.getLogger(AgentLigne.class.getName()).log(Level.SEVERE, null, ex);
                }
            }else{
                this.block();
            }
        }
    }
    /////// Injection d'un bus sur la ligne chaque "f" instatnts:
    class Injection_Bus extends CyclicBehaviour{
        long date_envoi_dernier_bus;
        AgentLigne AL;
        ACLMessage req;
        Injection_Bus(AgentLigne al){
            this.AL=al;
            this.date_envoi_dernier_bus=System.currentTimeMillis();
            req=new ACLMessage(ACLMessage.REQUEST);
            req.setProtocol(AL.ProtoCmd);
            req.setContent("demarrer");
            EnvoyerBus();
            
        }

        @Override
        public void action() {
            int t=(AL.pas_simul*AL.frequence)-
                    ((int) (System.currentTimeMillis() - this.date_envoi_dernier_bus));
            if(t<=0){
                this.EnvoyerBus();
            }else{

                this.block(t);
            }
        }

        private void EnvoyerBus() {
            AID aid_bus=this.AL.Create_Bus("AB "+String.valueOf(System.currentTimeMillis()),'a');
            req.clearAllReceiver();
            req.addReceiver(aid_bus);
            AL.send(req);
            this.date_envoi_dernier_bus=System.currentTimeMillis();
        }
    }
}
