
import java.io.*;
import java.util.*;

/**
 * 
 */
public class Evenement {

    /**
     * Default constructor
     */
    public Evenement() {
    }

    /**
     * 
     */
    private Date dateDebut;

    /**
     * 
     */
    private Date dateFin;

    /**
     * 
     */
    private String typeEvenement;

    /**
     * 
     */
    private Set<Oeuvre> evenements;

    /**
     * 
     */
    private Set<Membre> participants;

    /**
     * 
     */
    private Set<Intervenant> intervenant;

    /**
     * 
     */
    private Bibliotheque handler;

}