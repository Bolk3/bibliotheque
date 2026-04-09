
import java.io.*;
import java.util.*;

/**
 * 
 */
public class Retour {

    /**
     * Default constructor
     */
    public Retour(
			String etatRendus,
			Emprunt emprunt,
			Bibliothecaire validerPar) {
		this.etatRendus = etatRendus;
		this.emprunt = emprunt;
		this.validePar = validerPar;
    }

    /**
     * 
     */
    private String etatRendus;

    /**
     * 
     */
    private Emprunt emprunt;

    /**
     * 
     */
    private Bibliothecaire validePar;

	public String getEtatRendus() {
		return (this.etatRendus);
	}
	
	public Emprunt getReference() {
		return (this.emprunt);
	}

	public Bibliothecaire getEmploye() {
		return (this.validePar);
	}
}
