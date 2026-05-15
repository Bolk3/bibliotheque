package com.bibliotheque.errors;

/**
 * Exception thrown when an input string fails to match the required 
 * regular expression format policy (e.g., malformed names or emails).
 *
 * @see com.bibliotheque.model.User
 * @see com.bibliotheque.model.ValidationUtils
 * 
 * @version 1.0
 */
public class RegexFormatError extends Exception {

    /**
     * Constructs a new RegexFormatError with the specified detail message.
     *
     * @param errorMessage the detail message explaining the validation failure
     */
    public RegexFormatError(String errorMessage) {
        super(errorMessage);
    }
}