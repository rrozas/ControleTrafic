/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Reseau;

import jade.core.AID;
import java.io.Serializable;
import java.util.Vector;

/**
 *
 * @author Dihya
 */
public class LigneBus implements Serializable{
        public  String code_ligne;
        public  ListeArrets L_Arrets;       // la liste des arrets de la ligne.
        public  Vector<AID> liste_bus;      // la liste des bus de la ligne (leur Identifiants).
        public  Vector<Arc> listedesarcs;   // la liste des arcs par lesquels passent les bus de la ligne
        int frequence=5*60;
        
}
