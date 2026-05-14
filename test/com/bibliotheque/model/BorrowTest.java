package com.bibliotheque.model;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Date;

@DisplayName("Tests de Logique d'Emprunt (Borrow)")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BorrowTest {

    private static Librarian admin;
    private static Member member;
    private static Copy copy;
    private Borrow borrow;

    @BeforeAll
    static void setupGlobal() {
        // Configuration minimale pour les objets requis
        admin = new Librarian("Admin", "User", "admin@bib.fr", null, "Manager", 10);
        member = new Member("John", "Doe", "john@doe.com", null);
        Work work = new Book("123", "Titre", "CAT", "Edit", new Date(), null);
        copy = new Copy(State.NEUF, work);
    }

    @BeforeEach
    void initBorrow() {
        // On crée un emprunt frais avant chaque test
        // Échéance à +24h
        Date due = new Date(System.currentTimeMillis() + 86400000);
        borrow = new Borrow(due, admin, member, copy);
    }

    @Nested
    @Order(1)
    @DisplayName("Cycle de vie et États")
    class LifecycleTest {

        @Test
        @DisplayName("Initialisation correcte de l'état")
        void testInitialState() {
            assertAll("Vérification initialisation",
                () -> assertEquals(State.NEUF.toString(), borrow.getInitialState()),
                () -> assertFalse(borrow.isReturned()),
                () -> assertFalse(borrow.isLate())
            );
        }

        @Test
        @DisplayName("Détection de dommage au retour")
        void testDamageDetection() {
            // Note : Pour que returnBook passe, Settings.hasAccess doit être vrai.
            // Si vous n'avez pas de mock, assurez-vous que Settings autorise admin.
            borrow.returnBook(State.ABIME.toString(), admin);
            
            assertTrue(borrow.isReturned());
            assertTrue(borrow.isDamaged(), "Le livre devrait être marqué comme endommagé.");
            assertEquals(State.ABIME, copy.getState(), "L'état de la Copy doit avoir été mis à jour.");
        }
    }

    @Nested
    @Order(2)
    @DisplayName("Logic temporelle (Extensions et Retards)")
    class TimeLogicTest {

        @Test
        @DisplayName("Prolongation de la date d'échéance")
        void testExtension() {
            Date initialDue = borrow.getExpectedDate();
            Date newDue = new Date(initialDue.getTime() + 86400000); // +1 jour
            
            borrow.extendsDate(newDue, admin);
            
            assertEquals(newDue, borrow.getExpectedDate());
            assertEquals(1, borrow.getExtensions().size());
        }

        @Test
        @DisplayName("Refus d'extension si date antérieure")
        void testInvalidExtensionDate() {
            Date pastDate = new Date(System.currentTimeMillis() - 10000);
            
            assertThrows(IllegalArgumentException.class, () -> {
                borrow.extendsDate(pastDate, admin);
            }, "On ne devrait pas pouvoir prolonger à une date passée.");
        }

        @Test
        @DisplayName("Vérification du retard")
        void testIsLate() {
            // On crée un emprunt déjà expiré
            Date pastDue = new Date(System.currentTimeMillis() - 10000);
            Borrow lateBorrow = new Borrow(pastDue, admin, member, copy);
            
            assertTrue(lateBorrow.isLate(), "L'emprunt devrait être considéré en retard.");
        }
    }

    @Nested
    @Order(3)
    @DisplayName("Contraintes et Verrous")
    class ConstraintsTest {

        @Test
        @DisplayName("Verrouillage après retour")
        void testNoActionsAfterReturn() {
            borrow.returnBook(State.NEUF.toString(), admin);
            
            assertThrows(IllegalStateException.class, () -> {
                borrow.extendsDate(new Date(), admin);
            }, "Impossible de prolonger un livre déjà rendu.");
            
            assertThrows(IllegalStateException.class, () -> {
                borrow.returnBook(State.NEUF.toString(), admin);
            }, "Impossible de rendre deux fois le même emprunt.");
        }
    }
}