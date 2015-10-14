
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Agents;

/**
 *
 * @author Dihya
 */
public class VehGps extends javax.swing.JFrame {

    private javax.swing.JButton JBStart;
    private javax.swing.JTextField JTFDureeRegul;
    private javax.swing.JTextField JTFEtat;
    private javax.swing.JTextField JTFPosActuelle;
    private javax.swing.JTextField JTFPriorite;
    private javax.swing.JTextField JTFProchainArret;
    private javax.swing.JTextField JTFarcActuel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    AgentVehiculeGps agent;

    public VehGps(AgentVehiculeGps avp) {
        initComponents();
        this.agent = avp;
        this.setTitle("Interface de l'Agent Bus: '" + agent.getLocalName() + "'");
        this.setVisible(true);
        new ThreadAffichage(this);
    }

    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        JTFarcActuel = new javax.swing.JTextField();
        JTFPosActuelle = new javax.swing.JTextField();
        JTFProchainArret = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        JTFEtat = new javax.swing.JTextField();
        JBStart = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        JTFPriorite = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        JTFDureeRegul = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("Arc Actuel:");

        jLabel2.setText("pos actuelle:");

        jLabel3.setText("Prochain arret:");

        JTFarcActuel.setText("jTextField1");

        JTFPosActuelle.setText("jTextField2");

        JTFProchainArret.setText("jTextField3");

        jLabel4.setText("état:");

        JTFEtat.setText("jTextField1");

        JBStart.setText("start");
        JBStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JBStartActionPerformed(evt);
            }
        });

        jLabel5.setText("Priorité:");

        JTFPriorite.setText("jTextField1");

        jLabel6.setText("Durée régulation:");

        JTFDureeRegul.setText("jTextField1");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(JBStart)
                .addContainerGap())
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE)
                .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE)
                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                .addComponent(JTFDureeRegul)
                .addComponent(JTFPriorite)
                .addComponent(JTFEtat)
                .addComponent(JTFProchainArret)
                .addComponent(JTFPosActuelle)
                .addComponent(JTFarcActuel, javax.swing.GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE))
                .addGap(80, 80, 80)))));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel1)
                .addComponent(JTFarcActuel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel2)
                .addComponent(JTFPosActuelle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel3)
                .addComponent(JTFProchainArret, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel4)
                .addComponent(JTFEtat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel5)
                .addComponent(JTFPriorite, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel6)
                .addComponent(JTFDureeRegul, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                .addComponent(JBStart)
                .addContainerGap()));

        pack();
    }

    private void JBStartActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        this.agent.Demarrer();
    }

    class ThreadAffichage extends Thread {

        VehGps gui;

        ThreadAffichage(VehGps g) {
            gui = g;
            this.start();
        }

        @Override
        public void run() {
            while (true) {

                try {
                    this.gui.JTFEtat.setText(this.gui.agent.etat);
                    this.gui.JTFPosActuelle.setText(Integer.toString(this.gui.agent.position));
                   // this.gui.JTFarcActuel.setText(Integer.toString(this.gui.agent.arc_actuel.code_arc));
                    this.gui.JTFarcActuel.setText(Integer.toString(this.gui.agent.arcActuel.code_arc));
                    ThreadAffichage.sleep(50);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
