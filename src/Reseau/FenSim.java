/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * FenSim.java
 *
 * Created on 18 août 2009, 01:43:38
 */

package Reseau;

import Agents.AgentReseau;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JColorChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author HACIANE
 */
public class FenSim extends javax.swing.JFrame {

    
    Graphics2D g;
    boolean ajout_arc=false;    
    boolean premierclique=false;
    boolean ajout_ligne=false;
    private boolean ajout_feu=false;
    boolean supprimer=false;
    int x1,y1,x2,y2;
    int difMinX=40;
    int difMinY=40;
    Simul sim;
    
    Reseau reseau;
    //Config config;
    Config config;
    AgentReseau AR;

    //
    

    public FenSim(AgentReseau ar,Config conf) {
        initComponents();
        this.AR=ar;
        this.config=conf;
        this.reseau=conf.R;
        sim=conf.SIM;
        this.sim.ListeFeux.clear();
        this.sim.width_feu=6;        
        for (int i=0; i<sim.ListeArcs.size();i++){
           if(sim.ListeArcs.elementAt(i).arc.Des!=null){
             sim.AjouterFeu((int)sim.ListeArcs.elementAt(i).ligne.getX2(),(int)sim.ListeArcs.elementAt(i).ligne.getY2(), sim.ListeArcs.elementAt(i).arc);
           }
        }
        //g=(Graphics2D) this.can.getGraphics();
        //this.can.paint(g);
        this.pan.add( new SimulPanel( sim ) );
        sim.demarrer_simul(can, g);       
        this.setVisible(true);
        this.sim.vitesse_simul=500;
        this.setExtendedState(this.getExtendedState() | FenSim.MAXIMIZED_BOTH);
    
        //this.can.doLayout();
      
    }

    

    

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToolBar1 = new javax.swing.JToolBar();
        JBAjouterArc = new javax.swing.JButton();
        JBAjouterFeu = new javax.swing.JButton();
        JBDessinerLigne = new javax.swing.JButton();
        JBAnnuler = new javax.swing.JButton();
        JBDel = new javax.swing.JButton();
        JBTerminer = new javax.swing.JButton();
        JBSave = new javax.swing.JButton();
        JBCouleurVeh = new javax.swing.JButton();
        JBCouleurBus = new javax.swing.JButton();
        JBTailleVeh = new javax.swing.JButton();
        JBFermer = new javax.swing.JButton();
        JLBO = new javax.swing.JLabel();
        JPansud = new javax.swing.JPanel();
        JLabSud = new javax.swing.JLabel();
        PanDessin = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        pan = new javax.swing.JPanel();
        can = new java.awt.Canvas();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("visualisation");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        JBAjouterArc.setText(" Arc");
        JBAjouterArc.setFocusable(false);
        JBAjouterArc.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        JBAjouterArc.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        JBAjouterArc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JBAjouterArcActionPerformed(evt);
            }
        });
        jToolBar1.add(JBAjouterArc);

        JBAjouterFeu.setText("Feu");
        JBAjouterFeu.setFocusable(false);
        JBAjouterFeu.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        JBAjouterFeu.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        JBAjouterFeu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JBAjouterFeuActionPerformed(evt);
            }
        });
        jToolBar1.add(JBAjouterFeu);

        JBDessinerLigne.setText("Ligne");
        JBDessinerLigne.setFocusable(false);
        JBDessinerLigne.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        JBDessinerLigne.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        JBDessinerLigne.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JBDessinerLigneActionPerformed(evt);
            }
        });
        jToolBar1.add(JBDessinerLigne);

        JBAnnuler.setText("annuler");
        JBAnnuler.setFocusable(false);
        JBAnnuler.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        JBAnnuler.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        JBAnnuler.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JBAnnulerActionPerformed(evt);
            }
        });
        jToolBar1.add(JBAnnuler);

        JBDel.setText("Supprimer");
        JBDel.setFocusable(false);
        JBDel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        JBDel.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        JBDel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JBDelActionPerformed(evt);
            }
        });
        jToolBar1.add(JBDel);

        JBTerminer.setText("Terminer");
        JBTerminer.setFocusable(false);
        JBTerminer.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        JBTerminer.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        JBTerminer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JBTerminerActionPerformed(evt);
            }
        });
        jToolBar1.add(JBTerminer);

        JBSave.setText("Enregistrer");
        JBSave.setFocusable(false);
        JBSave.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        JBSave.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        JBSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JBSaveActionPerformed(evt);
            }
        });
        jToolBar1.add(JBSave);

        JBCouleurVeh.setText("Couleur véhicule");
        JBCouleurVeh.setFocusable(false);
        JBCouleurVeh.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        JBCouleurVeh.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        JBCouleurVeh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JBCouleurVehActionPerformed(evt);
            }
        });
        jToolBar1.add(JBCouleurVeh);

        JBCouleurBus.setText("Couleur bus");
        JBCouleurBus.setFocusable(false);
        JBCouleurBus.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        JBCouleurBus.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        JBCouleurBus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JBCouleurBusActionPerformed(evt);
            }
        });
        jToolBar1.add(JBCouleurBus);

        JBTailleVeh.setText("Taille véhicule");
        JBTailleVeh.setFocusable(false);
        JBTailleVeh.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        JBTailleVeh.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        JBTailleVeh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JBTailleVehActionPerformed(evt);
            }
        });
        jToolBar1.add(JBTailleVeh);

        JBFermer.setText("Fermer");
        JBFermer.setFocusable(false);
        JBFermer.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        JBFermer.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        JBFermer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JBFermerActionPerformed(evt);
            }
        });
        jToolBar1.add(JBFermer);

        JLBO.setFont(new java.awt.Font("Tahoma", 1, 12));
        JLBO.setForeground(new java.awt.Color(0, 102, 255));
        jToolBar1.add(JLBO);

        JPansud.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        JLabSud.setFont(new java.awt.Font("Tahoma", 0, 12));
        JLabSud.setForeground(new java.awt.Color(0, 102, 255));

        javax.swing.GroupLayout JPansudLayout = new javax.swing.GroupLayout(JPansud);
        JPansud.setLayout(JPansudLayout);
        JPansudLayout.setHorizontalGroup(
            JPansudLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPansudLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(JLabSud, javax.swing.GroupLayout.DEFAULT_SIZE, 827, Short.MAX_VALUE)
                .addContainerGap())
        );
        JPansudLayout.setVerticalGroup(
            JPansudLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPansudLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(JLabSud, javax.swing.GroupLayout.DEFAULT_SIZE, 21, Short.MAX_VALUE)
                .addContainerGap())
        );

        PanDessin.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        PanDessin.setPreferredSize(new java.awt.Dimension(900, 304));
        PanDessin.setLayout(new java.awt.CardLayout());

        jScrollPane1.setAutoscrolls(true);

        pan.setBackground(new java.awt.Color(255, 255, 255));
        pan.setAutoscrolls(true);
        pan.setPreferredSize(new java.awt.Dimension(1200, 1200));
        pan.setLayout(new java.awt.BorderLayout());

        pan.setBackground(new java.awt.Color(255, 255, 255));
        pan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                canMouseClicked(evt);
            }
        });
        pan.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                canMouseMoved(evt);
            }
        });
        //pan.add(can, java.awt.BorderLayout.CENTER);

        jScrollPane1.setViewportView(pan);

        PanDessin.add(jScrollPane1, "card3");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(PanDessin, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 851, Short.MAX_VALUE)
                    .addComponent(jToolBar1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 851, Short.MAX_VALUE)
                    .addComponent(JPansud, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(PanDessin, javax.swing.GroupLayout.DEFAULT_SIZE, 526, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JPansud, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void JBAnnulerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JBAnnulerActionPerformed
        
        this.ajout_arc=false;
        this.premierclique=false;        
        this.supprimer=false;
        this.ajout_feu=false;
        
}//GEN-LAST:event_JBAnnulerActionPerformed

    private void JBAjouterArcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JBAjouterArcActionPerformed
        this.ajout_arc=true;
        this.premierclique=false;       
        this.ajout_ligne=false;
        this.ajout_feu=false;
        this.supprimer=false;
    }//GEN-LAST:event_JBAjouterArcActionPerformed

    private void JBDessinerLigneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JBDessinerLigneActionPerformed
        this.ajout_ligne=true;
        this.premierclique=false;
        this.ajout_arc=false;
        this.ajout_feu=false;
}//GEN-LAST:event_JBDessinerLigneActionPerformed

    private void JBAjouterFeuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JBAjouterFeuActionPerformed
        this.ajout_feu=true;
        this.ajout_arc=false;
        this.ajout_ligne=false;
        this.premierclique=false;
    }//GEN-LAST:event_JBAjouterFeuActionPerformed

    private void JBDelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JBDelActionPerformed
        this.supprimer=true;
    }//GEN-LAST:event_JBDelActionPerformed

    private void canMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_canMouseMoved
        this.JLabSud.setText("X="+evt.getX()+" , Y="+evt.getY());
        this.JLBO.setText("X="+evt.getX()+" , Y="+evt.getY());
        for (int i=0; i<this.sim.ListeArcs.size();i++){
            if(sim.ListeArcs.elementAt(i).ligne.ptSegDist(evt.getPoint())<10){
                Arc a=sim.ListeArcs.elementAt(i).arc;
                int o, d;
                if(a.Origine!=null){
                    o=a.Origine.code_carrefour;
                }else{
                    o=999999;
                }
                if(a.Des!=null){
                    d=a.Des.code_carrefour;
                }else{d=99999;}
                String s="Arc "+Integer.toString(a.code_arc)+" , Origine:"+o+" , Cible:"+d;
                this.JLabSud.setText(s);
                this.JLBO.setText(s);
            }
        }
    }//GEN-LAST:event_canMouseMoved

    private void canMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_canMouseClicked
        if(this.ajout_arc){
            if(this.premierclique){
               ListeArcsCBModel LACBM=new ListeArcsCBModel(this.reseau.ListeArcs);
               ComboBoxDialog dial=new ComboBoxDialog(this, true, LACBM,"Ajouter arc", "selectionner un arc");
               if(dial.valider){
                   Arc a=this.reseau.Rech_Arc(Integer.valueOf(dial.selected));
                   this.x2=evt.getX();
                   this.y2=evt.getY();
                   if(Math.abs(x1-x2)<this.difMinX){
                      x2=x1;
                   }
                   if(Math.abs(y1-y2)<this.difMinY){
                      y2=y1;
                   }
                   int f=this.sim.vitesse_simul;
                   this.sim.vitesse_simul=2000;
                   this.sim.AjouterArc(x1, y1, x2, y2,a);
                   this.sim.vitesse_simul=f;
                   this.can.paint(g);
                   for(int i=0; i<this.sim.ListeArcs.size();i++){
                      this.g.draw(sim.ListeArcs.elementAt(i).ligne);
                   }

                   //System.out.println("x1="+x1+"; y1="+y1+"; x2="+x2+"; y2="+y2);
               }
               //this.ajout_arc=false;
               this.premierclique=false;
            }else{
                this.premierclique=true;
                this.x1=evt.getX();
                this.y1=evt.getY();
            }
        }
        if(this.ajout_ligne){
            if(this.premierclique){
                   this.x2=evt.getX();
                   this.y2=evt.getY();
                   if(Math.abs(x1-x2)<this.difMinX){
                      x2=x1;
                   }
                   if(Math.abs(y1-y2)<this.difMinY){
                      y2=y1;
                   }
                   int f=this.sim.vitesse_simul;
                   this.sim.vitesse_simul=2000;
                   this.sim.ListeLignes.addElement(new java.awt.geom.Line2D.Double(x1, y1, x2, y2));
                   this.sim.vitesse_simul=f;
                   this.can.paint(g);
                   for(int i=0; i<this.sim.ListeArcs.size();i++){
                      this.g.draw(sim.ListeArcs.elementAt(i).ligne);
                   }

                   for(int i=0; i<this.sim.ListeLignes.size();i++){
                      this.g.draw(sim.ListeLignes.elementAt(i));
                   }
                   //this.ajout_ligne=false;
                   this.premierclique=false;
            }else{
                this.premierclique=true;
                this.x1=evt.getX();
                this.y1=evt.getY();
            }
        }

        if(this.ajout_feu){
            this.ajout_feu=false;
            this.x1=evt.getX();
            this.y1=evt.getY();
            ListeArcsCBModel LACBM=new ListeArcsCBModel(this.reseau.ListeArcs);
            ComboBoxDialog dial=new ComboBoxDialog(this, true, LACBM,"Ajouter arc", "selectionner un arc");
            if(dial.valider){
               Arc a=this.reseau.Rech_Arc(Integer.valueOf(dial.selected));
               int f=this.sim.vitesse_simul;
               this.sim.vitesse_simul=2000;
               this.sim.AjouterFeu(x1, y1, a);
               this.sim.vitesse_simul=f;
            }
        }
        if(this.supprimer){            
            Point2D pt=evt.getPoint();
            this.sim.Supprimer(pt);
        }
    }//GEN-LAST:event_canMouseClicked

    private void JBSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JBSaveActionPerformed
    	this.AR.save();
    }//GEN-LAST:event_JBSaveActionPerformed

    private void JBCouleurVehActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JBCouleurVehActionPerformed
        Color c=JColorChooser.showDialog(this,"chosisser la couleur des véhicules", null);
        if(c!=null){
            this.sim.couleur_veh=c;
        }
    }//GEN-LAST:event_JBCouleurVehActionPerformed

    private void JBCouleurBusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JBCouleurBusActionPerformed
        Color c=JColorChooser.showDialog(this,"chosisser la couleur des véhicules", null);
        if(c!=null){
            this.sim.couleur_bus=c;
        }
    }//GEN-LAST:event_JBCouleurBusActionPerformed

    private void JBTailleVehActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JBTailleVehActionPerformed
        String s = (String)JOptionPane.showInputDialog(
                this,
                "Saisir la taille des véhicules en pixels",
                "modification",
                JOptionPane.QUESTION_MESSAGE,
                null,
                null, // c'est ouvert !!!
                null); // valeur initiale
        if ((s != null) && (s.length() > 0)){
            try{
                int t=Integer.valueOf(s);
                if(t>4){
                    this.sim.taille_veh=t;
                }
            }catch(Exception ex){
                //System.out.println(ex.toString());
            }
        }else{
            //System.out.println("la taille des véhicules n'est pas modifiée !");
        }
    }//GEN-LAST:event_JBTailleVehActionPerformed

    private void JBFermerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JBFermerActionPerformed
        this.dispose();
    }//GEN-LAST:event_JBFermerActionPerformed

    private void JBTerminerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JBTerminerActionPerformed
        this.supprimer=false;
        this.ajout_ligne=false;
        this.ajout_arc=false;
    }//GEN-LAST:event_JBTerminerActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton JBAjouterArc;
    private javax.swing.JButton JBAjouterFeu;
    private javax.swing.JButton JBAnnuler;
    private javax.swing.JButton JBCouleurBus;
    private javax.swing.JButton JBCouleurVeh;
    private javax.swing.JButton JBDel;
    private javax.swing.JButton JBDessinerLigne;
    private javax.swing.JButton JBFermer;
    private javax.swing.JButton JBSave;
    private javax.swing.JButton JBTailleVeh;
    private javax.swing.JButton JBTerminer;
    private javax.swing.JLabel JLBO;
    private javax.swing.JLabel JLabSud;
    private javax.swing.JPanel JPansud;
    private javax.swing.JPanel PanDessin;
    private java.awt.Canvas can;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JPanel pan;
    // End of variables declaration//GEN-END:variables

}
