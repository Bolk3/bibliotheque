package com.bibliotheque.model;

import com.bibliotheque.errors.RegexFormatError;

/**
 * Abstract base class representing a user within the library system.
 *
 * <p>This class serves as the parent for all types of users (e.g., members, 
 * librarians). It centralises common identity information — first name, 
 * last name, and email address — and ensures that all data conforms to 
 * the system's validation rules via {@link ValidationUtils}.</p>
 *
 * <p>Subclasses inherit the validation logic and the association with the 
 * managing {@link Bibliotheque} instance.</p>
 *
 * @see Bibliotheque
 * @see ValidationUtils
 * @see RegexFormatError
 *
 * @version 1.0
 */
public abstract class User {

    private String          _lastName;
    private String          _firstName;
    private String          _email;
    private Bibliotheque    _handler;

    /**
     * Constructs a new user with validated identity metadata.
     *
     * <p>This constructor uses the defined setters to ensure that the provided 
     * names and email address conform to the required formats. If any validation 
     * fails, an {@link IllegalArgumentException} is thrown.</p>
     *
     * @param firstname the user's first name; must match valid regex format
     * @param lastname  the user's last name; must match valid regex format
     * @param email     the user's email address; must match valid email format
     * @param handler   the {@link Bibliotheque} instance managing this user; 
     *                  must not be {@code null}
     * @throws IllegalArgumentException if any of the input strings fail 
     *                                  validation or are null
     */
    public User(
        String firstname,
        String lastname,
        String email,
        Bibliotheque handler
    ) throws IllegalArgumentException {
        try {
            setFirstName(firstname);
            setLastName(lastname);
            setEmail(email);
            this._handler = handler;
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    // -------------------------------------------------------------------------
    // Mutators
    // -------------------------------------------------------------------------

    /**
     * Updates the user's first name after validation.
     *
     * @param fn the new first name to set
     * @throws RegexFormatError if the provided string does not meet 
     *                          validation criteria
     */
    public void setFirstName(String fn) throws RegexFormatError {
        if (ValidationUtils.isFirstNameValid(fn)) {
            this._firstName = fn;
        } else {
             return;
        }
    }

    /**
     * Updates the user's last name after validation.
     *
     * @param ln the new last name to set
     * @throws RegexFormatError if the provided string does not meet 
     *                          validation criteria
     */
    public void setLastName(String ln) throws RegexFormatError {
        if (ValidationUtils.isLastNameValid(ln)) {
            this._lastName = ln;
        } else {
             return;
        }
    }

    /**
     * Updates the user's email address after validation.
     *
     * @param mail the new email address to set
     * @throws RegexFormatError if the provided string does not meet 
     *                          email format criteria
     */
    public void setEmail(String mail) throws RegexFormatError {
        if (ValidationUtils.isEmailValid(mail)) {
            this._email = mail;
        } else {
             return;
        }
    }

    // -------------------------------------------------------------------------
    // Queries
    // -------------------------------------------------------------------------

    /**
     * Compares the provided string with the user's first name using 
     * normalization.
     *
     * @param fn the first name to compare against
     * @return {@code true} if the normalized names match; {@code false} otherwise
     * @throws RegexFormatError if the input string format is invalid
     */
    public Boolean isFirstName(String fn) throws RegexFormatError {
        if (ValidationUtils.isFirstNameValid(fn)) {
            return ValidationUtils.normalize(this._firstName).equals(ValidationUtils.normalize(fn));
        } else {
             return false;
        }
    }

    /**
     * Compares the provided string with the user's last name using 
     * normalization.
     *
     * @param ln the last name to compare against
     * @return {@code true} if the normalized names match; {@code false} otherwise
     * @throws RegexFormatError if the input string format is invalid
     */
    public Boolean isLastName(String ln) throws RegexFormatError {
        if (ValidationUtils.isLastNameValid(ln)) { 
            return ValidationUtils.normalize(this._lastName).equals(ValidationUtils.normalize(ln));
        } else {
             return false;
        }
    }

    /**
     * Compares the provided string with the user's email using email-specific 
     * normalization.
     *
     * @param email Str the email string to compare against
     * @return {@code true} if the normalized emails match; {@code false} otherwise
     * @throws RegexFormatError if the input string format is invalid
     */
    public Boolean isEmail(String email) throws RegexFormatError {
        if (ValidationUtils.isEmailValid(email)) {
            return ValidationUtils.normalizeEmail(this._email).equals(ValidationUtils.normalizeEmail(email));
        } else {
             return false;
        }
    }

    // -------------------------------------------------------------------------
    // Getters
    // -------------------------------------------------------------------------

    /**
     * Returns the user's first name.
     *
     * @return the first name; never {@code null}
     */
    public String getFirstName() {
        return (this._firstName);
    }

    /**
     * Returns the user's last name.
     *
     * @return the last name; never {@code null}
     */
    public String getLastName() {
        return (this._lastName);
    }

    /**
     * Returns the user's email address.
     *
     * @return the email string; never {@code null}
     */
    public String getEmail() {
        return (this._email);
    }

    /**
     * Returns the {@link Bibliotheque} instance responsible for managing this user.
     *
     * @return the managing library instance; never {@code null}
     */
    public Bibliotheque getHandler() {
        return (this._handler);
    }
}