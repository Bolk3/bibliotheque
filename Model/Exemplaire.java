
import java.util.Set;

public class Exemplaire {

	/**
	 * 
	 * @param state
	 * @param ref
	 */
    public Exemplaire(String state, Oeuvre ref) {
		this._state = state;
		this.reference = ref;
    }

    private String			_state;
    private Set<Emprunt>	emprunts;
    private final Oeuvre	reference;

	/**
	 * 
	 */
	public void addEmprut(Emprunt e) {
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
