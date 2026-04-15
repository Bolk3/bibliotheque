
import java.io.*;
import java.util.*;

/**
 * 
 */
public abstract class Oeuvre {

    /**
     * Default constructor
     */
    public Oeuvre() {
    }

    /**
     * 
     */
    private String titre;

    /**
     * 
     */
    private String cote;

    /**
     * 
     */
    private String editeur;

    /**
     * 
     */
    private Date dateParution;

    /**
     * 
     */
    private Set<Auteur> auteurs;

    /**
     * 
     */
    private Set<Exemplaire> exemplaires;

    /**
     * 
     */
    private Bibliotheque handler;

    /**
     * 
     */
    private Set<Evenement> supports;

}