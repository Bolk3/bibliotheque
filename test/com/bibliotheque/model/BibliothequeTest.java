package com.bibliotheque.model;

import com.bibliotheque.errors.SearchStringTooSmall;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests unitaires - Classe principale Bibliotheque")
class BibliothequeTest {

    private Bibliotheque library;
    private Librarian librarian;
    private Member member;
    private Book book;
    private Copy copy;

    @BeforeEach
    void setUp() {
        library = new Bibliotheque("Bibliothèque Nationale", "Paris");
        librarian = new Librarian("Alice", "Admin", "alice@biblio.fr", library, "Directrice", 5);
        member = new Member("Jean", "Dupont", "jean@mail.com", library);
        book = new Book("978-2070415755", "Si c'est un homme", "C-45", "Gallimard", new Date(), library);
        copy = new Copy(State.BON, book); // La copie est par défaut disponible
    }

    @Nested
    @DisplayName("Tests d'ajouts et de gestion du registre")
    class RegistryManagementTests {

        @Test
        @DisplayName("Les entités ajoutées doivent être correctement indexées dans leurs sets respectifs")
        void shouldAddEntitiesToRegistry() {
            Author author = new Author("Primo", "Levi");
            Event event = new Event(new Date(), new Date(), "Lecture", library);

            library.addWork(book);
            library.addAuthor(author);
            library.addMember(member);
            library.addLibrarian(librarian);
            library.addEvent(event);

            assertEquals(1, library.getCatalogue().size());
            assertEquals(1, library.getAuthors().size());
            assertEquals(1, library.getMembers().size());
            assertEquals(1, library.getLibrarians().size());
        }

        @Test
        @DisplayName("get(int i) doit retourner une œuvre selon le tri alphabétique des titres")
        void shouldGetWorkByTitleSortedIndex() {
            Book bookA = new Book("1", "Antigone", "C-1", "Ed", new Date(), library);
            Book bookC = new Book("2", "Phèdre", "C-2", "Ed", new Date(), library);
            Book bookB = new Book("3", "Britannicus", "C-3", "Ed", new Date(), library);

            library.addWork(bookA);
            library.addWork(bookC);
            library.addWork(bookB);

            // Ordre attendu après tri alphabétique : Antigone (0), Britannicus (1), Phèdre (2)
            assertEquals("Antigone", library.get(0).getTitle());
            assertEquals("Britannicus", library.get(1).getTitle());
            assertEquals("Phèdre", library.get(2).getTitle());
        }

        @Test
        @DisplayName("get(int i) doit lever une IndexOutOfBoundsException si l'index dépasse la taille")
        void shouldThrowIndexOutOfBoundsOnInvalidIndex() {
            library.addWork(book);
            assertThrows(IndexOutOfBoundsException.class, () -> library.get(5));
        }
    }

    @Nested
    @DisplayName("Tests des règles métier de création d'emprunt (createBorrow)")
    class LendingOperationsTests {

        @Test
        @DisplayName("createBorrow doit initialiser l'emprunt et propager les liaisons si tout est valide")
        void shouldCreateBorrowSuccessfully() {
            Date expectedReturnDate = new Date(System.currentTimeMillis() + 86400000 * 14); // 14 jours
            
            Borrow borrow = library.createBorrow(copy, member, librarian, expectedReturnDate);

            assertNotNull(borrow);
            assertEquals(expectedReturnDate, borrow.getExpectedDate());
            
            // Vérification des propagations obligatoires demandées par ta Javadoc
            assertTrue(copy.getBorrowings().contains(borrow), "La copie doit enregistrer la transaction");
            assertTrue(member.getBorrows().contains(borrow), "Le membre doit posséder l'emprunt dans sa liste");
            assertTrue(librarian.getValidatedBorrows().contains(borrow), "L'historique du bibliothécaire doit être lié");
        }

        @Test
        @DisplayName("createBorrow doit lever une IllegalArgumentException si un paramètre essentiel est null")
        void shouldThrowExceptionWhenParametersAreNull() {
            assertThrows(IllegalArgumentException.class, () -> 
                library.createBorrow(null, member, librarian, new Date())
            );
        }

        @Test
        @DisplayName("createBorrow doit lever une IllegalStateException si la copie n'est pas disponible")
        void shouldThrowExceptionWhenCopyIsUnavailable() {
            // On simule une copie indisponible en lui ajoutant un emprunt actif non retourné
            Borrow activeBorrow = new Borrow(new Date(), librarian, member, copy);
            copy.addBorrowing(activeBorrow); // Rend copy.isAvailable() -> false

            assertThrows(IllegalStateException.class, () -> 
                library.createBorrow(copy, member, librarian, new Date())
            );
        }

        @Test
        @DisplayName("createBorrow doit lever une IllegalStateException si le membre est bloqué")
        void shouldThrowExceptionWhenMemberIsBlocked() {
            member.setBlocked(true, librarian); // Bloqué par un admin autorité

            assertThrows(IllegalStateException.class, () -> 
                library.createBorrow(copy, member, librarian, new Date())
            );
        }
    }

    @Nested
    @DisplayName("Tests de mise à jour et suppression (Cascades)")
    class MutationAndCascadeTests {

        @Test
        @DisplayName("removeAuthor doit nettoyer le registre de la bibliothèque ET détacher l'auteur de l'œuvre")
        void shouldCascadeAuthorRemoval() {
            Author author = new Author("Primo", "Levi");
            library.addAuthor(author);
            book.addAuthor(author);
            library.addWork(book);

            assertTrue(library.getAuthors().contains(author));
            assertTrue(book.getAuthors().contains(author));

            // Suppression par identité textuelle exacte
            library.removeAuthor("Primo", "Levi");

            assertFalse(library.getAuthors().contains(author), "L'auteur doit disparaître de la bibliothèque");
            assertFalse(book.getAuthors().contains(author), "L'auteur doit être désassocié du livre");
        }

        @Test
        @DisplayName("updateWork doit muter l'œuvre existante sans casser sa référence dans le catalogue")
        void shouldUpdateWorkMetadataInPlace() {
            library.addWork(book);
            List<Author> newAuthors = new ArrayList<>();
            newAuthors.add(new Author("Levi", "Primo"));
            Date newDate = new Date();

            library.updateWork(book, "Si c'est un homme - Nouvelle Édition", "C-46", "Pocket", newDate, newAuthors, "978-PocketISBN");

            // On vérifie que l'instance dans le catalogue a bien muté
            Work internalWork = library.get(0);
            assertEquals("Si c'est un homme - Nouvelle Édition", internalWork.getTitle());
            assertEquals("C-46", internalWork.getCategory());
            assertEquals("Pocket", internalWork.getEditor());
            assertEquals("978-PocketISBN", ((Book) internalWork).getIsbn());
        }
    }

    @Nested
    @DisplayName("Tests de requêtes et de retard (Search & Late)")
    class SearchAndLateQueriesTests {

        @Test
        @DisplayName("getLateBorrows doit isoler uniquement les emprunts non retournés et en retard")
        void shouldFilterOverdueAndUnreturnedBorrows() {
            // 1. Liaison cruciale : On s'assure que la copie appartient bien à l'œuvre
            // (Ajuste le nom de la méthode si elle s'appelle autrement, ex: addCopy ou dans le constructeur de Copy)
            book.addCopy(copy); 
            
            // 2. Enregistrement de l'œuvre dans le catalogue de la bibliothèque
            library.addWork(book);
            
            // 3. Création d'un emprunt dont la date de rendu attendue était hier (-24h)
            Date yesterday = new Date(System.currentTimeMillis() - 86400000);
            Borrow lateBorrow = new Borrow(yesterday, librarian, member, copy);
            copy.addBorrowing(lateBorrow);

            // 4. Exécution de la requête globale
            List<Borrow> overdues = library.getLateBorrows();
            
            // 5. Assertions
            assertEquals(1, overdues.size(), "La liste devrait contenir exactement 1 emprunt en retard");
            assertTrue(overdues.contains(lateBorrow));
        }

        @Test
        @DisplayName("findAuthorsByName et Surname doivent renvoyer des résultats normalisés")
        void shouldFindAuthorsWithInsensitiveCase() {
            Author author = new Author("Victor", "Hugo");
            library.addAuthor(author);

            List<Author> matchFirst = library.findAuthorsByName("victor");
            List<Author> matchLast = library.findAuthorsBySurname("HUGO");

            assertFalse(matchFirst.isEmpty());
            assertFalse(matchLast.isEmpty());
            assertEquals(author, matchFirst.get(0));
        }
    }
}