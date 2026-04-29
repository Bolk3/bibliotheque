package com.bibliotheque.vue;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainFrame extends JFrame{

    JPanel          menu;
    JPanel          current;
    Vector<JPanel>  history = new Vector<>();
    Integer         tab = 0;

    public MainFrame() {
        setLayout(new BorderLayout());
        if (menu != null && current != null) {
            getContentPane().add(menu, BorderLayout.WEST);
            getContentPane().add(current, BorderLayout.CENTER);
        }
        getContentPane().setPreferredSize(new Dimension(720, 480));
        pack();
    }
}
