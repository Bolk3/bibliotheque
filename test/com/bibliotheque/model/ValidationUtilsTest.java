package com.bibliotheque.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests complets de ValidationUtils")
class ValidationUtilsTest {

    @Nested
    @DisplayName("Validation des Noms et Prénoms")
    class NameValidationTests {

        @ParameterizedTest
        @ValueSource(strings = {"Jean", "Hélène", "François", "Édouard-Pierre", "O'Connor", "Jean-Noël"})
        @DisplayName("Doit accepter les structures de noms valides")
        void shouldAcceptValidNames(String name) {
            assertAll(
                () -> assertTrue(ValidationUtils.isFirstNameValid(name), "Le prénom devrait être valide"),
                () -> assertTrue(ValidationUtils.isLastNameValid(name), "Le nom devrait être valide")
            );
        }

        @ParameterizedTest
        @ValueSource(strings = {"jean42", "Jean_Paul", "Jean@", "   ", "", "-Jean", "'Lucas"})
        @DisplayName("Doit refuser les noms contenant des caractères interdits ou mal positionnés")
        void shouldRejectInvalidNames(String name) {
            assertAll(
                () -> assertFalse(ValidationUtils.isFirstNameValid(name), "Le prénom devrait être refusé"),
                () -> assertFalse(ValidationUtils.isLastNameValid(name), "Le nom devrait être refusé")
            );
        }

        @Test
        @DisplayName("Doit retourner false si la valeur passée est null")
        void shouldReturnFalseWhenNameIsNull() {
            assertAll(
                () -> assertFalse(ValidationUtils.isFirstNameValid(null)),
                () -> assertFalse(ValidationUtils.isLastNameValid(null))
            );
        }
    }

    @Nested
    @DisplayName("Validation des Adresses Email")
    class EmailValidationTests {

        @ParameterizedTest
        @ValueSource(strings = {"user@example.com", "jean.dupont@domain.fr", "info%test@sub.domain.org"})
        @DisplayName("Doit valider les formats d'emails standards")
        void shouldAcceptValidEmails(String email) {
            assertTrue(ValidationUtils.isEmailValid(email));
        }

        @ParameterizedTest
        @ValueSource(strings = {"user@example", "user.com", "@domain.fr", "user@.fr", "user@domain.c", "user @domain.com"})
        @DisplayName("Doit rejeter les formats d'emails corrompus ou incomplets")
        void shouldRejectInvalidEmails(String email) {
            assertFalse(ValidationUtils.isEmailValid(email));
        }

        @Test
        @DisplayName("Doit retourner false si l'email est null")
        void shouldReturnFalseWhenEmailIsNull() {
            assertFalse(ValidationUtils.isEmailValid(null));
        }
    }

    @Nested
    @DisplayName("Normalisation de Chaînes")
    class NormalizationTests {

        @ParameterizedTest(name = "L''entrée \"{0}\" doit produire \"{1}\"")
        @CsvSource({
            "'  Hélène  ', 'helene'",
            "'François', 'francois'",
            "'Édouard-PIERRE', 'edouard-pierre'",
            "'D''Artagnan', 'd''artagnan'"
        })
        @DisplayName("normalize() doit supprimer accents, trimer et passer en minuscule")
        void shouldNormalizeTextCorrectly(String input, String expected) {
            assertEquals(expected, ValidationUtils.normalize(input));
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("normalize() doit renvoyer une chaîne vide si l'input est null ou vide")
        void shouldReturnEmptyStringWhenNormalizeInputIsNullOrEmpty(String input) {
            assertEquals("", ValidationUtils.normalize(input));
        }

        @Test
        @DisplayName("normalizeEmail() doit uniquement trimmer et mettre en minuscule")
        void shouldNormalizeEmailCorrectly() {
            assertAll(
                () -> assertEquals("user.name@example.com", ValidationUtils.normalizeEmail(" User.Name@Example.COM ")),
                () -> assertEquals("gérard@domain.com", ValidationUtils.normalizeEmail("  Gérard@Domain.COM "))
            );
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("normalizeEmail() doit renvoyer une chaîne vide si l'email est null ou vide")
        void shouldReturnEmptyStringWhenNormalizeEmailInputIsNullOrEmpty(String input) {
            assertEquals("", ValidationUtils.normalizeEmail(input));
        }
    }
}