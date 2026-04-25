package com.bibliotheque.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Represents a physical copy of a specific {@link Work}.
 *
 * <p>A {@code Copy} is a concrete, borrowable instance of a {@link Work}. It tracks
 * the copy's current physical condition via a {@link State}, and maintains a complete
 * history of all associated {@link Borrow} transactions.</p>
 *
 * <p>At most one {@link Borrow} may be active at any given time. Attempting to add
 * a new borrowing while the copy is unavailable will throw an
 * {@link IllegalStateException}. Use {@link #isAvailable()} to check availability
 * before calling {@link #addBorrowing(Borrow)}.</p>
 *
 * @see Work
 * @see Borrow
 * @see State
 *
 * @version 1.1
 */
public class Copy {

    private final Work      _reference;
    private Set<Borrow>     _borrowings = new HashSet<>();
    private State           _state;

    /**
     * Constructs a new {@code Copy} of a work with an initial physical condition.
     *
     * @param state the initial condition of the copy (e.g., {@code State.NEW},
     *              {@code State.GOOD}, {@code State.DAMAGED}); must not be {@code null}
     * @param ref   the {@link Work} this copy is an instance of;
     *              must not be {@code null}
     */
    public Copy(State state, Work ref) {
        this._state     = state;
        this._reference = ref;
    }

    // -------------------------------------------------------------------------
    // Mutators
    // -------------------------------------------------------------------------

    /**
     * Records a new borrowing transaction for this copy.
     *
     * <p>The copy must be available (i.e., no active borrow) before a new
     * transaction can be added. Use {@link #isAvailable()} to verify first.</p>
     *
     * @param borrow the {@link Borrow} transaction to record;
     *               must not be {@code null}
     *
     * @throws IllegalStateException if the copy is already borrowed and
     *                               has not yet been returned
     */
    public void addBorrowing(Borrow borrow) throws IllegalStateException {
        if (!this.isAvailable())
            throw new IllegalStateException("Copy is already borrowed.");
        this._borrowings.add(borrow);
    }

    /**
     * Updates the physical condition of the copy.
     *
     * <p>This method is typically called automatically at the end of a
     * {@link Borrow} transaction when the copy is returned in a different
     * condition than it was borrowed — see
     * {@link Borrow#returnBook(String, Bibliothecaire)}.</p>
     *
     * @param state the new physical condition; must not be {@code null}
     */
    public void setState(State state) {
        this._state = state;
    }

    // -------------------------------------------------------------------------
    // Queries
    // -------------------------------------------------------------------------

    /**
     * Returns whether this copy is currently available to be borrowed.
     *
     * <p>A copy is considered available if none of its recorded borrowing
     * transactions are still active (i.e., all have been returned).</p>
     *
     * @return {@code true} if no active borrow exists; {@code false} otherwise
     */
    public Boolean isAvailable() {
        return this._borrowings.stream()
                .noneMatch(b -> !b.isReturned());
    }

    /**
     * Returns whether this copy is currently overdue.
     *
     * <p>Delegates to the active {@link Borrow}'s {@link Borrow#isLate()} method.
     * Returns {@code false} if the copy is not currently borrowed.</p>
     *
     * @return {@code true} if there is an active borrow and it is past its
     *         expected return date; {@code false} otherwise
     */
    public Boolean isLate() {
        return this.getCurrentBorrow()
                .map(Borrow::isLate)
                .orElse(false);
    }

    /**
     * Checks whether the copy's current physical condition matches the given state.
     *
     * @param state the {@link State} to compare against; must not be {@code null}
     * @return {@code true} if the copy's current state equals {@code state};
     *         {@code false} otherwise
     */
    public Boolean isState(State state) {
        return this._state.equals(state);
    }

    // -------------------------------------------------------------------------
    // Getters
    // -------------------------------------------------------------------------

    /**
     * Returns the {@link Work} that this physical copy represents.
     *
     * @return the associated {@link Work}; never {@code null}
     */
    public Work getReference() {
        return this._reference;
    }

    /**
     * Returns the current physical condition of this copy.
     *
     * @return the current {@link State}; never {@code null}
     */
    public State getState() {
        return this._state;
    }

    /**
     * Returns the currently active borrowing transaction, if any.
     *
     * <p>A borrow is considered active if {@link Borrow#isReturned()} returns
     * {@code false}. At most one active borrow can exist at a time.</p>
     *
     * @return an {@link Optional} containing the active {@link Borrow},
     *         or {@link Optional#empty()} if the copy is currently available
     */
    public Optional<Borrow> getCurrentBorrow() {
        return this._borrowings.stream()
                .filter(b -> !b.isReturned())
                .findFirst();
    }

    /**
     * Returns an unmodifiable view of all borrowing transactions ever recorded
     * for this copy, including both past and active borrows.
     *
     * <p>The returned set reflects the current state of the transaction history
     * but cannot be modified directly — use
     * {@link #addBorrowing(Borrow)} to record a new transaction.</p>
     *
     * @return an unmodifiable {@link Set} of {@link Borrow} objects;
     *         never {@code null}, but may be empty
     */
    public Set<Borrow> getBorrowings() {
        return Collections.unmodifiableSet(this._borrowings);
    }

    /**
     * Returns the total number of borrowing transactions ever recorded for
     * this copy, including both past and currently active borrows.
     *
     * @return the total borrow count; always &gt;= 0
     */
    public int getBorrowCount() {
        return this._borrowings.size();
    }
}
