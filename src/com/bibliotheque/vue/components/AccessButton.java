package com.bibliotheque.vue.components;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.bibliotheque.vue.MainFrame;
import com.bibliotheque.vue.controllers.AccessButtonCTR;

public class AccessButton extends JButton{
    public AccessButton (String text, Runnable action) {
        super(text);
        this.addActionListener(new AccessButtonCTR(action));
    }
}
