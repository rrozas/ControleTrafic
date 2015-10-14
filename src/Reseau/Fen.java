/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * Fen.java
 *
 * Created on 31 août 2009, 21:37:12
 */

package Reseau;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleInsets;

/**
 *
 * @author HACIANE
 */
public class Fen extends javax.swing.JFrame {

    XYDataset data1;
    Reseau R;
    List charts;
    char type='x';
    /** Creates new form Fen */
    public Fen(Reseau r, char t) {
        initComponents();
        this.type=t;
        this.R=r;
        if(type=='v'){
            this.setTitle("Retard des véhicules particulier");
        }else{
            this.setTitle("Retard des Bus");
        }
        this.setVisible(true);
        charts = new ArrayList();
        this.Afficher_graphe();     
        
    }    
    private void Afficher_graphe(){
        data1 = createSampleData();
        jtabbedpane.removeAll();
        jtabbedpane.add("Splines:", createChartPanel1());
        jtabbedpane.add("Lines:", createChartPanel2());
    }
    public void addChart(JFreeChart jfreechart) {
        charts.add(jfreechart);
    }

    public JFreeChart[] getCharts() {
        int i = charts.size();
        JFreeChart ajfreechart[] = new JFreeChart[i];
        for (int j = 0; j < i; j++) {
            ajfreechart[j] = (JFreeChart)charts.get(j);
        }

        return ajfreechart;
    }

    
    private ChartPanel createChartPanel1() {
            NumberAxis numberaxis = new NumberAxis("Temps (en seconde) ");
            numberaxis.setAutoRangeIncludesZero(false);
            NumberAxis numberaxis1 = new NumberAxis("Retard(%)");
            numberaxis1.setAutoRangeIncludesZero(false);
            XYSplineRenderer xysplinerenderer = new XYSplineRenderer();
            XYPlot xyplot = new XYPlot(data1, numberaxis, numberaxis1, xysplinerenderer);
            xyplot.setBackgroundPaint(Color.lightGray);
            xyplot.setDomainGridlinePaint(Color.white);
            xyplot.setRangeGridlinePaint(Color.white);
            xyplot.setAxisOffset(new RectangleInsets(4D, 4D, 4D, 4D));
            JFreeChart jfreechart = new JFreeChart("Retard en fonction du temps", JFreeChart.DEFAULT_TITLE_FONT, xyplot, true);
            addChart(jfreechart);
            ChartUtilities.applyCurrentTheme(jfreechart);
            ChartPanel chartpanel = new ChartPanel(jfreechart);
            return chartpanel;
        }
    private ChartPanel createChartPanel2() {
            NumberAxis numberaxis = new NumberAxis("Temps (en seconde)");
            numberaxis.setAutoRangeIncludesZero(false);
            NumberAxis numberaxis1 = new NumberAxis("Retard(%)");
            numberaxis1.setAutoRangeIncludesZero(false);
            XYLineAndShapeRenderer xylineandshaperenderer = new XYLineAndShapeRenderer();
            XYPlot xyplot = new XYPlot(data1, numberaxis, numberaxis1, xylineandshaperenderer);
            xyplot.setBackgroundPaint(Color.lightGray);
            xyplot.setDomainGridlinePaint(Color.white);
            xyplot.setRangeGridlinePaint(Color.white);
            xyplot.setAxisOffset(new RectangleInsets(4D, 4D, 4D, 4D));
            JFreeChart jfreechart = new JFreeChart("Retard en fonction du temps", JFreeChart.DEFAULT_TITLE_FONT, xyplot, true);
            addChart(jfreechart);
            ChartUtilities.applyCurrentTheme(jfreechart);
            ChartPanel chartpanel = new ChartPanel(jfreechart);
            return chartpanel;
        }
    private XYDataset createSampleData() {
        float Moy;
        int pas=0;
        //////////
        XYSeriesCollection xyseriescollection = new XYSeriesCollection();
        /////////
        if(type=='v' || type=='e'){
            XYSeries xyseries_Veh = new XYSeries("Retard des véhicules particuliers");
            for(int i=0; i<R.Retard_Veh.size();i++){
                pas+=R.unite_X;
                Moy=(R.Retard_Veh.elementAt(i).somme_retard/R.Retard_Veh.elementAt(i).nbre);
                xyseries_Veh.add(pas, Moy);
            }
            xyseriescollection.addSeries(xyseries_Veh);
            
        }
        if(type=='b' || type=='e'){
            XYSeries xyseries_bus = new XYSeries("Retard des bus");
            pas=0;
            if(R.Retard_Bus.size()>0){
              long dd=R.Retard_Bus.elementAt(0).dd;
              for(int i=0; i<R.Retard_Bus.size();i++){
                 pas=(int) ((R.Retard_Bus.elementAt(i).dd - dd) / 1000) ;
                 Moy=(R.Retard_Bus.elementAt(i).somme_retard/R.Retard_Bus.elementAt(i).nbre);
                 xyseries_bus.add(pas, Moy);
             }
            }
            xyseriescollection.addSeries(xyseries_bus);
           
        }
        return xyseriescollection;
            
        }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jtabbedpane = new javax.swing.JTabbedPane();
        JBRaffraichir = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        JBEffacer = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        JBRaffraichir.setText("Raffraichir");
        JBRaffraichir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JBRaffraichirActionPerformed(evt);
            }
        });

        jButton2.setText("Fermer");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        JBEffacer.setText("Effacer");
        JBEffacer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JBEffacerActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jtabbedpane, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 551, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(JBEffacer, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 190, Short.MAX_VALUE)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JBRaffraichir, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jtabbedpane, javax.swing.GroupLayout.DEFAULT_SIZE, 496, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(JBRaffraichir)
                    .addComponent(jButton2)
                    .addComponent(JBEffacer))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void JBRaffraichirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JBRaffraichirActionPerformed
       
        this.Afficher_graphe();
    }//GEN-LAST:event_JBRaffraichirActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        this.dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void JBEffacerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JBEffacerActionPerformed
        if(this.type=='v'){
            this.R.Retard_Veh.clear();
        }else{
            this.R.Retard_Bus.clear();
        }
    }//GEN-LAST:event_JBEffacerActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton JBEffacer;
    private javax.swing.JButton JBRaffraichir;
    private javax.swing.JButton jButton2;
    private javax.swing.JTabbedPane jtabbedpane;
    // End of variables declaration//GEN-END:variables
  

}
