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

/**
 *
 * @author ralambom
 */
public class EvaluationDAO extends AbstractDataBaseDAO {
    
    public EvaluationDAO(DataSource ds) {
        super(ds);
    }
    
    public void ajouterEvaluation(Integer evaluation, Integer idTacheAtom, String commentaire) throws DAOException {
        Connection conn = null ;
        ResultSet rs;
        String requestSQL;
        try {
            conn = getConnection();
            Statement st = conn.createStatement();
            requestSQL = "SELECT * FROM TachesAtom WHERE idTacheAtom=" + idTacheAtom.toString();
            rs = st.executeQuery(requestSQL);
            rs.next();
            requestSQL = "INSERT INTO Evaluations VALUES (" + idTacheAtom.toString() + ","+ evaluation.toString() + ", current_date , '" 
                + rs.getString("idCommanditaire") + "', '" 
                + rs.getString("idExecutant") + "','" + commentaire + "')";
            st.executeUpdate(requestSQL);
        } catch (SQLException e) {
            throw new DAOException("Erreur BD " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
    }
    
}
