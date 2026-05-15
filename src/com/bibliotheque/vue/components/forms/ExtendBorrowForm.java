package com.bibliotheque.vue.components.forms;

import com.bibliotheque.model.Bibliotheque;
import com.bibliotheque.model.Borrow;
import com.bibliotheque.model.Librarian;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.border.EmptyBorder;

/**
 * Modal dialogue registration wizard extending active media allocation lease durations.
 * 
 * <p>Validates temporal threshold transformations against historical parameters and processes 
 * authorization overrides signed off by designated system {@link Librarian} profiles.</p>
 * 
 * @see Borrow
 * @see Librarian
 * 
 * @version 1.1
 */
public class ExtendBorrowForm extends JDialog {

    private final JSpinner                    _targetLeaseDatePicker;
    private final JComboBox<LibrarianWrapper> _authorizingStaffCombo;
    private final Borrow                      _activeAllocationRecord;
    private boolean                           _isExtensionConfirmed = false;

    /**
     * Contextual nested decoupling view wrapper optimizing visual string 
     * representation mappings for core entities inside collection combo menus.
     */
    private static class LibrarianWrapper {
        private final Librarian _librarianEntity;
        
        public LibrarianWrapper(Librarian librarian) {
            this._librarianEntity = librarian;
        }
        
        public Librarian getLibrarian() {
            return _librarianEntity;
        }
        
        @Override
        public String toString() {
            return _librarianEntity.getFirstName() + " " + _librarianEntity.getLastName();
        }
    }

    /**
     * Constructs an application-modal allocation lease postponement wizard window.
     * 
     * @param owner  the top-level window frame layer managing display hierarchy focus
     * @param borrow the targeted ongoing active allocation record context
     * @param logic  the system backend central operations facade instance
     */
    public ExtendBorrowForm(Window owner, Borrow borrow, Bibliotheque logic) {
        super(owner, "Prolongation de l'emprunt", ModalityType.APPLICATION_MODAL);
        this._activeAllocationRecord = borrow;

        this.getContentPane().setLayout(new BorderLayout());

        // --- SECTION CENTRALE : LAYOUT PARAMETERS COMPOSITE ---
        JPanel fieldFormContainer = new JPanel(new GridBagLayout());
        fieldFormContainer.setBackground(Color.WHITE);
        fieldFormContainer.setBorder(new EmptyBorder(15, 15, 15, 15));

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(8, 10, 8, 10);
        constraints.fill = GridBagConstraints.HORIZONTAL;

        // Visual header showcasing current targeted transaction target
        JLabel transactionTargetLabel = new JLabel("Prolonger : " + _activeAllocationRecord.getCopy().getReference().getTitle());
        transactionTargetLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        constraints.gridx = 0; 
        constraints.gridy = 0; 
        constraints.gridwidth = 2;
        fieldFormContainer.add(transactionTargetLabel, constraints);

        // Sub-text tracking absolute deadline metrics currently applied
        SimpleDateFormat calendarFormatter = new SimpleDateFormat("dd/MM/yyyy");
        String currentDeadlineStr = _activeAllocationRecord.getExpectedDate() != null 
                ? calendarFormatter.format(_activeAllocationRecord.getExpectedDate()) 
                : "Inconnue";
        
        JLabel technicalStatusLabel = new JLabel("Échéance actuelle : " + currentDeadlineStr);
        technicalStatusLabel.setFont(new Font("SansSerif", Font.ITALIC, 12));
        technicalStatusLabel.setForeground(Color.DARK_GRAY);
        constraints.gridy = 1;
        fieldFormContainer.add(technicalStatusLabel, constraints);

        // Temporal extension input picker configurations
        constraints.gridwidth = 1;
        constraints.gridx = 0; 
        constraints.gridy = 2; 
        constraints.anchor = GridBagConstraints.LINE_END;
        fieldFormContainer.add(new JLabel("Nouvelle date d'échéance :"), constraints);

        // Derive safe dynamic default boundaries (current expiration + 14 business days)
        Calendar offsetCalendar = Calendar.getInstance();
        if (_activeAllocationRecord.getExpectedDate() != null) {
            offsetCalendar.setTime(_activeAllocationRecord.getExpectedDate());
        }
        offsetCalendar.add(Calendar.DAY_OF_MONTH, 14);
        Date defaultSuggestedDate = offsetCalendar.getTime();

        Date absoluteMinimumBound = _activeAllocationRecord.getExpectedDate() != null 
                ? _activeAllocationRecord.getExpectedDate() 
                : new Date();

        SpinnerDateModel modelStateConstraints = new SpinnerDateModel(
                defaultSuggestedDate, 
                absoluteMinimumBound, 
                null, 
                Calendar.DAY_OF_MONTH
        );
        _targetLeaseDatePicker = new JSpinner(modelStateConstraints);
        JSpinner.DateEditor textSegmentEditor = new JSpinner.DateEditor(_targetLeaseDatePicker, "dd/MM/yyyy");
        _targetLeaseDatePicker.setEditor(textSegmentEditor);

        constraints.gridx = 1; 
        constraints.anchor = GridBagConstraints.LINE_START;
        fieldFormContainer.add(_targetLeaseDatePicker, constraints);

        // Configuration field linking responsible management supervisor
        constraints.gridx = 0; 
        constraints.gridy = 3; 
        constraints.anchor = GridBagConstraints.LINE_END;
        fieldFormContainer.add(new JLabel("Autorisé par (Bibliothécaire) :"), constraints);

        _authorizingStaffCombo = new JComboBox<>();
        for (Librarian administratorProfile : logic.getLibrarians()) {
            _authorizingStaffCombo.addItem(new LibrarianWrapper(administratorProfile));
        }

        constraints.gridx = 1; 
        constraints.anchor = GridBagConstraints.LINE_START;
        fieldFormContainer.add(_authorizingStaffCombo, constraints);

        this.add(fieldFormContainer, BorderLayout.CENTER);

        // --- SECTION BAS : PERSISTENCE CONTROL FOOTER ---
        JPanel workflowControlsBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        workflowControlsBar.setBackground(new Color(245, 245, 245));
        workflowControlsBar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));

        JButton dismissWizardBtn = new JButton("Annuler");
        dismissWizardBtn.setFocusPainted(false);
        dismissWizardBtn.addActionListener(e -> this.dispose());

        JButton commitExtensionBtn = new JButton("Accorder l'extension");
        commitExtensionBtn.setBackground(new Color(0, 123, 255));
        commitExtensionBtn.setForeground(Color.WHITE);
        commitExtensionBtn.setFocusPainted(false);
        commitExtensionBtn.addActionListener(e -> this.processLeaseExtensionTransaction());

        workflowControlsBar.add(dismissWizardBtn);
        workflowControlsBar.add(commitExtensionBtn);
        this.add(workflowControlsBar, BorderLayout.SOUTH);

        // Layout constraints resolution packing
        this.pack();
        this.setResizable(false);
        this.setLocationRelativeTo(owner);
    }

    private void processLeaseExtensionTransaction() {
        LibrarianWrapper staffWrapper = (LibrarianWrapper) _authorizingStaffCombo.getSelectedItem();
        Date targetedExtensionDate = (Date) _targetLeaseDatePicker.getValue();

        if (staffWrapper == null) {
            JOptionPane.showMessageDialog(this, 
                    "Veuillez désigner le membre du personnel encadrant l'autorisation.", 
                    "Saisie requise", 
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Integrity checking ensuring new parameters step strictly forward in time
        if (_activeAllocationRecord.getExpectedDate() != null && !targetedExtensionDate.after(_activeAllocationRecord.getExpectedDate())) {
            JOptionPane.showMessageDialog(this, 
                    "La nouvelle échéance doit être positionnée chronologiquement après la date actuelle.", 
                    "Incohérence temporelle", 
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Librarian executingStaffMember = staffWrapper.getLibrarian();
            
            // Commit structural adjustments onto backend state tracking entities
            _activeAllocationRecord.extendsDate(targetedExtensionDate, executingStaffMember);
            
            _isExtensionConfirmed = true;
            this.dispose();
            
        } catch (Exception persistenceException) {
            JOptionPane.showMessageDialog(this, 
                    "Erreur lors de la modification des paramètres contractuels :\n" + persistenceException.getMessage(), 
                    "Échec d'infrastructure", 
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Checks if the lease extension was processed and successfully saved.
     * 
     * @return true if dates updated successfully on core data objects
     */
    public boolean isConfirmed() {
        return this._isExtensionConfirmed;
    }
}