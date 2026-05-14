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
     * Constructs a new {@code Author}.
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

    public void setFirstName(String firstName) throws RegexFormatError {
        if (ValidationUtils.isFirstNameValid(firstName)) {
            this._firstName = firstName;
        } else {
            throw new RegexFormatError("Format du prénom invalide selon ValidationUtils");
        }
    }

    public void setLastName(String lastName) throws RegexFormatError {
        if (ValidationUtils.isLastNameValid(lastName)) {
            this._lastName = lastName;
        } else {
            throw new RegexFormatError("Format du nom invalide selon ValidationUtils");
        }
    }

    public void addWork(Work work) {
        this._works.add(work);
    }

    // -------------------------------------------------------------------------
    // Queries using Normalization
    // -------------------------------------------------------------------------

    /**
     * Compares the provided name with the author's first name using normalization.
     */
    public Boolean isFirstName(String firstName) {
        return ValidationUtils.normalize(this._firstName)
               .equals(ValidationUtils.normalize(firstName));
    }

    /**
     * Compares the provided name with the author's last name using normalization.
     */
    public Boolean isLastName(String lastName) {
        return ValidationUtils.normalize(this._lastName)
               .equals(ValidationUtils.normalize(lastName));
    }

    // -------------------------------------------------------------------------
    // Getters
    // -------------------------------------------------------------------------

    public String getFirstName() {
        return this._firstName;
    }

    public String getLastName() {
        return this._lastName;
    }

    public Set<Work> getWorks() {
        return Collections.unmodifiableSet(this._works);
    }
}