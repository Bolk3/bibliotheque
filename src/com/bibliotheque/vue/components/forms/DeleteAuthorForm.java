package com.bibliotheque.vue.components.forms;

import com.bibliotheque.model.Author;
import com.bibliotheque.model.Bibliotheque;
import com.bibliotheque.vue.controllers.AccessButtonCTR;
import com.bibliotheque.vue.core.Authors;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * Administrative confirmation dialog panel used to remove an {@link Author} resource index.
 * 
 * <p>Prompts the operator for destructive deletion confirmation before calling down to the central 
 * synchronization engine and triggering a data table redraw across active {@link Authors} registries.</p>
 * 
 * @see Author
 * @see Bibliotheque
 * @see Authors
 * 
 * @version 1.1
 */
public class DeleteAuthorForm extends JDialog {

    private final Bibliotheque _businessLogic;
    private final Authors      _parentPresentationView;
    private final String       _targetFirstName;
    private final String       _targetLastName;

    private final JButton      _confirmButton = new JButton("Supprimer");
    private final JButton      _cancelButton  = new JButton("Annuler");

    /**
     * Constructs an application-modal warning prompt to safely execute structural asset deletions.
     * 
     * @param logic     the central backend system data operations layer context
     * @param parent    the primary management view container calling for index recalculations
     * @param firstName authorization search tracking element corresponding to target first name
     * @param lastName  authorization search tracking element corresponding to target last name
     */
    public DeleteAuthorForm(Bibliotheque logic, Authors parent, String firstName, String lastName) {
        super(JOptionPane.getFrameForComponent(parent), "Supprimer un auteur", ModalityType.APPLICATION_MODAL);
        
        this._businessLogic = logic;
        this._parentPresentationView = parent;
        this._targetFirstName = firstName;
        this._targetLastName = lastName;

        this.setSize(350, 160);
        this.setResizable(false);
        this.setLocationRelativeTo(parent);
        this.getContentPane().setLayout(new BorderLayout());

        // --- SECTION CENTRALE : WARNING NOTIFICATION LABEL ---
        JLabel warningPromptLabel = new JLabel(
                "<html><center>Voulez-vous vraiment supprimer l'auteur :<br><b>" 
                + _targetFirstName + " " + _targetLastName + "</b> ?</center></html>", 
                SwingConstants.CENTER
        );
        warningPromptLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        warningPromptLabel.setForeground(new Color(33, 33, 33));
        warningPromptLabel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        this.add(warningPromptLabel, BorderLayout.CENTER);

        // --- SECTION BAS : PERSISTENCE CONTROL FOOTER ---
        JPanel interactionDeckPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        interactionDeckPanel.setBackground(new Color(245, 245, 245));
        interactionDeckPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));

        _confirmButton.setBackground(new Color(220, 53, 69)); // Custom contextual hazard red
        _confirmButton.setForeground(Color.WHITE);
        _confirmButton.setFocusPainted(false);
        _confirmButton.addActionListener(new AccessButtonCTR(this::executeAuthorDeletionPipeline));

        _cancelButton.setFocusPainted(false);
        _cancelButton.addActionListener(new AccessButtonCTR(this::dispose));

        interactionDeckPanel.add(_confirmButton);
        interactionDeckPanel.add(_cancelButton);
        this.add(interactionDeckPanel, BorderLayout.SOUTH);
    }

    private void executeAuthorDeletionPipeline() {
        try {
            // Remove matching core tracking reference points from backend registry mapping
            _businessLogic.removeAuthor(_targetFirstName, _targetLastName);
            
            // Refresh visual view registries mapping current relational status codes
            _parentPresentationView.updateTable();
            this.dispose();
            
        } catch (Exception executionException) {
            JOptionPane.showMessageDialog(this, 
                    "Impossible de supprimer cet auteur. Il est probablement rattaché à une œuvre active :\n" 
                    + executionException.getMessage(), 
                    "Violation d'intégrité", 
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}