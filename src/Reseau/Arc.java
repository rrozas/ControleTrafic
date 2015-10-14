package Reseau;

import java.io.Serializable;
import java.util.Vector;



public class Arc implements Serializable{
   //////////////////////////// Attributes  ///////////////////////////////////
	// donnees statiques:
	public int longueur;        // en metre
	public int vitesse_moyenne; // en Km/heure
	public int capacite;        // en nombre de vehicules
	public int nbre_lignes;     // nombre de ligne de l'arc
	public float debit;           // en nombre de vehicule par UT (par seconde)
	public int code_arc;
	public int nbre_bus=0;
	// donnees dynamiques:
	public int nbre_veh=0;      // nombre de vehicule qui se trouve dans l'arc
	public char etat='v';           // etat de l'arc R:Rouge, V:Vert, O:Orange
	
	// donnee pour la simulation
	int long_cel=10;             // longueur d'une cellule.
	public int nbre_cel;
    int pt=0;
	Vector<Cellule> ListeCellules;
	public Vector<Element> suivants;  // la liste des suivants.
	public Carrefour Origine=null;
	public Carrefour Des=null;
	// constructeur:
	public Arc(int cod, int l, int VM, int nbre_l, int D, Carrefour Origine, Carrefour Destination){
		this.code_arc=cod;
		this.longueur= l;
		this.vitesse_moyenne=VM;
		this.capacite=l/this.long_cel;
		this.nbre_lignes=nbre_l;
		this.debit= D;
        this.nbre_veh=0;
		this.Origine=Origine;
		this.Des=Destination;
        this.suivants=new Vector<Element>();
		this.ListeCellules=new Vector<Cellule>();        
		this.creer_cel();
		
	}
    public void creer_cel(){
        this.nbre_cel=this.longueur/this.long_cel;
        this.ListeCellules.clear();
		for(int i=0; i<nbre_cel;i++){
			Cellule cel=new Cellule();
			ListeCellules.add(cel);
            //System.out.println("cellule "+i+" créee");
		}
    }
    
    public String toString(){
    	return ("Arc "+  this.code_arc +" ("+ this.Origine.code_carrefour + "=>" + this.Des.code_carrefour + ")" );
    }
	public void Initialiser(){        
        for(int i=0; i<this.nbre_cel;i++){
            this.liberer(i,'v');
            this.pt=0;
        }
        this.nbre_veh=0;
    }
  ///////////////////////////// Methods //////////////////////////////////////
    public Arc suivant(){
        Arc arc=null;
        if(this.suivants.size()>0){
            Element e=this.suivants.elementAt(this.pt);
            e.n++;
            arc=e.a;
            if(e.n==e.nbre){
               e.n=0;
               pt=(pt+1) % this.suivants.size();  // incremente le pointeur;
            }
        }
        
        return arc;
    }
	// tente d'occuper une cellule:
	public boolean occuper(int index, char typ){
	
		boolean res=false;
        if(index>=0 && index<this.ListeCellules.size()){
            Cellule C=this.ListeCellules.elementAt(index);
            if(! C.oqp){
              res=true;
              C.oqp=true;
              C.type=typ;
            }       
		    if(res && (index==0)){
              this.nbre_veh++;
              if(typ=='b'){
                  this.nbre_bus++;
              }
		    }
        }
		return res;
	} 	
	// lib�re une cellule:
	public void liberer(int index, char type){
		this.ListeCellules.get(index).oqp=false;
		if(index==(this.nbre_cel-1)){
            if(this.nbre_veh>0){
                this.nbre_veh--;
                if(type=='b'){
                    this.nbre_bus--;
                }
            }
		}
	}
	// ajoute un suivant � cet arc:
	public void Ajouter_Suivant(Arc a, int p){
        if(this.rech_suivant(a.code_arc)<0){
		   Element e=new Element();
		   e.a=a;
		   e.pourcentage=p;
		   this.suivants.add(e);
           Calculer_Proportions();
        }
	}
    int rech_suivant(int cod){
        int i=this.suivants.size()-1;
        boolean trouv =false;
        while ((i>=0) && !trouv){
            if (this.suivants.get(i).a.code_arc==cod){
                trouv=true;
            }else{
                i--;
            }
        }
        return i;
    }
    // calcule les proportions de vehicules qui partent dans chaque direction.
    public void Calculer_Proportions() {
        Element e;
        int pdgc=PDGC();
        for(int i=0; i<this.suivants.size(); i++){
            e=this.suivants.elementAt(i);
            e.nbre=e.pourcentage/pdgc;
            //System.out.println("e.nbre= "+e.nbre);
        }
    }
    //// recherche le min d'une liste d'entiers:
    private int Minumum() {
        int m=this.suivants.elementAt(0).pourcentage;
        Element e;
        for(int i=0; i<this.suivants.size();i++){
            e=this.suivants.elementAt(i);
            if(e.pourcentage<m){
                m=e.pourcentage;
            }
        }
        return m;
    }
    //// calcule le PDGC d'une liste de nombres.
    private int PDGC() {
        int m=Minumum();
        while(! TousDivisible(m)){
            m--;
        }
        return m;
    }
    // vérifie si tous les elements d'une liste sont divisibles pas "m":
    private boolean TousDivisible(int m) {
        boolean div=true;
        int i=0;
        while(div && i<this.suivants.size()){
            if(this.suivants.elementAt(i).pourcentage % m!=0){
                div=false;
            }else{
                i++;
            }
        }
        return div;
    }
   /////////////////////////// une cellule de l'arc: un arc est constitué d'un ensemble de cellules///
   class Cellule implements Serializable{
	   boolean oqp=false;
       char type='v';
   }
   ////////////////////////  les informations relative à un arc suivant   //////////

   public class Element implements Serializable{
	   public Arc a=null;        // l'arc suivant
	   int pourcentage=0; // le poucentage de véhicules qui partent dans cet arc sortant de l'arc actuel
       int nbre=0;        // nbre=pourcentage/pdgc(....)
       int n=0;           // nbre de vehicules qui ont dejà pris cette direction
   }
}
