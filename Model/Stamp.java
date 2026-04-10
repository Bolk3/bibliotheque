
import java.util.Date;

public abstract class Stamp {

    /**
     * 
     * @param timestamp
     * @param validatedBy
     * @param reference
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
     * 
     * @return
     */
    public Date getTimestamp() {
        return (this._timestamp);
    }

    /**
     * 
     * @return
     */
    public Bibliothecaire getValidator() {
        return (this._validatedBy);
    }

    /**
     * 
     * @return
     */
    public Borrow  getReference() {
        return (this._reference);
    }
}
