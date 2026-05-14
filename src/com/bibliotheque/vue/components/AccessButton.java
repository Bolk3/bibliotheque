package com.bibliotheque.vue.components;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import com.bibliotheque.vue.controllers.AccessButtonCTR;

public class AccessButton extends JButton{
    public AccessButton (String text, Runnable action) {
        super(text);
        this.addActionListener(new AccessButtonCTR(action));
    }

    public AccessButton (ImageIcon image, Runnable action) {
        this.setIcon(image);
        this.addActionListener(new AccessButtonCTR(action));
    }
}
