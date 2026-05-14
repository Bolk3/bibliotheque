package com.bibliotheque.model;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Vector;

/**
 * Represents a library event (workshop, exhibition, reading session, etc.).
 *
 * <p>An {@code Event} manages a schedule, a set of related works, 
 * a list of participating members, and the guest speakers involved.</p>
 *
 * @see Member
 * @see Speaker
 * @see Work
 * @see Settings
 *
 * @version 1.1
 */
public class Event {

    private Date                    _startDate;
    private Date                    _endDate;
    private String                  _type;
    private Bibliotheque            _handler;
    
    private final Vector<Work>      _relatedWorks = new Vector<>();
    private final Vector<Member>    _participants = new Vector<>();
    private final Vector<Speaker>   _speakers     = new Vector<>();

    /**
     * Constructs a new Event with its basic schedule and type.
     *
     * @param startDate the date and time the event begins
     * @param endDate   the date and time the event ends
     * @param type      the category of the event (e.g., "Workshop", "Conference")
     * @param handler   the library instance managing this event
     */
    public Event(Date startDate, Date endDate, String type, Bibliotheque handler) {
        this._startDate = startDate;
        this._endDate   = endDate;
        this._type      = type;
        this._handler   = handler;
    }

    // -------------------------------------------------------------------------
    // Operations
    // -------------------------------------------------------------------------

    /**
     * Registers a member to the event.
     *
     * @param member the {@link Member} joining
     */
    public void addParticipant(Member member) {
        if (member != null && !this._participants.contains(member)) {
            this._participants.add(member);
            member.joinEvent(this); // Maintains bidirectional relationship
        }
    }

    /**
     * Adds a guest speaker to the event.
     *
     * <p>Requires {@link Settings#PERM_ADMIN_METADATA} level.</p>
     *
     * @param speaker   the {@link Speaker} to add
     * @param validator the librarian authorizing the addition
     * @throws IllegalStateException if permission is insufficient
     */
    public void addSpeaker(Speaker speaker, Librarian validator) {
        if (!Settings.hasAccess(validator, Settings.PERM_ADMIN_METADATA)) {
            throw new IllegalStateException("Permission insuffisante pour ajouter un intervenant.");
        }
        if (speaker != null && !this._speakers.contains(speaker)) {
            this._speakers.add(speaker);
            speaker.addEvent(this); // Maintains bidirectional relationship
        }
    }

    /**
     * Associates a literary work with this event.
     *
     * @param work the {@link Work} to link
     */
    public void addRelatedWork(Work work) {
        if (work != null && !this._relatedWorks.contains(work)) {
            this._relatedWorks.add(work);
        }
    }

    // -------------------------------------------------------------------------
    // Queries
    // -------------------------------------------------------------------------

    /**
     * Checks if the event is currently happening.
     *
     * @return {@code true} if current date is between start and end date
     */
    public boolean isActive() {
        Date now = new Date();
        return now.after(_startDate) && now.before(_endDate);
    }

    /**
     * Checks if the event has already finished.
     *
     * @return {@code true} if current date is after end date
     */
    public boolean isFinished() {
        return new Date().after(_endDate);
    }

    // -------------------------------------------------------------------------
    // Getters & Setters
    // -------------------------------------------------------------------------

    public Date getStartDate() {
        return new Date(_startDate.getTime());
    }

    public void setStartDate(Date date) {
        this._startDate = date;
    }

    public Date getEndDate() {
        return new Date(_endDate.getTime());
    }

    public void setEndDate(Date date) {
        this._endDate = date;
    }

    public String getType() {
        return _type;
    }

    public void setType(String type) {
        this._type = type;
    }

    public List<Work> getRelatedWorks() {
        return Collections.unmodifiableList(_relatedWorks);
    }

    public List<Member> getParticipants() {
        return Collections.unmodifiableList(_participants);
    }

    public List<Speaker> getSpeakers() {
        return Collections.unmodifiableList(_speakers);
    }

    public Bibliotheque getHandler() {
        return _handler;
    }
}