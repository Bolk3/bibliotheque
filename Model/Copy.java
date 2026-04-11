
import java.util.Set;

public class Copy {

	/**
	 * 
	 * @param state
	 * @param ref
	 */
    public Copy(String state, Work ref) {
		this._state = state;
		this.reference = ref;
    }

    private final Work		reference;
    private String			_state;
    private Set<Borrow>		_Borrowing;

	/**
	 * 
	 */
	public void addEmprut(Borrow e) {
		if (!(this._Borrowing.contains(e))) {
			this._Borrowing.add(e);
		}
	}

	/**
	 * 
	 */
	public Work	getReference() {
		return (this.reference);
	}

	/**
	 * 
	 */
	public String	getState() {
		return (this._state);
	}

	/**
	 * 
	 */
	public void	setEtat(String e) {
		this._state = e;
	}

	/**
	 * 
	 */
	public Boolean	isState(String s) {
		return (this._state.equals(s));
	}
}
