package com.bibliotheque.model;

/**
 * Represents the physical condition of a specific copy within the library.
 *
 * <p>This enumeration is used to track the lifecycle and wear of media 
 * (books, DVDs, etc.). It allows the system to determine if a copy is 
 * fit for loan, requires replacement, or has been removed from inventory.</p>
 *
 * @see Copy
 * @see Work#getCopiesByState(State)
 * 
 * @version 1.0
 */
public enum State {

    /** 
     * The copy is in brand new condition, showing no signs of use. 
     */
    NEUF,

    /** 
     * The copy is in good condition, showing minor signs of regular handling. 
     */
    BON,

    /** 
     * The copy shows significant signs of wear (e.g., yellowed pages, 
     * scratched casing) but remains fully functional. 
     */
    USE,

    /** 
     * The copy is damaged (e.g., torn pages, broken disc) and may 
     * require repair or withdrawal from the collection. 
     */
    ABIME,

    /** 
     * The copy is no longer present in the library's physical inventory 
     * (e.g., not returned by a member or declared missing). 
     */
    PERDU
}