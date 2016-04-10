package controleur;

import dao.CompetenceDAO;
import dao.DAOException;
import dao.TacheAtomDAO;
import dao.TacheDAO;
import dao.UtilisateurDAO;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import modeles.Utilisateurs;
import modeles.outils.Competences;

/**
 *
 * @author hargec
 */
@WebServlet(name = "controleur", urlPatterns = {"/controleur"})
public class controleur extends HttpServlet {

    @Resource(name = "jdbc/crowdhelping")
    private DataSource ds;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException {
        PrintWriter out = response.getWriter();
        String action = request.getParameter("action");
        UtilisateurDAO utilisateurDAO = new UtilisateurDAO(ds);
        CompetenceDAO competenceDAO = new CompetenceDAO(ds);
        TacheDAO tacheDAO = new TacheDAO(ds);
        TacheAtomDAO tacheAtomDAO = new TacheAtomDAO(ds);
        
        try {
            if (action == null) {
                if (request.getSession(false).getAttribute("utilisateur") == null) {
                    actionLogin(request, response, utilisateurDAO);
                } else {
                    allerPageAccueilConnecté(request, response, tacheDAO, tacheAtomDAO);
                }
            } else if (action.equals("Deconnexion")) {
                request.getSession().invalidate();
                actionLogin(request, response, utilisateurDAO);
            } else switch (action) {
                case "Connexion": {
                    actionConnexion(request, response, utilisateurDAO, tacheDAO, tacheAtomDAO);
                    break;
                }
                case "Inscription": {
                    actionInscription(request, response, utilisateurDAO, competenceDAO);
                    break;
                }
                case "Validation": {
                    if (request.getSession(false).getAttribute("utilisateur") == null) 
                        actionValidationInscription(request, response, utilisateurDAO, competenceDAO, tacheDAO, tacheAtomDAO);
                    else actionValidationUpdateProfil(request, response, utilisateurDAO, competenceDAO, tacheDAO, tacheAtomDAO);
                    break;
                }
                case "MesTaches" : {
                    if (request.getSession(false).getAttribute("utilisateur") != null) {
                        actionVoirMesTaches(request, response, tacheDAO, utilisateurDAO);
                    } else {
                        response.sendRedirect("./controleur");
                    }
                    break;
                }
                case "AjoutTache": {
                    if (request.getSession(false).getAttribute("utilisateur") != null) {
                        actionAjoutTache(request, response, utilisateurDAO, competenceDAO);
                    } else {
                        response.sendRedirect("./controleur");
                    }
                    break;
                }
                case "ValidationAjoutTache": {
                    if (request.getSession(false).getAttribute("utilisateur") != null) {
                        actionValidationAjoutTache(request, response, utilisateurDAO, tacheDAO, tacheAtomDAO);
                        request.setAttribute("succesMessage", "Tâche créée");
                        allerPageAccueilConnecté(request, response, tacheDAO, tacheAtomDAO);
                    } else {
                        response.sendRedirect("./controleur");
                    }
                    break;
                }
                case "voirTache" : {
                    if (request.getSession(false).getAttribute("utilisateur") != null) {
                        actionConsulterTache(request, response, utilisateurDAO, tacheDAO, tacheAtomDAO);
                    } else {
                        response.sendRedirect("./controleur");
                    }
                    break;
                }
                case "Profil": {
                    if(request.getSession(false).getAttribute("utilisateur") != null) {
                        actionConsulterProfil(request, response, utilisateurDAO, competenceDAO);
                    } else {
                        response.sendRedirect("./controleur");
                    }
                    break;
                }
                case "Postuler": {
                    if(request.getSession(false).getAttribute("utilisateur") != null) {
                        actionPostuler(request, response, tacheAtomDAO, tacheDAO, utilisateurDAO);
                    } else {
                        response.sendRedirect("./controleur");
                    }
                    break;
                }
                case "Depostuler": {
                    if(request.getSession(false).getAttribute("utilisateur") != null) {
                        actionDepostuler(request,response, tacheAtomDAO, tacheDAO, utilisateurDAO);
                    } else {
                        response.sendRedirect("./controleur");
                    }
                    break;
                }
                case "SupprimerTache": {
                    if (request.getSession(false).getAttribute("utilisateur") != null) {
                        actionSupprimerTache(request, response, tacheDAO, utilisateurDAO);
                    } else {
                        response.sendRedirect("./controleur");
                    }
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

    public void actionInscription(HttpServletRequest request, HttpServletResponse response, UtilisateurDAO utilisateurDAO, CompetenceDAO competenceDAO) throws DAOException, ServletException, IOException {
        request.setAttribute("competences",competenceDAO.getListCompetences());
        getServletContext().getRequestDispatcher("/WEB-INF/inscription.jsp").forward(request, response);
    }

    public void actionConnexion(HttpServletRequest request, HttpServletResponse response, UtilisateurDAO utilisateurDAO, TacheDAO tacheDAO, TacheAtomDAO tacheAtomDAO) throws DAOException, ServletException, IOException {
        HttpSession session = request.getSession(true);
        Utilisateurs usr = utilisateurDAO.getUtilisateur(request.getParameter("email"));
        if(usr == null) {
            request.setAttribute("erreur","Identifiant inconnu");
            actionLogin(request, response, utilisateurDAO);
            return;
        }
        if( usr.getMdp().equals(request.getParameter("mdp"))) {
            session.setAttribute("utilisateur", usr);
            allerPageAccueilConnecté(request, response, tacheDAO, tacheAtomDAO);
        }
        else {
            request.setAttribute("erreur","Mot de passe invalide");
            actionLogin(request, response, utilisateurDAO);
        }
    }

    public void actionValidationInscription(HttpServletRequest request, HttpServletResponse response, UtilisateurDAO utilisateurDAO, CompetenceDAO competenceDAO, TacheDAO tacheDAO, TacheAtomDAO tacheAtomDAO) throws DAOException, ServletException, IOException {
        String email = request.getParameter("email");
        String mdp = request.getParameter("mdp");
        String mdpConfirm = request.getParameter("mdpconfirm");
        String nom = request.getParameter("nom");
        String prenom = request.getParameter("prenom");
        String date = request.getParameter("date");
        String adresse = request.getParameter("adresse");
        int genre = Integer.valueOf(request.getParameter("genre"));
        if (mdp.equals(mdpConfirm)) {
            try {
                utilisateurDAO.ajouterUtilisateur(email, mdp, nom, prenom, genre, date, adresse);
                for(Competences c : competenceDAO.getListCompetences()) {
                    if(request.getParameter(c.getNomCompetence()) != null) {
                        utilisateurDAO.ajouterCompetences(email, c.getNomCompetence());
                    }
                }
                request.getSession(true).setAttribute("utilisateur", utilisateurDAO.getUtilisateur(request.getParameter("email")));
                allerPageAccueilConnecté(request, response, tacheDAO, tacheAtomDAO);
            } catch (DAOException e) {
                request.setAttribute("erreurMessage", e);
                request.setAttribute("nom", nom);
                request.setAttribute("prenom", prenom);
                request.setAttribute("date", date);
                request.setAttribute("adresse", adresse);
                request.setAttribute("genre", genre);
                actionInscription(request, response, utilisateurDAO, competenceDAO);
            }
        } else {
            request.setAttribute("erreurMessage", "Mot de passe mal confirmé");
            request.setAttribute("email", email);
            request.setAttribute("nom", nom);
            request.setAttribute("prenom", prenom);
            request.setAttribute("date", date);
            request.setAttribute("adresse", adresse);
            request.setAttribute("genre", genre);
            actionInscription(request, response, utilisateurDAO,competenceDAO);
        }
    }

    private void actionAjoutTache(HttpServletRequest request, HttpServletResponse response, UtilisateurDAO utilisateurDAO, CompetenceDAO competenceDAO) throws DAOException, ServletException, IOException {
        request.setAttribute("competences",competenceDAO.getListCompetences());
        getServletContext().getRequestDispatcher("/WEB-INF/ajouter.jsp").forward(request, response);
    }
    
    private void actionConsulterProfil(HttpServletRequest request, HttpServletResponse response, UtilisateurDAO utilisateurDAO, CompetenceDAO competenceDAO) throws ServletException, IOException, DAOException {
        Utilisateurs user = (Utilisateurs) request.getSession().getAttribute("utilisateur");
        request.setAttribute("nom", user.getNom());
        request.setAttribute("prenom", user.getPrenom());
        request.setAttribute("adresse", user.getAdresse());
        request.setAttribute("date", user.getDate());
        request.setAttribute("email", user.getEmail());
        request.setAttribute("genre", user.getGenre());
        request.setAttribute("competences",utilisateurDAO.getUncheckedCompetences(user.getEmail()));
        request.setAttribute("usrCompetences",utilisateurDAO.getCompetences(user.getEmail()));
        getServletContext().getRequestDispatcher("/WEB-INF/profil.jsp").forward(request, response);
    }
    
    private void actionValidationUpdateProfil(HttpServletRequest request, HttpServletResponse response, UtilisateurDAO utilisateurDAO, CompetenceDAO competenceDAO, TacheDAO tacheDAO, TacheAtomDAO tacheAtomDAO) throws DAOException, ServletException, IOException {
        String email = request.getParameter("email");
        String mdp = request.getParameter("mdp");
        String mdpConfirm = request.getParameter("mdpconfirm");
        String nom = request.getParameter("nom");
        String prenom = request.getParameter("prenom");
        String date = request.getParameter("date");
        String adresse = request.getParameter("adresse");
        int genre = Integer.valueOf(request.getParameter("genre"));
        if (mdp.equals(mdpConfirm)) {
            if (((Utilisateurs)request.getSession(false).getAttribute("utilisateur")).getEmail().equals(email)) {
                utilisateurDAO.mettreAJourUtilisateur(email, mdp, nom, prenom, genre, date, adresse);
                request.getSession(true).setAttribute("utilisateur", utilisateurDAO.getUtilisateur(email));
                allerPageAccueilConnecté(request, response, tacheDAO, tacheAtomDAO);
            }
        } else {
            request.setAttribute("erreurMessage", "Mot de passe mal confirmé");
            request.setAttribute("email", email);
            request.setAttribute("nom", nom);
            request.setAttribute("prenom", prenom);
            request.setAttribute("date", date);
            request.setAttribute("adresse", adresse);
            actionConsulterProfil(request, response, utilisateurDAO, competenceDAO);
        }
    }

    private void actionVoirMesTaches(HttpServletRequest request, HttpServletResponse response, TacheDAO tacheDAO, UtilisateurDAO utilisateurDAO) throws ServletException, IOException, DAOException {
        request.setAttribute("taches", utilisateurDAO.getTache(((Utilisateurs) request.getSession(false).getAttribute("utilisateur")).getEmail()));
        request.setAttribute("candidatures", utilisateurDAO.getCandidaturesCommanditaire((Utilisateurs) request.getSession(false).getAttribute("utilisateur")));
        getServletContext().getRequestDispatcher("/WEB-INF/panneauTaches.jsp").forward(request, response);
    }

    private void actionValidationAjoutTache(HttpServletRequest request, HttpServletResponse response, 
            UtilisateurDAO utilisateurDAO, TacheDAO tacheDAO, TacheAtomDAO tacheAtomDAO) 
            throws DAOException, ServletException, IOException {
        
        String typeTache = request.getParameter("typeTache");
        Utilisateurs ut = (Utilisateurs) request.getSession(false).getAttribute("utilisateur");
        String email = ut.getEmail();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        
        String titre, description;
        double prix;
        String datetot, datetard; 
        int idMere;
        
        if(typeTache.equals("TUnique")) {  
            tacheDAO.ajouterTache(request.getParameter("titre1"), email);
        }
        else {
            tacheDAO.ajouterTache(request.getParameter("projetName"), email);
        }        
        int k = 1;
        while(request.getParameter("titre"+k) != null){
            titre = request.getParameter("titre"+k);
            description = request.getParameter("description"+k);
            prix = Integer.parseInt(request.getParameter("prix"+k));
            datetot = request.getParameter("SoonestDate"+k);
            datetard = request.getParameter("LatestDate"+k);
            idMere = utilisateurDAO.getIdLastTache(email);
            tacheAtomDAO.ajouterTacheAtom(titre, description, prix, datetot, datetard, email, idMere);
            k++;
        }
        
        request.setAttribute("succesMessage", "Tâche créée");
        allerPageAccueilConnecté(request, response, tacheDAO, tacheAtomDAO);
    }
  
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
        try {
            processRequest(request, response);
        } catch (ParseException ex) {
            Logger.getLogger(controleur.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (ParseException ex) {
            Logger.getLogger(controleur.class.getName()).log(Level.SEVERE, null, ex);
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

    private void allerPageAccueilConnecté(HttpServletRequest request, HttpServletResponse response, TacheDAO tacheDAO, TacheAtomDAO tacheAtomDAO) throws ServletException, IOException, DAOException {
        request.setAttribute("taches", tacheDAO.getTaches((Utilisateurs)request.getSession(false).getAttribute("utilisateur"),tacheAtomDAO));
        getServletContext().getRequestDispatcher("/WEB-INF/user_page.jsp").forward(request, response);
    }

    private void actionConsulterTache(HttpServletRequest request, HttpServletResponse response, UtilisateurDAO utilisateurDAO, TacheDAO tacheDAO, TacheAtomDAO tacheAtomDAO) throws ServletException, IOException, DAOException {
        if (request.getParameter("idTache")!=null) {
            request.setAttribute("tache", tacheDAO.getTache(Integer.valueOf(request.getParameter("idTache")), tacheAtomDAO));
            request.setAttribute("candidatures", utilisateurDAO.getCandidaturesExecutant((Utilisateurs) request.getSession().getAttribute("utilisateur")));
            getServletContext().getRequestDispatcher("/WEB-INF/ficheTache.jsp").forward(request, response);
        } else {
            response.sendRedirect("./controleur");
        }
    }

    private void actionPostuler(HttpServletRequest request, HttpServletResponse response, TacheAtomDAO tacheAtomDAO, TacheDAO tacheDAO, UtilisateurDAO utilisateurDAO) throws DAOException, ServletException, IOException {
        if (request.getSession(false).getAttribute("utilisateur")!=null) {
            int idTacheRetour = tacheAtomDAO.postuler(((Utilisateurs) request.getSession().getAttribute("utilisateur")), Integer.valueOf(request.getParameter("idTacheAtom")));
            request.setAttribute("tache", tacheDAO.getTache(idTacheRetour, tacheAtomDAO));
            request.setAttribute("candidatures", utilisateurDAO.getCandidaturesExecutant((Utilisateurs) request.getSession().getAttribute("utilisateur")));
            getServletContext().getRequestDispatcher("/WEB-INF/ficheTache.jsp").forward(request, response);
        } else {
            response.sendRedirect("./controleur");
        }
    }

    private void actionDepostuler(HttpServletRequest request, HttpServletResponse response, TacheAtomDAO tacheAtomDAO, TacheDAO tacheDAO, UtilisateurDAO utilisateurDAO) throws ServletException, IOException, DAOException {
        if (request.getSession(false).getAttribute("utilisateur")!=null) {
            int idTacheRetour = tacheAtomDAO.depostuler(((Utilisateurs) request.getSession().getAttribute("utilisateur")), Integer.valueOf(request.getParameter("idTacheAtom")));
            request.setAttribute("tache", tacheDAO.getTache(idTacheRetour, tacheAtomDAO));
            request.setAttribute("candidatures", utilisateurDAO.getCandidaturesExecutant((Utilisateurs) request.getSession().getAttribute("utilisateur")));
            getServletContext().getRequestDispatcher("/WEB-INF/ficheTache.jsp").forward(request, response);
        } else {
            response.sendRedirect("./controleur");
        }
    }

    private void actionSupprimerTache(HttpServletRequest request, HttpServletResponse response, TacheDAO tacheDAO, UtilisateurDAO utilisateurDAO) throws DAOException, IOException, ServletException {
        if (utilisateurDAO.proposedThisTask(Integer.valueOf(request.getParameter("idTache")), ((Utilisateurs) request.getSession(false).getAttribute("utilisateur")))){
            tacheDAO.supprimerTache(Integer.valueOf(request.getParameter("idTache")));
            actionVoirMesTaches(request, response, tacheDAO, utilisateurDAO);
        } else {
            response.sendRedirect("./controleur");
        }
    }
}