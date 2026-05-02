package com.bibliotheque.model;

import com.bibliotheque.errors.RegexFormatError;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents an author within the library system.
 *
 * <p>An {@code Author} holds identity information (first name and surname) and
 * maintains the set of {@link Work} instances they are credited on. Both name
 * fields are validated against a regex pattern on assignment to enforce a
 * consistent lowercase format that supports compound names, hyphenation,
 * nobility particles, and apostrophes.</p>
 *
 * <p>Name fields are mutable — they can be updated after construction via
 * {@link #setName(String)} and {@link #setSurname(String)} — but both setters
 * enforce the same format rules as the constructor.</p>
 *
 * <p>The set of works is exposed as an unmodifiable view. Use
 * {@link #addWork(Work)} to register a new entry.</p>
 *
 * @see Work
 * @see RegexFormatError
 *
 * @version 1.1
 */
public class Author {

    private String      _authorSurname;
    private String      _authorName;
    private Set<Work>   _works = new HashSet<>();

    /**
     * Constructs a new {@code Author} with the specified surname and first name.
     *
     * <p>Both values are validated via their respective setters
     * ({@link #setSurname(String)}, {@link #setName(String)}).
     * If either value fails format validation, an {@link IllegalArgumentException}
     * is thrown and the object is left in an uninitialised state — it should
     * not be used further.</p>
     *
     * @param authSurname the surname of the author; must match
     *                    {@code [a-z]+'?[a-z]*( [a-z]+'?[a-z]*)*(-[a-z]+'?[a-z]*)*}
     *                    (e.g. {@code "dupont"}, {@code "de la rochefoucauld"},
     *                    {@code "d'alembert"}); must not be {@code null}
     * @param authName    the first name of the author; must match
     *                    {@code [a-z]{1,}[a-z ]{0,}[-]{0,1}[a-z]{0,}}
     *                    (e.g. {@code "jean"}, {@code "jean-pierre"},
     *                    {@code "marie claire"}); must not be {@code null}
     *
     * @throws IllegalArgumentException if either {@code authSurname} or
     *                                  {@code authName} does not match its
     *                                  expected format
     */
    public Author(String authSurname, String authName) throws IllegalArgumentException {
        try {
            setName(authName);
            setSurname(authSurname);
        } catch (RegexFormatError e) {
            throw new IllegalArgumentException(
                    "Invalid author name format: " + e.getMessage(), e);
        }
    }

    // -------------------------------------------------------------------------
    // Mutators
    // -------------------------------------------------------------------------

    /**
     * Updates the author's first name after validating its format.
     *
     * <p>The name must consist of at least one lowercase letter, optionally
     * followed by lowercase letters or spaces, and optionally ending with a
     * single hyphen and additional lowercase letters. This supports compound
     * and hyphenated first names such as {@code "jean-pierre"} or
     * {@code "marie claire"}.</p>
     *
     * <p>Accepted pattern: {@code [a-z]{1,}[a-z ]{0,}[-]{0,1}[a-z]{0,}}</p>
     *
     * @param name the first name to assign; must not be {@code null}
     *
     * @throws RegexFormatError if {@code name} does not match the expected pattern
     */
    public void setName(String name) throws RegexFormatError {
        if (name.matches("[a-z]{1,}[a-z ]{0,}[-]{0,1}[a-z]{0,}"))
            this._authorName = name;
        else
            throw new RegexFormatError("test");
    }

    /**
     * Updates the author's surname after validating its format.
     *
     * <p>The surname must start with at least one lowercase letter and
     * supports the following forms:</p>
     * <ul>
     *   <li>Simple surnames: {@code "dupont"}</li>
     *   <li>Hyphenated surnames: {@code "dupont-martin"}</li>
     *   <li>Surnames with nobility particles: {@code "de courcelle"},
     *       {@code "de la rochefoucauld"}</li>
     *   <li>Surnames with apostrophes: {@code "d'alembert"},
     *       {@code "d'artagnan"}</li>
     * </ul>
     *
     * <p>Accepted pattern:
     * {@code [a-z]+'?[a-z]*( [a-z]+'?[a-z]*)*(-[a-z]+'?[a-z]*)*}</p>
     *
     * @param surname the surname to assign; must not be {@code null}
     *
     * @throws RegexFormatError if {@code surname} does not match the expected
     *                          pattern
     */
    public void setSurname(String surname) throws RegexFormatError {
        if (surname.matches("[a-z]+'?[a-z]*( [a-z]+'?[a-z]*)*(-[a-z]+'?[a-z]*)*"))
            this._authorSurname = surname;
        else
            throw new RegexFormatError("test");
    }

    /**
     * Adds a work to this author's bibliography.
     *
     * <p>Duplicate entries are silently ignored since the underlying collection
     * is a {@link HashSet}.</p>
     *
     * @param work the {@link Work} to credit to this author;
     *             must not be {@code null}
     */
    public void addWork(Work work) {
        this._works.add(work);
    }

    // -------------------------------------------------------------------------
    // Queries
    // -------------------------------------------------------------------------

    /**
     * Returns whether the given string matches this author's first name exactly.
     *
     * <p>Comparison is case-sensitive and uses {@link String#equals(Object)}.
     * Since names are stored in lowercase, the provided string should also
     * be lowercase to get a meaningful result.</p>
     *
     * @param name the string to compare against the stored first name;
     *             must not be {@code null}
     * @return {@code true} if {@code name} equals the author's first name;
     *         {@code false} otherwise
     */
    public Boolean isName(String name) {
        return this._authorName.equals(name);
    }

    /**
     * Returns whether the given string matches this author's surname exactly.
     *
     * <p>Comparison is case-sensitive and uses {@link String#equals(Object)}.
     * Since surnames are stored in lowercase, the provided string should also
     * be lowercase to get a meaningful result.</p>
     *
     * @param surname the string to compare against the stored surname;
     *                must not be {@code null}
     * @return {@code true} if {@code surname} equals the author's surname;
     *         {@code false} otherwise
     */
    public Boolean isSurname(String surname) {
        return this._authorSurname.equals(surname);
    }

    // -------------------------------------------------------------------------
    // Getters
    // -------------------------------------------------------------------------

    /**
     * Returns the author's first name.
     *
     * @return the first name string, as stored after the last successful call
     *         to {@link #setName(String)}; never {@code null} after construction
     */
    public String getName() {
        return this._authorName;
    }

    /**
     * Returns the author's surname.
     *
     * @return the surname string, as stored after the last successful call
     *         to {@link #setSurname(String)}; never {@code null} after construction
     */
    public String getSurname() {
        return this._authorSurname;
    }

    /**
     * Returns an unmodifiable view of all works credited to this author.
     *
     * <p>The returned set reflects the current state of the bibliography but
     * cannot be modified directly — use {@link #addWork(Work)} to register
     * a new entry.</p>
     *
     * @return an unmodifiable {@link Set} of {@link Work} objects;
     *         never {@code null}, but may be empty
     */
    public Set<Work> getWorks() {
        return Collections.unmodifiableSet(this._works);
    }
}
