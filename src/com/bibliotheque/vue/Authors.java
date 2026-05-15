package com.bibliotheque.vue;

import com.bibliotheque.model.Author;
import com.bibliotheque.model.Bibliotheque;
import com.bibliotheque.vue.components.AccessButton;
import com.bibliotheque.vue.components.AddAuthorForm;
import com.bibliotheque.vue.components.DeleteAuthorForm;
import java.awt.BorderLayout;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class Authors extends JPanel{
    Bibliotheque        handleLogic;
    MainFrame           handleVue;
    JTable              table;
    JPanel              actions;
    DefaultTableModel   model;

    public Authors(Bibliotheque logic, MainFrame vue) {
        this.handleLogic = logic;
        this.handleVue   = vue;
        this.setLayout(new BorderLayout());

        model = new DefaultTableModel(new String[]{"Prénom", "Nom"}, 0);
        updateTable();

        table = new JTable(model);

        AccessButton add = new AccessButton("Ajouter un auteur", () -> new AddAuthorForm(handleLogic, this));
        AccessButton delete = new AccessButton("Retirer un auteur", () -> {
            int selected = table.getSelectedRow();
            if (selected == -1) {
                JOptionPane.showMessageDialog(this, "Sélectionnez un auteur.", "Aucune sélection", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String prenom = (String) model.getValueAt(selected, 0);
            String nom    = (String) model.getValueAt(selected, 1);
            new DeleteAuthorForm(handleLogic, this, prenom, nom);
        });

        actions = new JPanel();
        actions.add(add);
        actions.add(delete);

        this.add(new JScrollPane(table), BorderLayout.CENTER);
        this.add(this.actions, BorderLayout.NORTH);
    }

    public void updateTable() {
        model.setRowCount(0); // vide le tableau
        for (Author a : handleLogic.getAuthors()) {
            model.addRow(new Object[]{a.getFirstName(), a.getLastName()});
        }
    }
}
