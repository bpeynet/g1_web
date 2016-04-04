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
            PreparedStatement st =
                conn.prepareStatement("INSERT INTO Utilisateurs (email,nom,prenom,hash_de_motdepasse,genre,datedenaissance,latitude,longitude,evaluation) VALUES (?, ?, ?, ?, ?, ?, ?, ? ,?)");
            st.setString(1, email);
            st.setString(2, nom);
            st.setString(3,prenom);
            st.setString(4,mdp);
            st.setInt(5,2);
            String date2 = "TO_date('11/11/1111','dd/mm/yyyy')";
            st.setString(6, date2);
            st.setDouble(7, 0);
            st.setDouble(8,0);
            st.setFloat(9,0);
            st.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Erreur BD " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
    }
    
}
