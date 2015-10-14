/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * DialAjoutSource.java
 *
 * Created on 16 août 2009, 20:46:00
 */

package Reseau;

import java.util.Vector;

/**
 *
 * @author HACIANE
 */
public class DialAjoutSource extends javax.swing.JDialog {

    boolean valide=false;
    String cod_src=null;
    int cod_arc;
    int periode;
    public DialAjoutSource(java.awt.Frame parent, boolean modal, Vector<Arc> v) {
        super(parent, modal);
        initComponents();
        ListeArcsCBModel model=new ListeArcsCBModel(v);
        this.JCBArc.setModel(model);
        this.setVisible(true);
        this.setModal(modal);
    }

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        JTFCodeS = new javax.swing.JTextField();
        JCBArc = new javax.swing.JComboBox();
        JTFPeriode = new javax.swing.JTextField();
        JBValder = new javax.swing.JButton();
        JBAnnuler = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("Code source:");

        jLabel2.setText("Arc de la source:");

        jLabel3.setText("Période (en secondes):");

        JCBArc.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        JTFPeriode.setToolTipText("Un véhicule est injecté sur l'arc à chaque période");

        JBValder.setText("Valider");
        JBValder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JBValderActionPerformed(evt);
            }
        });

        JBAnnuler.setText("Annuler");
        JBAnnuler.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JBAnnulerActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(JBAnnuler, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JBValder, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(JTFPeriode)
                            .addComponent(JCBArc, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(JTFCodeS, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(JTFCodeS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(JCBArc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(JTFPeriode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(JBValder)
                    .addComponent(JBAnnuler))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void JBAnnulerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JBAnnulerActionPerformed
        // TODO add your handling code here:
        this.valide=false;
        this.dispose();
    }//GEN-LAST:event_JBAnnulerActionPerformed

    private void JBValderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JBValderActionPerformed
        // TODO add your handling code here:
        try{
            this.cod_src=this.JTFCodeS.getText();
            this.periode=Integer.valueOf(this.JTFPeriode.getText());
            this.cod_arc=Integer.valueOf((String)this.JCBArc.getSelectedItem());
            this.valide=true;
            this.dispose();
        }catch(Exception e){
            //System.out.println(e.toString());
        }
    }//GEN-LAST:event_JBValderActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton JBAnnuler;
    private javax.swing.JButton JBValder;
    private javax.swing.JComboBox JCBArc;
    private javax.swing.JTextField JTFCodeS;
    private javax.swing.JTextField JTFPeriode;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    // End of variables declaration//GEN-END:variables

}
