/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controletrafic;

import Agents.AgentReseau;
import Reseau.Config;
import static controletrafic.Main.Erreur_MSG;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import java.awt.geom.Line2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import javax.swing.JOptionPane;

/**
 *
 * @author Dihya
 * 
 */
import java.util.Scanner;
import java.io.File;

public class Main2 {
    

    
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
                  jade.core.Runtime rt = jade.core.Runtime.instance();
                  Profile pro=new ProfileImpl();                  
                  pro.setParameter(Profile.CONTAINER_NAME, "Agents");
                  conteneur= rt.createAgentContainer(pro);                  
         }catch (Exception ex1) {
                Erreur_MSG(ex1.toString());
         }
        // recupere le reseau à partir du disque:
        Config config=creerExemple( conteneur );
        //Config config = Ouvrir();
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
    /*  private static Config creerExemple(){
    Config conf;
    conf = new Config();
    //configuration du réseaux
  for(int i=1;i<21;i++)
        conf.R.Ajouter_Carrefour(i,0,0);
    
    //conf.R.Ajouter_Arc(1, 300, 40, 30 , 10,  conf.R.Rech_Carrefour(), conf.R.Rech_Carrefour(1));
   conf.R.Ajouter_Arc(2, 300, 80, 30 , 10, conf.R.Rech_Carrefour(1), conf.R.Rech_Carrefour(2));
 conf.R.Ajouter_Arc(3, 400, 100, 30 , 10, conf.R.Rech_Carrefour(1), conf.R.Rech_Carrefour(3));
    conf.R.Ajouter_Arc(4, 300, 60, 30 , 10, conf.R.Rech_Carrefour(2), conf.R.Rech_Carrefour(4));
    conf.R.Ajouter_Arc(5, 300, 40, 30 , 10, conf.R.Rech_Carrefour(3), conf.R.Rech_Carrefour(5));
    conf.R.Ajouter_Arc(6, 400, 40, 30 , 10, conf.R.Rech_Carrefour(2), conf.R.Rech_Carrefour(5));
    conf.R.Ajouter_Arc(7, 400, 40, 30 , 10, conf.R.Rech_Carrefour(4), conf.R.Rech_Carrefour(5));
    conf.R.Ajouter_Arc(8, 300, 60, 30 , 10, conf.R.Rech_Carrefour(4), conf.R.Rech_Carrefour(8));
    conf.R.Ajouter_Arc(9, 300, 40, 30 , 10, conf.R.Rech_Carrefour(5), conf.R.Rech_Carrefour(8));
    conf.R.Ajouter_Arc(10, 300, 40, 30 , 10, conf.R.Rech_Carrefour(2), conf.R.Rech_Carrefour(6));
    conf.R.Ajouter_Arc(11, 300, 40, 30 , 10, conf.R.Rech_Carrefour(6), conf.R.Rech_Carrefour(7));
    conf.R.Ajouter_Arc(12, 300, 40, 30 , 10, conf.R.Rech_Carrefour(3), conf.R.Rech_Carrefour(9));
    conf.R.Ajouter_Arc(13, 300, 40, 30 , 10, conf.R.Rech_Carrefour(5), conf.R.Rech_Carrefour(9));
   // conf.R.Ajouter_Arc(14, 300, 40, 30 , 10, conf.R.Rech_Carrefour(7), conf.R.Rech_Carrefour(" "),'v');
   // conf.R.Ajouter_Arc(15, 300, 40, 30 , 10, conf.R.Rech_Carrefour(9), conf.R.Rech_Carrefour(" "),'v');
   // conf.R.Ajouter_Arc(16, 300, 40, 30 , 10, conf.R.Rech_Carrefour("C8"), conf.R.Rech_Carrefour(" "),'v');
    conf.R.Ajouter_Arc(17, 300, 40, 30 , 10, conf.R.Rech_Carrefour(10), conf.R.Rech_Carrefour(11));
    conf.R.Ajouter_Arc(18, 300, 40, 30 , 10, conf.R.Rech_Carrefour(11), conf.R.Rech_Carrefour(12));
    conf.R.Ajouter_Arc(19, 300, 40, 30 , 10, conf.R.Rech_Carrefour(12), conf.R.Rech_Carrefour(13));
    conf.R.Ajouter_Arc(20, 300, 40, 30 , 10, conf.R.Rech_Carrefour(13), conf.R.Rech_Carrefour(14));
    conf.R.Ajouter_Arc(21, 300, 40, 30 , 10, conf.R.Rech_Carrefour(15), conf.R.Rech_Carrefour(16));
    conf.R.Ajouter_Arc(22, 300, 40, 30 , 10, conf.R.Rech_Carrefour(16), conf.R.Rech_Carrefour(17));
    conf.R.Ajouter_Arc(23, 300, 40, 30 , 10, conf.R.Rech_Carrefour(17), conf.R.Rech_Carrefour(18));
    conf.R.Ajouter_Arc(24, 300, 40, 30 , 10, conf.R.Rech_Carrefour(18), conf.R.Rech_Carrefour(19));
    //conf.R.Ajouter_Arc(25, 300, 40, 30 , 10, conf.R.Rech_Carrefour(""), conf.R.Rech_Carrefour("C15"),'b');
    // conf.R.Ajouter_Arc(26, 300, 40, 30 , 10, conf.R.Rech_Carrefour(""), conf.R.Rech_Carrefour("C10"),'b');
     conf.R.Ajouter_Arc(27, 300, 40,30, 10, conf.R.Rech_Carrefour(6), conf.R.Rech_Carrefour(12));
     conf.R.Ajouter_Arc(28, 300, 40,30, 10, conf.R.Rech_Carrefour(12), conf.R.Rech_Carrefour(16));
     conf.R.Ajouter_Arc(29, 300, 40,30, 10, conf.R.Rech_Carrefour(7), conf.R.Rech_Carrefour(17));
      conf.R.Ajouter_Arc(30, 300, 40, 30 , 10, conf.R.Rech_Carrefour(19), conf.R.Rech_Carrefour(8));
    conf.SIM.vitesse_simul = 2000;
    
    conf.R.Rech_Arc(1).Ajouter_Suivant(conf.R.Rech_Arc(2), 30);
    conf.R.Rech_Arc(1).Ajouter_Suivant(conf.R.Rech_Arc(3), 10);
    conf.R.Rech_Arc(1).Ajouter_Suivant(conf.R.Rech_Arc(25), 10);
    conf.R.Rech_Arc(1).Ajouter_Suivant(conf.R.Rech_Arc(26), 10);
    conf.R.Rech_Arc(26).Ajouter_Suivant(conf.R.Rech_Arc(17), 10);
    conf.R.Rech_Arc(25).Ajouter_Suivant(conf.R.Rech_Arc(21), 10);
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
    conf.R.Rech_Arc(17).Ajouter_Suivant(conf.R.Rech_Arc(18), 10);
    conf.R.Rech_Arc(18).Ajouter_Suivant(conf.R.Rech_Arc(19), 10);
    conf.R.Rech_Arc(19).Ajouter_Suivant(conf.R.Rech_Arc(20), 10);
    conf.R.Rech_Arc(21).Ajouter_Suivant(conf.R.Rech_Arc(22), 10);
    conf.R.Rech_Arc(22).Ajouter_Suivant(conf.R.Rech_Arc(23), 10);
    conf.R.Rech_Arc(23).Ajouter_Suivant(conf.R.Rech_Arc(24), 10);
    conf.R.Rech_Arc(27).Ajouter_Suivant(conf.R.Rech_Arc(19), 10);
    conf.R.Rech_Arc(28).Ajouter_Suivant(conf.R.Rech_Arc(22), 10);
     conf.R.Rech_Arc(29).Ajouter_Suivant(conf.R.Rech_Arc(23), 10);

      conf.R.Rech_Arc(30).Ajouter_Suivant(conf.R.Rech_Arc(16), 10);
    
   conf.R.Ajouter_Source("S1", 1, 2);
    
    
    int[]C1 = {150,300};
    int[]C2 = {250,250};
    int[]C3 = {250,350};
    int[]C4 = {400,250};
    int[]C5 = {400,350};
    int[]C6 = {350,200};
    int[]C7 = {500,200};
    int[]C8 = {550,250};
    int[]C9 = {350,400};
    int[]C10 = {350,150};
    int[]C11 = {450,150};
    int[]C12 = {500,150};
    int[]C13 = {650,150};
    int[]C14 = {800,150};
    int[]C15 = {350,100};
    int[]C16 = {450,100};
    int[]C17 = {600,100};
    int[]C18 = {750,100};
    int[]C19 = {900,100};
    int[]C20 = {1050,100};
    int x1, x2, y1, y2, x3, y3, x4, y4;
    x1 = 10;y1 = 300 ;x2 = 150;y2= 300 ; x3= 200; y3= 150; x4=250; y4=100;
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
    conf.SIM.ListeLignes.add(new Line2D.Double(C6[0], C6[1],C12[0], C12[1]));
    conf.SIM.AjouterArc(C6[0], C6[1],C12[0], C12[1], conf.R.Rech_Arc(27));
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
    conf.SIM.ListeLignes.add(new Line2D.Double(C7[0], C7[1], C17[0], C17[1]));
    conf.SIM.AjouterArc(C7[0], C7[1], C17[0], C17[1], conf.R.Rech_Arc(29));
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
    conf.SIM.ListeLignes.add(new Line2D.Double(x3, y3, C10[0],C10[1]));
    conf.SIM.AjouterArc(x3, y3, C10[0],C10[1], conf.R.Rech_Arc(17)); 
    conf.SIM.ListeLignes.add(new Line2D.Double(C10[0], C10[1],C11[0],C11[1]));
    conf.SIM.AjouterArc(C10[0], C10[1],C11[0],C11[1],  conf.R.Rech_Arc(18)); 
    conf.SIM.ListeLignes.add(new Line2D.Double(C11[0], C11[1],C12[0],C12[1]));
    conf.SIM.AjouterArc(C11[0], C11[1],C12[0],C12[1], conf.R.Rech_Arc(19)); 
    conf.SIM.ListeLignes.add(new Line2D.Double(C12[0],C12[1], C13[0],C13[1]));
    conf.SIM.AjouterArc(C12[0],C12[1], C13[0],C13[1 ], conf.R.Rech_Arc(20)); 
    conf.SIM.ListeLignes.add(new Line2D.Double(C12[0],C12[1], C16[0],C16[1]));
    conf.SIM.AjouterArc(C12[0],C12[1], C16[0],C16[1], conf.R.Rech_Arc(28)); 
     conf.SIM.ListeLignes.add(new Line2D.Double(C19[0],C19[1], C8[0],C8[1]));
    conf.SIM.AjouterArc(C19[0],C19[1], C8[0],C8[1], conf.R.Rech_Arc(30)); 
      conf.SIM.ListeLignes.add(new Line2D.Double(x4,y4,C15[0],C15[1]));
    conf.SIM.AjouterArc(x4, y4, C15[0],C15[1],  conf.R.Rech_Arc(21)); 
      conf.SIM.ListeLignes.add(new Line2D.Double(C15[0], C15[1],C16[0],C16[1]));
    conf.SIM.AjouterArc(C15[0], C15[1],C16[0],C16[1],  conf.R.Rech_Arc(22));
      conf.SIM.ListeLignes.add(new Line2D.Double(C16[0], C16[1], C17[0],C17[1]));
    conf.SIM.AjouterArc(C16[0], C16[1], C17[0],C17[1], conf.R.Rech_Arc(23)); 
      conf.SIM.ListeLignes.add(new Line2D.Double(C17[0], C17[1], C18[0],C18[1]));
    conf.SIM.AjouterArc(C17[0], C17[1],C18[0],C18[1], conf.R.Rech_Arc(24)); 
      conf.SIM.ListeLignes.add(new Line2D.Double(C1[0], C1[1],x4,y4));
    conf.SIM.AjouterArc(C1[0], C1[1],x4,y4,  conf.R.Rech_Arc(25)); 
    conf.SIM.ListeLignes.add(new Line2D.Double(C1[0], C1[1],x3,y3));
    conf.SIM.AjouterArc(C1[0], C1[1],x3,y3,  conf.R.Rech_Arc(26)); 
    return conf;
    } 
    *
    // afficher un message d'erreur:
    static void Erreur_MSG(String S){
        JOptionPane.showMessageDialog(null, "Class Main: "+S,"Erreur ", JOptionPane.WARNING_MESSAGE);
    }
}
*/
    
   private static Config creerExemple( AgentContainer conteneur ){
    Config conf;
    conf = new Config();
        String nodesPath = "nodes.csv";
        try {
            Scanner scanner = new Scanner(new File(nodesPath));
            scanner.nextLine(); // On ne veut pas parser la première ligne qui indique juste le nom des metriques
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(";");
                int id = Integer.parseInt(parts[0]);
                int x = Integer.parseInt(parts[1]);
                int y = Integer.parseInt(parts[2]);
                conf.R.Ajouter_Carrefour(id, x, y);
            }
            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    

        String arcsPath = "arcs.csv";
        try {
            Scanner scanner = new Scanner(new File(arcsPath));
            scanner.nextLine(); // On ne veut pas parser la première ligne qui indique juste le nom des metriques
            int i=1;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(";");
                Integer a = Integer.parseInt(parts[0]);
                Integer b = Integer.parseInt(parts[1]);
                //Double s = Double.parseDouble(parts[7]);
                Integer c = Integer.parseInt(parts[8]);
                conf.R.Ajouter_Arc(i, 300, 40, 1 , 10, conf.R.Rech_Carrefour(a), conf.R.Rech_Carrefour(b));
               
                i++;
            }
            
            scanner.close();
        } catch (Exception exep) {
            exep.printStackTrace();
        }
         conf.SIM.vitesse_simul = 200;
        for(int i=0; i<conf.R.ListeArcs.size(); i++)
            for(int j=0; j<conf.R.ListeArcs.size(); j++)
                if ((conf.R.ListeArcs.elementAt(i).Des==conf.R.ListeArcs.elementAt(j).Origine)&&(conf.R.ListeArcs.elementAt(i).Origine != conf.R.ListeArcs.elementAt(j).Des))
            {
             
                conf.R.ListeArcs.elementAt(i).Ajouter_Suivant(conf.R.ListeArcs.elementAt(j), 10);
            }
        // conf.R.Ajouter_Source("S1", 5, 1);
       //conf.R.Ajouter_Source("S1", 10,701);
         conf.R.Ajouter_Source("S1", 5, 403);
         conf.R.Ajouter_Source("S1", 5, 255);
         conf.R.Ajouter_Source("S1", 5, 793);
         conf.R.Ajouter_Source("S1", 5, 633);
         conf.R.Ajouter_Source("S1", 5, 571);
           //conf.R.Ajouter_Source("S1", 5, 471);
           //conf.R.Ajouter_Source("S1", 5, 465);
        conf.R.Ajouter_Source("S1", 10,596);
        for (int i=0; i< conf.R.ListeArcs.size(); i++)
        {
            conf.SIM.ListeLignes.add(new Line2D.Double(conf.R.ListeArcs.elementAt(i).Origine.coor_x/10,conf.R.ListeArcs.elementAt(i).Origine.coor_y/10,conf.R.ListeArcs.elementAt(i).Des.coor_x/10, conf.R.ListeArcs.elementAt(i).Des.coor_y/10));
    conf.SIM.AjouterArc(conf.R.ListeArcs.elementAt(i).Origine.coor_x/10,conf.R.ListeArcs.elementAt(i).Origine.coor_y/10,conf.R.ListeArcs.elementAt(i).Des.coor_x/10, conf.R.ListeArcs.elementAt(i).Des.coor_y/10, conf.R.ListeArcs.elementAt(i));
        }
    
   return conf;
    }
    // afficher un message d'erreur:
    static void Erreur_MSG(String S){
        JOptionPane.showMessageDialog(null, "Class Main: "+S,"Erreur ", JOptionPane.WARNING_MESSAGE);
    }
}

    
   
  