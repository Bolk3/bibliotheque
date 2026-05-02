package com.bibliotheque.model;
import java.util.*;

/**
 * 
 */
public class Membre extends Utilisateur {

    /**
     * Default constructor
     */
    public Membre() {
    }

    /**
     * 
     */
    private double penalite;

    /**
     * 
     */
    private Boolean estBloque;

    /**
     * 
     */
    private Set<Borrow> emprunts;

    /**
     * 
     */
    private Set<Evenement> evenementsParticipe;

}
