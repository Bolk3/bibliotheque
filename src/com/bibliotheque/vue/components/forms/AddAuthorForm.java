package com.bibliotheque.vue.components.forms;

import com.bibliotheque.model.Author;
import com.bibliotheque.model.Bibliotheque;
import com.bibliotheque.vue.MainFrame;
import com.bibliotheque.vue.components.AccessButton;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

/**
 * Editorial management workspace panel used to register new {@link Author} metadata records into the infrastructure system.
 * 
 * <p>Enforces input validation constraints across isolated text fields, maps entries back into the centralized 
 * relational registry engine, and signals layout stack alterations up to the master structure frame container.</p>
 * 
 * @see Author
 * @see MainFrame
 * 
 * @version 1.1
 */
public class AddAuthorForm extends JPanel {

    private final Bibliotheque _businessLogic;
    private final MainFrame    _viewController;

    private final JTextField   _firstNameField = new JTextField(20);
    private final JTextField   _lastNameField  = new JTextField(20);

    /**
     * Constructs an interactive management form workspace targeted at creating unified author metadata definitions.
     * 
     * @param logic   the central tracking repository layer facade instance
     * @param handler the primary frame structure view manager coordinator
     */
    public AddAuthorForm(Bibliotheque logic, MainFrame handler) {
        this._businessLogic = logic;
        this._viewController = handler;

        this.setLayout(new GridBagLayout());
        this.setBackground(Color.WHITE);
        this.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints layoutConstraints = new GridBagConstraints();
        layoutConstraints.insets = new Insets(10, 10, 10, 10);
        layoutConstraints.fill = GridBagConstraints.HORIZONTAL;

        // --- SECTION BANNER HEADER ---
        JLabel formViewTitle = new JLabel("Ajouter un nouvel auteur");
        formViewTitle.setFont(new Font("SansSerif", Font.BOLD, 20));
        formViewTitle.setForeground(new Color(33, 33, 33));
        
        layoutConstraints.gridx = 0; 
        layoutConstraints.gridy = 0; 
        layoutConstraints.gridwidth = 2;
        layoutConstraints.anchor = GridBagConstraints.CENTER;
        this.add(formViewTitle, layoutConstraints);

        // --- GRID ROW 1: FIRST NAME METADATA FIELD ---
        layoutConstraints.gridwidth = 1;
        layoutConstraints.anchor = GridBagConstraints.LINE_END;

        layoutConstraints.gridx = 0; 
        layoutConstraints.gridy = 1; 
        this.add(new JLabel("Prénom :"), layoutConstraints);
        
        layoutConstraints.gridx = 1; 
        layoutConstraints.anchor = GridBagConstraints.LINE_START;
        this.add(_firstNameField, layoutConstraints);

        // --- GRID ROW 2: LAST NAME METADATA FIELD ---
        layoutConstraints.gridx = 0; 
        layoutConstraints.gridy = 2; 
        layoutConstraints.anchor = GridBagConstraints.LINE_END;
        this.add(new JLabel("Nom :"), layoutConstraints);
        
        layoutConstraints.gridx = 1; 
        layoutConstraints.anchor = GridBagConstraints.LINE_START;
        this.add(_lastNameField, layoutConstraints);

        // --- GRID ROW 3: WORKFLOW CONTROL ACTION DECK ---
        JPanel interactionButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        interactionButtonPanel.setBackground(Color.WHITE);

        AccessButton executionCancelBtn = new AccessButton("Annuler", _viewController::goBack);
        AccessButton executionConfirmBtn = new AccessButton("Enregistrer", this::executeAuthorAdditionPipeline);

        interactionButtonPanel.add(executionCancelBtn);
        interactionButtonPanel.add(executionConfirmBtn);

        layoutConstraints.gridx = 0; 
        layoutConstraints.gridy = 3; 
        layoutConstraints.gridwidth = 2;
        layoutConstraints.insets = new Insets(30, 10, 10, 10);
        layoutConstraints.anchor = GridBagConstraints.CENTER;
        this.add(interactionButtonPanel, layoutConstraints);
    }

    private void executeAuthorAdditionPipeline() {
        String inputsFirstName = _firstNameField.getText().trim();
        String inputsLastName  = _lastNameField.getText().trim();

        if (inputsFirstName.isEmpty() || inputsLastName.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                    "Veuillez renseigner l'intégralité des champs obligatoires avant de valider.", 
                    "Champs requis", 
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Push structured metadata records out into infrastructure mapping schemas
            _businessLogic.addAuthor(new Author(inputsFirstName, inputsLastName));
            
            JOptionPane.showMessageDialog(this, 
                    "L'auteur a été ajouté avec succès au catalogue !", 
                    "Enregistrement réussi", 
                    JOptionPane.INFORMATION_MESSAGE);
            
            _viewController.goBack(); 
            
        } catch (Exception executionException) {
            JOptionPane.showMessageDialog(this, 
                    "Une erreur d'infrastructure est survenue lors de l'archivage de la fiche d'auteur :\n" + executionException.getMessage(), 
                    "Erreur de création", 
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}