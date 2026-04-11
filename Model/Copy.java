
import java.util.Set;

/**
 * Represents a physical copy of a specific {@link Work}.
 * <p>
 * This class manages the current physical condition (state) of the copy
 * and maintains a history of all borrowing transactions associated with it.
 * </p>
 *
 * @version 1.0
 */
public class Copy {

	/**
     * Constructs a new Copy of a work with an initial physical state.
     *
     * @param state The initial condition of the copy (e.g., "New", "Good", "Damaged").
     * @param ref   The {@link Work} this copy refers to.
     */
    public Copy(String state, Work ref) {
		this._state = state;
		this.reference = ref;
    }

    private final Work		reference;
    private String			_state;
    private Set<Borrow>		_Borrowing;

	/**
     * Records a new borrowing transaction for this copy.
     * * @param e The {@link Borrow} transaction to add.
     */
	public void addBorrowing(Borrow e) {
		if (!(this._Borrowing.contains(e))) {
			this._Borrowing.add(e);
		}
	}

	/**
     * Returns the work that this physical copy represents.
     * * @return The associated {@link Work} instance.
     */
	public Work	getReference() {
		return (this.reference);
	}

	/**
     * Returns the current physical state of the copy.
     * * @return A string describing the current condition.
     */
	public String	getState() {
		return (this._state);
	}

	/**
     * Updates the physical state of the copy (e.g., after a return).
     * * @param e The new state string.
     */
	public void	setEtat(String e) {
		this._state = e;
	}

	/**
     * Checks if the copy's current state matches the provided string.
     * * @param s The state string to compare against.
     * @return {@code true} if the states match exactly; {@code false} otherwise.
     */
	public Boolean	isState(String s) {
		return (this._state.equals(s));
	}
}
