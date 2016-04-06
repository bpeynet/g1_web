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
import javax.sql.DataSource;
import modeles.Tache;

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
     * @return la tâche trouvée ou null
     * @throws dao.DAOException
     */
    public Tache getTache(int id) throws DAOException {
        Tache  tache = null ;
        ResultSet rs, rsAtomiques;
        String requeteSQL;
        Connection conn = null;
        try {
            conn = getConnection();
            Statement st = conn.createStatement();
            requeteSQL = "SELECT * FROM Taches where idTache =" + id;
            rs = st.executeQuery(requeteSQL);
            requeteSQL = "SELECT * FROM TachesAtom where idTacheMere=" + id;
            rsAtomiques = st.executeQuery(requeteSQL);
            if(rs.next()) {
                tache = new Tache(rs.getInt("idTache"),
                rs.getString("titreTache"),null);
            }
        } catch (SQLException e) {
            throw new DAOException(e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
        return tache;
    }
}
