package com.bibliotheque.errors;

/**
 * Exception thrown when a search query string does not meet the minimum length requirements.
 * <p>
 * This exception is used to prevent inefficient database or collection filtering 
 * when the provided search criteria are too vague (e.g., shorter than 3 characters).
 * </p>
 *
 * @version 1.0
 */
public class SearchStringTooSmall extends Exception{
    /**
     * Constructs a new exception with a specific detail message.
     *
     * @param errorMessage The detailed message describing the validation failure.
     */
    public SearchStringTooSmall (String errorMessage) {
        super(errorMessage);
    }
}
