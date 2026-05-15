package com.bibliotheque.vue.core;

import com.bibliotheque.model.Librarian;
import com.bibliotheque.model.Settings;
import com.bibliotheque.vue.MainFrame;
import com.bibliotheque.vue.components.AccessButton;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

/**
 * Visual configuration dashboard managing system clearance constraints and execution access thresholds.
 * 
 * <p>Restricts visibility reactively if the active operator's credential level fails security checks.
 * Authorized changes override system-wide parameter bounds on confirmation.</p>
 * 
 * @see Settings
 * @see Librarian
 * @see MainFrame
 * 
 * @version 1.1
 */
public class SettingsView extends JPanel {

    private final MainFrame    _viewController;
    private final Librarian    _currentUser;

    private JSpinner           _validateBorrowSpinner;
    private JSpinner           _extendLoanSpinner;
    private JSpinner           _processReturnSpinner;
    private JSpinner           _adminMetadataSpinner;
    private JSpinner           _superuserSpinner;
    
    private final JLabel       _accessDeniedLabel = new JLabel("Accès refusé : droits insuffisants.", SwingConstants.CENTER);

    /**
     * Constructs the global application operational rules tuning view dashboard.
     * 
     * @param vue         the root structural view coordinator window context
     * @param currentUser the identity metadata record of the acting operator
     */
    public SettingsView(MainFrame vue, Librarian currentUser) {
        this._viewController = vue;
        this._currentUser = currentUser;
        
        this.setLayout(new BorderLayout(15, 15));
        this.setBackground(Color.WHITE);
        this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        if (!Settings.hasAccess(currentUser, Settings.PERM_ADMIN_METADATA)) {
            this.renderAccessDeniedLayout();
            return;
        }

        this.buildOperationalFormLayout();
    }

    private void renderAccessDeniedLayout() {
        _accessDeniedLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        _accessDeniedLabel.setForeground(new Color(217, 83, 79));
        this.add(_accessDeniedLabel, BorderLayout.CENTER);
    }

    private void buildOperationalFormLayout() {
        // Headline Banner
        JLabel titleLabel = new JLabel("Paramètres des permissions", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        this.add(titleLabel, BorderLayout.NORTH);

        // Core Parametric Forms Field Matrix Grid
        JPanel fieldsContainerForm = new JPanel(new GridLayout(5, 2, 12, 12));
        fieldsContainerForm.setBackground(Color.WHITE);

        _validateBorrowSpinner = new JSpinner(new SpinnerNumberModel(Settings.PERM_VALIDATE_BORROW, 1, 10, 1));
        _extendLoanSpinner     = new JSpinner(new SpinnerNumberModel(Settings.PERM_EXTEND_LOAN,     1, 10, 1));
        _processReturnSpinner  = new JSpinner(new SpinnerNumberModel(Settings.PERM_PROCESS_RETURN,  1, 10, 1));
        _adminMetadataSpinner  = new JSpinner(new SpinnerNumberModel(Settings.PERM_ADMIN_METADATA,  1, 10, 1));
        _superuserSpinner      = new JSpinner(new SpinnerNumberModel(Settings.PERM_SUPERUSER,       1, 10, 1));

        fieldsContainerForm.add(new JLabel("Valider un emprunt :"));
        fieldsContainerForm.add(_validateBorrowSpinner);
        fieldsContainerForm.add(new JLabel("Prolonger un prêt :"));
        fieldsContainerForm.add(_extendLoanSpinner);
        fieldsContainerForm.add(new JLabel("Traiter un retour :"));
        fieldsContainerForm.add(_processReturnSpinner);
        fieldsContainerForm.add(new JLabel("Admin métadonnées :"));
        fieldsContainerForm.add(_adminMetadataSpinner);
        fieldsContainerForm.add(new JLabel("Superuser :"));
        fieldsContainerForm.add(_superuserSpinner);

        this.add(fieldsContainerForm, BorderLayout.CENTER);

        // Persistent State Serialization Switch Trigger Button
        AccessButton saveActionButton = new AccessButton("Sauvegarder", this::persistSettingsThresholds);
        this.add(saveActionButton, BorderLayout.SOUTH);
    }

    private void persistSettingsThresholds() {
        Settings.PERM_VALIDATE_BORROW = (int) _validateBorrowSpinner.getValue();
        Settings.PERM_EXTEND_LOAN     = (int) _extendLoanSpinner.getValue();
        Settings.PERM_PROCESS_RETURN  = (int) _processReturnSpinner.getValue();
        Settings.PERM_ADMIN_METADATA  = (int) _adminMetadataSpinner.getValue();
        Settings.PERM_SUPERUSER       = (int) _superuserSpinner.getValue();
        
        JOptionPane.showMessageDialog(this, 
                "Paramètres sauvegardés avec succès.", 
                "Succès", 
                JOptionPane.INFORMATION_MESSAGE);
    }
}