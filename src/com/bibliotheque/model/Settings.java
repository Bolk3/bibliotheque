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
    
    public static int PERM_VALIDATE_BORROW = 1;
    public static int PERM_EXTEND_LOAN     = 2;
    public static int PERM_PROCESS_RETURN  = 1;
    public static int PERM_ADMIN_METADATA  = 5;
    public static int PERM_SUPERUSER       = 10;

    /**
     * Checks if a librarian has sufficient permissions to perform an operation.
     *
     * @param librarian     the {@link Librarian} attempting the action; can be {@code null}
     * @param requiredLevel the minimum required security clearance level
     * @return {@code true} if the librarian's clearance is greater than or equal to the 
     *         required level; {@code false} if the librarian is {@code null} or lacks permission
     */
    public static boolean hasAccess(Librarian librarian, int requiredLevel) {
        if (librarian == null) return false;
        return librarian.getPermission() >= requiredLevel;
    }

    /**
     * Private constructor — this class is a static configuration utility and should 
     * never be instantiated.
     * 
     * @throws UnsupportedOperationException always
     */
    private Settings() {
        throw new UnsupportedOperationException("Utility class");
    }
}