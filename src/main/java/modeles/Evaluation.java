/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modeles;

import java.util.Date;

/**
 *
 * @author ralambom
 * @date 31/03/2016
 */
public class Evaluation {
    private Integer idEvaluation;
    private Integer evaluation;
    private Date date;
    private String commentaire;
    
    public Evaluation(Integer idEvaluation, Integer evaluation, Date date, String commentaire) {
        this.idEvaluation= idEvaluation;
        this.evaluation = evaluation; // retourner une erreur si evaluation n'est pas entre 0 et 5 ?
        this.date = date;
        this.commentaire = commentaire;
    }
    
    public Evaluation(Integer evaluation) {
        this.evaluation = evaluation;
    }
    
    public Integer getIdEval() {
        return idEvaluation;
    }
    
    public Integer getEvaluation() {
        return evaluation;
    }
    
    public Date getDate() {
        return date;
    }
    
    public String getCommentaire() {
        return commentaire;
    }
}
