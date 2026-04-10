
import java.util.Date;

public class ReturnStamp extends Stamp{
  /**
   * 
   * @param returnState
   * @param loan
   * @param validatedBy
   */
    public ReturnStamp(String returnState, Borrow reference, Bibliothecaire validatedBy) {
		super(new Date(), validatedBy, reference);
		this._returnState = returnState;
    }

    final private String	_returnState;

	public String getReturnState() {
		return (this._returnState);
	}
}
