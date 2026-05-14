package com.bibliotheque.model;

import com.bibliotheque.errors.SearchClassNotInherits;
import com.bibliotheque.errors.SearchStringTooSmall;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

@DisplayName("Tests du Moteur de Recherche (SearchingWork)")
public class SearchingWorkTest {

    private static Set<Work> catalogue;
    private static Book book1;
    private static Dvd dvd1;
    private static Date specificDate;

    @BeforeAll
    static void initCatalogue() {
        catalogue = new HashSet<>();
        specificDate = new Date();

        // Création d'un livre
        book1 = new Book("978123", "Le Hobbit", "FANTASY", "Livre de Poche", specificDate, null);
        
        // Création d'un DVD
        dvd1 = new Dvd("Inception", "SF", "Warner", new Date(0), null, "Zone 2");

        catalogue.add(book1);
        catalogue.add(dvd1);
    }

    @Nested
    @DisplayName("Recherche par type spécifique (ISBN / Region)")
    class TypeSpecificSearch {

        @Test
        @DisplayName("Trouver un livre par son ISBN")
        void testSearchByIsbn() throws SearchStringTooSmall {
            Optional<Book> result = SearchingWork.searchByIsbn(catalogue, "978123");
            assertTrue(result.isPresent());
            assertEquals(book1, result.get());
        }

        @Test
        @DisplayName("Trouver des DVDs par zone")
        void testSearchByRegion() throws SearchStringTooSmall {
            List<Dvd> results = SearchingWork.searchByRegion(catalogue, "Zone 2");
            assertEquals(1, results.size());
            assertEquals(dvd1, results.get(0));
        }

        @Test
        @DisplayName("Erreur si la clé de recherche est trop courte (<3)")
        void testShortQueryError() {
            assertThrows(SearchStringTooSmall.class, () -> {
                SearchingWork.searchByIsbn(catalogue, "97");
            });
        }
    }

    @Nested
    @DisplayName("Recherche Générique (Functional Search)")
    class GenericSearch {

        @Test
        @DisplayName("Recherche par titre (insensible à la casse)")
        void testTitleSearch() throws SearchStringTooSmall {
            // On cherche "hobbit" pour trouver "Le Hobbit"
            List<Work> results = SearchingWork.search(catalogue, "HOBBIT", Work::getTitle);
            assertEquals(1, results.size());
            assertEquals(book1, results.get(0));
        }

        @Test
        @DisplayName("Recherche par éditeur avec espaces superflus")
        void testEditorSearchWithTrim() throws SearchStringTooSmall {
            List<Work> results = SearchingWork.search(catalogue, "  warner  ", Work::getEditor);
            assertEquals(1, results.size());
            assertEquals(dvd1, results.get(0));
        }
    }

    @Nested
    @DisplayName("Filtres avancés (Date et Classe)")
    class AdvancedFilters {

        @Test
        @DisplayName("Recherche par date exacte")
        void testDateSearch() {
            List<Work> results = SearchingWork.searchByPubDate(catalogue, specificDate);
            assertTrue(results.contains(book1));
            assertFalse(results.contains(dvd1));
        }

        @Test
        @DisplayName("Extraction par type de classe")
        void testSearchByType() throws SearchClassNotInherits {
            List<Book> books = SearchingWork.searchByType(catalogue, Book.class);
            List<Dvd> dvds = SearchingWork.searchByType(catalogue, Dvd.class);

            assertAll("Vérification des types",
                () -> assertEquals(1, books.size(), "Il devrait y avoir 1 livre"),
                () -> assertEquals(1, dvds.size(), "Il devrait y avoir 1 DVD"),
                () -> assertTrue(books.get(0) instanceof Book)
            );
        }

        @Test
        @DisplayName("Erreur si la classe cible est null")
        void testNullType() {
            assertThrows(SearchClassNotInherits.class, () -> {
                SearchingWork.searchByType(catalogue, null);
            });
        }
    }
}