package com.bibliotheque.vue.components.forms;

import com.bibliotheque.model.Bibliotheque;
import com.bibliotheque.model.Borrow;
import com.bibliotheque.model.Librarian;
import com.bibliotheque.model.State;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * Modal dialogue registration wizard capturing media copy returns.
 * 
 * <p>Validates item state transitions against core {@link State} enumerations and logs 
 * authorizing {@link Librarian} credentials into active transaction tables.</p>
 * 
 * @see Borrow
 * @see State
 * @see Librarian
 * 
 * @version 1.1
 */
public class ReturnBookForm extends JDialog {

    private final JComboBox<String>           _stateSelectionCombo;
    private final JComboBox<LibrarianWrapper> _librarianSelectionCombo;
    private final Borrow                      _activeBorrowRecord;
    private boolean                           _isTransactionConfirmed = false;

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
     * Constructs an application-modal transaction workflow wizard window.
     * 
     * @param owner  the top-level window frame layer managing display hierarchy focus
     * @param borrow the targeted ongoing active allocation record context
     * @param logic  the system backend central operations facade instance
     */
    public ReturnBookForm(Window owner, Borrow borrow, Bibliotheque logic) {
        super(owner, "Formulaire de retour d'ouvrage", ModalityType.APPLICATION_MODAL);
        this._activeBorrowRecord = borrow;
        
        this.getContentPane().setLayout(new BorderLayout());
        
        // --- SECTION CENTRALE : INPUT SCHEDULING FORM PANEL ---
        JPanel formContentPanel = new JPanel(new GridBagLayout());
        formContentPanel.setBackground(Color.WHITE);
        formContentPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        GridBagConstraints layoutConstraints = new GridBagConstraints();
        layoutConstraints.insets = new Insets(8, 10, 8, 10);
        layoutConstraints.fill = GridBagConstraints.HORIZONTAL;

        // Display targeted transaction media asset banner headers
        JLabel mediaTitleLabel = new JLabel("Œuvre : " + _activeBorrowRecord.getCopy().getReference().getTitle());
        mediaTitleLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        layoutConstraints.gridx = 0; 
        layoutConstraints.gridy = 0; 
        layoutConstraints.gridwidth = 2;
        formContentPanel.add(mediaTitleLabel, layoutConstraints);

        // Selection input dropdown mapping for item return conditions
        layoutConstraints.gridwidth = 1;
        layoutConstraints.gridx = 0; 
        layoutConstraints.gridy = 1; 
        layoutConstraints.anchor = GridBagConstraints.LINE_END;
        formContentPanel.add(new JLabel("État au retour :"), layoutConstraints);
        
        String[] stateEnumerations = {
            State.NEUF.toString(), 
            State.BON.toString(), 
            State.USE.toString(), 
            State.ABIME.toString(), 
            State.PERDU.toString()
        }; 
        
        _stateSelectionCombo = new JComboBox<>(stateEnumerations);
        _stateSelectionCombo.setSelectedItem(_activeBorrowRecord.getInitialState());
        layoutConstraints.gridx = 1; 
        layoutConstraints.anchor = GridBagConstraints.LINE_START;
        formContentPanel.add(_stateSelectionCombo, layoutConstraints);

        // Selection input dropdown mapping for transaction authorization
        layoutConstraints.gridx = 0; 
        layoutConstraints.gridy = 2; 
        layoutConstraints.anchor = GridBagConstraints.LINE_END;
        formContentPanel.add(new JLabel("Validé par (Bibliothécaire) :"), layoutConstraints);
        
        _librarianSelectionCombo = new JComboBox<>();
        for (Librarian librarianProfile : logic.getLibrarians()) {
            _librarianSelectionCombo.addItem(new LibrarianWrapper(librarianProfile));
        }
        
        layoutConstraints.gridx = 1; 
        layoutConstraints.anchor = GridBagConstraints.LINE_START;
        formContentPanel.add(_librarianSelectionCombo, layoutConstraints);

        this.add(formContentPanel, BorderLayout.CENTER);

        // --- SECTION BAS : EXECUTION ACTIONS CONTROL FOOTER ---
        JPanel actionControlsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        actionControlsPanel.setBackground(new Color(245, 245, 245));
        actionControlsPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));
        
        JButton dismissButton = new JButton("Annuler");
        dismissButton.addActionListener(e -> this.dispose());
        
        JButton commitReturnButton = new JButton("Valider le retour");
        commitReturnButton.setBackground(new Color(40, 167, 69));
        commitReturnButton.setForeground(Color.WHITE);
        commitReturnButton.setFocusPainted(false);
        commitReturnButton.addActionListener(e -> this.processBookReturnTransaction());

        actionControlsPanel.add(dismissButton);
        actionControlsPanel.add(commitReturnButton);
        this.add(actionControlsPanel, BorderLayout.SOUTH);

        // Component structural packing configuration overrides
        this.pack();
        this.setResizable(false);
        this.setLocationRelativeTo(owner);
    }

    private void processBookReturnTransaction() {
        LibrarianWrapper assignedWrapper = (LibrarianWrapper) _librarianSelectionCombo.getSelectedItem();
        String targetedReturnStateStr = (String) _stateSelectionCombo.getSelectedItem();
        
        if (assignedWrapper == null) {
            JOptionPane.showMessageDialog(this, 
                    "Veuillez sélectionner un membre du personnel pour valider l'opération.", 
                    "Saisie requise", 
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            Librarian executingStaffMember = assignedWrapper.getLibrarian();
            
            // Persist the transaction mutations down to the core data models
            _activeBorrowRecord.returnBook(targetedReturnStateStr, executingStaffMember);
            
            _isTransactionConfirmed = true;
            this.dispose();
            
        } catch (Exception processException) {
            JOptionPane.showMessageDialog(this, 
                    "Impossible de valider le retour de l'ouvrage :\n" + processException.getMessage(), 
                    "Erreur d'infrastructure", 
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Checks if the transaction pipeline completed and persisted updates successfully.
     * 
     * @return true if data mutations committed onto core tracking segments
     */
    public boolean isConfirmed() {
        return this._isTransactionConfirmed;
    }
}