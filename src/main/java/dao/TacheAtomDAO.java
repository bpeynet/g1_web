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
import modeles.Utilisateurs;
import modeles.outils.Coordonnees;

/**
 *
 * @author cl
 */
public class TacheAtomDAO extends AbstractDataBaseDAO{

    public TacheAtomDAO(DataSource ds) {
        super(ds);
    }
    
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
    
    public void ajouterCompetence(int id, String emailC, String emailEx, String competence) throws DAOException {
        ResultSet rs = null;
        String requeteSQL;
        Connection conn = null;
        try {
            conn = getConnection();
            Statement st = conn.createStatement();
            requeteSQL = "INSERT INTO CompetencesTaches VALUES (" + id + ", \'"+ emailC + ", \'" + competence + "\')";
            st.executeUpdate(requeteSQL);
        } catch (SQLException e) {
            throw new DAOException("Erreur BD " + e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
    }
    
    public TacheAtom getTacheAtom(int idTacheAtom) throws DAOException {
        TacheAtom  tache = null ;
        ResultSet rs;
        String requeteSQL;
        Connection conn = null;
        try {
            conn = getConnection();
            Statement st = conn.createStatement();
            requeteSQL = "SELECT * FROM TachesAtom where idTacheAtom=" + idTacheAtom;
            rs = st.executeQuery(requeteSQL);
            rs.next();
            tache = new TacheAtom(rs.getInt("idTacheAtom"), rs.getInt("idTacheMere"),rs.getString("titreTacheAtom"),
                        rs.getString("descriptionTache"), rs.getFloat("prixTache"),
                        new Coordonnees(rs.getFloat("latitude"), rs.getFloat("longitude")),
                        rs.getDate("datePlusTot"), rs.getDate("datePlusTard"), rs.getString("idCommanditaire"),
                        null);
        } catch (SQLException e) {
            throw new DAOException(e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
        return tache;
        }
    
    public void ajouterTacheAtom(String titre, String description, double prix, 
        String datetot, String datetard, String idCommanditaire, int idMere ) throws DAOException {
        Connection conn = null ;
        ResultSet rs;
        String requestSQL;
        try {
            conn = getConnection();
            Statement st = conn.createStatement();
            requestSQL = "SELECT * FROM Utilisateurs WHERE email='" + idCommanditaire + "'";
            rs = st.executeQuery(requestSQL);
            rs.next();
            requestSQL = "INSERT INTO TachesAtom VALUES (tachesatom_sequence.nextval, '" + titre + "', '"
                + description + "', " + prix + ", " + rs.getFloat("latitude") + ", " 
                + rs.getFloat("longitude") + ", " + "TO_date('"+ datetot + "','yyyy/mm/dd')"
                + ", TO_date('"+ datetard + "','yyyy/mm/dd'), "
                + idMere + ", '" + idCommanditaire + "', null, 0)" ;
            st.executeUpdate(requestSQL);
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
                        rs.getDate("datePlusTot"), rs.getDate("datePlusTard"), rs.getString("idCommanditaire"),
                        rs.getString("idExecutant"), null));
            }
        } catch (SQLException ex) {
            throw new DAOException("Erreur SQL", ex);
        } finally {
            closeConnection(conn);
        }
        return listTachesAtomiques;
    }

    public int postuler(Utilisateurs utilisateur, int idTacheAtom) throws DAOException {
        Connection conn = null;
        TacheAtom ta = getTacheAtom(idTacheAtom);
        int idTacheARetourner = ta.getIdTacheMere();
        String emailCommanditaire = ta.getEmailCommanditaire();
        try {
            conn = getConnection();
            Statement st = conn.createStatement();
            String requeteSQL = "INSERT INTO Candidatures VALUES (" + idTacheAtom
                    + ",'" + emailCommanditaire + "','" + utilisateur.getEmail() + "')";
            //Double candidature ? S'en prévenir ? Même si cela n'arrivera pas.
            st.executeUpdate(requeteSQL);
        } catch (SQLException e) {
            throw new DAOException("Erreur SQL 'postuler'",e);
        } finally {
            closeConnection(conn);
        }
        return idTacheARetourner;
    }

    public int depostuler(Utilisateurs utilisateur, Integer idTacheAtom) throws DAOException {
        Connection conn = null;
        int idTacheARetourner = getTacheAtom(idTacheAtom).getIdTacheMere();
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
    }
    
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

    public void accepterCandidature(String idCandidat, int idTacheAtom) throws DAOException {
        Connection conn = null;
        try {
            conn = getConnection();
            Statement st = conn.createStatement();
            String requeteSQL = "UPDATE TachesAtom SET idExecutant='" + idCandidat + "' WHERE idTacheAtom=" + idTacheAtom;
            st.executeUpdate(requeteSQL);
            requeteSQL = "DELETE FROM Candidatures WHERE idTacheAtom=" + idTacheAtom;
            st.executeUpdate(requeteSQL);
        } catch (SQLException e) {
            throw new DAOException("Erreur SQL 'accepterCandidature", e);
        } finally {
            closeConnection(conn);
        }
    }   

    /**
     * Indique une tâche comme finie
     * @param valueOf 
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