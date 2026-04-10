import java.util.Date;
import java.util.Set;

public class Borrow {
    /**
     * 
     * @param expectedDate
     * @param validatedBy
     * @param borowedBy
     * @param copy
     */
    public Borrow(Date expectedDate, Bibliothecaire validatedBy, Membre borowedBy, Copy copy) {
		this._startDate = new Date();
		this._expectedDate = expectedDate;
		this._validatedBy = validatedBy;
		this._borrowedBy = borowedBy;
		this._copy = copy;
		this._initialState = this._copy.getState();
    }

    private final Date              _startDate;
	private final String            _initialState;
    private final Membre            _borrowedBy;
    private final Copy        _copy;
    private final Bibliothecaire    _validatedBy;
    private Date                    _expectedDate;
    private ReturnStamp             _returnStamp = null;
    private Set<ExtentionStamp>     _extentions;

    private void updateEtat() {
		if (this.isDamaged()) {
            this._copy.setEtat(this._returnStamp.getReturnState());
        }
	}

    public void extendsDate(Date newDate, Bibliothecaire validatedBy) {
        this._expectedDate = newDate;
        this._extentions.add(new ExtentionStamp(newDate, validatedBy, this));
    }

	public Boolean  isLate() {
		Date now = new Date();

		return (now.after(this._expectedDate));
	}

	public Boolean  isReturned() {
		return (this._returnStamp == null);
	}

	public Boolean  isDamaged() {
		String returnState = this._returnStamp.getReturnState();

		return (returnState.equals(this._initialState));
	}

	public void returnBook(String state, Bibliothecaire validatedBy) {
		this._returnStamp = new ReturnStamp(state, this, validatedBy);
		this.updateEtat();
	}

    public Set<ExtentionStamp>  getExtentions() {
        return (this._extentions);
    }

    public String   getInitialState() {
        return (this._initialState);
    }

    public Date getStartDate() {
        return (this._startDate);
    }

    public Membre   getBorower() {
        return (this._borrowedBy);
    }

    public Bibliothecaire getValidator() {
        return (this._validatedBy);
    }

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
