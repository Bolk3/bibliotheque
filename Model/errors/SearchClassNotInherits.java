package errors;

/**
 * Exeption indicating that the class is not a children of another class
 */
public class SearchClassNotInherits extends Exception{
    public SearchClassNotInherits (String errorMessage) {
        super(errorMessage);
    }
}
