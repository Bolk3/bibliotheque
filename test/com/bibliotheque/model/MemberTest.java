package com.bibliotheque.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests unitaires - Classe Member")
class MemberTest {

    private Bibliotheque library;
    private Librarian adminLibrarian;
    private Librarian lowPermLibrarian;

    @BeforeEach
    void setUp() {
        library = new Bibliotheque("Centrale", "Paris");
        // Configuration des bibliothécaires pour tester l'accès via Settings
        adminLibrarian = new Librarian("Alice", "Admin", "alice@biblio.fr", library, "Directrice", 5);
        lowPermLibrarian = new Librarian("Bob", "Stagiaire", "bob@biblio.fr", library, "Stagiaire", 1);
    }

    @Nested
    @DisplayName("Tests d'initialisation et d'opérations de base")
    class BasicOperationsTests {

        @Test
        @DisplayName("Un membre doit être initialisé sans dettes et non bloqué")
        void shouldInitializeWithDefaultStatus() {
            Member member = new Member("Jean", "Dupont", "jean@mail.com", library);

            assertEquals(0.0, member.getPenalty());
            assertFalse(member.isBlocked());
            assertTrue(member.getBorrows().isEmpty());
            assertTrue(member.getParticipatedEvents().isEmpty());
            assertEquals("Jean Dupont", member.toString());
        }

        @Test
        @DisplayName("L'ajout d'emprunts et d'événements doit ignorer les doublons et le null")
        void shouldHandleCollectionsSafely() {
            Member member = new Member("Jean", "Dupont", "jean@mail.com", library);
            Book book = new Book("123", "Titre", "C-1", "Editeur", new Date(), library);
            Copy copy = new Copy(State.BON, book);
            Borrow borrow = new Borrow(new Date(), adminLibrarian, member, copy);

            // Ajout valide
            member.addBorrow(borrow);
            assertEquals(1, member.getBorrows().size());

            // Doublon & Null
            member.addBorrow(borrow);
            member.addBorrow(null);
            assertEquals(1, member.getBorrows().size(), "Les doublons et valeurs nulles doivent être ignorés");
        }
    }

    @Nested
    @DisplayName("Tests des règles métier et droits d'accès (Pénalités & Blocages)")
    class PermissionRulesTests {

        @Test
        @DisplayName("Un bibliothécaire autorisé doit pouvoir appliquer et réinitialiser les pénalités")
        void shouldAllowPenaltyOperationsForAuthorizedStaff() {
            Member member = new Member("Jean", "Dupont", "jean@mail.com", library);

            member.addPenalty(15.50, adminLibrarian);
            assertEquals(15.50, member.getPenalty());

            member.resetPenalty(adminLibrarian);
            assertEquals(0.0, member.getPenalty());
        }

        @Test
        @DisplayName("Un bibliothécaire non autorisé doit se faire rejeter par une exception")
        void shouldRejectPenaltyOperationsForUnauthorizedStaff() {
            Member member = new Member("Jean", "Dupont", "jean@mail.com", library);

            assertThrows(IllegalStateException.class, () -> member.addPenalty(10.0, lowPermLibrarian));
            assertThrows(IllegalStateException.class, () -> member.setBlocked(true, lowPermLibrarian));
            assertThrows(IllegalStateException.class, () -> member.resetPenalty(lowPermLibrarian));
        }

        @Test
        @DisplayName("Le statut de blocage doit être modifiable par le personnel habilité")
        void shouldAllowBlockingStatusToChange() {
            Member member = new Member("Jean", "Dupont", "jean@mail.com", library);

            member.setBlocked(true, adminLibrarian);
            assertTrue(member.isBlocked());
        }
    }
}