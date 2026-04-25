package com.bibliotheque.model;

import java.util.Date;

/**
 * Represents a DVD media work within the library system.
 *
 * <p>This class extends {@link Work} and adds a geographical region code,
 * which is specific to optical disc media and used for digital rights
 * management (DRM). The region code is immutable once the DVD is created.</p>
 *
 * <p>All general work attributes (title, category, editor, publication date,
 * copies, authors, and events) are managed by the parent class {@link Work}.
 * Only the DVD-specific {@code region} attribute is declared here.</p>
 *
 * @see Work
 * @see Bibliotheque
 * @see Copy
 * @see Author
 *
 * @version 1.1
 */
public class Dvd extends Work {

    private final String _region;

    /**
     * Constructs a new {@code Dvd} instance with its media-specific attributes.
     *
     * <p>All shared metadata is forwarded to the {@link Work} constructor.
     * Only the region code is handled at this level.</p>
     *
     * @param title    the title of the film or content; must not be {@code null}
     * @param category the library classification code ("cote") of this work;
     *                 must not be {@code null}
     * @param editor   the studio or publishing house; must not be {@code null}
     * @param pubDate  the release or publication date; must not be {@code null}
     * @param handler  the {@link Bibliotheque} instance managing this work;
     *                 must not be {@code null}
     * @param region   the geographical region code for the disc (e.g. {@code "0"}
     *                 for region-free, {@code "1"} for North America, {@code "2"}
     *                 for Europe and Japan); must not be {@code null}
     */
    public Dvd(String title, String category, String editor, Date pubDate,
               Bibliotheque handler, String region) {
        super(title, category, editor, pubDate, handler);
        this._region = region;
    }

    // -------------------------------------------------------------------------
    // Queries
    // -------------------------------------------------------------------------

    /**
     * Returns whether this DVD's region code matches the specified code.
     *
     * @param region the region code to compare against; must not be {@code null}
     * @return {@code true} if the region codes match; {@code false} otherwise
     */
    public Boolean isRegion(String region) {
        return this._region.equals(region);
    }

    // -------------------------------------------------------------------------
    // Getters
    // -------------------------------------------------------------------------

    /**
     * Returns the geographical region code of this DVD.
     *
     * <p>Common region codes include {@code "0"} (region-free), {@code "1"}
     * (North America), {@code "2"} (Europe, Japan, Middle East),
     * {@code "3"} (Southeast Asia), {@code "4"} (Latin America and Oceania),
     * {@code "5"} (Africa, Eastern Europe, Russia) and {@code "6"} (China).</p>
     *
     * @return the region code string; never {@code null}
     */
    public String getRegion() {
        return this._region;
    }
}
