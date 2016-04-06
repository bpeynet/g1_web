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
import modeles.Utilisateurs;

/**
 *
 * @author hargec
 */
@WebServlet(name = "controleur", urlPatterns = {"/controleur"})
public class controleur extends HttpServlet {

    @Resource(name = "jdbc/crowdhelping")
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
            if (action == null) {
                if (request.getSession(false).getAttribute("utilisateur") == null) {
                    actionLogin(request, response, utilisateurDAO);
                } else {
                    getServletContext().getRequestDispatcher("/WEB-INF/user_page.jsp").forward(request, response);
                }
            } else if (action.equals("Deconnexion")) {
                request.getSession().invalidate();
                actionLogin(request, response, utilisateurDAO);
            } else switch (action) {
                case "Connexion": {
                    actionConnexion(request, response, utilisateurDAO);
                    break;
                }
                case "Inscription": {
                    actionInscription(request, response, utilisateurDAO);
                    break;
                }
                case "Validation": {
                    if (request.getSession(false).getAttribute("utilisateur") == null) 
                        actionValidationInscription(request, response, utilisateurDAO);
                    else actionValidationUpdateProfil(request, response, utilisateurDAO);
                    break;
                }
                case "AjoutTache": {
                    if (request.getSession(false).getAttribute("utilisateur") != null) {
                        actionAjoutTache(request, response, utilisateurDAO);
                    } else {
                        response.sendRedirect("./controleur");
                    }
                    break;
                }
                case "Profil": {
                    if(request.getSession(false).getAttribute("utilisateur") != null) {
                        actionConsulterProfil(request, response, utilisateurDAO);
                    } else {
                        response.sendRedirect("./controleur");
                    }
                    break;
                }
                default: {
                    getServletContext().getRequestDispatcher("/WEB-INF/controleurErreur.jsp").forward(request, response);
                    break;
                }
            }
        } catch (DAOException e) {
            request.setAttribute("erreur", e);
            request.setAttribute("erreur_message", e.getMessage());
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
        HttpSession session = request.getSession(true);
        session.setAttribute("utilisateur", utilisateurDAO.getUtilisateur(request.getParameter("email")));
        getServletContext().getRequestDispatcher("/WEB-INF/user_page.jsp").forward(request, response);
    }

    public void actionValidationInscription(HttpServletRequest request, HttpServletResponse response, UtilisateurDAO utilisateurDAO) throws DAOException, ServletException, IOException {
        String email = request.getParameter("email");
        String mdp = request.getParameter("mdp");
        String mdpConfirm = request.getParameter("mdpconfirm");
        String nom = request.getParameter("nom");
        String prenom = request.getParameter("prenom");
        String date = request.getParameter("date");
        String adresse = request.getParameter("adresse");
        int genre = Integer.valueOf(request.getParameter("genre"));
        if (mdp.equals(mdpConfirm)) {
            utilisateurDAO.ajouterUtilisateur(email, mdp, nom, prenom, genre, date, adresse);
            request.getSession(true).setAttribute("utilisateur", utilisateurDAO.getUtilisateur(request.getParameter("email")));
            getServletContext().getRequestDispatcher("/WEB-INF/user_page.jsp").forward(request, response);
        } else {
            request.setAttribute("erreurMessage", "Mot de passe mal confirmé");
            request.setAttribute("email", email);
            request.setAttribute("nom", nom);
            request.setAttribute("prenom", prenom);
            request.setAttribute("date", date);
            request.setAttribute("adresse", adresse);
            actionInscription(request, response, utilisateurDAO);
        }
    }

    private void actionAjoutTache(HttpServletRequest request, HttpServletResponse response, UtilisateurDAO utilisateurDAO) throws DAOException, ServletException, IOException {
        getServletContext().getRequestDispatcher("/WEB-INF/ajouter.jsp").forward(request, response);
    }
    
    private void actionConsulterProfil(HttpServletRequest request, HttpServletResponse response, UtilisateurDAO utilisateurDAO) throws ServletException, IOException {
        Utilisateurs user = (Utilisateurs) request.getSession().getAttribute("utilisateur");
        request.setAttribute("nom", user.getNom());
        request.setAttribute("prenom", user.getPrenom());
        request.setAttribute("adresse", user.getAdresse());
        request.setAttribute("date", user.getDate());
        request.setAttribute("email", user.getEmail());
        request.setAttribute("genre", user.getGenre());
        getServletContext().getRequestDispatcher("/WEB-INF/profil.jsp").forward(request, response);
    }
    
    private void actionValidationUpdateProfil(HttpServletRequest request, HttpServletResponse response, UtilisateurDAO utilisateurDAO) throws DAOException, ServletException, IOException {
        String email = request.getParameter("email");
        String mdp = request.getParameter("mdp");
        String mdpConfirm = request.getParameter("mdpconfirm");
        String nom = request.getParameter("nom");
        String prenom = request.getParameter("prenom");
        String date = request.getParameter("date");
        String adresse = request.getParameter("adresse");
        if (mdp.equals(mdpConfirm)) {
            if (((Utilisateurs)request.getSession(false).getAttribute("utilisateur")).getEmail().equals(email)) {
            //utilisateurDAO.mettreAJourUtilisateur(email, mdp, nom, prenom, genre, date, adresse);
            request.getSession(true).setAttribute("utilisateur", utilisateurDAO.getUtilisateur(request.getParameter("email")));
            getServletContext().getRequestDispatcher("/WEB-INF/user_page.jsp").forward(request, response);
            }/* else {
                utilisateurDAO.remplacerUtilisateur(email, mdp, nom, prenom, genre)
            }*/
        } else {
            request.setAttribute("erreurMessage", "Mot de passe mal confirmé");
            request.setAttribute("email", email);
            request.setAttribute("nom", nom);
            request.setAttribute("prenom", prenom);
            request.setAttribute("date", date);
            request.setAttribute("adresse", adresse);
            actionConsulterProfil(request, response, utilisateurDAO);
        }
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
