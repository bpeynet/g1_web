package modeles;

import java.util.ArrayList;
import java.util.Date;
import modeles.outils.Competences;
import modeles.outils.Coordonnees;

public class TacheAtom {
    private Integer idTacheAtom=null;
    private Integer idTache=null;
    private String titreTacheAtom=null;
    private String descriptionTache=null;
    private Float prixTache=null;
    private Coordonnees localisation=null;
    private Date datePlusTot=null;
    private Date datePlusTard=null;
    private String emailCommanditaire=null;
    private String emailExecutant=null;
    private ArrayList<Competences> competences=null;
    private Integer indicateurFin=null;
    private String adresse = null;
    
    public TacheAtom(Integer idTacheAtom, Integer idTache, String titreTacheAtom,
            String descriptionTache, Float prixTache, Coordonnees localisation,
            Date datePlusTot, Date datePlusTard, String emailCommanditaire,
            ArrayList<Competences> competences, String adresse) {
        this.idTacheAtom = idTacheAtom;
        this.idTache = idTache;
        this.titreTacheAtom = titreTacheAtom;
        this.descriptionTache = descriptionTache;
        this.prixTache = prixTache;
        this.localisation = localisation;
        this.datePlusTot = datePlusTot;
        this.datePlusTard = datePlusTard;
        this.emailCommanditaire = emailCommanditaire;
        this.competences = new ArrayList<>();
        if (competences != null) this.competences.addAll(competences);
        this.adresse = adresse;
    }
    
    public TacheAtom(Integer idTacheAtom, Integer idTache, String titreTacheAtom,
            String descriptionTache, Float prixTache, Coordonnees localisation,
            Date datePlusTot, Date datePlusTard, String emailCommanditaire,
            String emailExecutant, ArrayList<Competences> competences,
            Integer indicateurFin, String adresse) {
        this.idTacheAtom = idTacheAtom;
        this.idTache = idTache;
        this.titreTacheAtom = titreTacheAtom;
        this.descriptionTache = descriptionTache;
        this.prixTache = prixTache;
        this.localisation = localisation;
        this.datePlusTot = datePlusTot;
        this.datePlusTard = datePlusTard;
        this.emailCommanditaire = emailCommanditaire;
        this.emailExecutant = emailExecutant;
        this.competences = new ArrayList<>();
        if (competences != null) this.competences.addAll(competences);
        this.indicateurFin = indicateurFin;
        this.adresse = adresse;
    }
    
    public Integer getIdTacheAtom() {
        return idTacheAtom;
    }
    
    public Integer getIdTacheMere() {
        return idTache;
    }
    
    public String getTitreTacheAtom() {
        return titreTacheAtom;
    }
    
    public String getDescription() {
        return descriptionTache;
    }
    
    public Float getPrix() {
        return prixTache;
    }
    
    public Coordonnees getLocalisation() {
        return localisation;
    }
    
    public Date getDateTot() {
        return datePlusTot;
    }
    
    public Date getDatetard() {
        return datePlusTard;
    }
    
    public ArrayList<Competences> getCompetences() {
        return competences;
    }

    public String getEmailCommanditaire() {
        return emailCommanditaire;
    }
    
    public String getEmailExecutant() {
        return emailExecutant;
    }
    
    public void ajouterCompetences(String competence) {
        this.competences.add(new Competences(competence));
    }
    
    public Integer getIndicateurFin() {
        return indicateurFin;
    }
    
    public String getAdresse() {
        return adresse;
    }
    
    public boolean estEntamee() {
        return emailExecutant!=null ? true : false;
    }
}
