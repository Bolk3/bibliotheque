
import java.util.Date;

public class ExtentionStamp extends Stamp{
    /**
     * 
     * @param extendedDate
     * @param validatedBy
     * @param reference
     */
    public ExtentionStamp(Date extendedDate, Bibliothecaire validatedBy, Borrow reference) {
        super(new Date(), validatedBy, reference);
        this._extentionDate = extendedDate;
    }

    private final Date  _extentionDate;

    public Date getExtentionDate() {
        return (this._extentionDate);
    }
}
