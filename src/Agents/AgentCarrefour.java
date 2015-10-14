/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Agents;

import Reseau.Carrefour;
import Reseau.HistoriqueDesPlans;
import Reseau.Phase;
import Reseau.PlanDeFeux;
import Reseau.Arc;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Stack;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Dihya
 */
public class AgentCarrefour extends Agent {

   Carrefour carrefour;
   int[] Cycles_Possibles = {40, 60, 80, 100, 120, 140}; // liste des valeurs possibles pour un cycle
    Vector<Phase> Liste_Phases; // liste des phases du carrefour;
   Vector<stockage> listPath;
   PlanDeFeux Plan;
   AgentContainer ac;
   int pas_simul = 1000;
   public int rouge_integral;    // en millisecondes
    AC_Gui myGui;          // interface de l'agent carrefour
    //------------------- Protocole supportés par l'agent carrefour --------------//
    String ProtoRecherche = "chemin";
    final String ProtoCmd = "commandes";
   final String ProtoCalculCycle = "CalculCycle";
   final String ProtoReservation = "Reservation";
   final String ProtoInfo = "volume_envoye?";
   final String ProtoInscription = "Inscription";
   String ProtoReponse = "reponse";
   boolean strategie_fixe = false;
   int long_phase = 30;
   HistoriqueDesPlans Historique;
    //----------------------------------------------------------------------------//
    ExecutionPlan ExecPlan = null;

    @Override
    protected void setup() {
        this.rouge_integral = 2 * pas_simul;
        this.Historique = new HistoriqueDesPlans(this.getLocalName());
        this.carrefour = (Carrefour) (this.getArguments()[0]);
        this.carrefour.agent_carrefour = this.getAID();
        this.listPath = new Vector<stockage>();
        this.Liste_Phases = this.carrefour.Liste_Phases;
        this.ac = this.getContainerController();
        this.Plan = new PlanDeFeux(this);
        ////////// ajout des comportements initiaux
        this.addBehaviour(new commandes(this));
        this.addBehaviour(new Path_Prediction(this));
        //this.addBehaviour(new Reception_res(this));
        // this.addBehaviour(new FournirInfo(this));
        /// creation des agents Phase:
        for (int i = 0; i < this.Liste_Phases.size(); i++) {
            this.Ajouter_Phase(this.Liste_Phases.elementAt(i));

        }
        ///////   
        //  this.demarrer();
    }
    //------------------------------ Méthodes utilisées par l'agent Carrefour ---------//
    //1) ajouter une phase (un AgentPhase)

    public void ecrire(String nomFic, String texte) {
        String adressedufichier = System.getProperty("user.dir") + "/" + nomFic;

        //on met try si jamais il y a une exception
        try {
            /**
             * BufferedWriter a besoin d un FileWriter, les 2 vont ensemble, on
             * donne comme argument le nom du fichier true signifie qu on ajoute
             * dans le fichier (append), on ne marque pas par dessus
             *
             */
            FileWriter fw = new FileWriter(adressedufichier, true);

            // le BufferedWriter output auquel on donne comme argument le FileWriter fw cree juste au dessus
            BufferedWriter output = new BufferedWriter(fw);
            //on marque dans le fichier ou plutot dans le BufferedWriter qui sert comme un tampon(stream)
            output.write(texte + "\r\n\n");
            //on peut utiliser plusieurs fois methode write
            output.flush();
            //ensuite flush envoie dans le fichier, ne pas oublier cette methode pour le BufferedWriter
            output.close();
            //et on le ferme
            //System.out.println("fichier créé");
        } catch (IOException ioe) {
            System.out.print("Erreur : ");
        }

    }

    void Ajouter_Phase(String s) {
        Phase p = new Phase(s);
        this.Liste_Phases.addElement(p);
        Object[] args = {this.getAID(), p};
        AgentController myagent;
        try {
            myagent = ac.createNewAgent(s, AgentPhase.class.getName(), args);
            myagent.start();
        } catch (StaleProxyException ex) {
            this.Erreur_MSG("Ajouter_Phase (String S): " + ex.toString());
        }
    }

    void Ajouter_Phase(Phase p) {
        Object[] args = {this.getAID(), p};
        AgentController myagent;
        try {
            myagent = ac.createNewAgent(p.code_phase, AgentPhase.class.getName(), args);
            myagent.start();
            ACLMessage reply = this.blockingReceive(MessageTemplate.MatchProtocol(this.ProtoInscription), 5000);
        } catch (StaleProxyException ex) {
            this.Erreur_MSG("Ajouter_Phase (Phase p): " + ex.toString());
        }
    }
    //2) Supprime une phase ainsi que l'agent correspondant

    void SuppPhase(int index_phase) {
        if ((index_phase >= 0) && (index_phase < this.Liste_Phases.size())) {
            AID aid = this.Liste_Phases.elementAt(index_phase).agent;
            ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
            msg.setProtocol(this.ProtoCmd);
            msg.setContent("arreter");
            msg.addReceiver(aid);
            this.send(msg);
            MessageTemplate mt = MessageTemplate.MatchProtocol(this.ProtoCmd);
            ACLMessage reply = this.blockingReceive(mt, 6000);
            this.Liste_Phases.removeElementAt(index_phase);
            if (this.myGui != null) {
                this.myGui.raffraichir();
            }
        }
    }

    void Calculer_Cycle() {
        if (this.strategie_fixe) {
            this.Plan.Init_Plan();
            long dd = System.currentTimeMillis();
            for (int i = 0; i < this.Liste_Phases.size(); i++) {
                String cod_p = this.Liste_Phases.elementAt(i).code_phase;
                this.Plan.Ajouter_elemnt(cod_p, dd, dd + this.long_phase * this.pas_simul);
                dd = dd + this.long_phase * this.pas_simul + this.rouge_integral;
            }
        } else {
            this.addBehaviour(new ComportementCalculCycle(this));
        }

    }

    public void demarrer() {
        if (this.ExecPlan == null) {
            if (this.Liste_Phases.size() > 0) {
                this.ExecPlan = new ExecutionPlan(this);
                this.addBehaviour(ExecPlan);
            }

        }
    }

    public void Afficher() {
        if (this.myGui != null) {
            this.myGui.raffraichir();
        }
    }

    private void AjouterRes(String bus, int priorite, int a1, int a2, long td, long tf) {
        int i = this.Liste_Phases.size() - 1;
        Phase p;
        while (i >= 0) {
            p = this.Liste_Phases.elementAt(i);
            if (p.Satisfait(a1, a2)) {
                this.Plan.ajouter_res(bus, priorite, p.code_phase, td - this.rouge_integral, tf);
            }
            i--;
        }
        if (this.myGui != null) {
            this.myGui.raffraichir();
        }
    }

    private void ArreterPhase(int pt) {
        String cod = this.Plan.plan.elementAt(pt).code_phase;
        Phase p = this.Rech_Phase(cod);
        if (p != null) {
            for (int i = 0; i < p.E.size(); i++) {
                p.E.elementAt(i).etat = 'r';
            }
        }
    }

    private void ExecuterPhase(int pt) {
        String cod = this.Plan.plan.elementAt(pt).code_phase;
        Phase p = this.Rech_Phase(cod);

        if (p != null) {
            for (int i = 0; i < p.E.size(); i++) {
                p.E.elementAt(i).etat = 'v';
            }
        }

    }
    // recherche une phase:

    Phase Rech_Phase(String cod) {
        Phase p = null;
        Iterator it = this.Liste_Phases.iterator();
        while (it.hasNext()) {
            p = (Phase) it.next();
            if (p.code_phase.equals(cod)) {
                return p;
            }
        }
        p = null;
        return p;
    }

    @Override
    protected void takeDown() {
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.setProtocol(ProtoCmd);
        msg.setContent("arreter");
        for (int i = 0; i < this.Liste_Phases.size(); i++) {
            msg.addReceiver(this.Liste_Phases.elementAt(i).agent);
        }
        this.send(msg);
    }
    //------------------------------ fin méthodes ---------------------------------//

    //*********************** Classes utilisées par l'agent ***********************//
    class commandes extends CyclicBehaviour {

        AgentCarrefour AC;
        MessageTemplate template;
        ACLMessage msg;
        String s;

        commandes(AgentCarrefour ac) {
            this.AC = ac;
            template = MessageTemplate.MatchProtocol(AC.ProtoCmd);
        }

        @Override
        public void action() {
            msg = myAgent.receive(template);
            if (msg != null) {
                s = msg.getContent();
                if (s.equals("arreter")) {  // arreter l'agent phase.                    
                    ACLMessage reply = msg.createReply();
                    reply.setContent("ok");
                    myAgent.send(reply);
                    //////System.out.println("Agent Carrefour "+myAgent.getLocalName()+" arrêté !!");
                    myAgent.doDelete();
                }
                if (s.equals("show")) {
                    this.AC.myGui = new AC_Gui(AC);
                }
                if (s.equals("historique")) {
                    System.out.println("Histo envoyé");
                    ACLMessage rep = msg.createReply();
                    try {
                        rep.setContentObject(this.AC.Historique);
                        this.AC.send(rep);
                    } catch (IOException ex) {
                        Logger.getLogger(AgentCarrefour.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } else {
                this.block();
            }
        }
    }

    class stockage {

       public Arc arcDestination;
       public Arc arcOrigine;
       public Vector<Arc> chemin;

        public stockage() {

            arcDestination = null;
            arcOrigine = null;
            chemin = new Vector<Arc>();
        }
    }

    class travelInfo {

       public Arc arcDestination;
       public Arc arcDepart;
        String idVehicle;

        public travelInfo() {
            arcDestination = null;
            arcDepart = null;
            idVehicle = new String();
        }
    }

    class NoeudCout {

       Carrefour noeud = null;
       double cout;

        public NoeudCout() {
            cout = 0;


        }
    }

    class TravelTime {

       Arc arcCors = null;
        double traveltime;
        int capaciteRest;

        public TravelTime() {
            capaciteRest = 0;
            traveltime = 0.0;
        }
    }

    class Path_Prediction extends CyclicBehaviour {

       AgentCarrefour AC;
       AID VehiculeEnvoie;
       Vector<NoeudCout> listeNoeud;
       Vector<Arc> listeArc_P;
       travelInfo demande;
       MessageTemplate template;
       ACLMessage msg;
       Object[] obj;
       int etape = 0;
       Vector<Arc> sons_O;
       Vector<Arc> parents_D;
       Arc destinationReponse = null;

        public Path_Prediction(AgentCarrefour ac) {
            this.AC = ac;
            VehiculeEnvoie = new AID();
            listeArc_P = new Vector<Arc>();
            template = MessageTemplate.MatchProtocol(AC.ProtoRecherche);
            listeNoeud = new Vector<NoeudCout>();
            etape = 0;
            sons_O = new Vector<Arc>();
            parents_D = new Vector<Arc>();
            demande = new travelInfo();
            listeNoeud = new Vector<NoeudCout>();
        }

        public Vector<Arc> sons(travelInfo deman) {

            Arc origine = null;
            int index_fils = 0;


            Vector<Arc> listesuivants = new Vector<Arc>();
            Vector<Arc> listeintermediaire = new Vector<Arc>();
            System.out.println("l'arc est ::::::" + deman.arcDepart.code_arc);
            System.out.println("la liste suivant ::::::::::" + deman.arcDepart.suivants.size());
            for (int i = 0; i < deman.arcDepart.suivants.size(); i++) {

                listesuivants.add(deman.arcDepart.suivants.elementAt(i).a);
            }
            System.out.println("la liste des suisvant est de taille :" + listesuivants.size());
            origine = listesuivants.elementAt(index_fils);
            System.out.println("l'origine est " + origine.code_arc);
            while (origine != null) {
                System.out.println("que ca commence ");
                System.out.println("l'origine est " + origine.code_arc);
                if (origine.suivants.size() > 0) {
                    for (int j = 0; j < origine.suivants.size(); j++) {
                        listeintermediaire.add(origine.suivants.elementAt(j).a);
                    }

                    for (int f = 0; f < listeintermediaire.size(); f++) {
                        boolean indiexist = true;
                        Arc intermediaire = listeintermediaire.elementAt(f);
                        for (int g = 0; g < listesuivants.size(); g++) {
                            if (intermediaire.equals(listesuivants.elementAt(g))) {
                                indiexist = false;
                            }

                        }
                        if (indiexist) {
                            listesuivants.add(intermediaire);
                            //   index_fils++;
                        }

                    }
                }

                index_fils++;
                if (index_fils < listesuivants.size()) {
                    if (listesuivants.elementAt(index_fils) != null) {
                        origine = listesuivants.elementAt(index_fils);
                    }
                } else {
                    origine = null;
                }

            }

            return listesuivants;
        }

        /// Methode de calcul de djikistra 
        /// Methode de recherche de la liste des noeuds (Carrefour)
        public void rechercheNoeud(travelInfo dem) {
            Vector<Arc> listeArcChemin = new Vector<Arc>();
            NoeudCout transfert = new NoeudCout();
            boolean indPremier = false;
            System.err.println("la liste path est de taille " + listPath.size());
            System.out.println("la liste path" + listPath.firstElement().chemin.firstElement().code_arc);
            if (listPath.size() > 0) {
                for (int i = 0; i < listPath.size(); i++) {
                    if ((listPath.elementAt(i).arcDestination.equals(dem.arcDestination)) && (listPath.elementAt(i).arcOrigine.equals(dem.arcDepart))) {
                        listeArcChemin = listPath.elementAt(i).chemin;

                    }
                }
            }

            if (listeArcChemin.firstElement().Origine != null) {
                indPremier = true;
                transfert.cout = 0;
                transfert.noeud = listeArcChemin.firstElement().Origine;

                listeNoeud.add(transfert);
            }
            if (indPremier) {
                for (int j = 0; j < listeArcChemin.size(); j++) {
                    //listeArcChemin.elementAt(j).Des

                    if (listeArcChemin.elementAt(j).Des != null) {
                        boolean ind = false;
                        for (int g = 0; g < listeNoeud.size(); g++) {
                            if (listeNoeud.elementAt(g).equals(listeArcChemin.elementAt(j))) {
                                ind = true;
                            }
                        }
                        if (ind) {
                            transfert.cout = 300000000;
                            transfert.noeud = listeArcChemin.firstElement().Des;
                            listeNoeud.add(transfert);
                        }
                    }
                }
            } else {
                transfert.cout = 0;
                transfert.noeud = listeArcChemin.firstElement().Des;
                listeNoeud.add(transfert);
                for (int j = 0; j < listeArcChemin.size(); j++) {
                    if (listeArcChemin.elementAt(j).Des != null) {
                        boolean ind = false;
                        for (int g = 0; g < listeNoeud.size(); g++) {
                            if (listeNoeud.elementAt(g).equals(listeArcChemin.elementAt(j))) {
                                ind = true;
                            }
                        }
                        if (ind) {
                            transfert.cout = 300000000;
                            transfert.noeud = listeArcChemin.firstElement().Des;
                            listeNoeud.add(transfert);
                        }
                    }
                }

            }
        }

        // methode dijkstra 
        public Arc dijkstra() {
            double travel;
            if ((demande.arcDepart.Des == demande.arcDestination.Origine)) {
                return demande.arcDestination;
            } else {
                // initialisation 
                Vector<Arc> listeArcChemin = new Vector<Arc>();
                Vector<TravelTime> listeArcMeilleurChemin = new Vector<TravelTime>();
                Arc resultat = null;
                listeArc_P.removeAllElements();
                TravelTime variableInter = new TravelTime();
                Vector<Arc> listeIntermediaire = new Vector<Arc>();
                //Carrefour carrefourTraiEncours = listeNoeud.firstElement().noeud;

                Carrefour carrefourTraiEncours = demande.arcDepart.Des;
                System.err.println("la liste path" + listPath.size());
                for (int i = 0; i < listPath.size(); i++) {
                    for (int j = 0; j < listPath.elementAt(i).chemin.size(); j++) {
                        System.out.println("le carrefour est : " + AC.getLocalName() + "les element de la liste : " + listPath.elementAt(i).chemin.elementAt(j).code_arc);
                    }
                }
//                System.out.println("l'agent est " + AC.getLocalName() + "Le depart est : " + demande.arcDepart.code_arc + "L'arc destination est :" + demande.arcDestination.code_arc);
//                if (AC.getLocalName().equals("C9")) {
//                    System.err.println("la taille de la liste de chemin est  :  " + listPath.firstElement().chemin.size());
//                    System.out.println("la taille de la liste des chemin est : " + listPath.size());
//
//                    System.out.println("le depart est : " + listPath.firstElement().arcOrigine.code_arc);
//                    System.out.println("l'origine est : " + listPath.firstElement().arcDestination.code_arc);
//
//                }
                for (int i = 0; i < listPath.size(); i++) {
                    if ((listPath.elementAt(i).arcOrigine.code_arc == demande.arcDepart.code_arc) && (listPath.elementAt(i).arcDestination.code_arc == demande.arcDestination.code_arc)) {
                        listeArcChemin = listPath.elementAt(i).chemin;
                    }
                }


                listeIntermediaire = carrefourTraiEncours.S;
                //listeIntermediaire = listeNoeud.firstElement().noeud.S;
                variableInter.traveltime = Double.MAX_VALUE;

                //System.out.println("MA TAILLEEEEEE: " + listeIntermediaire.size());
                //System.out.println("que sont: ");
                for (Arc ax : listeIntermediaire) {
                    System.out.println(ax.code_arc);
                }

                // for (int i = 0; i < listeArcChemin.size(); i++) {
                
                for (int j = 0; j < listeIntermediaire.size(); j++) {
                     Vector<Arc> predec = new Vector<Arc>();
                     predec=(listeIntermediaire.elementAt(j).Origine.E);
                     int vehPrec =0;
                     for (int i = 0; i < predec.size(); i++) {
                        vehPrec= vehPrec+ predec.elementAt(i).nbre_bus+predec.elementAt(i).nbre_veh;
                
                     }
                    if (listeArcChemin.contains(listeIntermediaire.get(j))) {
                        System.out.println(listeIntermediaire.get(j).code_arc + " y est !!!");
                        //if (listeIntermediaire.get(j).equals(listeIntermediaire.elementAt(j))) {
                        double travelTx = (0.0 + ((listeIntermediaire.get(j).longueur) / (0.0 + listeIntermediaire.get(j).vitesse_moyenne))) * (1 + (((listeIntermediaire.get(j).nbre_bus + listeIntermediaire.get(j).nbre_veh) / listeIntermediaire.get(j).capacite)));//+vehPrec*(1+(((listeIntermediaire.get(j).nbre_bus + listeIntermediaire.get(j).nbre_veh) / listeIntermediaire.get(j).capacite)));
                        int capaciteRest = listeIntermediaire.get(j).nbre_cel - (listeIntermediaire.get(j).nbre_bus + listeIntermediaire.get(j).nbre_veh);

                        System.out.print("mon travelTx =" + travelTx);
                        System.out.print("ma capRest =" + capaciteRest);

                        System.out.print("nbcel" + listeIntermediaire.get(j).nbre_cel);
                        System.out.print(("nbbus" + listeIntermediaire.get(j).nbre_bus));
                        System.out.print("nbveh" + listeIntermediaire.get(j).nbre_veh);


                        if ((travelTx <= variableInter.traveltime) && (capaciteRest > 0) // && (listeIntermediaire.get(j).nbre_bus <= capaciteRest)
                                ) {
                            //if (listeIntermediaire.get(j).nbre_bus <= capaciteRest)
                            {
                                //variableInter = new TravelTime();
                                variableInter.arcCors = listeIntermediaire.get(j);
                                variableInter.traveltime = travelTx;
                                //(0.0+((listeArcChemin.elementAt(i).longueur) / (0.0+listeArcChemin.elementAt(i).vitesse_moyenne)));// + ((listeArcChemin.elementAt(i).nbre_bus + listeArcChemin.elementAt(i).nbre_veh) / listeArcChemin.elementAt(i).capacite));
                                variableInter.capaciteRest = capaciteRest;
                                //listeArcChemin.elementAt(i).nbre_cel - (listeArcChemin.elementAt(i).nbre_bus + listeArcChemin.elementAt(i).nbre_veh);
                                //listeArcMeilleurChemin.add(variableInter);
                                System.out.println("La longueur est::::::" + listeIntermediaire.get(j).longueur);
                                System.out.println("La vitesse est::::::" + listeIntermediaire.get(j).vitesse_moyenne);
                                System.out.println("Le travel time est::::::" + variableInter.traveltime);
                            }
                        }


                    }
                    System.out.println(listeIntermediaire.get(j).code_arc + " n'y est pas !!!");

                }
                // }
            /*TravelTime min = listeArcMeilleurChemin.firstElement();
                 for (int i = 0; i < listeArcMeilleurChemin.size(); i++) {
                 if ((listeArcMeilleurChemin.elementAt(i).traveltime <= min.traveltime) && (listeArcMeilleurChemin.elementAt(i).capaciteRest > 0)) {
                 min = listeArcMeilleurChemin.elementAt(i);
                 resultat = min.arcCors;
                 System.out.println("Le mintravel time est::::::"+min.traveltime);
                 }
                 }*/
            

            resultat = variableInter.arcCors;
            travel=variableInter.traveltime;
            System.out.println("Le mintravel time est::::::" + variableInter.traveltime);
            return resultat;
            }


        }

        public Vector<Arc> parent(travelInfo deman) {
            Carrefour noeudDestination = deman.arcDestination.Origine;
            System.out.println("le noeud destination est:::::::::" + noeudDestination);
            Arc destination = null;
            boolean ind = true;
            int indexParent = 0;
            Vector<Arc> listeParent = new Vector<Arc>();
            listeParent.add(demande.arcDestination);
            Vector<Arc> listeIntermediaire = new Vector<Arc>();
            for (int i = 0; i < noeudDestination.E.size(); i++) {
                listeParent.add(noeudDestination.E.elementAt(i));
                System.out.println("l'arc de depart est :::::::: " + noeudDestination.E.elementAt(i).code_arc);
            }
            destination = listeParent.elementAt(indexParent);
            System.out.println("la destination est:::::::::" + destination);
            while (destination != null) {
                System.out.println("11111111111 la destination est  " + destination.code_arc);

                if (ind) {
                    if (destination.code_arc != deman.arcDepart.code_arc) {
                        System.out.println("la destination est " + destination.code_arc);
                        for (int j = 0; j < destination.Origine.E.size(); j++) {
                            listeIntermediaire.add(destination.Origine.E.elementAt(j));

                        }
                        for (int f = 0; f < listeIntermediaire.size(); f++) {
                            boolean indiexist = true;
                            Arc intermediaire = listeIntermediaire.elementAt(f);
                            for (int g = 0; g < listeParent.size(); g++) {
                                if (intermediaire.equals(listeParent.elementAt(g))) {
                                    indiexist = false;
                                }

                            }
                            if (indiexist) {
                                listeParent.add(intermediaire);
                            }
                        }
                    }
                }
                indexParent++;
                // if (indexParent < listeParent.size()) {
                if (listeParent.elementAt(indexParent).code_arc == deman.arcDepart.code_arc) {
                    destination = null;
                    ind = false;
                } else {
                    destination = listeParent.elementAt(indexParent);
                    System.out.println("La destination est comme suit :::: " + destination.code_arc);
                }
                //}

            }



            return listeParent;
        }
        //a corriger
     /*   public void Envoyer_Chemin(stockage chemin)
         {if(chemin !=null)
         {
         Object[] obj = {chemin};
         Carrefour c=this.arc_actuel.Des;
         ACLMessage msg=new ACLMessage(ACLMessage.INFORM);
         msg.
         msg.addReceiver(c.agent_carrefour);
         msg.setProtocol(this.ProtoRecherche);
         try {
         msg.setContentObject(obj);
         this.send(msg);
         } catch (IOException ex) {
         Logger.getLogger(AgentBus.class.getName()).log(Level.SEVERE, null, ex);
         }
         }
        
         }*/

        public void action() {
            boolean firs = false;
            switch (etape) {
                case 0: {

                    msg = myAgent.receive(template);
//                    msg.getProtocol().equals("chemin");
                    //  msg.getSender();
                    if (msg != null) {
                        try {
                            obj = (Object[]) msg.getContentObject();
                            VehiculeEnvoie = msg.getSender();
                            //{this.getLocalName(),a1,a2,td,tf};
                            String voiture = (String) obj[0];
                            Arc arcDest = (Arc) obj[1];
                            Arc arcDapart = (Arc) obj[2];
                            firs = ((Boolean) obj[3]).booleanValue();
                            if (AC.getLocalName().equals("C1")) {
                                System.out.println("J'ai recu l'arc destination : " + arcDest.code_arc);
                            }
                            demande.arcDepart = arcDapart;
                            demande.arcDestination = arcDest;
                            demande.idVehicle = voiture;
                            etape = 2;
                        } catch (UnreadableException ex) {
                            this.AC.Erreur_MSG("Recherche_destination " + ex.toString());
                        }
                    } else {
                        this.block();
                    }
                }
                break;
                case 1: {
                    //  if (firs) {
                    sons_O = sons(demande);
                    System.out.println("Je suis  la dans les fils ");
                    parents_D = parent(demande);
                    System.out.println("Je suis  la dans les pere ");
                    stockage stock = new stockage();
                    for (int i = 0; i < sons_O.size(); i++) {

                        Arc arcInter = sons_O.elementAt(i);
                        for (int j = 0; j < parents_D.size(); j++) {
                            if (arcInter.code_arc == parents_D.elementAt(j).code_arc) {
                                stock.chemin.add(arcInter);
                                stock.arcOrigine = demande.arcDepart;
                                stock.arcDestination = demande.arcDestination;
                                System.out.println("l'agent est : " + AC.getLocalName() + "chemin rempli");
                                //if(AC.getLocalName().equals("C3")){
                                //    System.out.println("le chemin de C3  est : "+stock.chemin.elementAt(i).code_arc);

                                //}
                            }
                        }
                    }
                    if (stock != null) {
                        AC.listPath.add(stock);
                        System.out.println("l'agent est  :::" + AC.getLocalName() + "Chemin calculé");
                        // il restela methode d'envoie des chemin au carrefour restant. 
                    }
                    etape = 2;
                    //   }
                }
                break;
                case 2: { // Calcul de djikistra
                    //rechercheNoeud(demande); // génération de la liste des noeuds
                    System.out.println("le carfour " + AC.getLocalName());
                    Vector<Arc> path = controletrafic.Dijkstra.dijkstra(demande.arcDepart, demande.arcDestination );
                    //destinationReponse = dijkstra();
                    if (path != null) {
                        Object[] obj = {path};
                        //Carrefour c=this.arc_actuel.Des;
                        System.out.println("la reponse est est est :" + path );
                        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
                        /// msg.
                        msg.addReceiver(VehiculeEnvoie);
                        msg.setProtocol(AC.ProtoReponse);
                        try {
                            msg.setContentObject(obj);
                            AC.send(msg);
                        } catch (IOException ex) {
                            Logger.getLogger(AgentBus.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    initialiseAgent();
                    etape = 0;

                }
                break;
            }
        }

        public void initialiseAgent() {
            // AC.rouge_integral = 2 * pas_simul;
            //  AC.Historique = new HistoriqueDesPlans(this.getLocalName());
            //  AC.carrefour = (Carrefour) (this.getArguments()[0]);
            //   AC.carrefour.agent_carrefour = this.getAID();
            AC.listPath = new Vector<stockage>();
            template = MessageTemplate.MatchProtocol(AC.ProtoRecherche);
            // AC.Liste_Phases = this.carrefour.Liste_Phases;
            //  AC.ac = this.getContainerController();
            //   AC.Plan = new PlanDeFeux(this);
            ////////// ajout des comportements initiaux
            //   AC.addBehaviour(new commandes(this));
            //   AC.addBehaviour(new Path_Prediction(this));

        }
    }

    class FournirInfo extends CyclicBehaviour {

       AgentCarrefour AC;
       MessageTemplate template;
       ACLMessage msg, reply;

        FournirInfo(AgentCarrefour ac) {
            this.AC = ac;
            template = MessageTemplate.MatchProtocol(AC.ProtoInfo);
        }

        @Override
        public void action() {
            msg = AC.receive(template);
            if (msg != null) {
                int cod_arc = Integer.valueOf(msg.getContent()).intValue();
                int vol = this.AC.carrefour.VolumeEnvoyerA(cod_arc);
                Object[] obj = {cod_arc, vol};
                reply = msg.createReply();
                try {
                    reply.setContentObject(obj);
                } catch (IOException ex) {
                    this.AC.Erreur_MSG("FournirInfo: " + ex.toString());
                }
                reply.setPerformative(ACLMessage.INFORM);
                reply.setProtocol(AC.ProtoInfo);
                AC.send(reply);
            } else {
                this.block();
            }
        }
    }
    // Ce comportement est execté pour calculer le nouveau cycle. donc, il est chargé à la
    // fin de chaque cycle.

    class ComportementCalculCycle extends Behaviour {

        boolean done;
        AgentCarrefour AC;
        int nbre;              // nombre de réponses attendues
        long dd;               // date debut de la phase.
        int etape = 0;
        int t = 0;
        int Max_Cycle;
        //int cycle;
        int c, Max, d, PosMax, cout;
       float degre_congest = (float) 0.0;
       float degre;
        //Vector ListeDes;
       Vector<Object[]> ListeRep;
       Vector<Object[]> ListeOffres;
       Object[] obj;
       Vector<AID> coop_group;
       MessageTemplate template;
       ACLMessage msg;

        ComportementCalculCycle(AgentCarrefour ac) {

            this.AC = ac;
            this.ListeRep = new Vector<Object[]>();
            this.ListeOffres = new Vector<Object[]>();
            this.coop_group = new Vector<AID>();
            // initialisation de coop_group
            for (int i = 0; i < this.AC.Liste_Phases.size(); i++) {
                this.coop_group.addElement(AC.Liste_Phases.elementAt(i).agent);
            }
            //
            this.Max_Cycle = this.AC.Cycles_Possibles[AC.Cycles_Possibles.length - 1];
            this.dd = System.currentTimeMillis();//+AC.rouge_integral;     //rouge integrale
            AC.Plan.Init_Plan();  // reinitialise le plan avant de le calculer
            System.out.println("Plan MAJ !!");
            template = MessageTemplate.MatchProtocol(AC.ProtoCalculCycle);

            nbre = AC.Liste_Phases.size(); //le nombre de reponses que l'agent carrefour doit recevoir            
            etape = 0;
            done = false;
        }

        @Override
        public void action() {
            switch (etape) {
                case 0: {
                    nbre = coop_group.size();
                    if (nbre > 0) {
                        msg = new ACLMessage(ACLMessage.REQUEST);
                        msg.setProtocol(AC.ProtoCalculCycle);
                        msg.setContent(Integer.toString(t));
                        for (int i = 0; i < nbre; i++) {
                            msg.addReceiver(coop_group.elementAt(i));
                        }
                        etape = 1;
                        this.AC.send(msg);
                    } else {
                        this.done = true;
                        AC.Afficher();
                    }
                }
                break;
                case 1: {  // reception des reponses:                    
                    msg = this.AC.receive(template);
                    if (msg != null) {
                        nbre--;
                        try {
                            obj = (Object[]) msg.getContentObject();
                        } catch (UnreadableException ex) {
                            this.AC.Erreur_MSG("ComportementCalculCycle :" + ex.toString());
                        }
                        this.ListeRep.addElement(obj);
                        if (nbre == 0) {  // s'il a reçu toutes les réponses:
                            etape = 2;
                        }
                    } else {
                        this.block();
                    }
                }
                break;
                case 2: {

                    c = 0;
                    d = 0;
                    Max = 0;
                    PosMax = 0;
                    degre_congest = (float) 0.0;
                    for (int i = 0; i < this.ListeRep.size(); i++) {   // on calcule la taille du cycle
                        obj = this.ListeRep.elementAt(i);
                        d = ((Integer) obj[1]).intValue();
                        c = c + d;
                        degre = ((Float) obj[2]).floatValue();
                        degre += this.AC.Plan.Totale_Res_Recues((String) obj[0], t, d);
                        if (degre > degre_congest) {
                            degre_congest = degre;
                            PosMax = i;
                            Max = d;
                        }
                    }
                    if (c > Max_Cycle) {
                        etape = 3;  // reduire le cycle
                        cout = 0;
                    } else {
                        Max_Cycle = Max_Cycle - Max;//-AC.rouge_integral/AC.pas_simul;
                        t = t + Max + AC.rouge_integral / AC.pas_simul;
                        String cod_p = (String) this.ListeRep.elementAt(PosMax)[0];
                        Max = Max * AC.pas_simul;
                        AC.Plan.Ajouter_elemnt(cod_p, dd, dd + Max);
                        // envoie du message d'acceptation a la phase:
                        msg = new ACLMessage(ACLMessage.CONFIRM);
                        msg.setProtocol(AC.ProtoCalculCycle);
                        msg.setContent("ok");
                        msg.addReceiver(AC.Rech_Phase(cod_p).agent);
                        AC.send(msg);
                        //
                        dd = dd + Max + AC.rouge_integral;
                        Exclure(cod_p);  // supprime la phase de coop_group
                        this.ListeRep.clear();
                        etape = 0;
                    }
                }
                break;
                case 3: { // Envoi d'un appel d'offre                    
                    this.ListeOffres.clear();
                    cout++;
                    msg = new ACLMessage(ACLMessage.CFP);
                    msg.setProtocol(AC.ProtoCalculCycle);
                    msg.setContent(Integer.toString(cout));
                    for (int i = 0; i < this.coop_group.size(); i++) {
                        msg.addReceiver(this.coop_group.elementAt(i));
                    }
                    AC.send(msg);
                    nbre = this.coop_group.size();
                    etape = 4;  // reception des propositions
                }
                break;
                case 4: {    // reception des offres                   
                    msg = AC.receive(template);
                    if (msg != null) {
                        try {
                            obj = (Object[]) msg.getContentObject();
                        } catch (UnreadableException ex) {
                            this.AC.Erreur_MSG("ComportementCalculCycle " + ex.toString());
                        }
                        this.ListeOffres.addElement(obj);
                        nbre--;
                        if (nbre == 0) {
                            int somme = 0;
                            for (int i = 0; i < this.ListeOffres.size(); i++) {
                                somme = somme + ((Integer) this.ListeOffres.elementAt(i)[1]).intValue();
                            }
                            if (c - somme <= Max_Cycle) {
                                DistribuerEtModifier(Max_Cycle - c + somme, somme);
                                etape = 2;
                            } else {
                                etape = 3;
                            }
                        }
                    } else {
                        this.block();
                    }
                }
                break;
            }
        }

        @Override
        public boolean done() {
            if (this.done && (AC.myGui != null)) {
                long d_deb = this.AC.Plan.plan.firstElement().date_d;
                long d_fin = this.AC.Plan.plan.lastElement().date_f;
                int cycle = (int) ((d_fin - d_deb) / 1000);
                this.AC.myGui.JL_TailleCycle.setText(Integer.toString(cycle));
            }
            return done;
        }

        private void DistribuerEtModifier(int residu, int total) {
            int offre;
            float r = residu;
            float somme = total;
            for (int i = 0; i < this.ListeOffres.size(); i++) {
                obj = this.ListeOffres.elementAt(i);
                offre = ((Integer) obj[1]).intValue();
                offre = (int) Math.ceil(offre - ((float) offre * r / somme));
                // recherche dans Liste_Rep:
                int j = 0;
                boolean trouv = false;
                String cod_p = (String) obj[0];
                String cod;
                while (!trouv && (j < this.ListeRep.size())) {
                    obj = this.ListeRep.elementAt(j);
                    cod = (String) obj[0];
                    if (cod.equals(cod_p)) {
                        obj[1] = (Integer) obj[1] - offre;
                        trouv = true;
                    } else {
                        j++;
                    }
                }
            }
        }
        // exclue la phase cpd_p de "coop_group"

        private void Exclure(String cod_p) {
            int i = this.coop_group.size() - 1;
            boolean trouv = false;
            while (i >= 0 && !trouv) {
                if (this.coop_group.elementAt(i).getLocalName().equals(cod_p)) {
                    trouv = true;
                    this.coop_group.removeElementAt(i);
                } else {
                    i--;
                }
            }
        }
    }
    //****************************************************************************//

    class Reception_res extends CyclicBehaviour {

        AgentCarrefour AC;
        MessageTemplate template;
        ACLMessage msg;
        Object[] obj;

        Reception_res(AgentCarrefour a) {
            this.AC = a;
            template = MessageTemplate.MatchProtocol(AC.ProtoReservation);
        }

        @Override
        public void action() {
            Arc arcPassage = null;
            msg = myAgent.receive(template);

            if (msg != null) {
                try {
                    obj = (Object[]) msg.getContentObject();
                    //{this.getLocalName(),a1,a2,td,tf};
                    arcPassage = (Arc) obj[0];



                } catch (UnreadableException ex) {
                    this.AC.Erreur_MSG("Reception_res " + ex.toString());
                }
            } else {
                this.block();
            }
            return;
        }
    }

    //****************************** Suivi de l'execution du plan ***************************//
    class ExecutionPlan extends CyclicBehaviour {

       AgentCarrefour AC;
       int etape = 0;
       long date;
       PlanDeFeux plan_feux;
       boolean calcul_lance = false;
       private long df;
       private int r;

        public ExecutionPlan(AgentCarrefour ac) {
            this.AC = ac;
            this.plan_feux = AC.Plan;
        }

        @Override
        public void action() {
            //this.date=System.currentTimeMillis();
            switch (etape) {
                case 0: {
                    if (this.calcul_lance) {
                        this.block(100);
                    } else {
                        AC.Calculer_Cycle();
                        this.calcul_lance = true;
                        this.block(100);
                    }
                    if (this.plan_feux.plan.size() >= this.AC.Liste_Phases.size()) {
                        this.plan_feux.pt = 0;
                        //dd=this.plan_feux.plan.elementAt(pt).date_d;
                        df = this.plan_feux.plan.elementAt(this.plan_feux.pt).date_f;
                        etape = 1;
                        AC.ExecuterPhase(this.plan_feux.pt);
                        this.calcul_lance = false;
                        //this.AC.Historique.Ajouter_Plan(Plan);

                    }
                }
                break;
                case 1: {
                    date = System.currentTimeMillis();
                    this.plan_feux.MAJ_Plan();
                    df = this.plan_feux.plan.elementAt(this.plan_feux.pt).date_f;
                    if (date >= df) {
                        etape = 2;
                        AC.ArreterPhase(this.plan_feux.pt);
                        r = AC.rouge_integral;
                    } else {
                        block(1000);
                    }
                }
                break;
                case 2: {
                    r = (int) (AC.rouge_integral - (System.currentTimeMillis() - date));
                    if (r > 100) {
                        block(r);
                    } else {
                        etape = 3;
                    }
                }
                break;
                case 3: {
                    this.plan_feux.pt++;
                    if (this.plan_feux.pt >= this.plan_feux.plan.size()) {
                        this.AC.Historique.Ajouter_Plan(Plan);
                        this.plan_feux.Init_Plan();
                        etape = 0;
                    } else {
                        df = this.plan_feux.plan.elementAt(this.plan_feux.pt).date_f;
                        AC.ExecuterPhase(this.plan_feux.pt);
                        etape = 1;
                    }
                }
                break;
            }
        }
    }

    // afficher un message d'erreur:
    void Erreur_MSG(String S) {
        JOptionPane.showMessageDialog(null, "Agent Carrefour: " + S, "Erreur ", JOptionPane.WARNING_MESSAGE);
    }
}
