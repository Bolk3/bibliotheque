package com.bibliotheque.model;

import java.util.Date;

/**
 * Represents a formal record of a borrowing period extension.
 *
 * <p>An {@code ExtensionStamp} is created each time a librarian officially
 * extends the due date of an active {@link Borrow} transaction. It extends
 * {@link Stamp} to inherit the core audit fields (timestamp, validator, borrow
 * reference) and adds the new due date that was granted.</p>
 *
 * <p>The processing timestamp is captured automatically from the system clock
 * at construction — it reflects the moment the extension was recorded, not
 * the new due date itself.</p>
 *
 * <p>A single {@link Borrow} may accumulate multiple {@code ExtensionStamp}
 * instances over its lifetime, each representing one granted extension.
 * The full history is accessible via {@link Borrow#getExtensions()}.</p>
 *
 * @see Stamp
 * @see ReturnStamp
 * @see Borrow
 * @see Bibliothecaire
 *
 * @version 1.1
 */
public class ExtensionStamp extends Stamp {

    private final Date _extensionDate;

    /**
     * Constructs a new {@code ExtensionStamp}, capturing the current system
     * time as the moment the extension was processed.
     *
     * <p>This constructor is typically called from
     * {@link Borrow#extendsDate(Date, Bibliothecaire)} and should not need
     * to be instantiated directly elsewhere.</p>
     *
     * @param extensionDate the new due date granted by this extension;
     *                      must be strictly after the previous expected return
     *                      date — this is enforced by
     *                      {@link Borrow#extendsDate(Date, Bibliothecaire)}
     *                      before this stamp is created; must not be {@code null}
     * @param validatedBy   the {@link Bibliothecaire} who authorised the
     *                      extension; must not be {@code null}
     * @param reference     the {@link Borrow} transaction this extension
     *                      applies to; must not be {@code null}
     */
    public ExtensionStamp(Date extensionDate, Bibliothecaire validatedBy, Borrow reference) {
        super(new Date(), validatedBy, reference);
        this._extensionDate = extensionDate;
    }

    // -------------------------------------------------------------------------
    // Getters
    // -------------------------------------------------------------------------

    /**
     * Returns the new due date granted by this extension.
     *
     * <p>A defensive copy is returned to prevent external mutation of the
     * internal date, since {@link Date} is mutable.</p>
     *
     * <p>Note that this date is distinct from the stamp's processing timestamp
     * returned by {@link #getTimestamp()} — the timestamp records <em>when</em>
     * the extension was processed, while this date records <em>until when</em>
     * the loan was extended.</p>
     *
     * @return a copy of the new due date; never {@code null}
     */
    public Date getExtensionDate() {
        return new Date(this._extensionDate.getTime());
    }
}
