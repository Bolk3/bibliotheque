package com.bibliotheque.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import java.util.Date;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Gestion d'un emprunt")
class BorrowTest {

    private Bibliotheque library;
    private Librarian librarianWithAllPerms;
    private Librarian librarianWithoutPerms;
    private Member member;
    private Book book;
    private Copy copy;
    private Date initialExpectedDate;

    @BeforeEach
    void setUp() {
        library = new Bibliotheque("Centrale", "Paris");
        
        // ATTENTION : Pour ces tests, on part du principe que Settings.hasAccess 
        // renvoie true ou false selon l'état du Librarian ou de ses paramètres.
        // Ici, Alice a les droits complets (ex: grade élevé ou config par défaut si ta classe est épurée).
        librarianWithAllPerms = new Librarian("Alice", "Smith", "alice@biblio.fr", library, "Directeur", 5);
        librarianWithoutPerms = new Librarian("Bob", "Jones", "bob@biblio.fr", library, "Stagiaire", 1);
        
        member = new Member("Jean", "Dupont", "jean@mail.com", library);
        book = new Book("12345", "Les Misérables", "C-1", "Éditeur Test", new Date(), library);
        copy = new Copy(State.BON, book);
        
        // Date d'échéance initiale définie à +14 jours
        initialExpectedDate = new Date(System.currentTimeMillis() + (14L * 24 * 60 * 60 * 1000));
    }

    @Nested
    @DisplayName("Tests d'initialisation et d'état initial")
    class InitializationTests {

        @Test
        @DisplayName("Un emprunt doit être correctement configuré à sa création")
        void shouldInitializeBorrowCorrectly() {
            Borrow borrow = new Borrow(initialExpectedDate, librarianWithAllPerms, member, copy);

            assertEquals(librarianWithAllPerms, borrow.getValidator(), "Le validateur initial doit correspondre");
            assertEquals(member, borrow.getBorrower(), "Le membre emprunteur doit correspondre");
            assertEquals(copy, borrow.getCopy(), "L'exemplaire associé doit correspondre");
            assertEquals(initialExpectedDate, borrow.getExpectedDate(), "La date d'échéance initiale doit correspondre");
            assertEquals(State.BON.toString(), borrow.getInitialState(), "L'état initial textuel doit être capturé");
            
            assertNull(borrow.getReturnStamp(), "Aucun tampon de retour ne doit exister au départ");
            assertTrue(borrow.getExtensions().isEmpty(), "La liste des prolongations doit être vide au départ");
            assertFalse(borrow.isReturned(), "L'emprunt ne doit pas être marqué comme rendu");
            assertFalse(borrow.isDamaged(), "L'exemplaire ne peut pas être marqué dégradé avant le retour");
        }

        @Test
        @DisplayName("Les getters de dates doivent appliquer la copie défensive")
        void shouldApplyDefensiveCopyOnGetters() {
            Borrow borrow = new Borrow(initialExpectedDate, librarianWithAllPerms, member, copy);

            long originalStartTime = borrow.getStartDate().getTime();
            long originalExpectedTime = borrow.getExpectedDate().getTime();

            // Modification des objets récupérés
            borrow.getStartDate().setTime(0L);
            borrow.getExpectedDate().setTime(0L);

            assertEquals(originalStartTime, borrow.getStartDate().getTime(), "getStartDate() doit être protégé");
            assertEquals(originalExpectedTime, borrow.getExpectedDate().getTime(), "getExpectedDate() doit être protégé");
        }
    }

    @Nested
    @DisplayName("Tests des prolongations (extendsDate)")
    class ExtensionTests {

        @Test
        @DisplayName("Doit enregistrer une prolongation valide par un bibliothécaire autorisé")
        void shouldExtendBorrowSuccessfully() {
            Borrow borrow = new Borrow(initialExpectedDate, librarianWithAllPerms, member, copy);
            
            // Nouvelle date valide (au-delà des 14 jours initiaux)
            Date newExpectedDate = new Date(initialExpectedDate.getTime() + (7L * 24 * 60 * 60 * 1000));

            borrow.extendsDate(newExpectedDate, librarianWithAllPerms);

            assertEquals(newExpectedDate, borrow.getExpectedDate(), "La date attendue doit être mise à jour");
            List<ExtensionStamp> extensions = borrow.getExtensions();
            assertEquals(1, extensions.size(), "L'historique doit contenir 1 extension");
            assertEquals(newExpectedDate, extensions.get(0).getExtensionDate(), "Le stamp doit stocker la bonne date");
        }

        @Test
        @DisplayName("Doit rejeter une prolongation si la nouvelle date n'est pas strictement après l'échéance actuelle")
        void shouldThrowExceptionWhenNewDateIsBeforeOrEqual() {
            Borrow borrow = new Borrow(initialExpectedDate, librarianWithAllPerms, member, copy);

            // Tentative avec exactement la même date
            assertThrows(IllegalArgumentException.class, () -> borrow.extendsDate(initialExpectedDate, librarianWithAllPerms));
            
            // Tentative avec une date passée
            Date pastDate = new Date(initialExpectedDate.getTime() - 1000L);
            assertThrows(IllegalArgumentException.class, () -> borrow.extendsDate(pastDate, librarianWithAllPerms));
        }

        @Test
        @DisplayName("Doit lever une exception si la prolongation concerne un livre déjà retourné")
        void shouldThrowExceptionWhenBorrowIsAlreadyReturned() {
            Borrow borrow = new Borrow(initialExpectedDate, librarianWithAllPerms, member, copy);
            borrow.returnBook(State.BON.toString(), librarianWithAllPerms);

            Date futureDate = new Date(initialExpectedDate.getTime() + 86400000L);
            assertThrows(IllegalStateException.class, () -> borrow.extendsDate(futureDate, librarianWithAllPerms));
        }
    }

    @Nested
    @DisplayName("Tests du retour de livre (returnBook)")
    class ReturnTests {

        @Test
        @DisplayName("Doit valider le retour et générer le ReturnStamp")
        void shouldProcessReturnCorrectly() {
            Borrow borrow = new Borrow(initialExpectedDate, librarianWithAllPerms, member, copy);
            
            borrow.returnBook(State.BON.toString(), librarianWithAllPerms);

            assertTrue(borrow.isReturned(), "L'emprunt doit être marqué comme retourné");
            assertNotNull(borrow.getReturnStamp(), "Un ReturnStamp doit être généré");
            assertEquals(State.BON.toString(), borrow.getReturnStamp().getReturnState());
            assertFalse(borrow.isDamaged(), "Le livre n'est pas abîmé s'il revient dans le même état");
        }

        @Test
        @DisplayName("Doit mettre à jour automatiquement l'état de la Copy en cas de dégradation")
        void shouldUpdateCopyStateIfReturnedDamaged() {
            // Initialisé en état BON
            Borrow borrow = new Borrow(initialExpectedDate, librarianWithAllPerms, member, copy);
            assertEquals(State.BON, copy.getState());

            // Retourné en état ABIME
            borrow.returnBook(State.ABIME.toString(), librarianWithAllPerms);

            assertTrue(borrow.isReturned());
            assertTrue(borrow.isDamaged(), "isDamaged() doit passer à true car ABIME != BON");
            assertEquals(State.ABIME, copy.getState(), "L'état de la Copy physique sous-jacente doit avoir été muté");
        }

        @Test
        @DisplayName("Doit interdire un second retour sur un prêt déjà clos")
        void shouldThrowExceptionOnDoubleReturn() {
            Borrow borrow = new Borrow(initialExpectedDate, librarianWithAllPerms, member, copy);
            borrow.returnBook(State.BON.toString(), librarianWithAllPerms);

            assertThrows(IllegalStateException.class, () -> borrow.returnBook(State.BON.toString(), librarianWithAllPerms));
        }
    }

    @Nested
    @DisplayName("Tests des calculs de métriques temporelles")
    class LogicQueriesTests {

        @Test
        @DisplayName("getElapsedTime() doit retourner un écart de temps cohérent")
        void shouldReturnValidElapsedTime() throws InterruptedException {
            Borrow borrow = new Borrow(initialExpectedDate, librarianWithAllPerms, member, copy);
            
            // Petite pause artificielle pour laisser s'écouler du temps
            Thread.sleep(10);
            
            long elapsedActive = borrow.getElapsedTime();
            assertTrue(elapsedActive > 0, "Le temps écoulé actif doit être supérieur à 0ms");

            // Clôture du prêt
            borrow.returnBook(State.BON.toString(), librarianWithAllPerms);
            long elapsedFinal = borrow.getElapsedTime();
            
            Thread.sleep(10);
            assertEquals(elapsedFinal, borrow.getElapsedTime(), 
                "Une fois le livre rendu, getElapsedTime() doit rester figé (basé sur le timestamp du retour)");
        }
    }
}