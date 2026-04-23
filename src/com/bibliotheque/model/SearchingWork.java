package Model;
import errors.SearchClassNotInherits;
import errors.SearchStringTooSmall;
import java.util.Date;
import java.util.Set;
import java.util.Vector;
import java.util.function.Function;

/**
 * Utility class providing static methods to filter and search through collections of {@link Work}.
 * <p>
 * This class supports various search strategies, including specific attribute filtering 
 * (ISBN, Region), functional extraction for flexible string searches, and type-based filtering.
 * </p>
 *
 * 
 * @version 1.0
 */
public class SearchingWork {
    /**
     * Searches through a Set of Works and filters for DVDs matching a specific region.
     * @param list      Set of unique {@link Work} elements to search through.
     * @param region    The region code to match.
     * @return          A {@link Vector} of {@link Dvd} objects that match the region.
     * @throws          SearchStringTooSmall If the region string is null or shorter than 3 characters.
     */
    public static Vector<Dvd> searchByRegion(Set<Work> list, String region) throws SearchStringTooSmall {
        Vector<Dvd>    result = new Vector<>();

        if (region == null || region.length() < 3) {
            throw new SearchStringTooSmall("querry is too small");
        }
        for (Work em: list) {
            if (em instanceof Dvd d && d.isRegion(region)) {
                result.add(d);
            }
        }
        return (result);
    }

    /**
     * Searches through a Set of Works for a specific Book by its ISBN.
     * @param list  Set of unique {@link Work} elements to search through.
     * @param isbn  The ISBN identifier to match.
     * @return      The matching {@link Book} instance, or {@code null} if no match is found.
     * @throws      SearchStringTooSmall If the ISBN string is null or shorter than 3 characters.
     */
    public static Book searchByIsbn(Set<Work> list, String isbn) throws SearchStringTooSmall {
        if (isbn == null || isbn.length() < 3) {
            throw new SearchStringTooSmall("querry is too small");
        }
        for (Work em: list) {
            if (em instanceof Book l && l.isIsbn(isbn)) {
                return (l);
            }
        }
        return (null);
    }

    /**
     * Performs a flexible string-based search using a functional extractor.
     * <p>
     * The search is case-insensitive and ignores leading/trailing whitespace.
     * </p>
     * @param list      Set of unique {@link Work} elements to search through.
     * @param query     The search string.
     * @param extractor A {@link Function} that defines which attribute of the Work to search (e.g., Work::getTitle).
     * @return          A {@link Vector} of {@link Work} objects containing the query string.
     * @throws          SearchStringTooSmall If the query (after trimming) is null or shorter than 3 characters.
     */
    public static Vector<Work>    search(Set<Work> list, String querry, Function<Work, String> exctractor) throws SearchStringTooSmall {
        Vector<Work>  result = new Vector<>();
        String        search;

        if (querry == null || querry.length() < 3) {
            throw new SearchStringTooSmall("querry too small");
        }
        search = querry.trim().toLowerCase();
        for (Work em: list) {
            String  value = exctractor.apply(em);
            if (value != null && value.toLowerCase().contains(search)) {
                result.add(em);
            }
        }
        return (result);
    }

    /**
     * Filters a Set of Works based on their publication date.
     * @param list    Set of unique {@link Work} elements to search through.
     * @param pubDate The exact {@link Date} of publication to match.
     * @return        A {@link Vector} of works published on that exact date.
     */
    public static Vector<Work>    searchByPubDate(Set<Work> list, Date pubDate) {
        Vector<Work>  result = new Vector<>();
        for (Work em: list) {
            if (em.getPublicationDate().equals(pubDate)) {
                result.add(em);
            }
        }
        return (result);
    }

    /**
     * Filters a collection of works by their specific class type.
     * @param <T>        The specific subtype of {@link Work} to return.
     * @param list       Set of unique {@link Work} elements to search through.
     * @param targetType The class literal (e.g., Dvd.class) to filter by.
     * @return           A {@link Vector} containing only instances of the target type.
     * @throws SearchClassNotInherits If the target type does not extend {@link Work}.
     */
    public static <T extends Work> Vector<T> searchByType(Set<Work> list, Class<T> targetType) throws SearchClassNotInherits{
        Vector<T> result = new Vector<>();
        if (targetType == null) {
            throw new SearchClassNotInherits("Target type cannot be null");
        }
        if (!Work.class.isAssignableFrom(targetType)) {
            throw new SearchClassNotInherits("The class " + targetType.getSimpleName() + " does not inherit from Work");
        }
        if (list == null) {
            return result;
        }
        for (Work em : list) {
            if (targetType.isInstance(em)) {
                result.add(targetType.cast(em));
            }
        }
        return(result);
    }
}
