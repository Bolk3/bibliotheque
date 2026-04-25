package com.bibliotheque.model;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a borrowing transaction in the library system.
 *
 * <p>This class manages the full lifecycle of a copy loan: from the initial
 * borrowing to optional date extensions and the final return. It tracks the
 * condition of the copy at both checkout and return, and provides utility
 * methods to check whether the loan is overdue, already returned, or whether
 * the copy was damaged during the loan.</p>
 *
 * <p>Once a return has been recorded via {@link #returnBook(String, Bibliothecaire)},
 * no further modifications (extensions, returns) are permitted.</p>
 *
 * @see ExtensionStamp
 * @see ReturnStamp
 * @see Membre
 * @see Bibliothecaire
 * @see Copy
 *
 * @version 1.1
 */
public class Borrow {

    private final Date              _startDate;
    private final String            _initialState;
    private final Membre            _borrowedBy;
    private final Copy              _copy;
    private final Bibliothecaire    _validatedBy;
    private Date                    _expectedDate;
    private ReturnStamp             _returnStamp = null;
    private Set<ExtensionStamp>     _extensions = new HashSet<>();

    /**
     * Constructs a new {@code Borrow} transaction, recording the current
     * timestamp as the start date and capturing the copy's initial condition.
     *
     * @param expectedDate the date by which the copy is expected to be returned;
     *                     must not be {@code null}
     * @param validatedBy  the librarian who authorised this transaction;
     *                     must not be {@code null}
     * @param borrowedBy   the member borrowing the copy;
     *                     must not be {@code null}
     * @param copy         the specific copy being borrowed;
     *                     must not be {@code null}
     */
    public Borrow(Date expectedDate, Bibliothecaire validatedBy, Membre borrowedBy, Copy copy) {
        this._startDate    = new Date();
        this._expectedDate = expectedDate;
        this._validatedBy  = validatedBy;
        this._borrowedBy   = borrowedBy;
        this._copy         = copy;
        this._initialState = this._copy.getState();
    }

    /**
     * Updates the copy's condition to match the state recorded at return,
     * but only if the copy was returned in a different condition than it
     * was borrowed ({@link #isDamaged()} returns {@code true}).
     *
     * <p>This method is called automatically by {@link #returnBook(String, Bibliothecaire)}
     * and should not be called directly.</p>
     */
    private void updateState() {
        if (this.isDamaged()) {
            this._copy.setEtat(this._returnStamp.getReturnState());
        }
    }

    /**
     * Extends the expected return date and records the extension as an
     * {@link ExtensionStamp}.
     *
     * @param newDate     the new expected return date; must be strictly after
     *                    the current expected date
     * @param validatedBy the librarian authorising the extension;
     *                    must not be {@code null}
     *
     * @throws IllegalStateException    if the copy has already been returned
     * @throws IllegalArgumentException if {@code newDate} is not strictly after
     *                                  the current expected return date
     */
    public void extendsDate(Date newDate, Bibliothecaire validatedBy)
            throws IllegalStateException, IllegalArgumentException {
        if (this.isReturned())
            throw new IllegalStateException("Déjà rendu.");
        if (!newDate.after(this._expectedDate))
            throw new IllegalArgumentException("Date invalide.");
        this._expectedDate = newDate;
        this._extensions.add(new ExtensionStamp(newDate, validatedBy, this));
    }

    /**
     * Processes the return of the borrowed copy, recording the librarian,
     * the return timestamp, and the condition of the copy at return.
     *
     * <p>If the copy is returned in a different state than it was borrowed,
     * its condition is automatically updated via {@link #updateState()}.</p>
     *
     * @param state       the condition of the copy at the time of return;
     *                    must not be {@code null}
     * @param validatedBy the librarian processing the return;
     *                    must not be {@code null}
     *
     * @throws IllegalStateException if the copy has already been returned
     */
    public void returnBook(String state, Bibliothecaire validatedBy)
            throws IllegalStateException {
        if (this.isReturned())
            throw new IllegalStateException("Déjà rendu.");
        this._returnStamp = new ReturnStamp(state, this, validatedBy);
        this.updateState();
    }

    /**
     * Returns whether the loan is currently overdue.
     *
     * <p>A loan is overdue if the current date is strictly after the
     * expected return date. Note that this check remains meaningful even
     * after the copy has been returned, since it reflects whether the
     * return was made on time.</p>
     *
     * @return {@code true} if the current date is past the expected return date;
     *         {@code false} otherwise
     */
    public Boolean isLate() {
        return new Date().after(this._expectedDate);
    }

    /**
     * Returns whether the copy has been returned.
     *
     * @return {@code true} if a {@link ReturnStamp} has been recorded;
     *         {@code false} otherwise
     */
    public Boolean isReturned() {
        return (this._returnStamp != null);
    }

    /**
     * Returns whether the copy was damaged during the loan, i.e. whether
     * its condition at return differs from its condition at checkout.
     *
     * @return {@code true} if the return state differs from the initial state;
     *         {@code false} if the copy has not yet been returned, or if its
     *         condition is unchanged
     */
    public Boolean isDamaged() {
        if (!this.isReturned())
            return false;
        return !this._returnStamp.getReturnState().equals(this._initialState);
    }

    /**
     * Calculates the total duration of the loan in milliseconds.
     *
     * <p>If the copy has been returned, the duration is measured from the
     * start date to the recorded return timestamp. Otherwise, it is measured
     * from the start date to the current moment.</p>
     *
     * @return the elapsed time in milliseconds since the start of the loan
     */
    public long getElapsedTime() {
        long startEpoch   = this._startDate.getTime();
        long currentEpoch = this.isReturned()
                ? this._returnStamp.getTimestamp().getTime()
                : new Date().getTime();
        return (currentEpoch - startEpoch);
    }

    // -------------------------------------------------------------------------
    // Getters
    // -------------------------------------------------------------------------

    /**
     * Returns the date on which this loan started.
     *
     * @return the start date; never {@code null}
     */
    public Date getStartDate() {
        return new Date(this._startDate.getTime());
    }

    /**
     * Returns the current expected return date.
     *
     * <p>This value may differ from the original date if extensions have
     * been granted via {@link #extendsDate(Date, Bibliothecaire)}.</p>
     *
     * @return the expected return date; never {@code null}
     */
    public Date getExpectedDate() {
        return new Date(this._expectedDate.getTime());
    }

    /**
     * Returns the condition of the copy at the time it was borrowed.
     *
     * @return the initial state string; never {@code null}
     */
    public String getInitialState() {
        return this._initialState;
    }

    /**
     * Returns the member who borrowed the copy.
     *
     * @return the borrowing {@link Membre}; never {@code null}
     */
    public Membre getBorrower() {
        return this._borrowedBy;
    }

    /**
     * Returns the librarian who validated the initial borrow.
     *
     * @return the validating {@link Bibliothecaire}; never {@code null}
     */
    public Bibliothecaire getValidator() {
        return this._validatedBy;
    }

    /**
     * Returns the copy involved in this loan.
     *
     * @return the borrowed {@link Copy}; never {@code null}
     */
    public Copy getCopy() {
        return this._copy;
    }

    /**
     * Returns the return stamp associated with this loan, if the copy
     * has been returned.
     *
     * @return the {@link ReturnStamp}, or {@code null} if not yet returned
     */
    public ReturnStamp getReturnStamp() {
        return this._returnStamp;
    }

    /**
     * Returns an unmodifiable view of all extensions granted for this loan.
     *
     * <p>The returned set reflects the current state of extensions but cannot
     * be modified directly — use {@link #extendsDate(Date, Bibliothecaire)}
     * to add a new extension.</p>
     *
     * @return an unmodifiable {@link Set} of {@link ExtensionStamp} objects;
     *         never {@code null}, but may be empty
     */
    public Set<ExtensionStamp> getExtensions() {
        return Collections.unmodifiableSet(this._extensions);
    }
}
