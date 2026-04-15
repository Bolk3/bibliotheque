package Model;
import java.io.*;
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
    private Set<Emprunt> emprunts;

    /**
     * 
     */
    private Set<Evenement> evenementsParticipe;

}