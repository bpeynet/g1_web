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
import modeles.TacheAtom;
import modeles.outils.Coordonnees;

/**
 *
 * @author cl
 */
public class TacheAtomDAO extends AbstractDataBaseDAO{

    public TacheAtomDAO(DataSource ds) {
        super(ds);
    }
    
    public void ajouterCompetence(int id, String emailC, String emailEx, String competence) throws DAOException {
        ResultSet rs = null;
        String requeteSQL;
        Connection conn = null;
        try {
            conn = getConnection();
            Statement st = conn.createStatement();
            requeteSQL = "INSERT INTO CompetencesTaches VALUES (" + id + ", \'"+ emailC + ", \'"+ emailEx + "\',\'" + competence + "\')";
            st.executeUpdate(requeteSQL);
        } catch (SQLException e) {
            throw new DAOException("Erreur BD " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
    }
    
    public ArrayList<TacheAtom> getTaches(int idTacheMere) throws DAOException {
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
                        rs.getDate("datePlusTot"), rs.getDate("datePlusTard"), null));
            }
        } catch (SQLException ex) {
            throw new DAOException("Erreur SQL", ex);
        } finally {
            closeConnection(conn);
        }
        return listTachesAtomiques;
    }
    
}
