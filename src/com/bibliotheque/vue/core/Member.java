package com.bibliotheque.vue.core;

import com.bibliotheque.model.Bibliotheque;
import com.bibliotheque.vue.MainFrame;
import com.bibliotheque.vue.components.AccessButton;
import com.bibliotheque.vue.components.forms.MemberForm;
import com.bibliotheque.vue.member.MemberBorrowHistory;
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
 * Visual management control center dashboard handling library membership metrics.
 * 
 * <p>Renders a comprehensive user database roster that allows system operator controls 
 * to audit personalized history accounts, assign access status suspensions (bans), 
 * or register new member profiles into the business tier collections.</p>
 * 
 * @see com.bibliotheque.model.Member
 * @see MainFrame
 * @see Bibliotheque
 * 
 * @version 1.1
 */
public class Member extends JPanel {

    private final Bibliotheque    _businessLogic;
    private final MainFrame       _viewController;
    
    private final JTable          _tableMembers;
    private final DefaultTableModel _tableModel;

    /**
     * Constructs a unified user registration dashboard control segment panel.
     * 
     * @param logic   the system backend layer manager facade
     * @param handler the application view viewport frame router context
     */
    public Member(Bibliotheque logic, MainFrame handler) {
        this._businessLogic = logic;
        this._viewController = handler;
        
        this.setLayout(new BorderLayout(10, 10));
        this.setBackground(Color.WHITE);
        this.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // --- SECTION HAUT : VIEW BANNER HEADLINE ---
        JLabel titleLabel = new JLabel("Gestion des Membres", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        this.add(titleLabel, BorderLayout.NORTH);

        // --- SECTION CENTRALE : TABULAR MEMBERSHIP LIST ROSTER ---
        String[] columns = {"Nom", "Prénom", "Email", "Pénalités", "Statut"};
        _tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { 
                return false; 
            }
        };
        
        _tableMembers = new JTable(_tableModel);
        _tableMembers.setRowHeight(25);
        _tableMembers.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        this.add(new JScrollPane(_tableMembers), BorderLayout.CENTER);

        // --- SECTION BAS : DISPATCH ACTION PANEL BAR ---
        JPanel actionControlBar = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        actionControlBar.setBackground(new Color(240, 240, 240));
        actionControlBar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));
        
        AccessButton registerMemberButton = new AccessButton("Nouveau Membre", () -> 
                _viewController.goForward(new MemberForm(_businessLogic, _viewController)));

        AccessButton auditHistoryButton = new AccessButton("Historique", this::handleAuditHistoryAction);
        AccessButton moderateStatusButton = new AccessButton("Bannir/Gracier", this::handleModerateStatusAction);

        actionControlBar.add(registerMemberButton);
        actionControlBar.add(auditHistoryButton);
        actionControlBar.add(moderateStatusButton);
        this.add(actionControlBar, BorderLayout.SOUTH);

        this.updateTable();
    }

    // -------------------------------------------------------------------------
    // Execution Handlers
    // -------------------------------------------------------------------------

    private void handleAuditHistoryAction() {
        int selectedRow = _tableMembers.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un membre dans le tableau.");
            return;
        }

        com.bibliotheque.model.Member chosenMember = this.extractMemberAt(selectedRow);
        _viewController.goForward(new MemberBorrowHistory(chosenMember, _businessLogic, _viewController));
    }

    private void handleModerateStatusAction() {
        int selectedRow = _tableMembers.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un membre dans le tableau.");
            return;
        }

        com.bibliotheque.model.Member targetedMember = this.extractMemberAt(selectedRow);
        try {
            // Apply inverted status validation checks verified via the active administrative operator credentials
            targetedMember.setBlocked(!targetedMember.isBlocked(), _viewController.currentUser); 
            this.updateTable();
        } catch (IllegalStateException exception) {
            JOptionPane.showMessageDialog(this, 
                    exception.getMessage(), 
                    "Erreur de privilèges", 
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private com.bibliotheque.model.Member extractMemberAt(int index) {
        return new ArrayList<>(_businessLogic.getMembers()).get(index);
    }

    // -------------------------------------------------------------------------
    // Synchronization Operations
    // -------------------------------------------------------------------------

    /**
     * Purges and synchronizes matrix rows matching active system model lists.
     */
    public void updateTable() {
        _tableModel.setRowCount(0);
        for (com.bibliotheque.model.Member memberRecord : _businessLogic.getMembers()) {
            String operationalStatusText = memberRecord.isBlocked() ? "BANNI" : "ACTIF";
            
            _tableModel.addRow(new Object[]{
                memberRecord.getLastName(),
                memberRecord.getFirstName(),
                memberRecord.getEmail(),
                String.format("%.2f €", memberRecord.getPenalty()),
                operationalStatusText
            });
        }
    }
}