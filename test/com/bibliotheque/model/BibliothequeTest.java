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
        // On met un grade 5 pour avoir accès à PERM_ADMIN_METADATA et PERM_PROCESS_RETURN
        staff = new Librarian("S01", "Martin", "Alice", biblio, "alice@biblio.com", 5);
        
        book = new Book("978-1", "Java Testing", "Tech", "Oracle", new Date(), null);
        copy = new Copy(State.NEUF, book);
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
            // Échéance passée (il y a 1 heure)
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
            
            // Correction finale : Utilisation de la méthode métier returnBook
            // Elle prend l'état sous forme de String et le bibliothécaire
            b.returnBook(State.BON.toString(), staff);
            
            assertTrue(b.isReturned(), "L'emprunt doit être marqué comme rendu");
            assertTrue(biblio.getLateBorrows().isEmpty(), "Un livre rendu ne doit plus apparaître dans les retards");
        }
    }
}