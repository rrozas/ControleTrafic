/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Reseau;

import java.util.Vector;

/**
 *
 * @author Dihya
 */
public class FrequencesPassageBus {

    public Vector<InfoFreqLigne> ListeFrequences;
    public FrequencesPassageBus(){
        this.ListeFrequences=new Vector<InfoFreqLigne>();
    }
    public InfoFreqLigne AjouterLigne(String cod_ligne){
        InfoFreqLigne e=new InfoFreqLigne(cod_ligne);
        this.ListeFrequences.addElement(e);
        return e;
    }
    public class InfoFreqLigne{
        public String cod_ligne;
        public Vector<ElementFreq> frequences;
        InfoFreqLigne(String cod){
            cod_ligne=cod;
            frequences=new Vector<ElementFreq>();
        }
        public void Ajouter_Freq(int d, int fr){
            this.frequences.addElement(new ElementFreq(d,fr));
        }
    }
    public class ElementFreq{
        ElementFreq(int d, int fr){
            this.date=d;
            this.f=fr;
        }
        public long date;
        public int f;
    }

}
