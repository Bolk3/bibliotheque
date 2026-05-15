package com.bibliotheque.vue.core;

import com.bibliotheque.model.Bibliotheque;
import com.bibliotheque.model.Librarian;
import com.bibliotheque.vue.MainFrame;
import com.bibliotheque.vue.components.AccessButton;
import com.bibliotheque.vue.components.forms.LibrarianForm;
import com.bibliotheque.vue.librarian.LibrarianActionHistory;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

/**
 * Visual administrative roster management panel tracking internal organization personnel.
 * 
 * <p>Provides view segments for monitoring operational clearance profiles, pulling granular execution 
 * performance trails via {@link LibrarianActionHistory}, and provisioning new administrative 
 * credentials into the data layer storage system.</p>
 * 
 * @see Librarian
 * @see MainFrame
 * @see Bibliotheque
 * 
 * @version 1.1
 */
public class LibrarianView extends JPanel {

    private final Bibliotheque    _businessLogic;
    private final MainFrame       _viewController;
    
    private final JTable          _tableLibrarians;
    private final DefaultTableModel _tableModel;

    /**
     * Constructs a unified staff roster management interface panel.
     * 
     * @param logic   the system backend layer manager facade
     * @param handler the application view viewport frame router context
     */
    public LibrarianView(Bibliotheque logic, MainFrame handler) {
        this._businessLogic = logic;
        this._viewController = handler;
        
        this.setLayout(new BorderLayout(10, 10));
        this.setBackground(Color.WHITE);
        this.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // --- SECTION HAUT : VIEW BANNER HEADLINE ---
        JLabel titleLabel = new JLabel("Gestion des Bibliothécaires", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        this.add(titleLabel, BorderLayout.NORTH);

        // --- SECTION CENTRALE : TABULAR PERSONNEL ROSTER ---
        String[] columns = {"Nom", "Prénom", "Email", "Poste", "Niveau Permission"};
        _tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { 
                return false; 
            }
        };
        
        _tableLibrarians = new JTable(_tableModel);
        _tableLibrarians.setRowHeight(25);
        _tableLibrarians.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        this.add(new JScrollPane(_tableLibrarians), BorderLayout.CENTER);

        // --- SECTION BAS : DISPATCH ACTION PANEL BAR ---
        JPanel actionControlBar = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        actionControlBar.setBackground(new Color(240, 240, 240));
        actionControlBar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));
        
        AccessButton registerStaffButton = new AccessButton("Nouveau Bibliothécaire", () -> 
                _viewController.goForward(new LibrarianForm(_businessLogic, _viewController)));

        AccessButton auditActionsButton = new AccessButton("Historique Actions", this::handleAuditActionsAction);

        actionControlBar.add(registerStaffButton);
        actionControlBar.add(auditActionsButton);
        this.add(actionControlBar, BorderLayout.SOUTH);

        this.updateTable();
    }

    // -------------------------------------------------------------------------
    // Execution Handlers
    // -------------------------------------------------------------------------

    private void handleAuditActionsAction() {
        int selectedRow = _tableLibrarians.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un bibliothécaire.");
            return;
        }

        Librarian chosenStaff = this.extractLibrarianAt(selectedRow);
        _viewController.goForward(new LibrarianActionHistory(chosenStaff, _businessLogic, _viewController));
    }

    private Librarian extractLibrarianAt(int index) {
        return new ArrayList<>(_businessLogic.getLibrarians()).get(index);
    }

    // -------------------------------------------------------------------------
    // Synchronization Operations
    // -------------------------------------------------------------------------

    /**
     * Purges and synchronizes matrix rows matching active staff structural data lists.
     */
    public void updateTable() {
        _tableModel.setRowCount(0);
        for (Librarian staffRecord : _businessLogic.getLibrarians()) {
            _tableModel.addRow(new Object[]{
                staffRecord.getLastName(),
                staffRecord.getFirstName(),
                staffRecord.getEmail(),
                staffRecord.getPosition(),
                "Niveau " + staffRecord.getPermission()
            });
        }
    }
}