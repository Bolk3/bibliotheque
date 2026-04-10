import java.util.Date;

public class Livre extends Oeuvre {

    /**
     * 
     * @param isbn
     * @param title
     * @param cote
     * @param editor
     * @param dateParution
     * @param reference
     */
    public Livre(String isbn, String title, String cote, String editor, Date dateParution, Bibliotheque reference) {
        super(title, cote, editor, dateParution, reference);
        this._isbn = isbn;
    }
    
    final private String _isbn;

    /**
     * 
     */
    public String   getIsbn() {
        return (this._isbn);
    }
}