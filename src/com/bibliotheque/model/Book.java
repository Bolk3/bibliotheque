package com.bibliotheque.model;

import java.util.Date;

/**
 * Represents a book within the library system.
 *
 * <p>This class extends {@link Work} and adds an ISBN (International Standard
 * Book Number) identifier, which uniquely identifies a book's edition across
 * the publishing industry. The ISBN is immutable once the book is created.</p>
 *
 * <p>All general work attributes (title, category, editor, publication date,
 * copies, authors, and events) are managed by the parent class {@link Work}.
 * Only the book-specific {@code isbn} attribute is declared here.</p>
 *
 * @see Work
 * @see Bibliotheque
 * @see Copy
 * @see Author
 *
 * @version 1.1
 */
public class Book extends Work {

    private final String _isbn;

    /**
     * Constructs a new {@code Book} instance with its specific identifier.
     *
     * <p>All shared metadata is forwarded to the {@link Work} constructor.
     * Only the ISBN is handled at this level.</p>
     *
     * @param isbn      the unique International Standard Book Number (ISBN-10
     *                  or ISBN-13); must not be {@code null}
     * @param title     the title of the book; must not be {@code null}
     * @param category  the library classification code ("cote") of this work;
     *                  must not be {@code null}
     * @param editor    the publishing house or editor name; must not be {@code null}
     * @param pubDate   the official publication date; must not be {@code null}
     * @param handler   the {@link Bibliotheque} instance managing this work;
     *                  must not be {@code null}
     */
    public Book(String isbn, String title, String category, String editor,
                Date pubDate, Bibliotheque handler) {
        super(title, category, editor, pubDate, handler);
        this._isbn = isbn;
    }

    // -------------------------------------------------------------------------
    // Queries
    // -------------------------------------------------------------------------

    /**
     * Returns whether the given string matches this book's ISBN exactly.
     *
     * @param isbn the ISBN string to compare against; must not be {@code null}
     * @return {@code true} if the ISBNs match; {@code false} otherwise
     */
    public Boolean isIsbn(String isbn) {
        return this._isbn.equals(isbn);
    }

    // -------------------------------------------------------------------------
    // Getters
    // -------------------------------------------------------------------------

    /**
     * Returns the ISBN identifier of this book.
     *
     * <p>The returned value may be either an ISBN-10 or an ISBN-13 depending
     * on when the book was registered. No format validation is enforced
     * by this class.</p>
     *
     * @return the ISBN string; never {@code null}
     */
    public String getIsbn() {
        return this._isbn;
    }
}
