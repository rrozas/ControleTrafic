package Reseau;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Panel;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.Vector;

import javax.swing.JPanel;

import Reseau.Arc.Cellule;
import Reseau.Simul.InterfaceArc;

public class SimulPanel extends JPanel {
	Simul sim;
	Color couleur_veh=Color.BLACK;
	Color couleur_gps=Color.RED;
	Color couleur_bus=Color.BLUE;
	Color couleur_font=Color.WHITE;
	int taille_veh=10;


    public SimulPanel( Simul s ){
    	s.pan = this;
		this.sim = s;
    }
	
	public void paint(Graphics g){
		super.paint(g);
		Graphics2D g2 = (Graphics2D) g;
        this.setBackground(couleur_font);
        g2.setColor(sim.couleur_veh);
        // affichage des vehicules:
        for(int i=0; i<sim.ListeArcs.size(); i++){
        	InterfaceArc in=sim.ListeArcs.elementAt(i);
            double x1=in.ligne.getX1();
            double y1=in.ligne.getY1();
            double px=in.px;
            double py=in.py;
            x1=x1+px/2;
            y1=y1+py/2;
            Arc a=in.arc;
            for(int j=0; j<a.nbre_cel; j++){
            	Cellule cel=a.ListeCellules.elementAt(j);
                if(cel.oqp){
                    if(cel.type=='b'){
                        g2.setColor(couleur_bus);
                        g2.fill(new Ellipse2D.Double(x1+px*j-taille_veh/2,y1+py*j-taille_veh/2, taille_veh,taille_veh));
                    }else if(cel.type=='g'){
                        g2.setColor(couleur_gps);
                        g2.fill(new Ellipse2D.Double(x1+px*j-taille_veh/2,y1+py*j-taille_veh/2, taille_veh,taille_veh));
                    }else{
                        g2.setColor(couleur_veh);
                        g2.fill(new Ellipse2D.Double(x1+px*j-taille_veh/2,y1+py*j-taille_veh/2, taille_veh,taille_veh));
                    }
                }
            }
        }
        // affichage des lignes:
        g2.setColor(Color.BLACK);
        for(int i=0;i<sim.ListeLignes.size();i++){
            g2.draw(sim.ListeLignes.elementAt(i));
        }
        // affichage des feux:
        // 1) les feux qui sont au vert:
     /*   PAINT=new GradientPaint(0,0,Color.GREEN,100, 0,Color.GREEN);
        g.setPaint(PAINT);
        for(i=0;i<ListeFeux.size();i++){
            InterfaceFeu feu=ListeFeux.elementAt(i);
            if(feu.arc.etat=='v'){
                g.fill(feu.cercle);
            }
        }*/
        // 2) les feux verts:
      /*  PAINT=new GradientPaint(0,0,Color.RED,100, 0,Color.RED);
        g.setPaint(PAINT);
        for(i=0;i<ListeFeux.size();i++){
            InterfaceFeu feu=ListeFeux.elementAt(i);
            if(feu.arc.etat=='r'){
                g.fill(feu.cercle);
            }
        }    */    
		
	}
}

