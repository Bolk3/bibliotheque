package com.bibliotheque;

import com.bibliotheque.model.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        try {
            // 1. Initialisation
            Bibliotheque maBibi = new Bibliotheque("Médiathèque Municipale", "1 rue de la Paix");
            System.out.println("=== " + maBibi.getNom() + " ===");

            // 2. Création des auteurs et utilisateurs
            Author victorHugo = new Author("Victor", "Hugo");
            Librarian chefLib = new Librarian("Alice", "Admin", "alice@bibi.com", maBibi, "LIB-001", 5);
            Member member1 = new Member("Jean", "Dupont", "jean.dupont@email.com", maBibi);
            
            maBibi.addAuthor(victorHugo);
            maBibi.addLibrarian(chefLib);
            maBibi.addMember(member1);

            // 3. Création d'un livre et de ses exemplaires
            // Note : Assure-toi que le constructeur de Book prend bien ces arguments
            Book lesMis = new Book("978-2012705135", "Les Misérables", "Roman", "Hachette", new Date(), maBibi); lesMis.addAuthor(victorHugo);
            
            Copy copy1 = new Copy(State.NEUF, lesMis);
            Copy copy2 = new Copy(State.ABIME, lesMis);
            
            lesMis.addCopy(copy1);
            lesMis.addCopy(copy2);
            maBibi.addWork(lesMis);

            System.out.println("Livre enregistré : " + lesMis.getTitle());
            System.out.println("Nombre d'exemplaires : " + lesMis.getCopies().size());

            // 4. Simulation d'un emprunt (Date de retour : J+14)
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_YEAR, 14);
            
            System.out.println("\nTentative d'emprunt pour " + member1.getFirstName() + "...");
            Borrow currentBorrow = maBibi.createBorrow(copy1, member1, chefLib, cal.getTime());
            
            System.out.println("SUCCÈS : Emprunt validé.");
            System.out.println("Le livre doit être rendu le : " + currentBorrow.getExpectedDate());
            System.out.println("Disponibilité de l'exemplaire 1 : " + copy1.isAvailable());

            // 5. Test de recherche
            System.out.println("\nRecherche du mot 'Miser'...");
            List<Work> results = maBibi.findWorksByTitle("Miser");
            for (Work w : results) {
                System.out.println(" - Trouvé : " + w.getTitle() + " par " + w.getAuthor().getLastName());
            }

            // 6. Test d'événement
            System.out.println("\nOrganisation d'une conférence...");
            Speaker speaker = new Speaker("Zola", "Emile", "zola@ecrivain.fr", maBibi, "Littérature");
            Event conf = new Event(new Date(), cal.getTime(), "Conférence", maBibi);
            
            conf.addSpeaker(speaker, chefLib);
            conf.addParticipant(member1);
            maBibi.addEvent(conf);
            
            System.out.println("Événement créé : " + conf.getType() + " avec " + speaker.getFirstName() + " " + speaker.getLastName());

        } catch (Exception e) {
            System.err.println("ERREUR : " + e.getMessage());
        }
    }
}