package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.sql.DataSource;
import modeles.Tache;
import modeles.Utilisateurs;


public class TacheDAO extends AbstractDataBaseDAO {
    
    public TacheDAO(DataSource ds) {
        super(ds);
    }
    
    /**
     * Récupère la tâche à partir de son id dans la base de données
     * @param id l'identifiant de la tâche recherchée
     * @param tacheAtomDAO DAO TacheAtom
     * @param competenceDAO DAO Competence
     * @param utilisateurDAO DAO Utilisateur
     * @return la tâche trouvée ou null
     * @throws dao.DAOException DAO Exception
     */
    public Tache getTache(int id, TacheAtomDAO tacheAtomDAO, CompetenceDAO competenceDAO, UtilisateurDAO utilisateurDAO) throws DAOException {
        Tache  tache = null ;
        ResultSet rs;
        String requeteSQL;
        Connection conn = null;
        try {
            conn = getConnection();
            Statement st = conn.createStatement();
            requeteSQL = "SELECT * FROM Taches where idTache =" + id;
            rs = st.executeQuery(requeteSQL);
            if(rs.next()) {
                tache = new Tache(rs.getInt("idTache"),
                    rs.getString("idCommanditaire"),
                    rs.getString("titreTache"),
                    tacheAtomDAO.getTaches(rs.getInt("idTache"), competenceDAO, utilisateurDAO));
            }
        } catch (SQLException e) {
            throw new DAOException(e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
        return tache;
    }

    /**
     * Ajoute une tâche
     * @param titre titre de la tâche
     * @param idCommanditaire email du commanditaire
     * @throws DAOException DAO Exception
     */
    public void ajouterTache(String titre, String idCommanditaire ) throws DAOException {
        Connection conn = null ;
        try {
            conn = getConnection();
            Statement st = conn.createStatement();
            String requestSQL = "INSERT INTO Taches VALUES (taches_sequence.nextval, '"
                    + titre.replaceAll("'", "''") + "', '" + idCommanditaire + "')";
            st.executeUpdate(requestSQL);
        } catch (SQLException e) {
            throw new DAOException("Erreur BD " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
    }
    
    /**
     * Récupère la liste des tâches auxquelles l'utilisateur peut postuler
     * @param utilisateur utilisateur
     * @param tacheAtomDAO DAO TacheAtom
     * @param competenceDAO DAO Competence
     * @param ut DAO Utilisateur
     * @return Liste des tâches potentielles
     * @throws DAOException DAO Exception
     */
    public ArrayList<Tache> getTaches(Utilisateurs utilisateur, TacheAtomDAO tacheAtomDAO, CompetenceDAO competenceDAO, UtilisateurDAO ut) throws DAOException {
        ArrayList<Tache> liste = null;
        ResultSet rs;
        String requeteSQL;
        Connection conn = null;
        try {
            conn = getConnection();
            Statement st = conn.createStatement();
            requeteSQL = "SELECT * FROM Taches WHERE idCommanditaire !='" + utilisateur.getEmail() + "'";
            rs = st.executeQuery(requeteSQL);
            if (rs.getFetchSize()>0) {
                liste = new ArrayList<>();
                while (rs.next()) {
                    liste.add(new Tache(rs.getInt("idTache"), rs.getString("idCommanditaire"),
                        rs.getString("titreTache"), tacheAtomDAO.getTaches(rs.getInt("idTache"), competenceDAO, ut)));
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
     * Supprime une tâche
     * @param idTache id de la tâche à supprimer
     * @throws DAOException DAO Exception
     */
    public void supprimerTache(Integer idTache) throws DAOException {
        Connection conn = null;
        try {
            conn = getConnection();
            Statement st = conn.createStatement();
            String requeteSQL = "DELETE FROM Taches WHERE idTache=" + idTache;
            st.executeUpdate(requeteSQL);
        } catch (SQLException e) {
            throw new DAOException("Erreur SQL 'supprimerTache'", e);
        } finally {
            closeConnection(conn);
        }
    }
}