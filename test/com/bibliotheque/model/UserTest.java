package com.bibliotheque.model;

import com.bibliotheque.errors.RegexFormatError;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Classe Abstraite User")
class UserTest {

    private Bibliotheque library;

    @BeforeEach
    void setUp() {
        library = new Bibliotheque("Centrale", "Paris");
    }

    @Nested
    @DisplayName("Tests des validations et requêtes d'identité")
    class IdentityValidationTests {

        @Test
        @DisplayName("Les requêtes d'identité doivent utiliser la normalisation (insensible à la casse)")
        void shouldNormalizeIdentityQueries() {
            // Utilisation d'une sous-classe concrète pour tester le comportement hérité de User
            User user = new Member("Albert", "Camus", "albert@camus.fr", library);

            assertTrue(user.isFirstName("albert"));
            assertTrue(user.isFirstName("ALBERT"));
            assertTrue(user.isLastName("camus"));
            assertTrue(user.isEmail("ALBERT@camus.fr"));
            
            assertFalse(user.isFirstName("Jean"));
        }

        @Test
        @DisplayName("Les requêtes avec des paramètres invalides ou null doivent renvoyer false gentiment")
        void shouldReturnFalseOnInvalidOrNullQueryParameters() {
            User user = new Member("Albert", "Camus", "albert@camus.fr", library);

            assertFalse(user.isFirstName(null));
            assertFalse(user.isLastName(""));
            assertFalse(user.isEmail("adresse-invalide"));
        }

        @Test
        @DisplayName("Le constructeur doit interdire un gestionnaire de bibliothèque null")
        void shouldThrowExceptionWhenLibraryHandlerIsNull() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                new Member("Albert", "Camus", "albert@camus.fr", null);
            });
            assertEquals("The managing library handler cannot be null.", exception.getMessage());
        }
    }
}