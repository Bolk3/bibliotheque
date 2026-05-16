package com.bibliotheque.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Classe Librarian")
class LibrarianTest {

    private Bibliotheque library;

    @BeforeEach
    void setUp() {
        library = new Bibliotheque("Centrale", "Paris");
    }

    @Test
    @DisplayName("Un bibliothécaire doit collecter l'historique des tampons et emprunts qu'il valide")
    void shouldTrackValidatedActions() {
        Librarian librarian = new Librarian("Alice", "Smith", "alice@biblio.fr", library, "Manager", 3);
        Member member = new Member("Jean", "Dupont", "jean@mail.com", library);
        Book book = new Book("123", "Titre", "C-1", "Editeur", new Date(), library);
        Copy copy = new Copy(State.BON, book);
        Borrow borrow = new Borrow(new Date(), librarian, member, copy);

        // Simulation de validation d'actions
        librarian.addValidatedBorrow(borrow);
        ReturnStamp returnStamp = new ReturnStamp("Bon", borrow, librarian);
        librarian.addValidatedStamp(returnStamp);

        // Vérifications
        assertTrue(librarian.isPermission(3));
        assertTrue(librarian.isPosition("manager")); // Test de la normalisation textuelle
        assertEquals(1, librarian.getValidatedBorrows().size());
        assertEquals(1, librarian.getValidatedStamps().size());
        assertEquals("Alice Smith", librarian.toString());
    }
}