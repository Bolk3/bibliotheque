import errors.SearchClassNotInherits;
import errors.SearchStringTooSmall;
import java.util.function.Function;
import java.util.Date;
import java.util.Set;
import java.util.Vector;

public class SearchingWork {
    /**
     * Search through a Set of Dvds with the region as querry.
     * @param list      Set of unique element to search through
     * @param region    The discriminator
     * @return          A vector of {@link Dvd} that qualify to the querry
     * @throws          SearchStringTooSmall
     */
    public static Vector<Dvd> searchByRegion(Set<Work> list, String region) throws SearchStringTooSmall {
        Vector<Dvd>    result = new Vector<>();

        if (region == null || region.length() < 3) {
            throw new SearchStringTooSmall("your shearch field is too small");
        }
        for (Work em: list) {
            if (em instanceof Dvd d && d.isRegion(region)) {
                result.add(d);
            }
        }
        return (result);
    }

    /**
     * Search through a Set of Books with the isbn as querry.
     * @param list  Set of unique element to search through
     * @param isbn  The discriminator
     * @return      The {@link Book} that qualify to the querry
     * @throws      SearchStringTooSmall
     */
    public static Book searchByIsbn(Set<Work> list, String isbn) throws SearchStringTooSmall {
        if (isbn == null || isbn.length() < 3) {
            throw new SearchStringTooSmall("your shearch field is too small");
        }
        for (Work em: list) {
            if (em instanceof Book l && l.isIsbn(isbn)) {
                return (l);
            }
        }
        return (null);
    }

    /**
     * Search through a set of work with a string querry.
     * @param list          Set of unique element to search through
     * @param querry        The discriminator
     * @param exctractor    Function to obtain the string atribute of the search
     * @return              A vector of {@link Work} that qualify to the querry
     * @throws              SearchStringTooSmall
     * @see                 java.util.function.Function
     */
    public static Vector<Work>    search(Set<Work> list, String querry, Function<Work, String> exctractor) throws SearchStringTooSmall {
        Vector<Work>  result = new Vector<>();
        String          search = querry.trim().toLowerCase();

        if (search == null || search.length() < 3) {
            throw new SearchStringTooSmall("querry too small");
        }
        for (Work em: list) {
            String  value = exctractor.apply(em);
            if (value != null && value.toLowerCase().contains(search)) {
                result.add(em);
            }
        }
        return (result);
    }

    /**
     * Search through a Set of Works by publication Date.
     * @param list      Set of unique element to search through
     * @param pubDate   The discriminator
     * @return          A Vector of works 
     * @see             java.util.Date
     */
    public static Vector<Work>    searchByPubDate(Set<Work> list, Date pubDate) {
        Vector<Work>  result = new Vector<>();
        for (Work em: list) {
            if (em.getPublicationDate() == pubDate) {
                result.add(em);
            }
        }
        return (result);
    }

    /**
     * Search through a Set of Works by the class they are.
     * @param <T>           Class of the type to search by and the vector to return. T is an children of {@link Work}
     * @param list          Set of unique element to search through
     * @param targetType    The type to search by
     * @return              A vector of Type T
     * @throws              SearchClassNotInherits
     */
    public static <T extends Work> Vector<T> searchByType(Set<Work> list, Class<T> targetType) throws SearchClassNotInherits{
        Vector<T>   result = new Vector<>();
        if (!(Work.class.isAssignableFrom(targetType))) {
            throw new SearchClassNotInherits(null);
        }
        for (Work em : list) {
            if (targetType.isInstance(em)) {
                result.add(targetType.cast(em));
            }
        }
        return(result);
    }
}
