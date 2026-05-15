package com.bibliotheque.vue.components;

import com.bibliotheque.model.Bibliotheque;
import com.bibliotheque.vue.Authors;
import com.bibliotheque.vue.controllers.AccessButtonCTR;
import java.awt.GridLayout;
import javax.swing.*;

public class DeleteAuthorForm extends JFrame {
    JButton confirm = new JButton("Supprimer");
    JButton cancel  = new JButton("Annuler");

    public DeleteAuthorForm(Bibliotheque logic, Authors parent, String firstName, String lastName) {
        setTitle("Supprimer un auteur");
        setSize(300, 200);
        setLocationRelativeTo(parent);
        setLayout(new GridLayout(2, 1, 5, 5));

        add(new JLabel("Supprimer " + firstName + " " + lastName + " ?", SwingConstants.CENTER));

        JPanel buttons = new JPanel();
        buttons.add(confirm);
        buttons.add(cancel);
        add(buttons);

        confirm.addActionListener(new AccessButtonCTR(() -> {
            logic.removeAuthor(firstName, lastName);
            parent.updateTable();
            dispose();
        }));

        cancel.addActionListener(new AccessButtonCTR(() -> dispose()));

        setVisible(true);
    }
}