
import java.io.*;
import java.util.*;

/**
 * 
 */
public class Exemplaire {

    /**
     * Default constructor
     */
    public Exemplaire(
			String etat,
			Oeuvre ref) {
		this.etat = etat;
		this.reference = ref;
    }

    /**
     * 
     */
    private String etat;

    /**
     * 
     */
    private Vector<Emprunt> emprunts = new Vector<Emprunt>();

    /**
     * 
     */
    private Oeuvre reference;

	public void addEmprut(Emprunt e) {
		if (!(this.emprunts.contains(e))) {
			this.emprunts.add(e);
		}
	}

	public Oeuvre getReference() {
		return (this.reference);
	}

	public String getEtat() {
		return (this.etat);
	}

	public void setEtat(String e) {
		this.etat = e;
	}
	
}
