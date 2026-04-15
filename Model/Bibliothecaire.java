package Model;
import java.util.*;

/**
 * 
 */
public class Bibliothecaire extends Utilisateur {

    /**
     * Default constructor
     */
    public Bibliothecaire() {
    }

    /**
     * 
     */
    private String poste;

    /**
     * 
     */
    private int permision;

    /**
     * 
     */
    private Set<Emprunt> empruntsValide;

    /**
     * 
     */
    private Set<Retour> retoursValide;

}