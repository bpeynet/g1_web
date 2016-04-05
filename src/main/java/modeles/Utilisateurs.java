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
    private String prenom;
    private int genre;
    private Date date;
    private Coordonnees localisation;
    private String adresse;
    private ArrayList<Competences> competences;
    private float evaluation;
    
    public Utilisateurs(String email, String mdp, String nom, String prenom, int genre, Date date, String adresse, Coordonnees coordonnees, ArrayList<Competences> competences, float evaluation) {
        this.email = email;
        this.mdp = mdp;
        this.nom = nom;
        this.prenom = prenom;
        this.genre = genre;
        this.date = date;
        this.adresse = adresse;
        this.localisation = coordonnees;
        this.competences = new ArrayList<Competences>();
        this.competences.addAll(competences);
        this.evaluation = evaluation;
    }
    
    public String getEmail() {
        return this.email;
    }
    
    public String getMdp() {
        return this.mdp;
    }
    
    public String getNom() {
        return this.nom;
    }
    
    public String getPrenom() {
        return this.prenom;
    }
    
    public String getNomPrenom() {
        return this.prenom + " " + this.nom;
     }
    
    public int getGenre() {
        return this.genre;
    }
    
    public Date getDate() {
        return this.date;
    }
    
    public String getAdresse() {
        return this.adresse;
    }
    
    public Coordonnees getLocalisation() {
        return this.localisation;
    }
    
    public ArrayList<Competences> getCompetences() {
        return this.competences;
    }
    
    public float getEvaluation() {
        return evaluation;
    }
    
}
