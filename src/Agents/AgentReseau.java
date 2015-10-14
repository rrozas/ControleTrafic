/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Agents;

import Reseau.Arc;
import Reseau.Arret;
import Reseau.Carrefour;
import Reseau.Config;
import Reseau.FrequencesPassageBus;
import Reseau.GestionReseau;
import Reseau.HistoriqueDesPlans;
import Reseau.JF_HistoriquePlans;
import Reseau.Ligne;
import Reseau.ListeLignes;
import Reseau.SourceDeVehiculesGps;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author Dihya
 */
public class AgentReseau extends Agent{

    public Config cfg;
    GestionReseau gestReseau;
    public Vector<String> ListeLigne;
    final String ProtoCmd="commandes";
    public String chemin="Config";
    public FrequencesPassageBus Frequence_Passage;
    public ListeLignes Lignes;

    @Override
    protected void setup(){
        cfg=(Config) this.getArguments()[0];
        this.ListeLigne=new Vector<String>();
        this.gestReseau=new GestionReseau(this, cfg);
        //
        if(cfg.R.ListeArcs.size()>0){
            Carrefour c;
            for(int i=0; i<cfg.R.ListeCarrefour.size();i++){
                c=cfg.R.ListeCarrefour.elementAt(i);
                this.AjouterAgentCarrefour(this.gestReseau.Rech_Nom_Car(c));
            }
            for(int i=0; i<cfg.R.ListeArcs.size();i++){
                cfg.R.ListeArcs.elementAt(i).Initialiser();
            }
        }
        //
        this.Frequence_Passage=new FrequencesPassageBus();
        Initialiser_Liste_Lignes();
        Initialiser_Routes_Gps();
    }


    public void AjouterAgentCarrefour(int nom) {
        Object[] args={cfg.R.Rech_Carrefour(nom)};
        AgentController myagent;
        try {
            myagent = this.getContainerController().createNewAgent(Integer.toString(nom), AgentCarrefour.class.getName(), args);
            myagent.start();
        } catch (StaleProxyException ex) {
            ////System.out.println(ex.toString());
        }
    }
    public void afficher_historique(){
        JF_HistoriquePlans fen=new JF_HistoriquePlans(this);
        ACLMessage msg=new ACLMessage(ACLMessage.REQUEST);
        msg.setProtocol(this.ProtoCmd);
        msg.setContent("historique");
        int nbre=0;
        for (int i=0;i<this.cfg.R.ListeCarrefour.size(); i++){
            msg.addReceiver(this.cfg.R.ListeCarrefour.elementAt(i).agent_carrefour);
            nbre++;
        }
        this.send(msg);
        MessageTemplate template=MessageTemplate.MatchProtocol(ProtoCmd);
        HistoriqueDesPlans H;
        fen.writeln("_____________________________________________________________________________________________________________________________________________");
        while(nbre>0){
            msg=this.blockingReceive(template, 4000);
            try {
                H = (HistoriqueDesPlans) msg.getContentObject();
                nbre--;
                
                fen.writeln("Les Plan du carrefour "+ H.carrefour);
                fen.writeln("________________________________________________________________________________________________________________________");
                for(int j=0; j<H.ListeDesPlans.size(); j++){                    
                    fen.writeln("Plan "+Integer.toString(j+1));
                    fen.writeln("Commence à:"+H.ListeDesPlans.elementAt(j).date_debut_plan);
                    fen.writeln("Taille du cycle: "+H.ListeDesPlans.elementAt(j).taille_cycle);
                    fen.writeln("Durée des phases:");
                    for(int t=0; t<H.ListeDesPlans.elementAt(j).Elements.size();t++){
                        String cod=H.ListeDesPlans.elementAt(j).Elements.elementAt(t).cod_phase;
                        String dd=H.ListeDesPlans.elementAt(j).Elements.elementAt(t).date_deb;
                        String df= H.ListeDesPlans.elementAt(j).Elements.elementAt(t).date_fin;
                        String duree= H.ListeDesPlans.elementAt(j).Elements.elementAt(t).duree;
                        fen.writeln("             Phase "+cod+": Durée "+duree+" ; Debute à :"+dd+" ; Se termine à: "+df);
                    }
                    fen.writeln("====================================================================");
                }
                fen.writeln("__________________________________________________________________________________________________________________________");
            } catch (UnreadableException ex) {
                Logger.getLogger(AgentReseau.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
     public boolean AjouterAgentLigne(String nom) {
        Ligne L=new Ligne();
        L.Cod_ligne=nom;
        Object[] args={this.cfg.R, this.Frequence_Passage.AjouterLigne(nom), L};
        AgentController myagent;
        try {

            myagent = this.getContainerController().createNewAgent(nom, AgentLigne.class.getName(), args);
            myagent.start();
            this.ListeLigne.addElement(nom);
            this.Lignes.ListeDesLigne.addElement(L);
            return true;
        } catch (StaleProxyException ex) {
            ////System.out.println("La ligne n'est pas créée "+ex.toString());
            return false;
        }
    }
    public void Affichier_Interface(String nom){
        AID aid=new AID(nom, AID.ISLOCALNAME);
        ACLMessage msg=new ACLMessage(ACLMessage.INFORM);
        msg.setProtocol("commandes");
        msg.setContent("show");
        msg.addReceiver(aid);
        this.send(msg);
    }
    // creer un nouveau réseau
    public void nouveau(){
        this.arreter_tous();
        this.cfg=new Config();
        this.ListeLigne=new Vector<String>();
        if (this.gestReseau!=null){
            this.gestReseau.dispose();
            this.gestReseau=new GestionReseau(this, cfg);
        }
        this.chemin=null;
    }
    // arrete tous les agents:
    public void arreter_tous(){
        ACLMessage msg=new ACLMessage(ACLMessage.REQUEST);
        msg.setProtocol(ProtoCmd);
        msg.setContent("arreter");
        for (int i=0; i<this.cfg.R.ListeCarrefour.size(); i++){
            msg.addReceiver(this.cfg.R.ListeCarrefour.elementAt(i).agent_carrefour);
        }
        for(int i=0; i<this.ListeLigne.size(); i++){
            msg.addReceiver(new AID(this.ListeLigne.elementAt(i), AID.ISLOCALNAME));
        }
        this.send(msg);
    }
    public void ouvrir(){
        JFileChooser choix = new JFileChooser();
        choix.setFileFilter(null);
        //choix.addChoosableFileFilter(filtre);
        int retour = choix.showOpenDialog(this.gestReseau);
        if(retour == JFileChooser.APPROVE_OPTION) {
        // un fichier a été choisi ( sortie par OK)
        // nom du fichier  choisi
          String ch=choix.getSelectedFile().getPath();
          ////System.out.println("chemin="+ch);
          //
          Config conf = null;
          ObjectInputStream in = null;
          try {
            in = new ObjectInputStream(new FileInputStream(ch));
            conf = (Config) in.readObject();
            this.arreter_tous();
            conf.R.REinit();
            in.close();
            this.cfg=conf;
            this.ListeLigne=new Vector<String>();
            if (this.gestReseau!=null){
                this.gestReseau.dispose();
                this.gestReseau=new GestionReseau(this, cfg);
                if(cfg.R.ListeArcs.size()>0){
                  Carrefour c;
                  for(int i=0; i<cfg.R.ListeCarrefour.size();i++){
                     c=cfg.R.ListeCarrefour.elementAt(i);
                     this.AjouterAgentCarrefour(this.gestReseau.Rech_Nom_Car(c));
                  }
                }
            }
            this.chemin=ch;

          } catch (Exception ex) {
              JOptionPane.showMessageDialog(this.gestReseau,
                        "Le fichier ne contient pas la configuration d'un réseau",
                        "Erreur ",
                        JOptionPane.WARNING_MESSAGE);

          }
        }
        //

    }
    // enregistrer le réseau sous
    public void save_as(){
        JFileChooser choix = new JFileChooser();
        choix.setFileFilter(null);
        //choix.addChoosableFileFilter(filtre);
        int retour = choix.showSaveDialog(this.gestReseau);
        if(retour == JFileChooser.APPROVE_OPTION) {
        // un fichier a été choisi ( sortie par OK)
        // nom du fichier  choisi
          String ch=choix.getSelectedFile().getPath();
          this.chemin=ch;
          ObjectOutputStream out = null;
          try {
            out = new ObjectOutputStream(new FileOutputStream(this.chemin));
            out.writeObject(this.cfg);
            out.close();
          } catch (IOException ex) {
            JOptionPane.showMessageDialog(this.gestReseau,
                        "Erreur pendant l'enregistrement du fichier",
                        "Erreur ",
                        JOptionPane.WARNING_MESSAGE);
          }
        }
    }
    // enregistrer le réseau
    public void save(){
       if(this.chemin!=null){
           ObjectOutputStream out = null;
          try {
            out = new ObjectOutputStream(new FileOutputStream(this.chemin));
            out.writeObject(this.cfg);
            out.close();
            ///
            out = new ObjectOutputStream(new FileOutputStream("LignesBus"));
            out.writeObject(this.Lignes);
            //out.flush();
            out.close();
            System.out.println("Enregistrer");
          } catch (IOException ex) {
            JOptionPane.showMessageDialog(this.gestReseau,
                        "Erreur pendant l'enregistrement du fichier" + ex,
                        "Erreur ",
                        JOptionPane.WARNING_MESSAGE);
          }
       }else{
           this.save_as();
       }
    }
 private void Initialiser_Routes_Gps() {
        try {
//            AgentController myagent;
//            Object[] args={this.cfg.R,0,5};
//            myagent = this.getContainerController().createNewAgent("vehgps28012015A", AgentVehiculeGps.class.getName(), args);
//            myagent.start();
//            
//            Object[] args2={this.cfg.R,0,3};
//            myagent = this.getContainerController().createNewAgent("vehgps28012015BB", AgentVehiculeGps.class.getName(), args2);
//            myagent.start();
        	
        	  SourceDeVehiculesGps S =new SourceDeVehiculesGps(351, 50, 298, this.cfg.R ,this.getContainerController());

        } catch (Exception ex) {
        		System.out.println(ex.toString());
            
        }
    }

    private void Initialiser_Liste_Lignes() {
        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(new FileInputStream("LignesBus"));
            ListeLignes liste = (ListeLignes) in.readObject();
            in.close();
            for (int i=0; i<liste.ListeDesLigne.size(); i++){
                Ligne L=liste.ListeDesLigne.elementAt(i);
                for(int j=0; j<L.L_Arrets.size();j++){
                    Arret A=L.L_Arrets.elementAt(j);
                    A.arc=this.cfg.R.Rech_Arc(A.arc.code_arc);
                    A.date_arrive_dernier=0;
                    A.duree=0;
                }
                Vector<Arc> LARC=new Vector<Arc>();
                for(int j=0; j<L.listedesarcs.size();j++){
                    LARC.addElement(cfg.R.Rech_Arc(L.listedesarcs.elementAt(j).code_arc));
                }
                L.listedesarcs=LARC;                
                /// Creation des l'agent ligne
                Object[] args={this.cfg.R, this.Frequence_Passage.AjouterLigne(L.Cod_ligne), L};
                AgentController myagent;
                try {
                   myagent = this.getContainerController().createNewAgent(L.Cod_ligne, AgentLigne.class.getName(), args);
                   myagent.start();
                   this.ListeLigne.addElement(L.Cod_ligne);
                   System.out.println(L.Cod_ligne+" créée !");
                } catch (StaleProxyException ex) {
                    System.out.println(ex.toString());
                }
                ////

            }
            this.Lignes=liste;
            
            System.out.println("Ligne créee !");
        } catch (Exception ex) {
            this.Lignes=new ListeLignes();
            System.out.println(ex.toString());
            
        }
    }

     //*********************** Classes utilisées par l'agent ***********************//
    class commandes extends CyclicBehaviour{
        AgentReseau AR;
        MessageTemplate template;
        ACLMessage msg;
        String s;
        commandes(AgentReseau ar){
           this.AR=ar;
           template=MessageTemplate.MatchProtocol(AR.ProtoCmd);
        }
        @Override
        public void action() {
            msg = myAgent.receive(template);
            if(msg!=null){
                s=msg.getContent();
                if(s.equals("arreter")){  // arreter l'agent phase.
                    this.AR.doDelete();
                }
                if(s.equals("show")){
                    this.AR.gestReseau=new GestionReseau(AR, AR.cfg);
                }
            }else{
                this.block();
            }
        }
    }    
}
