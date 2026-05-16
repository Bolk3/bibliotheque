package com.bibliotheque.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Stamp et son cycle d'audit")
class StampTest {

    private Bibliotheque fakeHandler;
    private Librarian librarian;
    private Borrow fakeBorrow;

    // Classe concrète minimale pour tester uniquement la classe abstraite Stamp
    private static class ConcreteStamp extends Stamp {
        public ConcreteStamp(Date timestamp, Librarian validator, Borrow reference) {
            super(timestamp, validator, reference); 
        }
    }

    @BeforeEach
    void setUp() {
        fakeHandler = new Bibliotheque("Centrale", "Paris");
        librarian = new Librarian("Alice", "Smith", "alice@biblio.fr", fakeHandler, "Gestionnaire", 3);
        
        Book book = new Book("12345", "Test Book", "C-1", "Editeur", new Date(), fakeHandler);
        Copy copy = new Copy(State.BON, book);
        Member member = new Member("Jean", "Dupont", "jean@mail.com", fakeHandler);
        fakeBorrow = new Borrow(new Date(), librarian, member, copy);
    }

    @Nested
    @DisplayName("Tests d'instanciation et d'intégrité des données de Stamp")
    class IntegrityTests {

        @Test
        @DisplayName("Un Stamp doit correctement assigner ses données à la création")
        void shouldInitializeStampCorrectly() {
            Date creationDate = new Date();
            Stamp stamp = new ConcreteStamp(creationDate, librarian, fakeBorrow);

            assertEquals(librarian, stamp.getValidator());
            assertEquals(fakeBorrow, stamp.getReference());
            assertEquals(creationDate, stamp.getTimestamp());
        }

        @Test
        @DisplayName("La date interne du Stamp doit être protégée contre les modifications externes (Immuabilité)")
        void shouldBeProtectedAgainstDateMutation() {
            Date mutableDate = new Date();
            Stamp stamp = new ConcreteStamp(mutableDate, librarian, fakeBorrow);

            long originalTime = stamp.getTimestamp().getTime();
            
            // 1. Tentative de corruption via l'objet initial externe
            mutableDate.setTime(0L); 
            assertEquals(originalTime, stamp.getTimestamp().getTime(), 
                "Modifier l'objet Date passé au constructeur ne doit pas altérer le composant interne.");

            // 2. Tentative de corruption via le Getter
            Date getterDate = stamp.getTimestamp();
            getterDate.setTime(0L);
            assertEquals(originalTime, stamp.getTimestamp().getTime(), 
                "Modifier l'objet Date récupéré par le getter ne doit pas altérer le composant interne.");
        }
    }

    @Nested
    @DisplayName("Tests des validations aux limites (Cas d'erreurs)")
    class BoundaryAndExceptionTests {

        @Test
        @DisplayName("Doit lever une exception si les composants requis sont nuls")
        void shouldThrowExceptionWhenParametersAreNull() {
            assertThrows(IllegalArgumentException.class, () -> new ConcreteStamp(null, librarian, fakeBorrow));
            assertThrows(IllegalArgumentException.class, () -> new ConcreteStamp(new Date(), null, fakeBorrow));
            assertThrows(IllegalArgumentException.class, () -> new ConcreteStamp(new Date(), librarian, null));
        }
    }

    // -------------------------------------------------------------------------
    // Nouveaux tests spécifiques pour ExtensionStamp
    // -------------------------------------------------------------------------
    @Nested
    @DisplayName("Tests spécifiques - ExtensionStamp")
    class ExtensionStampTests {

        @Test
        @DisplayName("ExtensionStamp doit correctement assigner sa date de prolongation et ses attributs parents")
        void shouldInitializeExtensionStampCorrectly() {
            Date futureDueDate = new Date(System.currentTimeMillis() + 86400000L); // Demain
            ExtensionStamp extStamp = new ExtensionStamp(futureDueDate, librarian, fakeBorrow);

            assertEquals(librarian, extStamp.getValidator(), "Le bibliothécaire doit être lié via super()");
            assertEquals(fakeBorrow, extStamp.getReference(), "Le prêt doit être lié via super()");
            assertNotNull(extStamp.getTimestamp(), "Le timestamp d'exécution automatique doit être généré");
            assertEquals(futureDueDate, extStamp.getExtensionDate(), "La date de prolongation doit correspondre");
        }

        @Test
        @DisplayName("La date de prolongation doit être protégée par copie défensive")
        void shouldProtectExtensionDateAgainstMutation() {
            Date mutableDate = new Date();
            ExtensionStamp extStamp = new ExtensionStamp(mutableDate, librarian, fakeBorrow);

            long originalTime = extStamp.getExtensionDate().getTime();

            // 1. Mutation via la référence passée au constructeur
            mutableDate.setTime(0L);
            assertEquals(originalTime, extStamp.getExtensionDate().getTime(),
                "Modifier la date après construction ne doit pas corrompre ExtensionStamp.");

            // 2. Mutation via la valeur retournée par le getter
            Date getterDate = extStamp.getExtensionDate();
            getterDate.setTime(0L);
            assertEquals(originalTime, extStamp.getExtensionDate().getTime(),
                "Le getter doit renvoyer une copie défensive de la date de prolongation.");
        }

        @Test
        @DisplayName("ExtensionStamp doit lever une exception si un des paramètres requis est null")
        void shouldThrowExceptionWhenParametersAreNull() {
            Date validDate = new Date();
            // Erreur sur l'attribut propre
            assertThrows(IllegalArgumentException.class, () -> new ExtensionStamp(null, librarian, fakeBorrow));
            // Relais des erreurs vers la classe mère Stamp
            assertThrows(IllegalArgumentException.class, () -> new ExtensionStamp(validDate, null, fakeBorrow));
            assertThrows(IllegalArgumentException.class, () -> new ExtensionStamp(validDate, librarian, null));
        }
    }

    // -------------------------------------------------------------------------
    // Nouveaux tests spécifiques pour ReturnStamp
    // -------------------------------------------------------------------------
    @Nested
    @DisplayName("Tests spécifiques - ReturnStamp")
    class ReturnStampTests {

        @Test
        @DisplayName("ReturnStamp doit correctement assigner l'état de retour et ses attributs parents")
        void shouldInitializeReturnStampCorrectly() {
            String state = "Abîmé";
            ReturnStamp returnStamp = new ReturnStamp(state, fakeBorrow, librarian);

            assertEquals(librarian, returnStamp.getValidator(), "Le bibliothécaire doit être lié via super()");
            assertEquals(fakeBorrow, returnStamp.getReference(), "Le prêt doit être lié via super()");
            assertNotNull(returnStamp.getTimestamp(), "Le timestamp de retour automatique doit être généré");
            assertEquals(state, returnStamp.getReturnState(), "L'état de retour doit correspondre");
        }

        @Test
        @DisplayName("ReturnStamp doit lever une exception si un des paramètres requis est null")
        void shouldThrowExceptionWhenParametersAreNull() {
            String validState = "Bon";
            // Erreur sur l'attribut propre
            assertThrows(IllegalArgumentException.class, () -> new ReturnStamp(null, fakeBorrow, librarian));
            // Relais des erreurs vers la classe mère Stamp (Attention à l'ordre des paramètres de ton constructeur !)
            assertThrows(IllegalArgumentException.class, () -> new ReturnStamp(validState, null, librarian));
            assertThrows(IllegalArgumentException.class, () -> new ReturnStamp(validState, fakeBorrow, null));
        }
    }
}