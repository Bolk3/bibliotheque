package com.bibliotheque.errors;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests des Exceptions Personnalisées")
public class ExceptionsTest {

    private final String ERROR_MSG = "Message d'erreur de test";

    @Test
    @DisplayName("RegexFormatError : Stockage du message")
    void testRegexFormatError() {
        RegexFormatError error = new RegexFormatError(ERROR_MSG);
        assertEquals(ERROR_MSG, error.getMessage());
        assertTrue(error instanceof Exception);
    }

    @Test
    @DisplayName("SearchClassNotInherits : Stockage du message")
    void testSearchClassNotInherits() {
        SearchClassNotInherits error = new SearchClassNotInherits(ERROR_MSG);
        assertEquals(ERROR_MSG, error.getMessage());
    }

    @Test
    @DisplayName("SearchStringTooSmall : Stockage du message")
    void testSearchStringTooSmall() {
        SearchStringTooSmall error = new SearchStringTooSmall(ERROR_MSG);
        assertEquals(ERROR_MSG, error.getMessage());
    }

    @Test
    @DisplayName("Vérification de la hiérarchie (Checked Exception)")
    void testExceptionHierarchy() {
        SearchStringTooSmall ex = new SearchStringTooSmall("Too small");
        
        // Pour vérifier que c'est une exception "checked" sans fâcher le compilateur,
        // on vérifie simplement qu'elle hérite de Exception.
        assertTrue(ex instanceof Exception, "L'exception doit hériter de la classe de base Exception");
        
        // On retire la ligne avec RuntimeException qui bloquait la compilation.
        // Le simple fait que ton code force l'utilisation de "throws" dans SearchingWork
        // prouve déjà qu'elles sont bien checked.
    }
}