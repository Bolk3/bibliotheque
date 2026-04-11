import java.util.Date;
import java.util.Set;
import java.util.Vector;

public abstract class Work {

	/**
	 * 
	 * @param title		title of the work
	 * @param category	category of the work 
	 * @param editor	the editor of work
	 * @param pubDate	publication date of the work
	 * @param handler	general manager of the app
	 */
    public Work(String title, String category, String editor, Date pubDate, Bibliotheque handler) {
		this._title = title;
		this._category = category;
		this._editor = editor;
		this._publicationDate = pubDate;
		this._handler = handler;
    }

    private final Bibliotheque			_handler;
    private final String				_title;
    private final String				_category;
    private final String				_editor;
    private final Date					_publicationDate;
    private final Vector<Copy>			_copies = new Vector<>();
    private Set<Author>					_authors;
    private Set<Evenement>				_evenements;
	
	/**
	 * add a new unique evenment to the set of event
	 * @param e - the new evenment 
	 */
	public void	addEvenments(Evenement e) {
		this._evenements.add(e);
	}

	/**
	 * add a new unique author to the set of authors
	 * @param a - the new author
	 */
	public void	addAuthor(Author a) {
		this._authors.add(a);
	}

	/**
	 * add a new copy to the vector of copies
	 * @param e - the new copy
	 */
	public void	addExemplaire(Copy e){
		if (!(this._copies.contains(e))) {
			this._copies.add(e);
		}
	}

	/**
	 * 
	 * @return the title of the work
	 */
	public String	getTitle() {
		return (this._title);
	}

	/**
	 * 
	 * @return
	 */
	public Date	getPublicationDate() {
		return (this._publicationDate);
	}

	/**
	 * 
	 * @return
	 */
	public Set<Evenement>	get_evenements() {
		return (this._evenements);
	}

	/**
	 * 
	 * @return
	 */
	public Bibliotheque	getHandler() {
		return (this._handler);
	}

	/**
	 * 
	 * @param s
	 * @return
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

	/**
	 * 
	 * @param s
	 * @return
	 */
	public Boolean	isEditor(String s) {
		return (this._editor.equals(s));
	}

	/**
	 * 
	 * @param author
	 * @return
	 */
	public Boolean isAuthor(Author author) {
		return (this._authors.contains(author));
	}

	/**
	 * 
	 * @param c
	 * @return
	 */
	public Boolean isCote(String c) {
		return (this._category.equals(c));
	}

	/**
	 * 
	 * @param t
	 * @return
	 */
	public Boolean isTitle(String t) {
		return (this._title.equals(t));
	}
}
