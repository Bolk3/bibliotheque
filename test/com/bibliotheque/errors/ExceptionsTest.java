package com.bibliotheque.errors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests unitaires des exceptions personnalisées")
class ExceptionsTest {

    @Nested
    @DisplayName("Tests pour SearchClassNotInherits")
    class SearchClassNotInheritsTests {
        
        @Test
        @DisplayName("Doit stocker et retourner le bon message d'erreur et être du bon type")
        void shouldStoreMessageAndBeCorrectType() {
            String expectedMessage = "La classe spécifiée ne dérive pas de Work.";
            
            SearchClassNotInherits exception = new SearchClassNotInherits(expectedMessage);

            assertAll(
                () -> assertEquals(expectedMessage, exception.getMessage(), "Le message est incorrect"),
                () -> assertTrue(exception instanceof Exception, "Devrait hériter de Exception")
            );
        }
    }

    @Nested
    @DisplayName("Tests pour RegexFormatError")
    class RegexFormatErrorTests {
        
        @Test
        @DisplayName("Doit stocker et retourner le bon message d'erreur et être du bon type")
        void shouldStoreMessageAndBeCorrectType() {
            String expectedMessage = "L'adresse email ne respecte pas le format requis.";
            
            RegexFormatError exception = new RegexFormatError(expectedMessage);

            assertAll(
                () -> assertEquals(expectedMessage, exception.getMessage(), "Le message est incorrect"),
                () -> assertTrue(exception instanceof Exception, "Devrait hériter de Exception")
            );
        }
    }

    @Nested
    @DisplayName("Tests pour SearchStringTooSmall")
    class SearchStringTooSmallTests {
        
        @Test
        @DisplayName("Doit stocker et retourner le bon message d'erreur et être du bon type")
        void shouldStoreMessageAndBeCorrectType() {
            String expectedMessage = "La chaîne de recherche doit contenir au moins 3 caractères.";
            
            SearchStringTooSmall exception = new SearchStringTooSmall(expectedMessage);

            assertAll(
                () -> assertEquals(expectedMessage, exception.getMessage(), "Le message est incorrect"),
                () -> assertTrue(exception instanceof Exception, "Devrait hériter de Exception")
            );
        }
    }
}