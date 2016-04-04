/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controleur;

import dao.DAOException;
import dao.UtilisateurDAO;
import java.io.IOException;
import java.io.PrintWriter;
import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

/**
 *
 * @author hargec
 */
@WebServlet(name = "controleur", urlPatterns = {"/controleur"})
public class controleur extends HttpServlet {
    
    @Resource(name="jdbc/crowdhelping")
    private DataSource ds;
    
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        String action = request.getParameter("action");
        UtilisateurDAO utilisateurDAO = new UtilisateurDAO(ds);
       
        try {
            if(action == null) {
                actionLogin(request, response, utilisateurDAO);
            }
            if (action.equals("Connexion")) {
                actionConnexion(request, response, utilisateurDAO);
            }
            if(action.equals("Inscription")) {
                actionInscription(request, response, utilisateurDAO);
            }
            else {
                getServletContext().getRequestDispatcher("/WEB-INF/controleurErreur.jsp").forward(request, response);
            }
        } catch (DAOException e) {
            getServletContext().getRequestDispatcher("/WEB-INF/bdErreur.jsp").forward(request, response);
        }
    }
    
    
    public void actionLogin(HttpServletRequest request, HttpServletResponse response, UtilisateurDAO utilisateurDAO) throws DAOException, ServletException, IOException {
        getServletContext().getRequestDispatcher("/WEB-INF/login_accueil.jsp").forward(request, response);
    }
    
    public void actionInscription(HttpServletRequest request, HttpServletResponse response, UtilisateurDAO utilisateurDAO) throws DAOException, ServletException, IOException {
        getServletContext().getRequestDispatcher("/WEB-INF/inscription.jsp").forward(request, response);
    }
    
    public void actionConnexion(HttpServletRequest request, HttpServletResponse response, UtilisateurDAO utilisateurDAO) throws DAOException, ServletException, IOException {
        request.setAttribute("utilisateur",utilisateurDAO.getUtilisateur(request.getParameter("email")));
        getServletContext().getRequestDispatcher("/WEB-INF/user_page.jsp").forward(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
