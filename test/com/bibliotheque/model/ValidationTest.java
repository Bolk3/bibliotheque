package com.bibliotheque.model;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests des Utilitaires de Validation")
class ValidationUtilsTest {

    @Nested
    @DisplayName("Validation des Noms (Prénoms/Noms)")
    class NameValidation {

        @Test
        @DisplayName("Noms valides (simples et composés)")
        void testValidNames() {
            assertAll("Noms qui devraient être acceptés",
                () -> assertTrue(ValidationUtils.isFirstNameValid("Jean-Pierre"), "Tiret"),
                () -> assertTrue(ValidationUtils.isFirstNameValid("Hélène"), "Accents"),
                () -> assertTrue(ValidationUtils.isLastNameValid("O'Connor"), "Apostrophe"),
                () -> assertTrue(ValidationUtils.isLastNameValid("De La Fontaine"), "Espaces")
            );
        }

        @Test
        @DisplayName("Noms invalides (caractères interdits)")
        void testInvalidNames() {
            assertAll("Noms qui devraient être refusés",
                () -> assertFalse(ValidationUtils.isFirstNameValid("123"), "Chiffres"),
                () -> assertFalse(ValidationUtils.isFirstNameValid(" jean"), "Espace au début"),
                () -> assertFalse(ValidationUtils.isLastNameValid("Jean@Doe"), "Symbole @"),
                () -> assertFalse(ValidationUtils.isLastNameValid(""), "Vide")
            );
        }
    }

    @Nested
    @DisplayName("Validation des Emails")
    class EmailValidation {

        @Test
        @DisplayName("Emails au format correct")
        void testValidEmails() {
            assertAll(
                () -> assertTrue(ValidationUtils.isEmailValid("user@example.com")),
                () -> assertTrue(ValidationUtils.isEmailValid("jean.dupont123@service.fr"))
            );
        }

        @Test
        @DisplayName("Emails au format incorrect")
        void testInvalidEmails() {
            assertAll(
                () -> assertFalse(ValidationUtils.isEmailValid("user@com"), "Manque extension"),
                () -> assertFalse(ValidationUtils.isEmailValid("@example.com"), "Manque partie locale"),
                () -> assertFalse(ValidationUtils.isEmailValid("user.example.com"), "Manque @")
            );
        }
    }

    @Nested
    @DisplayName("Normalisation des données")
    class Normalization {

        @Test
        @DisplayName("Normalisation de texte (Accents, Casse, Espaces)")
        void testTextNormalization() {
            assertAll(
                () -> assertEquals("helene", ValidationUtils.normalize("  Hélène  ")),
                () -> assertEquals("francois", ValidationUtils.normalize("François")),
                () -> assertEquals("edouard-pierre", ValidationUtils.normalize("Édouard-PIERRE")),
                () -> assertEquals("", ValidationUtils.normalize(null), "Gère le null")
            );
        }

        @Test
        @DisplayName("Normalisation d'Email")
        void testEmailNormalization() {
            assertAll(
                () -> assertEquals("user.name@example.com", ValidationUtils.normalizeEmail(" User.Name@Example.COM ")),
                () -> assertEquals("", ValidationUtils.normalizeEmail(null), "Gère le null")
            );
        }
    }
}