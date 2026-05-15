package com.bibliotheque.vue.components.forms;

import com.bibliotheque.model.Bibliotheque;
import com.bibliotheque.model.Copy;
import com.bibliotheque.model.State;
import com.bibliotheque.model.Work;
import com.bibliotheque.vue.controllers.AccessButtonCTR;
import com.bibliotheque.vue.work.WorkInfoPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * Administrative configuration dialog workspace panel for registering new physical {@link Copy} units.
 * 
 * <p>Safely handles the assignment of a structural {@link State} enumerator context to a target {@link Work},
 * updates the application inventory registry maps, and triggers data grid synchronizations downstream 
 * on the parent tracking {@link WorkInfoPanel}.</p>
 * 
 * @see Work
 * @see Copy
 * @see State
 * @see WorkInfoPanel
 * 
 * @version 1.1
 */
public class AddCopyForm extends JDialog {

    private final Bibliotheque    _businessLogic;
    private final Work            _activeWorkContext;
    private final WorkInfoPanel   _parentPresentationPanel;

    private final JComboBox<State> _stateSelectionCombo = new JComboBox<>(State.values());
    private final JButton          _confirmButton       = new JButton("Ajouter à l'inventaire");
    private final JButton          _cancelButton        = new JButton("Annuler");

    /**
     * Constructs an application-modal input dialog block to append localized item inventory records.
     * 
     * @param logic       the central tracking repository layer facade instance
     * @param work        the parent operational catalog item tracking instance context
     * @param parentPanel the presentation panel dashboard capturing refresh view updates
     */
    public AddCopyForm(Bibliotheque logic, Work work, WorkInfoPanel parentPanel) {
        super(JOptionPane.getFrameForComponent(parentPanel), "Nouvelle copie : " + work.getTitle(), ModalityType.APPLICATION_MODAL);
        
        this._businessLogic = logic;
        this._activeWorkContext = work;
        this._parentPresentationPanel = parentPanel;

        this.setSize(420, 240);
        this.setResizable(false);
        this.setLocationRelativeTo(parentPanel);
        this.getContentPane().setLayout(new BorderLayout());

        // --- SECTION HAUT : BANNER HEADER TITLE ---
        JLabel formHeaderLabel = new JLabel("Nouvel Exemplaire", SwingConstants.CENTER);
        formHeaderLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        formHeaderLabel.setForeground(new Color(33, 33, 33));
        formHeaderLabel.setBorder(BorderFactory.createEmptyBorder(15, 10, 5, 10));
        this.add(formHeaderLabel, BorderLayout.NORTH);

        // --- SECTION CENTRALE : ATTRIBUTE SETTINGS MATRIX ---
        JPanel inputGridPanel = new JPanel(new GridBagLayout());
        inputGridPanel.setBackground(Color.WHITE);
        inputGridPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        GridBagConstraints layoutConstraints = new GridBagConstraints();
        layoutConstraints.insets = new Insets(6, 8, 6, 8);
        layoutConstraints.fill = GridBagConstraints.HORIZONTAL;

        // Grid Row 1: Active Target Structural Metadata Label
        layoutConstraints.gridx = 0;
        layoutConstraints.gridy = 0;
        layoutConstraints.anchor = GridBagConstraints.LINE_END;
        inputGridPanel.add(new JLabel("Œuvre cible :"), layoutConstraints);

        layoutConstraints.gridx = 1;
        layoutConstraints.weightx = 1.0;
        layoutConstraints.anchor = GridBagConstraints.LINE_START;
        JLabel workTitleDisplayLabel = new JLabel(work.getTitle());
        workTitleDisplayLabel.setFont(new Font("SansSerif", Font.ITALIC | Font.BOLD, 13));
        inputGridPanel.add(workTitleDisplayLabel, layoutConstraints);

        // Grid Row 2: Condition Enumeration ComboBox Target
        layoutConstraints.gridx = 0;
        layoutConstraints.gridy = 1;
        layoutConstraints.weightx = 0.0;
        layoutConstraints.anchor = GridBagConstraints.LINE_END;
        inputGridPanel.add(new JLabel("État de la copie :"), layoutConstraints);

        layoutConstraints.gridx = 1;
        layoutConstraints.weightx = 1.0;
        layoutConstraints.anchor = GridBagConstraints.LINE_START;
        inputGridPanel.add(_stateSelectionCombo, layoutConstraints);

        this.add(inputGridPanel, BorderLayout.CENTER);

        // --- SECTION BAS : CONTROL FOOTER ACTION DECK ---
        JPanel interactionDeckPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        interactionDeckPanel.setBackground(new Color(245, 245, 245));
        interactionDeckPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));

        _confirmButton.setBackground(new Color(40, 167, 69)); // Custom execution context safe green
        _confirmButton.setForeground(Color.WHITE);
        _confirmButton.setFocusPainted(false);
        _confirmButton.addActionListener(new AccessButtonCTR(this::executeInventoryAdditionPipeline));

        _cancelButton.setFocusPainted(false);
        _cancelButton.addActionListener(new AccessButtonCTR(this::dispose));

        interactionDeckPanel.add(_confirmButton);
        interactionDeckPanel.add(_cancelButton);
        this.add(interactionDeckPanel, BorderLayout.SOUTH);
    }

    private void executeInventoryAdditionPipeline() {
        State selectedStateContext = (State) _stateSelectionCombo.getSelectedItem();

        if (selectedStateContext == null) {
            JOptionPane.showMessageDialog(this, 
                    "Veuillez renseigner un état valide pour l'exemplaire.", 
                    "Champ requis", 
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Instantiate copy unit mapping target properties data matrices
            Copy engineeredCopyRecord = new Copy(selectedStateContext, _activeWorkContext);
            _activeWorkContext.addCopy(engineeredCopyRecord);

            // Sync alterations down across active tracking panels
            _parentPresentationPanel.updateCopiesTable();

            JOptionPane.showMessageDialog(_parentPresentationPanel, 
                    "Le nouvel exemplaire a été injecté avec succès au sein de l'inventaire !", 
                    "Unité enregistrée", 
                    JOptionPane.INFORMATION_MESSAGE);

            this.dispose();

        } catch (Exception executionException) {
            JOptionPane.showMessageDialog(this, 
                    "Une erreur d'infrastructure est survenue lors du référencement de la copie :\n" + executionException.getMessage(), 
                    "Échec d'enregistrement", 
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}