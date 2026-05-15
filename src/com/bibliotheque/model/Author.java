package com.bibliotheque.model;

import com.bibliotheque.errors.RegexFormatError;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents an author within the library system.
 * 
 * <p>Uses {@link ValidationUtils} for consistent data integrity across the system.</p>
 *
 * @version 1.3
 */
public class Author {

    private String      _firstName;
    private String      _lastName;
    private Set<Work>   _works = new HashSet<>();

    /**
     * Constructs a new {@code Author} with the specified first name and last name.
     * 
     * @param firstName the first name of the author
     * @param lastName  the last name of the author
     * @throws IllegalArgumentException if validation fails via ValidationUtils
     */
    public Author(String firstName, String lastName) throws IllegalArgumentException {
        try {
            setFirstName(firstName);
            setLastName(lastName);
        } catch (RegexFormatError e) {
            throw new IllegalArgumentException(
                    "Invalid author name format: " + e.getMessage(), e);
        }
    }

    // -------------------------------------------------------------------------
    // Mutators using ValidationUtils
    // -------------------------------------------------------------------------

    /**
     * Sets the first name of the author after validating its format.
     * 
     * @param firstName the new first name to assign
     * @throws RegexFormatError if the first name format is invalid according to {@link ValidationUtils#isFirstNameValid(String)}
     */
    public void setFirstName(String firstName) throws RegexFormatError {
        if (ValidationUtils.isFirstNameValid(firstName)) {
            this._firstName = firstName;
        } else {
            throw new RegexFormatError("Format du prénom invalide selon ValidationUtils");
        }
    }

    /**
     * Sets the last name of the author after validating its format.
     * 
     * @param lastName the new last name to assign
     * @throws RegexFormatError if the last name format is invalid according to {@link ValidationUtils#isLastNameValid(String)}
     */
    public void setLastName(String lastName) throws RegexFormatError {
        if (ValidationUtils.isLastNameValid(lastName)) {
            this._lastName = lastName;
        } else {
            throw new RegexFormatError("Format du nom invalide selon ValidationUtils");
        }
    }

    /**
     * Adds a literary work to the collection of works associated with this author.
     * 
     * @param work the {@link Work} to add
     */
    public void addWork(Work work) {
        this._works.add(work);
    }

    // -------------------------------------------------------------------------
    // Queries using Normalization
    // -------------------------------------------------------------------------

    /**
     * Compares the provided name with the author's first name using normalization.
     * 
     * @param firstName the first name to compare
     * @return {@code true} if the normalized first names match, otherwise {@code false}
     */
    public Boolean isFirstName(String firstName) {
        return ValidationUtils.normalize(this._firstName)
               .equals(ValidationUtils.normalize(firstName));
    }

    /**
     * Compares the provided name with the author's last name using normalization.
     * 
     * @param lastName the last name to compare
     * @return {@code true} if the normalized last names match, otherwise {@code false}
     */
    public Boolean isLastName(String lastName) {
        return ValidationUtils.normalize(this._lastName)
               .equals(ValidationUtils.normalize(lastName));
    }

    // -------------------------------------------------------------------------
    // Getters
    // -------------------------------------------------------------------------

    /**
     * Returns the first name of the author.
     * 
     * @return the author's first name
     */
    public String getFirstName() {
        return this._firstName;
    }

    /**
     * Returns the last name of the author.
     * 
     * @return the author's last name
     */
    public String getLastName() {
        return this._lastName;
    }

    /**
     * Returns an unmodifiable view of the set of works associated with this author.
     * 
     * @return an unmodifiable {@link Set} containing the author's {@link Work} instances
     */
    public Set<Work> getWorks() {
        return Collections.unmodifiableSet(this._works);
    }
}