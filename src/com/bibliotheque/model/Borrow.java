package com.bibliotheque.model;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Vector;

/**
 * Represents a borrowing transaction in the library system.
 *
 * <p>This class manages the full lifecycle of a copy loan: from the initial
 * checkout to optional date extensions and the final return. It maintains 
 * accountability by tracking the librarians involved in each step and 
 * monitoring the physical condition of the copy.</p>
 *
 * <p><b>Business Rules:</b>
 * <ul>
 *   <li>Extensions and returns require specific permission levels defined in {@link Settings}.</li>
 *   <li>The copy's state is automatically updated in the system if it is returned damaged.</li>
 *   <li>Once a {@link ReturnStamp} is issued, the transaction is closed and cannot be modified.</li>
 * </ul>
 * </p>
 *
 * @see ExtensionStamp
 * @see ReturnStamp
 * @see Member
 * @see Librarian
 * @see Copy
 * @see Settings
 *
 * @version 1.3
 */
public class Borrow {

    /** The date and time when the loan was initiated. */
    private final Date                 _startDate;

    /** The physical condition of the copy at the time of checkout. */
    private final String               _initialState;

    /** The library member who borrowed the copy. */
    private final Member               _borrowedBy;

    /** The specific physical copy associated with this loan. */
    private final Copy                 _copy;

    /** The librarian who authorized the initial borrowing. */
    private final Librarian            _validatedBy;

    /** The current deadline for returning the copy (can be updated via extensions). */
    private Date                       _expectedDate;

    /** The record of the return, or {@code null} if the book is still out. */
    private ReturnStamp                _returnStamp = null;

    /** A chronological list of all date extensions granted for this loan. */
    private final Vector<ExtensionStamp> _extensions = new Vector<>();

    /**
     * Constructs a new {@code Borrow} transaction.
     * 
     * <p>Initializes the start date to the current system time and captures 
     * the initial state of the copy for future comparison.</p>
     *
     * @param expectedDate the initial deadline for the loan; must not be {@code null}
     * @param validatedBy  the librarian authorizing the transaction; must not be {@code null}
     * @param borrowedBy   the member borrowing the copy; must not be {@code null}
     * @param copy         the specific {@link Copy} being loaned; must not be {@code null}
     */
    public Borrow(Date expectedDate, Librarian validatedBy, Member borrowedBy, Copy copy) {
        this._startDate    = new Date();
        this._expectedDate = expectedDate;
        this._validatedBy  = validatedBy;
        this._borrowedBy   = borrowedBy;
        this._copy         = copy;
        this._initialState = this._copy.getState().toString();
    }

    /**
     * Updates the physical copy's state if damage is detected upon return.
     * 
     * <p>This method is invoked internally by {@link #returnBook(String, Librarian)}.</p>
     */
    private void updateState() {
        if (this.isDamaged()) {
            this._copy.setState(State.valueOf(this._returnStamp.getReturnState()));
        }
    }

    /**
     * Extends the expected return date and logs the action.
     *
     * <p>Validation checks:
     * <ol>
     *   <li>Librarian must have {@link Settings#PERM_EXTEND_LOAN} permission.</li>
     *   <li>The loan must not be already closed (returned).</li>
     *   <li>The new date must be strictly after the current deadline.</li>
     * </ol>
     * </p>
     *
     * @param newDate     the new deadline for the loan
     * @param validatedBy the librarian authorizing this extension
     * @throws IllegalStateException    if permission is denied or the book is already returned
     * @throws IllegalArgumentException if the provided date is null or invalid
     */
    public void extendsDate(Date newDate, Librarian validatedBy)
            throws IllegalStateException, IllegalArgumentException {
        
        if (!Settings.hasAccess(validatedBy, Settings.PERM_EXTEND_LOAN)) {
            throw new IllegalStateException("Permission insuffisante pour prolonger.");
        }
        if (this.isReturned()) {
            throw new IllegalStateException("Impossible de prolonger un livre déjà rendu.");
        }
        if (newDate == null || !newDate.after(this._expectedDate)) {
            throw new IllegalArgumentException("La nouvelle date doit être postérieure à l'échéance actuelle.");
        }

        this._expectedDate = newDate;
        
        ExtensionStamp stamp = new ExtensionStamp(newDate, validatedBy, this);
        this._extensions.add(stamp);
        
        validatedBy.addValidatedStamp(stamp);
    }

    /**
     * Records the return of the borrowed copy.
     *
     * <p>This operation closes the transaction. If the return state differs from 
     * the initial state, the copy's metadata is updated to reflect the damage.</p>
     *
     * @param state       the condition of the copy at the time of return
     * @param validatedBy the librarian processing the return
     * @throws IllegalStateException if permission is denied or already returned
     */
    public void returnBook(String state, Librarian validatedBy)
            throws IllegalStateException {
        
        if (!Settings.hasAccess(validatedBy, Settings.PERM_PROCESS_RETURN)) {
            throw new IllegalStateException("Permission insuffisante pour valider le retour.");
        }
        if (this.isReturned()) {
            throw new IllegalStateException("Ce prêt a déjà été clôturé.");
        }

        this._returnStamp = new ReturnStamp(state, this, validatedBy);
        
        validatedBy.addValidatedStamp(this._returnStamp);
        
        this.updateState();
    }

    // -------------------------------------------------------------------------
    // Logic Queries
    // -------------------------------------------------------------------------

    /**
     * Checks if the loan is overdue based on the current system time.
     * 
     * @return {@code true} if the current date is past the expected date, otherwise {@code false}
     */
    public boolean isLate() {
        return new Date().after(this._expectedDate);
    }

    /**
     * Checks if the copy has been returned to the library.
     * 
     * @return {@code true} if a return stamp exists, otherwise {@code false}
     */
    public boolean isReturned() {
        return (this._returnStamp != null);
    }

    /**
     * Compares the initial and return conditions of the copy.
     * 
     * @return {@code true} if the book was returned in a different state, otherwise {@code false}
     */
    public boolean isDamaged() {
        if (!this.isReturned()) return false;
        return !this._returnStamp.getReturnState().equals(this._initialState);
    }

    /**
     * Calculates the duration of the loan.
     * 
     * @return time elapsed in milliseconds from start to return, or start to now if still active
     */
    public long getElapsedTime() {
        long startEpoch = this._startDate.getTime();
        long endEpoch   = this.isReturned() 
                ? this._returnStamp.getTimestamp().getTime() 
                : new Date().getTime();
        return (endEpoch - startEpoch);
    }

    // -------------------------------------------------------------------------
    // Getters
    // -------------------------------------------------------------------------

    /**
     * Returns a defensive copy of the borrowing transaction start date.
     * 
     * @return the loan initialization date
     */
    public Date getStartDate() {
        return new Date(this._startDate.getTime());
    }

    /**
     * Returns a defensive copy of the current expected deadline date.
     * 
     * @return the expected return deadline date
     */
    public Date getExpectedDate() {
        return new Date(this._expectedDate.getTime());
    }

    /**
     * Returns the textual description of the copy's condition at checkout.
     * 
     * @return the initial state string
     */
    public String getInitialState() {
        return this._initialState;
    }

    /**
     * Returns the member associated with this transaction.
     * 
     * @return the borrowing {@link Member}
     */
    public Member getBorrower() {
        return this._borrowedBy;
    }

    /**
     * Returns the librarian who authorized the initial borrowing.
     * 
     * @return the validating {@link Librarian}
     */
    public Librarian getValidator() {
        return this._validatedBy;
    }

    /**
     * Returns the physical copy linked to this transaction.
     * 
     * @return the loaned {@link Copy}
     */
    public Copy getCopy() {
        return this._copy;
    }

    /**
     * Returns the return record stamp associated with this transaction.
     * 
     * @return the {@link ReturnStamp} instance, or {@code null} if the item is still loaned out
     */
    public ReturnStamp getReturnStamp() {
        return this._returnStamp;
    }

    /**
     * Returns an unmodifiable view of the extension history.
     * 
     * @return a chronological {@link List} of {@link ExtensionStamp} objects
     */
    public List<ExtensionStamp> getExtensions() {
        return Collections.unmodifiableList(this._extensions);
    }
}