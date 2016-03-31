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
    String email;
    String mdp;
    String nom;
    String prénom;
    Integer genre;
    Date date;
    Coordonnees coordonnees;
    ArrayList<Competences> competences;
    
    public Utilisateurs(String email, String mdp, String nom, String prénom, Integer genre, Date date, Coordonnees coordonnees) {
        this.email = email;
        this.mdp = mdp;
        this.nom = nom;
        this.prénom = prénom;
        this.genre = genre;
        this.date = date;
        this.coordonnees = coordonnees;
        this.competences = new ArrayList<Competences>();
    }
    
    // on efface et recopie tout à chaque modification
    // solution plus économique ?
    public void competencesAJour (ArrayList<Competences> competences) {
        this.competences.clear();
        this.competences.addAll(competences);
    }
    
}
