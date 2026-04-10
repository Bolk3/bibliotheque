
import java.util.Set;

public class Copy {

	/**
	 * 
	 * @param state
	 * @param ref
	 */
    public Copy(String state, Oeuvre ref) {
		this._state = state;
		this.reference = ref;
    }

    private String			_state;
    private Set<Borrow>		emprunts;
    private final Oeuvre	reference;

	/**
	 * 
	 */
	public void addEmprut(Borrow e) {
		if (!(this.emprunts.contains(e))) {
			this.emprunts.add(e);
		}
	}

	/**
	 * 
	 */
	public Oeuvre	getReference() {
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
