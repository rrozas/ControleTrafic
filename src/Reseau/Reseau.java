package Reseau;


import java.util.Vector;
import java.io.Serializable;
import Agents.AgentReseau;
import jade.wrapper.AgentContainer;

public class  Reseau implements Serializable{
	
	//////////////////////// Attributs  ////////////////////////////////
	public Vector<Arc> ListeArcs;
	public Vector<Carrefour> ListeCarrefour;
	public Vector<SourceDeVehicules> ListeDesSources;
    public Vector<SourceDeVehiculesGps> ListeDesSourcesGps;
    public Vector<InfoRetard> Retard_Bus;
    public Vector<InfoRetard> Retard_Veh;
    public int unite_X=30;
	// Constructor:
	public Reseau(){
        this.ListeDesSources=new Vector<SourceDeVehicules>();
        this.ListeDesSourcesGps= new Vector<SourceDeVehiculesGps>();
		this.ListeArcs=new Vector<Arc>();
		this.ListeCarrefour=new Vector<Carrefour>();
        //
        this.Retard_Bus=new Vector<InfoRetard>();
        this.Retard_Veh=new Vector<InfoRetard>();
	}
	
	/////////////////////// Methodes  ///////////////////////////////////
	
	public void Ajouter_Carrefour(int cod, int coord_x, int coord_y){
		this.ListeCarrefour.add(new Carrefour(cod, coord_x, coord_y) );
	}
	public Carrefour Rech_Carrefour(int cod){ // retourne "null" si le carrefour n'est pas trouver
		Carrefour c = null,c2=null;
		int i=this.ListeCarrefour.size()-1;
		boolean cont=true;
		while(i>=0 && cont){
			c2=this.ListeCarrefour.get(i);
			if(c2.code_carrefour==cod){
				cont=false;
                c=c2;
			}else{
				i--;
			}
		}
		return c;
		
	}
    public int Rech_Pos_Carrefour(int cod){ // retourne "null" si le carrefour n'est pas trouver
		Carrefour c = null;
		int i=this.ListeCarrefour.size()-1;
		boolean cont=true;
		while(i>=0 && cont){
			c=this.ListeCarrefour.get(i);
			if(c.code_carrefour==cod){
				cont=false;
			}else{
				i--;
			}
		}
		return i;

	}
	
	public void Ajouter_Arc(int cod, int l, int VM, int nbre_l, int D, Carrefour Origine, Carrefour Des){
		Arc a=new Arc(cod, l, VM, nbre_l, D, Origine, Des);
		this.ListeArcs.add(a);
		if(Origine!=null){
			Origine.ajouter_arc('S', a);
		}
		if(Des!=null){
			Des.ajouter_arc('E', a);
		}
	}
    public void Ajouter_Arc(Arc a){
        this.ListeArcs.add(a);
		if(a.Origine!=null){
			a.Origine.ajouter_arc('S', a);
		}
		if(a.Des!=null){
			a.Des.ajouter_arc('E', a);
		}
    }
	public Arc Rech_Arc(int cod){
		Arc a=null;
		int i=this.ListeArcs.size()-1;
		boolean cont=true;
		while (i>=0 && cont){
			a=this.ListeArcs.elementAt(i);
			if(a.code_arc==cod){
				cont=false;
			}else{
				i--;
			}
		}
		return a;
	}
	public void supp_arc(int cod){
		Arc a=this.Rech_Arc(cod);
		if(a!=null){
            if(a.Origine!=null){ a.Origine.Supp_arc(cod); }
		    if(a.Des!=null){ a.Des.Supp_arc(cod); }
		    this.ListeArcs.remove(a);
		}
	}
	public void supp_carr(int cod){
		Carrefour c=this.Rech_Carrefour(cod);
		Arc a=null;
		for(int i=0;i<c.E.size();i++){
			a=c.E.get(i);
			this.ListeArcs.remove(a);
		}
		for(int i=0;i<c.S.size();i++){
			a=c.S.get(i);
			this.ListeArcs.remove(a);
		}
		this.ListeCarrefour.remove(c);
	}

    // ajouter une source de vehicules au reseau
    public void Ajouter_Source(String cod_src, int periode, int cod_arc){
        Arc a=this.Rech_Arc(cod_arc);
        SourceDeVehicules src=new SourceDeVehicules(cod_src,periode, a, this);
        this.ListeDesSources.addElement(src);
    }
    
       public void Ajouter_Source_Gps(int cod_src, int periode, int cod_arc, AgentContainer conteneur ){
       
        SourceDeVehiculesGps src=new SourceDeVehiculesGps(cod_src,periode, cod_arc, this ,  conteneur);
        this.ListeDesSourcesGps.addElement(src);
    }
    public void REinit(){
        Arc a;
        for(int i=0; i<this.ListeArcs.size();i++){
            a=this.ListeArcs.elementAt(i);
            a.etat='r';
            a.nbre_veh=0;
            for(int j=0; j<a.ListeCellules.size();j++){
                a.ListeCellules.elementAt(j).oqp=false;
            }
        }
        // demarrer toutes les sources:
        for(int i=0; i<this.ListeDesSources.size();i++){
            this.ListeDesSources.elementAt(i).demarrer_source();
        }
    }
    public void Ajouter_Retard(int retard, char type){
        if(type=='b'){
            if(this.Retard_Bus.size()==0){
                InfoRetard R=new InfoRetard();
                R.nbre=1;
                R.somme_retard=retard;
                R.dd=System.currentTimeMillis();
                this.Retard_Bus.addElement(R);
            }else{
                InfoRetard R=this.Retard_Bus.lastElement();
                if(System.currentTimeMillis()-R.dd>this.unite_X*6000){
                    InfoRetard R2=new InfoRetard();
                    R2.dd=System.currentTimeMillis();
                    R2.nbre=1;
                    R2.somme_retard=retard;
                    this.Retard_Bus.addElement(R2);
                }else{
                    R.nbre++;
                    R.somme_retard+=retard;
                }
            }
        }else{
            if(this.Retard_Veh.size()==0){
                InfoRetard R=new InfoRetard();
                R.nbre=1;
                R.somme_retard=retard;
                R.dd=System.currentTimeMillis();
                this.Retard_Veh.addElement(R);
            }else{
                InfoRetard R=this.Retard_Veh.lastElement();
                if(System.currentTimeMillis()-R.dd>this.unite_X*1000){
                    InfoRetard R2=new InfoRetard();
                    R2.dd=System.currentTimeMillis();
                    R2.nbre=1;
                    R2.somme_retard=retard;
                    this.Retard_Veh.addElement(R2);
                }else{
                    R.nbre++;
                    R.somme_retard+=retard;
                }
            }
        }

    }
    public void ReinitRetards(){
        this.Retard_Bus.clear();
        this.Retard_Veh.clear();
    }

    public class InfoRetard implements Serializable{
        public int nbre=0;
        public int somme_retard=0;
        public long dd;
    }
}
