package com.bibliotheque.model;

import java.util.Collections;
import java.util.List;
import java.util.Vector;

/**
 * Represents a library member (subscriber).
 *
 * <p>A {@code Member} can borrow copies, participate in events, and may incur
 * financial penalties for late returns or damages. If the member is blocked,
 * they lose their borrowing privileges.</p>
 *
 * <p>Access control for sensitive operations (penalties, blocking) is managed
 * via the {@link Settings} class.</p>
 *
 * @see User
 * @see Borrow
 * @see Event
 * @see Settings
 *
 * @version 1.2
 */
public class Member extends User {

    private double                  _penalty;
    private boolean                 _isBlocked;
    private final Vector<Borrow>    _borrows = new Vector<>();
    private final Vector<Event> _participatedEvents = new Vector<>();

    /**
     * Constructs a new library member with default status.
     *
     * @param firstName the member's first name
     * @param lastName  the member's last name
     * @param email     the member's contact email
     * @param handler   the {@link Bibliotheque} instance managing this user
     * @throws IllegalArgumentException if identity validation fails
     */
    public Member(
        String firstName,
        String lastName,
        String email,
        Bibliotheque handler
    ) throws IllegalArgumentException {
        super(firstName, lastName, email, handler);
        this._penalty = 0.0;
        this._isBlocked = false;
    }

    // -------------------------------------------------------------------------
    // Operations
    // -------------------------------------------------------------------------

    /**
     * Increments the member's financial penalty.
     *
     * <p>Requires {@link Settings#PERM_ADMIN_METADATA} permission level.</p>
     *
     * @param value   the amount to add to the current penalty
     * @param handler the librarian authorizing the penalty
     * @throws IllegalStateException if the librarian lacks sufficient permissions
     */
    public void addPenalty(double value, Librarian handler) {
        if (!Settings.hasAccess(handler, Settings.PERM_ADMIN_METADATA)) {
            throw new IllegalStateException("Permission insuffisante pour appliquer une pénalité.");
        }
        if (value > 0) {
            this._penalty += value;
        }
    }

    /**
     * Records a new borrowing transaction for this member.
     *
     * @param borrow the {@link Borrow} record to add; must not be {@code null}
     */
    public void addBorrow(Borrow borrow) {
        if (borrow != null && !this._borrows.contains(borrow)) {
            this._borrows.add(borrow);
        }
    }

    /**
     * Registers the member's participation in an event.
     *
     * @param event the {@link Event} to join; must not be {@code null}
     */
    public void joinEvent(Event event) {
        if (event != null && !this._participatedEvents.contains(event)) {
            this._participatedEvents.add(event);
        }
    }

    // -------------------------------------------------------------------------
    // Mutators
    // -------------------------------------------------------------------------

    /**
     * Sets the blocked status of the member.
     *
     * <p>Requires {@link Settings#PERM_ADMIN_METADATA} permission level.</p>
     *
     * @param status  {@code true} to block the member, {@code false} to unblock
     * @param handler the librarian authorizing the status change
     * @throws IllegalStateException if the librarian lacks sufficient permissions
     */
    public void setBlocked(boolean status, Librarian handler) {
        if (Settings.hasAccess(handler, Settings.PERM_ADMIN_METADATA)) {
            this._isBlocked = status;
        } else {
            throw new IllegalStateException("Permission insuffisante pour modifier le statut du membre.");
        }
    }

    /**
     * Resets the member's penalty to zero.
     *
     * <p>Requires {@link Settings#PERM_ADMIN_METADATA} permission level.</p>
     *
     * @param handler the librarian validating the payment/reset
     * @throws IllegalStateException if the librarian lacks sufficient permissions
     */
    public void resetPenalty(Librarian handler) {
        if (Settings.hasAccess(handler, Settings.PERM_ADMIN_METADATA)) {
            this._penalty = 0.0;
        } else {
            throw new IllegalStateException("Permission insuffisante pour remettre à zéro les pénalités.");
        }
    }

    // -------------------------------------------------------------------------
    // Getters & Queries
    // -------------------------------------------------------------------------

    /**
     * Returns the total amount of penalties accrued by the member.
     *
     * @return the penalty amount
     */
    public double getPenalty() {
        return this._penalty;
    }

    /**
     * Checks if the member is currently blocked from borrowing.
     *
     * @return {@code true} if blocked; {@code false} otherwise
     */
    public boolean isBlocked() {
        return this._isBlocked;
    }

    /**
     * Returns an unmodifiable list of all borrowing transactions associated with this member.
     *
     * @return a {@link List} of {@link Borrow} records; never {@code null}
     */
    public List<Borrow> getBorrows() {
        return Collections.unmodifiableList(this._borrows);
    }

    /**
     * Returns an unmodifiable list of events the member has participated in.
     *
     * @return a {@link List} of {@link Event} objects; never {@code null}
     */
    public List<Event> getParticipatedEvents() {
        return Collections.unmodifiableList(this._participatedEvents);
    }
}