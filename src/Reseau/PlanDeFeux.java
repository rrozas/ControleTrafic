/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Reseau;

import Agents.AgentCarrefour;
import java.util.Vector;

/**
 *
 * @author DIHYA
 */
public class PlanDeFeux {

    public Vector<ElementPlan> plan;          // le plan de feux: un vecteur d'element.
    public Vector<Reservation> reservations;  // la liste des reservations reçues par le carrefour
    AgentCarrefour AC;                        // l'agent carrefour proprietaire du plan
    public int pt=0;
    int VMax=70*1000;
    int VMin=6*1000;
    int RI=2*1000;
    Reservation res_actuelle=null;
    public PlanDeFeux(AgentCarrefour ac){
        this.plan=new Vector<ElementPlan>();
        this.reservations=new Vector<Reservation>();
        this.AC=ac;
    }


    //---------------------------Methodes-------------------------------------//
    public void Ajouter_elemnt(String cod_phase,long dd, long df){
        ElementPlan e=new ElementPlan();
        e.code_phase=cod_phase;
//        e.duree=d;
        e.date_d=dd;
        e.date_f=df;
        this.plan.addElement(e);
        
    }
    // ajoute une reservation
    public void ajouter_res(String cod_b, int priorite, String cod_p, long td, long tf){
        boolean cont=true;
        int i=0;
        Reservation R=new Reservation();
        R.cod_phase=cod_p;
        R.priorite=priorite;
        R.dd=td;
        R.df=tf;
        R.cod_bus=cod_b;
        R.etat='n';
        //
        Reservation R2;
        while (cont && i<this.reservations.size()){
                R2= this.reservations.elementAt(i);
                if(R2.dd>=td){
                    cont=false;
                }else{
                    i++;
                }

        }
        this.reservations.add(i,R);
        this.EvaluerListeRes(this.reservations);        
    }
    //
    public int Totale_Res_Recues(String cod,long t, int d){
        int tot=0;
        Reservation R;
        long BMin=System.currentTimeMillis()+t*1000;
        long Bmax=BMin+d*1000;
        for (int i=0; i<this.reservations.size(); i++){
            R=reservations.elementAt(i);
            if(R.dd>=BMin && R.dd<=Bmax || R.df>BMin && R.df<=Bmax){
                tot=tot+R.priorite;
            }
        }
        return tot;
    }
    public void MAJ_Plan(){
        if((reservations.size()>0)&&(System.currentTimeMillis()>=reservations.firstElement().dd)){
            ElementPlan E;
            //System.out.println("-Appel--------------------------------Plan MAJ");
            Reservation R=this.reservations.elementAt(0);
            if((System.currentTimeMillis()>=R.df) || (R.etat=='r')){
                this.reservations.removeElementAt(0);
                this.res_actuelle=null;
                MAJ_Plan();
            }else{
                if(R!=this.res_actuelle){
                    //long df_r=this.reservations.firstElement().df;
                   int i=0;
                   boolean cont=true;
                   while (cont && i<this.plan.size()){
                     E=this.plan.elementAt(i);
                     if(E.date_d>=R.dd){
                       cont=false;
                     }else{
                       i++;
                     }
                   }
                                          
                   if(i>=1){
                     int j=Type_Modif(this.plan, R);
                     if(j>0){
                         this.res_actuelle=R;
                     }else{
                         this.res_actuelle=null;
                     }
                     switch (j){
                       case 1:{  //extension de la phase:
                          System.out.println("Case 1");
                          plan.elementAt(i-1).date_f=Math.max(plan.elementAt(i-1).date_f, R.df+RI);
                          if(i<plan.size()){ // avancer la phase suivante
                              plan.elementAt(i).date_d=plan.elementAt(i-1).date_f+RI;
                          }
                       }
                       break;
                       case 2:{ // retarder une phase:
                          System.out.println("Case 2");
                          plan.elementAt(i).date_d=Math.min(R.dd+RI,plan.elementAt(i).date_d);
                          plan.elementAt(i-1).date_d=plan.elementAt(i).date_d-RI;
                       }
                       break;
                       case 3:{ // appel de phase:
                          System.out.println("Case 3");
                          if(plan.elementAt(i-1).date_f-(R.df+RI)>=VMin){ // si on peut diviser la phase
                              ElementPlan e1=new ElementPlan();           // actuelle en deux
                              e1.code_phase=plan.elementAt(i-1).code_phase;
                              e1.date_d=R.df+RI;
                              e1.date_f=plan.elementAt(i-1).date_f;
                              plan.add(i,e1);
                              ElementPlan e2=new ElementPlan();
                              e2.date_d=R.dd+RI;
                              e2.date_f=R.df;
                              e2.code_phase=R.cod_phase;
                              plan.add(i,e2);
                              plan.elementAt(i-1).date_f=R.dd;
                          }else{
                              plan.elementAt(i-1).date_f=R.dd;
                              if(i<plan.size()){// on décale le suivant
                                  plan.elementAt(i).date_d=R.df+RI;
                              }
                              ElementPlan e=new ElementPlan();
                              e.date_d=R.dd+RI;
                              e.date_f=R.df;
                              e.code_phase=R.cod_phase;
                              plan.add(i,e);
                          }
                       }
                       break;
                       case -1:{ // le plan ne peut pas entre modifier
                          System.out.println("Case -1");
                          this.reservations.removeElementAt(0);
                          this.res_actuelle=null;
                          this.EvaluerListeRes(this.reservations);
                          MAJ_Plan();
                       }
                       break;
                    }                    
                 }
                                    
                }
             }
            this.AC.Afficher();
        }
    }

    public void EvaluerListeRes(Vector<Reservation> L){
        Arbre A=new Arbre(L);
        Vector<Reservation> V= A.Get_Best_Path();
        for(int i=0; i<this.reservations.size();i++){
            this.reservations.elementAt(i).etat='r';
        }
        
        //System.out.println(AC.getLocalName()+":--------- resrevation retenue ---------------------------");
        for(int i=0; i<V.size();i++){
            V.elementAt(i).etat='a';
            //System.out.println(AC.getLocalName()+": resrevation retenue "+V.elementAt(i).cod_phase);
        }
        //System.out.println(AC.getLocalName()+" --------- fin resrevation retenue ---------------------------");
        
        
    }
    // réinitialise le plan
    public void Init_Plan(){
        this.plan.clear();
    }

    private int Type_Modif(Vector<ElementPlan> plan, Reservation R) {
        int i=0;
        int type=-1;
        boolean cont=true;
        ElementPlan E;
        while (cont && i<plan.size()){
             E=plan.elementAt(i);
             if(E.date_d>=R.dd){
                    cont=false;
             }else{
                    i++;
             }
         }
        //System.out.println("----------------- Type_Modif -------------------");
        // verfier si l'extension d'une phase est possible
        boolean C11=plan.elementAt(i-1).code_phase.equals(R.cod_phase);
        //if (C11) System.out.println("C11=true");
        boolean C12=(R.dd-plan.elementAt(i-1).date_f<VMin+RI);
        //if (C12)System.out.println("C12=true");
        boolean C13=(R.df-plan.elementAt(i-1).date_d<=VMax);
        //if (C13)System.out.println("C13=true");
        boolean C14=((i>=plan.size())||(plan.elementAt(i).date_f-R.df>=VMin+RI));
        //if (C14) System.out.println("C14=true");
        if(C11 && C12 && C13 && C14){
            type=1;
        }else{
            // verifier s'il y a possibilité de retarder une phase
            boolean C21=(i<plan.size());
            if(C21){
                //System.out.println("C21=true");
                boolean C22=(plan.elementAt(i).code_phase.equals(R.cod_phase));
                if(C22){
                    //System.out.println("C22=true");
                    boolean C23=(plan.elementAt(i).date_d-RI-(R.df+RI)<VMin);
                    //if(C23)System.out.println("C23=true");
                    boolean C24=(plan.elementAt(i).date_f-R.dd<VMax);
                    //if(C24)System.out.println("C24=true");
                    boolean C25=(R.dd-RI-plan.elementAt(i-1).date_d>=VMin);
                    //if(C25)System.out.println("C25=true");
                    if(C23 && C24 && C25){
                        type=2;
                    }else{ // verifier s'il y a possibilité de faire un appel de phase
                        boolean C31 =((plan.elementAt(i-1).code_phase.equals(R.cod_phase)) &&
                                (i>=plan.size() || plan.elementAt(i).code_phase.equals(R.cod_phase)));
                        if(C31){
                            //System.out.println("C31=true");
                            boolean C32=(R.dd-plan.elementAt(i-1).date_d>=VMin);
                            //if(C32) System.out.println("C32=true");
                            boolean C33=((i>=plan.size())||(plan.elementAt(i).date_f-(R.df+RI)>=VMin));
                            //if(C33)System.out.println("C33=true");
                            if(C32 && C33){
                                type=3;
                            }
                        }
                    }

                }
            }

        }
        //System.out.println("----------------- Type_Modif -------------------");
        return type;
    }
    //------------------------------------------------------------------------//



    public class ElementPlan{
        public String code_phase;
        //public int duree;
        public long date_d;
        public long date_f;
        public char etat='r';
    }
    public class Reservation{
        String cod_bus;   // le bus ayant fait la reservation
        String cod_phase; // la phase demandee
        int priorite;
        long dd;          // date debut de la reservation
        long df;          // date fin de la reservation.
        char etat='n';
    }
    ///////////////
    class Noeud{
        Reservation R;
        Vector<Noeud> Fils;
        Noeud pere;
        int cout_total;
    }
    class Arbre{
        Noeud Racine=new Noeud();
        Vector<Noeud> Liste_Feuilles=new Vector<Noeud>();
        Vector<Reservation> ListeR;
        Arbre(Vector<Reservation> LR){
            ListeR=LR;
            Racine.pere=null;
            Racine.cout_total=0;
            Racine.R=null;
            Eval(Racine);
        }


        public Vector Get_Best_Path(){
            Vector<Reservation> V=new Vector();    // le meilleur (cout max) chemin dans l'arbre.
            Noeud F = this.Racine;    //meilleure feuille; de laquelle on remonte.
            int cout=-1;
            // recherche de la meilleure feuille:
            for (int i=0; i<Liste_Feuilles.size();i++){
                Noeud N= Liste_Feuilles.elementAt(i);
                if(N.cout_total>cout){   // il fo voir après si les 2 couts toto sont egaux
                    F=N;                 // on prend celui qui entraine moins de changement de phase
                    cout=N.cout_total;
                }
            }
            while(F.pere!=null){
                V.add(0,F.R);
                F=F.pere;
            }
            return V;
        }

        private void Eval(Noeud N) {
            Vector<Noeud> Fils=Const_Fils(N);
            if(Fils.isEmpty()){
                this.Liste_Feuilles.add(N);
            }else{
                for(int i=0;i<Fils.size();i++){
                    Noeud FilsI=(Noeud) Fils.get(i);
                    Eval(FilsI);
                }
            }
        }

        private Vector Const_Fils(Noeud N) {
            Vector V=new Vector();
            int deb=Rech_Res(N.R);
            for(int i=deb+1;i<this.ListeR.size();i++){
                if(Valide(i,deb, N)){
                    Noeud fils=new Noeud();
                    fils.R= this.ListeR.get(i);
                    fils.pere=N;
                    fils.cout_total=N.cout_total+fils.R.priorite;
                    V.add(fils);
                }
            }
            return V;


        }
        int Rech_Res(Reservation R){
            int i=0;
            boolean trouv=false;
            while (!trouv && i<this.ListeR.size()){
                if (ListeR.get(i)==R){
                    trouv=true;
                }else{
                    i++;
                }
            }
            if(trouv){
              return i;
            }else{
                return -1;
            }
        }
        // vérifie si la reservation à l'index i peut etre fils de celle à deb////////
        private boolean Valide(int i, int deb, Noeud N) {

            boolean val=true;
            long df_deb;
            String lib_deb;
            Noeud N1=N;
            if(deb<0){  // si N est le Noeud racine:
                df_deb=0;
                lib_deb="";
            }else{
                df_deb=N.R.df;
                lib_deb=N.R.cod_phase;
            }
            Reservation r= this.ListeR.get(i);
            long dd_i=r.dd; // date debut de la res qu'on veut vérifier
            String lib_i=r.cod_phase;

            if(dd_i>=df_deb || lib_i.equals(lib_deb)){
                if(dd_i>=df_deb){
                    boolean chevauche=false;
                    while (N1.pere!=null && ! chevauche){
                        if(N1.R.df>dd_i && (! N1.R.cod_phase.equals(lib_i))){
                            chevauche=true;
                        }else{
                            N1=N1.pere;
                        }
                    }
                    if (chevauche){
                        val=false;
                    }else{
                        val=true;
                    }
                }else{
                    val=true;
                }
            }else{
                val=false;
            }
            return val;
        }
    }
}
