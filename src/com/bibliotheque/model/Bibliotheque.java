package com.bibliotheque.model;

import com.bibliotheque.errors.SearchStringTooSmall;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents the main library system (Bibliothèque).
 * 
 * <p>Acts as the central registry and coordinator for the library's catalog, 
 * registered users, staff, scheduled events, and lending operations.</p>
 *
 * @version 1.0
 */
public class Bibliotheque {

    private String _nom;
    private String _adresse;
    
    private final Set<Work>      _catalogue  = new HashSet<>();
    private final Set<Author>    _authors    = new HashSet<>();
    private final Set<Member>    _members    = new HashSet<>();
    private final Set<Librarian> _librarians = new HashSet<>();
    private final Set<Event>     _events     = new HashSet<>();

    /**
     * Constructs a new {@code Bibliotheque} with the specified name and address.
     * 
     * @param nom     the name of the library
     * @param adresse the physical address of the library
     */
    public Bibliotheque(String nom, String adresse) {
        this._nom = nom;
        this._adresse = adresse;
    }

    // --- Entity Management ---
    
    /**
     * Adds a literary work to the library catalog.
     * 
     * @param work the {@link Work} to add to the catalog
     */
    public void addWork(Work work)           { this._catalogue.add(work); }

    /**
     * Registers a new author within the library system.
     * 
     * @param author the {@link Author} to add
     */
    public void addAuthor(Author author)     { this._authors.add(author); }

    /**
     * Registers a new member with the library.
     * 
     * @param member the {@link Member} to register
     */
    public void addMember(Member member)     { this._members.add(member); }

    /**
     * Adds a staff librarian to the library system.
     * 
     * @param lib the {@link Librarian} to add
     */
    public void addLibrarian(Librarian lib)  { this._librarians.add(lib); }

    /**
     * Schedules a new event organized by the library.
     * 
     * @param event the {@link Event} to add
     */
    public void addEvent(Event event)        { this._events.add(event); }

    /**
     * Processes and creates a new book lending transaction (Borrow).
     * 
     * <p>This method enforces critical business logic: ensuring all parameters are non-null, 
     * confirming the requested copy is available, verifying the member is not blocked, 
     * and establishing biographical references across the associated objects.</p>
     * 
     * @param copy         the specific physical {@link Copy} to be borrowed
     * @param borrowedBy   the {@link Member} performing the borrow
     * @param validatedBy  the {@link Librarian} authorizing the transaction
     * @param expectedDate the scheduled return deadline date
     * @return the successfully initialized {@link Borrow} record
     * @throws IllegalArgumentException if any of the entity parameters are {@code null}
     * @throws IllegalStateException    if the copy is unavailable or if the member is blocked
     */
    public Borrow createBorrow(Copy copy, Member borrowedBy, Librarian validatedBy, Date expectedDate) {
        if (copy == null || borrowedBy == null || validatedBy == null) {
            throw new IllegalArgumentException("Les paramètres ne peuvent pas être nuls.");
        }
        if (!copy.isAvailable()) {
            throw new IllegalStateException("Cette copie n'est pas disponible actuellement.");
        }
        if (borrowedBy.isBlocked()) {
            throw new IllegalStateException("Le membre est bloqué et ne peut pas emprunter.");
        }
        Borrow borrow = new Borrow(expectedDate, validatedBy, borrowedBy, copy);
        copy.addBorrowing(borrow);
        borrowedBy.addBorrow(borrow);
        
        // Links the transaction history record directly to the validating librarian
        validatedBy.addValidatedBorrow(borrow); 
        
        return borrow;
    }

    /**
     * Completely removes an author from the system registry and unlinks them from any associated catalog works.
     * 
     * @param firstName the explicit first name criteria of the author to drop
     * @param lastName  the explicit last name criteria of the author to drop
     */
    public void removeAuthor(String firstName, String lastName) {
        _authors.removeIf(a -> a.getFirstName().equals(firstName)
                            && a.getLastName().equals(lastName));
        
        _catalogue.forEach(w -> w.removeAuthor(firstName, lastName));
    }

    /**
     * Updates the metadata of an existing work in the catalogue.
     *
     * <p>Directly mutates the fields of the existing instance, avoiding
     * the need to recreate an object and lose its reference in the catalogue.</p>
     *
     * @param target      the existing work to update; must be present in the catalogue
     * @param title       the new title
     * @param category    the new call number (cote)
     * @param editor      the new editor / publishing house
     * @param pubDate     the new publication date
     * @param authors     the new list of authors
     * @param extraField  the ISBN (if Book) or the region code (if Dvd)
     * @throws IllegalArgumentException if {@code target} is null or not found in the catalogue
     */
    public void updateWork(Work target, String title, String category,
                        String editor, Date pubDate,
                        List<Author> authors, String extraField) {
        if (target == null || !_catalogue.contains(target)) {
            throw new IllegalArgumentException("L'œuvre cible est introuvable dans le catalogue.");
        }

        target.setTitle(title);
        target.setCategory(category);
        target.setEditor(editor);
        target.setPublicationDate(pubDate);

        // Réinitialise les auteurs
        new HashSet<>(target.getAuthors())
            .forEach(a -> target.removeAuthor(a.getFirstName(), a.getLastName()));
        authors.forEach(target::addAuthor);

        // Champ spécifique au type
        if (target instanceof Book) {
            ((Book) target).setIsbn(extraField);
        } else if (target instanceof Dvd) {
            ((Dvd) target).setRegion(extraField);
        }
    }

    // --- Search Queries ---
    
    /**
     * Searches for cataloged works matching the provided textual query string by title.
     * 
     * @param q the text or search keyword to look up
     * @return a {@link List} of matching {@link Work} elements
     * @throws SearchStringTooSmall if the search query length does not meet the minimal requirements defined by SearchingWork
     */
    public List<Work> findWorksByTitle(String q) throws SearchStringTooSmall {
        return SearchingWork.search(this._catalogue, q, Work::getTitle);
    }

    /**
     * Searches the registry for authors whose first name matches the provided criteria.
     * 
     * @param name the target first name to evaluate
     * @return a {@link List} of matching {@link Author} instances
     */
    public List<Author> findAuthorsByName(String name) {
        return this._authors.stream()
                .filter(a -> a.isFirstName(name))
                .collect(Collectors.toList());
    }

    /**
     * Searches the registry for authors whose last name matches the provided criteria.
     * 
     * @param surname the target last name to evaluate
     * @return a {@link List} of matching {@link Author} instances
     */
    public List<Author> findAuthorsBySurname(String surname) {
        return this._authors.stream()
                .filter(a -> a.isLastName(surname))
                .collect(Collectors.toList());
    }

    /**
     * Compiles a comprehensive list of all ongoing borrow transactions across the catalog 
     * that are currently unreturned and past their due date.
     * 
     * @return a {@link List} containing overdue {@link Borrow} instances
     */
    public List<Borrow> getLateBorrows() {
        return this._catalogue.stream()
                .flatMap(w -> w.getCopies().stream())
                .flatMap(c -> c.getBorrowings().stream())
                .filter(b -> !b.isReturned() && b.isLate())
                .collect(Collectors.toList());
    }

    // --- Getters ---
    
    /**
     * Returns the name of the library.
     * 
     * @return the library name
     */
    public String getNom()      { return _nom; }

    /**
     * Returns the physical location address of the library.
     * 
     * @return the library address
     */
    public String getAdresse()  { return _adresse; }

    /**
     * Returns an unmodifiable view of the current catalog works.
     * 
     * @return an unmodifiable {@link Set} of {@link Work} records
     */
    public Set<Work> getCatalogue()     { return Collections.unmodifiableSet(_catalogue); }

    /**
     * Returns an unmodifiable view of all registered authors.
     * 
     * @return an unmodifiable {@link Set} of {@link Author} entities
     */
    public Set<Author> getAuthors()     { return Collections.unmodifiableSet(_authors); }

    /**
     * Returns an unmodifiable view of all registered library members.
     * 
     * @return an unmodifiable {@link Set} of {@link Member} entities
     */
    public Set<Member> getMembers()     { return Collections.unmodifiableSet(_members); }

    /**
     * Returns an unmodifiable view of all professional library staff records.
     * 
     * @return an unmodifiable {@link Set} of {@link Librarian} entities
     */
    public Set<Librarian> getLibrarians() { return Collections.unmodifiableSet(_librarians); }

    /**
     * Retrieves a specific work from the catalog by its sequential index based on title sorting order.
     * 
     * <p>Explicitly applies alphabetic sorting over the catalog set to bypass unpredictable 
     * iteration order variations typical to internal HashSet storage structures.</p>
     * 
     * @param i the sorted index element offset to retrieve
     * @return the matching sorted {@link Work} entry
     * @throws IndexOutOfBoundsException if the provided index parameter falls outside the catalog dimensions
     */
    public Work get(int i) {
        return _catalogue.stream()
            .sorted(Comparator.comparing(Work::getTitle))
            .skip(i)
            .findFirst()
            .orElseThrow(() -> new IndexOutOfBoundsException("Index: " + i));
    }

}