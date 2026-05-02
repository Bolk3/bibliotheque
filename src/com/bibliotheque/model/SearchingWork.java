package com.bibliotheque.model;

import com.bibliotheque.errors.SearchClassNotInherits;
import com.bibliotheque.errors.SearchStringTooSmall;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Utility class providing static methods to filter and search through
 * collections of {@link Work}.
 *
 * <p>This class supports several search strategies:</p>
 * <ul>
 *   <li>Type-specific attribute filtering — ISBN for {@link Book},
 *       region code for {@link Dvd}</li>
 *   <li>Functional string extraction — flexible case-insensitive search
 *       on any {@link Work} attribute via a {@link Function} extractor</li>
 *   <li>Publication date filtering</li>
 *   <li>Runtime type filtering via {@link #searchByType(Set, Class)}</li>
 * </ul>
 *
 * <p>All methods are {@code static} — this class is not meant to be
 * instantiated.</p>
 *
 * <p>String-based search methods enforce a minimum query length of 3
 * characters and throw {@link SearchStringTooSmall} otherwise, to prevent
 * overly broad searches.</p>
 *
 * @see Work
 * @see Book
 * @see Dvd
 * @see SearchStringTooSmall
 * @see SearchClassNotInherits
 *
 * @version 1.1
 */
public class SearchingWork {

    /**
     * Private constructor — this class is a static utility and should
     * never be instantiated.
     */
    private SearchingWork() {}

    // -------------------------------------------------------------------------
    // Type-specific searches
    // -------------------------------------------------------------------------

    /**
     * Filters a set of works for {@link Dvd} instances matching a specific
     * region code.
     *
     * <p>Only elements that are instances of {@link Dvd} and whose region
     * code matches {@code region} exactly are included in the result.</p>
     *
     * @param catalogue the set of {@link Work}s to search through;
     *                  must not be {@code null}
     * @param region    the region code to match; must not be {@code null}
     *                  and must be at least 3 characters long
     * @return a {@link List} of {@link Dvd}s matching the region code;
     *         never {@code null}, but may be empty
     *
     * @throws SearchStringTooSmall if {@code region} is {@code null} or
     *                              shorter than 3 characters
     */
    public static List<Dvd> searchByRegion(Set<Work> catalogue, String region)
            throws SearchStringTooSmall {
        if (region == null || region.length() < 3)
            throw new SearchStringTooSmall("Query is too small.");
        return catalogue.stream()
                .filter(w -> w instanceof Dvd)
                .map(w -> (Dvd) w)
                .filter(d -> d.isRegion(region))
                .collect(Collectors.toList());
    }

    /**
     * Searches a set of works for a {@link Book} matching a specific ISBN.
     *
     * <p>Since an ISBN uniquely identifies a book edition, at most one result
     * is expected. The result is wrapped in an {@link Optional} to avoid
     * returning {@code null}.</p>
     *
     * @param catalogue the set of {@link Work}s to search through;
     *                  must not be {@code null}
     * @param isbn      the ISBN identifier to match; must not be {@code null}
     *                  and must be at least 3 characters long
     * @return an {@link Optional} containing the matching {@link Book},
     *         or {@link Optional#empty()} if no match is found
     *
     * @throws SearchStringTooSmall if {@code isbn} is {@code null} or
     *                              shorter than 3 characters
     */
    public static Optional<Book> searchByIsbn(Set<Work> catalogue, String isbn)
            throws SearchStringTooSmall {
        if (isbn == null || isbn.length() < 3)
            throw new SearchStringTooSmall("Query is too small.");
        return catalogue.stream()
                .filter(w -> w instanceof Book)
                .map(w -> (Book) w)
                .filter(b -> b.isIsbn(isbn))
                .findFirst();
    }

    // -------------------------------------------------------------------------
    // Generic attribute search
    // -------------------------------------------------------------------------

    /**
     * Performs a flexible, case-insensitive string search on any attribute
     * of a {@link Work}, using a functional extractor to select which field
     * to search.
     *
     * <p>The query is trimmed and lowercased before comparison. A work is
     * included in the result if the extracted value contains the query as a
     * substring. Works for which the extractor returns {@code null} are
     * silently skipped.</p>
     *
     * <p>Example usage:</p>
     * <pre>{@code
     * // Search by title
     * SearchingWork.search(catalogue, "hobbit", Work::getTitle);
     *
     * // Search by editor
     * SearchingWork.search(catalogue, "gallimard", Work::getEditor);
     * }</pre>
     *
     * @param catalogue the set of {@link Work}s to search through;
     *                  must not be {@code null}
     * @param query     the search string; must not be {@code null} and must
     *                  be at least 3 characters long after trimming
     * @param extractor a {@link Function} that extracts the string attribute
     *                  to search from each {@link Work} (e.g.
     *                  {@code Work::getTitle}, {@code Work::getEditor});
     *                  must not be {@code null}
     * @return a {@link List} of {@link Work}s whose extracted attribute
     *         contains the query; never {@code null}, but may be empty
     *
     * @throws SearchStringTooSmall if {@code query} is {@code null} or
     *                              shorter than 3 characters
     */
    public static List<Work> search(Set<Work> catalogue, String query,
                                    Function<Work, String> extractor)
            throws SearchStringTooSmall {
        if (query == null || query.trim().length() < 3)
            throw new SearchStringTooSmall("Query is too small.");
        String search = query.trim().toLowerCase();
        return catalogue.stream()
                .filter(w -> {
                    String value = extractor.apply(w);
                    return value != null && value.toLowerCase().contains(search);
                })
                .collect(Collectors.toList());
    }

    // -------------------------------------------------------------------------
    // Date search
    // -------------------------------------------------------------------------

    /**
     * Filters a set of works by their exact publication date.
     *
     * <p>Comparison uses {@link Date#equals(Object)}, so the provided date
     * must match down to the millisecond. For broader date ranges, consider
     * implementing a range-based overload.</p>
     *
     * @param catalogue the set of {@link Work}s to search through;
     *                  must not be {@code null}
     * @param pubDate   the exact publication date to match;
     *                  must not be {@code null}
     * @return a {@link List} of {@link Work}s published on that exact date;
     *         never {@code null}, but may be empty
     */
    public static List<Work> searchByPubDate(Set<Work> catalogue, Date pubDate) {
        return catalogue.stream()
                .filter(w -> w.getPublicationDate().equals(pubDate))
                .collect(Collectors.toList());
    }

    // -------------------------------------------------------------------------
    // Type filtering
    // -------------------------------------------------------------------------

    /**
     * Filters a set of works by their runtime type, returning only instances
     * of the specified subclass of {@link Work}.
     *
     * <p>The generic bound {@code <T extends Work>} ensures at compile time
     * that only valid subtypes of {@link Work} can be passed as
     * {@code targetType}.</p>
     *
     * <p>Example usage:</p>
     * <pre>{@code
     * List<Dvd>  dvds  = SearchingWork.searchByType(catalogue, Dvd.class);
     * List<Book> books = SearchingWork.searchByType(catalogue, Book.class);
     * }</pre>
     *
     * @param <T>        the specific subtype of {@link Work} to return
     * @param catalogue  the set of {@link Work}s to search through;
     *                   must not be {@code null}
     * @param targetType the {@link Class} literal of the desired subtype
     *                   (e.g. {@code Dvd.class}, {@code Book.class});
     *                   must not be {@code null}
     * @return a {@link List} containing only instances of {@code targetType};
     *         never {@code null}, but may be empty
     *
     * @throws SearchClassNotInherits if {@code targetType} is {@code null}
     */
    public static <T extends Work> List<T> searchByType(Set<Work> catalogue,
                                                         Class<T> targetType)
            throws SearchClassNotInherits {
        if (targetType == null)
            throw new SearchClassNotInherits("Target type cannot be null.");
        return catalogue.stream()
                .filter(targetType::isInstance)
                .map(targetType::cast)
                .collect(Collectors.toList());
    }
}
