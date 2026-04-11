import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a borrowing transaction in the library.
 * <p>
 * This class tracks the lifecycle of a borrow, including the start date, 
 * expected return date, extensions, and the final return process.
 * </p>
 *
 * @version 1.0
 */
public class Borrow {
    /**
     * Constructs a new Borrow transaction.
     *
     * @param expectedDate The date the copy is expected to be returned.
     * @param validatedBy  The librarian who authorized the borrow.
     * @param borrowedBy   The member who is borrowing the copy.
     * @param copy         The specific copy being borrowed.
     */
    public Borrow(Date expectedDate, Bibliothecaire validatedBy, Membre borowedBy, Copy copy) {
		this._startDate = new Date();
		this._expectedDate = expectedDate;
		this._validatedBy = validatedBy;
		this._borrowedBy = borowedBy;
		this._copy = copy;
		this._initialState = this._copy.getState();
        this._extentions = new HashSet<>();
    }

    private final Date              _startDate;
	private final String            _initialState;
    private final Membre            _borrowedBy;
    private final Copy              _copy;
    private final Bibliothecaire    _validatedBy;
    private Date                    _expectedDate;
    private ReturnStamp             _returnStamp = null;
    private Set<ExtensionStamp>     _extentions;

    /**
     * Updates the state of the copy if it was returned in a different condition.
     */
    private void updateEtat() {
		if (this.isDamaged()) {
            this._copy.setEtat(this._returnStamp.getReturnState());
        }
	}

    /**
     * Extends the expected return date and records the extension.
     *
     * @param newDate     The new expected return date.
     * @param validatedBy The librarian validating the extension.
     */
    public void extendsDate(Date newDate, Bibliothecaire validatedBy) {
        this._expectedDate = newDate;
        this._extentions.add(new ExtensionStamp(newDate, validatedBy, this));
    }

    /**
     * Checks if the current date is past the expected return date.
     *
     * @return {@code true} if the borrow is overdue; {@code false} otherwise.
     */
	public Boolean  isLate() {
		Date now = new Date();

		return (now.after(this._expectedDate));
	}

    /**
     * Checks if the copy has been returned.
     *
     * @return {@code true} if a return stamp exists; {@code false} otherwise.
     */
	public Boolean  isReturned() {
		return (this._returnStamp != null);
	}

    /**
     * Compares the return state with the initial state to check for damage.
     *
     * @return {@code true} if the state has changed; {@code false} otherwise.
     */
	public Boolean  isDamaged() {
		String returnState = this._returnStamp.getReturnState();

		return (!returnState.equals(this._initialState));
	}

    /**
     * Processes the return of the borrowed copy.
     *
     * @param state       The condition of the copy upon return.
     * @param validatedBy The librarian processing the return.
     */
	public void returnBook(String state, Bibliothecaire validatedBy) {
		this._returnStamp = new ReturnStamp(state, this, validatedBy);
		this.updateEtat();
	}

    /**
     * Returns the set of all extensions granted for this borrow.
     * @return A set of {@link ExtensionStamp}.
     */
    public Set<ExtensionStamp>  getExtentions() {
        return (this._extentions);
    }

    /** @return The state of the copy at the time of borrowing. */
    public String   getInitialState() {
        return (this._initialState);
    }

    /** @return The date the borrowing transaction started. */
    public Date getStartDate() {
        return (this._startDate);
    }

    /** @return The member who borrowed the copy. */
    public Membre   getBorower() {
        return (this._borrowedBy);
    }

    /** @return The librarian who validated the initial borrow. */
    public Bibliothecaire getValidator() {
        return (this._validatedBy);
    }

    /**
     * Calculates the time elapsed since the start of the borrow in milliseconds.
     * If returned, calculates up to the return date; otherwise, up to now.
     *
     * @return The elapsed time in milliseconds.
     */
    public long  getElapsedTime(){
        long    startEpoch = this._startDate.getTime();
        long    currentEpoch;

        if (this.isReturned()) {
            currentEpoch = this._returnStamp.getTimestamp().getTime();
        } else {
            currentEpoch = new Date().getTime();
        }

        return (currentEpoch - startEpoch);
    }
}
