package com.bibliotheque.model;

/**
 * Utility class providing static methods for data validation and normalization.
 *
 * <p>This class centralizes the business rules for string validation (names and emails)
 * used throughout the library system. It ensures consistency in how user data is 
 * verified and compared by providing standard regular expressions and 
 * normalization techniques.</p>
 *
 * @see User
 * @version 1.1
 */
public class ValidationUtils {

    private static final String REGEX_NAME = "^[a-zA-ZÀ-ÿ][a-zA-ZÀ-ÿ' -]*$";
    private static final String REGEX_EMAIL = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    /**
     * Private constructor — this class is a static utility and should
     * never be instantiated.
     */
    private ValidationUtils() {}

    // -------------------------------------------------------------------------
    // Validation Methods
    // -------------------------------------------------------------------------

    /**
     * Validates a first name against the internal naming policy.
     *
     * @param fn the string to validate
     * @return {@code true} if the string matches {@link #REGEX_NAME}
     */
    public static boolean isFirstNameValid(String fn) {
        return fn != null && fn.matches(REGEX_NAME);
    }

    /**
     * Validates a last name against the internal naming policy.
     *
     * @param ln the string to validate
     * @return {@code true} if the string matches {@link #REGEX_NAME}
     */
    public static boolean isLastNameValid(String ln) {
        return ln != null && ln.matches(REGEX_NAME);
    }

    /**
     * Validates an email address against the internal format policy.
     *
     * @param mail the string to validate
     * @return {@code true} if the string matches {@link #REGEX_EMAIL}
     */
    public static boolean isEmailValid(String mail) {
        return mail != null && mail.matches(REGEX_EMAIL);
    }

    // -------------------------------------------------------------------------
    // Normalization Methods
    // -------------------------------------------------------------------------

    /**
     * Normalizes a string for comparison purposes.
     *
     * <p>The normalization process involves:
     * <ul>
     *   <li>Decomposing accented characters (NFD form)</li>
     *   <li>Removing diacritical marks (accents)</li>
     *   <li>Trimming leading/trailing whitespace</li>
     *   <li>Converting to lower case</li>
     * </ul>
     * </p>
     * 
     * <p><b>Examples:</b>
     * <ul>
     *   <li>{@code "  Hélène  "} becomes {@code "helene"}</li>
     *   <li>{@code "François"} becomes {@code "francois"}</li>
     *   <li>{@code "Édouard-PIERRE"} becomes {@code "edouard-pierre"}</li>
     * </ul>
     * </p>
     *
     * @param str the string to normalize
     * @return a normalized version of the string, or an empty string if input 
     *         is {@code null}
     */
    public static String normalize(String str) {
        if (str == null) return "";
        return java.text.Normalizer.normalize(str, java.text.Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .trim()
                .toLowerCase();
    }

    /**
     * Normalizes an email address for comparison purposes.
     *
     * <p>This method only performs trimming and case conversion to preserve 
     * the integrity of the email format.</p>
     * 
     * <p><b>Examples:</b>
     * <ul>
     *   <li>{@code " User.Name@Example.COM "} becomes {@code "user.name@example.com"}</li>
     *   <li>{@code "contact@WEB.fr"} becomes {@code "contact@web.fr"}</li>
     * </ul>
     * </p>
     *
     * @param email the email address to normalize
     * @return the trimmed, lowercase email, or an empty string if input 
     *         is {@code null}
     */
    public static String normalizeEmail(String email) {
        if (email == null) return "";
        return email.trim().toLowerCase();
    }
}