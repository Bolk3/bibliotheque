import java.util.*;

/**
 * 
 */
public abstract class Oeuvre {

    /**
     * Default constructor
     */
    public Oeuvre(
			String titre,
			String cote,
			String editeur,
			Date dateParution,
			Bibliotheque handler) {
		this.titre = titre;
		this.cote = cote;
		this.editeur = editeur;
		this.dateParution = dateParution;
		this.handler = handler;
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
    private Vector<Auteur> auteurs = new Vector<Auteur>();

    /**
     * 
     */
    private Vector<Exemplaire> exemplaires = new Vector<Exemplaire>();

    /**
     * 
     */
    private Bibliotheque handler;

    /**
     * 
     */
    private Vector<Evenement> supports = new Vector<Evenement>();
	
	public void addAuteur(Auteur auteur) {
		if (!(this.auteurs.contains(auteur))) {
			this.auteurs.add(auteur);
		}
	}

	public void addExemplaire(Exemplaire e) {
		if (!(this.exemplaires.contains(e))) {
			this.exemplaires.add(e);
		}
	}

	public void addEvenement(Evenement e) {
		if (!(this.supports.contains(e))) {
			this.supports.add(e);
		}
	}

	public Vector<Auteur> getAuteurs() {
		return (this.auteurs);
	}

	public Vector<Exemplaire> getExemplaires() {
		return (this.exemplaires);
	}

	public Vector<Evenement> getEvenements() {
		return (this.supports);
	}

	public String getTitle() {
		return (this.titre);
	}

	public String getCote() {
		return (this.cote);
	}

	public String getEditeur() {
		return (this.editeur);
	}

	public Date getDateParution() {
		return (this.dateParution);
	}
}
