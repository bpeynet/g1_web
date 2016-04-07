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
import modeles.outils.Competences;

/**
 *
 * @author moreaale
 */
public class CompetenceDAO extends AbstractDataBaseDAO {

    public CompetenceDAO(DataSource ds) {
        super(ds);
    }
    
    public ArrayList<Competences> getListCompetences()throws DAOException {
        ArrayList<Competences> result = new ArrayList<Competences>();
        Connection conn = null;
        try {
            conn = getConnection();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM competences");
            while (rs.next()) {
                Competences competence =
                    new Competences(rs.getString("nomComp"));
                result.add(competence);
            }
        } catch (SQLException e) {
            throw new DAOException("Erreur BD " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
        return result;
    }
    
}
