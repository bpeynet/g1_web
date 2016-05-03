/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modeles.outils;

public class Coordonnees {
    private double latitude;
    private double longitude;
    
    public Coordonnees (double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
    
    
    //ajouter fonction convertissant adresse en coordonnées

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
