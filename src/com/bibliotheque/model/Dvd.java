package Model;
import java.util.*;

/**
 * Represents a DVD media work within the library system.
 * <p>
 * This class extends {@link Work} and introduces a region code attribute,
 * which is specific to optical disc media for digital rights management.
 * </p>
 *
 * @version 1.0
 */
public class Dvd extends Work {

    /**
     * Constructs a new DVD instance with specific media attributes.
     *
     * @param title    The title of the film or content.
     * @param cote     The library shelf reference or call number (category).
     * @param editor   The studio or publishing house.
     * @param pubDate  The release or publication date.
     * @param handler  The {@link Bibliotheque} instance managing this work.
     * @param region   The geographical region code for the disc.
     * @see Work
     */
    public Dvd(String title, String category, String edithor, Date pubDate, Bibliotheque handler, String region) {
		super(title, category, edithor, pubDate, handler);
		this._region = region;
    }

    private final String _region;

	/**
     * Verifies if the DVD's region matches the specified region code.
     * * @param region The region string to compare against.
     * @return {@code true} if the regions match exactly; {@code false} otherwise.
     */
	public Boolean isRegion(String region) {
		return (this._region.equals(region));
	}
}
