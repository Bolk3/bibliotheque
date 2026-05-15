package com.bibliotheque.vue.components;

import com.bibliotheque.model.Author;
import com.bibliotheque.model.Bibliotheque;
import com.bibliotheque.vue.Authors;
import com.bibliotheque.vue.controllers.AccessButtonCTR;
import java.awt.GridLayout;
import javax.swing.*;

public class AddAuthorForm extends JFrame {
    JTextField  firstNameField = new JTextField();
    JTextField  lastNameField  = new JTextField();
    JButton     confirm        = new JButton("Ajouter");
    JButton     cancel         = new JButton("Annuler");

    public AddAuthorForm(Bibliotheque logic, Authors parent) {
        setTitle("Ajouter un auteur");
        setSize(300, 200);
        setLocationRelativeTo(parent);
        setLayout(new GridLayout(3, 2, 5, 5));

        add(new JLabel("Prénom :"));
        add(firstNameField);
        add(new JLabel("Nom :"));
        add(lastNameField);
        add(confirm);
        add(cancel);

        confirm.addActionListener(new AccessButtonCTR(() -> {
            try {
                logic.addAuthor(new Author(firstNameField.getText(), lastNameField.getText()));
                parent.updateTable();
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }));

        cancel.addActionListener(new AccessButtonCTR(() -> dispose()));

        setVisible(true);
    }
}