package com.bibliotheque.model;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Date;
import java.util.List;

@DisplayName("Tests du Système Central (Bibliotheque)")
public class BibliothequeTest {
    private Bibliotheque biblio;
    private Member member;
    private Librarian staff;
    private Book book;
    private Copy copy;

    @BeforeEach
    void setUp() {
        biblio = new Bibliotheque("La Grande Bibliothèque", "Paris");
        member = new Member("M01", "Dupont", "Jean", biblio);
        staff  = new Librarian("S01", "Martin", "Alice", biblio, "alice@biblio.com", 5);
        book   = new Book("978-1", "Java Testing", "Tech", "Oracle", new Date(), null);
        copy   = new Copy(State.NEUF, book);
        book.addCopy(copy);
        biblio.addWork(book);
        biblio.addMember(member);
        biblio.addLibrarian(staff);
    }

    @Nested
    @DisplayName("Tests de la Logique d'Emprunt")
    class BorrowingLogic {
        @Test
        @DisplayName("Création d'un emprunt valide")
        void testSuccessfulBorrow() {
            Date tomorrow = new Date(System.currentTimeMillis() + 86400000);
            Borrow borrow = biblio.createBorrow(copy, member, staff, tomorrow);
            assertAll("Vérification des impacts de l'emprunt",
                () -> assertNotNull(borrow),
                () -> assertFalse(copy.isAvailable(), "L'exemplaire ne doit plus être disponible"),
                () -> assertTrue(member.getBorrows().contains(borrow), "L'emprunt doit être listé chez le membre")
            );
        }

        @Test
        @DisplayName("Refus d'emprunt si le membre est bloqué")
        void testBorrowBlockedMember() {
            member.setBlocked(true, staff);
            Date due = new Date(System.currentTimeMillis() + 86400000);
            assertThrows(IllegalStateException.class, () -> {
                biblio.createBorrow(copy, member, staff, due);
            }, "Un membre bloqué ne devrait pas pouvoir emprunter");
        }
    }

    @Nested
    @DisplayName("Tests des Statistiques et Rapports")
    class ReportsAndStats {
        @Test
        @DisplayName("Détection des emprunts en retard")
        void testGetLateBorrows() {
            Date pastDate = new Date(System.currentTimeMillis() - 3600000);
            biblio.createBorrow(copy, member, staff, pastDate);
            List<Borrow> lateOnes = biblio.getLateBorrows();
            assertFalse(lateOnes.isEmpty(), "L'emprunt devrait être détecté comme en retard");
        }

        @Test
        @DisplayName("Un emprunt rendu n'est plus en retard")
        void testNoLateIfReturned() {
            Date longAgo = new Date(System.currentTimeMillis() - 1000000000L);
            Borrow b = biblio.createBorrow(copy, member, staff, longAgo);
            b.returnBook(State.BON.toString(), staff);
            assertTrue(b.isReturned(), "L'emprunt doit être marqué comme rendu");
            assertTrue(biblio.getLateBorrows().isEmpty(), "Un livre rendu ne doit plus apparaître dans les retards");
        }
    }

    @Nested
    @DisplayName("Tests de la Gestion des Auteurs")
    class AuthorManagement {
        private Author author;
        private Book bookWithAuthor;

        @BeforeEach
        void setUpAuthor() {
            author = new Author("Victor", "Hugo");
            biblio.addAuthor(author);
            bookWithAuthor = new Book("978-2", "Les Misérables", "Roman", "Gallimard", new Date(), null);
            bookWithAuthor.addAuthor(author);
            biblio.addWork(bookWithAuthor);
        }

        @Test
        @DisplayName("Ajout d'un auteur dans la bibliothèque")
        void testAddAuthor() {
            List<Author> found = biblio.findAuthorsByName("Victor");
            assertFalse(found.isEmpty(), "L'auteur doit être trouvable après ajout");
        }

        @Test
        @DisplayName("Suppression d'un auteur de la bibliothèque")
        void testRemoveAuthor() {
            biblio.removeAuthor("Victor", "Hugo");
            List<Author> found = biblio.findAuthorsByName("Victor");
            assertTrue(found.isEmpty(), "L'auteur ne doit plus être dans la bibliothèque");
        }

        @Test
        @DisplayName("Suppression d'un auteur le retire aussi des works")
        void testRemoveAuthorAlsoRemovesFromWorks() {
            assertTrue(bookWithAuthor.getAuthors().contains(author));
            biblio.removeAuthor("Victor", "Hugo");
            assertFalse(bookWithAuthor.getAuthors().contains(author), "L'auteur doit être retiré du work");
        }

        @Test
        @DisplayName("Suppression d'un auteur inexistant ne plante pas")
        void testRemoveNonExistentAuthor() {
            assertDoesNotThrow(() -> biblio.removeAuthor("Inconnu", "Inconnu"));
        }

        @Test
        @DisplayName("Un work sans auteur après suppression est toujours valide")
        void testWorkRemainsValidAfterAuthorRemoval() {
            biblio.removeAuthor("Victor", "Hugo");
            assertTrue(bookWithAuthor.getAuthors().isEmpty());
            assertNotNull(bookWithAuthor.getTitle());
        }
    }
}