package com.bibliotheque;

import com.bibliotheque.vue.Catalog;
import com.bibliotheque.vue.MainFrame;
import com.bibliotheque.vue.Menu;

public class Main {
    public static void main(String[] args) {
        MainFrame   frame = new MainFrame();

        frame.setVisible(true);

        frame.goForward(new Catalog());
        frame.setMenu(new Menu(frame));
    }
}
