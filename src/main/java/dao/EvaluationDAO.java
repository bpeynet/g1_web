package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.sql.DataSource;
import modeles.Evaluation;


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
            requestSQL = "INSERT INTO Evaluations VALUES (" + idTacheAtom.toString()
                + "," + evaluation.toString() + ", current_date , '" 
                + rs.getString("idCommanditaire") + "', '" 
                + rs.getString("idExecutant") + "','" + commentaire.replaceAll("'", "''") + "')";
            st.executeUpdate(requestSQL);
        } catch (SQLException e) {
            throw new DAOException("Erreur BD " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
    }
    
    public ArrayList commentairesEvaluation(String email) throws DAOException {
        ArrayList<Evaluation> listCommentaires = new ArrayList<>();
        Connection conn = null;
        try {
            conn = getConnection();
            Statement st = conn.createStatement();
            String requeteSQL = "SELECT * FROM Evaluations WHERE idEvalue='"
                    + email + "'";
            ResultSet rs = st.executeQuery(requeteSQL);
            while (rs.next()) {
                listCommentaires.add(new Evaluation(rs.getInt("idTache"),
                        rs.getInt("evaluation"),
                        rs.getDate("dateEval"),
                        rs.getString("commentaire")));
            }
        } catch (SQLException e) {
            throw new DAOException("Erreur SQL 'commentairesEvaluation "
                    + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
        return listCommentaires;
    }
    
}
