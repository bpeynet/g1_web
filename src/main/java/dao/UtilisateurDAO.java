/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;
import javax.sql.DataSource;
import modeles.Evaluation;
import modeles.Utilisateurs;
import modeles.outils.Competences;
import modeles.outils.Coordonnees;


//TODO

/**
 *
 * @author ralambom
 */
public class UtilisateurDAO extends AbstractDataBaseDAO {

    public UtilisateurDAO(DataSource ds) {
        super(ds);
    }
    
    /**
     * Récupère l'utilisateur à partir de son email dans la base de données
     */
    public Utilisateurs getUtilisateur(String email) throws DAOException {
        Utilisateurs  utilisateur = null ;
        ResultSet rs = null;
        String requeteSQL = "";
        Connection conn = null;
        String mail = "'" + email + "'";
        try {
            conn = getConnection();
            Statement st = conn.createStatement();
            requeteSQL = "select * from Utilisateurs";
            rs = st.executeQuery(requeteSQL);
            if(rs.next()) {
                utilisateur = new Utilisateurs(rs.getString("email"), 
                        rs.getString("hash_de_motdepasse"), 
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getInt("genre"),
                        rs.getDate("datedenaissance"),
                        new Coordonnees(rs.getFloat("latitude"),rs.getFloat("longitude")),
                        getCompetences(email),
                        rs.getFloat("evaluation"));
                System.err.println(utilisateur);
            }
        } catch (SQLException e) {
            throw new DAOException("Erreur BD " + e.getMessage(), e);
        } finally{
            closeConnection(conn);
        }
        return utilisateur;
    }
    
    
    public ArrayList<Competences> getCompetences(String email) throws DAOException {
        ArrayList<Competences> competencesUtilisateur = null;
        ResultSet rs = null;
        String requeteSQL = "";
        Connection conn = null;
         /*try {
            conn = getConnection();
            Statement st = conn.createStatement();
            requeteSQL = "select competence from CompetencesUtilisateurs where idUtilisateur = " + email;
            rs = st.executeQuery(requeteSQL);

            while (rs.next()) {
                Competences competence = new Competences(rs.getString("competence"));
                System.err.println(competence);
                competencesUtilisateur.add(competence);
            }
        } catch (SQLException e) {
            throw new DAOException("Erreur BD " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }*/
        
        return competencesUtilisateur;
    }
    
}
