package com.bibliotheque.model;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Date;

@DisplayName("Tests des Enregistrements (Stamps)")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class StampTest {

    private static Librarian admin;
    private static Borrow loan;
    private static Copy copy;

    @BeforeAll
    static void setupContext() {
        // 1. Initialisation du bibliothécaire
        // (firstname, lastname, email, handler, position, permission)
        admin = new Librarian("Jean", "Dupont", "j.dupont@bibliotheque.fr", null, "Gestionnaire", 5);
        
        // 2. Initialisation d'un membre
        Member member = new Member("John", "Doe", "john.doe@mail.com", null);

        // 3. Initialisation de l'œuvre (Book) - 6 arguments requis d'après l'erreur précédente
        // (title, summary, language, isbn, date, handler)
        Work work = new Book(
            "Le Seigneur des Anneaux", 
            "Une aventure épique en Terre du Milieu.", 
            "Français", 
            "978-2264050373", 
            new Date(), 
            null
        );

        // 4. Initialisation de l'exemplaire (Copy)
        // (State, Work)
        copy = new Copy(State.NEUF, work);

        // 5. Initialisation de l'emprunt (Borrow)
        // (expectedDate, validatedBy, borrowedBy, copy)
        Date due = new Date(System.currentTimeMillis() + 1000000);
        loan = new Borrow(due, admin, member, copy);
    }

    @Nested
    @Order(1)
    @DisplayName("Logique de base (Stamp)")
    class GeneralTest {

        @Test
        @DisplayName("Protection contre la mutation du timestamp (Defensive Copy)")
        void testTimestampImmutability() {
            Stamp stamp = new ReturnStamp("BON", loan, admin);
            
            Date dateFromGetter = stamp.getTimestamp();
            long originalTime = dateFromGetter.getTime();
            
            // Tentative de modification de l'objet Date retourné
            dateFromGetter.setTime(0); 
            
            assertEquals(originalTime, stamp.getTimestamp().getTime(), 
                "Le getter doit renvoyer une copie de la date pour éviter toute modification externe.");
        }

        @Test
        @DisplayName("Vérification des références d'audit")
        void testReferences() {
            Stamp stamp = new ReturnStamp("ABIME", loan, admin);
            assertAll("Audit",
                () -> assertEquals(admin, stamp.getValidator(), "Le validateur enregistré est incorrect."),
                () -> assertEquals(loan, stamp.getReference(), "La référence du prêt est incorrecte.")
            );
        }
    }

    @Nested
    @Order(2)
    @DisplayName("Tampons d'extension (ExtensionStamp)")
    class ExtensionTest {

        @Test
        @DisplayName("Validation de la nouvelle date d'échéance")
        void testExtensionDate() {
            Date newDue = new Date(System.currentTimeMillis() + 2000000);
            ExtensionStamp ext = new ExtensionStamp(newDue, admin, loan);
            
            assertEquals(newDue.getTime(), ext.getExtensionDate().getTime(), 
                "La date d'extension doit correspondre à la date prévue.");
        }
    }

    @Nested
    @Order(3)
    @DisplayName("Tampons de retour (ReturnStamp)")
    class ReturnTest {

        @Test
        @DisplayName("Enregistrement de l'état au retour")
        void testReturnState() {
            String stateDesc = State.ABIME.toString();
            ReturnStamp ret = new ReturnStamp(stateDesc, loan, admin);
            
            assertEquals(stateDesc, ret.getReturnState(), 
                "L'état de retour textuel doit être sauvegardé correctement.");
            assertNotNull(ret.getTimestamp(), "Le timestamp de retour doit être généré automatiquement.");
        }
    }
}