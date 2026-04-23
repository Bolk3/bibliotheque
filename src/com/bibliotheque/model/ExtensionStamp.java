package Model;
import java.util.Date;

/**
 * Represents a record of a loan duration extension.
 * <p>
 * This class extends {@link Stamp} to log when a borrowing period was 
 * officially extended by a librarian and what the new due date is.
 * </p>
 *
 * @version 1.0
 */
public class ExtensionStamp extends Stamp{
    /**
     * Constructs an extension record.
     * <p>
     * The super constructor is called with {@code new Date()} to record 
     * the exact moment this extension was processed.
     * </p>
     * @param extendedDate The new due date for the borrowed copy.
     * @param validatedBy  The librarian who authorized the extension.
     * @param reference    The {@link Borrow} transaction this extension applies to.
     * @see Stamp
     */
    public ExtensionStamp(Date extendedDate, Bibliothecaire validatedBy, Borrow reference) {
        super(new Date(), validatedBy, reference);
        this._extentionDate = extendedDate;
    }

    private final Date  _extentionDate;

    /**
     * Returns the new due date associated with this extension.
     * @return The new {@link Date} assigned to the loan.
     */
    public Date getExtentionDate() {
        return (this._extentionDate);
    }
}
