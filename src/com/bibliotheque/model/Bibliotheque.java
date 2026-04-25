package com.bibliotheque.model;

import com.bibliotheque.erreur.SearchStringTooSmall;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Central handler for the library system.
 *
 * <p>{@code Bibliotheque} is the entry point for all major operations in the
 * library: it owns the catalogue of {@link Work}s, the credited {@link Author}s,
 * and centralises borrow creation logic.</p>
 *
 * <p>Search operations are delegated to {@link SearchingWork}, which provides
 * flexible filtering strategies (by type, ISBN, region, date, or any string
 * attribute). {@code Bibliotheque} exposes convenience methods that forward
 * to {@link SearchingWork} using its internal catalogue.</p>
 *
 * <p>Support for users ({@code Membre}, {@code Bibliothecaire}) and events
 * ({@code Evenement}) will be added once those classes are implemented.</p>
 *
 * <p>All collections are exposed as unmodifiable views. Mutations must go
 * through the dedicated {@code add*()} methods defined on this class.</p>
 *
 * @see Work
 * @see Copy
 * @see Borrow
 * @see Author
 * @see SearchingWork
 *
 * @version 1.1
 */
public class Bibliotheque {

    private String      _nom;
    private String      _adresse;
    private Set<Work>   _catalogue = new HashSet<>();
    private Set<Author> _authors   = new HashSet<>();

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Constructs a new {@code Bibliotheque} with a name and a physical address.
     *
     * @param nom     the name of the library; must not be {@code null}
     * @param adresse the physical address of the library; must not be {@code null}
     */
    public Bibliotheque(String nom, String adresse) {
        this._nom     = nom;
        this._adresse = adresse;
    }

    // -------------------------------------------------------------------------
    // Catalogue management
    // -------------------------------------------------------------------------

    /**
     * Adds a work to the library's catalogue.
     *
     * <p>Duplicate entries are silently ignored since the underlying collection
     * is a {@link HashSet}.</p>
     *
     * @param work the {@link Work} to register; must not be {@code null}
     */
    public void addWork(Work work) {
        this._catalogue.add(work);
    }

    /**
     * Returns all copies across every work in the catalogue that are currently
     * available to be borrowed.
     *
     * @return a {@link List} of available {@link Copy} instances; never
     *         {@code null}, but may be empty
     */
    public List<Copy> getAvailableCopies() {
        return this._catalogue.stream()
                .flatMap(w -> w.getCopies().stream())
                .filter(Copy::isAvailable)
                .collect(Collectors.toList());
    }

    // -------------------------------------------------------------------------
    // Catalogue search — delegates to SearchingWork
    // -------------------------------------------------------------------------

    /**
     * Searches the catalogue for works whose title contains the given query
     * (case-insensitive).
     *
     * <p>Delegates to
     * {@link SearchingWork#search(Set, String, Function)}
     * using {@link Work#getTitle()} as the extractor.</p>
     *
     * @param query the string to search for; must not be {@code null} and
     *              must be at least 3 characters long after trimming
     * @return a {@link List} of matching {@link Work}s; never {@code null},
     *         but may be empty
     *
     * @throws SearchStringTooSmall if {@code query} is shorter than 3 characters
     */
    public List<Work> findWorksByTitle(String query) throws SearchStringTooSmall {
        return SearchingWork.search(this._catalogue, query, Work::getTitle);
    }

    /**
     * Searches the catalogue for works whose category code contains the given
     * query (case-insensitive).
     *
     * <p>Delegates to
     * {@link SearchingWork#search(Set, String, Function)}
     * using {@link Work#getCategory()} as the extractor.</p>
     *
     * @param query the category code to search for; must not be {@code null}
     *              and must be at least 3 characters long after trimming
     * @return a {@link List} of matching {@link Work}s; never {@code null},
     *         but may be empty
     *
     * @throws SearchStringTooSmall if {@code query} is shorter than 3 characters
     */
    public List<Work> findWorksByCategory(String query) throws SearchStringTooSmall {
        return SearchingWork.search(this._catalogue, query, Work::getCategory);
    }

    /**
     * Searches the catalogue for works whose editor name contains the given
     * query (case-insensitive).
     *
     * <p>Delegates to
     * {@link SearchingWork#search(Set, String, Function)}
     * using {@link Work#getEditor()} as the extractor.</p>
     *
     * @param query the editor name to search for; must not be {@code null}
     *              and must be at least 3 characters long after trimming
     * @return a {@link List} of matching {@link Work}s; never {@code null},
     *         but may be empty
     *
     * @throws SearchStringTooSmall if {@code query} is shorter than 3 characters
     */
    public List<Work> findWorksByEditor(String query) throws SearchStringTooSmall {
        return SearchingWork.search(this._catalogue, query, Work::getEditor);
    }

    /**
     * Searches the catalogue for works credited to the given author.
     *
     * @param author the {@link Author} to search for; must not be {@code null}
     * @return a {@link List} of matching {@link Work}s; never {@code null},
     *         but may be empty
     */
    public List<Work> findWorksByAuthor(Author author) {
        return this._catalogue.stream()
                .filter(w -> w.isAuthor(author))
                .collect(Collectors.toList());
    }

    /**
     * Searches the catalogue for a {@link Book} matching the given ISBN exactly.
     *
     * <p>Delegates to {@link SearchingWork#searchByIsbn(Set, String)}.</p>
     *
     * @param isbn the ISBN to search for; must not be {@code null} and must
     *             be at least 3 characters long
     * @return an {@link Optional} containing the matching {@link Book},
     *         or {@link Optional#empty()} if no match is found
     *
     * @throws SearchStringTooSmall if {@code isbn} is shorter than 3 characters
     */
    public Optional<Book> findByIsbn(String isbn) throws SearchStringTooSmall {
        return SearchingWork.searchByIsbn(this._catalogue, isbn);
    }

    /**
     * Searches the catalogue for {@link Dvd}s matching the given region code.
     *
     * <p>Delegates to {@link SearchingWork#searchByRegion(Set, String)}.</p>
     *
     * @param region the region code to match; must not be {@code null} and
     *               must be at least 3 characters long
     * @return a {@link List} of matching {@link Dvd}s; never {@code null},
     *         but may be empty
     *
     * @throws SearchStringTooSmall if {@code region} is shorter than 3 characters
     */
    public List<Dvd> findDvdsByRegion(String region) throws SearchStringTooSmall {
        return SearchingWork.searchByRegion(this._catalogue, region);
    }

    /**
     * Filters the catalogue by publication date.
     *
     * <p>Delegates to {@link SearchingWork#searchByPubDate(Set, Date)}.</p>
     *
     * @param pubDate the exact publication date to match; must not be {@code null}
     * @return a {@link List} of matching {@link Work}s; never {@code null},
     *         but may be empty
     */
    public List<Work> findWorksByPubDate(Date pubDate) {
        return SearchingWork.searchByPubDate(this._catalogue, pubDate);
    }

    /**
     * Filters the catalogue by runtime type, returning only instances of the
     * specified subclass of {@link Work}.
     *
     * <p>Delegates to {@link SearchingWork#searchByType(Set, Class)}.</p>
     *
     * <p>Example usage:</p>
     * <pre>{@code
     * List<Dvd>  dvds  = bibliotheque.findByType(Dvd.class);
     * List<Book> books = bibliotheque.findByType(Book.class);
     * }</pre>
     *
     * @param <T>        the specific subtype of {@link Work} to return
     * @param targetType the {@link Class} literal of the desired subtype;
     *                   must not be {@code null}
     * @return a {@link List} of works that are instances of {@code targetType};
     *         never {@code null}, but may be empty
     *
     * @throws com.bibliotheque.erreur.SearchClassNotInherits if {@code targetType}
     *                                                         is {@code null}
     */
    public <T extends Work> List<T> findByType(Class<T> targetType)
            throws com.bibliotheque.erreur.SearchClassNotInherits {
        return SearchingWork.searchByType(this._catalogue, targetType);
    }

    // -------------------------------------------------------------------------
    // Borrow management
    // -------------------------------------------------------------------------

    /**
     * Creates and records a new borrowing transaction for the given copy.
     *
     * <p>This method is the single authorised entry point for creating a
     * {@link Borrow}. It enforces the availability check before delegating
     * to {@link Copy#addBorrowing(Borrow)}.</p>
     *
     * @param copy         the {@link Copy} to borrow; must not be {@code null}
     * @param borrowedBy   the {@link Membre} borrowing the copy;
     *                     must not be {@code null}
     * @param validatedBy  the {@link Bibliothecaire} authorising the transaction;
     *                     must not be {@code null}
     * @param expectedDate the expected return date; must not be {@code null}
     *                     and should be strictly after the current date
     * @return the newly created {@link Borrow} transaction
     *
     * @throws IllegalStateException if the copy is not currently available
     */
    public Borrow createBorrow(Copy copy, Membre borrowedBy,
                               Bibliothecaire validatedBy, Date expectedDate)
            throws IllegalStateException {
        if (!copy.isAvailable())
            throw new IllegalStateException("Cette copie n'est pas disponible.");
        Borrow borrow = new Borrow(expectedDate, validatedBy, borrowedBy, copy);
        copy.addBorrowing(borrow);
        return borrow;
    }

    /**
     * Returns all active (unreturned) borrows across the entire catalogue.
     *
     * @return a {@link List} of active {@link Borrow} instances; never
     *         {@code null}, but may be empty
     */
    public List<Borrow> getActiveBorrows() {
        return this._catalogue.stream()
                .flatMap(w -> w.getCopies().stream())
                .flatMap(c -> c.getBorrowings().stream())
                .filter(b -> !b.isReturned())
                .collect(Collectors.toList());
    }

    /**
     * Returns all overdue borrows across the entire catalogue.
     *
     * <p>A borrow is overdue if it is still active and its expected return
     * date has passed.</p>
     *
     * @return a {@link List} of overdue {@link Borrow} instances; never
     *         {@code null}, but may be empty
     */
    public List<Borrow> getLateBorrows() {
        return this.getActiveBorrows().stream()
                .filter(Borrow::isLate)
                .collect(Collectors.toList());
    }

    // -------------------------------------------------------------------------
    // Author management
    // -------------------------------------------------------------------------

    /**
     * Registers an author with this library.
     *
     * <p>Duplicate entries are silently ignored since the underlying collection
     * is a {@link HashSet}.</p>
     *
     * @param author the {@link Author} to register; must not be {@code null}
     */
    public void addAuthor(Author author) {
        this._authors.add(author);
    }

    /**
     * Searches for authors whose first name matches the given string exactly.
     *
     * @param name the first name to search for; must not be {@code null}
     * @return a {@link List} of matching {@link Author}s; never {@code null},
     *         but may be empty
     */
    public List<Author> findAuthorsByName(String name) {
        return this._authors.stream()
                .filter(a -> a.isName(name))
                .collect(Collectors.toList());
    }

    /**
     * Searches for authors whose surname matches the given string exactly.
     *
     * @param surname the surname to search for; must not be {@code null}
     * @return a {@link List} of matching {@link Author}s; never {@code null},
     *         but may be empty
     */
    public List<Author> findAuthorsBySurname(String surname) {
        return this._authors.stream()
                .filter(a -> a.isSurname(surname))
                .collect(Collectors.toList());
    }

    // -------------------------------------------------------------------------
    // Getters
    // -------------------------------------------------------------------------

    /**
     * Returns the name of this library.
     *
     * @return the library name; never {@code null}
     */
    public String getNom() {
        return this._nom;
    }

    /**
     * Returns the physical address of this library.
     *
     * @return the address string; never {@code null}
     */
    public String getAdresse() {
        return this._adresse;
    }

    /**
     * Returns an unmodifiable view of the library's full catalogue.
     *
     * <p>Use {@link #addWork(Work)} to register a new work.</p>
     *
     * @return an unmodifiable {@link Set} of {@link Work}s; never {@code null},
     *         but may be empty
     */
    public Set<Work> getCatalogue() {
        return Collections.unmodifiableSet(this._catalogue);
    }

    /**
     * Returns an unmodifiable view of all authors registered with this library.
     *
     * <p>Use {@link #addAuthor(Author)} to register a new author.</p>
     *
     * @return an unmodifiable {@link Set} of {@link Author}s; never {@code null},
     *         but may be empty
     */
    public Set<Author> getAuthors() {
        return Collections.unmodifiableSet(this._authors);
    }
}
