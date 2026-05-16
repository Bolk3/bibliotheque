package com.bibliotheque.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Classe Speaker (Intervenant)")
class SpeakerTest {

    private Bibliotheque library;
    private Date startDate;
    private Date endDate;

    @BeforeEach
    void setUp() {
        library = new Bibliotheque("Centrale", "Paris");
        
        // Définition de dates valides pour l'événement
        long now = System.currentTimeMillis();
        startDate = new Date(now);
        endDate = new Date(now + 3600000); // Durée : 1 heure
    }

    @Test
    @DisplayName("Un intervenant doit pouvoir être assigné à des événements et matcher sa spécialité")
    void shouldManageSpeakerEventsAndSpecialty() {
        // Initialisation du Speaker
        Speaker speaker = new Speaker("Hubert", "Reeves", "hubert@space.com", library, "Astrophysique");

        // 1. Test de la requête normalisée sur la spécialité (Insensible à la casse)
        assertTrue(speaker.isSpecialty("astrophysique"));
        assertTrue(speaker.isSpecialty("ASTROPHYSIQUE"));
        assertFalse(speaker.isSpecialty("Littérature"));
        
        // 2. Initialisation correcte de l'événement (4 paramètres requis)
        Event conference = new Event(startDate, endDate, "Conférence", library);
        
        // 3. Vérification des liaisons d'événements
        assertFalse(speaker.hasParticipated(conference), "L'intervenant ne doit pas avoir participé avant l'ajout");
        
        speaker.addEvent(conference);
        
        assertTrue(speaker.hasParticipated(conference), "L'intervenant doit être marqué comme ayant participé");
        assertEquals(1, speaker.getParticipatedEvents().size(), "L'historique doit contenir exactement 1 événement");
        assertEquals(conference, speaker.getParticipatedEvents().get(0));
    }
}