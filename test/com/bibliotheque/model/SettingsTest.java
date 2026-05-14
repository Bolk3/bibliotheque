package com.bibliotheque.model;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

@DisplayName("Tests des Paramètres et Sécurité (Settings)")
public class SettingsTest {

    private Bibliotheque biblio;
    private Librarian lowLevelLib;
    private Librarian highLevelLib;

    @BeforeEach
    void setUp() {
        biblio = new Bibliotheque("Test Biblio", "ID-01");
        // Un bibliothécaire de niveau 1 (standard)
        lowLevelLib = new Librarian("L01", "Bas", "Niveau", biblio, "low@test.com", 1);
        // Un bibliothécaire de niveau 10 (superuser)
        highLevelLib = new Librarian("H01", "Haut", "Niveau", biblio, "high@test.com", 10);
    }

    @Test
    @DisplayName("Vérification des seuils de permission par défaut")
    void testPermissionThresholds() {
        // Ce test garantit que personne ne change les constantes sans s'en rendre compte
        assertEquals(1, Settings.PERM_VALIDATE_BORROW);
        assertEquals(5, Settings.PERM_ADMIN_METADATA);
        assertEquals(10, Settings.PERM_SUPERUSER);
    }

    @Nested
    @DisplayName("Logique de contrôle d'accès (hasAccess)")
    class AccessControl {

        @Test
        @DisplayName("Accès accordé si le niveau est égal au requis")
        void testAccessGrantedEqual() {
            assertTrue(Settings.hasAccess(lowLevelLib, 1), 
                "Le grade 1 devrait avoir accès au niveau 1");
        }

        @Test
        @DisplayName("Accès accordé si le niveau est supérieur au requis")
        void testAccessGrantedSuperior() {
            assertTrue(Settings.hasAccess(highLevelLib, Settings.PERM_ADMIN_METADATA), 
                "Le superuser devrait avoir accès aux metadata");
        }

        @Test
        @DisplayName("Accès refusé si le niveau est insuffisant")
        void testAccessDenied() {
            assertFalse(Settings.hasAccess(lowLevelLib, Settings.PERM_ADMIN_METADATA), 
                "Un grade 1 ne doit pas pouvoir modifier les metadata (niveau 5)");
        }

        @Test
        @DisplayName("Gestion des cas null")
        void testAccessNull() {
            assertFalse(Settings.hasAccess(null, 1), "Un utilisateur null ne doit avoir aucun accès");
        }
    }

    @Test
    @DisplayName("Le constructeur doit être privé et inaccessible")
    void testConstructorIsPrivate() throws NoSuchMethodException {
        Constructor<Settings> constructor = Settings.class.getDeclaredConstructor();
        // Vérifie que le constructeur est bien privé
        assertTrue(java.lang.reflect.Modifier.isPrivate(constructor.getModifiers()));
        
        // Test optionnel : vérifier qu'il lève une exception même si on force l'accès (via réflexion)
        constructor.setAccessible(true);
        assertThrows(InvocationTargetException.class, constructor::newInstance, 
            "L'instanciation devrait lever une UnsupportedOperationException");
    }
}