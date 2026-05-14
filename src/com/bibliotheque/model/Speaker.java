package com.bibliotheque.model;

import java.util.Collections;
import java.util.List;
import java.util.Vector;

/**
 * Represents an external guest or specialist who participates in library events.
 *
 * <p>A {@code Speaker} is a specialized {@link User} characterized by a professional 
 * field of expertise (specialty). This class tracks all events the speaker has 
 * been involved in.</p>
 *
 * @see User
 * @see Event
 *
 * @version 1.0
 */
public class Speaker extends User {

    private String                  _specialty;
    private final Vector<Event> _participatedEvents = new Vector<>();

    /**
     * Constructs a new Speaker with full identity and professional specialty.
     *
     * @param firstName the speaker's first name
     * @param lastName  the speaker's last name
     * @param email     the speaker's contact email
     * @param handler   the {@link Bibliotheque} instance managing this user
     * @param specialty the speaker's field of expertise
     * @throws IllegalArgumentException if identity validation fails
     */
    public Speaker(
        String firstName,
        String lastName,
        String email,
        Bibliotheque handler,
        String specialty
    ) throws IllegalArgumentException {
        super(firstName, lastName, email, handler);
        this._specialty = specialty;
    }

    // -------------------------------------------------------------------------
    // Operations
    // -------------------------------------------------------------------------

    /**
     * Adds an event to the speaker's history.
     *
     * @param event the {@link Event} to add; must not be {@code null}
     */
    public void addEvent(Event event) {
        if (event != null && !this._participatedEvents.contains(event)) {
            this._participatedEvents.add(event);
        }
    }

    /**
     * Checks if the speaker has participated in a specific event.
     *
     * @param event the event to check
     * @return {@code true} if the speaker participated; {@code false} otherwise
     */
    public boolean hasParticipated(Event event) {
        return this._participatedEvents.contains(event);
    }

    /**
     * Checks if the speaker's specialty matches a given string (normalized).
     *
     * @param specialty the specialty string to compare against
     * @return {@code true} if specialties match; {@code false} otherwise
     */
    public boolean isSpecialty(String specialty) {
        return ValidationUtils.normalize(this._specialty)
                .equals(ValidationUtils.normalize(specialty));
    }

    // -------------------------------------------------------------------------
    // Getters & Setters
    // -------------------------------------------------------------------------

    /**
     * Returns the speaker's professional specialty.
     *
     * @return the specialty string
     */
    public String getSpecialty() {
        return this._specialty;
    }

    /**
     * Updates the speaker's professional specialty.
     *
     * @param specialty the new specialty title
     */
    public void setSpecialty(String specialty) {
        this._specialty = specialty;
    }

    /**
     * Returns an unmodifiable list of events the speaker has participated in.
     *
     * @return a {@link List} of {@link Event} objects; never {@code null}
     */
    public List<Event> getParticipatedEvents() {
        return Collections.unmodifiableList(this._participatedEvents);
    }
}