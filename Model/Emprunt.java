import java.util.*;

public class Emprunt {

    /**
     * 
     * @param expectedDate
     * @param validatedBy
     * @param borowedBy
     * @param exemplaire
     */
    public Emprunt(Date expectedDate, Bibliothecaire validatedBy, Membre borowedBy, Exemplaire exemplaire) {
		this._startDate = new Date();
		this.expectedDate = expectedDate;
		this._validatedBy = validatedBy;
		this._borrowedBy = borowedBy;
		this._copy = exemplaire;
		this._initialState = this._copy.getState();
    }

    private final Date      _startDate;
    private Retour          _returnStamp = null;
    private Date            _returnDate = null;
    private Bibliothecaire  _validatedBy;
    private Membre          _borrowedBy;
    private Exemplaire      _copy;
	private String          _initialState;
    public Date             expectedDate;

    private void updateEtat() {
		if (this.isDamaged()) {
            this._copy.setEtat(this._returnStamp.getReturnState());
        }
	}

    public void    extendsDate(Date newDate, Bibliothecaire validatedBy) {
        this.expectedDate = newDate;
    }

	public Boolean  isLate() {
		Date now = new Date();

		return (now.after(this.expectedDate));
	}

	public Boolean  isReturned() {
		return (this._returnStamp == null);
	}

	public Boolean  isDamaged() {
		String returnState = this._returnStamp.getReturnState();

		return (returnState.equals(this._initialState));
	}

	public void returnBook(String state, Bibliothecaire validatedBy) {
        this._returnDate = new Date();
		this._returnStamp = new Retour(state, this, validatedBy);
		this.updateEtat();
	}
}
