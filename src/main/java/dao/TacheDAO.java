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
import javax.sql.DataSource;
import modeles.Tache;
import modeles.Utilisateurs;

/**
 *
 * @author ben
 */
public class TacheDAO extends AbstractDataBaseDAO {
    
    public TacheDAO(DataSource ds) {
        super(ds);
    }
    
    /**
     * Récupère la tâche à partir de son id dans la base de données
     * @param id l'identifiant de la tâche recherchée
     * @param tacheAtomDAO
     * @return la tâche trouvée ou null
     * @throws dao.DAOException
     */
    public Tache getTache(int id, TacheAtomDAO tacheAtomDAO) throws DAOException {
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
                    tacheAtomDAO.getTaches(rs.getInt("idTache")));
            }
        } catch (SQLException e) {
            throw new DAOException(e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
        return tache;
    }
    
    public void ajouterTache(String titre, String idCommanditaire ) throws DAOException {
        Connection conn = null ;
        try {
            conn = getConnection();
            Statement st = conn.createStatement();
            String requestSQL = "INSERT INTO Taches VALUES (taches_sequence.nextval, '" + titre + "', '" + idCommanditaire + "')";
            st.executeUpdate(requestSQL);
        } catch (SQLException e) {
            throw new DAOException("Erreur BD " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
    }
    
    public ArrayList<Tache> getTaches(Utilisateurs utilisateur, TacheAtomDAO tacheAtomDAO) throws DAOException {
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
                    liste.add(new Tache(rs.getInt("idTache"), utilisateur.getEmail(),
                        rs.getString("titreTache"), tacheAtomDAO.getTaches(rs.getInt("idTache"))));
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Erreur BD " + ex.getMessage(), ex);
        } finally {
            closeConnection(conn);
        }
        return liste;
    }
}
