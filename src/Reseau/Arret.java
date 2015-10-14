/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


package Reseau;

import java.io.Serializable;

/**
 *
 * @author Dihya
 */
public class Arret implements Serializable{
    public String cod_arret; // code de l'arret
    public Arc arc;       // l'arc sur lequel se trouve l'arret.
    public int position;  // position de l'arret sur l'arc.
    public int duree;     // donnée dynamique, la durée que met un bus pour arriver à cet arret.
    public long date_arrive_dernier;
    public Arret(String c, Arc a, int p){
        this.cod_arret=c;
        this.arc=a;
        this.position=p/a.long_cel;
        this.duree=-1;
        this.date_arrive_dernier=-1;
    }

}
