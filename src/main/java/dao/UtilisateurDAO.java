/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
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
            requeteSQL = "select * from Utilisateurs where email ='" + email + "'";
            rs = st.executeQuery(requeteSQL);
            if(rs.next()) {
                utilisateur = new Utilisateurs(rs.getString("email"), 
                        rs.getString("hash_de_motdepasse"), 
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getInt("genre"),
                        rs.getDate("datedenaissance"),
                        rs.getString("adresse"),
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
        ArrayList<Competences> competencesUtilisateur = new ArrayList<Competences>();
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
    
    
    // TODO : ajouter Competences
    public void ajouterUtilisateur(String email, String mdp, String nom, String prenom, int genre, String date, String adresse) throws DAOException {
        Coordonnees coordonnees;
        GeoApiContext context = new GeoApiContext().setApiKey("AIzaSyCIhR44YdJoRc8tqOQ8SFslDZ3PX-SYDtQ");
        GeocodingResult[] results = null;
        try {
            results = GeocodingApi.geocode(context, adresse).await();
        } catch (Exception ex) {
            throw new DAOException("Erreur Geocoding " + ex.getMessage(), ex);
        }
        coordonnees = new Coordonnees(results[0].geometry.location.lat, results[0].geometry.location.lng);
        Connection conn = null ;
        try {
            conn = getConnection();
            Statement st = conn.createStatement();
            String requeteSQL = "INSERT INTO Utilisateurs VALUES (\'"+ email + "\',\'" + nom + "\',\'" + prenom + "\',\'" + mdp + "\'," + genre + ",TO_date('"+ date +"','yyyy/mm/dd')," + 0 + "," + 0 + ",\'" + adresse + "\',null)";
            ResultSet rs = st.executeQuery(requeteSQL);
        } catch (SQLException e) {
            throw new DAOException("Erreur BD " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
    }
    
    /**
     * Modifie les données existantes dans la relation Utilisateurs dont la clé primaire est email.
     * @param email
     * @param mdp
     * @param nom
     * @param prenom
     * @param genre
     * @param date
     * @param adresse
     */
    public void mettreAJourUtilisateur(String email, String mdp, String nom, String prenom, int genre, String date, String adresse) throws DAOException {
        Coordonnees coordonnees;
        GeoApiContext context = new GeoApiContext().setApiKey("AIzaSyCIhR44YdJoRc8tqOQ8SFslDZ3PX-SYDtQ");
        GeocodingResult[] results = null;
        try {
            results = GeocodingApi.geocode(context, adresse).await();
        } catch (Exception ex) {
            throw new DAOException("Erreur Geocoding " + ex.getMessage(), ex);
        }
        coordonnees = new Coordonnees(results[0].geometry.location.lat, results[0].geometry.location.lng);
        Connection conn = null ;
        try {
            conn = getConnection();
            Statement st = conn.createStatement();
            String requeteSQL = "UPDATE Utilisateurs SET nom='" + nom + "', prenom='" + prenom
                    + "', hash_de_motdepasse='" + mdp + "', genre=" + genre
                    + ", datedenaissance=TO_date('" + date + "','yyyy/mm/dd'), latitude=" + coordonnees.getLatitude()
                    + ", longitude=" + coordonnees.getLongitude() + ", adresse='" + adresse
                    + "' WHERE email='"+ email + "'";
            ResultSet rs = st.executeQuery(requeteSQL);
        } catch (SQLException e) {
            throw new DAOException("Erreur BD " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
    }
    
}
