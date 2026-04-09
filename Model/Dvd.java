import java.util.*;

/**
 * 
 */
public class Dvd extends Oeuvre {

    /**
     * Default constructor
     */
    public Dvd(
			String titre,
			String cote,
			String editeur,
			Date dateParution,
			Bibliotheque handler,
			String region
			) {
		super(titre, cote, editeur, dateParution, handler);
		this.region = region;
    }

    /**
     * 
     */
    private String region;

	public String getRegion() {
		return (this.region);
	}

	public void setRegion(String r) {
		this.region = r;
	}
}
