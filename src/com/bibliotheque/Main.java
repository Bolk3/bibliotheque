package com.bibliotheque;

import com.bibliotheque.model.Bibliotheque;
import com.bibliotheque.model.Librarian;
import com.bibliotheque.vue.MainFrame;

public class Main {

    public static void main(String[] args) {
        Bibliotheque    logic= new Bibliotheque("null", "null");
        MainFrame       frame = new MainFrame(logic);

        frame.currentUser = new Librarian("a", "a", "a@a.frame", logic, "superadmin", 10);

        frame.setVisible(true);
    }
}