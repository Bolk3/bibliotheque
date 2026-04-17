package Model;
import java.util.Date;

/**
 * Represents the final record of a completed borrowing transaction.
 * <p>
 * This class extends {@link Stamp} to capture the specific details 
 * of when a copy was returned, including its physical condition 
 * at the time of return.
 * </p>
 *
 * @version 1.0
 */
public class ReturnStamp extends Stamp{
  /**
     * Constructs a record of a returned item.
     * <p>
     * Automatically captures the current system date as the official return time 
     * via the super constructor.
     * </p>
     * @param returnState The observed physical condition of the item being returned.
     * @param reference   The {@link Borrow} transaction associated with this return.
     * @param validatedBy The librarian processing the return.
     * @see Stamp
     */
    public ReturnStamp(String returnState, Borrow reference, Bibliothecaire validatedBy) {
		super(new Date(), validatedBy, reference);
		this._returnState = returnState;
    }

    final private String	_returnState;

	/**
     * Returns the recorded condition of the item at the moment of return.
     * * @return A string representing the return state.
     */
	public String getReturnState() {
		return (this._returnState);
	}
}
