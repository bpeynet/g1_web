package controleur;

import dao.CompetenceDAO;
import dao.DAOException;
import dao.EvaluationDAO;
import dao.TacheAtomDAO;
import dao.TacheDAO;
import dao.UtilisateurDAO;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import modeles.Tache;
import modeles.TacheAtom;
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
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action==null || !action.equals("Facture")) {
            PrintWriter out = response.getWriter();
        }
        UtilisateurDAO utilisateurDAO = new UtilisateurDAO(ds);
        CompetenceDAO competenceDAO = new CompetenceDAO(ds);
        TacheDAO tacheDAO = new TacheDAO(ds);
        TacheAtomDAO tacheAtomDAO = new TacheAtomDAO(ds);
        EvaluationDAO evaluationDAO = new EvaluationDAO(ds);
        
        try {
            if (action == null) {
                if (request.getSession(false) == null || request.getSession(false).getAttribute("utilisateur") == null) {
                    actionLogin(request, response, utilisateurDAO);
                } else {
                    allerPageAccueilConnecté(request, response, utilisateurDAO, tacheDAO, tacheAtomDAO);
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
                        actionVoirMesTaches(request, response, utilisateurDAO);
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
                        actionValidationAjoutTache(request, response, utilisateurDAO,
                                tacheDAO, tacheAtomDAO, competenceDAO);
                        request.setAttribute("succesMessage", "Tâche créée");
                        allerPageAccueilConnecté(request, response, utilisateurDAO, tacheDAO, tacheAtomDAO);
                    } else {
                        response.sendRedirect("./controleur");
                    }
                    break;
                }
                case "voirTache" : {
                    if (request.getSession(false).getAttribute("utilisateur") != null) {
                        actionConsulterTache(request, response, utilisateurDAO, tacheDAO, tacheAtomDAO, competenceDAO);
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
                case "ModifProfil": {
                    if (request.getSession(false).getAttribute("utilisateur") != null) {
                        actionModifierProfil(request, response, utilisateurDAO, competenceDAO);
                    } else {
                        response.sendRedirect("./controleur");
                    }
                    break;
                }
                case "Postuler": {
                    if(request.getSession(false).getAttribute("utilisateur") != null) {
                        actionPostuler(request, response, tacheAtomDAO, tacheDAO, utilisateurDAO, competenceDAO);
                    } else {
                        response.sendRedirect("./controleur");
                    }
                    break;
                }
                case "Depostuler": {
                    if(request.getSession(false).getAttribute("utilisateur") != null) {
                        actionDepostuler(request,response, tacheAtomDAO, tacheDAO, utilisateurDAO, competenceDAO);
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
                    break;
                }
                case "SupprimerTacheAtom": {
                    if (request.getSession(false).getAttribute("utilisateur") != null) {
                        actionSupprimerTacheAtom(request, response, tacheAtomDAO, utilisateurDAO, tacheDAO);
                    } else {
                        response.sendRedirect("./controleur");
                    }
                    break;
                }
                case "SupprimerCompte": {
                    actionSupprimerCompte(request, response, utilisateurDAO);
                    break;
                }
                case "FinDeTache": {
                    actionFinDeTache(request, response, tacheAtomDAO, utilisateurDAO);
                    break;
                }
                case "MesCandidatures": {
                    actionVoirMesCandidatures(request, response, utilisateurDAO, tacheAtomDAO);
                    break;
                }
                case "voirCandidaturesTache": {
                    actionVoirCandidaturesTache(request,response, utilisateurDAO, tacheAtomDAO);
                    break;
                }
                case "AccepterCandidature": {
                    actionAccepterCandidature(request, response, tacheAtomDAO, utilisateurDAO);
                    break;
                }
                case "Evaluer" : {
                    actionEvaluer(request, response, evaluationDAO, utilisateurDAO);
                    allerPageAccueilConnecté(request, response, utilisateurDAO, tacheDAO, tacheAtomDAO);
                    break;
                }
                case "Facture": {
                    actionGenerationFacture(request, response, utilisateurDAO, tacheAtomDAO);
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

    private void actionLogin(HttpServletRequest request, HttpServletResponse response, UtilisateurDAO utilisateurDAO) throws DAOException, ServletException, IOException {
        getServletContext().getRequestDispatcher("/WEB-INF/login_accueil.jsp").forward(request, response);
    }

    private void actionInscription(HttpServletRequest request, HttpServletResponse response, UtilisateurDAO utilisateurDAO, CompetenceDAO competenceDAO) throws DAOException, ServletException, IOException {
        request.setAttribute("competences",competenceDAO.getListCompetences());
        getServletContext().getRequestDispatcher("/WEB-INF/inscription.jsp").forward(request, response);
    }

    private void actionConnexion(HttpServletRequest request, HttpServletResponse response, UtilisateurDAO utilisateurDAO, TacheDAO tacheDAO, TacheAtomDAO tacheAtomDAO) throws DAOException, ServletException, IOException {
        HttpSession session = request.getSession(true);
        Utilisateurs usr = utilisateurDAO.getUtilisateur(new String(request.getParameter("email").getBytes("iso-8859-1"), "UTF-8"));
        if (usr == null) {
            request.setAttribute("erreur","Identifiant inconnu");
            actionLogin(request, response, utilisateurDAO);
            return;
        }
        if (usr.getMdp().equals(new String(request.getParameter("mdp").getBytes("iso-8859-1"), "UTF-8"))) {
            session.setAttribute("utilisateur", usr);
            allerPageAccueilConnecté(request, response, utilisateurDAO, tacheDAO, tacheAtomDAO);
        }
        else {
            request.setAttribute("erreur","Mot de passe invalide");
            actionLogin(request, response, utilisateurDAO);
        }
    }

    private void actionValidationInscription(HttpServletRequest request, HttpServletResponse response, UtilisateurDAO utilisateurDAO, CompetenceDAO competenceDAO, TacheDAO tacheDAO, TacheAtomDAO tacheAtomDAO) throws DAOException, ServletException, IOException {
        if (request.getParameter("email") != null
                && request.getParameter("mdp") != null
                && request.getParameter("mdpconfirm") != null
                && request.getParameter("nom") != null
                && request.getParameter("prenom") != null
                && request.getParameter("adresse") != null
                && request.getParameter("genre") != null
                && request.getParameter("date") != null) {
            String email = new String(request.getParameter("email").getBytes("iso-8859-1"), "UTF-8");
            String mdp = new String(request.getParameter("mdp").getBytes("iso-8859-1"), "UTF-8");
            String mdpConfirm = new String(request.getParameter("mdpconfirm").getBytes("iso-8859-1"), "UTF-8");
            String nom = new String(request.getParameter("nom").getBytes("iso-8859-1"), "UTF-8");
            String prenom = new String(request.getParameter("prenom").getBytes("iso-8859-1"), "UTF-8");
            String adresse = new String(request.getParameter("adresse").getBytes("iso-8859-1"), "UTF-8");
            int genre = Integer.valueOf(request.getParameter("genre"));
            String date = new String(request.getParameter("date").getBytes("iso-8859-1"), "UTF-8");
            
            Utilisateurs usr = utilisateurDAO.getUtilisateur(email);
            if(usr !=  null) {
                request.setAttribute("erreur","Email déjà utilisé");
                request.setAttribute("nom", nom);
                request.setAttribute("prenom", prenom);
                request.setAttribute("date", date);
                request.setAttribute("adresse", adresse);
                request.setAttribute("genre", genre);
                actionInscription(request, response, utilisateurDAO, competenceDAO);
                return;
            }
            if (date.matches("../../....") || date.matches("..-..-....")){
                date=date.substring(6, 10) + "/" + date.substring(3, 5) + "/" + date.substring(0,2);
            } else if (!date.matches("..../../..") && !date.matches("....-..-..")) {
                request.setAttribute("erreurMessage", "Rentrer une VRAIE date.");
                request.setAttribute("nom", nom);
                request.setAttribute("prenom", prenom);
                request.setAttribute("adresse", adresse);
                request.setAttribute("genre", genre);
                request.setAttribute("email", email);
                actionInscription(request, response, utilisateurDAO, competenceDAO);
            }
            if (mdp.equals(mdpConfirm)) {
                try {
                    utilisateurDAO.ajouterUtilisateur(email, mdp, nom, prenom, genre, date, adresse);
                    for(Competences c : competenceDAO.getListCompetences()) {
                        if(request.getParameter(c.getNomCompetence()) != null) {
                            utilisateurDAO.ajouterCompetences(email, c.getNomCompetence());
                        }
                    }
                    request.getSession(true).setAttribute("utilisateur", utilisateurDAO.getUtilisateur(request.getParameter("email")));
                    allerPageAccueilConnecté(request, response, utilisateurDAO, tacheDAO, tacheAtomDAO);
                } catch (DAOException e) {
                    request.setAttribute("erreurMessage", e);
                    request.setAttribute("nom", nom);
                    request.setAttribute("prenom", prenom);
                    request.setAttribute("date", date);
                    request.setAttribute("adresse", adresse);
                    request.setAttribute("genre", genre);
                    request.setAttribute("email", email);
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
        } else {
            actionInscription(request, response, utilisateurDAO, competenceDAO);
        }
    }

    private void actionAjoutTache(HttpServletRequest request, HttpServletResponse response, UtilisateurDAO utilisateurDAO, CompetenceDAO competenceDAO) throws DAOException, ServletException, IOException {
        request.setAttribute("competences",competenceDAO.getListCompetences());
        getServletContext().getRequestDispatcher("/WEB-INF/ajouter.jsp").forward(request, response);
    }
    
    private void actionModifierProfil(HttpServletRequest request, HttpServletResponse response, UtilisateurDAO utilisateurDAO, CompetenceDAO competenceDAO) throws ServletException, IOException, DAOException {
        Utilisateurs user = (Utilisateurs) request.getSession().getAttribute("utilisateur");
        request.setAttribute("nom", user.getNom());
        request.setAttribute("prenom", user.getPrenom());
        request.setAttribute("adresse", user.getAdresse());
        request.setAttribute("date", user.getDate());
        request.setAttribute("email", user.getEmail());
        request.setAttribute("genre", user.getGenre());
        request.setAttribute("competences",utilisateurDAO.getUncheckedCompetences(user.getEmail()));
        request.setAttribute("usrCompetences",utilisateurDAO.getCompetences(user.getEmail()));
        getServletContext().getRequestDispatcher("/WEB-INF/modifProfil.jsp").forward(request, response);
    }
    
    private void actionValidationUpdateProfil(HttpServletRequest request, HttpServletResponse response, UtilisateurDAO utilisateurDAO, CompetenceDAO competenceDAO, TacheDAO tacheDAO, TacheAtomDAO tacheAtomDAO) throws DAOException, ServletException, IOException {
        if (request.getParameter("email") != null
                && request.getParameter("mdp") != null
                && request.getParameter("mdpconfirm") != null
                && request.getParameter("nom") != null
                && request.getParameter("prenom") != null
                && request.getParameter("adresse") != null
                && request.getParameter("genre") != null
                && request.getParameter("date") != null
                && request.getParameter("rayon") != null) {
            String email = new String(request.getParameter("email").getBytes("iso-8859-1"), "UTF-8");
            String mdp = new String(request.getParameter("mdp").getBytes("iso-8859-1"), "UTF-8");
            String mdpConfirm = new String(request.getParameter("mdpconfirm").getBytes("iso-8859-1"), "UTF-8");
            String nom = new String(request.getParameter("nom").getBytes("iso-8859-1"), "UTF-8");
            String prenom = new String(request.getParameter("prenom").getBytes("iso-8859-1"), "UTF-8");
            String date = new String(request.getParameter("date").getBytes("iso-8859-1"), "UTF-8");
            String adresse = new String(request.getParameter("adresse").getBytes("iso-8859-1"), "UTF-8");
            try {
                int rayon = Integer.valueOf(request.getParameter("rayon"));
                int genre = Integer.valueOf(request.getParameter("genre"));
                if (mdp.equals(mdpConfirm)) {
                    if (((Utilisateurs)request.getSession(false).getAttribute("utilisateur")).getEmail().equals(email)) {
                        utilisateurDAO.mettreAJourUtilisateur(email, mdp, nom, prenom, genre, date, adresse, rayon);
                        request.getSession(true).setAttribute("utilisateur", utilisateurDAO.getUtilisateur(email));
                        allerPageAccueilConnecté(request, response, utilisateurDAO, tacheDAO, tacheAtomDAO);
                    }
                } else {
                    request.setAttribute("erreurMessage", "Mot de passe mal confirmé");
                    request.setAttribute("email", email);
                    request.setAttribute("nom", nom);
                    request.setAttribute("prenom", prenom);
                    request.setAttribute("date", date);
                    request.setAttribute("adresse", adresse);
                    request.setAttribute("genre", genre);
                    actionConsulterProfil(request, response, utilisateurDAO, competenceDAO);
                }
            } catch (NumberFormatException e) {
                request.setAttribute("erreurMessage", "Entiers invalides");
                request.setAttribute("email", email);
                request.setAttribute("nom", nom);
                request.setAttribute("prenom", prenom);
                request.setAttribute("date", date);
                request.setAttribute("adresse", adresse);
                actionConsulterProfil(request, response, utilisateurDAO, competenceDAO);
            }
        }
    }

    /**
     * Action pour afficher les tâches que je propose, les tâches que j'exécute
     * et les tâches auxquelles je pourrais postuler
     * @param request
     * @param response
     * @param utilisateurDAO DAO du modèle Utilisateurs
     * @throws ServletException
     * @throws IOException
     * @throws DAOException
     */
    private void actionVoirMesTaches(HttpServletRequest request, HttpServletResponse response, UtilisateurDAO utilisateurDAO) throws ServletException, IOException, DAOException {
        Utilisateurs utilisateur = (Utilisateurs) request.getSession(false).getAttribute("utilisateur");
        request.setAttribute("taches", utilisateurDAO.getToutesTachesCommanditaire(utilisateur.getEmail()));
        request.setAttribute("candidatures", utilisateurDAO.getNbCandidaturesCommanditaire(utilisateur));
        getServletContext().getRequestDispatcher("/WEB-INF/panneauTaches.jsp").forward(request, response);
    }

    private void actionValidationAjoutTache(HttpServletRequest request, HttpServletResponse response, 
            UtilisateurDAO utilisateurDAO, TacheDAO tacheDAO, TacheAtomDAO tacheAtomDAO,
            CompetenceDAO competenceDAO) 
            throws DAOException, ServletException, IOException {
        
        String typeTache = request.getParameter("typeTache");
        Utilisateurs ut = (Utilisateurs) request.getSession(false).getAttribute("utilisateur");
        String email = ut.getEmail();
        
        String titre, description;
        double prix;
        String datetot, datetard; 
        int idMere;
        ArrayList<Competences> listCompetences;
        
        if(typeTache.equals("TUnique")) {
            tacheDAO.ajouterTache(new String(request.getParameter("titre1").getBytes("iso-8859-1"), "UTF-8"), email);
        } else {
            tacheDAO.ajouterTache(new String(request.getParameter("projetName").getBytes("iso-8859-1"), "UTF-8"), email);
        }        
        int k = 1;
        while(request.getParameter("titre"+k) != null){
            titre = new String(request.getParameter("titre"+k).getBytes("iso-8859-1"), "UTF-8");
            description = new String(request.getParameter("description"+k).getBytes("iso-8859-1"), "UTF-8");
            prix = Double.valueOf(new String(request.getParameter("prix"+k).getBytes("iso-8859-1"), "UTF-8"));
            datetot = new String(request.getParameter("SoonestDate"+k).getBytes("iso-8859-1"), "UTF-8");
            datetard = new String(request.getParameter("LatestDate"+k).getBytes("iso-8859-1"), "UTF-8");
            idMere = utilisateurDAO.getIdLastTache(email);
            listCompetences = competenceDAO.whichCompetences(request,k);
            tacheAtomDAO.ajouterTacheAtom(titre, description, prix,
                    datetot,datetard, email, idMere, listCompetences, utilisateurDAO);
            k++;
        }
        if(typeTache.equals("TUnique")) {
            request.setAttribute("succesMessage", "Tâche créée");
        } else {
            request.setAttribute("succesMessage", "Projet créé");
        }
        allerPageAccueilConnecté(request, response, utilisateurDAO, tacheDAO, tacheAtomDAO);
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
            processRequest(request, response);
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
            processRequest(request, response);
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

    private void allerPageAccueilConnecté(HttpServletRequest request, HttpServletResponse response, UtilisateurDAO utilisateurDAO, TacheDAO tacheDAO, TacheAtomDAO tacheAtomDAO) throws ServletException, IOException, DAOException {
        Utilisateurs utilisateur = (Utilisateurs)request.getSession(false).getAttribute("utilisateur");
        request.setAttribute("tachesCommanditaire", utilisateurDAO.getTachesCommanditaire(utilisateur.getEmail()));
        request.setAttribute("tachesEnCours", utilisateurDAO.getTachesEnCours(utilisateur.getEmail()));
        //request.setAttribute("taches", tacheDAO.getTaches((Utilisateurs)request.getSession(false).getAttribute("utilisateur"),tacheAtomDAO));
        request.setAttribute("tachesExecutant", utilisateurDAO.getTachesPotentielles((Utilisateurs)request.getSession(false).getAttribute("utilisateur")));
        getServletContext().getRequestDispatcher("/WEB-INF/user_page.jsp").forward(request, response);
    }

    private void actionConsulterTache(HttpServletRequest request, HttpServletResponse response, UtilisateurDAO utilisateurDAO, TacheDAO tacheDAO, TacheAtomDAO tacheAtomDAO, CompetenceDAO competenceDAO) throws ServletException, IOException, DAOException {
        String numeroDeTache = request.getParameter("idTache");
        if (numeroDeTache != null) {
            try {
                Tache tache = tacheDAO.getTache(Integer.valueOf(request.getParameter("idTache")), tacheAtomDAO, competenceDAO);
                if (tache != null) {
                    Utilisateurs utilisateur = (Utilisateurs) request.getSession().getAttribute("utilisateur");
                    request.setAttribute("tache", tache);
                    if (tache.getEmailCommanditaire().equals(utilisateur.getEmail())) {
                        request.setAttribute("candidatures", utilisateurDAO.getNbCandidaturesCommanditaire(utilisateur));
                        //request.setAttribute("candidaturesDetails", utilisateurDAO.getCandidaturesCommanditaire(utilisateur, Integer.valueOf(request.getParameter("idTache"))));
                    } else {
                        request.setAttribute("candidatures", utilisateurDAO.getCandidaturesExecutant(utilisateur));
                    }
                    getServletContext().getRequestDispatcher("/WEB-INF/ficheTache.jsp").forward(request, response);
                } else {
                    response.sendRedirect("./controleur");
                }
            } catch (NumberFormatException nEx) {
                response.sendRedirect("./controleur");
            }
        } else {
            response.sendRedirect("./controleur");
        }
    }

    private void actionPostuler(HttpServletRequest request, HttpServletResponse response, TacheAtomDAO tacheAtomDAO, TacheDAO tacheDAO, UtilisateurDAO utilisateurDAO, CompetenceDAO competenceDAO) throws DAOException, ServletException, IOException {
        Utilisateurs utilisateur = (Utilisateurs) request.getSession(false).getAttribute("utilisateur");
        String idTacheAtom = request.getParameter("idTacheAtom");
        try {
            if (utilisateur!= null && !utilisateurDAO.proposedThisAtomTask(Integer.valueOf(idTacheAtom), utilisateur)) {
                int idTacheRetour = tacheAtomDAO.postuler(((Utilisateurs) request.getSession().getAttribute("utilisateur")), Integer.valueOf(idTacheAtom));
                request.setAttribute("tache", tacheDAO.getTache(idTacheRetour, tacheAtomDAO, competenceDAO));
                request.setAttribute("candidatures", utilisateurDAO.getCandidaturesExecutant((Utilisateurs) request.getSession().getAttribute("utilisateur")));
                getServletContext().getRequestDispatcher("/WEB-INF/ficheTache.jsp").forward(request, response);
            } else {
                response.sendRedirect("./controleur");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("./controleur");
        }
    }

    private void actionDepostuler(HttpServletRequest request, HttpServletResponse response, TacheAtomDAO tacheAtomDAO, TacheDAO tacheDAO, UtilisateurDAO utilisateurDAO, CompetenceDAO competenceDAO) throws ServletException, IOException, DAOException {
        if (request.getSession(false).getAttribute("utilisateur")!=null) {
            int idTacheRetour = tacheAtomDAO.depostuler(((Utilisateurs) request.getSession().getAttribute("utilisateur")), Integer.valueOf(request.getParameter("idTacheAtom")));
            request.setAttribute("tache", tacheDAO.getTache(idTacheRetour, tacheAtomDAO, competenceDAO));
            request.setAttribute("candidatures", utilisateurDAO.getCandidaturesExecutant((Utilisateurs) request.getSession().getAttribute("utilisateur")));
            getServletContext().getRequestDispatcher("/WEB-INF/ficheTache.jsp").forward(request, response);
        } else {
            response.sendRedirect("./controleur");
        }
    }

    private void actionSupprimerTache(HttpServletRequest request, HttpServletResponse response, TacheDAO tacheDAO, UtilisateurDAO utilisateurDAO) throws DAOException, IOException, ServletException {
        if (utilisateurDAO.proposedThisTask(Integer.valueOf(request.getParameter("idTache")), ((Utilisateurs) request.getSession(false).getAttribute("utilisateur")))){
            tacheDAO.supprimerTache(Integer.valueOf(request.getParameter("idTache")));
            actionVoirMesTaches(request, response, utilisateurDAO);
        } else {
            response.sendRedirect("./controleur");
        }
    }
    
    private void actionSupprimerCompte(HttpServletRequest request, HttpServletResponse response, UtilisateurDAO utilisateurDAO) throws IOException, DAOException, ServletException {
        if (request.getSession(false).getAttribute("utilisateur") != null && request.getParameter("email") != null) {
            Utilisateurs utilisateur = (Utilisateurs) request.getSession(false).getAttribute("utilisateur");
            if (utilisateur.getEmail().equals(request.getParameter("email"))) {
                utilisateurDAO.supprimerUtilisateur(utilisateur.getEmail());
                request.getSession(false).invalidate();
                request.setAttribute("message", "Compte supprimé");
            }
            response.sendRedirect("./controleur");
        } else {
            response.sendRedirect("./controleur");
        }
    }

    private void actionSupprimerTacheAtom(HttpServletRequest request, HttpServletResponse response, TacheAtomDAO tacheAtomDAO, UtilisateurDAO utilisateurDAO, TacheDAO tacheDAO) throws DAOException, IOException, ServletException {
        if (request.getParameter("idTacheAtom") != null && 
                utilisateurDAO.proposedThisAtomTask(Integer.valueOf(request.getParameter("idTacheAtom")), ((Utilisateurs) request.getSession(false).getAttribute("utilisateur")))){
            tacheAtomDAO.supprimerTacheAtom(Integer.valueOf(request.getParameter("idTacheAtom")));
            actionVoirMesTaches(request, response, utilisateurDAO);
        } else {
            response.sendRedirect("./controleur");
        }
    }

        
    private void actionVoirMesCandidatures(HttpServletRequest request, HttpServletResponse response, UtilisateurDAO utilisateurDAO, TacheAtomDAO tacheAtomDAO) throws DAOException, IOException, ServletException {
        ArrayList<TacheAtom> candidatures = new ArrayList<TacheAtom>();
        Utilisateurs utilisateur = (Utilisateurs)request.getSession(false).getAttribute("utilisateur");
        HashSet<Integer> list = utilisateurDAO.getCandidaturesExecutant(utilisateur);
        for(Integer i : list) {
            candidatures.add(tacheAtomDAO.getTacheAtom(i));
        }
        
        request.setAttribute("candidatures",candidatures);
        request.setAttribute("services",utilisateurDAO.getTachesExecutantFinies(utilisateur));
        getServletContext().getRequestDispatcher("/WEB-INF/panneauCandidatures.jsp").forward(request, response);
        
    }


    private void actionVoirCandidaturesTache(HttpServletRequest request, HttpServletResponse response, UtilisateurDAO utilisateurDAO, TacheAtomDAO tacheAtomDAO) throws IOException, DAOException, ServletException {
        if (request.getSession(false).getAttribute("utilisateur") != null && request.getParameter("idTacheAtom") != null) {
            Utilisateurs utilisateur = (Utilisateurs) request.getSession(false).getAttribute("utilisateur");
            int idTacheAtom = Integer.valueOf(request.getParameter("idTacheAtom"));
            if (utilisateurDAO.proposedThisAtomTask(idTacheAtom, utilisateur)) {
                request.setAttribute("tacheAtom", tacheAtomDAO.getTacheAtom(idTacheAtom));
                request.setAttribute("candidatures", utilisateurDAO.getCandidaturesCommanditaire(utilisateur, idTacheAtom));
                getServletContext().getRequestDispatcher("/WEB-INF/candidaturesTache.jsp").forward(request, response);
            } else {
                response.sendRedirect("./controleur");
            }
        } else {
            response.sendRedirect("./controleur");
        }

    }

    private void actionAccepterCandidature(HttpServletRequest request, HttpServletResponse response, TacheAtomDAO tacheAtomDAO, UtilisateurDAO utilisateurDAO) throws DAOException, IOException, ServletException {
        if (request.getSession(false).getAttribute("utilisateur") != null
                && request.getParameter("idTacheAtom") != null
                && request.getParameter("idCandidat") != null) {
            Utilisateurs utilisateur = (Utilisateurs) request.getSession(false).getAttribute("utilisateur");
            int idTacheAtom = Integer.valueOf(request.getParameter("idTacheAtom"));
            String idCandidat = request.getParameter("idCandidat");
            if (utilisateurDAO.proposedThisAtomTask(idTacheAtom, utilisateur)) {
                tacheAtomDAO.accepterCandidature(idCandidat, idTacheAtom);
                actionVoirMesTaches(request, response, utilisateurDAO);
            } else {
                response.sendRedirect("./controleur");
            }
        } else {
            response.sendRedirect("./controleur");
        }
    }

    /**
     * Lorsque le commanditaire déclare une tâche comme finie
     * Aller à la page pour évaluation
     * @param request
     * @param response
     * @param utilisateurDAO 
     */
    private void actionFinDeTache(HttpServletRequest request, HttpServletResponse response, TacheAtomDAO tacheAtomDAO, UtilisateurDAO utilisateurDAO) throws DAOException, ServletException, IOException {
        if (request.getSession(false).getAttribute("utilisateur") != null && request.getParameter("idTacheAtom") != null
                && request.getParameter("idCandidat") != null) {
            tacheAtomDAO.finir(Integer.valueOf(request.getParameter("idTacheAtom")));
            request.setAttribute("commanditaire", (Utilisateurs)request.getSession(false).getAttribute("utilisateur"));
            request.setAttribute("tache", tacheAtomDAO.getTacheAtom(Integer.valueOf(request.getParameter("idTacheAtom"))));
            request.setAttribute("executant",utilisateurDAO.getUtilisateur(request.getParameter("idCandidat")));
            getServletContext().getRequestDispatcher("/WEB-INF/evaluations.jsp").forward(request, response);
        }
    }
    
    private void actionGenerationFacture(HttpServletRequest request, HttpServletResponse response, UtilisateurDAO utilisateurDAO, TacheAtomDAO tacheAtomDAO) throws IOException, DAOException {
        String idTacheAtom = request.getParameter("idTacheAtom");
        Utilisateurs utilisateur = (Utilisateurs) request.getSession(false).getAttribute("utilisateur");
        if (idTacheAtom != null && utilisateur != null) {
            try {
                if ((utilisateurDAO.executesThisAtomTask(utilisateur.getEmail(), Integer.valueOf(idTacheAtom))
                        || utilisateurDAO.proposedThisAtomTask(Integer.valueOf(idTacheAtom), utilisateur))
                        && tacheAtomDAO.isOver(Integer.valueOf(idTacheAtom)))
                try {
                    utilisateurDAO.genereFacture(Integer.valueOf(idTacheAtom), response.getOutputStream());
                    response.setContentType("application/pdf");
                } catch (NumberFormatException e) {
                    response.sendRedirect("./controleur");
                }
            } catch (NumberFormatException e) {
                response.sendRedirect("./controleur");
            }
        } else {
            response.sendRedirect("./controleur");
        }
    }

    /**
     * Lors de la validation de l'évaluation
     * @param request
     * @param response
     * @param utilisateurDAO 
     */
    private void actionEvaluer(HttpServletRequest request, HttpServletResponse response, EvaluationDAO evaluationDAO, UtilisateurDAO utilisateurDAO) throws DAOException, ServletException, IOException {
        try {
            evaluationDAO.ajouterEvaluation(Integer.valueOf(request.getParameter("note")),
                    (Integer) request.getSession(false).getAttribute("idTacheAtom"),
                    request.getParameter("commentaire"));
            utilisateurDAO.miseAJourMoyenneUtilisateur((Integer) request.getSession(false).getAttribute("idTacheAtom"));
        } catch (NumberFormatException e) {
            response.sendRedirect("./controleur");
        }
    }

    private void actionConsulterProfil(HttpServletRequest request, HttpServletResponse response, UtilisateurDAO utilisateurDAO, CompetenceDAO competenceDAO) throws IOException, ServletException, DAOException {
        Utilisateurs utilisateur = (Utilisateurs) request.getSession(false).getAttribute("utilisateur");
        if (request.getParameter("utilisateurConsulte") == null || request.getParameter("utilisateurConsulte").equals(utilisateur.getEmail())) {
            request.setAttribute("utilisateurConsulte", utilisateur);
            getServletContext().getRequestDispatcher("/WEB-INF/profil.jsp").forward(request, response);
        } else {
            Utilisateurs utilisateurConsulte = utilisateurDAO.getUtilisateur(request.getParameter("utilisateurConsulte"));
            if (utilisateurConsulte==null) {
                response.sendRedirect("./controleur");
            } else {
                request.setAttribute("utilisateurConsulte", utilisateurConsulte);
                getServletContext().getRequestDispatcher("/WEB-INF/profil.jsp").forward(request, response);
            }
        }
    }
    
}