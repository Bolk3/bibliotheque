import java.util.*;

/**
 * Represents an author within the library system.
 * <p>
 * This class maintains the author's identity and tracks the collection 
 * of works they have contributed to.
 * </p>
 *
 * @version 1.0
 */
public class Author {

    /**
     * Constructs a new Author with a specified name and surname.
     * Initializes an empty set of works.
     * * @param authSurname Surname of the author.
     * @param authName    First name of the author.
     */
    public Author(String authSurname, String authName) {
        this._authorSurname = authSurname;
        this._authorName = authName;
        this._works = new HashSet<>();
    }

    private final String    _authorSurname;
    private final String    _authorName;
    private Set<Work>       _works;

    /**
     * Returns the author's first name.
     * * @return The first name string.
     */
    public String getName() {
        return (this._authorName);
    }

    /**
     * Returns the author's surname.
     * * @return The surname string.
     */
    public String getSurname() {
        return (this._authorSurname);
    }

    /**
     * Verifies if the provided string matches the author's first name.
     * * @param n The string to check.
     * @return {@code true} if the name matches; {@code false} otherwise.
     */
    public Boolean  isName(String n) {
        return (this._authorName.equals(n));
    }

    /**
     * Verifies if the provided string matches the author's surname.
     * * @param n The string to check.
     * @return {@code true} if the surname matches; {@code false} otherwise.
     */
    public Boolean  isSurname(String n) {
        return (this._authorSurname.equals(n));
    }

    /**
     * Adds a new unique work to the author's bibliography.
     * * @param w The work to be added to the set.
     */
    public void addWork(Work w) {
        this._works.add(w);
    }

    /**
     * Returns the complete set of works credited to this author.
     * * @return A {@link Set} containing all works associated with the author.
     */
    public Set<Work> getWorks() {
        return (this._works);
    }
}