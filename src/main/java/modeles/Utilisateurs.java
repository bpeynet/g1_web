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
public class Utilisateurs {
    private String email;
    private String mdp;
    private String nom;
    private String prénom;
    private Integer genre;
    private Date date;
    private Coordonnees localisation;
    private ArrayList<Competences> competences;
    
    public Utilisateurs(String email, String mdp, String nom, String prénom, Integer genre, Date date, Coordonnees coordonnees, ArrayList<Competences> competences) {
        this.email = email;
        this.mdp = mdp;
        this.nom = nom;
        this.prénom = prénom;
        this.genre = genre;
        this.date = date;
        this.localisation = coordonnees;
        this.competences = new ArrayList<Competences>();
        this.competences.addAll(competences);
    }
    
    public String getEmail() {
        return this.email;
    }
    
    public String getMdp() {
        return this.mdp;
    }
    
    public String getNomPrénom() {
        return this.prénom + " " + this.nom;
     }
    
    public Integer getGenre() {
        return this.genre;
    }
    
    public Date getDate() {
        return this.date;
    }
    
    public Coordonnees getLocalisation() {
        return this.localisation;
    }
    
    public ArrayList<Competences> getCompetences() {
        return this.competences;
    }
    
}
