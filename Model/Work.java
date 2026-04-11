import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

/**
 * Abstract base class representing a generic work (intellectual content) within the library.
 * <p>
 * This class serves as the parent for all specific media types like {@link Book} and {@link Dvd}.
 * It manages common metadata such as titles, authors, and the physical copies associated with it.
 * </p>
 *
 * @version 1.0
 */
public abstract class Work {

	/**
     * Constructs the fundamental metadata for any library work.
     *
     * @param title    The title of the work.
     * @param category The classification category (or "cote") of the work.
     * @param editor   The publishing house or editor.
     * @param pubDate  The official publication date.
     * @param handler  The {@link Bibliotheque} instance managing this work.
     */
    public Work(String title, String category, String editor, Date pubDate, Bibliotheque handler) {
		this._title = title;
		this._category = category;
		this._editor = editor;
		this._publicationDate = pubDate;
		this._handler = handler;
		this._authors = new HashSet<>();
		this._evenements = new HashSet<>();
    }

    private final Bibliotheque		_handler;
    private final String			_title;
    private final String			_category;
    private final String			_editor;
    private final Date				_publicationDate;
    private final Vector<Copy>		_copies = new Vector<>();
    private final Set<Author>		_authors;
    private final Set<Evenement>	_evenements;
	
	/**
     * Records a new unique event associated with this work.
     * * @param e The {@link Evenement} to be added.
     */
	public void	addEvenments(Evenement e) {
		this._evenements.add(e);
	}

	/**
     * Adds an author to the work's credited authors.
     * * @param a The {@link Author} to be credited.
     */
	public void	addAuthor(Author a) {
		this._authors.add(a);
	}

	/**
     * Adds a new physical copy of this work to the library's inventory.
     * * @param e The {@link Copy} instance to add.
     */
	public void	addExemplaire(Copy e){
		if (!(this._copies.contains(e))) {
			this._copies.add(e);
		}
	}

	/** @return The title of the work. */
	public String	getTitle() {
		return (this._title);
	}

	/** @return The official publication date. */
	public Date	getPublicationDate() {
		return (this._publicationDate);
	}

	/** @return A {@link Set} of events related to this work. */
	public Set<Evenement>	get_evenements() {
		return (this._evenements);
	}

	/** @return The {@link Bibliotheque} managing this work. */
	public Bibliotheque	getHandler() {
		return (this._handler);
	}

	/**
     * Filters the copies of this work based on their physical condition.
     * @param s The state string to filter by (e.g., "New").
     * @return A {@link Vector} containing all {@link Copy} instances matching the state.
     */
	public Vector<Copy>	getExemplaireByState(String s) {
		Vector<Copy>	result = new Vector<>();
		for (Copy e: _copies) {
			if (e.isState(s)) {
				result.add(e);
			}		
		}
		return (result);
	}

	/** @return {@code true} if the provided string matches the editor. */
	public Boolean	isEditor(String s) {
		return (this._editor.equals(s));
	}

	/** @return {@code true} if the specified author is credited on this work. */
	public Boolean isAuthor(Author author) {
		return (this._authors.contains(author));
	}

	/** @return {@code true} if the provided string matches the work's category code. */
	public Boolean isCote(String c) {
		return (this._category.equals(c));
	}

	/** @return {@code true} if the provided string matches the exact title. */
	public Boolean isTitle(String t) {
		return (this._title.equals(t));
	}
}
