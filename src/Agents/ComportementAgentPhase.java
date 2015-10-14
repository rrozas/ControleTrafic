/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Agents;

import Reseau.Arc;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.JOptionPane;

/**
 *
 * @author Dihya
 */
public class ComportementAgentPhase extends CyclicBehaviour{
        AgentPhase AP;
        MessageTemplate temp1, temp2;
        ACLMessage msg, reply;
        int t, cout, nbre, etape;
        float degre_congestion=(float) 0.0;
        Vector<Element> liste; 
        public ComportementAgentPhase(AgentPhase ap){
            this.AP=ap;
            temp1=MessageTemplate.MatchProtocol(AP.ProtoCalculCycle);
            temp2=MessageTemplate.MatchProtocol(AP.ProtoInfo);
            this.liste=new Vector<Element>();
            this.cout=1;
            this.etape=0;
            
        }

    @Override
    public void action() {
 
            switch(etape){
                case 0:{
                    msg=AP.receive(temp1);
                    if(msg!=null && (AP.phase.E.size()>0)){
                        t=Integer.valueOf(msg.getContent()).intValue();                        
                        etape=1;
                        Init_Liste();
                    }else{
                        block();
                    }
                }
                break;
                case 1:{                    
                    this.Reinit_Liste();
                    if(t>0){
                        nbre=EnvoyerMsgDmdeInfo();
                        if(nbre>0){  // si des demandes d'info sont envoyees
                            etape=2; // aller a reception des reponses
                        }else{
                            etape=3;
                        }
                    }else{
                        etape=3;
                    }
                }
                break;
                case 2:{
                    msg=AP.receive(temp2);
                    if(msg!=null){
                       Object[] obj = null;
                       try {
                          obj = (Object[]) msg.getContentObject();
                       } catch (UnreadableException ex) {
                          Erreur_MSG(ex.toString());
                       }
                       this.Ajouter_Liste(((Integer)obj[0]).intValue(), ((Integer)obj[1]).intValue());
                       nbre--;
                       if(nbre==0){
                         etape=3;
                       }                      
                    }else{
                        this.block();
                    }
                }
                break;
                case 3:{
                    Calculer_Durees();
                    Trier_Liste();
                    int d=liste.lastElement().duree;
                    msg=new ACLMessage(ACLMessage.INFORM);
                    msg.setProtocol(AP.ProtoCalculCycle);
                    msg.addReceiver(AP.agent_carrefour);
                    Object[] obj2={this.AP.phase.code_phase, d, this.degre_congestion};                    
                    try {
                       msg.setContentObject(obj2);
                    } catch (IOException ex) {
                       Erreur_MSG(ex.toString());
                    }
                    this.AP.send(msg);
                    etape=4;
                }
                break;
                case 4:{
                    msg=AP.receive(temp1);
                    if(msg!=null){
                        switch(msg.getPerformative()){
                            case ACLMessage.CONFIRM:
                                etape=0;
                                break;
                            case ACLMessage.REQUEST:
                                t=Integer.valueOf(msg.getContent()).intValue();
                                etape=1;
                                break;
                            case ACLMessage.CFP:                                
                                cout=Integer.valueOf(msg.getContent()).intValue();
                                int d=GetProposition(cout);
                                reply=msg.createReply();
                                Object[] obj={this.AP.phase.code_phase, d};
                                try {
                                  reply.setContentObject(obj);
                                } catch (IOException ex) {
                                   Erreur_MSG(ex.toString());
                                }
                                reply.setPerformative(ACLMessage.PROPOSE);
                                AP.send(reply);
                                break;
                        }
                    }else{
                        this.block();
                    }
                }
                break;
            }        
    }

    private void Ajouter_Liste(int cod_arc_entrant, int vol) {
        int i=liste.size()-1;
        boolean trouv=false;
        Element e;
        while (!trouv && i>=0){
            e=liste.elementAt(i);
            if(e.arc.code_arc==cod_arc_entrant){
                trouv=true;                
                e.vol_sup=vol;
            }else{
                i--;
            }
        }
    }

    private void Calculer_Durees() {
        int T=AP.vert_min;    // le temps requis par la phase est initialisé au vert minimum
        int Fi=0;          // la taille de la file formee sur l'arc ai
        int Ti=0;
        int vol;
        Arc ai;
        Element e;
        this.degre_congestion=0;
        float d= (float) 0.0;
        for(int i=0; i<this.liste.size(); i++){
            e=this.liste.elementAt(i);
            ai=e.arc;     // l'arc entrant ai;
            if(ai.longueur<=200){
                vol=e.arc.nbre_veh+e.vol_sup/2;
            }else{
                vol=e.arc.nbre_veh;
            }
            d= (float)vol/e.arc.capacite;
            this.degre_congestion=(float) (degre_congestion + Math.exp(d));//+Math.exp(ai.nbre_bus)*ai.nbre_bus);
            Fi=Math.round((vol*ai.longueur)/ai.capacite);         // Fi=Ni*Li/Ci
            Ti=Math.round((ai.nbre_veh/(float)(ai.debit*1.5))+((Fi*36)/(ai.vitesse_moyenne*10)));  // Ti=(Ni/di)+(Fi/Vi)
            T=Math.max(AP.vert_min, Ti);
            T=Math.min(T, AP.vert_max);
            e.duree=T;            
        }

    }

    private int EnvoyerMsgDmdeInfo() {
        Iterator it=this.AP.phase.E.iterator();
        int nb=0;
        Arc a;
        msg=new ACLMessage(ACLMessage.REQUEST);
        msg.setProtocol(AP.ProtoInfo);
        while (it.hasNext()){
            a= (Arc) it.next();
            if(a.Origine!=null){
                msg.clearAllReceiver();
                msg.addReceiver(a.Origine.agent_carrefour);
                msg.setContent(Integer.toString(a.code_arc));
                AP.send(msg);
                nb++;
            }
        }        
        return nb;
    }

    private int GetProposition(int cout) {
        int max=this.liste.lastElement().duree;
        int quota=max-AP.vert_min;
        if(cout<liste.size()){
            quota=max-liste.elementAt(liste.size()-cout-1).duree;
        }
        return quota;
    }

    private void Init_Liste() {
        Iterator it=this.AP.phase.E.iterator();
        Arc a;
        this.liste.clear();
        while(it.hasNext()){
            Element e=new Element();
            a= (Arc) it.next();
            e.arc=a;
            e.duree=0;
            e.vol_sup=0;
            this.liste.addElement(e);
        }
        it=null;
    }
    private void Reinit_Liste(){
        int i=this.liste.size()-1;
        Element e;
        while (i>=0){
            e=liste.elementAt(i);
            e.vol_sup=0;
            e.duree=0;
            i--;
        }        
    }

    private void Trier_Liste() {
        boolean triee=false;
        int BMax=liste.size()-1;
        Element e1, e2;
        while (!triee){
            triee=true;
            for(int i=0;i<BMax; i++){
                e1=liste.elementAt(i);
                e2=liste.elementAt(i+1);
                if(e1.duree>e2.duree){
                    triee=false;
                    liste.setElementAt(liste.set(i, e2), i+1);
                }
            }
        }
    }
    // afficher un message d'erreur:
    static void Erreur_MSG(String S){
        JOptionPane.showMessageDialog(null,"Agent phase.comportement: "+S,"Erreur ", JOptionPane.WARNING_MESSAGE);
    }

    ///////////////////////////////
    class Element{
        Arc arc;
        int vol_sup;
        int duree;
    }

}
