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
public class Config implements Serializable{
    public Config(){
        this.R=new Reseau();
        this.SIM=new Simul();
    }
    public  Reseau R;
    public  Simul SIM;

}
