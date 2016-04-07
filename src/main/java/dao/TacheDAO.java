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
import modeles.TacheAtom;
import modeles.outils.Coordonnees;

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
                ArrayList<TacheAtom> listTachesAtomiques = new ArrayList<>();
                while (rsAtomiques.next()){
                    listTachesAtomiques.add(new TacheAtom(rsAtomiques.getInt("idTacheAtom"), rsAtomiques.getInt("idTacheMere"),rsAtomiques.getString("titreTacheAtom"),
                            rsAtomiques.getString("descriptionTache"), rsAtomiques.getFloat("prixTache"),
                            new Coordonnees(rsAtomiques.getFloat("latitude"), rsAtomiques.getFloat("longitude")),
                            rs.getDate("datePlusTot"), rs.getDate("datePlusTard"), null));
                }
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
    
    /**
     * Récupère la tâche à partir de son id dans la base de données
     * @param id l'identifiant de la tâche recherchée
     * @return la tâche trouvée ou null
     * @throws dao.DAOException
     */
    public Tache getTache(String email) throws DAOException {
        Tache  tache = null ;
        ResultSet rs, rsAtomiques;
        String requeteSQL;
        Connection conn = null;
        try {
            conn = getConnection();
            Statement st = conn.createStatement();
            requeteSQL = "SELECT * FROM Taches where idCommanditaire ='" + email + "'";
            rs = st.executeQuery(requeteSQL);
            if(rs.next()) {
                int idTache = rs.getInt("idTache");
                String titre = rs.getString("titreTache");
                requeteSQL = "SELECT * FROM TachesAtom where idTacheMere=" + idTache;
                rsAtomiques = st.executeQuery(requeteSQL);
                ArrayList<TacheAtom> listTachesAtomiques = new ArrayList<>();
                while (rsAtomiques.next()) {
                    listTachesAtomiques.add(new TacheAtom(rsAtomiques.getInt("idTacheAtom"), rsAtomiques.getInt("idTacheMere"),
                            rsAtomiques.getString("titreTacheAtom"),rsAtomiques.getString("descriptionTache"), rsAtomiques.getFloat("prixTache"),
                            new Coordonnees(rsAtomiques.getFloat("latitude"), rsAtomiques.getFloat("longitude")),
                            rsAtomiques.getDate("datePlusTot"), rsAtomiques.getDate("datePlusTard"), null));
                }
                tache = new Tache(idTache, titre, listTachesAtomiques);
            }
        } catch (SQLException e) {
            throw new DAOException(e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
        return tache;
    }
}
