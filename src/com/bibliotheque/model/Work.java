package com.bibliotheque.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Abstract base class representing a generic intellectual work within the library system.
 *
 * <p>This class serves as the parent for all specific media types (e.g., {@link Book},
 * {@link Dvd}). It centralises the common metadata shared by every work — title,
 * category, editor, publication date — and manages the two main collections
 * associated with a work: its physical {@link Copy} instances and its credited
 * {@link Author}s.</p>
 *
 * <p>Subclasses are expected to add media-specific attributes (e.g., an ISBN for
 * books, a region code for DVDs) without duplicating the logic handled here.</p>
 *
 * <p>All collections exposed by this class are returned as unmodifiable views.
 * Use the dedicated {@code add*()} methods to register new entries.</p>
 *
 * @see Book
 * @see Dvd
 * @see Copy
 * @see Author
 * @see Evenement
 * @see Bibliotheque
 *
 * @version 1.1
 */
public abstract class Work {

    private final Bibliotheque  _handler;
    private final String        _title;
    private final String        _category;
    private final String        _editor;
    private final Date          _publicationDate;
    private final List<Copy>    _copies     = new ArrayList<>();
    private final Set<Author>   _authors    = new HashSet<>();
    private final Set<Evenement> _evenements = new HashSet<>();

    /**
     * Constructs the fundamental metadata for any library work.
     *
     * <p>This constructor is intended to be called by subclass constructors via
     * {@code super(...)}. It initialises all shared fields and the internal
     * collections for copies, authors, and events.</p>
     *
     * @param title    the title of the work; must not be {@code null}
     * @param category the classification category (call number / "cote") of the
     *                 work within the library; must not be {@code null}
     * @param editor   the publishing house or editor; must not be {@code null}
     * @param pubDate  the official publication date; must not be {@code null}
     * @param handler  the {@link Bibliotheque} instance responsible for managing
     *                 this work; must not be {@code null}
     */
    public Work(String title, String category, String editor, Date pubDate, Bibliotheque handler) {
        this._title           = title;
        this._category        = category;
        this._editor          = editor;
        this._publicationDate = pubDate;
        this._handler         = handler;
    }

    // -------------------------------------------------------------------------
    // Mutators
    // -------------------------------------------------------------------------

    /**
     * Registers a new event associated with this work.
     *
     * <p>Duplicate events are silently ignored since the underlying collection
     * is a {@link Set}.</p>
     *
     * @param evenement the {@link Evenement} to associate with this work;
     *                  must not be {@code null}
     */
    public void addEvenement(Evenement evenement) {
        this._evenements.add(evenement);
    }

    /**
     * Credits an author on this work.
     *
     * <p>Duplicate authors are silently ignored since the underlying collection
     * is a {@link Set}.</p>
     *
     * @param author the {@link Author} to credit; must not be {@code null}
     */
    public void addAuthor(Author author) {
        this._authors.add(author);
    }

    /**
     * Adds a physical copy of this work to the library's inventory.
     *
     * <p>If the exact same {@link Copy} instance is already registered,
     * it will not be added again.</p>
     *
     * @param copy the {@link Copy} instance to add; must not be {@code null}
     */
    public void addCopy(Copy copy) {
        if (!this._copies.contains(copy))
            this._copies.add(copy);
    }

    // -------------------------------------------------------------------------
    // Queries
    // -------------------------------------------------------------------------

    /**
     * Filters the physical copies of this work by their current condition.
     *
     * @param state the {@link State} to filter by; must not be {@code null}
     * @return a new {@link List} containing all {@link Copy} instances whose
     *         current state matches {@code state}; never {@code null},
     *         but may be empty
     */
    public List<Copy> getCopiesByState(State state) {
        return this._copies.stream()
                .filter(c -> c.isState(state))
                .collect(Collectors.toList());
    }

    /**
     * Returns whether the given string matches this work's editor.
     *
     * @param editor the editor name to compare against; must not be {@code null}
     * @return {@code true} if the names match; {@code false} otherwise
     */
    public Boolean isEditor(String editor) {
        return this._editor.equals(editor);
    }

    /**
     * Returns whether the given {@link Author} is credited on this work.
     *
     * @param author the author to look up; must not be {@code null}
     * @return {@code true} if the author is credited; {@code false} otherwise
     */
    public Boolean isAuthor(Author author) {
        return this._authors.contains(author);
    }

    /**
     * Returns whether the given string matches this work's category code.
     *
     * @param category the category code (call number / "cote") to compare against;
     *                 must not be {@code null}
     * @return {@code true} if the codes match; {@code false} otherwise
     */
    public Boolean isCategory(String category) {
        return this._category.equals(category);
    }

    /**
     * Returns whether the given string matches this work's title exactly.
     *
     * @param title the title string to compare against; must not be {@code null}
     * @return {@code true} if the titles match; {@code false} otherwise
     */
    public Boolean isTitle(String title) {
        return this._title.equals(title);
    }

    // -------------------------------------------------------------------------
    // Getters
    // -------------------------------------------------------------------------

    /**
     * Returns the title of this work.
     *
     * @return the title; never {@code null}
     */
    public String getTitle() {
        return this._title;
    }

    /**
     * Returns the classification category (call number / "cote") of this work.
     *
     * @return the category string; never {@code null}
     */
    public String getCategory() {
        return this._category;
    }

    /**
     * Returns the editor or publishing house of this work.
     *
     * @return the editor name; never {@code null}
     */
    public String getEditor() {
        return this._editor;
    }

    /**
     * Returns the official publication date of this work.
     *
     * @return a defensive copy of the publication date; never {@code null}
     */
    public Date getPublicationDate() {
        return new Date(this._publicationDate.getTime());
    }

    /**
     * Returns the {@link Bibliotheque} instance responsible for managing this work.
     *
     * @return the managing library instance; never {@code null}
     */
    public Bibliotheque getHandler() {
        return this._handler;
    }

    /**
     * Returns an unmodifiable view of all physical copies of this work
     * currently registered in the library's inventory.
     *
     * <p>Use {@link #addCopy(Copy)} to register a new copy.</p>
     *
     * @return an unmodifiable {@link List} of {@link Copy} instances;
     *         never {@code null}, but may be empty
     */
    public List<Copy> getCopies() {
        return Collections.unmodifiableList(this._copies);
    }

    /**
     * Returns an unmodifiable view of all authors credited on this work.
     *
     * <p>Use {@link #addAuthor(Author)} to credit a new author.</p>
     *
     * @return an unmodifiable {@link Set} of {@link Author} instances;
     *         never {@code null}, but may be empty
     */
    public Set<Author> getAuthors() {
        return Collections.unmodifiableSet(this._authors);
    }

    /**
     * Returns an unmodifiable view of all events associated with this work.
     *
     * <p>Use {@link #addEvenement(Evenement)} to register a new event.</p>
     *
     * @return an unmodifiable {@link Set} of {@link Evenement} instances;
     *         never {@code null}, but may be empty
     */
    public Set<Evenement> getEvenements() {
        return Collections.unmodifiableSet(this._evenements);
    }
}
