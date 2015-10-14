
package controletrafic;

import Agents.AgentReseau;
import Reseau.*;
import jade.core.Profile;
import jade.core.Runtime;
import jade.core.ProfileImpl;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import java.awt.geom.Line2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.swing.JOptionPane;
/**
 *
 * @author HACIANE
 */
public class Main {
    
    static AgentContainer conteneur;    
    public static void main(String[] args) { 
        
        //  Supprimer fichier log
        File MyFile = new File("log.csv");
        MyFile.delete();
        
        // démarrage de la plateforme
        try {
               String[] args2 = {"-gui"};
               jade.Boot.main(args2);
         }catch (Exception ex) {
             Erreur_MSG(ex.toString());
         }
        // Creation du conteur "Agents" qui contiendra les agent du système
        try {
                  Runtime rt = Runtime.instance();
                  Profile pro=new ProfileImpl();                  
                  pro.setParameter(Profile.CONTAINER_NAME, "Agents");
                  conteneur= rt.createAgentContainer(pro);                  
         }catch (Exception ex1) {
                Erreur_MSG(ex1.toString());
         }
        // recupere le reseau à partir du disque:
        //Config config=Ouvrir();        
        Config config = creerExemple();
        config.R.Retard_Veh.clear();
        config.R.Retard_Bus.clear();                                                      
        // creation de l'agent réseau:
        Object[] args2={config};
        AgentController myagent;
        try {            
            myagent = conteneur.createNewAgent("AgentReseau", AgentReseau.class.getName(), args2);
            myagent.start();
        } catch (StaleProxyException ex) {
            Erreur_MSG(ex.toString());
        }        
    }
    ////////////
    private static Config Ouvrir() {
        Config conf = null;
        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(new FileInputStream("Config"));
            conf = (Config) in.readObject();
            conf.R.REinit();
            in.close();
        } catch (Exception ex) {
            conf=new Config();            
        }
        return conf;        
    }
    
    
       private static Config creerExemple(){
    Config conf;
    conf = new Config();
    //configuration du réseaux
    /*
    for(int i=1;i<9;i++)
        conf.R.Ajouter_Carrefour("C"+i);
    
    conf.R.Ajouter_Arc(1, 300, 40, 30 , 10, conf.R.Rech_Carrefour(" "), conf.R.Rech_Carrefour("C1"));
    conf.R.Ajouter_Arc(2, 300, 80, 30 , 10, conf.R.Rech_Carrefour("C1"), conf.R.Rech_Carrefour("C2"));
    conf.R.Ajouter_Arc(3, 400, 100, 30 , 10, conf.R.Rech_Carrefour("C1"), conf.R.Rech_Carrefour("C3"));
    conf.R.Ajouter_Arc(4, 300, 60, 30 , 10, conf.R.Rech_Carrefour("C2"), conf.R.Rech_Carrefour("C4"));
    conf.R.Ajouter_Arc(5, 300, 40, 30 , 10, conf.R.Rech_Carrefour("C3"), conf.R.Rech_Carrefour("C5"));
    conf.R.Ajouter_Arc(6, 400, 40, 30 , 10, conf.R.Rech_Carrefour("C2"), conf.R.Rech_Carrefour("C5"));
    conf.R.Ajouter_Arc(7, 400, 40, 30 , 10, conf.R.Rech_Carrefour("C4"), conf.R.Rech_Carrefour("C5"));
    conf.R.Ajouter_Arc(8, 300, 60, 30 , 10, conf.R.Rech_Carrefour("C4"), conf.R.Rech_Carrefour("C8"));
    conf.R.Ajouter_Arc(9, 300, 40, 30 , 10, conf.R.Rech_Carrefour("C5"), conf.R.Rech_Carrefour("C8"));
    conf.R.Ajouter_Arc(10, 300, 40, 30 , 10, conf.R.Rech_Carrefour("C2"), conf.R.Rech_Carrefour("C6"));
    conf.R.Ajouter_Arc(11, 300, 40, 30 , 10, conf.R.Rech_Carrefour("C6"), conf.R.Rech_Carrefour("C7"));
    conf.R.Ajouter_Arc(12, 300, 40, 30 , 10, conf.R.Rech_Carrefour("C3"), conf.R.Rech_Carrefour("C9"));
    conf.R.Ajouter_Arc(13, 300, 40, 30 , 10, conf.R.Rech_Carrefour("C5"), conf.R.Rech_Carrefour("C9"));
    conf.R.Ajouter_Arc(14, 300, 40, 30 , 10, conf.R.Rech_Carrefour("C7"), conf.R.Rech_Carrefour(" "));
    conf.R.Ajouter_Arc(15, 300, 40, 30 , 10, conf.R.Rech_Carrefour("C9"), conf.R.Rech_Carrefour(" "));
    conf.R.Ajouter_Arc(16, 300, 40, 30 , 10, conf.R.Rech_Carrefour("C8"), conf.R.Rech_Carrefour(" "));
    
    conf.SIM.vitesse_simul = 2000;
    
    conf.R.Rech_Arc(1).Ajouter_Suivant(conf.R.Rech_Arc(2), 10);
    conf.R.Rech_Arc(1).Ajouter_Suivant(conf.R.Rech_Arc(3), 10);
    conf.R.Rech_Arc(2).Ajouter_Suivant(conf.R.Rech_Arc(4), 10);
    conf.R.Rech_Arc(2).Ajouter_Suivant(conf.R.Rech_Arc(6), 10);
    conf.R.Rech_Arc(2).Ajouter_Suivant(conf.R.Rech_Arc(10), 10);
    conf.R.Rech_Arc(3).Ajouter_Suivant(conf.R.Rech_Arc(5), 10);
    conf.R.Rech_Arc(3).Ajouter_Suivant(conf.R.Rech_Arc(12), 10);
    conf.R.Rech_Arc(4).Ajouter_Suivant(conf.R.Rech_Arc(7), 10);
    conf.R.Rech_Arc(4).Ajouter_Suivant(conf.R.Rech_Arc(8), 10);
    conf.R.Rech_Arc(5).Ajouter_Suivant(conf.R.Rech_Arc(9), 10);
    conf.R.Rech_Arc(5).Ajouter_Suivant(conf.R.Rech_Arc(13), 10);
    conf.R.Rech_Arc(6).Ajouter_Suivant(conf.R.Rech_Arc(9), 10);
    conf.R.Rech_Arc(6).Ajouter_Suivant(conf.R.Rech_Arc(13), 10);
    conf.R.Rech_Arc(7).Ajouter_Suivant(conf.R.Rech_Arc(9), 10);
    conf.R.Rech_Arc(7).Ajouter_Suivant(conf.R.Rech_Arc(13), 10);
    conf.R.Rech_Arc(8).Ajouter_Suivant(conf.R.Rech_Arc(16), 10);
    conf.R.Rech_Arc(9).Ajouter_Suivant(conf.R.Rech_Arc(16), 10);
    conf.R.Rech_Arc(10).Ajouter_Suivant(conf.R.Rech_Arc(11), 10);
    conf.R.Rech_Arc(11).Ajouter_Suivant(conf.R.Rech_Arc(14), 10);
    conf.R.Rech_Arc(12).Ajouter_Suivant(conf.R.Rech_Arc(15), 10);
    conf.R.Rech_Arc(13).Ajouter_Suivant(conf.R.Rech_Arc(15), 10);
    
    conf.R.Ajouter_Source("S1", 5, 1);
    
    
    int[]C1 = {150,300};
    int[]C2 = {250,250};
    int[]C3 = {250,350};
    int[]C4 = {400,250};
    int[]C5 = {400,350};
    int[]C6 = {350,200};
    int[]C7 = {500,200};
    int[]C8 = {550,250};
    int[]C9 = {350,400};
    int x1, x2, y1, y2;
    x1 = 10;y1 = 300 ;x2 = 150;y2= 300 ;
conf.SIM.ListeLignes.add(new Line2D.Double(x1,y1,C1[0], C1[1]));
    conf.SIM.AjouterArc(x1, y1, C1[0], C1[1], conf.R.Rech_Arc(1));
conf.SIM.ListeLignes.add(new Line2D.Double(C1[0], C1[1],C2[0], C2[1]));
    conf.SIM.AjouterArc(C1[0], C1[1], C2[0], C2[1], conf.R.Rech_Arc(2));
conf.SIM.ListeLignes.add(new Line2D.Double(C1[0], C1[1],C3[0], C3[1]));
    conf.SIM.AjouterArc(C1[0], C1[1], C3[0], C3[1], conf.R.Rech_Arc(3));    
conf.SIM.ListeLignes.add(new Line2D.Double(C2[0], C2[1],C4[0], C4[1]));
    conf.SIM.AjouterArc(C2[0], C2[1],C4[0], C4[1], conf.R.Rech_Arc(4));
conf.SIM.ListeLignes.add(new Line2D.Double(C4[0], C4[1],C8[0], C8[1]));
    conf.SIM.AjouterArc(C4[0], C4[1],C8[0], C8[1], conf.R.Rech_Arc(8));
    x2 = 700;y2= 250 ;
conf.SIM.ListeLignes.add(new Line2D.Double(C8[0], C8[1],x2,y2));
    conf.SIM.AjouterArc(C8[0], C8[1],x2,y2, conf.R.Rech_Arc(16));
conf.SIM.ListeLignes.add(new Line2D.Double(C2[0], C2[1],C6[0], C6[1]));
    conf.SIM.AjouterArc(C2[0], C2[1],C6[0], C6[1], conf.R.Rech_Arc(10));
    x2 = 500;y2= 200 ;
conf.SIM.ListeLignes.add(new Line2D.Double(C6[0], C6[1],C7[0], C7[1]));
    conf.SIM.AjouterArc(C6[0], C6[1],C7[0], C7[1], conf.R.Rech_Arc(11));
        x2 = 650;y2= 200 ;
conf.SIM.ListeLignes.add(new Line2D.Double(C7[0], C7[1], x2, y2));
    conf.SIM.AjouterArc(C7[0], C7[1], x2, y2, conf.R.Rech_Arc(14));
conf.SIM.ListeLignes.add(new Line2D.Double(C3[0], C3[1], C5[0], C5[1]));
    conf.SIM.AjouterArc(C3[0], C3[1], C5[0], C5[1], conf.R.Rech_Arc(5));
conf.SIM.ListeLignes.add(new Line2D.Double(C2[0], C2[1], C5[0], C5[1]));
    conf.SIM.AjouterArc(C2[0], C2[1], C5[0], C5[1], conf.R.Rech_Arc(6));
conf.SIM.ListeLignes.add(new Line2D.Double(C4[0], C4[1], C5[0], C5[1]));
    conf.SIM.AjouterArc(C4[0], C4[1], C5[0], C5[1], conf.R.Rech_Arc(7));
conf.SIM.ListeLignes.add(new Line2D.Double(C5[0], C5[1], C8[0], C8[1]));
    conf.SIM.AjouterArc(C5[0], C5[1], C8[0], C8[1], conf.R.Rech_Arc(9));
conf.SIM.ListeLignes.add(new Line2D.Double(C5[0], C5[1], C9[0], C9[1]));
    conf.SIM.AjouterArc(C5[0], C5[1], C9[0], C9[1], conf.R.Rech_Arc(13));
conf.SIM.ListeLignes.add(new Line2D.Double(C3[0], C3[1], C9[0], C9[1]));
    conf.SIM.AjouterArc(C3[0], C3[1], C9[0], C9[1], conf.R.Rech_Arc(12));
                x2 = 500;y2= 400 ;
    conf.SIM.ListeLignes.add(new Line2D.Double(C9[0],C9[1],x2,y2));
    conf.SIM.AjouterArc(C9[0],C9[1], x2, y2, conf.R.Rech_Arc(15));    
            */
    
    
    
    
    
     //configuration du réseaux
    
   
        
    
  /*  conf.R.Ajouter_Arc(1, 300, 40, 30 , 10, conf.R.Rech_Carrefour(" "), conf.R.Rech_Carrefour("C1"));
    conf.R.Ajouter_Arc(2, 300, 80, 30 , 10, conf.R.Rech_Carrefour("C1"), conf.R.Rech_Carrefour("C2"));
    conf.R.Ajouter_Arc(3, 400, 100, 30 , 10, conf.R.Rech_Carrefour("C1"), conf.R.Rech_Carrefour("C3"));
    conf.R.Ajouter_Arc(4, 300, 60, 30 , 10, conf.R.Rech_Carrefour("C2"), conf.R.Rech_Carrefour("C4"));
    conf.R.Ajouter_Arc(5, 300, 40, 30 , 10, conf.R.Rech_Carrefour("C3"), conf.R.Rech_Carrefour("C5"));
    conf.R.Ajouter_Arc(6, 400, 40, 30 , 10, conf.R.Rech_Carrefour("C2"), conf.R.Rech_Carrefour("C5"));
    conf.R.Ajouter_Arc(7, 400, 40, 30 , 10, conf.R.Rech_Carrefour("C4"), conf.R.Rech_Carrefour("C5"));
    conf.R.Ajouter_Arc(8, 300, 60, 30 , 10, conf.R.Rech_Carrefour("C4"), conf.R.Rech_Carrefour("C8"));
    conf.R.Ajouter_Arc(9, 300, 40, 30 , 10, conf.R.Rech_Carrefour("C5"), conf.R.Rech_Carrefour("C8"));
    conf.R.Ajouter_Arc(10, 300, 40, 30 , 10, conf.R.Rech_Carrefour("C2"), conf.R.Rech_Carrefour("C6"));
    conf.R.Ajouter_Arc(11, 300, 40, 30 , 10, conf.R.Rech_Carrefour("C6"), conf.R.Rech_Carrefour("C7"));
    conf.R.Ajouter_Arc(12, 300, 40, 30 , 10, conf.R.Rech_Carrefour("C3"), conf.R.Rech_Carrefour("C9"));
    conf.R.Ajouter_Arc(13, 300, 40, 30 , 10, conf.R.Rech_Carrefour("C5"), conf.R.Rech_Carrefour("C9"));
    conf.R.Ajouter_Arc(14, 300, 40, 30 , 10, conf.R.Rech_Carrefour("C7"), conf.R.Rech_Carrefour(" "));
    conf.R.Ajouter_Arc(15, 300, 40, 30 , 10, conf.R.Rech_Carrefour("C9"), conf.R.Rech_Carrefour(" "));
    conf.R.Ajouter_Arc(16, 300, 40, 30 , 10, conf.R.Rech_Carrefour("C8"), conf.R.Rech_Carrefour(" "));
    
    conf.SIM.vitesse_simul = 2000;
    
    conf.R.Rech_Arc(1).Ajouter_Suivant(conf.R.Rech_Arc(2), 10);
    conf.R.Rech_Arc(1).Ajouter_Suivant(conf.R.Rech_Arc(3), 10);
    conf.R.Rech_Arc(2).Ajouter_Suivant(conf.R.Rech_Arc(4), 10);
    conf.R.Rech_Arc(2).Ajouter_Suivant(conf.R.Rech_Arc(6), 10);
    conf.R.Rech_Arc(2).Ajouter_Suivant(conf.R.Rech_Arc(10), 10);
    conf.R.Rech_Arc(3).Ajouter_Suivant(conf.R.Rech_Arc(5), 10);
    conf.R.Rech_Arc(3).Ajouter_Suivant(conf.R.Rech_Arc(12), 10);
    conf.R.Rech_Arc(4).Ajouter_Suivant(conf.R.Rech_Arc(7), 10);
    conf.R.Rech_Arc(4).Ajouter_Suivant(conf.R.Rech_Arc(8), 10);
    conf.R.Rech_Arc(5).Ajouter_Suivant(conf.R.Rech_Arc(9), 10);
    conf.R.Rech_Arc(5).Ajouter_Suivant(conf.R.Rech_Arc(13), 10);
    conf.R.Rech_Arc(6).Ajouter_Suivant(conf.R.Rech_Arc(9), 10);
    conf.R.Rech_Arc(6).Ajouter_Suivant(conf.R.Rech_Arc(13), 10);
    conf.R.Rech_Arc(7).Ajouter_Suivant(conf.R.Rech_Arc(9), 10);
    conf.R.Rech_Arc(7).Ajouter_Suivant(conf.R.Rech_Arc(13), 10);
    conf.R.Rech_Arc(8).Ajouter_Suivant(conf.R.Rech_Arc(16), 10);
    conf.R.Rech_Arc(9).Ajouter_Suivant(conf.R.Rech_Arc(16), 10);
    conf.R.Rech_Arc(10).Ajouter_Suivant(conf.R.Rech_Arc(11), 10);
    conf.R.Rech_Arc(11).Ajouter_Suivant(conf.R.Rech_Arc(14), 10);
    conf.R.Rech_Arc(12).Ajouter_Suivant(conf.R.Rech_Arc(15), 10);
    conf.R.Rech_Arc(13).Ajouter_Suivant(conf.R.Rech_Arc(15), 10);
    
    conf.R.Ajouter_Source("S1", 5, 1);
    
    
    int[]C1 = {150,300};
    int[]C2 = {250,250};
    int[]C3 = {250,350};
    int[]C4 = {400,250};
    int[]C5 = {400,350};
    int[]C6 = {350,200};
    int[]C7 = {500,200};
    int[]C8 = {550,250};
    int[]C9 = {350,400};
    int x1, x2, y1, y2;
    x1 = 10;y1 = 300 ;x2 = 150;y2= 300 ;
conf.SIM.ListeLignes.add(new Line2D.Double(x1,y1,C1[0], C1[1]));
    conf.SIM.AjouterArc(x1, y1, C1[0], C1[1], conf.R.Rech_Arc(1));
conf.SIM.ListeLignes.add(new Line2D.Double(C1[0], C1[1],C2[0], C2[1]));
    conf.SIM.AjouterArc(C1[0], C1[1], C2[0], C2[1], conf.R.Rech_Arc(2));
conf.SIM.ListeLignes.add(new Line2D.Double(C1[0], C1[1],C3[0], C3[1]));
    conf.SIM.AjouterArc(C1[0], C1[1], C3[0], C3[1], conf.R.Rech_Arc(3));    
conf.SIM.ListeLignes.add(new Line2D.Double(C2[0], C2[1],C4[0], C4[1]));
    conf.SIM.AjouterArc(C2[0], C2[1],C4[0], C4[1], conf.R.Rech_Arc(4));
conf.SIM.ListeLignes.add(new Line2D.Double(C4[0], C4[1],C8[0], C8[1]));
    conf.SIM.AjouterArc(C4[0], C4[1],C8[0], C8[1], conf.R.Rech_Arc(8));
    x2 = 700;y2= 250 ;
conf.SIM.ListeLignes.add(new Line2D.Double(C8[0], C8[1],x2,y2));
    conf.SIM.AjouterArc(C8[0], C8[1],x2,y2, conf.R.Rech_Arc(16));
conf.SIM.ListeLignes.add(new Line2D.Double(C2[0], C2[1],C6[0], C6[1]));
    conf.SIM.AjouterArc(C2[0], C2[1],C6[0], C6[1], conf.R.Rech_Arc(10));
    x2 = 500;y2= 200 ;
conf.SIM.ListeLignes.add(new Line2D.Double(C6[0], C6[1],C7[0], C7[1]));
    conf.SIM.AjouterArc(C6[0], C6[1],C7[0], C7[1], conf.R.Rech_Arc(11));
        x2 = 650;y2= 200 ;
conf.SIM.ListeLignes.add(new Line2D.Double(C7[0], C7[1], x2, y2));
    conf.SIM.AjouterArc(C7[0], C7[1], x2, y2, conf.R.Rech_Arc(14));
conf.SIM.ListeLignes.add(new Line2D.Double(C3[0], C3[1], C5[0], C5[1]));
    conf.SIM.AjouterArc(C3[0], C3[1], C5[0], C5[1], conf.R.Rech_Arc(5));
conf.SIM.ListeLignes.add(new Line2D.Double(C2[0], C2[1], C5[0], C5[1]));
    conf.SIM.AjouterArc(C2[0], C2[1], C5[0], C5[1], conf.R.Rech_Arc(6));
conf.SIM.ListeLignes.add(new Line2D.Double(C4[0], C4[1], C5[0], C5[1]));
    conf.SIM.AjouterArc(C4[0], C4[1], C5[0], C5[1], conf.R.Rech_Arc(7));
conf.SIM.ListeLignes.add(new Line2D.Double(C5[0], C5[1], C8[0], C8[1]));
    conf.SIM.AjouterArc(C5[0], C5[1], C8[0], C8[1], conf.R.Rech_Arc(9));
conf.SIM.ListeLignes.add(new Line2D.Double(C5[0], C5[1], C9[0], C9[1]));
    conf.SIM.AjouterArc(C5[0], C5[1], C9[0], C9[1], conf.R.Rech_Arc(13));
conf.SIM.ListeLignes.add(new Line2D.Double(C3[0], C3[1], C9[0], C9[1]));
    conf.SIM.AjouterArc(C3[0], C3[1], C9[0], C9[1], conf.R.Rech_Arc(12));
                x2 = 500;y2= 400 ;
    conf.SIM.ListeLignes.add(new Line2D.Double(C9[0],C9[1],x2,y2));
    conf.SIM.AjouterArc(C9[0],C9[1], x2, y2, conf.R.Rech_Arc(15));   
         
    return conf;
    }
    // afficher un message d'erreur:
    static void Erreur_MSG(String S){
        JOptionPane.showMessageDialog(null, "Class Main: "+S,"Erreur ", JOptionPane.WARNING_MESSAGE);
    }
}
*/
    
    return conf;
    } 
    
    // afficher un message d'erreur:
    static void Erreur_MSG(String S){
        JOptionPane.showMessageDialog(null, "Class Main: "+S,"Erreur ", JOptionPane.WARNING_MESSAGE);
    }

  

}
