/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Reseau;

import Reseau.Arc.Cellule;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Panel;
import java.awt.Graphics;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JPanel;

/**
 *
 * @author DIHYA
 */
public class Simul implements Serializable{
   Vector<InterfaceArc> ListeArcs;
   
   public  Vector<Line2D> ListeLignes;
   public  Vector<InterfaceFeu> ListeFeux;
   public  int width=50;
   public  int width_feu=10;
   public  int taille_veh=10;
   public  int vitesse_simul=700;
   public  Color couleur_veh;
   public  Color couleur_bus;   
   public  Color couleur_gps;
   public  Color couleur_font;
   Cellule cel;
   JPanel pan;

    public Simul(){
        this.ListeArcs=new Vector<InterfaceArc>();
        
        this.ListeLignes=new Vector<Line2D>();
        ListeFeux=new Vector<InterfaceFeu>();
        this.couleur_veh=Color.BLACK;
        this.couleur_bus=Color.BLUE;
        this.couleur_gps=Color.RED;
        this.taille_veh=10;

    }
    public void AjouterArc(int x1, int y1, int x2, int y2, Arc a){
        InterfaceArc in=new InterfaceArc();
        in.ligne=new Line2D.Double(x1, y1, x2, y2);
        in.arc=a;
        in.px=(float)(x2-x1)/a.nbre_cel;
        in.py=(float)(y2-y1)/a.nbre_cel;        
        this.ListeArcs.addElement(in);
    }
    
    public void AjouterFeu(int x, int y, Arc a){
        InterfaceFeu feu=new InterfaceFeu();
        Ellipse2D c=new Ellipse2D.Double(x-this.width_feu/2, y-this.width_feu/2, this.width_feu,this.width_feu);
        feu.arc=a;
        feu.cercle=c;
        this.ListeFeux.addElement(feu);

    }    
    // demarre la simulation
    public void demarrer_simul(Canvas c, Graphics2D gr){
       // if(this.th!=null){th.cont=false;}
        this.couleur_font=c.getBackground();
        new ThreadAffichage( this.pan );
    }

    void Supprimer(Point2D pt) {
        for(int i=this.ListeArcs.size()-1; i>=0;i--){
            if(this.ListeArcs.elementAt(i).ligne.ptSegDist(pt)<5){
                this.ListeArcs.removeElementAt(i);
            }
        }
        for(int i=this.ListeLignes.size()-1; i>=0;i--){
            if(this.ListeLignes.elementAt(i).ptSegDist(pt)<5){
                this.ListeLignes.removeElementAt(i);
            }
        }
        for(int i=this.ListeFeux.size()-1; i>=0;i--){
            Ellipse2D e=this.ListeFeux.elementAt(i).cercle;
            if(pt.getX()>=e.getX() && pt.getX()<=e.getX()+e.getWidth() &&
                    pt.getY()>=e.getY() && pt.getY()<=e.getY()+e.getWidth()){
                this.ListeLignes.removeElementAt(i);
            }
        }
    }
    class InterfaceArc implements Serializable{
        public  Line2D ligne;
        public  Arc arc;
        public  float px;
        public  float py;

    }
    
    public class InterfaceFeu implements Serializable{
         Arc arc;
         Ellipse2D cercle;
    }
    
    
    ////
    class ThreadAffichage extends Thread implements Serializable{
        JPanel pan;
        Graphics2D g;
         Vector<InterfaceArc> ListeArcs;
         Arc a;
          int i;
         boolean cont=true;
         
        ThreadAffichage(JPanel p){
            this.pan=p;
            //g.setPaint(redtowhite);
            this.start();
        }

        @Override
        public void run() {
           while(!isInterrupted())
           {
        	   pan.repaint();
                try {
                	Thread.sleep(vitesse_simul);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Simul.class.getName()).log(Level.SEVERE, null, ex);
                }

           }
        }
  }

}
