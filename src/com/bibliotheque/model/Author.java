package com.bibliotheque.model;

import com.bibliotheque.erreur.RegexFormatError;
import java.util.*;

/**
 * Represents an author within the library system.
 * <p>
 * This class maintains the author's identity (first name and surname) and tracks
 * the collection of works they have contributed to. Names are validated against
 * a regex pattern upon assignment to ensure proper formatting (lowercase letters,
 * optional hyphens, spaces, and apostrophes where applicable).
 * </p>
 *
 * @version 1.0
 */
public class Author {
    private String      _authorSurname;
    private String      _authorName;
    private Set<Work>   _works = new HashSet<>();

    /**
     * Constructs a new {@code Author} with the specified surname and first name.
     * <p>
     * Both values are validated via their respective setters. If either value
     * fails the regex validation, an {@link IllegalArgumentException} is thrown.
     * The set of works is initialized as empty.
     * </p>
     *
     * @param authSurname  The surname of the author. Must match
     *                     {@code [a-z]+'?[a-z]*( [a-z]+'?[a-z]*)*(-[a-z]+'?[a-z]*)*}.
     * @param authName     The first name of the author. Must match
     *                     {@code [a-z]{1,}[a-z ]{0,}[-]{0,1}[a-z]{0,}}.
     * @throws IllegalArgumentException if either {@code authSurname} or {@code authName}
     *                                  does not match the expected format.
     */
    public Author(String authSurname, String authName) throws IllegalArgumentException {
        try {
            setName(authName);
            setSurname(authSurname);
        } catch (RegexFormatError e) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Sets the author's first name after validating its format.
     * <p>
     * The name must consist of at least one lowercase letter, optionally followed
     * by lowercase letters or spaces, and optionally ending with a hyphen and
     * additional lowercase letters (to support compound or hyphenated first names
     * such as {@code "jean-pierre"} or {@code "marie claire"}).
     * </p>
     * <p>
     * Accepted pattern: {@code [a-z]{1,}[a-z ]{0,}[-]{0,1}[a-z]{0,}}
     * </p>
     *
     * @param name  The first name to assign to the author.
     * @throws RegexFormatError if {@code name} does not match the expected pattern.
     */
    public void setName(String name) throws RegexFormatError {
        if (name.matches("[a-z]{1,}[a-z ]{0,}[-]{0,1}[a-z]{0,}"))
            this._authorName = name;
        else
            throw new RegexFormatError();
    }

    /**
     * Sets the author's surname after validating its format.
     * <p>
     * The surname must consist of at least one lowercase letter and supports:
     * </p>
     * <ul>
     *   <li>Simple surnames: {@code "dupont"}</li>
     *   <li>Hyphenated surnames: {@code "dupont-martin"}</li>
     *   <li>Surnames with nobility particles: {@code "de courcelle"}, {@code "de la rochefoucauld"}</li>
     *   <li>Surnames with apostrophes: {@code "d'alembert"}, {@code "d'artagnan"}</li>
     * </ul>
     * <p>
     * Accepted pattern: {@code [a-z]+'?[a-z]*( [a-z]+'?[a-z]*)*(-[a-z]+'?[a-z]*)*}
     * </p>
     *
     * @param surname  The surname to assign to the author.
     * @throws RegexFormatError if {@code surname} does not match the expected pattern.
     */
    public void setSurname(String surname) throws RegexFormatError {
        if (surname.matches("[a-z]+'?[a-z]*( [a-z]+'?[a-z]*)*(-[a-z]+'?[a-z]*)*"))
            this._authorSurname = surname;
        else
            throw new RegexFormatError();
    }

    /**
     * Returns the author's first name.
     *
     * @return The first name string, as stored after the last successful call
     *         to {@link #setName(String)}.
     */
    public String getName() { return (this._authorName); }

    /**
     * Returns the author's surname.
     *
     * @return The surname string, as stored after the last successful call
     *         to {@link #setSurname(String)}.
     */
    public String getSurname() { return (this._authorSurname); }

    /**
     * Verifies if the provided string matches the author's first name.
     * <p>
     * Comparison is case-sensitive and uses {@link String#equals(Object)}.
     * </p>
     *
     * @param n  The string to compare against the stored first name.
     * @return {@code true} if {@code n} equals the author's first name;
     *         {@code false} otherwise.
     */
    public Boolean isName(String n) { return (this._authorName.equals(n)); }

    /**
     * Verifies if the provided string matches the author's surname.
     * <p>
     * Comparison is case-sensitive and uses {@link String#equals(Object)}.
     * </p>
     *
     * @param n  The string to compare against the stored surname.
     * @return {@code true} if {@code n} equals the author's surname;
     *         {@code false} otherwise.
     */
    public Boolean isSurname(String n) { return (this._authorSurname.equals(n)); }

    /**
     * Adds a work to the author's bibliography.
     * <p>
     * Since the underlying collection is a {@link HashSet}, duplicate works
     * (as determined by {@link Work#equals(Object)}) will be silently ignored.
     * </p>
     *
     * @param w  The {@link Work} to add to the author's bibliography.
     */
    public void addWork(Work w) { this._works.add(w); }

    /**
     * Returns the complete set of works credited to this author.
     * <p>
     * The returned set is the internal collection — modifications to it will
     * directly affect the author's bibliography. Consider wrapping with
     * {@link Collections#unmodifiableSet(Set)} if immutability is required.
     * </p>
     *
     * @return A {@link Set} of {@link Work} objects associated with this author.
     *         The set may be empty but will never be {@code null}.
     */
    public Set<Work> getWorks() { return (this._works); }
}
