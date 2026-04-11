import java.util.*;

public class Author {

    /**
     * 
     * @param authSurname   Surname of the author
     * @param authName      Name of the author
     */
    public Author(String authSurname, String authName) {
        this._authorSurname = authSurname;
        this._authorName = authName;
    }

    private final String    _authorSurname;
    private final String    _authorName;
    private Set<Work>     _Works;

    /**
     * Returns the author name
     * @return  The author name
     */
    public String getName() {
        return (this._authorName);
    }

    /**
     * Returns the author surname
     * @return  The author Surname
     */
    public String getSurname() {
        return (this._authorSurname);
    }

    /**
     * Verify if a string is the name of the author
     * @param n String to check
     * @return  Boolean checking if n is the name of the author
     */
    public Boolean  isName(String n) {
        return (this._authorName.equals(n));
    }

    /**
     * Verify if a string is the surname of the author
     * @param n String to check
     * @return  Boolean checking if n is the surname of the author
     */
    public Boolean  isSurname(String n) {
        return (this._authorSurname.equals(n));
    }

    /**
     * Adds a new unique work to the author credit
     * @param w A work that hasn't already been added to the set
     */
    public void addWork(Work w) {
        this._Works.add(w);
    }

    /**
     * Get all works the author was credited on
     * @return  A Set of all works the author was credited on
     */
    public Set<Work> getWorks() {
        return (this._Works);
    }
}