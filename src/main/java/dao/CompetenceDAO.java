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
import javax.servlet.http.HttpServletRequest;
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
        ArrayList<Competences> result = new ArrayList<>();
        Connection conn = null;
        try {
            conn = getConnection();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM competences");
            while (rs.next()) {
                Competences competence =
                    new Competences(rs.getString("competence"));
                result.add(competence);
            }
        } catch (SQLException e) {
            throw new DAOException("Erreur BD " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
        return result;
    }
    
    public ArrayList<Competences> getListCompetences(int idTacheAtom) throws DAOException {
        ArrayList<Competences> listCompetences = new ArrayList<>();
        Connection conn = null;
        try {
            conn = getConnection();
            Statement st = conn.createStatement();
            String requeteSQL = "SELECT competence FROM CompetencesTaches"
                    + " WHERE idTacheAtom=" + idTacheAtom;
            ResultSet rs = st.executeQuery(requeteSQL);
            while (rs.next()) {
                listCompetences.add(new Competences(rs.getString("competence")));
            }
        } catch (SQLException e) {
            throw new DAOException("Erreur SQL 'getListCompetences' " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
        return listCompetences;
    }
    
    public ArrayList<Competences> whichCompetences(HttpServletRequest request) throws DAOException {
        ArrayList<Competences> listCompetences = new ArrayList<>();
        Connection conn = null;
        try {
            conn = getConnection();
            Statement st = conn.createStatement();
            String requeteSQL = "SELECT competence FROM Competences";
            ResultSet rs = st.executeQuery(requeteSQL);
            while (rs.next()) {
                if (request.getParameter(rs.getString("competence")) != null)
                    listCompetences.add(new Competences(rs.getString("competence")));
            }
        } catch (SQLException e) {
            throw new DAOException("Erreur SQL 'whichCompetences' " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
        return listCompetences;
    }

}
