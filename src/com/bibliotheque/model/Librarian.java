package com.bibliotheque.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a library staff member responsible for administrative tasks.
 */
public class Librarian extends User {

    private String _position;
    private int _permission;
    
    // Remplacement de Vector par ArrayList pour de meilleures performances
    private final List<Borrow> _validatedBorrows = new ArrayList<>();
    private final List<Stamp> _validatedStamps = new ArrayList<>();

    public Librarian(
        String firstName,
        String lastName,
        String email,
        Bibliotheque handler,
        String position,
        int permission
    ) throws IllegalArgumentException {
        super(firstName, lastName, email, handler);
        setPermission(permission);
        setPosition(position);
    }

    // -------------------------------------------------------------------------
    // Mutators
    // -------------------------------------------------------------------------

    public void setPermission(int permission) {
        this._permission = permission;
    }

    public void setPosition(String position) {
        this._position = position;
    }

    public void addValidatedBorrow(Borrow borrow) {
        if (borrow != null && !this._validatedBorrows.contains(borrow)) {
            this._validatedBorrows.add(borrow);
        }
    }

    public void addValidatedStamp(Stamp stamp) {
        if (stamp != null && !this._validatedStamps.contains(stamp)) {
            this._validatedStamps.add(stamp);
        }
    }

    // -------------------------------------------------------------------------
    // Queries
    // -------------------------------------------------------------------------

    public boolean isPermission(int permission) {
        return this._permission == permission;
    }

    public boolean isPosition(String position) {
        if (position == null) return false;
        return ValidationUtils.normalize(this._position)
                .equals(ValidationUtils.normalize(position));
    }

    // -------------------------------------------------------------------------
    // Getters
    // -------------------------------------------------------------------------

    public int getPermission() { return this._permission; }
    public String getPosition() { return this._position; }

    public List<Stamp> getValidatedStamps() {
        return Collections.unmodifiableList(this._validatedStamps);
    }

    public List<Borrow> getValidatedBorrows() {
        return Collections.unmodifiableList(this._validatedBorrows);
    }

    @Override
    public String toString() {
        return this.getFirstName() + " " + this.getLastName();
    }
}