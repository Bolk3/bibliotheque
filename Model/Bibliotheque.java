
import java.io.*;
import java.util.*;

/**
 * 
 */
public class Bibliotheque {

    /**
     * Default constructor
     */
    public Bibliotheque(String name, String adr) {
		//pas sur d'utiliser vector en vrai
		this.nom = name;
		this.adresse = adr;
		this.catalogue = new Vector<Oeuvre>();
		this.Utilisateurs = new Vector<Utilisateur>();
		this.listEvenement = new Vector<Evenement>();
    }

	public boolean	OeuvreIsPresent(Oeuvre o) {
		return (this.catalogue.contains(o));
	}
    /**
     * 
     */
    private String nom;

    /**
     * 
     */
    private String adresse;

    /**
     * 
     */
    private Vector<Oeuvre> catalogue;

    /**
     * 
     */
    private Vector<Utilisateur> Utilisateurs;

    /**
     * 
     */
    private Vector<Evenement> listEvenement;

}