package com.bibliotheque.model;

import com.bibliotheque.errors.RegexFormatError;

/**
 * Abstract base class representing a user within the library system.
 */
public abstract class User {

    private String _lastName;
    private String _firstName;
    private String _email;
    private final Bibliotheque _handler; // Rendu final car le handler ne change pas au cours de la vie du user

    /**
     * Constructs a new user with validated identity metadata.
     *
     * @param firstname the user's first name
     * @param lastname  the user's last name
     * @param email     the user's email address
     * @param handler   the managing library instance
     * @throws IllegalArgumentException if any validation fails
     */
    protected User(String firstname, String lastname, String email, Bibliotheque handler) {
        if (handler == null) {
            throw new IllegalArgumentException("The managing library handler cannot be null.");
        }
        try {
            setFirstName(firstname);
            setLastName(lastname);
            setEmail(email);
            this._handler = handler;
        } catch (RegexFormatError e) {
            throw new IllegalArgumentException("User initialization failed: " + e.getMessage(), e);
        }
    }

    // -------------------------------------------------------------------------
    // Mutators
    // -------------------------------------------------------------------------

    public void setFirstName(String fn) throws RegexFormatError {
        if (!ValidationUtils.isFirstNameValid(fn)) {
            throw new RegexFormatError("Invalid first name format: " + fn);
        }
        this._firstName = fn;
    }

    public void setLastName(String ln) throws RegexFormatError {
        if (!ValidationUtils.isLastNameValid(ln)) {
            throw new RegexFormatError("Invalid last name format: " + ln);
        }
        this._lastName = ln;
    }

    public void setEmail(String mail) throws RegexFormatError {
        if (!ValidationUtils.isEmailValid(mail)) {
            throw new RegexFormatError("Invalid email format: " + mail);
        }
        this._email = mail;
    }

    // -------------------------------------------------------------------------
    // Queries (Changement des types de retour en types primitifs 'boolean')
    // -------------------------------------------------------------------------

    public boolean isFirstName(String fn) {
        if (fn == null || !ValidationUtils.isFirstNameValid(fn)) {
            return false;
        }
        return ValidationUtils.normalize(this._firstName).equals(ValidationUtils.normalize(fn));
    }

    public boolean isLastName(String ln) {
        if (ln == null || !ValidationUtils.isLastNameValid(ln)) {
            return false;
        }
        return ValidationUtils.normalize(this._lastName).equals(ValidationUtils.normalize(ln));
    }

    public boolean isEmail(String email) {
        if (email == null || !ValidationUtils.isEmailValid(email)) {
            return false;
        }
        return ValidationUtils.normalizeEmail(this._email).equals(ValidationUtils.normalizeEmail(email));
    }

    // -------------------------------------------------------------------------
    // Getters
    // -------------------------------------------------------------------------

    public String getFirstName() { return this._firstName; }
    public String getLastName() { return this._lastName; }
    public String getEmail() { return this._email; }
    public Bibliotheque getHandler() { return this._handler; }
}