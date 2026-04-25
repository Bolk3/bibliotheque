package com.bibliotheque.model;

import java.util.Date;

/**
 * Represents the final record of a completed borrowing transaction.
 *
 * <p>A {@code ReturnStamp} is created at the exact moment a borrowed
 * {@link Copy} is handed back to the library. It extends {@link Stamp}
 * to inherit the core audit fields (timestamp, validator, borrow reference)
 * and adds the physical condition of the copy as observed at return time.</p>
 *
 * <p>The return timestamp is captured automatically from the system clock
 * at construction — it is not supplied by the caller, ensuring the recorded
 * time always reflects the actual moment of return.</p>
 *
 * <p>The return state is then compared against the copy's initial state by
 * {@link Borrow#isDamaged()} to determine whether the copy was damaged
 * during the loan.</p>
 *
 * @see Stamp
 * @see ExtensionStamp
 * @see Borrow
 * @see Bibliothecaire
 *
 * @version 1.1
 */
public class ReturnStamp extends Stamp {

    private final String _returnState;

    /**
     * Constructs a new {@code ReturnStamp}, capturing the current system time
     * as the official return timestamp.
     *
     * <p>This constructor is typically called from
     * {@link Borrow#returnBook(String, Bibliothecaire)} and should not need
     * to be instantiated directly elsewhere.</p>
     *
     * @param returnState the observed physical condition of the copy at the
     *                    time of return (e.g., {@code "Good"}, {@code "Damaged"});
     *                    must not be {@code null}
     * @param reference   the {@link Borrow} transaction this return closes;
     *                    must not be {@code null}
     * @param validatedBy the {@link Bibliothecaire} processing the return;
     *                    must not be {@code null}
     */
    public ReturnStamp(String returnState, Borrow reference, Bibliothecaire validatedBy) {
        super(new Date(), validatedBy, reference);
        this._returnState = returnState;
    }

    // -------------------------------------------------------------------------
    // Getters
    // -------------------------------------------------------------------------

    /**
     * Returns the physical condition of the copy as recorded at the time
     * of return.
     *
     * <p>This value is compared against {@link Borrow#getInitialState()} by
     * {@link Borrow#isDamaged()} to determine whether the copy sustained
     * damage during the loan period.</p>
     *
     * @return the return state string; never {@code null}
     */
    public String getReturnState() {
        return this._returnState;
    }
}
