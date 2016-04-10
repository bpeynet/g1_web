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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import javax.sql.DataSource;
import modeles.Tache;
import modeles.TacheAtom;
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
    
    public void ajouterCompetences(String email, String competence) throws DAOException {
        ResultSet rs = null;
        String requeteSQL = "";
        Connection conn = null;
        try {
            conn = getConnection();
            Statement st = conn.createStatement();
            requeteSQL = "INSERT INTO CompetencesUtilisateurs VALUES (\'"+ email + "\',\'" + competence + "\')";
            st.executeUpdate(requeteSQL);
        } catch (SQLException e) {
            throw new DAOException("Erreur BD " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
    }
    
    
    public ArrayList<Competences> getCompetences(String email) throws DAOException {
        ArrayList<Competences> competencesUtilisateur = new ArrayList<Competences>();
        ResultSet rs = null;
        String requeteSQL = "";
        Connection conn = null;
        try {
            conn = getConnection();
            Statement st = conn.createStatement();
            requeteSQL = "select competence from CompetencesUtilisateurs where idUtilisateur = \'" + email + "\'";
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
        }
        
        return competencesUtilisateur;
    }
    
    public ArrayList<Competences> getUncheckedCompetences(String email) throws DAOException {
        ArrayList<Competences> uncheckedCompetences = new ArrayList<Competences>();
        ResultSet rs = null;
        String requeteSQL = "";
        Connection conn = null;
        try {
            conn = getConnection();
            Statement st = conn.createStatement();
            requeteSQL = "(select competence from Competences) MINUS (select competence from CompetencesUtilisateurs where idUtilisateur = \'" + email + "\')";
            rs = st.executeQuery(requeteSQL);

            while (rs.next()) {
                Competences competence = new Competences(rs.getString("competence"));
                System.err.println(competence);
                uncheckedCompetences.add(competence);
            }
        } catch (SQLException e) {
            throw new DAOException("Erreur BD " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
        
        return uncheckedCompetences;
    }
    
    
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
            String requeteSQL = "INSERT INTO Utilisateurs VALUES (\'"+ email + "\',\'" + nom + "\',\'" + prenom + "\',\'" + mdp + "\'," + genre + ",TO_date(\'" + date + "\','yyyy-mm-dd')," + results[0].geometry.location.lat + "," + results[0].geometry.location.lng + ",\'" + adresse + "\',-1)";
            st.executeUpdate(requeteSQL);
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
     * @throws dao.DAOException
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
            st.executeUpdate(requeteSQL);
        } catch (SQLException e) {
            throw new DAOException("Erreur BD " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
    }
    
    /**
     * Récupère les tâches d'un utilisateur (en tant que commanditaire) dans la base de données
     * @param id l'identifiant de la tâche recherchée
     * @return la tâche trouvée ou null
     * @throws dao.DAOException
     */
    public ArrayList<Tache> getTache(String email) throws DAOException {
        ArrayList<Tache>  listeTaches = new ArrayList<>() ;
        ResultSet rs, rsAtomiques;
        String requeteSQL;
        Connection conn = null;
        try {
            conn = getConnection();
            Statement st = conn.createStatement();
            requeteSQL = "SELECT * FROM Taches where idCommanditaire ='" + email + "'";
            rs = st.executeQuery(requeteSQL);
            while(rs.next()) {
                int idTache = rs.getInt("idTache");
                String titre = rs.getString("titreTache");
                requeteSQL = "SELECT * FROM TachesAtom where idTacheMere=" + idTache;
                Statement st1 = conn.createStatement();
                rsAtomiques = st1.executeQuery(requeteSQL);
                ArrayList<TacheAtom> listTachesAtomiques = new ArrayList<>();
                while (rsAtomiques.next()) {
                    listTachesAtomiques.add(new TacheAtom(rsAtomiques.getInt("idTacheAtom"), rsAtomiques.getInt("idTacheMere"),
                            rsAtomiques.getString("titreTacheAtom"),rsAtomiques.getString("descriptionTache"), rsAtomiques.getFloat("prixTache"),
                            new Coordonnees(rsAtomiques.getFloat("latitude"), rsAtomiques.getFloat("longitude")),
                            rsAtomiques.getDate("datePlusTot"), rsAtomiques.getDate("datePlusTard"),
                            rsAtomiques.getString("idCommanditaire"), null));
                }
                listeTaches.add(new Tache(idTache, email, titre, listTachesAtomiques));
            }
        } catch (SQLException e) {
            throw new DAOException(e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
        return listeTaches;
    }
    
    /**
     * Récupère la dernière tâche ajoutée par un utilisateur (en tant que commanditaire) dans la base de données
     * @param email l'identifiant du commanditaire
     * @return  id l'id de la dernière tâche ajoutée
     * @throws dao.DAOException
     */
    public int getIdLastTache(String email) throws DAOException {
        ResultSet rs;
        String requeteSQL;
        Connection conn = null;
        int idTache = -1;
        try {
            conn = getConnection();
            Statement st = conn.createStatement();
            requeteSQL = "SELECT * FROM Taches where idCommanditaire ='" + email + "'";
            rs = st.executeQuery(requeteSQL);
            if(rs.next()) {
                idTache = rs.getInt("idTache");
            }
        } catch (SQLException e) {
            throw new DAOException(e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
        return idTache;
    }

    public HashSet<Integer> getCandidaturesExecutant(Utilisateurs utilisateur) throws DAOException {
        HashSet<Integer> candidatures = new HashSet<>();
        Connection conn = null;
        try {
            conn = getConnection();
            Statement st = conn.createStatement();
            String requeteSQL = "SELECT * FROM Candidatures WHERE idCandidat='" + utilisateur.getEmail() + "'";
            ResultSet rs = st.executeQuery(requeteSQL);
            while (rs.next()) {
                candidatures.add(rs.getInt("idTacheAtom"));
            }
        } catch (SQLException ex) {
            throw new DAOException("Erreur SQL 'getCandidaturesExecutant'", ex);
        } finally {
            closeConnection(conn);
        }
        return candidatures;
    }

    public HashMap<Integer, Integer> getCandidaturesCommanditaire(Utilisateurs utilisateur) throws DAOException {
        HashMap<Integer, Integer> candidatures = new HashMap<>();
        Connection conn = null;
        try {
            conn = getConnection();
            Statement st = conn.createStatement();
            String requeteSQL = "SELECT idTacheAtom, COUNT(idCandidat) AS nombre FROM Candidatures WHERE idCommanditaire='" + utilisateur.getEmail()
                    + "' GROUP BY idTacheAtom";
            ResultSet rs = st.executeQuery(requeteSQL);
            while (rs.next()) {
                candidatures.put(rs.getInt("idTacheAtom"), rs.getInt("nombre"));
            }
        } catch (SQLException e) {
            throw new DAOException("Erreur SQL 'getCandidaturesCommanditaire'", e);
        } finally {
            closeConnection(conn);
        }
        return candidatures;
    }
}
