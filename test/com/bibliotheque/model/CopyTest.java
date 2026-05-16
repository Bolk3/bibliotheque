package com.bibliotheque.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Copy (Exemplaire physique)")
class CopyTest {

    private Bibliotheque library;
    private Librarian librarian;
    private Member member;
    private Book book; // Concrétise l'interface ou classe parente Work
    private Date futureDate;

    @BeforeEach
    void setUp() {
        library = new Bibliotheque("Centrale", "Paris");
        librarian = new Librarian("Alice", "Smith", "alice@biblio.fr", library, "Directeur", 5);
        member = new Member("Jean", "Dupont", "jean@mail.com", library);
        book = new Book("12345", "Les Misérables", "C-1", "Éditeur Test", new Date(), library);
        
        // Date d'échéance standard (+14 jours)
        futureDate = new Date(System.currentTimeMillis() + (14L * 24 * 60 * 60 * 1000));
    }

    @Nested
    @DisplayName("Tests d'initialisation et d'état")
    class InitializationTests {

        @Test
        @DisplayName("Une nouvelle copie doit être initialisée correctement")
        void shouldInitializeCopyCorrectly() {
            Copy copy = new Copy(State.NEUF, book);

            assertEquals(State.NEUF, copy.getState(), "L'état initial doit être NEUF");
            assertEquals(book, copy.getReference(), "La référence vers l'œuvre doit être correcte");
            assertTrue(copy.isAvailable(), "Une nouvelle copie doit être disponible par défaut");
            assertFalse(copy.isLate(), "Une nouvelle copie ne peut pas être en retard");
            assertEquals(0, copy.getBorrowCount(), "L'historique des emprunts doit être vide (0)");
            assertTrue(copy.getBorrowings().isEmpty(), "Le Set des emprunts doit être vide");
            assertFalse(copy.getCurrentBorrow().isPresent(), "Il ne doit pas y avoir d'emprunt actif");
        }

        @Test
        @DisplayName("La méthode isState doit répondre correctement")
        void shouldVerifyStateCorrectly() {
            Copy copy = new Copy(State.BON, book);

            assertTrue(copy.isState(State.BON));
            assertFalse(copy.isState(State.ABIME));
        }

        @Test
        @DisplayName("Le setter de statut doit modifier l'état de la copie")
        void shouldAllowStateMutation() {
            Copy copy = new Copy(State.NEUF, book);
            copy.setState(State.ABIME);

            assertEquals(State.ABIME, copy.getState());
        }
    }

    @Nested
    @DisplayName("Tests du cycle de vie des emprunts (addBorrowing & Verfügbarkeit)")
    class BorrowingLifecycleTests {

        @Test
        @DisplayName("Doit réussir à ajouter un emprunt et basculer la disponibilité")
        void shouldAddBorrowingSuccessfully() {
            Copy copy = new Copy(State.BON, book);
            Borrow borrow = new Borrow(futureDate, librarian, member, copy);

            // Action
            copy.addBorrowing(borrow);

            // Vérifications d'état après l'emprunt
            assertFalse(copy.isAvailable(), "La copie ne doit plus être disponible");
            assertEquals(1, copy.getBorrowCount(), "Le compteur d'emprunts doit être à 1");
            
            // Vérification de l'emprunt courant
            Optional<Borrow> currentBorrowOpt = copy.getCurrentBorrow();
            assertTrue(currentBorrowOpt.isPresent(), "Un emprunt actif doit être détecté");
            assertEquals(borrow, currentBorrowOpt.get(), "L'emprunt actif doit être celui qu'on vient d'ajouter");
        }

        @Test
        @DisplayName("Doit lever une exception si on tente d'emprunter une copie déjà prise")
        void shouldThrowExceptionWhenCopyIsAlreadyBorrowed() {
            Copy copy = new Copy(State.BON, book);
            Borrow borrow1 = new Borrow(futureDate, librarian, member, copy);
            Borrow borrow2 = new Borrow(futureDate, librarian, member, copy);

            // Premier emprunt : OK
            copy.addBorrowing(borrow1);

            // Deuxième emprunt sur la même copie non rendue : KO
            IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
                copy.addBorrowing(borrow2);
            });
            
            assertEquals("Copy is already borrowed.", exception.getMessage());
        }

        @Test
        @DisplayName("La copie doit redevenir disponible dès que l'emprunt est retourné")
        void shouldBecomeAvailableAgainAfterReturn() {
            Copy copy = new Copy(State.BON, book);
            Borrow borrow = new Borrow(futureDate, librarian, member, copy);
            
            copy.addBorrowing(borrow);
            assertFalse(copy.isAvailable());

            // On simule le retour du livre
            borrow.returnBook(State.BON.toString(), librarian);

            // Vérifications
            assertTrue(copy.isAvailable(), "La copie doit redevenir disponible après restitution");
            assertFalse(copy.getCurrentBorrow().isPresent(), "Il ne doit plus y avoir d'emprunt actif");
            assertEquals(1, copy.getBorrowCount(), "L'historique doit toujours garder la trace de cet emprunt");
        }
    }

    @Nested
    @DisplayName("Tests de la logique de retard (isLate)")
    class LatenessTests {

        @Test
        @DisplayName("isLate() doit renvoyer false si le livre n'est pas emprunté")
        void shouldReturnFalseWhenNotBorrowed() {
            Copy copy = new Copy(State.BON, book);
            assertFalse(copy.isLate());
        }

        @Test
        @DisplayName("isLate() doit renvoyer true si l'emprunt actif est en retard")
        void shouldReturnTrueWhenActiveBorrowIsLate() {
            Copy copy = new Copy(State.BON, book);
            
            // On crée une date d'échéance passée (ex: hier) pour forcer le retard
            Date pastExpectedDate = new Date(System.currentTimeMillis() - 1000L * 60 * 60 * 24);
            Borrow borrow = new Borrow(pastExpectedDate, librarian, member, copy);
            
            copy.addBorrowing(borrow);

            assertTrue(copy.isLate(), "La copie doit être signalée en retard si l'emprunt l'est");
        }
    }

    @Nested
    @DisplayName("Tests de sécurité de l'encapsulation (Collections)")
    class EncapsulationSecurityTests {

        @Test
        @DisplayName("getBorrowings() doit renvoyer une vue non modifiable")
        void shouldReturnUnmodifiableSet() {
            Copy copy = new Copy(State.BON, book);
            Borrow borrow = new Borrow(futureDate, librarian, member, copy);
            copy.addBorrowing(borrow);

            Set<Borrow> borrowingsSet = copy.getBorrowings();

            // Tenter de modifier le Set directement via l'API Set doit lever une exception
            assertThrows(UnsupportedOperationException.class, () -> {
                borrowingsSet.clear();
            }, "Tenter de vider le set retourné doit provoquer une UnsupportedOperationException");
        }
    }
}