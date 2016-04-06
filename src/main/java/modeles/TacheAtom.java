/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modeles;

import java.util.ArrayList;
import java.util.Date;
import modeles.outils.Competences;
import modeles.outils.Coordonnees;

/**
 *
 * @author ralambom
 * @date 31/03/2016
 */
public class TacheAtom {
    private Integer idTacheAtom;
    private Integer idTache;
    private String titreTacheAtom;
    private String descriptionTache;
    private Float prixTache;
    private Coordonnees localisation;
    private Date datePlusTot;
    private Date datePlusTard;
    private ArrayList<Competences> competences;
    
    public TacheAtom(Integer idTacheAtom, Integer idTache, String titreTacheAtom, String descriptionTache, Float prixTache, Coordonnees localisation, Date datePlusTot, Date datePlusTard, ArrayList<Competences> competences) {
        this.idTacheAtom = idTacheAtom;
        this.idTache = idTache;
        this.titreTacheAtom = titreTacheAtom;
        this.descriptionTache = descriptionTache;
        this.prixTache = prixTache;
        this.localisation = localisation;
        this.datePlusTot = datePlusTot;
        this.datePlusTard = datePlusTard;
        this.competences = new ArrayList<Competences>();
        if (competences != null) this.competences.addAll(competences);
    }
    
    public Integer getIdTacheAtom() {
        return idTacheAtom;
    }
    
    public Integer getIdTache() {
        return idTache;
    }
    
    public String getTitreTacheAtom() {
        return titreTacheAtom;
    }
    
    public String getDescription() {
        return descriptionTache;
    }
    
    public Float getPrix() {
        return prixTache;
    }
    
    public Coordonnees getLocalisation() {
        return localisation;
    }
    
    public Date getDateTot() {
        return datePlusTot;
    }
    
    public Date getDatetard() {
        return datePlusTard;
    }
    
    public ArrayList<Competences> getCompetences() {
        return competences;
    }
}
