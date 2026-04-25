package com.bibliotheque.model;

import com.bibliotheque.model.State;
import com.bibliotheque.model.Borrow;
import java.util.HashSet;
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

    private final Work	_reference;
    private Set<Borrow>	_borrowing = new HashSet<>();
    private State       _state;

	/**
     * Constructs a new Copy of a work with an initial physical state.
     *
     * @param state The initial condition of the copy (e.g., "New", "Good", "Damaged").
     * @param ref   The {@link Work} this copy refers to.
     */
    public Copy(State state, Work ref) {
		this._state = state;
		this.reference = ref;
    }

	/**
     * Records a new borrowing transaction for this copy.
     * * @param e The {@link Borrow} transaction to add.
     */
	public void addBorrowing(Borrow e) { this._borrowing.add(e); }

	/**
     * Returns the work that this physical copy represents.
     * * @return The associated {@link Work} instance.
     */
	public Work	getReference() { return (this.reference); }

	/**
     * Returns the current physical State of the copy.
     * * @return A string describing the current condition.
     */
	public State	getState() { return (this._state); }

	/**
     * Updates the physical state of the copy (e.g., after a return).
     * * @param e The new state string.
     */
	public void	setEtat(State e) { this._state = e; }

	/**
     * Checks if the copy's current state matches the provided string.
     * * @param s The state string to compare against.
     * @return {@code true} if the states match exactly; {@code false} otherwise.
     */
	public Boolean	isState(State s) { return s == this._state; }

    public Boolean  isDisponible() {
        Borrow[]    arrCopy = new Borrow[this._borrowing.size()];
        Borrow      latest;

        this._borrowing.toArray(arrCopy);
        
    }
}
