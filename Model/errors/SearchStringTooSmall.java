package errors;

/**
 * Exeption indicating that the querry is too small to process the search
 */
public class SearchStringTooSmall extends Exception{
    public SearchStringTooSmall (String errorMessage) {
        super(errorMessage);
    }
}
