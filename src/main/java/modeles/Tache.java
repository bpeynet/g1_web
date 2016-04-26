/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modeles;

import java.util.ArrayList;

/**
 *
 * @author ralambom
 */
public class Tache {
    private Integer idTache;
    private String emailCommanditaire;
    private String titreTache;
    private ArrayList<TacheAtom> taches;
    
    public Tache(Integer idTache, String email, String titreTache, ArrayList<TacheAtom> taches) {
        this.idTache = idTache;
        this.emailCommanditaire = email;
        this.titreTache = titreTache;
        this.taches = new ArrayList<TacheAtom>();
        if(taches != null) this.taches.addAll(taches);
    }
    
    public Integer getIdTache() {
        return idTache;
    }
    
    public String getEmailCommanditaire() {
        return emailCommanditaire;
    }
    
    public String getTitreTache() {
        return titreTache;
    }
    
    public ArrayList<TacheAtom> getTaches() {
        //retourner erreur si taches est null
        return taches;
    }

    public boolean isOver() {
        for (TacheAtom ta : taches) {
            if (ta.getIndicateurFin()==0) return false;
        }
        return true;
    }
}
