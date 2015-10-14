/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Reseau;

import java.util.logging.Level;
import java.util.logging.Logger;

////////////// la class Vehicule ///////////////////////////
    class Vehicule extends Thread{
        boolean cont;
        Arc a,aa;
        int etape;
        int pos;
        int pas=800;
        int pas_FR;
        int t_perdu=0;
        int retard_sur_reseau=0;
        long date_depart;
        private int pas_simul=1000;
        private int nb_arc;
        Reseau R;

        Vehicule(Arc arc, Reseau r){
            cont=true;
            this.aa=arc;
            this.R=r;
            this.nb_arc=0;
            etape=0;
            pos=0;
            this.pas=36*pas_simul/arc.vitesse_moyenne;
            pas_FR=(int) ((1 / aa.debit) * this.pas_simul);
            this.date_depart=System.currentTimeMillis();
            this.start();
        }

        @Override
        public void run() {
         while(cont){
          try {
            switch (etape){
                case 0:{
                    pos++;
                    if(pos>=aa.nbre_cel){
                        a=aa.suivant();
                        if((a!=null)  && (this.nb_arc<4)){
                            etape=1;    // changer arc;
                            this.nb_arc++;
                        }else{
                            if(this.nb_arc>=3){
                                int duree=(int) (System.currentTimeMillis() - this.date_depart);
                                int retard=(this.retard_sur_reseau*100)/duree;
                                this.R.Ajouter_Retard(retard, 'v');
                            }
                                           
                            cont=false;
                            aa.liberer(pos-1,'v');
                        }
                    }else{
                        etape=2;  // avancer d'une cellule (5 metres)
                    }
                }
                break;
                case 1:{  // changement d'arc
                    if(this.aa.etat=='v'){  // verifie que le feu est vert
                        if(a.occuper(0,'v')){   // tente de changer l'arc
                          aa.liberer(pos-1,'v');
                          aa=a;
                          pos=0;
                          etape=0;
                          this.pas=36*pas_simul/aa.vitesse_moyenne;
                          pas_FR=(int) ((1 /aa.debit) * this.pas_simul);
                          this.t_perdu=0;
                          Vehicule.sleep(pas);
                        }else{
                          t_perdu+=200;                          
                          Vehicule.sleep(200);
                        }
                    }else{  // si le feu est rouge
                        this.retard_sur_reseau+=pas_FR;
                        Vehicule.sleep(pas_FR);
                    }
                    
                }
                break;
                case 2:{
                    if(aa.occuper(pos,'v')){ // s'il reussit a avancer
                        aa.liberer(pos-1,'v');
                        etape=0;
                        this.t_perdu=0;
                        Vehicule.sleep(pas);
                    }else{              // sinon (peut etre le feu est rouge !!
                        if(this.t_perdu>this.pas_FR*2){ // le feu est rouge
                            this.retard_sur_reseau+=pas_FR;
                            Vehicule.sleep(pas_FR);        // attendre pas_FR instant avant de tenter d'avancer                           
                        }else{
                            Vehicule.sleep(200);
                            this.t_perdu+=200;
                        }
                        
                    }
                }

                break;

            }
          } catch (Exception ex) {
               Logger.getLogger(SourceDeVehicules.class.getName()).log(Level.SEVERE, null, ex);
          }
         }
        }
        void arreter(){
            this.cont=false;
        }


    }
