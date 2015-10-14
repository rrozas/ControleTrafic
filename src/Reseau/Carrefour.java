package Reseau;

import jade.core.AID;
import java.io.Serializable;
import java.util.Vector;




public class Carrefour implements Serializable {
  ////////////////////////////// Attributs ///////////////////////////
  public int code_carrefour;
  public int coor_x;
  public int coor_y;
  public Vector<Arc> E;             // liste des arcs entrants
  public Vector<Arc> S;             // Liste des arcs sortants
  public Vector<Phase> Liste_Phases;
  public AID agent_carrefour;
  ////////////////////////////////////////////////////////////////////
  // constructor
  public Carrefour(int cod, int coord_x, int coord_y){
	  this.code_carrefour=cod;
          this.coor_x= coord_x;
          this.coor_y = coord_y;
	  this.E=new Vector<Arc>();
	  this.S=new Vector<Arc>();
      this.Liste_Phases=new Vector<Phase>();
  }
  ////////////////////// Methodes /////////////////////////////
  // ajouter un arc � la liste
  public void ajouter_arc(char type, Arc a){
	  if(type=='E'){
		  this.E.add(a);
	  }
	  if(type=='S'){
		  this.S.add(a);
	  }
  }
  // supprimer un arc de la liste:
  public void Supp_arc(int cod){	  
	  Arc a;
	  for (int i=E.size()-1;i>=0; i--){
		  a=E.get(i);
		  if(a.code_arc==cod){
			  E.remove(i);
		  }
	  }
	  for (int i=S.size()-1;i>=0; i--){
		  a=S.get(i);
		  if(a.code_arc==cod){
			  S.remove(i);
		  }
	  }
  }
  // calcul le volume envoyé à l'arc a:
  public int VolumeEnvoyerA(int cod_arc){
        int v=0; Arc a = null, suiv;
        for (int i=0; i<this.E.size();i++){
            a=E.elementAt(i);
            for(int j=0; j<a.suivants.size(); j++){
                suiv=a.suivants.elementAt(j).a;
                if(suiv.code_arc==cod_arc){
                    v=v+(a.nbre_veh*a.suivants.elementAt(j).pourcentage)/100;
                }
            }
        }
        return v;
  }
}
