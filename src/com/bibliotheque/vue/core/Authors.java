package com.bibliotheque.vue.core;

import com.bibliotheque.model.Author;
import com.bibliotheque.model.Bibliotheque;
import com.bibliotheque.vue.MainFrame;
import com.bibliotheque.vue.components.AccessButton;
import com.bibliotheque.vue.components.forms.AddAuthorForm;
import com.bibliotheque.vue.components.forms.DeleteAuthorForm;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

/**
 * Visual directory dashboard tracking registered book creators and authors.
 * 
 * <p>Provides view rosters for viewing metadata profiles, invoking appending wizard flows, 
 * or triggering data-layer removal sequences for selected records.</p>
 * 
 * @see Author
 * @see MainFrame
 * @see Bibliotheque
 * 
 * @version 1.1
 */
public class Authors extends JPanel {

    private final Bibliotheque      _businessLogic;
    private final MainFrame         _viewController;
    
    private final JTable            _tableAuthors;
    private final DefaultTableModel _tableModel;
    private final JPanel            _actionControlBar;

    /**
     * Constructs a centralized author registry index management view panel.
     * 
     * @param logic the system backend layer manager facade
     * @param vue   the principal structural navigation view framework context
     */
    public Authors(Bibliotheque logic, MainFrame vue) {
        this._businessLogic = logic;
        this._viewController = vue;
        
        this.setLayout(new BorderLayout(10, 10));
        this.setBackground(Color.WHITE);
        this.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // --- SECTION HAUT : VIEW BANNER HEADLINE ---
        JLabel titleLabel = new JLabel("Gestion des Auteurs", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        this.add(titleLabel, BorderLayout.NORTH);

        // --- SECTION CENTRALE : TABULAR CREATOR ROSTER ---
        _tableModel = new DefaultTableModel(new String[]{"Prénom", "Nom"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        this.updateTable();

        _tableAuthors = new JTable(_tableModel);
        _tableAuthors.setRowHeight(25);
        _tableAuthors.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        this.add(new JScrollPane(_tableAuthors), BorderLayout.CENTER);

        // --- SECTION BAS : DISPATCH ACTION PANEL BAR ---
        _actionControlBar = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        _actionControlBar.setBackground(new Color(240, 240, 240));
        _actionControlBar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));

        AccessButton registerAuthorButton = new AccessButton("Ajouter un auteur", () -> 
                _viewController.goForward(new AddAuthorForm(_businessLogic, _viewController)));
        
        AccessButton removeAuthorButton = new AccessButton("Retirer un auteur", this::handleRemoveAuthorAction);

        _actionControlBar.add(registerAuthorButton);
        _actionControlBar.add(removeAuthorButton);
        this.add(_actionControlBar, BorderLayout.SOUTH);
    }

    // -------------------------------------------------------------------------
    // Execution Handlers
    // -------------------------------------------------------------------------

    private void handleRemoveAuthorAction() {
        int selectedRow = _tableAuthors.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                    "Sélectionnez un auteur dans le tableau.", 
                    "Aucune sélection", 
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String targetFirstName = (String) _tableModel.getValueAt(selectedRow, 0);
        String targetLastName  = (String) _tableModel.getValueAt(selectedRow, 1);
        
        // Instantiates the structural modification sub-dialog sequence wizard
        new DeleteAuthorForm(_businessLogic, this, targetFirstName, targetLastName);
    }

    // -------------------------------------------------------------------------
    // Synchronization Operations
    // -------------------------------------------------------------------------

    /**
     * Purges and synchronizes matrix rows matching active creator core lists.
     */
    public void updateTable() {
        _tableModel.setRowCount(0);
        for (Author authorRecord : _businessLogic.getAuthors()) {
            _tableModel.addRow(new Object[]{
                authorRecord.getFirstName(), 
                authorRecord.getLastName()
            });
        }
    }
}