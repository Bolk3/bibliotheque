package com.bibliotheque.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import java.util.Date;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests de la hiérarchie des Œuvres (Work, Book, Dvd)")
class WorksTest {

    private Bibliotheque bibliotheque;
    private Date defaultDate;
    private Book sampleBook; // Utilisé pour tester les comportements génériques de Work

    @BeforeEach
    void setUp() {
        bibliotheque = new Bibliotheque("Médiathèque", "12 Rue du Code");
        defaultDate = new Date(100000000000L);
        // On crée un livre générique pour tester la logique de base de la classe Work
        sampleBook = new Book("9782070413119", "Le Hobbit", "Cote-B1", "Gallimard", defaultDate, bibliotheque);
    }

    // =========================================================================
    // 1. TESTS DES METHODES DE WORK (LOGIQUE COMMUNE)
    // =========================================================================
    @Nested
    @DisplayName("Cas Génériques (Classe Work)")
    class GenericWorkTests {

        @Test
        @DisplayName("Doit valider les requêtes de comparaison (isTitle, isCategory, isEditor)")
        void shouldValidateQueryMethods() {
            assertTrue(sampleBook.isTitle("Le Hobbit"));
            assertTrue(sampleBook.isCategory("Cote-B1"));
            assertTrue(sampleBook.isEditor("Gallimard"));
        }

        @Test
        @DisplayName("Doit modifier correctement les champs communs via les setters")
        void shouldModifyFieldsWithSetters() {
            sampleBook.setTitle("Nouveau Titre");
            assertEquals("Nouveau Titre", sampleBook.getTitle());
        }

        @Test
        @DisplayName("Doit gérer les auteurs sans doublons ni valeurs nulles")
        void shouldManageAuthorsSafely() {
            Author author = new Author("Frank", "Herbert");
            sampleBook.addAuthor(author);
            sampleBook.addAuthor(author); // Doublon ignoré

            assertEquals(1, sampleBook.getAuthors().size());
            assertTrue(sampleBook.isAuthor(author));
        }

        @Test
        @DisplayName("Doit filtrer les exemplaires selon leur état")
        void shouldFilterCopiesByState() {
            // Correction ici : Signature exacte -> Copy(State, Work)
            Copy goodCopy = new Copy(State.BON, sampleBook);
            Copy damagedCopy = new Copy(State.ABIME, sampleBook);
            
            sampleBook.addCopy(goodCopy);
            sampleBook.addCopy(damagedCopy);

            List<Copy> goodCopies = sampleBook.getCopiesByState(State.BON);
            assertEquals(1, goodCopies.size());
            assertTrue(goodCopies.contains(goodCopy));
        }
    }

    // =========================================================================
    // 2. SOUS-CLASSE POUR LES METHODES DE BOOK
    // =========================================================================
    @Nested
    @DisplayName("Spécificités de la classe Book")
    class BookTests {

        private Book concreteBook;

        @BeforeEach
        void setUpBook() {
            concreteBook = new Book("1234567890", "Clean Code", "Tech-01", "Pearson", defaultDate, bibliotheque);
        }

        @Test
        @DisplayName("Doit valider l'ISBN initial et répondre correctement à isIsbn")
        void shouldValidateIsbn() {
            assertEquals("1234567890", concreteBook.getIsbn());
            assertTrue(concreteBook.isIsbn("1234567890"));
            assertFalse(concreteBook.isIsbn("9999999999"));
        }

        @Test
        @DisplayName("Doit mettre à jour l'ISBN via le setter")
        void shouldModifyIsbn() {
            concreteBook.setIsbn("0000000000");
            assertEquals("0000000000", concreteBook.getIsbn());
        }
    }

    // =========================================================================
    // 3. SOUS-CLASSE POUR LES METHODES DE DVD
    // =========================================================================
    @Nested
    @DisplayName("Spécificités de la classe Dvd")
    class DvdTests {

        private Dvd concreteDvd;

        @BeforeEach
        void setUpDvd() {
            concreteDvd = new Dvd("Gladiator", "Film-01", "Universal", defaultDate, bibliotheque, "Zone 2");
        }

        @Test
        @DisplayName("Doit valider la région initiale et répondre correctement à isRegion")
        void shouldValidateRegion() {
            assertEquals("Zone 2", concreteDvd.getRegion());
            assertTrue(concreteDvd.isRegion("Zone 2"));
            assertFalse(concreteDvd.isRegion("Zone 1"));
        }

        @Test
        @DisplayName("Doit mettre à jour la région via le setter")
        void shouldModifyRegion() {
            concreteDvd.setRegion("Zone 1");
            assertEquals("Zone 1", concreteDvd.getRegion());
        }
    }
}