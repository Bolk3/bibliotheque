package com.bibliotheque.model;
import java.io.*;
import java.util.*;

/**
 * 
 */
public class Intervenant extends Utilisateur {

    /**
     * Default constructor
     */
    public Intervenant() {
    }

    /**
     * 
     */
    private String specialite;

    /**
     * 
     */
    private Set<Evenement> evenementParticipe;

}
