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
import org.apache.commons.codec.digest.DigestUtils;

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
        if (action == null || !action.equals("Facture")) {
            PrintWriter out = response.getWriter();
        }
        UtilisateurDAO utilisateurDAO = new UtilisateurDAO(ds);
        CompetenceDAO competenceDAO = new CompetenceDAO(ds);
        TacheDAO tacheDAO = new TacheDAO(ds);
        TacheAtomDAO tacheAtomDAO = new TacheAtomDAO(ds);
        EvaluationDAO evaluationDAO = new EvaluationDAO(ds);

        try {
            Utilisateurs utilisateur = null;
            if (request.getSession(false) != null) {
                utilisateur = (Utilisateurs) request.getSession(false).getAttribute("utilisateur");
                if (utilisateur != null) {
                    utilisateur = utilisateurDAO.getUtilisateur(utilisateur.getEmail());
                    request.getSession(false).setAttribute("utilisateur", utilisateur);
                }
            }
            if (action == null) {
                if (request.getSession(false) == null || utilisateur == null) {
                    actionLogin(request, response, utilisateurDAO);
                } else {
                    allerPageAccueilConnecté(request, response, utilisateurDAO);
                }
            } else if (action.equals("Deconnexion")) {
                request.getSession().invalidate();
                actionLogin(request, response, utilisateurDAO);
            } else {
                switch (action) {
                    case "Connexion": {
                        actionConnexion(request, response, utilisateurDAO);
                        break;
                    }
                    case "Inscription": {
                        actionInscription(request, response, utilisateurDAO, competenceDAO);
                        break;
                    }
                    case "Validation": {
                        if (utilisateur == null) {
                            actionValidationInscription(request, response,
                                    utilisateurDAO, competenceDAO);
                        } else {
                            actionValidationUpdateProfil(request, response,
                                    utilisateurDAO, competenceDAO, evaluationDAO);
                        }
                        break;
                    }
                    case "MesTaches": {
                        if (utilisateur != null) {
                            actionVoirMesTaches(request, response, utilisateurDAO);
                        } else {
                            response.sendRedirect("./controleur");
                        }
                        break;
                    }
                    case "AjoutTache": {
                        if (utilisateur != null) {
                            actionAjoutTache(request, response, competenceDAO);
                        } else {
                            response.sendRedirect("./controleur");
                        }
                        break;
                    }
                    case "ValidationAjoutTache": {
                        if (utilisateur != null) {
                            actionValidationAjoutTache(request, response, utilisateurDAO,
                                    tacheDAO, tacheAtomDAO, competenceDAO);
                            request.setAttribute("succesMessage", "Tâche créée");
                            allerPageAccueilConnecté(request, response, utilisateurDAO);
                        } else {
                            response.sendRedirect("./controleur");
                        }
                        break;
                    }
                    case "voirTache": {
                        if (utilisateur != null) {
                            actionConsulterTache(request, response, utilisateurDAO, tacheDAO, tacheAtomDAO, competenceDAO);
                        } else {
                            response.sendRedirect("./controleur");
                        }
                        break;
                    }
                    case "Profil": {
                        if (utilisateur != null) {
                            actionConsulterProfil(request, response, utilisateurDAO, evaluationDAO);
                        } else {
                            response.sendRedirect("./controleur");
                        }
                        break;
                    }
                    case "ModifProfil": {
                        if (utilisateur != null) {
                            actionModifierProfil(request, response, utilisateurDAO);
                        } else {
                            response.sendRedirect("./controleur");
                        }
                        break;
                    }
                    case "Postuler": {
                        if (utilisateur != null) {
                            actionPostuler(request, response, tacheAtomDAO, tacheDAO, utilisateurDAO, competenceDAO);
                        } else {
                            response.sendRedirect("./controleur");
                        }
                        break;
                    }
                    case "Depostuler": {
                        if (utilisateur != null) {
                            actionDepostuler(request, response, tacheAtomDAO, tacheDAO, utilisateurDAO, competenceDAO);
                        } else {
                            response.sendRedirect("./controleur");
                        }
                        break;
                    }
                    case "SupprimerTache": {
                        if (utilisateur != null) {
                            actionSupprimerTache(request, response, tacheDAO, utilisateurDAO, tacheAtomDAO, competenceDAO);
                        } else {
                            response.sendRedirect("./controleur");
                        }
                        break;
                    }
                    case "SupprimerTacheAtom": {
                        if (utilisateur != null) {
                            actionSupprimerTacheAtom(request, response, tacheAtomDAO, utilisateurDAO);
                        } else {
                            response.sendRedirect("./controleur");
                        }
                        break;
                    }
                    case "SupprimerCompte": {
                        if (utilisateur != null) {
                            actionSupprimerCompte(request, response, utilisateurDAO);
                        } else {
                            response.sendRedirect("./controleur");
                        }
                        break;
                    }
                    case "FinDeTache": {
                        if (utilisateur != null) {
                            actionFinDeTache(request, response, tacheAtomDAO, utilisateurDAO);
                        } else {
                            response.sendRedirect("./controleur");
                        }
                        break;
                    }
                    case "MesCandidatures": {
                        if (utilisateur != null) {
                            actionVoirMesCandidatures(request, response, utilisateurDAO, tacheAtomDAO);
                        } else {
                            response.sendRedirect("./controleur");
                        }
                        break;
                    }
                    case "voirCandidaturesTache": {
                        if (utilisateur != null) {
                            actionVoirCandidaturesTache(request, response, utilisateurDAO, tacheAtomDAO);
                        } else {
                            response.sendRedirect("./controleur");
                        }
                        break;
                    }
                    case "AccepterCandidature": {
                        if (utilisateur != null) {
                            actionAccepterCandidature(request, response, tacheAtomDAO, utilisateurDAO);
                        } else {
                            response.sendRedirect("./controleur");
                        }
                        break;
                    }
                    case "Evaluer": {
                        if (utilisateur != null) {
                            actionEvaluer(request, response, evaluationDAO, utilisateurDAO);
                            allerPageAccueilConnecté(request, response, utilisateurDAO);
                        } else {
                            response.sendRedirect("./controleur");
                        }
                        break;
                    }
                    case "Facture": {
                        if (utilisateur != null) {
                            actionGenerationFacture(request, response, utilisateurDAO, tacheAtomDAO);
                        } else {
                            response.sendRedirect("./controleur");
                        }
                        break;
                    }
                    case "Rechercher": {
                        if (utilisateur != null) {
                            actionRecherche(request, response, utilisateurDAO);
                        } else {
                            response.sendRedirect("./controleur");
                        }
                    }
                    default: {
                        response.sendRedirect("./controleur");
                        break;
                    }
                }
            }
        } catch (DAOException e) {
            request.setAttribute("erreur", e);
            request.setAttribute("erreur_message", e.getMessage());
            getServletContext().getRequestDispatcher("/WEB-INF/bdErreur.jsp").forward(request, response);
        }
    }

    /**
     * Affiche la page permettant de se connecter au site.
     * @param request
     * @param response
     * @param utilisateurDAO
     * @throws DAOException
     * @throws ServletException
     * @throws IOException 
     */
    private void actionLogin(HttpServletRequest request,
            HttpServletResponse response,
            UtilisateurDAO utilisateurDAO)
            throws DAOException, ServletException, IOException {
        getServletContext().getRequestDispatcher("/WEB-INF/login_accueil.jsp").forward(request, response);
    }

    /**
     * Affiche la page permettant de s'inscrire.
     * @param request
     * @param response
     * @param utilisateurDAO
     * @param competenceDAO
     * @throws DAOException
     * @throws ServletException
     * @throws IOException 
     */
    private void actionInscription(HttpServletRequest request,
            HttpServletResponse response,
            UtilisateurDAO utilisateurDAO,
            CompetenceDAO competenceDAO)
            throws DAOException, ServletException, IOException {
        request.setAttribute("competences", competenceDAO.getListCompetences());
        getServletContext().getRequestDispatcher("/WEB-INF/inscription.jsp").forward(request, response);
    }

    /**
     * Authentifie un utilisateur et ouvre un session si la demande est valide
     * @param request the value of request
     * @param response the value of response
     * @param utilisateurDAO the value of utilisateurDAO
     * @throws DAOException
     * @throws ServletException
     * @throws IOException
     */
    private void actionConnexion(HttpServletRequest request, HttpServletResponse response, UtilisateurDAO utilisateurDAO)
            throws DAOException, ServletException, IOException {
        HttpSession session = request.getSession(true);
        Utilisateurs usr = utilisateurDAO.getUtilisateur(new String(request.getParameter("email").getBytes("iso-8859-1"), "UTF-8"));
        if (usr == null) {
            request.setAttribute("erreur", "Identifiant inconnu");
            actionLogin(request, response, utilisateurDAO);
            return;
        }
        String hashMDP = DigestUtils.md5Hex(new String(request.getParameter("mdp").getBytes("iso-8859-1"), "UTF-8"));
        if (usr.getMdp().equals(hashMDP)) {
            session.setAttribute("utilisateur", usr);
            allerPageAccueilConnecté(request, response, utilisateurDAO);
        } else {
            request.setAttribute("erreur", "Mot de passe invalide");
            actionLogin(request, response, utilisateurDAO);
        }
    }

    /**
     * Inscrit l'utilisateur dans la base de données à partir des informations
     * contenues dans la request
     * @param request the value of request
     * @param response the value of response
     * @param utilisateurDAO the value of utilisateurDAO
     * @param competenceDAO the value of competenceDAO
     * @throws DAOException
     * @throws ServletException
     * @throws IOException
     */
    private void actionValidationInscription(HttpServletRequest request, HttpServletResponse response, UtilisateurDAO utilisateurDAO, CompetenceDAO competenceDAO)
            throws DAOException, ServletException, IOException {
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
            String adresse = new String(request.getParameter("adresse").getBytes("iso-8859-1"), "UTF-8");
            String date = new String(request.getParameter("date").getBytes("iso-8859-1"), "UTF-8");
            try {
                int genre = Integer.valueOf(request.getParameter("genre"));
                int rayon = Integer.valueOf(request.getParameter("rayon"));

                Utilisateurs usr = utilisateurDAO.getUtilisateur(email);
                if (usr != null) {
                    request.setAttribute("erreur", "Email déjà utilisé");
                    request.setAttribute("nom", nom);
                    request.setAttribute("prenom", prenom);
                    request.setAttribute("date", date);
                    request.setAttribute("adresse", adresse);
                    request.setAttribute("genre", genre);
                    request.setAttribute("rayon", rayon);
                    actionInscription(request, response, utilisateurDAO, competenceDAO);
                    return;
                }
                if (date.matches("../../....") || date.matches("..-..-....")) {
                    date = date.substring(6, 10) + "/" + date.substring(3, 5) + "/" + date.substring(0, 2);
                } else if (!date.matches("..../../..") && !date.matches("....-..-..")) {
                    request.setAttribute("erreurMessage", "Rentrer une VRAIE date.");
                    request.setAttribute("nom", nom);
                    request.setAttribute("prenom", prenom);
                    request.setAttribute("adresse", adresse);
                    request.setAttribute("genre", genre);
                    request.setAttribute("email", email);
                    request.setAttribute("rayon", rayon);
                    actionInscription(request, response, utilisateurDAO, competenceDAO);
                }
                if (mdp.equals(mdpConfirm)) {
                    try {
                        mdp = DigestUtils.md5Hex(mdp);
                        utilisateurDAO.ajouterUtilisateur(email, mdp, nom, prenom, genre, date, adresse, rayon);
                        for (Competences c : competenceDAO.getListCompetences()) {
                            if (request.getParameter(c.getNomCompetence()) != null) {
                                utilisateurDAO.ajouterCompetences(email, c.getNomCompetence());
                            }
                        }
                        request.getSession(true).setAttribute("utilisateur", utilisateurDAO.getUtilisateur(request.getParameter("email")));
                        allerPageAccueilConnecté(request, response, utilisateurDAO);
                    } catch (DAOException e) {
                        request.setAttribute("erreurMessage", "Email déjà utilisé");
                        request.setAttribute("nom", nom);
                        request.setAttribute("prenom", prenom);
                        request.setAttribute("date", date);
                        request.setAttribute("adresse", adresse);
                        request.setAttribute("genre", genre);
                        request.setAttribute("email", email);
                        request.setAttribute("rayon", rayon);
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
                    request.setAttribute("rayon", rayon);
                    actionInscription(request, response, utilisateurDAO, competenceDAO);
                }
            } catch (NumberFormatException e) {
                actionInscription(request, response, utilisateurDAO, competenceDAO);
            }
        } else {
            actionInscription(request, response, utilisateurDAO, competenceDAO);
        }
    }

    /**
     * Affiche la page permettant d'ajouter une tâche
     * @param request the value of request
     * @param response the value of response
     * @param competenceDAO the value of competenceDAO
     * @throws DAOException
     * @throws ServletException
     * @throws IOException
     */
    private void actionAjoutTache(HttpServletRequest request, HttpServletResponse response, CompetenceDAO competenceDAO)
            throws DAOException, ServletException, IOException {
        request.setAttribute("competences", competenceDAO.getListCompetences());
        getServletContext().getRequestDispatcher("/WEB-INF/ajouter.jsp").forward(request, response);
    }

    /**
     * Affiche la page de modification de profil en remplissant les champs
     * @param request the value of request
     * @param response the value of response
     * @param utilisateurDAO the value of utilisateurDAO
     * @throws ServletException
     * @throws IOException
     * @throws DAOException
     */
    private void actionModifierProfil(HttpServletRequest request, HttpServletResponse response, UtilisateurDAO utilisateurDAO)
            throws ServletException, IOException, DAOException {
        Utilisateurs user = (Utilisateurs) request.getSession().getAttribute("utilisateur");
        request.setAttribute("nom", user.getNom());
        request.setAttribute("prenom", user.getPrenom());
        request.setAttribute("adresse", user.getAdresse());
        request.setAttribute("date", user.getDate());
        request.setAttribute("email", user.getEmail());
        request.setAttribute("genre", user.getGenre());
        request.setAttribute("rayon", user.getRayon());
        request.setAttribute("competences", utilisateurDAO.getUncheckedCompetences(user.getEmail()));
        request.setAttribute("usrCompetences", utilisateurDAO.getCompetences(user.getEmail()));
        getServletContext().getRequestDispatcher("/WEB-INF/modifProfil.jsp").forward(request, response);
    }

    /**
     * Met à jour les détails d'un utilisateur avec les informations contenues
     * dans la request
     * @param request the value of request
     * @param response the value of response
     * @param utilisateurDAO the value of utilisateurDAO
     * @param competenceDAO the value of competenceDAO
     * @param evaluationDAO the value of evaluationDAO
     * @throws DAOException
     * @throws ServletException
     * @throws IOException
     */
    private void actionValidationUpdateProfil(HttpServletRequest request, HttpServletResponse response, UtilisateurDAO utilisateurDAO, CompetenceDAO competenceDAO, EvaluationDAO evaluationDAO) throws DAOException, ServletException, IOException {
        if (request.getParameter("mdp") != null
                && request.getParameter("mdpconfirm") != null
                && request.getParameter("nom") != null
                && request.getParameter("prenom") != null
                && request.getParameter("adresse") != null
                && request.getParameter("genre") != null
                && request.getParameter("date") != null
                && request.getParameter("rayon") != null) {
            Utilisateurs utilisateur = (Utilisateurs) request.getSession(false).getAttribute("utilisateur");
            String email = utilisateur.getEmail();
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
                    mdp = DigestUtils.md5Hex(mdp);
                    utilisateurDAO.mettreAJourUtilisateur(email, mdp, nom, prenom, genre, date, adresse, rayon);
                    boolean cochee;
                    for (Competences c : competenceDAO.getListCompetences()) {
                        cochee = request.getParameter(c.getNomCompetence()) != null;
                        utilisateurDAO.mettreAJourCompetences(email, c.getNomCompetence(), cochee);
                    }
                    request.getSession(true).setAttribute("utilisateur", utilisateurDAO.getUtilisateur(email));
                    allerPageAccueilConnecté(request, response, utilisateurDAO);
                } else {
                    request.setAttribute("erreurMessage", "Mot de passe mal confirmé");
                    request.setAttribute("email", email);
                    request.setAttribute("nom", nom);
                    request.setAttribute("prenom", prenom);
                    request.setAttribute("date", date);
                    request.setAttribute("adresse", adresse);
                    request.setAttribute("genre", genre);
                    actionConsulterProfil(request, response, utilisateurDAO, evaluationDAO);
                }
            } catch (NumberFormatException e) {
                request.setAttribute("erreurMessage", "Entiers invalides");
                request.setAttribute("email", email);
                request.setAttribute("nom", nom);
                request.setAttribute("prenom", prenom);
                request.setAttribute("date", date);
                request.setAttribute("adresse", adresse);
                actionConsulterProfil(request, response, utilisateurDAO, evaluationDAO);
            }
        } else {
            actionModifierProfil(request, response, utilisateurDAO);
        }
    }

    /**
     * Action pour afficher les tâches que je propose
     * avec le nombre de candidatures éventuelles
     * @param request
     * @param response
     * @param utilisateurDAO DAO du modèle Utilisateurs
     * @throws ServletException
     * @throws IOException
     * @throws DAOException
     */
    private void actionVoirMesTaches(HttpServletRequest request,
            HttpServletResponse response,
            UtilisateurDAO utilisateurDAO)
            throws ServletException, IOException, DAOException {
        Utilisateurs utilisateur = (Utilisateurs) request.getSession(false).getAttribute("utilisateur");
        request.setAttribute("taches", utilisateurDAO.getToutesTachesCommanditaire(utilisateur.getEmail()));
        request.setAttribute("candidatures", utilisateurDAO.getNbCandidaturesCommanditaire(utilisateur));
        getServletContext().getRequestDispatcher("/WEB-INF/panneauTaches.jsp").forward(request, response);
    }

    /**
     * Ajoute une tâche dont toutes les informations sont contenues
     * dans la request
     * @param request
     * @param response
     * @param utilisateurDAO
     * @param tacheDAO
     * @param tacheAtomDAO
     * @param competenceDAO
     * @throws DAOException
     * @throws ServletException
     * @throws IOException 
     */
    private void actionValidationAjoutTache(HttpServletRequest request,
            HttpServletResponse response,
            UtilisateurDAO utilisateurDAO,
            TacheDAO tacheDAO,
            TacheAtomDAO tacheAtomDAO,
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

        if (typeTache.equals("TUnique")) {
            tacheDAO.ajouterTache(new String(request.getParameter("titre1").getBytes("iso-8859-1"), "UTF-8"), email);
        } else {
            tacheDAO.ajouterTache(new String(request.getParameter("projetName").getBytes("iso-8859-1"), "UTF-8"), email);
        }
        int k = 1;
        while (request.getParameter("titre" + k) != null) {
            titre = new String(request.getParameter("titre" + k).getBytes("iso-8859-1"), "UTF-8");
            description = new String(request.getParameter("description" + k).getBytes("iso-8859-1"), "UTF-8");
            prix = Double.valueOf(new String(request.getParameter("prix" + k).getBytes("iso-8859-1"), "UTF-8"));
            datetot = new String(request.getParameter("SoonestDate" + k).getBytes("iso-8859-1"), "UTF-8");
            datetard = new String(request.getParameter("LatestDate" + k).getBytes("iso-8859-1"), "UTF-8");
            if (datetot.matches("../../....") || datetot.matches("..-..-....")) {
                datetot = datetot.substring(6, 10) + "/" + datetot.substring(3, 5) + "/" + datetot.substring(0, 2);
            } else if (!datetot.matches("..../../..") && !datetot.matches("....-..-..")) {
                request.setAttribute("erreurMessage", "Rentrer une VRAIE date pour la date d'exécution au plus tôt.");
                actionAjoutTache(request, response, competenceDAO);
            }
            if (datetard.matches("../../....") || datetard.matches("..-..-....")) {
                datetard = datetard.substring(6, 10) + "/" + datetard.substring(3, 5) + "/" + datetard.substring(0, 2);
            } else if (!datetard.matches("..../../..") && !datetard.matches("....-..-..")) {
                request.setAttribute("erreurMessage", "Rentrer une VRAIE date pour la date d'exécution au plus tard.");
                actionAjoutTache(request, response, competenceDAO);
            }
            idMere = utilisateurDAO.getIdLastTache(email);
            listCompetences = competenceDAO.whichCompetences(request, k);
            tacheAtomDAO.ajouterTacheAtom(titre, description, prix,
                    datetot, datetard, email, idMere, listCompetences, utilisateurDAO);
            k++;
        }
        if (typeTache.equals("TUnique")) {
            request.setAttribute("succesMessage", "Tâche créée");
        } else {
            request.setAttribute("succesMessage", "Projet créé");
        }
        allerPageAccueilConnecté(request, response, utilisateurDAO);
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
    }

    /**
     * Dirige vers la page d'accueil de l'espace connecté
     * et prépare les tâches à afficher à l'utilisateur connecté:
     * - les tâches qu'il a proposées
     * - les tâches qu'il est en train d'exécuter
     * - les tâches auxquelles il pourrait candidater
     * @param request the value of request
     * @param response the value of response
     * @param utilisateurDAO the value of utilisateurDAO
     * @throws ServletException
     * @throws IOException
     * @throws DAOException
     */
    private void allerPageAccueilConnecté(HttpServletRequest request, HttpServletResponse response, UtilisateurDAO utilisateurDAO) throws ServletException, IOException, DAOException {
        Utilisateurs utilisateur = (Utilisateurs) request.getSession(false).getAttribute("utilisateur");
        request.setAttribute("tachesCommanditaire", utilisateurDAO.getTachesCommanditaire(utilisateur.getEmail()));
        request.setAttribute("tachesEnCours", utilisateurDAO.getTachesEnCours(utilisateur.getEmail()));
        request.setAttribute("tachesExecutant", utilisateurDAO.getTachesPotentielles(utilisateur));
        getServletContext().getRequestDispatcher("/WEB-INF/user_page.jsp").forward(request, response);
    }

    /**
     * Affiche une fiche de la tâche dont l'id idTache est contenu
     * dans la request
     * @param request
     * @param response
     * @param utilisateurDAO
     * @param tacheDAO
     * @param tacheAtomDAO
     * @param competenceDAO
     * @throws ServletException
     * @throws IOException
     * @throws DAOException 
     */
    private void actionConsulterTache(HttpServletRequest request,
            HttpServletResponse response,
            UtilisateurDAO utilisateurDAO,
            TacheDAO tacheDAO, TacheAtomDAO tacheAtomDAO,
            CompetenceDAO competenceDAO)
            throws ServletException, IOException, DAOException {
        String numeroDeTache = request.getParameter("idTache");
        if (numeroDeTache != null) {
            try {
                Tache tache = tacheDAO.getTache(Integer.valueOf(numeroDeTache), tacheAtomDAO, competenceDAO, utilisateurDAO);
                if (tache != null) {
                    Utilisateurs utilisateur = (Utilisateurs) request.getSession().getAttribute("utilisateur");
                    request.setAttribute("tache", tache);
                    if (tache.getEmailCommanditaire().equals(utilisateur.getEmail())) {
                        request.setAttribute("candidatures", utilisateurDAO.getNbCandidaturesCommanditaire(utilisateur));
                    } else {
                        request.setAttribute("candidatures", utilisateurDAO.getCandidaturesExecutant(utilisateur));
                    }
                    getServletContext().getRequestDispatcher("/WEB-INF/ficheTache.jsp").forward(request, response);
                } else {
                    request.setAttribute("succesMessage", "Cette tâche n'existe plus.");
                    allerPageAccueilConnecté(request, response, utilisateurDAO);
                }
            } catch (NumberFormatException nEx) {
                response.sendRedirect("./controleur");
            }
        } else {
            response.sendRedirect("./controleur");
        }
    }

    /**
     * Ajoute la candidature de l'utilisateur actuellement connecté
     * pour la taĉhe d'id idTacheAtom contenu dans la request
     * @param request
     * @param response
     * @param tacheAtomDAO
     * @param tacheDAO
     * @param utilisateurDAO
     * @param competenceDAO
     * @throws DAOException
     * @throws ServletException
     * @throws IOException 
     */
    private void actionPostuler(HttpServletRequest request,
            HttpServletResponse response,
            TacheAtomDAO tacheAtomDAO,
            TacheDAO tacheDAO,
            UtilisateurDAO utilisateurDAO,
            CompetenceDAO competenceDAO)
            throws DAOException, ServletException, IOException {
        Utilisateurs utilisateur = (Utilisateurs) request.getSession(false).getAttribute("utilisateur");
        String idTacheAtom = request.getParameter("idTacheAtom");
        try {
            if (utilisateur != null
                    && !utilisateurDAO.proposedThisAtomTask(Integer.valueOf(idTacheAtom), utilisateur)
                    && !tacheAtomDAO.getTacheAtom(Integer.valueOf(idTacheAtom), utilisateurDAO).estEntamee()) {
                int idTacheRetour = tacheAtomDAO.postuler(utilisateur, Integer.valueOf(idTacheAtom), utilisateurDAO);
                switch (idTacheRetour) {
                    case -1: {
                        request.setAttribute("succesMessage", "Cette tâche n'existe pas ou plus.");
                        allerPageAccueilConnecté(request, response, utilisateurDAO);
                        break;
                    }
                    default: {
                        request.setAttribute("tache", tacheDAO.getTache(idTacheRetour, tacheAtomDAO, competenceDAO, utilisateurDAO));
                        request.setAttribute("candidatures", utilisateurDAO.getCandidaturesExecutant((Utilisateurs) request.getSession().getAttribute("utilisateur")));
                        getServletContext().getRequestDispatcher("/WEB-INF/ficheTache.jsp").forward(request, response);
                        break;
                    }
                }
            } else {
                response.sendRedirect("./controleur");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("./controleur");
        }
    }

    /**
     * Retire la candidature de l'utilisateur actuellement connecté
     * pour la tâche dont l'id idTacheAtom est apporté par la request
     * @param request
     * @param response
     * @param tacheAtomDAO
     * @param tacheDAO
     * @param utilisateurDAO
     * @param competenceDAO
     * @throws ServletException
     * @throws IOException
     * @throws DAOException 
     */
    private void actionDepostuler(HttpServletRequest request,
            HttpServletResponse response,
            TacheAtomDAO tacheAtomDAO,
            TacheDAO tacheDAO,
            UtilisateurDAO utilisateurDAO,
            CompetenceDAO competenceDAO)
            throws ServletException, IOException, DAOException {
        Utilisateurs utilisateur = (Utilisateurs) request.getSession(false).getAttribute("utilisateur");
        String idTacheAtom = request.getParameter("idTacheAtom");
        try {
            if (utilisateur != null
                    && !utilisateurDAO.proposedThisAtomTask(Integer.valueOf(idTacheAtom), utilisateur)) {
                int idTacheRetour = tacheAtomDAO.depostuler(utilisateur, Integer.valueOf(idTacheAtom), utilisateurDAO);
                request.setAttribute("tache", tacheDAO.getTache(idTacheRetour, tacheAtomDAO, competenceDAO, utilisateurDAO));
                request.setAttribute("candidatures", utilisateurDAO.getCandidaturesExecutant((Utilisateurs) request.getSession().getAttribute("utilisateur")));
                getServletContext().getRequestDispatcher("/WEB-INF/ficheTache.jsp").forward(request, response);
            } else {
                response.sendRedirect("./controleur");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("./controleur");
        }
    }

    /**
     * Supprime une tâche dont l'id idTache doit être contenu dans la request
     * @param request
     * @param response
     * @param tacheDAO
     * @param utilisateurDAO
     * @param tacheAtomDAO
     * @param competenceDAO
     * @throws DAOException
     * @throws IOException
     * @throws ServletException
     */
    private void actionSupprimerTache(HttpServletRequest request, HttpServletResponse response, TacheDAO tacheDAO, UtilisateurDAO utilisateurDAO, TacheAtomDAO tacheAtomDAO, CompetenceDAO competenceDAO) throws DAOException, IOException, ServletException {
        String idTache = request.getParameter("idTache");
        try {
            if (idTache != null
                && utilisateurDAO.proposedThisTask(Integer.valueOf(idTache),
                        ((Utilisateurs) request.getSession(false).getAttribute("utilisateur")))
                && !tacheDAO.getTache(Integer.valueOf(idTache), tacheAtomDAO, competenceDAO, utilisateurDAO).estEntamee()) {
            tacheDAO.supprimerTache(Integer.valueOf(request.getParameter("idTache")));
            actionVoirMesTaches(request, response, utilisateurDAO);
            } else {
                response.sendRedirect("./controleur");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("./controleur");
        }
    }

    private void actionSupprimerCompte(HttpServletRequest request,
            HttpServletResponse response,
            UtilisateurDAO utilisateurDAO)
            throws IOException, DAOException, ServletException {
        if (request.getParameter("email") != null) {
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

    /**
     * Supprime une tâche atomique dont l'id idTacheAtom doit être apporté par la request
     * @param request the value of request
     * @param response the value of response
     * @param tacheAtomDAO the value of tacheAtomDAO
     * @param utilisateurDAO the value of utilisateurDAO
     * @throws DAOException
     * @throws IOException
     * @throws ServletException
     */
    private void actionSupprimerTacheAtom(HttpServletRequest request, HttpServletResponse response, TacheAtomDAO tacheAtomDAO, UtilisateurDAO utilisateurDAO)
            throws DAOException, IOException, ServletException {
        try {
            String idTacheAtom = request.getParameter("idTacheAtom");
            if (idTacheAtom != null
                    && utilisateurDAO.proposedThisAtomTask(Integer.valueOf(idTacheAtom), ((Utilisateurs) request.getSession(false).getAttribute("utilisateur")))
                    && !tacheAtomDAO.getTacheAtom(Integer.valueOf(idTacheAtom), utilisateurDAO).estEntamee()) {
                tacheAtomDAO.supprimerTacheAtom(Integer.valueOf(idTacheAtom));
                actionVoirMesTaches(request, response, utilisateurDAO);
            } else {
                response.sendRedirect("./controleur");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("./controleur");
        }
    }

    private void actionVoirMesCandidatures(HttpServletRequest request,
            HttpServletResponse response,
            UtilisateurDAO utilisateurDAO,
            TacheAtomDAO tacheAtomDAO)
            throws DAOException, IOException, ServletException {
        ArrayList<TacheAtom> candidatures = new ArrayList<>();
        Utilisateurs utilisateur = (Utilisateurs) request.getSession(false).getAttribute("utilisateur");
        HashSet<Integer> list = utilisateurDAO.getCandidaturesExecutant(utilisateur);
        for (Integer i : list) {
            candidatures.add(tacheAtomDAO.getTacheAtom(i, utilisateurDAO));
        }
        request.setAttribute("candidatures", candidatures);
        request.setAttribute("services", utilisateurDAO.getTachesExecutantFinies(utilisateur));
        getServletContext().getRequestDispatcher("/WEB-INF/panneauCandidatures.jsp").forward(request, response);
    }

    private void actionVoirCandidaturesTache(HttpServletRequest request,
            HttpServletResponse response,
            UtilisateurDAO utilisateurDAO,
            TacheAtomDAO tacheAtomDAO)
            throws IOException, DAOException, ServletException {
        try {
            Utilisateurs utilisateur = (Utilisateurs) request.getSession(false).getAttribute("utilisateur");
            int idTacheAtom = Integer.valueOf(request.getParameter("idTacheAtom"));
            if (utilisateurDAO.proposedThisAtomTask(idTacheAtom, utilisateur)) {
                request.setAttribute("tacheAtom", tacheAtomDAO.getTacheAtom(idTacheAtom, utilisateurDAO));
                request.setAttribute("candidatures", utilisateurDAO.getCandidaturesCommanditaire(utilisateur, idTacheAtom));
                getServletContext().getRequestDispatcher("/WEB-INF/candidaturesTache.jsp").forward(request, response);
            } else {
                response.sendRedirect("./controleur");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("./controleur");
        }

    }

    private void actionAccepterCandidature(HttpServletRequest request,
            HttpServletResponse response,
            TacheAtomDAO tacheAtomDAO,
            UtilisateurDAO utilisateurDAO)
            throws DAOException, IOException, ServletException {
        try {
            if (request.getParameter("idTacheAtom") != null
                    && request.getParameter("idCandidat") != null) {
                Utilisateurs utilisateur = (Utilisateurs) request.getSession(false).getAttribute("utilisateur");
                int idTacheAtom = Integer.valueOf(request.getParameter("idTacheAtom"));
                String idCandidat = request.getParameter("idCandidat");
                if (utilisateurDAO.proposedThisAtomTask(idTacheAtom, utilisateur)) {
                    if (tacheAtomDAO.accepterCandidature(idCandidat, idTacheAtom) == -1) {
                        request.setAttribute("Message", "Impossible d'accepter la candidature car elle a été retirée.");
                    }
                    actionVoirMesTaches(request, response, utilisateurDAO);
                } else {
                    response.sendRedirect("./controleur");
                }
            } else {
                response.sendRedirect("./controleur");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("./controleur");
        }
    }

    /**
     * Lorsque le commanditaire déclare une tâche comme finie Aller à la page
     * pour évaluation
     *
     * @param request
     * @param response
     * @param utilisateurDAO
     */
    private void actionFinDeTache(HttpServletRequest request,
            HttpServletResponse response,
            TacheAtomDAO tacheAtomDAO,
            UtilisateurDAO utilisateurDAO)
            throws DAOException, ServletException, IOException {
        try {
            int idTacheAtom = Integer.valueOf(request.getParameter("idTacheAtom"));
            if (request.getParameter("idCandidat") != null) {
                tacheAtomDAO.finir(idTacheAtom);
                request.setAttribute("commanditaire", (Utilisateurs) request.getSession(false).getAttribute("utilisateur"));
                request.setAttribute("tache", tacheAtomDAO.getTacheAtom(idTacheAtom, utilisateurDAO));
                request.setAttribute("executant", utilisateurDAO.getUtilisateur(request.getParameter("idCandidat")));
                getServletContext().getRequestDispatcher("/WEB-INF/evaluations.jsp").forward(request, response);
            } else {
                response.sendRedirect("./controleur");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("./controleur");
        }
    }

    private void actionGenerationFacture(HttpServletRequest request,
            HttpServletResponse response,
            UtilisateurDAO utilisateurDAO,
            TacheAtomDAO tacheAtomDAO)
            throws IOException, DAOException {
        String idTacheAtom = request.getParameter("idTacheAtom");
        Utilisateurs utilisateur = (Utilisateurs) request.getSession(false).getAttribute("utilisateur");
        if (idTacheAtom != null && utilisateur != null) {
            try {
                if ((utilisateurDAO.executesThisAtomTask(utilisateur.getEmail(), Integer.valueOf(idTacheAtom))
                        || utilisateurDAO.proposedThisAtomTask(Integer.valueOf(idTacheAtom), utilisateur))
                        && tacheAtomDAO.isOver(Integer.valueOf(idTacheAtom))) {
                    try {
                        utilisateurDAO.genereFacture(Integer.valueOf(idTacheAtom), response.getOutputStream());
                        response.setContentType("application/pdf");
                    } catch (NumberFormatException e) {
                        response.sendRedirect("./controleur");
                    }
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
    private void actionEvaluer(HttpServletRequest request,
            HttpServletResponse response,
            EvaluationDAO evaluationDAO,
            UtilisateurDAO utilisateurDAO)
            throws DAOException, ServletException, IOException {
        String note = request.getParameter("note");
        if (note != null) {
            try {
                evaluationDAO.ajouterEvaluation(Integer.valueOf(note),
                        (Integer) request.getSession(false).getAttribute("idTacheAtom"),
                        new String(request.getParameter("commentaire").getBytes("iso-8859-1"), "UTF-8"));
                utilisateurDAO.miseAJourMoyenneUtilisateur((Integer) request.getSession(false).getAttribute("idTacheAtom"));
            } catch (NumberFormatException e) {
                response.sendRedirect("./controleur");
            }
        } else {
            response.sendRedirect("./controleur");
        }
    }

    /**
     *  Affiche la page de profil d'un utilisateur
     * @param request the value of request
     * @param response the value of response
     * @param utilisateurDAO the value of utilisateurDAO
     * @param evaluationDAO the value of evaluationDAO
     * @throws IOException
     * @throws ServletException
     * @throws DAOException
     */
    private void actionConsulterProfil(HttpServletRequest request, HttpServletResponse response, UtilisateurDAO utilisateurDAO, EvaluationDAO evaluationDAO)
            throws IOException, ServletException, DAOException {
        Utilisateurs utilisateur = (Utilisateurs) request.getSession(false).getAttribute("utilisateur");
        if (request.getParameter("utilisateurConsulte") == null
                || request.getParameter("utilisateurConsulte").equals(utilisateur.getEmail())) {
            request.setAttribute("utilisateurConsulte", utilisateur);
            request.setAttribute("commentaires", evaluationDAO.commentairesEvaluation(utilisateur.getEmail()));
            getServletContext().getRequestDispatcher("/WEB-INF/profil.jsp").forward(request, response);
        } else {
            Utilisateurs utilisateurConsulte = utilisateurDAO.getUtilisateur(request.getParameter("utilisateurConsulte"));
            if (utilisateurConsulte == null) {
                response.sendRedirect("./controleur");
            } else {
                request.setAttribute("commentaires", evaluationDAO.commentairesEvaluation(utilisateurConsulte.getEmail()));
                request.setAttribute("utilisateurConsulte", utilisateurConsulte);
                getServletContext().getRequestDispatcher("/WEB-INF/profil.jsp").forward(request, response);
            }
        }
    }

    private void actionRecherche(HttpServletRequest request, HttpServletResponse response, UtilisateurDAO utilisateurDAO) throws ServletException, IOException, DAOException {
        String recherche = request.getParameter("recherche");
        if (recherche != null) {
            Utilisateurs utilisateur = (Utilisateurs) request.getSession(false).getAttribute("utilisateur");
            request.setAttribute("tachesCommanditaire", utilisateurDAO.getTachesCommanditaire(utilisateur.getEmail()));
            request.setAttribute("tachesEnCours", utilisateurDAO.getTachesEnCours(utilisateur.getEmail()));
            request.setAttribute("tachesExecutant", utilisateurDAO.getTachesPotentiellesRecherchees(utilisateur, recherche));
            getServletContext().getRequestDispatcher("/WEB-INF/user_page.jsp").forward(request, response);
        } else {
            response.sendRedirect("./controleur");
        }
    }
}