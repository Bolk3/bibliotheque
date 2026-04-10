import java.util.*;

public class Author {

    /**
     * 
     * @param authSurname
     * @param authName
     */
    public Author(String authSurname, String authName) {
        this._authorSurname = authSurname;
        this._authorName = authName;
    }

    private final String    _authorSurname;
    private final String    _authorName;
    private Set<Oeuvre>     _Works;

    /**
     * 
     * @return
     */
    public String getName() {
        return (this._authorName);
    }

    /**
     * 
     * @return
     */
    public String getSurname() {
        return (this._authorSurname);
    }

    /**
     * 
     * @param n
     * @return
     */
    public Boolean  isName(String n) {
        return (this._authorName.equals(n));
    }

    /**
     * 
     * @param n
     * @return
     */
    public Boolean  isSurname(String n) {
        return (this._authorSurname.equals(n));
    }

    /**
     * 
     * @param w
     */
    public void addWork(Oeuvre w) {
        this._Works.add(w);
    }

    /**
     * 
     * @return
     */
    public Set<Oeuvre> getWorks() {
        return (this._Works);
    }
}