package com.bibliotheque.model;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Date;
import java.util.List;

@DisplayName("Tests des Œuvres (Work, Book, Dvd)")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class WorkTest {

    private static Book testBook;
    private static Dvd testDvd;

    @BeforeAll
    static void setup() {
        // Initialisation d'un livre (isbn, title, category, editor, pubDate, handler)
        testBook = new Book(
            "978-2070413164", 
            "L'Étranger", 
            "ROM", 
            "Gallimard", 
            new Date(), 
            null
        );

        // Initialisation d'un DVD (title, category, editor, pubDate, handler, region)
        testDvd = new Dvd(
            "Inception", 
            "FILM-SF", 
            "Warner Bros", 
            new Date(), 
            null, 
            "2"
        );
    }

    @Nested
    @Order(1)
    @DisplayName("Tests communs (Logique Work)")
    class CommonLogicTest {

        @Test
        @DisplayName("Gestion des exemplaires (Copies)")
        void testCopyManagement() {
            Copy c1 = new Copy(State.NEUF, testBook);
            testBook.addCopy(c1);
            
            assertEquals(1, testBook.getCopies().size());
            assertTrue(testBook.getCopies().contains(c1));
            
            // Vérification du filtre par état
            List<Copy> freshCopies = testBook.getCopiesByState(State.NEUF);
            assertEquals(1, freshCopies.size());
        }

        @Test
        @DisplayName("Protection des données (Encapsulation)")
        void testDefensiveCopy() {
            Date originalDate = testBook.getPublicationDate();
            long time = originalDate.getTime();
            
            originalDate.setTime(0); // Tentative de modification malveillante
            
            assertEquals(time, testBook.getPublicationDate().getTime(), 
                "La date de publication doit être protégée par une copie défensive.");
        }
    }

    @Nested
    @Order(2)
    @DisplayName("Tests spécifiques au Livre (Book)")
    class BookSpecificTest {

        @Test
        @DisplayName("Validation de l'ISBN")
        void testIsbn() {
            assertEquals("978-2070413164", testBook.getIsbn());
            assertTrue(testBook.isIsbn("978-2070413164"));
            assertFalse(testBook.isIsbn("000-0000000000"));
        }
    }

    @Nested
    @Order(3)
    @DisplayName("Tests spécifiques au DVD (Dvd)")
    class DvdSpecificTest {

        @Test
        @DisplayName("Validation de la zone géographique (Region)")
        void testRegion() {
            assertEquals("2", testDvd.getRegion());
            assertTrue(testDvd.isRegion("2"));
            assertFalse(testDvd.isRegion("1"));
        }
    }

    @Nested
    @Order(4)
    @DisplayName("Tests des relations (Auteurs)")
    class RelationTest {

        @Test
        @DisplayName("Ajout et vérification d'un auteur")
        void testAuthorRelation() {
            // On suppose l'existence d'une classe Author simplifiée
            // Si la classe Author nécessite des paramètres, adaptez ici
            Author camus = new Author("Albert", "Camus"); 
            testBook.addAuthor(camus);
            
            assertTrue(testBook.isAuthor(camus));
            assertEquals(camus, testBook.getAuthor());
        }
    }
}