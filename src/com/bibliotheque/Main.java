package com.bibliotheque;

import com.bibliotheque.model.*;
import com.bibliotheque.vue.MainFrame;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Main {
    
    // Méthode utilitaire utilisant uniquement java.util.Date
    @SuppressWarnings("deprecation")
    private static Date createDate(int year, int month, int day) {
        // year - 1900 car l'époque commence en 1900
        // month - 1 car Janvier vaut 0
        return new Date(year - 1900, month - 1, day);
    }

    public static void main(String[] args) {
        // 1. Initialisation de la bibliothèque
        Bibliotheque bib = new Bibliotheque("La Grande Bibliothèque", "1 rue du Java");

        // 2. Création des auteurs (Écrivains et Réalisateurs)
        List<Author> authors = new ArrayList<>();
        try {
            authors.add(new Author("Victor", "Hugo"));        // Index 0
            authors.add(new Author("Émile", "Zola"));         // Index 1
            authors.add(new Author("Albert", "Camus"));       // Index 2
            authors.add(new Author("George", "Orwell"));      // Index 3
            authors.add(new Author("Joanne", "Rowling"));     // Index 4
            authors.add(new Author("Christopher", "Nolan"));  // Index 5
            authors.add(new Author("Quentin", "Tarantino"));  // Index 6
        } catch (IllegalArgumentException e) {
            System.err.println("Erreur de format nom/prénom : " + e.getMessage());
        }
        
        for (Author a : authors) {
            bib.addAuthor(a);
        }

        // 3. Personnel (Librarians)
        Librarian adminStaff = new Librarian("Jean", "Dupont", "j.dupont@bib.fr", bib, "SUPER_ADMIN", 99);
        Librarian managerStaff = new Librarian("Sophie", "Martin", "s.martin@bib.fr", bib, "MANAGER", 10);
        Librarian juniorStaff1 = new Librarian("Lucas", "Bernard", "l.bernard@bib.fr", bib, "ASSISTANT", 5);
        Librarian juniorStaff2 = new Librarian("Emma", "Petit", "e.petit@bib.fr", bib, "STAGIAIRE", 2);

        bib.addLibrarian(adminStaff);
        bib.addLibrarian(managerStaff);
        bib.addLibrarian(juniorStaff1);
        bib.addLibrarian(juniorStaff2);

        // 4. Membres
        bib.addMember(new Member("Alice", "Zola", "alice.zola@mail.com", bib));
        bib.addMember(new Member("Thomas", "Dubois", "t.dubois@mail.com", bib));
        bib.addMember(new Member("Chloé", "Lefebvre", "chloe.le@mail.com", bib));
        bib.addMember(new Member("Antoine", "Moreau", "a.moreau@mail.com", bib));
        bib.addMember(new Member("Sarah", "Rousseau", "sarah.r@mail.com", bib));
        bib.addMember(new Member("Maxime", "Girard", "max.girard@mail.com", bib));

        // 5. Données réelles pour les Livres (Titre, Catégorie, Éditeur, Index Auteur, Année, Mois, Jour)
        Object[][] realBooksData = {
            // Victor Hugo
            {"Les Misérables", "Roman Historique", "Gallimard", 0, 1862, 3, 30},
            {"Notre-Dame de Paris", "Roman Historique", "Le Livre de Poche", 0, 1831, 3, 16},
            {"Les Contemplations", "Poésie", "Gallimard", 0, 1856, 4, 23},
            {"Hernani", "Théâtre", "Flammarion", 0, 1830, 2, 25},
            {"Le Dernier Jour d'un condamné", "Roman", "Librio", 0, 1829, 2, 1},
            {"Claude Gueux", "Nouvelle", "Pocket", 0, 1834, 7, 6},

            // Émile Zola
            {"Germinal", "Naturalisme", "Charpentier", 1, 1885, 3, 1},
            {"L'Assommoir", "Naturalisme", "Gallimard", 1, 1877, 1, 1},
            {"Au Bonheur des Dames", "Naturalisme", "Pocket", 1, 1883, 3, 1},
            {"La Bête Humaine", "Naturalisme", "Le Livre de Poche", 1, 1890, 3, 1},
            {"Nana", "Naturalisme", "GF Flammarion", 1, 1880, 2, 15},
            {"Le Ventre de Paris", "Naturalisme", "Gallimard", 1, 1873, 4, 1},

            // Albert Camus
            {"L'Étranger", "Philosophie / Roman", "Gallimard", 2, 1942, 5, 19},
            {"La Peste", "Roman", "Gallimard", 2, 1947, 6, 10},
            {"Le Mythe de Sisyphe", "Essai", "Folio", 2, 1942, 10, 1},
            {"Caligula", "Théâtre", "Gallimard", 2, 1944, 5, 1},
            {"Les Justes", "Théâtre", "Folio", 2, 1949, 12, 15},
            {"Le Premier Homme", "Roman Autobiographique", "Gallimard", 2, 1994, 4, 15},

            // George Orwell
            {"1984", "Dystopie", "Seuil", 3, 1949, 6, 8},
            {"La Ferme des animaux", "Satire politique", "Gallimard", 3, 1945, 8, 17},
            {"Hommage à la Catalogne", "Récit documentaire", "10-18", 3, 1938, 4, 25},
            {"Dans la dèche à Paris et à Londres", "Autobiographie", "Pocket", 3, 1933, 1, 9},
            {"Une histoire birmane", "Roman", "Penguin", 3, 1934, 10, 25},
            {"Un peu d'air frais", "Roman", "Gallimard", 3, 1939, 6, 12},

            // J.K. Rowling
            {"Harry Potter à l'école des sorciers", "Fantastique", "Gallimard Jeunesse", 4, 1997, 6, 26},
            {"Harry Potter et la Chambre des secrets", "Fantastique", "Gallimard Jeunesse", 4, 1998, 7, 2},
            {"Harry Potter et le Prisonnier d'Azkaban", "Fantastique", "Gallimard Jeunesse", 4, 1999, 7, 8},
            {"Harry Potter et la Coupe de feu", "Fantastique", "Gallimard Jeunesse", 4, 2000, 7, 8},
            {"Harry Potter et l'Ordre du Phénix", "Fantastique", "Gallimard Jeunesse", 4, 2003, 6, 21},
            {"Harry Potter et le Prince de sang-mêlé", "Fantastique", "Gallimard Jeunesse", 4, 2005, 7, 16}
        };

        // Génération des 30 Livres
        for (int i = 0; i < realBooksData.length; i++) {
            String title = (String) realBooksData[i][0];
            String category = (String) realBooksData[i][1];
            String publisher = (String) realBooksData[i][2];
            int authorIndex = (int) realBooksData[i][3];
            int year = (int) realBooksData[i][4];
            int month = (int) realBooksData[i][5];
            int day = (int) realBooksData[i][6];

            Book book = new Book(
                "ISBN-" + String.format("%04d", i + 1),
                title,
                category,
                publisher,
                createDate(year, month, day), 
                bib
            );

            if (authorIndex < authors.size()) {
                Author select = authors.get(authorIndex);
                book.addAuthor(select);
                select.addWork(book);
            }
            
            book.addCopy(new Copy(State.NEUF, book));
            book.addCopy(new Copy(State.BON, book));
            bib.addWork(book);
        }

        // 6. Données réelles pour les DVD (Titre, Genre/Catégorie, Studio, Index Réalisateur, Région, Année, Mois, Jour)
        Object[][] realDvdsData = {
            {"Inception", "Science-Fiction", "Warner Bros.", 5, "Zone 2", 2010, 7, 21},
            {"Interstellar", "Science-Fiction", "Paramount", 5, "Zone 2", 2014, 11, 5},
            {"The Dark Knight", "Action / Thriller", "Warner Bros.", 5, "All Zone", 2008, 8, 13},
            {"Pulp Fiction", "Policier / Drame", "Miramax", 6, "Zone 2", 1994, 10, 26},
            {"Inglourious Basterds", "Guerre / Action", "Universal", 6, "Zone 2", 2009, 8, 19}
        };

        // Génération des DVD
        for (int i = 0; i < realDvdsData.length; i++) {
            String title = (String) realDvdsData[i][0];
            String category = (String) realDvdsData[i][1];
            String studio = (String) realDvdsData[i][2];
            int directorIndex = (int) realDvdsData[i][3];
            String region = (String) realDvdsData[i][4];
            int year = (int) realDvdsData[i][5];
            int month = (int) realDvdsData[i][6];
            int day = (int) realDvdsData[i][7];

            Dvd dvd = new Dvd(
                title,
                category,
                studio,
                createDate(year, month, day), 
                bib,
                region
            );

            if (directorIndex < authors.size()) {
                Author director = authors.get(directorIndex);
                dvd.addAuthor(director);
                director.addWork(dvd);
            }

            dvd.addCopy(new Copy(State.NEUF, dvd));
            dvd.addCopy(new Copy(State.BON, dvd));
            bib.addWork(dvd);
        }

        // 7. Interface graphique
        MainFrame frame = new MainFrame(bib);
        frame.currentUser = adminStaff; 
        frame.setVisible(true);
    }
}