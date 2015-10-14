/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Agents;

import Reseau.Phase;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;


/**
 *
 * @author Dihya
 */
public class AgentPhase extends Agent{
    //////////////////////// Données Statiques //////////////////////////
    AID agent_carrefour;
    Phase phase;
    int vert_min=6;
    int vert_max=60;

    ///////////////////////  données dynamiques  ////////////////////////

    /////////////////////// autres données /////////////////////////////
    final String ProtoCmd="commandes";
    final String ProtoCalculCycle="CalculCycle";
    final String ProtoInfo="volume_envoye?";
    final String ProtoInscription="Inscription";

    @Override
    protected void setup(){
        this.agent_carrefour=(AID) (this.getArguments()[0]); //
        this.phase=(Phase) (this.getArguments()[1]);         // la phase gérée par cette agent
        this.phase.agent=this.getAID();        
        ///// Ajout des comportements initiaux
        this.addBehaviour(new commandes(this));
        this.addBehaviour(new ComportementAgentPhase(this));
        //
        ACLMessage msg=new  ACLMessage(ACLMessage.INFORM);
        msg.setProtocol(this.ProtoInscription);
        msg.addReceiver(agent_carrefour);
        this.send(msg);
    }

    
    //------------------------------- fin méthodes ---------------------------------//


    /////////////// Classes utilisée par l'agent Phase ///////////////////////
    class commandes extends CyclicBehaviour{
        AgentPhase AF;
        MessageTemplate template;
        ACLMessage msg;
        String s;
        commandes(AgentPhase af){
           this.AF=af;
           template=MessageTemplate.MatchProtocol(AF.ProtoCmd);
        }
        @Override
        public void action() {
            msg = myAgent.receive(template);
            if(msg!=null){
                s=msg.getContent();
                if(s.equals("arreter")){  // arreter l'agent phase.
                    ACLMessage reply=msg.createReply();
                    reply.setContent("ok");
                    myAgent.send(reply);                    
                    myAgent.doDelete();
                }
            }else{
                this.block();
            }
        }
    }
    

}
