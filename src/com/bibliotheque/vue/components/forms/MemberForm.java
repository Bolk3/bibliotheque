package com.bibliotheque.vue.components.forms;

import com.bibliotheque.model.Bibliotheque;
import com.bibliotheque.model.Member;
import com.bibliotheque.vue.MainFrame;
import com.bibliotheque.vue.components.AccessButton;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Data entry form viewport panel handling new user registration sequences.
 * 
 * <p>Captures subscriber contact details, validates inputs against structural constraints,
 * and provisions the newly constructed {@link Member} record directly down into the persistent data subsystem.</p>
 * 
 * @see Member
 * @see MainFrame
 * @see Bibliotheque
 * 
 * @version 1.1
 */
public class MemberForm extends JPanel {

    private final Bibliotheque _businessLogic;
    private final MainFrame    _viewController;

    private final JTextField   _lastNameField       = new JTextField(20);
    private final JTextField   _firstNameField      = new JTextField(20);
    private final JTextField   _emailAddressField   = new JTextField(20);

    /**
     * Constructs a stylized, structural layout member registration viewport form.
     * 
     * @param logic   the operational central library backend facade instance
     * @param handler the principal structural navigation view framework context
     */
    public MemberForm(Bibliotheque logic, MainFrame handler) {
        this._businessLogic = logic;
        this._viewController = handler;

        this.setLayout(new GridBagLayout());
        this.setBackground(Color.WHITE);
        this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints layoutConstraints = new GridBagConstraints();
        layoutConstraints.insets = new Insets(10, 10, 10, 10);
        layoutConstraints.fill = GridBagConstraints.HORIZONTAL;

        // --- TITLE FORM HEADER ---
        JLabel formTitleLabel = new JLabel("Inscrire un nouveau membre");
        formTitleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        formTitleLabel.setForeground(new Color(33, 33, 33));
        
        layoutConstraints.gridx = 0; 
        layoutConstraints.gridy = 0; 
        layoutConstraints.gridwidth = 2;
        layoutConstraints.insets = new Insets(10, 10, 20, 10);
        this.add(formTitleLabel, layoutConstraints);

        // Reset spacing parameters for input components rows matrix
        layoutConstraints.gridwidth = 1;
        layoutConstraints.insets = new Insets(8, 10, 8, 10);

        // --- ROW 1: LAST NAME INPUT MAPPING ---
        layoutConstraints.gridx = 0; 
        layoutConstraints.gridy = 1; 
        layoutConstraints.anchor = GridBagConstraints.LINE_END;
        this.add(new JLabel("Nom :"), layoutConstraints);

        layoutConstraints.gridx = 1; 
        layoutConstraints.anchor = GridBagConstraints.LINE_START;
        this.add(_lastNameField, layoutConstraints);

        // --- ROW 2: FIRST NAME INPUT MAPPING ---
        layoutConstraints.gridx = 0; 
        layoutConstraints.gridy = 2; 
        layoutConstraints.anchor = GridBagConstraints.LINE_END;
        this.add(new JLabel("Prénom :"), layoutConstraints);

        layoutConstraints.gridx = 1; 
        layoutConstraints.anchor = GridBagConstraints.LINE_START;
        this.add(_firstNameField, layoutConstraints);

        // --- ROW 3: EMAIL ADDRESS INPUT MAPPING ---
        layoutConstraints.gridx = 0; 
        layoutConstraints.gridy = 3; 
        layoutConstraints.anchor = GridBagConstraints.LINE_END;
        this.add(new JLabel("Email :"), layoutConstraints);

        layoutConstraints.gridx = 1; 
        layoutConstraints.anchor = GridBagConstraints.LINE_START;
        this.add(_emailAddressField, layoutConstraints);

        // --- ROW 4: SUBMIT TRANSACTION CONTROL BUTTON ---
        AccessButton submitRegistrationBtn = new AccessButton("Enregistrer", this::executeMemberRegistration);
        
        layoutConstraints.gridx = 0; 
        layoutConstraints.gridy = 4; 
        layoutConstraints.gridwidth = 2;
        layoutConstraints.insets = new Insets(20, 10, 10, 10);
        this.add(submitRegistrationBtn, layoutConstraints);
    }

    private void executeMemberRegistration() {
        String inputLastName  = _lastNameField.getText().trim();
        String inputFirstName = _firstNameField.getText().trim();
        String inputEmail     = _emailAddressField.getText().trim();

        if (inputLastName.isEmpty() || inputFirstName.isEmpty() || inputEmail.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                    "Veuillez renseigner tous les champs obligatoires du formulaire.", 
                    "Saisie incomplète", 
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Member newlyCreatedMember = new Member(inputFirstName, inputLastName, inputEmail, _businessLogic);
            _businessLogic.addMember(newlyCreatedMember);

            JOptionPane.showMessageDialog(this, 
                    "Le nouveau membre a été inscrit avec succès !", 
                    "Inscription validée", 
                    JOptionPane.INFORMATION_MESSAGE);
            
            _viewController.goBack();

        } catch (Exception persistenceException) {
            JOptionPane.showMessageDialog(this, 
                    "Une erreur est survenue lors de l'enregistrement de la fiche :\n" + persistenceException.getMessage(), 
                    "Erreur d'infrastructure", 
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}