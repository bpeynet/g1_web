package dao;

import com.google.maps.DistanceMatrixApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.IOException;
import java.io.OutputStream;
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


public class UtilisateurDAO extends AbstractDataBaseDAO {

    public UtilisateurDAO(DataSource ds) {
        super(ds);
    }
    
    /**
     * Récupère l'utilisateur à partir de son email dans la base de données
     * @param email email de l'utilisateur recherché
     * @return Objet Utilisateur ayant email pour email. null sinon.
     * @throws dao.DAOException
     */
    public Utilisateurs getUtilisateur(String email) throws DAOException {
        Utilisateurs  utilisateur = null ;
        if (email == null) return utilisateur;
        Connection conn = null;
        try {
            conn = getConnection();
            Statement st = conn.createStatement();
            String requeteSQL = "select * from Utilisateurs where email ='" + email + "'";
            ResultSet rs = st.executeQuery(requeteSQL);
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
                        rs.getFloat("evaluation"),
                        rs.getInt("rayon"));
                System.err.println(utilisateur);
            }
        } catch (SQLException e) {
            throw new DAOException("Erreur BD " + e.getMessage(), e);
        } finally{
            closeConnection(conn);
        }
        return utilisateur;
    }
    
    public String getAdresse(String email) throws DAOException{
        String adresse = null;
        String requeteSQL;
        Connection conn =  null;
        ResultSet rs = null;
        try{
            conn = getConnection();
            Statement st = conn.createStatement();
            requeteSQL = "SELECT DISTINCT adresse FROM Utilisateurs WHERE email='"
                    + email +"'";
            rs = st.executeQuery(requeteSQL);
            if(rs.next()){
                adresse = rs.getString("adresse");
            }
        }
        catch (SQLException e) {
            throw new DAOException("Erreur BD " + e.getMessage(), e);
        } finally{
            closeConnection(conn);
        }
        return adresse;
    }
    
    public void ajouterCompetences(String email, String competence) throws DAOException {
        String requeteSQL;
        Connection conn = null;
        try {
            conn = getConnection();
            Statement st = conn.createStatement();
            requeteSQL = "INSERT INTO CompetencesUtilisateurs VALUES (\'"
                    + email + "\',\'" + competence + "\')";
            st.executeUpdate(requeteSQL);
        } catch (SQLException e) {
            throw new DAOException("Erreur BD " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
    }

    public void mettreAJourCompetences(String email, String nomCompetence, boolean cochee) throws DAOException {
        String requeteSQL;
        Connection conn = null;
        try {
            conn = getConnection();
            Statement st = conn.createStatement();
            if (cochee) {
                requeteSQL = "DELETE From CompetencesUtilisateurs WHERE idUtilisateur='"
                        + email + "' AND competence='" + nomCompetence +"'";
                st.executeUpdate(requeteSQL);
                requeteSQL = "INSERT INTO CompetencesUtilisateurs VALUES (\'"
                        + email + "\',\'" + nomCompetence + "\')";
            } else {
                requeteSQL = "DELETE From CompetencesUtilisateurs WHERE idUtilisateur='"
                        + email + "' AND competence='" + nomCompetence +"'";
            }
            st.executeUpdate(requeteSQL);
        } catch (SQLException e) {
            throw new DAOException("Erreur SQL 'mettreAJourCompetences' " + e.getMessage(), e);
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
    
    
    public void ajouterUtilisateur(String email, String mdp, String nom, String prenom, int genre, String date, String adresse, int rayon) throws DAOException {
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
            String requeteSQL = "INSERT INTO Utilisateurs VALUES (\'"+ email.replaceAll("'", "''")
                    + "\',\'" + nom.replaceAll("'", "''") + "\',\'" + prenom.replaceAll("'", "''")
                    + "\',\'" + mdp.replaceAll("'", "''") + "\'," + genre
                    + ",TO_date(\'" + date + "\','yyyy-mm-dd'),"
                    + results[0].geometry.location.lat + "," + results[0].geometry.location.lng
                    + ",\'" + adresse.replaceAll("'", "''") + "\',-1," + rayon +")";
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
    public void mettreAJourUtilisateur(String email, String mdp, String nom, String prenom, int genre, String date, String adresse, int rayon) throws DAOException {
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
            String requeteSQL = "UPDATE Utilisateurs SET nom='" + nom.replaceAll("'", "''")
                    + "', prenom='" + prenom.replaceAll("'", "''")
                    + "', hash_de_motdepasse='" + mdp.replaceAll("'", "''") + "', genre=" + genre
                    + ", datedenaissance=TO_date('" + date + "','yyyy/mm/dd'), latitude="
                    + coordonnees.getLatitude() + ", longitude=" + coordonnees.getLongitude()
                    + ", adresse='" + adresse.replaceAll("'", "''")
                    + "', rayon=" + rayon
                    + " WHERE email='"+ email.replaceAll("'", "''") + "'";
            st.executeUpdate(requeteSQL);
        } catch (SQLException e) {
            throw new DAOException("Erreur BD " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
    }
    
    /**
     * Récupère les tâches d'un utilisateur (en tant que commanditaire) dans la base de données
     * @param email l'identifiant de l'utilisateur dont on recherche les tâches
     * @return la liste des tâches trouvées ou null
     * @throws dao.DAOException
     */
    public ArrayList<Tache> getTachesCommanditaire(String email) throws DAOException {
        ArrayList<Tache>  listeTaches = new ArrayList<>() ;
        ResultSet rs, rsAtomiques;
        String requeteSQL;
        Connection conn = null;
        try {
            Tache t;
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
                            rsAtomiques.getString("idCommanditaire"), rsAtomiques.getString("idExecutant"),null,rsAtomiques.getInt("indicateurFin"),
                            getAdresse(email)));
                }
                t = new Tache(idTache, email, titre, listTachesAtomiques);
                if (!t.isOver()) {
                    listeTaches.add(t);
                }
            }
        } catch (SQLException e) {
            throw new DAOException(e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
        return listeTaches;
    }
    
    /**
     * Récupère les tâches d'un utilisateur (en tant que commanditaire) dans la base de données même les finies
     * @param email l'identifiant de l'utilisateur dont on recherche les tâches
     * @return la liste des tâches trouvées ou null
     * @throws dao.DAOException
     */
    public ArrayList<Tache> getToutesTachesCommanditaire(String email) throws DAOException {
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
                            rsAtomiques.getString("idCommanditaire"), rsAtomiques.getString("idExecutant"),null,rsAtomiques.getInt("indicateurFin"),
                            getAdresse(email)));
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
     * Récupère l'id de la dernière tâche ajoutée par un utilisateur (en tant que commanditaire) dans la base de données
     * @param email l'identifiant du commanditaire
     * @return  idTache l'id de la dernière tâche ajoutée
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
            requeteSQL = "SELECT Max(idTache) as max FROM Taches where idCommanditaire ='" + email + "'";
            rs = st.executeQuery(requeteSQL);
            if(rs.next()) {
                idTache = rs.getInt("max");
            }
        } catch (SQLException e) {
            throw new DAOException(e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
        return idTache;
    }

    /**
     * Récupère l'id de la dernière tâche atomique ajoutée par un utilisateur (en tant que commanditaire) dans la base de données
     * @param email l'identifiant du commanditaire
     * @return  idTache l'id de la dernière tâche ajoutée
     * @throws dao.DAOException
     */
    public int getIdLastTacheAtom(String email) throws DAOException {
        ResultSet rs;
        String requeteSQL;
        Connection conn = null;
        int idTache = -1;
        try {
            conn = getConnection();
            Statement st = conn.createStatement();
            requeteSQL = "SELECT Max(idTacheAtom) as max FROM TachesAtom where idCommanditaire ='" + email + "'";
            rs = st.executeQuery(requeteSQL);
            if(rs.next()) {
                idTache = rs.getInt("max");
            }
        } catch (SQLException e) {
            throw new DAOException(e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
        return idTache;
    }
    
    /**
     * Retourne liste des idTacheAtom des tâches où il a candidaté
     * @param utilisateur
     * @return
     * @throws DAOException 
     */
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

    /**
     * Récupère le nombre de candidatures reçues par le commanditaire pour toutes ses tâches (atomiques)
     * @param utilisateur commanditaire
     * @return table de hachage liant les numéros des tâches du commanditaire au nombre de candidature qu'a chaque tâche (atomique)
     * @throws DAOException 
     */
    public HashMap<Integer, Integer> getNbCandidaturesCommanditaire(Utilisateurs utilisateur) throws DAOException {
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
            throw new DAOException("Erreur SQL 'getNbCandidaturesCommanditaire'", e);
        } finally {
            closeConnection(conn);
        }
        return candidatures;
    }

    /**
     * Récupère les détails de candidatures reçues par un commanditaire pour une tâche atomique particulière
     * @param utilisateur commanditaire
     * @param idTacheAtom numéro de tâche atomique dont on veut récupérer les candidatures
     * @return Liste contenant les emails des candidats à une tâche atomique
     * @throws DAOException 
     */
    public ArrayList<String> getCandidaturesCommanditaire(Utilisateurs utilisateur, int idTacheAtom) throws DAOException {
        ArrayList<String> listeCandidats = new ArrayList<>();
        Connection conn = null;
        try {
            conn = getConnection();
            Statement st = conn.createStatement();
            String requeteSQL = "SELECT idCandidat FROM Candidatures WHERE idTacheAtom="
                    + idTacheAtom;
            ResultSet rs = st.executeQuery(requeteSQL);
            while (rs.next()) {
                listeCandidats.add(rs.getString("idCandidat"));
                }
        } catch (SQLException e) {
            throw new DAOException("Erreur SQL 'getCandidaturesCommanditaire'", e);
        } finally {
            closeConnection(conn);
        }
        return listeCandidats;
    }

    public boolean proposedThisTask(int idTache, Utilisateurs utilisateur) throws DAOException {
        Connection conn = null;
        try {
            conn = getConnection();
            Statement st = conn.createStatement();
            String requeteSQL = "SELECT idTache FROM Taches WHERE idCommanditaire='" + utilisateur.getEmail() + "'";
            ResultSet rs = st.executeQuery(requeteSQL);
            while (rs.next()) {
                if (rs.getInt("idTache")==idTache) {
                    closeConnection(conn);
                    return true;
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Erreur SQL 'proposedThisTask'", e);
        } finally {
            closeConnection(conn);
        }
        return false;
    }
    
    /**
     * Indique si la tache atomique d'id idTacheAtom a été proposée par l'utilisateur utilisateur
     * @param idTacheAtom
     * @param utilisateur
     * @return true si l'utilisateur a bien proposé cette tâche
     * @throws DAOException 
     */
    public boolean proposedThisAtomTask(int idTacheAtom, Utilisateurs utilisateur) throws DAOException {
        Connection conn = null;
        try {
            conn = getConnection();
            Statement st = conn.createStatement();
            String requeteSQL = "SELECT idTacheAtom FROM TachesAtom WHERE idCommanditaire='" + utilisateur.getEmail() + "'";
            ResultSet rs = st.executeQuery(requeteSQL);
            while (rs.next()) {
                if (rs.getInt("idTacheAtom")==idTacheAtom) {
                    closeConnection(conn);
                    return true;
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Erreur SQL 'proposedThisTask'", e);
        } finally {
            closeConnection(conn);
        }
        return false;
    }
    
    public void supprimerUtilisateur(String email) throws DAOException{
        Connection conn = null;
        try {
            conn = getConnection();
            Statement st = conn.createStatement();
            String requeteSQL = "DELETE FROM Utilisateurs WHERE email='" + email + "'";
            st.executeUpdate(requeteSQL);
        } catch (SQLException e) {
            throw new DAOException("Erreur SQL 'supprimerUtilisateur'", e);
        } finally {
            closeConnection(conn);
        }
    }
    
    /**
     * Récupère la liste des tâches auxquelles l'utilisateur peut postuler, 
     * selon ses compétences
     * @param utilisateur
     * @return
     * @throws DAOException 
     */
    public HashMap<Integer,TacheAtom> getTachesPotentielles(Utilisateurs utilisateur) throws DAOException{
        HashMap<Integer,TacheAtom> liste = null;
        ArrayList<Competences> comp = null;
        String email = utilisateur.getEmail();
        String emailCommanditaire = null;
        TacheAtom tache;
        ResultSet rs, rsDist;
        String requeteSQL;
        Connection conn = null;
        try {
            conn = getConnection();
            Statement st = conn.createStatement();
            if(utilisateur.getRayon() == -1){
                requeteSQL = "SELECT * FROM TachesAtom t LEFT JOIN CompetencesTaches c "//Pour avoir toutes les tâches
                        + "ON t.idtacheatom=c.idtacheatom AND t.idcommanditaire=c.idcommanditaire "//associées à leurs compétences
                        + "WHERE t.idCommanditaire !='" + email + "' AND t.indicateurfin = 0"//Sauf celles proposées par celui qui consulte la liste et sauf celles qui sont finies
                        + "AND ("
                            + "c.competence IN (SELECT competence FROM CompetencesUtilisateurs WHERE idutilisateur='" + email + "')"
                            + " OR"
                            + " c.competence is null)"
                        + " AND t.idTacheATom NOT IN (SELECT idTacheAtom FROM Candidatures WHERE idCandidat ='" + email + "')"
                        + " AND t.idExecutant is null";
                
                rs = st.executeQuery(requeteSQL);
                if (rs.getFetchSize()>0) {
                    liste = new HashMap<>();
                    while (rs.next()) {
                        int idTacheAtom = rs.getInt("idTacheAtom");
                        if (liste.containsKey(idTacheAtom)) {
                            liste.get(idTacheAtom).ajouterCompetences(rs.getString("competence"));
                        }
                        else {
                            emailCommanditaire =rs.getString("idcommanditaire");
                            comp = new ArrayList<Competences>();
                            comp.add(new Competences(rs.getString("competence")));
                            liste.put(idTacheAtom,new TacheAtom(idTacheAtom, rs.getInt("idTacheMere"), rs.getString("titretacheatom"),rs.getString("descriptiontache"), 
                                rs.getFloat("prixtache"), new Coordonnees(rs.getDouble("latitude"), rs.getDouble("longitude")),
                                rs.getDate("dateplustot"), rs.getDate("dateplustard"), rs.getString("idcommanditaire"),
                                comp, getAdresse(emailCommanditaire)));
                        }
                    }
                }
            }
            else {
                int rayon = utilisateur.getRayon();
                GeoApiContext context = new GeoApiContext().setApiKey("AIzaSyCIhR44YdJoRc8tqOQ8SFslDZ3PX-SYDtQ");
                try { 
                    requeteSQL = "SELECT * FROM TachesAtom t LEFT JOIN CompetencesTaches c "//Pour avoir toutes les tâches
                        + "ON t.idtacheatom=c.idtacheatom AND t.idcommanditaire=c.idcommanditaire "//associées à leurs compétences
                        + "WHERE t.idCommanditaire !='" + email + "' AND t.indicateurfin = 0"//Sauf celles proposées par celui qui consulte la liste et sauf celles qui sont finies
                        + "AND ("
                            + "c.competence IN (SELECT competence FROM CompetencesUtilisateurs WHERE idutilisateur='" + email + "')"
                            + " OR"
                            + " c.competence is null)"
                        + " AND t.idTacheATom NOT IN (SELECT idTacheAtom FROM Candidatures WHERE idCandidat ='" + email + "')"
                        + " AND t.idExecutant is null";
                    rs = st.executeQuery(requeteSQL);
                    if (rs.getFetchSize()>0) {
                        liste = new HashMap<>();
                        while(rs.next()) {
                            int idTacheAtom = rs.getInt("idTacheAtom");
                            
                            DistanceMatrixApiRequest distReq = new DistanceMatrixApiRequest(context);
                            distReq.origins(new LatLng(utilisateur.getLocalisation().getLatitude(), utilisateur.getLocalisation().getLongitude()));
                            distReq.destinations(new LatLng(rs.getDouble("latitude"), rs.getDouble("longitude")));
                            DistanceMatrix dist = distReq.await();
                            if(dist.rows[0].elements[0].distance.inMeters <= rayon) {
                                if (liste.containsKey(idTacheAtom)) {
                                    liste.get(idTacheAtom).ajouterCompetences(rs.getString("competence"));
                                }
                                else {
                                    comp = new ArrayList<Competences>();
                                    comp.add(new Competences(rs.getString("competence")));
                                    liste.put(idTacheAtom,new TacheAtom(idTacheAtom, rs.getInt("idTacheMere"), rs.getString("titretacheatom"),rs.getString("descriptiontache"), 
                                        rs.getFloat("prixtache"), new Coordonnees(rs.getDouble("latitude"), rs.getDouble("longitude")),
                                        rs.getDate("dateplustot"), rs.getDate("dateplustard"), rs.getString("idcommanditaire"),
                                        comp, getAdresse(rs.getString("idCommanditaire"))));
                                }
                            }
                        }
                    }
                }catch (Exception ex) { 
                    throw new DAOException("Erreur Geocoding " + ex.getMessage(), ex);
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Erreur BD " + ex.getMessage(), ex);
        } finally {
            closeConnection(conn);
        }
        return liste;
    }
    
    /**
     * Retourne les tâches finies de l'exécutant
     * @param utilisateur
     * @return
     * @throws DAOException 
     */
    public ArrayList<TacheAtom> getTachesExecutantFinies(Utilisateurs utilisateur) throws DAOException {
        ArrayList<TacheAtom> liste = new ArrayList<TacheAtom>();
        String email = utilisateur.getEmail();
        TacheAtom tache;
        ResultSet rs;
        String requeteSQL;
        Connection conn = null;
        try {
            conn = getConnection();
            Statement st = conn.createStatement();
            requeteSQL = "SELECT * FROM TachesAtom WHERE idExecutant='" + email + "' AND indicateurFin = 1";
            rs = st.executeQuery(requeteSQL);
            while (rs.next()) {
                tache = new TacheAtom(rs.getInt("idTacheAtom"),
                                rs.getInt("idTacheMere"),
                                rs.getString("titreTacheAtom"),
                                rs.getString("descriptionTache"),
                                rs.getFloat("prixTache"),
                                new Coordonnees(rs.getFloat("latitude"),rs.getFloat("longitude")),
                                rs.getDate("datePlusTot"),
                                rs.getDate("datePlusTard"),
                                rs.getString("idCommanditaire"),
                                null, getAdresse(rs.getString("idCommanditaire")));
                System.err.println(tache);
                liste.add(tache);
            }
        } catch (SQLException e) {
            throw new DAOException("Erreur BD " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
        
        return liste;
    }       

    /**
     * Récupère les tâches (atomiques) pour lequel un utilisateur est exécutant et qui ne sont pas finies
     * @param email utilisateur dont on veut trouver les tâches qu'il exécute
     * @return une liste des tâches atomiques
     * @throws DAOException 
     */
    public ArrayList<TacheAtom> getTachesEnCours(String email) throws DAOException {
        Connection conn = null;
        ArrayList<TacheAtom> listeTachesAtom = null;
        try {
            conn = getConnection();
            Statement st = conn.createStatement();
            String requeteSQL = "SELECT * FROM TachesAtom WHERE idExecutant='" + email + "' AND indicateurFin=0";
            ResultSet rs = st.executeQuery(requeteSQL);
            if (rs.next()) {
                listeTachesAtom = new ArrayList<>();
                listeTachesAtom.add(new TacheAtom(rs.getInt("idTacheAtom"), rs.getInt("idTacheMere"), rs.getString("titreTacheAtom"),
                        rs.getString("descriptionTache"), rs.getFloat("prixTache"),
                        new Coordonnees(rs.getFloat("latitude"), rs.getFloat("longitude")),
                        rs.getDate("datePlusTot"), rs.getDate("datePlusTard"), rs.getString("idCommanditaire"), null,
                        getAdresse(rs.getString("idCommanditaire"))));
                while (rs.next()) {
                    listeTachesAtom.add(new TacheAtom(rs.getInt("idTacheAtom"), rs.getInt("idTacheMere"), rs.getString("titreTacheAtom"),
                        rs.getString("descriptionTache"), rs.getFloat("prixTache"),
                        new Coordonnees(rs.getFloat("latitude"), rs.getFloat("longitude")),
                        rs.getDate("datePlusTot"), rs.getDate("datePlusTard"), rs.getString("idCommanditaire"), null,
                        getAdresse(rs.getString("idCommanditaire"))));
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Erreur SQL 'getTachesEnCours' " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
        return listeTachesAtom;
    }
    
    /**
     * Génère la facture (en pdf)
     * @param idTacheAtom
     * @param out
     * @throws DAOException
     * @throws IOException 
     */
    public void genereFacture(int idTacheAtom, OutputStream out) throws DAOException, IOException {
        Connection conn = null;
        try {
            conn = getConnection();
            Statement st = conn.createStatement();
            String requeteSQL = "SELECT * FROM TachesAtom WHERE idTacheAtom=" + idTacheAtom;
            ResultSet rs = st.executeQuery(requeteSQL);
            if (rs.next()) {
                Document document = new Document();
                ArrayList<String> listStrings = new ArrayList<>();
                listStrings.add("Titre de la tâche");
                listStrings.add("Description fournie par le commanditaire");
                listStrings.add("Récompense");
                listStrings.add(rs.getString("titreTacheAtom"));
                listStrings.add(rs.getString("descriptionTache"));
                listStrings.add(Float.toString(rs.getFloat("prixTache")) + "€");
                try {
                    PdfWriter writer = PdfWriter.getInstance(document, out);
                    Rectangle size = new Rectangle(595,842);
                    document.setPageSize(size);
                    document.setMargins(20, 20, 20, 20);
                    document.open();
                    document.addTitle("Facture - CrowdHelping");
                    document.addAuthor("Crowdhelping");
                    Paragraph titre = new Paragraph("Facture", new Font(BaseFont.createFont(), 32));
                    titre.setIndentationLeft(40);
                    document.add(titre);
                    Paragraph nomSite = new Paragraph("CrowdHelping", new Font(BaseFont.createFont(), 25, Font.ITALIC));
                    nomSite.setAlignment(Element.ALIGN_RIGHT);
                    nomSite.setIndentationRight(40);
                    document.add(nomSite);
                    PdfPTable parties = new PdfPTable(1);
                    parties.setWidthPercentage(40);
                    parties.setHorizontalAlignment(Element.ALIGN_LEFT);
                    PdfPCell encadre = new PdfPCell();
                    encadre.addElement(new Paragraph("Exécutant: " + rs.getString("idExecutant")));
                    parties.addCell(encadre);
                    encadre = new PdfPCell();
                    encadre.addElement(new Paragraph("Commanditaire: " + rs.getString("idCommanditaire")));
                    parties.addCell(encadre);
                    parties.setSpacingAfter(30f);
                    document.add(parties);
                    int nbCol = 3;
                    int nblign = 2;
                    PdfPTable table = new PdfPTable(nbCol);
                    table.setExtendLastRow(true);
                    table.setHorizontalAlignment(Element.ALIGN_LEFT);
                    float[] widths = {20, 35, 15};
                    table.setTotalWidth(widths);
                    table.setWidthPercentage(100);
                    PdfPCell cell;
                    for (int i=0; i<nbCol*nblign; i++) {
                        cell = new PdfPCell();
                        cell.addElement(new Paragraph(listStrings.get(i)));
                        table.addCell(cell);
                    }
                    document.add(table);
                    document.close();
                } catch (DocumentException e) {
                    throw new DAOException("Erreur Génération de PDF", e);
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Erreur SQL 'genereFacture' " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
    }

    public boolean executesThisAtomTask(String email, int idTacheAtom) throws DAOException {
        Connection conn = null;
        boolean execute = false;
        try {
            conn = getConnection();
            Statement st = conn.createStatement();
            String requeteSQL = "SELECT idExecutant FROM TachesAtom WHERE idTacheAtom=" + idTacheAtom;
            ResultSet rs = st.executeQuery(requeteSQL);
            if (rs.next()) {
                if (rs.getString("idExecutant").equals(email)) execute = true;
            }
        } catch (SQLException e) {
            throw new DAOException("Erreur SQL 'executesThisAtomTask' " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
        return execute;
    }

    public void miseAJourMoyenneUtilisateur(Integer idTacheAtom) throws DAOException {
        Connection conn = null;
        try {
            conn = getConnection();
            Statement st = conn.createStatement();
            String requeteSQL = "UPDATE Utilisateurs SET evaluation="
                    + " (SELECT AVG(evaluation) FROM Evaluations WHERE idEvalue="
                    + "(SELECT idExecutant FROM TachesAtom WHERE idTacheAtom="
                    + idTacheAtom + ") ) WHERE email= (SELECT idExecutant FROM TachesAtom WHERE idTacheAtom="
                    + idTacheAtom + ")";
            st.executeUpdate(requeteSQL);
        } catch (SQLException e) {
            throw new DAOException("Erreur SQL 'miseAJourMoyenneUtilisateur' ", e);
        } finally {
            closeConnection(conn);
        }
    }
}
