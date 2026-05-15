package com.bibliotheque;
import com.bibliotheque.model.*;
import com.bibliotheque.vue.MainFrame;
import java.util.Date;

public class Main {
    public static void main(String[] args) {
        Bibliotheque bib = new Bibliotheque("Ma Bibliothèque", "12 rue des Livres");

        // Livres
        for (int i = 1; i <= 30; i++) {
            bib.addWork(new Book(
                "ISBN-" + i,
                "Titre du livre " + i,
                "Categorie " + (i % 5),
                "Editeur " + (i % 3),
                new Date(),
                bib
            ));
        }

        MainFrame frame = new MainFrame(bib);
        frame.setVisible(true);
    }
}