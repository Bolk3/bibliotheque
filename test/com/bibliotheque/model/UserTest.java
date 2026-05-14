package com.bibliotheque.model;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Date;

@DisplayName("Tests des Utilisateurs (Member, Librarian, Speaker)")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserTest {

    private static Librarian admin;
    private static Member member;
    private static Speaker speaker;

    @BeforeAll
    static void setup() {
        // Librarian(firstName, lastName, email, handler, position, permission)
        admin = new Librarian("Alice", "Admin", "alice@bib.fr", null, "Directrice", 10);
        member = new Member("Bob", "Subscriber", "bob@mail.com", null);
        speaker = new Speaker("Charlie", "Expert", "charlie@expert.com", null, "Histoire");
    }

    @Nested
    @Order(1)
    @DisplayName("Validation de la classe abstraite User")
    class AbstractUserLogicTest {

        @Test
        @DisplayName("Validation et Normalisation du nom/prénom")
        void testNameNormalization() throws Exception { // Ajout du throws ici
            assertTrue(member.isFirstName("BOB"), "La comparaison devrait être insensible à la casse.");
            // Attention : ton code source a un bug sur isLastName (il appelle isFirstNameValid)
            assertTrue(member.isLastName("Subscriber"), "La comparaison devrait fonctionner.");
        }

        @Test
        @DisplayName("Changement d'email avec validation")
        void testEmailUpdate() throws Exception { // Ajout du throws ici
            member.setEmail("new.bob@mail.com");
            assertEquals("new.bob@mail.com", member.getEmail());
            
            // On teste la sécurité : un mauvais format ne doit pas changer l'email actuel
            member.setEmail("invalid-email"); 
            assertEquals("new.bob@mail.com", member.getEmail(), "L'email ne devrait pas avoir changé car le format est invalide.");
        }
    }

    @Nested
    @Order(2)
    @DisplayName("Spécificités du Membre (Member)")
    class MemberTest {

        @Test
        @DisplayName("Gestion des pénalités et permissions")
        void testPenalties() {
            member.addPenalty(15.50, admin);
            assertEquals(15.50, member.getPenalty());
            
            member.resetPenalty(admin);
            assertEquals(0.0, member.getPenalty());
        }

        @Test
        @DisplayName("Blocage du compte")
        void testBlockingStatus() {
            member.setBlocked(true, admin);
            assertTrue(member.isBlocked());
            
            Librarian stagiaire = new Librarian("Tom", "Stagiaire", "tom@bib.fr", null, "Stagiaire", 0);
            
            assertThrows(IllegalStateException.class, () -> {
                member.setBlocked(false, stagiaire);
            }, "Un stagiaire ne devrait pas pouvoir débloquer un membre.");
        }
    }

    @Nested
    @Order(3)
    @DisplayName("Spécificités du Bibliothécaire (Librarian)")
    class LibrarianTest {

        @Test
        @DisplayName("Suivi de l'activité (Audit)")
        void testLibrarianActivity() {
            Work work = new Book("999", "Titre", "CAT", "Ed", new Date(), null);
            Copy copy = new Copy(State.NEUF, work);
            Borrow borrow = new Borrow(new Date(), admin, member, copy);
            
            admin.addValidatedBorrow(borrow);
            
            assertEquals(1, admin.getValidatedBorrows().size());
            assertTrue(admin.getValidatedBorrows().contains(borrow));
        }
    }

    @Nested
    @Order(4)
    @DisplayName("Spécificités de l'Intervenant (Speaker)")
    class SpeakerTest {

        @Test
        @DisplayName("Gestion de la spécialité")
        void testSpecialty() {
            assertTrue(speaker.isSpecialty("HISTOIRE"));
            speaker.setSpecialty("Archéologie");
            assertEquals("Archéologie", speaker.getSpecialty());
        }
    }
}