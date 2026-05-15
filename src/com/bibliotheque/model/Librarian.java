package com.bibliotheque.model;

import java.util.Collections;
import java.util.List;
import java.util.Vector;

/**
 * Represents a library staff member responsible for administrative tasks.
 *
 * <p>A {@code Librarian} is a specialized {@link User} who has the authority 
 * to validate borrowing transactions and administrative stamps (extensions, returns). 
 * This class tracks the history of all actions performed by the librarian to 
 * ensure accountability within the system.</p>
 *
 * <p>Each librarian is defined by a specific position title and a numerical 
 * permission level determining their access rights.</p>
 *
 * @see User
 * @see Borrow
 * @see Stamp
 * @see ValidationUtils
 *
 * @version 1.2
 */
public class Librarian extends User {

    private String          _position;
    private int             _permission;
    private Vector<Borrow>  _validatedBorrows = new Vector<>();
    private Vector<Stamp>   _validatedStamps  = new Vector<>();

    /**
     * Constructs a new librarian with full identity and professional metadata.
     *
     * <p>Initializes the librarian's profile by calling the {@link User} constructor 
     * and setting specific professional attributes.</p>
     *
     * @param firstName  the librarian's first name
     * @param lastName   the librarian's last name
     * @param email      the librarian's professional email
     * @param handler    the {@link Bibliotheque} instance managing this user
     * @param position   the official job title or position
     * @param permission the numerical permission level
     * @throws IllegalArgumentException if user identity validation fails
     */
    public Librarian(
        String firstName,
        String lastName,
        String email,
        Bibliotheque handler,
        String position,
        int permission
    ) throws IllegalArgumentException {
        super(firstName, lastName, email, handler);
        setPermission(permission);
        setPosition(position);
    }

    // -------------------------------------------------------------------------
    // Mutators
    // -------------------------------------------------------------------------

    /**
     * Updates the librarian's permission level.
     *
     * @param permission the new numerical permission level
     */
    public void setPermission(int permission) {
        this._permission = permission;
    }

    /**
     * Updates the librarian's job position title.
     *
     * @param position the new position string
     */
    public void setPosition(String position) {
        this._position = position;
    }

    /**
     * Registers a borrowing transaction as validated by this librarian.
     *
     * <p>Duplicate entries are ignored to maintain history integrity.</p>
     *
     * @param borrow the {@link Borrow} record to add; must not be {@code null}
     */
    public void addValidatedBorrow(Borrow borrow) {
        if (borrow != null && !this._validatedBorrows.contains(borrow)) {
            this._validatedBorrows.add(borrow);
        }
    }

    /**
     * Registers a formal stamp (extension or return) as validated by this librarian.
     *
     * <p>Duplicate entries are ignored to maintain history integrity.</p>
     *
     * @param stamp the {@link Stamp} record to add; must not be {@code null}
     */
    public void addValidatedStamp(Stamp stamp) {
        if (stamp != null && !this._validatedStamps.contains(stamp)) {
            this._validatedStamps.add(stamp);
        }
    }

    // -------------------------------------------------------------------------
    // Queries
    // -------------------------------------------------------------------------

    /**
     * Compares the provided level with the librarian's current permission level.
     *
     * @param permission the permission level to check
     * @return {@code true} if the levels match; {@code false} otherwise
     */
    public boolean isPermission(int permission) {
        return this._permission == permission;
    }

    /**
     * Compares the provided string with the librarian's position title using normalization.
     *
     * @param position the position title to compare against
     * @return {@code true} if the titles match after normalization; {@code false} otherwise
     */
    public boolean isPosition(String position) {
        return ValidationUtils.normalize(this._position)
                .equals(ValidationUtils.normalize(position));
    }

    // -------------------------------------------------------------------------
    // Getters
    // -------------------------------------------------------------------------

    /**
     * Returns the librarian's numerical permission level.
     *
     * @return the permission level
     */
    public int getPermission() {
        return this._permission;
    }

    /**
     * Returns the librarian's official job position title.
     *
     * @return the position string
     */
    public String getPosition() {
        return this._position;
    }

    /**
     * Returns an unmodifiable view of all stamps validated by this librarian.
     *
     * @return an unmodifiable {@link List} of {@link Stamp} objects; never {@code null}
     */
    public List<Stamp> getValidatedStamps() {
        return Collections.unmodifiableList(this._validatedStamps);
    }

    /**
     * Returns an unmodifiable view of all borrowing transactions validated by this librarian.
     *
     * @return an unmodifiable {@link List} of {@link Borrow} objects; never {@code null}
     */
    public List<Borrow> getValidatedBorrows() {
        return Collections.unmodifiableList(this._validatedBorrows);
    }

    /**
     * Returns a string representation of the librarian, consisting of their full name.
     * 
     * @return the librarian's first name followed by their last name
     */
    @Override
    public String toString() {
        return this.getFirstName() + " " + this.getLastName();
    }
}