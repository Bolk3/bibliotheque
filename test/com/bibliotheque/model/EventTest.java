package com.bibliotheque.model;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

@DisplayName("Tests de la classe Event")
public class EventTest {

    private Event event;
    private Bibliotheque biblio;
    private Date startDate;
    private Date endDate;

    @BeforeEach
    void setUp() {
        // Bibliotheque(Nom, Identifiant/Adresse)
        biblio = new Bibliotheque("Médiathèque Centrale", "MC-001");
        
        startDate = new Date(System.currentTimeMillis() - 1000 * 60 * 30);
        endDate = new Date(System.currentTimeMillis() + 1000 * 60 * 30);
        event = new Event(startDate, endDate, "Atelier", biblio);
    }

    @Nested
    @DisplayName("Tests de gestion des participants et intervenants")
    class PeopleManagement {

        @Test
        @DisplayName("Ajout d'un participant")
        void testAddParticipant() {
            // Correction : Member n'attend que (id, nom, prenom, handler)
            Member member = new Member("JD001", "Jean", "Dupont", biblio);
            event.addParticipant(member);

            assertTrue(event.getParticipants().contains(member), "L'événement doit contenir le membre");
        }

        @Test
        @DisplayName("Ajout d'un intervenant avec permissions")
        void testAddSpeakerWithPermission() {
            Speaker speaker = new Speaker("Nom", "Prenom", "Informatique", biblio, "expert@test.com");
            
            // Utilisation de la constante de Settings pour garantir le succès
            int niveauRequis = Settings.PERM_ADMIN_METADATA;
            Librarian admin = new Librarian("Staff01", "Admin", "Admin", biblio, "admin@biblio.com", niveauRequis);

            event.addSpeaker(speaker, admin);
            assertTrue(event.getSpeakers().contains(speaker));
        }

        @Test
        @DisplayName("Erreur lors de l'ajout d'intervenant sans permission")
        void testAddSpeakerWithoutPermission() {
            Speaker speaker = new Speaker("Nom", "Prenom", "Informatique", biblio, "expert@test.com");
            Librarian stagiaire = new Librarian("Stag01", "Petit", "Jean", biblio, "st@biblio.com", 0);

            assertThrows(IllegalStateException.class, () -> {
                event.addSpeaker(speaker, stagiaire);
            });
        }
    }

    @Nested
    @DisplayName("Tests de statut temporel")
    class TimeStatus {
        @Test
        @DisplayName("L'événement est actif")
        void testIsActive() {
            assertTrue(event.isActive());
        }

        @Test
        @DisplayName("L'événement est terminé")
        void testIsFinished() {
            // On fixe une fin dans le passé
            event.setEndDate(new Date(System.currentTimeMillis() - 5000));
            assertTrue(event.isFinished());
        }
    }

    @Test
    @DisplayName("L'encapsulation des listes est respectée")
    void testUnmodifiableLists() {
        assertThrows(UnsupportedOperationException.class, () -> {
            // Correction ici aussi pour le constructeur de Member
            event.getParticipants().add(new Member("X", "X", "X", biblio));
        });
    }
}