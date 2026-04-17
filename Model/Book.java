package Model;

import java.util.Date;

/**
 * Represents a Book type work within the library system.
 * <p>
 * This class extends {@link Work} and adds specific management 
 * for the ISBN (International Standard Book Number) identifier.
 * </p>
 *
 * @version 1.0
 */
public class Book extends Work {

    /**
     * Full constructor to initialize a Book instance.
     *
     * @param isbn         The unique International Standard Book Number.
     * @param title        The title of the book.
     * @param cote         The library shelf reference (call number).
     * @param editor       The publishing house or editor name.
     * @param dateParution The publication date.
     * @param reference    The library instance this work belongs to.
     * @see Work
     */
    public Book(String isbn, String title, String cote, String editor, Date dateParution, Bibliotheque reference) {
        super(title, cote, editor, dateParution, reference);
        this._isbn = isbn;
    }
    
    final private String _isbn;

    /**
     * Returns the book's ISBN.
     * * @return A string representing the ISBN identifier.
     */
    public String   getIsbn() {
        return (this._isbn);
    }

    /**
     * Checks if the provided string matches the book's ISBN.
     * * @param s The string to compare against the ISBN.
     * @return {@code true} if the ISBNs match exactly; {@code false} otherwise.
     */
    public Boolean  isIsbn(String s) {
        return (this._isbn.equals(s));
    }
}