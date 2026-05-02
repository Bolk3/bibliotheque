package com.bibliotheque.vue.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AccessButtonCTR implements ActionListener{

    private Runnable action;

    public AccessButtonCTR (Runnable action) {
        this.action = action;
    }

    public void actionPerformed(ActionEvent e) { 
        action.run();
    }
}
