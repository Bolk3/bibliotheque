package com.bibliotheque.model;

/**
 * Centralized configuration class for library system settings and security levels.
 *
 * <p>This class defines the permission thresholds required for various administrative 
 * actions. It acts as a single source of truth to avoid magic numbers throughout 
 * the application.</p>
 *
 * @version 1.0
 */
public final class Settings {

    // -------------------------------------------------------------------------
    // Permission Levels
    // -------------------------------------------------------------------------

    /** Minimum level required to validate a standard borrow. */
    public static final int PERM_VALIDATE_BORROW = 1;

    /** Minimum level required to grant a return date extension. */
    public static final int PERM_EXTEND_LOAN = 2;

    /** Minimum level required to process a return and update copy states. */
    public static final int PERM_PROCESS_RETURN = 1;

    /** Minimum level required to modify library metadata (Users, Works). */
    public static final int PERM_ADMIN_METADATA = 5;

    /** Level reserved for system administrators. */
    public static final int PERM_SUPERUSER = 10;

    // -------------------------------------------------------------------------
    // Utility Logic
    // -------------------------------------------------------------------------

    /**
     * Checks if a librarian has sufficient privileges for a specific action level.
     *
     * @param librarian the {@link Librarian} to check
     * @param requiredLevel the minimum permission level required
     * @return {@code true} if the librarian's level is equal to or higher than 
     *         the required level; {@code false} otherwise
     */
    public static boolean hasAccess(Librarian librarian, int requiredLevel) {
        if (librarian == null) return false;
        return librarian.getPermission() >= requiredLevel;
    }

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private Settings() {
        throw new UnsupportedOperationException("Utility class");
    }
}