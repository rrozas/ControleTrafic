package controletrafic;

import java.io.File;
import java.util.Scanner;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jfree.chart.labels.IntervalCategoryItemLabelGenerator;

import Reseau.Config;
import Reseau.Arc;
import Reseau.Reseau;
import Reseau.Simul;
import Reseau.Arc.Element;
import jade.wrapper.AgentContainer;

public class Dijkstra {

	
	public static Vector<Arc> dijkstra( Arc dep, Arc dest){
		
		//int n = R.ListeArcs.size();
		Vector<Arc> ListArc  = new Vector<Arc>();
		Vector<Integer> best_pred = new Vector<Integer>();
		Vector<Boolean> flag = new Vector<Boolean>();
		Vector<Integer> dist = new Vector<Integer>();		
		
		// add departure arc
		ListArc.add(dep);
		dist.add(0);
		flag.add(true);
		best_pred.add(1);
		int i_dep = 0;

		
		// destination 
		ListArc.add(dest);
		dist.add(Integer.MAX_VALUE);
		flag.add(false);
		best_pred.add(2);
		int i_dest = 1;
		
		Arc current = dep ;
		int next = 0;
		int idx;
		int i_current = 0;
		while( !flag.get(i_dest) ){
			for( Element e : current.suivants){
				if( ListArc.contains(e.a))
					idx = ListArc.indexOf(e.a);
				else{
					ListArc.add(e.a);
					dist.add(Integer.MAX_VALUE);
					flag.add(false);
					best_pred.add(2);
					idx = ListArc.size()-1;
				}
				if( !flag.get(idx) && ( dist.get(i_current) + current.longueur < dist.get( idx )  )){
					dist.set( idx, dist.get( i_current ) + current.longueur);
					best_pred.set(idx, i_current);
				}

			}
			int m = Integer.MAX_VALUE;
			for( int i=0; i< flag.size(); i++){
				if( !flag.get(i) && dist.get(i) < m)
					next = i;
				if((!flag.get(i) && dist.get(i) == m) && ( i < next ) )
					next = i;
				}
			i_current = next;
			current = ListArc.get(i_current);
			flag.set(i_current, true);
					
		}
		
		Vector<Arc> path, path_reversed;
		path_reversed= new Vector<Arc>();
		path = new Vector<Arc>();
		
		path_reversed.add(ListArc.get(i_dest));
		while(i_current != i_dep){
			i_current = best_pred.get(i_current);
			path_reversed.add(ListArc.get(i_current));
			
		}
		
		for(int i=path_reversed.size()-1; i >= 0; i-- ){
			path.add(path_reversed.get(i));
		}
		
		return(path);
	}
	
	public static void main(String [] args ){
		Config conf = creerExemple();
		Vector<Arc> path = dijkstra(conf.R.Rech_Arc(351), conf.R.Rech_Arc(298));
		System.out.println(path);
		
		
	}
	
	private static Config creerExemple( ){
	    Config conf;
	    conf = new Config();
	        String nodesPath = "nodes.csv";
	        try {
	            Scanner scanner = new Scanner(new File(nodesPath));
	            scanner.nextLine(); // On ne veut pas parser la première ligne qui indique juste le nom des metriques
	            while (scanner.hasNextLine()) {
	                String line = scanner.nextLine();
	                String[] parts = line.split(";");
	                int id = Integer.parseInt(parts[0]);
	                int x = Integer.parseInt(parts[1]);
	                int y = Integer.parseInt(parts[2]);
	                conf.R.Ajouter_Carrefour(id, x, y);
	            }
	            scanner.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    

	        String arcsPath = "arcs.csv";
	        try {
	            Scanner scanner = new Scanner(new File(arcsPath));
	            scanner.nextLine(); // On ne veut pas parser la première ligne qui indique juste le nom des metriques
	            int i=1;
	            while (scanner.hasNextLine()) {
	                String line = scanner.nextLine();
	                String[] parts = line.split(";");
	                Integer a = Integer.parseInt(parts[0]);
	                Integer b = Integer.parseInt(parts[1]);
	                //Double s = Double.parseDouble(parts[7]);
	                Integer c = Integer.parseInt(parts[8]);
	                conf.R.Ajouter_Arc(i, 300, 40, 1 , 10, conf.R.Rech_Carrefour(a), conf.R.Rech_Carrefour(b));
	               
	                i++;
	            }
	            
	            scanner.close();
	        } catch (Exception exep) {
	            exep.printStackTrace();
	        }
	         conf.SIM.vitesse_simul = 200;
	        for(int i=0; i<conf.R.ListeArcs.size(); i++)
	            for(int j=0; j<conf.R.ListeArcs.size(); j++)
	                if ((conf.R.ListeArcs.elementAt(i).Des==conf.R.ListeArcs.elementAt(j).Origine)&&(conf.R.ListeArcs.elementAt(i).Origine != conf.R.ListeArcs.elementAt(j).Des))
	            {
	             
	                conf.R.ListeArcs.elementAt(i).Ajouter_Suivant(conf.R.ListeArcs.elementAt(j), 10);
	            }
	        return(conf);
	}
}
