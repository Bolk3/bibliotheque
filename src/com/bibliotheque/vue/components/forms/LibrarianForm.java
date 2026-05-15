package com.bibliotheque.vue.components.forms;

import com.bibliotheque.model.Bibliotheque;
import com.bibliotheque.model.Librarian;
import com.bibliotheque.vue.MainFrame;
import com.bibliotheque.vue.core.LibrarianView;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

/**
 * Administrative configuration pane capturing newly onboarded system staff operators.
 * 
 * <p>Validates domain authorization credentials, provisions explicit security clearance levels 
 * via spinner increments, and requests synchronization pipelines across refreshed {@link LibrarianView} registries.</p>
 * 
 * @see Librarian
 * @see MainFrame
 * @see Bibliotheque
 * 
 * @version 1.1
 */
public class LibrarianForm extends JPanel {

    private final Bibliotheque      _businessLogic;
    private final MainFrame         _viewController;

    private final JTextField        _firstNameField     = new JTextField(20);
    private final JTextField        _lastNameField      = new JTextField(20);
    private final JTextField        _emailAddressField  = new JTextField(20);
    private final JTextField        _positionTitleField = new JTextField(20);
    private final JSpinner          _clearanceSpinner   = new JSpinner(new SpinnerNumberModel(1, 1, 5, 1));

    /**
     * Constructs a stylized management intake dashboard layout for indexing personnel assets.
     * 
     * @param logic   the operational central system backend facade layer context
     * @param handler the principal structural navigation frame window coordinator
     */
    public LibrarianForm(Bibliotheque logic, MainFrame handler) {
        this._businessLogic = logic;
        this._viewController = handler;
        
        this.setLayout(new BorderLayout(15, 15));
        this.setBackground(Color.WHITE);
        this.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // --- SECTION HAUT : HEADER CONTROL LABEL ---
        JLabel bannerHeaderTitle = new JLabel("Nouveau Bibliothécaire", SwingConstants.CENTER);
        bannerHeaderTitle.setFont(new Font("SansSerif", Font.BOLD, 20));
        bannerHeaderTitle.setForeground(new Color(33, 33, 33));
        this.add(bannerHeaderTitle, BorderLayout.NORTH);

        // --- SECTION CENTRALE : INPUT METADATA GRID GRID ---
        JPanel fieldGridContainer = new JPanel(new GridLayout(5, 2, 12, 12));
        fieldGridContainer.setBackground(Color.WHITE);
        fieldGridContainer.setBorder(BorderFactory.createEmptyBorder(15, 35, 15, 35));

        fieldGridContainer.add(new JLabel("Prénom :"));
        fieldGridContainer.add(_firstNameField);
        fieldGridContainer.add(new JLabel("Nom :"));
        fieldGridContainer.add(_lastNameField);
        fieldGridContainer.add(new JLabel("Email :"));
        fieldGridContainer.add(_emailAddressField);
        fieldGridContainer.add(new JLabel("Poste / Position :"));
        fieldGridContainer.add(_positionTitleField);
        fieldGridContainer.add(new JLabel("Niveau Permission (1 à 5) :"));
        fieldGridContainer.add(_clearanceSpinner);

        this.add(fieldGridContainer, BorderLayout.CENTER);

        // --- SECTION BAS : PERSISTENCE CONTROL FOOTER ---
        JPanel executionControlBar = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 10));
        executionControlBar.setBackground(new Color(245, 245, 245));
        executionControlBar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));

        JButton savePersonnelRecordBtn = new JButton("Enregistrer");
        savePersonnelRecordBtn.setBackground(new Color(0, 123, 255));
        savePersonnelRecordBtn.setForeground(Color.WHITE);
        savePersonnelRecordBtn.setFocusPainted(false);
        savePersonnelRecordBtn.addActionListener(e -> this.executeLibrarianOnboardingPipeline());

        JButton cancelTransactionBtn = new JButton("Annuler");
        cancelTransactionBtn.setFocusPainted(false);
        cancelTransactionBtn.addActionListener(e -> _viewController.goBack());
        
        executionControlBar.add(savePersonnelRecordBtn);
        executionControlBar.add(cancelTransactionBtn);
        this.add(executionControlBar, BorderLayout.SOUTH);
    }

    private void executeLibrarianOnboardingPipeline() {
        String inputFirstName = _firstNameField.getText().trim();
        String inputLastName  = _lastNameField.getText().trim();
        String inputEmail     = _emailAddressField.getText().trim();
        String inputPosition  = _positionTitleField.getText().trim();
        int explicitClearance = (int) _clearanceSpinner.getValue();

        if (inputFirstName.isEmpty() || inputLastName.isEmpty() || inputEmail.isEmpty() || inputPosition.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                    "Veuillez compléter l'ensemble des champs du formulaire administratif.", 
                    "Saisie incomplète", 
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Librarian onboardedStaffMember = new Librarian(
                    inputFirstName, 
                    inputLastName, 
                    inputEmail, 
                    _businessLogic, 
                    inputPosition, 
                    explicitClearance
            );
            
            _businessLogic.addLibrarian(onboardedStaffMember);

            JOptionPane.showMessageDialog(this, 
                    "L'agent de bibliothèque a été enregistré avec succès !", 
                    "Opération validée", 
                    JOptionPane.INFORMATION_MESSAGE);
            
            _viewController.goBack();
            
            if (_viewController.getCurrentPanel() instanceof LibrarianView) {
                ((LibrarianView) _viewController.getCurrentPanel()).updateTable();
            }
            
        } catch (IllegalArgumentException validationException) {
            JOptionPane.showMessageDialog(this, 
                    "Contraintes d'intégrité non respectées :\n" + validationException.getMessage(), 
                    "Erreur de validation", 
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}