package Model.errors;

/**
 * Exception thrown when a search operation is attempted using a class type 
 * that does not inherit from the required base class.
 * <p>
 * This typically occurs in generic search methods (like {@code searchByType}) 
 * if the provided {@link Class} argument is not a valid subtype of {@link Work}.
 * </p>
 *
 * @version 1.0
 */
public class SearchClassNotInherits extends Exception{
    /**
     * Constructs a new exception with a specific detail message.
     *
     * @param errorMessage The detailed message explaining the inheritance failure.
     */
    public SearchClassNotInherits (String errorMessage) {
        super(errorMessage);
    }
}
