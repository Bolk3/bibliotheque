package com.bibliotheque.model;

import java.util.Date;

/**
 * Abstract base class representing a formal record — or "stamp" — within
 * a borrowing lifecycle.
 *
 * <p>A {@code Stamp} acts as an immutable audit log entry that captures three
 * core pieces of information: the exact moment an action occurred, the
 * {@link Bibliothecaire} who authorised it, and the {@link Borrow} transaction
 * it belongs to.</p>
 *
 * <p>This class is extended by concrete stamp types that each represent a
 * distinct event in the borrowing lifecycle:</p>
 * <ul>
 *   <li>{@link ExtensionStamp} — records a due-date extension</li>
 *   <li>{@link ReturnStamp} — records the return of a borrowed copy</li>
 * </ul>
 *
 * <p>All fields are immutable once the stamp is created. No subclass should
 * expose setters for the attributes declared here.</p>
 *
 * @see ExtensionStamp
 * @see ReturnStamp
 * @see Borrow
 * @see Bibliothecaire
 *
 * @version 1.1
 */
public abstract class Stamp {

    private final Date              _timestamp;
    private final Bibliothecaire    _validatedBy;
    private final Borrow            _reference;

    /**
     * Initialises the core attributes of a transaction stamp.
     *
     * <p>This constructor is intended to be called by subclass constructors
     * via {@code super(...)}. The timestamp is typically {@code new Date()}
     * at the moment the event occurs, but is accepted as a parameter to keep
     * the class testable and flexible.</p>
     *
     * @param timestamp   the exact date and time the event occurred;
     *                    must not be {@code null}
     * @param validatedBy the {@link Bibliothecaire} who authorised this action;
     *                    must not be {@code null}
     * @param reference   the {@link Borrow} transaction this stamp belongs to;
     *                    must not be {@code null}
     */
    public Stamp(Date timestamp, Bibliothecaire validatedBy, Borrow reference) {
        this._timestamp   = timestamp;
        this._validatedBy = validatedBy;
        this._reference   = reference;
    }

    // -------------------------------------------------------------------------
    // Getters
    // -------------------------------------------------------------------------

    /**
     * Returns the date and time at which this stamp was created.
     *
     * <p>A defensive copy is returned to prevent external mutation of the
     * internal timestamp, since {@link Date} is mutable.</p>
     *
     * @return a copy of the event timestamp; never {@code null}
     */
    public Date getTimestamp() {
        return new Date(this._timestamp.getTime());
    }

    /**
     * Returns the librarian who authorised the action recorded by this stamp.
     *
     * @return the validating {@link Bibliothecaire}; never {@code null}
     */
    public Bibliothecaire getValidator() {
        return this._validatedBy;
    }

    /**
     * Returns the borrowing transaction this stamp is associated with.
     *
     * @return the linked {@link Borrow} record; never {@code null}
     */
    public Borrow getReference() {
        return this._reference;
    }
}
