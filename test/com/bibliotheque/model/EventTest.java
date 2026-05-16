package com.bibliotheque.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests unitaires - Classe Event (Événement)")
class EventTest {

    private Bibliotheque library;
    private Librarian adminLibrarian;
    private Librarian lowPermLibrarian;
    private Book dummyBook;

    @BeforeEach
    void setUp() {
        library = new Bibliotheque("Centrale", "Paris");
        adminLibrarian = new Librarian("Alice", "Admin", "alice@biblio.fr", library, "Directrice", 5);
        lowPermLibrarian = new Librarian("Bob", "Stagiaire", "bob@biblio.fr", library, "Stagiaire", 1);
        dummyBook = new Book("978-3-16-148410-0", "Le Nom de la Rose", "R-12", "Grasset", new Date(), library);
    }

    @Nested
    @DisplayName("Tests du cycle de vie temporel (Active / Finished)")
    class TemporalStateTests {

        @Test
        @DisplayName("Un événement en cours doit être actif et non terminé")
        void shouldBeActiveWhenCurrentTimeIsBetweenDates() {
            long now = System.currentTimeMillis();
            Date start = new Date(now - 3600000); // Commencé il y a 1h
            Date end = new Date(now + 3600000);   // Se termine dans 1h

            Event event = new Event(start, end, "Atelier", library);

            assertTrue(event.isActive(), "L'événement devrait être actif");
            assertFalse(event.isFinished(), "L'événement ne devrait pas être terminé");
        }

        @Test
        @DisplayName("Un événement futur ne doit être ni actif ni terminé")
        void shouldNotBeActiveOrFinishedWhenEventIsFuture() {
            long now = System.currentTimeMillis();
            Date start = new Date(now + 3600000); // Commence dans 1h
            Date end = new Date(now + 7200000);   // Se termine dans 2h

            Event event = new Event(start, end, "Conférence", library);

            assertFalse(event.isActive());
            assertFalse(event.isFinished());
        }

        @Test
        @DisplayName("Un événement passé doit être terminé et inactif")
        void shouldBeFinishedWhenCurrentTimeIsAfterEndDate() {
            long now = System.currentTimeMillis();
            Date start = new Date(now - 7200000); // Commencé il y a 2h
            Date end = new Date(now - 3600000);   // Terminé il y a 1h

            Event event = new Event(start, end, "Exposition", library);

            assertFalse(event.isActive());
            assertTrue(event.isFinished());
        }
    }

    @Nested
    @DisplayName("Tests d'encapsulation (Copies défensives)")
    class EncapsulationTests {

        @Test
        @DisplayName("getStartDate et getEndDate doivent renvoyer des copies pour éviter les mutations externes")
        void shouldReturnDefensiveCopiesOfDates() {
            Date start = new Date();
            Date end = new Date(start.getTime() + 10000);
            Event event = new Event(start, end, "Lecture", library);

            Date internalStart = event.getStartDate();
            // On tente de modifier directement la Date renvoyée par le getter
            internalStart.setTime(0);

            // La date réelle de l'événement ne doit pas avoir été altérée
            assertNotEquals(0, event.getStartDate().getTime(), "Le getter doit appliquer une copie défensive");
        }
    }

    @Nested
    @DisplayName("Tests des relations bi-directionnelles et droits de sécurité")
    class RelationAndSecurityTests {

        @Test
        @DisplayName("addParticipant doit enregistrer le membre et déclencher la relation bi-directionnelle")
        void shouldEstablishBidirectionalRelationWithMember() {
            Event event = new Event(new Date(), new Date(), "Club de lecture", library);
            Member member = new Member("Hugo", "Victor", "hugo@mail.fr", library);

            event.addParticipant(member);

            // Vérification de la liaison dans les deux sens
            assertTrue(event.getParticipants().contains(member));
            assertTrue(member.getParticipatedEvents().contains(event), "Le membre doit posséder l'événement dans son historique");
        }

        @Test
        @DisplayName("addSpeaker doit ajouter l'intervenant si le bibliothécaire a les droits requis")
        void shouldAllowAddingSpeakerWithProperPermissions() {
            Event event = new Event(new Date(), new Date(), "Rencontre", library);
            Speaker speaker = new Speaker("Hubert", "Reeves", "hubert@space.fr", library, "Sciences");

            // L'admin possède la permission (ex: 5) requise par Settings.PERM_ADMIN_METADATA
            assertDoesNotThrow(() -> event.addSpeaker(speaker, adminLibrarian));
            
            assertTrue(event.getSpeakers().contains(speaker));
            assertTrue(speaker.getParticipatedEvents().contains(event), "L'intervenant doit posséder l'événement dans sa collection");
        }

        @Test
        @DisplayName("addSpeaker doit lever une IllegalStateException si les privilèges sont insuffisants")
        void shouldRejectAddingSpeakerWithLowPermissions() {
            Event event = new Event(new Date(), new Date(), "Rencontre", library);
            Speaker speaker = new Speaker("Hubert", "Reeves", "hubert@space.fr", library, "Sciences");

            // Le stagiaire (permission 1) n'a pas les droits requis
            assertThrows(IllegalStateException.class, () -> {
                event.addSpeaker(speaker, lowPermLibrarian);
            });
            
            assertFalse(event.getSpeakers().contains(speaker));
        }

        @Test
        @DisplayName("addRelatedWork doit lier une œuvre à l'événement de manière sécurisée")
        void shouldLinkRelatedWorkSafely() {
            Event event = new Event(new Date(), new Date(), "Exposition", library);
            
            event.addRelatedWork(dummyBook);

            assertTrue(event.getRelatedWorks().contains(dummyBook));
            assertEquals(1, event.getRelatedWorks().size());
            
            // Doublons et valeurs nulles doivent être ignorés sans planter
            event.addRelatedWork(dummyBook);
            event.addRelatedWork(null);
            assertEquals(1, event.getRelatedWorks().size());
        }
    }
}