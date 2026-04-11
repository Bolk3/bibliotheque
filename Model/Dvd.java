import java.util.*;

public class Dvd extends Oeuvre {

    /**
	 * 
	 * @param title
	 * @param category
	 * @param edithor
	 * @param pubDate
	 * @param handler
	 * @param region
	 */
    public Dvd(String title, String category, String edithor, Date pubDate, Bibliotheque handler, String region) {
		super(title, category, edithor, pubDate, handler);
		this._region = region;
    }

    private final String _region;

	/**
	 * 
	 * @param region
	 * @return
	 */
	public Boolean isRegion(String region) {
		return (this._region.equals(region));
	}
}
