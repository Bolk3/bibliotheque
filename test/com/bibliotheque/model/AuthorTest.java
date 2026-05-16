package com.bibliotheque.model;

import com.bibliotheque.errors.RegexFormatError;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import java.util.Date;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests unitaires - Classe Author (Auteur)")
class AuthorTest {

    private Bibliotheque library;
    private Book dummyWork; // Concrétise la classe/interface Work pour l'associer à l'auteur

    @BeforeEach
    void setUp() {
        library = new Bibliotheque("Centrale", "Paris");
        // Initialisation d'une œuvre de test
        dummyWork = new Book("12345", "Les Misérables", "C-1", "Éditeur Test", new Date(), library);
    }

    @Nested
    @DisplayName("Tests de construction et de validation initiale")
    class ConstructorTests {

        @Test
        @DisplayName("Un auteur doit être correctement initialisé avec des données valides")
        void shouldInitializeAuthorCorrectly() {
            Author author = new Author("Victor", "Hugo");

            assertEquals("Victor", author.getFirstName(), "Le prénom doit être correctement affecté");
            assertEquals("Hugo", author.getLastName(), "Le nom doit être correctement affecté");
            assertTrue(author.getWorks().isEmpty(), "La liste des œuvres de l'auteur doit être vide au départ");
        }

        @Test
        @DisplayName("Le constructeur doit encapsuler la RegexFormatError dans une IllegalArgumentException en cas de format invalide")
        void shouldThrowIllegalArgumentExceptionOnInvalidFormat() {
            // On passe une chaîne vide ou invalide (en supposant que ValidationUtils la rejette)
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                new Author("", "Hugo");
            }, "Le constructeur doit rejeter un prénom invalide");

            // On vérifie que la cause d'origine est bien une RegexFormatError
            assertTrue(exception.getCause() instanceof RegexFormatError, 
                "La cause de l'exception doit être une RegexFormatError");
        }
    }

    @Nested
    @DisplayName("Tests des mutateurs (Setters) et des exceptions")
    class MutatorTests {

        @Test
        @DisplayName("setFirstName doit modifier le prénom si le format est valide")
        void shouldUpdateFirstNameWhenValid() throws RegexFormatError {
            Author author = new Author("Victor", "Hugo");
            author.setFirstName("Alexandre");
            
            assertEquals("Alexandre", author.getFirstName());
        }

        @Test
        @DisplayName("setFirstName doit lever une RegexFormatError si le format est invalide")
        void shouldThrowRegexFormatErrorOnInvalidFirstName() {
            Author author = new Author("Victor", "Hugo");

            assertThrows(RegexFormatError.class, () -> {
                author.setFirstName(""); // Format invalide testé directement sur le setter
            });
        }

        @Test
        @DisplayName("setLastName doit modifier le nom si le format est valide")
        void shouldUpdateLastNameWhenValid() throws RegexFormatError {
            Author author = new Author("Victor", "Hugo");
            author.setLastName("Dumas");
            
            assertEquals("Dumas", author.getLastName());
        }

        @Test
        @DisplayName("setLastName doit lever une RegexFormatError si le format est invalide")
        void shouldThrowRegexFormatErrorOnInvalidLastName() {
            Author author = new Author("Victor", "Hugo");

            assertThrows(RegexFormatError.class, () -> {
                author.setLastName(""); // Format invalide testé directement sur le setter
            });
        }
    }

    @Nested
    @DisplayName("Tests de requêtes et de normalisation (isFirstName / isLastName)")
    class QueryTests {

        @Test
        @DisplayName("isFirstName doit renvoyer true si les prénoms correspondent après normalisation")
        void shouldReturnTrueWhenFirstNamesMatchWithNormalization() {
            // On teste si la normalisation gère les écarts de casse (ex: "victor" vs "Victor")
            Author author = new Author("Victor", "Hugo");

            assertTrue(author.isFirstName("victor"), "La comparaison doit être insensible à la casse / normalisée");
            assertTrue(author.isFirstName("Victor"));
            assertFalse(author.isFirstName("Émile"), "Doit renvoyer false si le prénom ne correspond pas");
        }

        @Test
        @DisplayName("isLastName doit renvoyer true si les noms correspondent après normalisation")
        void shouldReturnTrueWhenLastNamesMatchWithNormalization() {
            Author author = new Author("Victor", "Hugo");

            assertTrue(author.isLastName("hugo"), "La comparaison doit être insensible à la casse / normalisée");
            assertTrue(author.isLastName("HUGO"));
            assertFalse(author.isLastName("Zola"), "Doit renvoyer false si le nom ne correspond pas");
        }
    }

    @Nested
    @DisplayName("Tests de la gestion des œuvres (Works)")
    class WorksTests {

        @Test
        @DisplayName("addWork doit ajouter une œuvre à la collection de l'auteur")
        void shouldAddWorkSuccessfully() {
            Author author = new Author("Victor", "Hugo");
            
            author.addWork(dummyWork);

            Set<Work> works = author.getWorks();
            assertEquals(1, works.size(), "La collection doit contenir exactement 1 œuvre");
            assertTrue(works.contains(dummyWork), "L'œuvre ajoutée doit être présente dans le Set");
        }

        @Test
        @DisplayName("getWorks() doit renvoyer une vue non modifiable du Set")
        void shouldReturnUnmodifiableSet() {
            Author author = new Author("Victor", "Hugo");
            author.addWork(dummyWork);

            Set<Work> worksSet = author.getWorks();

            // Tenter de modifier le Set directement doit lever une exception d'incompatibilité
            assertThrows(UnsupportedOperationException.class, () -> {
                worksSet.clear();
            }, "L'accès direct en écriture sur le Set retourné doit être verrouillé");
        }
    }
}