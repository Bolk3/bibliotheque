
import java.io.*;
import java.util.*;

/**
 * 
 */
public class Emprunt {

    /**
     * Default constructor
     */
    public Emprunt(
			Date prevu,
			Bibliothecaire valide,
			Membre emprunteur,
			Exemplaire exemplaire) {
		this.dateDebut = new Date();
		this.datePrevu = prevu;
		this.dateRendu = null;
		this.validePar = valide;
		this.empruntePar = emprunteur;
		this.exemplaire = exemplaire;
		this.retour = null;
		this.etatInitial = this.exemplaire.getEtat();
    }

    /**
     * 
     */
    private Date dateDebut;

    /**
     * 
     */
    private Date datePrevu;

    /**
     * 
     */
    private Date dateRendu;

    /**
     * 
     */
    private Bibliothecaire validePar;

    /**
     * 
     */
    private Membre empruntePar;

    /**
     * 
     */
    private Exemplaire exemplaire;

    /**
     * 
     */
    private Retour retour;

	private String etatInitial;

	public Boolean isLate() {
		Date now = new Date();

		return (now.after(this.datePrevu));
	}

	public Boolean isReturned() {
		return (this.retour == null);
	}

	public Boolean isDamaged() {
		String returnState = this.retour.getEtatRendus();

		return (returnState != this.etatInitial);
	}

	public void updateEtat() {
		if (this.isDamaged()) {
			this.exemplaire.setEtat(this.retour.getEtatRendus());
		}
	}

	public void returnBook(String etat, Bibliothecaire validerPar) {
		this.retour = new Retour(etat, this, validePar);
		this.updateEtat();
	}
}
