
import java.io.*;
import java.util.*;

// TODO /!\: operateur equals a mettre sinn ca casse 

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