/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ALGui.java
 *
 * Created on 2 août 2009, 13:49:42
 */

package Agents;

import Reseau.Arc;
import Reseau.Carrefour;
import Reseau.ComboBoxDialog;
import Reseau.ListeArcs2TabModel;
import Reseau.ListeArcsCBModel;
import Reseau.ListeArretsTabModel;
import Reseau.ListeBusTabModel;
import java.util.Vector;
import javax.swing.JOptionPane;

/**
 *
 * @author HACIANE
 */
public class ALGui extends javax.swing.JFrame {

    AgentLigne AL;
    public ALGui(AgentLigne al) {
        initComponents();
        this.AL=al;
        //this.setVisible(true);
        ListeArcs2TabModel model_itineraire=new ListeArcs2TabModel(AL.listedesarcs);
        this.JTABItineraire.setModel(model_itineraire);
        ListeArretsTabModel model_listeArret= new ListeArretsTabModel(AL.L_Arrets);
        this.JTabListeArrets.setModel(model_listeArret);
        ListeBusTabModel model_listeBus=new ListeBusTabModel(AL.liste_bus);
        this.JTabListeBus.setModel(model_listeBus);
        this.JLCodeLigne.setText(AL.getLocalName());
        this.setTitle(AL.getName());
        this.JTFFrequency.setText(Integer.toString(AL.frequence));
        Raffraichir();

    }

    public void Raffraichir() {
        ((ListeArcs2TabModel)this.JTABItineraire.getModel()).fireTableDataChanged();
        ((ListeArretsTabModel)this.JTabListeArrets.getModel()).fireTableDataChanged();
        ((ListeBusTabModel)this.JTabListeBus.getModel()).fireTableDataChanged();
        this.JTFFrequency.setText(String.valueOf(this.AL.frequence));
        this.jPanel3.setVisible(false);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        JLCodeLigne = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        JTabListeArrets = new javax.swing.JTable();
        JBAjouterArret = new javax.swing.JButton();
        JBSuppArret = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        JTabListeBus = new javax.swing.JTable();
        JBAjouterBus = new javax.swing.JButton();
        JBSuppBus = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        JTABItineraire = new javax.swing.JTable();
        JBAjouterArc = new javax.swing.JButton();
        JBSuppArc = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        JTFFrequency = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        JBapp = new javax.swing.JButton();
        JBHide = new javax.swing.JButton();
        JBStop = new javax.swing.JButton();
        JBDemarrer = new javax.swing.JButton();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12));
        jLabel1.setText("Edition de la ligne:");

        JLCodeLigne.setFont(new java.awt.Font("Tahoma", 1, 12));
        JLCodeLigne.setForeground(new java.awt.Color(0, 102, 255));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JLCodeLigne, javax.swing.GroupLayout.PREFERRED_SIZE, 326, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(329, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(JLCodeLigne, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Liste des arrêts"));

        JTabListeArrets.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(JTabListeArrets);

        JBAjouterArret.setText("Ajouter");
        JBAjouterArret.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JBAjouterArretActionPerformed(evt);
            }
        });

        JBSuppArret.setText("Supprimer");
        JBSuppArret.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JBSuppArretActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 302, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(JBSuppArret, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JBAjouterArret, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(JBAjouterArret)
                    .addComponent(JBSuppArret))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Liste des bus"));

        JTabListeBus.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(JTabListeBus);

        JBAjouterBus.setText("Ajouter");
        JBAjouterBus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JBAjouterBusActionPerformed(evt);
            }
        });

        JBSuppBus.setText("Supprimer");
        JBSuppBus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JBSuppBusActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 187, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(JBSuppBus, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(JBAjouterBus)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(JBAjouterBus)
                    .addComponent(JBSuppBus))
                .addGap(19, 19, 19))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Itinéraire"));

        JTABItineraire.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane3.setViewportView(JTABItineraire);

        JBAjouterArc.setText("Ajouter");
        JBAjouterArc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JBAjouterArcActionPerformed(evt);
            }
        });

        JBSuppArc.setText("Supprimer");
        JBSuppArc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JBSuppArcActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 196, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addComponent(JBSuppArc, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JBAjouterArc, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(JBAjouterArc)
                    .addComponent(JBSuppArc))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12));
        jLabel2.setText("Fréquence d'injection des bus sur la ligne (en seconds):");

        JTFFrequency.setFont(new java.awt.Font("Tahoma", 1, 12));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12));
        jLabel3.setForeground(new java.awt.Color(0, 51, 204));
        jLabel3.setText("secondes");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 378, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JTFFrequency, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(221, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(JTFFrequency, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        JBapp.setText("Appliquer");
        JBapp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JBappActionPerformed(evt);
            }
        });

        JBHide.setText("Fermer");
        JBHide.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JBHideActionPerformed(evt);
            }
        });

        JBStop.setText("Arreter la ligne");

        JBDemarrer.setText("Démarrer ligne");
        JBDemarrer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JBDemarrerActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(397, Short.MAX_VALUE)
                .addComponent(JBDemarrer, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JBStop)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JBHide)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JBapp)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(JBapp)
                    .addComponent(JBHide)
                    .addComponent(JBStop)
                    .addComponent(JBDemarrer))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_formWindowClosing

    private void JBAjouterArcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JBAjouterArcActionPerformed
        Vector<Arc> V=new Vector<Arc>();
        Arc arc=null;
        Carrefour c=null;
        try{
            arc=this.AL.listedesarcs.lastElement();
            c=arc.Des;
            for(int i=0; i<c.E.size(); i++){
                V.add(c.E.elementAt(i));
            }
            for(int i=0;i<arc.suivants.size();i++ ){
                Arc a=arc.suivants.elementAt(i).a;
                V.add(a);
            }
        }catch(Exception ex){
            arc=null;
            V=this.AL.R.ListeArcs;
        }

        ListeArcsCBModel LACBM=new ListeArcsCBModel(V);
        ComboBoxDialog dial=new ComboBoxDialog(this, true, LACBM,"Ajouter arc", "selectionner un arc");
        if(dial.valider){
            Arc a=this.AL.R.Rech_Arc(Integer.valueOf(dial.selected));
            this.AL.listedesarcs.addElement(a);
            Raffraichir();
        }
     

    }//GEN-LAST:event_JBAjouterArcActionPerformed

    private void JBSuppArcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JBSuppArcActionPerformed

        int index=this.JTABItineraire.getSelectedRow();
        if(index>=0){
            this.AL.listedesarcs.remove(index);
            Raffraichir();
        }
    }//GEN-LAST:event_JBSuppArcActionPerformed

    private void JBAjouterArretActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JBAjouterArretActionPerformed

        
        AjouterArretDial dial=new AjouterArretDial(this, true,this.AL.listedesarcs);
        if(dial.valide){
            String cod_arret=dial.cod_arret;
            int cod_arc=dial.cod_arc;
            int pos=dial.pos;
            Arc a=this.AL.R.Rech_Arc(cod_arc);
            this.AL.L_Arrets.Ajouter_arret(cod_arret, a, pos);
            Raffraichir();
        }

    }//GEN-LAST:event_JBAjouterArretActionPerformed

    private void JBAjouterBusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JBAjouterBusActionPerformed
        String s = (String)JOptionPane.showInputDialog(
				   this,
				   "Saisir le code du bus",
				   "Ajout de nouveau bus",
				   JOptionPane.QUESTION_MESSAGE,
				   null,
				   null, // c'est ouvert !!!
				   null); // valeur initiale
		if ((s != null) && (s.length() > 0)){
			this.AL.Create_Bus(s,'n');
            Raffraichir();
		}
    }//GEN-LAST:event_JBAjouterBusActionPerformed

    private void JBSuppArretActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JBSuppArretActionPerformed
        int index=this.JTabListeArrets.getSelectedRow();
        if(index>=0){
            this.AL.L_Arrets.remove(index);
            Raffraichir();
        }
    }//GEN-LAST:event_JBSuppArretActionPerformed

    private void JBSuppBusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JBSuppBusActionPerformed
        this.AL.delete_bus(this.JTabListeBus.getSelectedRow());
        Raffraichir();
    }//GEN-LAST:event_JBSuppBusActionPerformed

    private void JBHideActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JBHideActionPerformed
        this.setVisible(false);
    }//GEN-LAST:event_JBHideActionPerformed

    private void JBappActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JBappActionPerformed
       try{
         int f=Integer.valueOf(this.JTFFrequency.getText());
         this.AL.frequence=f;
         AL.ligne.frequence=f;
       }catch(Exception e){
           //System.out.println(e.toString());
       }
    }//GEN-LAST:event_JBappActionPerformed

    private void JBDemarrerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JBDemarrerActionPerformed
        try{
         int f=Integer.valueOf(this.JTFFrequency.getText());
         this.AL.frequence=f;
         this.AL.demarrer_ligne();
       }catch(Exception e){
           //System.out.println(e.toString());
       }
    }//GEN-LAST:event_JBDemarrerActionPerformed

    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton JBAjouterArc;
    private javax.swing.JButton JBAjouterArret;
    private javax.swing.JButton JBAjouterBus;
    private javax.swing.JButton JBDemarrer;
    private javax.swing.JButton JBHide;
    private javax.swing.JButton JBStop;
    private javax.swing.JButton JBSuppArc;
    private javax.swing.JButton JBSuppArret;
    private javax.swing.JButton JBSuppBus;
    private javax.swing.JButton JBapp;
    private javax.swing.JLabel JLCodeLigne;
    private javax.swing.JTable JTABItineraire;
    private javax.swing.JTextField JTFFrequency;
    private javax.swing.JTable JTabListeArrets;
    private javax.swing.JTable JTabListeBus;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    // End of variables declaration//GEN-END:variables

}