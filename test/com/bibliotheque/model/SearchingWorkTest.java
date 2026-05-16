package com.bibliotheque.model;

import com.bibliotheque.errors.SearchClassNotInherits;
import com.bibliotheque.errors.SearchStringTooSmall;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests complets de SearchingWork")
class SearchingWorkTest {

    private Bibliotheque bibliotheque;
    private Set<Work> catalogue;
    private Book realBook;
    private Dvd realDvd;
    private Date defaultDate;

    @BeforeEach
    void setUp() {
        // Utilisation directe de la vraie classe avec des paramètres basiques
        bibliotheque = new Bibliotheque("Bibliothèque Centrale", "10 Rue des Livres");
        catalogue = new HashSet<>();
        defaultDate = new Date(1500000000000L); // Date fixe et stable pour les tests

        // Instanciation des vrais objets métiers
        realBook = new Book(
            "9782070413119", 
            "Le Hobbit", 
            "Cote-B1", 
            "Gallimard", 
            defaultDate, 
            bibliotheque
        );

        realDvd = new Dvd(
            "Gladiator", 
            "Cote-D1", 
            "Universal", 
            defaultDate, 
            bibliotheque, 
            "Zone 2"
        );

        catalogue.add(realBook);
        catalogue.add(realDvd);
    }

    @Nested
    @DisplayName("Recherche par Région (DVD)")
    class SearchByRegionTests {

        @Test
        @DisplayName("Doit retourner le DVD si le code de région concorde exactement")
        void shouldReturnDvdWhenRegionMatches() throws SearchStringTooSmall {
            List<Dvd> result = SearchingWork.searchByRegion(catalogue, "Zone 2");

            assertEquals(1, result.size());
            assertEquals(realDvd, result.get(0));
        }

        @Test
        @DisplayName("Doit retourner une liste vide si aucune région ne correspond")
        void shouldReturnEmptyListWhenNoRegionMatches() throws SearchStringTooSmall {
            List<Dvd> result = SearchingWork.searchByRegion(catalogue, "Zone 1");

            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("Doit lever SearchStringTooSmall si la région recherchée est trop courte")
        void shouldThrowExceptionWhenRegionTooShort() {
            SearchStringTooSmall exception = assertThrows(
                SearchStringTooSmall.class, 
                () -> SearchingWork.searchByRegion(catalogue, "Z1")
            );
            assertEquals("Query is too small.", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Recherche par ISBN (Livre)")
    class SearchByIsbnTests {

        @Test
        @DisplayName("Doit encapsuler le livre trouvé dans un Optional si l'ISBN correspond")
        void shouldReturnOptionalWithBookWhenIsbnMatches() throws SearchStringTooSmall {
            Optional<Book> result = SearchingWork.searchByIsbn(catalogue, "9782070413119");

            assertTrue(result.isPresent());
            assertEquals(realBook, result.get());
        }

        @Test
        @DisplayName("Doit retourner un Optional vide si aucun livre n'a cet ISBN")
        void shouldReturnEmptyOptionalWhenNoIsbnMatches() throws SearchStringTooSmall {
            Optional<Book> result = SearchingWork.searchByIsbn(catalogue, "0000000000000");

            assertFalse(result.isPresent());
        }

        @Test
        @DisplayName("Doit lever SearchStringTooSmall si l'ISBN recherché fait moins de 3 caractères")
        void shouldThrowExceptionWhenIsbnTooShort() {
            SearchStringTooSmall exception = assertThrows(
                SearchStringTooSmall.class, 
                () -> SearchingWork.searchByIsbn(catalogue, "12")
            );
            assertEquals("Query is too small.", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Recherche Fonctionnelle Générique (search)")
    class GenericSearchTests {

        @Test
        @DisplayName("Doit filtrer de manière insensible à la casse et par sous-chaîne sur le titre")
        void shouldSearchCaseInsensitiveAndSubstringOnTitle() throws SearchStringTooSmall {
            List<Work> result = SearchingWork.search(catalogue, "  HOBB  ", Work::getTitle);

            assertEquals(1, result.size());
            assertTrue(result.contains(realBook));
        }

        @Test
        @DisplayName("Doit filtrer par sous-chaîne sur l'éditeur")
        void shouldSearchOnEditor() throws SearchStringTooSmall {
            List<Work> result = SearchingWork.search(catalogue, "univer", Work::getEditor);

            assertEquals(1, result.size());
            assertTrue(result.contains(realDvd));
        }

        @Test
        @DisplayName("Doit ignorer les oeuvres dont l'attribut extrait vaut null")
        void shouldIgnoreElementsWithNullAttribute() throws SearchStringTooSmall {
            // Modification temporaire de l'état réel pour tester la tolérance aux pannes de search()
            realBook.setTitle(null);

            List<Work> result = SearchingWork.search(catalogue, "Gladiator", Work::getTitle);

            assertEquals(1, result.size());
            assertTrue(result.contains(realDvd));
        }

        @Test
        @DisplayName("Doit lever SearchStringTooSmall si la requête est inférieure à 3 caractères après trim")
        void shouldThrowExceptionWhenQueryTooShortAfterTrim() {
            SearchStringTooSmall exception = assertThrows(
                SearchStringTooSmall.class, 
                () -> SearchingWork.search(catalogue, "  ho  ", Work::getTitle)
            );
            assertEquals("Query is too small.", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Recherche par Date de Publication")
    class SearchByPubDateTests {

        @Test
        @DisplayName("Doit retourner toutes les oeuvres publiées à la date exacte")
        void shouldReturnWorksWithExactPublicationDate() {
            List<Work> result = SearchingWork.searchByPubDate(catalogue, defaultDate);

            assertEquals(2, result.size());
            assertTrue(result.contains(realBook));
            assertTrue(result.contains(realDvd));
        }

        @Test
        @DisplayName("Doit retourner une liste vide si aucune oeuvre ne correspond à la date")
        void shouldReturnEmptyListWhenNoDateMatches() {
            Date wrongDate = new Date(1600000000000L);
            List<Work> result = SearchingWork.searchByPubDate(catalogue, wrongDate);

            assertTrue(result.isEmpty());
        }
    }

    @Nested
    @DisplayName("Filtrage par Type à l'Exécution (searchByType)")
    class SearchByTypeTests {

        @Test
        @DisplayName("Doit filtrer et convertir la liste uniquement pour les instances de Book")
        void shouldFilterAndCastBooksOnly() throws SearchClassNotInherits {
            List<Book> books = SearchingWork.searchByType(catalogue, Book.class);

            assertEquals(1, books.size());
            assertEquals(realBook, books.get(0));
        }

        @Test
        @DisplayName("Doit filtrer et convertir la liste uniquement pour les instances de Dvd")
        void shouldFilterAndCastDvdsOnly() throws SearchClassNotInherits {
            List<Dvd> dvds = SearchingWork.searchByType(catalogue, Dvd.class);

            assertEquals(1, dvds.size());
            assertEquals(realDvd, dvds.get(0));
        }

        @Test
        @DisplayName("Doit lever SearchClassNotInherits si le type de classe cible est null")
        void shouldThrowExceptionWhenTargetTypeIsNull() {
            SearchClassNotInherits exception = assertThrows(
                SearchClassNotInherits.class, 
                () -> SearchingWork.searchByType(catalogue, null)
            );
            assertEquals("Target type cannot be null.", exception.getMessage());
        }
    }
}