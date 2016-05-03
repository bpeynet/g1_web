package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.sql.DataSource;
import modeles.TacheAtom;
import modeles.Utilisateurs;
import modeles.outils.Competences;
import modeles.outils.Coordonnees;


public class TacheAtomDAO extends AbstractDataBaseDAO{

    public TacheAtomDAO(DataSource ds) {
        super(ds);
    }
    
    /**
     * Vérifie si une tâche est finie.
     * @param idTacheAtom id de la tâche qui est vérifiée
     * @return true si la tâche est finie
     * @throws DAOException DAOException 
     */
    public boolean isOver(int idTacheAtom) throws DAOException {
        Connection conn = null;
        boolean fini = false;
        try {
            conn = getConnection();
            Statement st = conn.createStatement();
            String requeteSQL = "SELECT indicateurFin FROM TachesAtom WHERE idTacheAtom=" + idTacheAtom;
            ResultSet rs = st.executeQuery(requeteSQL);
            if (rs.next()) {
                if (rs.getInt("indicateurFin")==1) fini = true;
            }
        } catch (SQLException e) {
            throw new DAOException("Erreur SQL 'isOver'" + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
        return fini;
    }
    
    /**
     * Ajoute une compétence nécessaire à une tâche.
     * @param id id de la tâche concernée par cette compétence
     * @param emailC email du commanditaire de la tâche
     * @param competence compétence ajoutée
     * @throws DAOException DAOException 
     */
    public void ajouterCompetence(int id, String emailC, String competence) throws DAOException {
        ResultSet rs = null;
        String requeteSQL;
        Connection conn = null;
        try {
            conn = getConnection();
            Statement st = conn.createStatement();
            requeteSQL = "INSERT INTO CompetencesTaches VALUES (" + id + ", \'"+ emailC + "\', \'" + competence + "\')";
            st.executeUpdate(requeteSQL);
        } catch (SQLException e) {
            throw new DAOException("Erreur BD " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
    }
    
    /**
     * Récupère la tâche atomique d'id idTacheAtom
     * @param idTacheAtom id de la tâche atomique
     * @param utilisateurDAO DAO Utilisateur
     * @return la tâche atomique demandée ou null
     * @throws DAOException DAOException
     */
    public TacheAtom getTacheAtom(int idTacheAtom, UtilisateurDAO utilisateurDAO) throws DAOException {
        TacheAtom  tache = null ;
        ResultSet rs;
        String requeteSQL;
        Connection conn = null;
        try {
            conn = getConnection();
            Statement st = conn.createStatement();
            requeteSQL = "SELECT * FROM TachesAtom where idTacheAtom=" + idTacheAtom;
            rs = st.executeQuery(requeteSQL);
            if (rs.next()) {
                String email = rs.getString("idCommanditaire");
                tache = new TacheAtom(rs.getInt("idTacheAtom"),
                            rs.getInt("idTacheMere"),rs.getString("titreTacheAtom"),
                            rs.getString("descriptionTache"), rs.getFloat("prixTache"),
                            new Coordonnees(rs.getFloat("latitude"), rs.getFloat("longitude")),
                            rs.getDate("datePlusTot"), rs.getDate("datePlusTard"),
                            rs.getString("idCommanditaire"), rs.getString("idExecutant"),
                            null, rs.getInt("indicateurFin"), utilisateurDAO.getAdresse(email));
            }
        } catch (SQLException e) {
            throw new DAOException(e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
        return tache;
    }
    
    /**
     * Ajoute une tâche atomique à la base de données.
     * @param titre titre de la tâche
     * @param description description de la tâche
     * @param prix récompense offerte pour la tâche
     * @param datetot date au plus tôt du début d'exécution
     * @param datetard date au plus tard du début d'exécution
     * @param idCommanditaire email du commanditaire de la tâche
     * @param idMere id de la tâche à laquelle appartient cette tâche
     * @param listCompetences liste des compétences nécessaires pour cette tâche
     * @param utilisateurDAO DAO Utilisateur
     * @throws DAOException DAOException 
     */
    public void ajouterTacheAtom(String titre, String description, double prix, 
        String datetot, String datetard, String idCommanditaire, int idMere,
        ArrayList<Competences> listCompetences, UtilisateurDAO utilisateurDAO) throws DAOException {
        Connection conn = null ;
        ResultSet rs;
        String requestSQL;
        try {
            conn = getConnection();
            Statement st = conn.createStatement();
            requestSQL = "SELECT * FROM Utilisateurs WHERE email='" + idCommanditaire + "'";
            rs = st.executeQuery(requestSQL);
            rs.next();
            requestSQL = "INSERT INTO TachesAtom VALUES (tachesatom_sequence.nextval, '"
                + titre.replaceAll("'", "''") + "', '"
                + description.replaceAll("'", "''") + "', " + prix + ", " + rs.getFloat("latitude") + ", " 
                + rs.getFloat("longitude") + ", " + "TO_date('"+ datetot + "','yyyy/mm/dd')"
                + ", TO_date('"+ datetard + "','yyyy/mm/dd'), "
                + idMere + ", '" + idCommanditaire + "', null, 0)" ;
            st.executeUpdate(requestSQL);
            int lastIdTacheAtom = utilisateurDAO.getIdLastTacheAtom(idCommanditaire);
            for (Competences c : listCompetences) {
                ajouterCompetence(lastIdTacheAtom, idCommanditaire, c.getNomCompetence());
            }
        } catch (SQLException e) {
            throw new DAOException("Erreur BD " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
    }
        
    /**
     * Récuppère l'ensemble des tâches atomiques appartenant à la tâche mère demandée
     * @param idTacheMere id de la tâche mère
     * @param competenceDAO DAO Competence
     * @param ut DAO Utilisateur
     * @return la liste des tâches atomiques de cette tâche
     * @throws DAOException DAOException 
     */  
    public ArrayList<TacheAtom> getTaches(int idTacheMere, CompetenceDAO competenceDAO, UtilisateurDAO ut) throws DAOException {
        Connection conn=null;
        ResultSet rs;
        ArrayList<TacheAtom> listTachesAtomiques = null;
        try {
            conn = getConnection();
            Statement st = conn.createStatement();
            String requeteSQL = "SELECT * FROM TachesAtom where idTacheMere=" + idTacheMere;
            rs = st.executeQuery(requeteSQL);
            listTachesAtomiques = new ArrayList<>();
            while (rs.next()) {
                listTachesAtomiques.add(new TacheAtom(rs.getInt("idTacheAtom"), rs.getInt("idTacheMere"),
                        rs.getString("titreTacheAtom"),rs.getString("descriptionTache"), rs.getFloat("prixTache"),
                        new Coordonnees(rs.getFloat("latitude"), rs.getFloat("longitude")),
                        rs.getDate("datePlusTot"), rs.getDate("datePlusTard"), rs.getString("idCommanditaire"),
                        rs.getString("idExecutant"),
                        competenceDAO.getListCompetences(rs.getInt("idTacheAtom")),
                        rs.getInt("indicateurFin"), ut.getAdresse(rs.getString("idCommanditaire"))));
            }
        } catch (SQLException ex) {
            throw new DAOException("Erreur SQL", ex);
        } finally {
            closeConnection(conn);
        }
        return listTachesAtomiques;
    }

    /**
     * Ajoute une candidature pour un utilisateur et une tâche.
     * @param utilisateur utilisateur
     * @param idTacheAtom id de la tâche atomique
     * @param ut DAO Utilisateur
     * @return id de la tâche à laquelle appartient la tâche atomique pour laquelle on a postulé
     * @throws DAOException DAOException 
     */
    public int postuler(Utilisateurs utilisateur, int idTacheAtom, UtilisateurDAO ut) throws DAOException {
        Connection conn = null;
        TacheAtom ta = getTacheAtom(idTacheAtom,ut);
        int idTacheARetourner;
        if (ta != null) {
            idTacheARetourner = ta.getIdTacheMere();
            String emailCommanditaire = ta.getEmailCommanditaire();
            try {
                conn = getConnection();
                Statement st = conn.createStatement();
                String requeteSQL = "SELECT DISTINCT idTacheAtom FROM CompetencesTaches WHERE idTacheAtom="
                        + idTacheAtom
                        + " AND competence IN (SELECT competence FROM CompetencesUtilisateurs WHERE idUtilisateur='"
                        + utilisateur.getEmail() + "')";
                ResultSet rs = st.executeQuery(requeteSQL);
                if (rs.next()) {
                    requeteSQL = "SELECT * FROM Candidatures WHERE idCandidat='"
                            + utilisateur.getEmail() + "' AND idTacheAtom=" + idTacheAtom;
                    rs = st.executeQuery(requeteSQL);
                    if (!rs.next()) {
                        requeteSQL = "INSERT INTO Candidatures VALUES (" + idTacheAtom
                                + ",'" + emailCommanditaire + "','" + utilisateur.getEmail() + "')";
                        st.executeUpdate(requeteSQL);
                    }
                } else {
                    idTacheARetourner = -2;//Pas les compétences
                }
            } catch (SQLException e) {
                throw new DAOException("Erreur SQL 'postuler' " + e.getMessage(),e);
            } finally {
                closeConnection(conn);
            }
        } else {
            idTacheARetourner = -1;//Tâche inexistante
        }
        return idTacheARetourner;
    }

    /**
     * Retire la candidature d'un utilisateur pour une tâche.
     * @param utilisateur utilisateur
     * @param idTacheAtom id de la tâche atomique
     * @param ut DAO Utilisateur
     * @return l'id de la tâche mère à laquelle appartient la tâche atomique
     *          pour laquelle on retire sa candidature
     * @throws DAOException DAOException 
     */
    public int depostuler(Utilisateurs utilisateur, Integer idTacheAtom, UtilisateurDAO ut) throws DAOException {
        Connection conn = null;
        TacheAtom ta = getTacheAtom(idTacheAtom, ut);
        if (ta != null) {
            int idTacheARetourner = ta.getIdTacheMere();
            try {
                conn = getConnection();
                Statement st = conn.createStatement();
                String requeteSQL = "DELETE FROM Candidatures WHERE idTacheAtom=" + idTacheAtom +
                        "AND idCandidat='" + utilisateur.getEmail() + "'";
                st.executeUpdate(requeteSQL);
            } catch (SQLException e) {
                throw new DAOException("Erreur SQL 'depostuler'",e);
            } finally {
                closeConnection(conn);
            }
            return idTacheARetourner;
        } else {
            return -1;
        }
    }
    
    /**
     * Supprimer de la base de données une tâche atomique
     * @param idTacheAtom id de la tâche atomique
     * @throws DAOException DAO Exception
     */
    public void supprimerTacheAtom(Integer idTacheAtom) throws DAOException {
        Connection conn = null;
        try {
            conn = getConnection();
            Statement st = conn.createStatement();
            String requeteSQL = "DELETE FROM TachesAtom WHERE idTacheAtom=" + idTacheAtom;
            st.executeUpdate(requeteSQL);
        } catch (SQLException e) {
            throw new DAOException("Erreur SQL 'supprimerTacheAtom'", e);
        } finally {
            closeConnection(conn);
        }
    }

    /**
     * Accepte une candidature en enregistrant l'email du candidat retenu
     * après avoir vérifié que la candidature existe toujours.
     * Supprime ensuite toutes les candidatures pour cette tâche.
     * @param idCandidat email du candidat retenu
     * @param idTacheAtom id de la tâche pour laquelle le candidat est retenu
     * @return 0 si une candidature a bien été trouvée et acceptée, -1 sinon.
     * @throws DAOException DAOException 
     */
    public int accepterCandidature(String idCandidat, int idTacheAtom) throws DAOException {
        Connection conn = null;
        boolean erreur = false;
        try {
            conn = getConnection();
            Statement st = conn.createStatement();
            String requeteSQL = "SELECT * FROM Candidatures WHERE idCandidat='"
                    + idCandidat +"' AND idTacheAtom=" + idTacheAtom;
            ResultSet rs = st.executeQuery(requeteSQL);
            if (rs.next()) {
                requeteSQL = "UPDATE TachesAtom SET idExecutant='" + idCandidat + "' WHERE idTacheAtom=" + idTacheAtom;
                st.executeUpdate(requeteSQL);
                requeteSQL = "DELETE FROM Candidatures WHERE idTacheAtom=" + idTacheAtom;
                st.executeUpdate(requeteSQL);
            } else {
                erreur = true;
            }
        } catch (SQLException e) {
            throw new DAOException("Erreur SQL 'accepterCandidature", e);
        } finally {
            closeConnection(conn);
        }
        return erreur ? -1 : 0;
    }

    /**
     * Indique une tâche comme finie
     * @param idTacheAtom id de la tâche atomique
     * @throws dao.DAOException 
     */
    public void finir(int idTacheAtom) throws DAOException {
        Connection conn = null;
        try {
            conn = getConnection();
            Statement st = conn.createStatement();
            String requeteSQL = "UPDATE TachesAtom SET indicateurFin=1 WHERE idTacheAtom=" + idTacheAtom;
            st.executeUpdate(requeteSQL);
        } catch (SQLException e) {
            throw new DAOException("Erreur BD 'finir une tâche' : " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
    }
}