
import java.util.Date;

/**
 * Abstract base class representing a formal record or "stamp" within a borrowing lifecycle.
 * <p>
 * A Stamp acts as an immutable log entry that tracks who authorized an action, 
 * when it occurred, and which borrowing transaction it refers to.
 * </p>
 *
 * @version 1.0
 */
public abstract class Stamp {

    /**
     * Initializes the core attributes of a transaction stamp.
     *
     * @param timestamp   The exact date and time the event occurred.
     * @param validatedBy The librarian responsible for validating the transaction.
     * @param reference   The {@link Borrow} transaction associated with this stamp.
     */
    public Stamp(Date timestamp, Bibliothecaire validatedBy, Borrow reference) {
        this._timestamp = timestamp;
        this._validatedBy = validatedBy;
        this._reference = reference;
    }

    private final Date              _timestamp;
    private final Bibliothecaire    _validatedBy;
    private final Borrow           _reference;

    /**
     * Returns the date and time when this stamp was created.
     * * @return The {@link Date} of the event.
     */
    public Date getTimestamp() {
        return (this._timestamp);
    }

    /**
     * Returns the librarian who authorized this specific event.
     * * @return The {@link Bibliothecaire} instance.
     */
    public Bibliothecaire getValidator() {
        return (this._validatedBy);
    }

    /**
     * Returns the borrowing transaction linked to this stamp.
     * * @return The associated {@link Borrow} record.
     */
    public Borrow  getReference() {
        return (this._reference);
    }
}
